package io.github.probably_oxy.drift.audio

import android.content.Context
import android.media.AudioManager
import android.media.AudioAttributes as PlatformAudioAttributes
import android.media.AudioFocusRequest
import android.net.Uri
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer

/**
 * Phase 2c: the multi-layer mixer. Holds one [ExoPlayer] per active sound and
 * mixes them through the system audio output. Each layer loops its first
 * segment for now; gapless concatenation of all three segments is Phase 2d.
 *
 * Audio focus is handled ONCE here for the whole app, not per player. With one
 * player per layer, letting each request focus would make them steal it from
 * one another (a same-app focus request sends LOSS to the previous owner), so
 * every layer player is built with handleAudioFocus = false and this class owns
 * a single [AudioFocusRequest] for the group. Players actually sound only when
 * the user wants playback ([masterPlaying]) AND we currently hold focus.
 *
 * [onStateChanged] fires after any change so the [MixerPlayer] can refresh what
 * the MediaSession/notification shows.
 */
class PlaybackEngine(private val context: Context) {

    private class Layer(val source: FileSoundSource, val player: ExoPlayer)

    private val layers = LinkedHashMap<String, Layer>()

    /** User intent to play the mix. Actual sound also requires [hasFocus]. */
    private var masterPlaying = false
    private var hasFocus = false

    /** 1.0 normally; lowered while another app transiently ducks us. */
    private var duckFactor = 1f

    /** Called on the app main thread after any state change. */
    var onStateChanged: (() -> Unit)? = null

    private val audioManager =
        context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    private val focusRequest: AudioFocusRequest =
        AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
            .setAudioAttributes(
                PlatformAudioAttributes.Builder()
                    .setUsage(PlatformAudioAttributes.USAGE_MEDIA)
                    .setContentType(PlatformAudioAttributes.CONTENT_TYPE_MUSIC)
                    .build(),
            )
            .setOnAudioFocusChangeListener(::onFocusChange)
            .build()

    // ── Public state the MixerPlayer reads ──────────────────────────────────

    /** True when the mix is audibly playing right now. */
    val isPlaying: Boolean get() = masterPlaying && hasFocus && layers.isNotEmpty()

    /** True when at least one layer is selected (mix is non-empty). */
    val hasLayers: Boolean get() = layers.isNotEmpty()

    fun isLayerActive(id: String): Boolean = layers.containsKey(id)

    fun activeLayerIds(): Set<String> = layers.keys.toSet()

    // ── Layer management ────────────────────────────────────────────────────

    /** Add the layer if absent, remove it if present. Returns now-active?. */
    fun toggleLayer(source: FileSoundSource): Boolean {
        return if (layers.containsKey(source.id)) {
            removeLayer(source.id)
            false
        } else {
            addLayer(source)
            true
        }
    }

    fun addLayer(source: FileSoundSource) {
        if (layers.containsKey(source.id)) return

        // First active layer turns playback intent on (tap-to-play UX).
        if (layers.isEmpty()) masterPlaying = true

        val player = ExoPlayer.Builder(context)
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(C.USAGE_MEDIA)
                    .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
                    .build(),
                /* handleAudioFocus = */ false, // focus is owned by the engine
            )
            // Hold a partial wake lock while playing so the CPU doesn't sleep
            // mid-loop with the screen off (and under battery savers).
            .setWakeMode(C.WAKE_MODE_LOCAL)
            .build()
            .apply {
                val resId = source.segmentResIds.first() // 2d: concat all segments
                setMediaItem(MediaItem.fromUri(rawUri(resId)))
                repeatMode = Player.REPEAT_MODE_ALL
                volume = source.volume * duckFactor
                prepare()
            }

        // Insert BEFORE deciding play state: isPlaying depends on the layer
        // count, so the new player must already be in the map to start.
        layers[source.id] = Layer(source, player)
        ensureFocus()
        applyPlayWhenReady()
        onStateChanged?.invoke()
    }

    fun removeLayer(id: String) {
        val layer = layers.remove(id) ?: return
        layer.player.release()
        if (layers.isEmpty()) {
            masterPlaying = false
            abandonFocus()
        }
        onStateChanged?.invoke()
    }

    fun setVolume(id: String, volume: Float) {
        val layer = layers[id] ?: return
        layer.source.volume = volume
        layer.player.volume = layer.source.volume * duckFactor
        onStateChanged?.invoke()
    }

    // ── Global transport (drives the notification play/pause) ────────────────

    fun setMasterPlaying(playing: Boolean) {
        if (playing == masterPlaying) return
        masterPlaying = playing
        if (playing) {
            hasFocus = if (layers.isNotEmpty()) requestFocus() else false
        } else {
            abandonFocus()
        }
        applyPlayWhenReady()
        onStateChanged?.invoke()
    }

    /** Stop everything and release. Tears the mix down to empty. */
    fun stopAll() {
        masterPlaying = false
        abandonFocus()
        layers.values.forEach { it.player.release() }
        layers.clear()
        onStateChanged?.invoke()
    }

    fun release() {
        layers.values.forEach { it.player.release() }
        layers.clear()
        abandonFocus()
        onStateChanged = null
    }

    // ── Internals ───────────────────────────────────────────────────────────

    private fun rawUri(resId: Int): Uri =
        Uri.parse("android.resource://${context.packageName}/$resId")

    private fun applyPlayWhenReady() {
        val play = isPlaying
        layers.values.forEach { it.player.playWhenReady = play }
    }

    private fun applyVolume() {
        layers.values.forEach { it.player.volume = it.source.volume * duckFactor }
    }

    /** Acquire focus if we want playback, have layers, and don't hold it yet. */
    private fun ensureFocus() {
        if (masterPlaying && layers.isNotEmpty() && !hasFocus) {
            hasFocus = requestFocus()
        }
    }

    private fun requestFocus(): Boolean =
        audioManager.requestAudioFocus(focusRequest) ==
            AudioManager.AUDIOFOCUS_REQUEST_GRANTED

    private fun abandonFocus() {
        if (hasFocus) {
            audioManager.abandonAudioFocusRequest(focusRequest)
            hasFocus = false
        }
    }

    private fun onFocusChange(change: Int) {
        when (change) {
            AudioManager.AUDIOFOCUS_GAIN -> {
                hasFocus = true
                duckFactor = 1f
                applyVolume()
                applyPlayWhenReady()
            }
            AudioManager.AUDIOFOCUS_LOSS -> {
                // Permanent: another app took over. Stay paused, drop intent.
                hasFocus = false
                masterPlaying = false
                applyPlayWhenReady()
            }
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> {
                // Temporary (e.g. a call): pause but keep intent to resume.
                hasFocus = false
                applyPlayWhenReady()
            }
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK -> {
                duckFactor = 0.3f
                applyVolume()
            }
        }
        onStateChanged?.invoke()
    }
}
