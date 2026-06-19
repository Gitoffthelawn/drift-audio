package io.github.probably_oxy.drift.audio

import android.content.Context
import android.media.AudioManager
import android.media.AudioAttributes as PlatformAudioAttributes
import android.media.AudioFocusRequest

/**
 * The multi-layer mixer. Owns one [CrossfadeLayer] per active sound and mixes
 * them through the system audio output. Each layer hides its segment seams with
 * an equal-power crossfade (see [CrossfadeLayer]); this class is responsible for
 * the *set* of layers, global play/pause, volume, and audio focus.
 *
 * Audio focus is handled ONCE here for the whole app, not per player. With one
 * player per layer (two, with crossfade), letting each request focus would make
 * them steal it from one another (a same-app focus request sends LOSS to the
 * previous owner), so every layer player is built with handleAudioFocus = false
 * and this class owns a single [AudioFocusRequest] for the group. Layers are
 * audible only when the user wants playback ([masterPlaying]) AND we hold focus.
 *
 * [onStateChanged] fires after any change so the [MixerPlayer] can refresh what
 * the MediaSession/notification shows.
 */
class PlaybackEngine(private val context: Context) {

    private val layers = LinkedHashMap<String, CrossfadeLayer>()

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

        val layer = CrossfadeLayer(context, source)

        // Insert and acquire focus BEFORE starting: isPlaying depends on the
        // layer count and focus, so both must be settled before the layer fades in.
        layers[source.id] = layer
        ensureFocus()
        layer.start(play = isPlaying, effectiveVolume = source.volume * duckFactor)
        onStateChanged?.invoke()
    }

    fun removeLayer(id: String) {
        val layer = layers.remove(id) ?: return
        layer.releaseWithFadeOut() // ease out rather than hard-cutting a loud moment
        if (layers.isEmpty()) {
            masterPlaying = false
            abandonFocus()
        }
        onStateChanged?.invoke()
    }

    fun setVolume(id: String, volume: Float) {
        val layer = layers[id] ?: return
        layer.source.volume = volume
        layer.setEffectiveVolume(volume * duckFactor)
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
        layers.values.forEach { it.release() }
        layers.clear()
        onStateChanged?.invoke()
    }

    fun release() {
        layers.values.forEach { it.release() }
        layers.clear()
        abandonFocus()
        onStateChanged = null
    }

    // ── Internals ───────────────────────────────────────────────────────────

    private fun applyPlayWhenReady() {
        val play = isPlaying
        layers.values.forEach { it.setPlaying(play) }
    }

    private fun applyVolume() {
        layers.values.forEach { it.setEffectiveVolume(it.source.volume * duckFactor) }
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
