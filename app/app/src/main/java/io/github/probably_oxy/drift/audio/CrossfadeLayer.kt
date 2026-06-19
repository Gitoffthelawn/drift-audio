package io.github.probably_oxy.drift.audio

import android.content.Context
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

/**
 * One sound's playback as a file-independent crossfade between its segments.
 *
 * Two ExoPlayers ("decks") alternate: one is audible, the other stands by. Every
 * segment file is an independent seamless loop, so a deck loops its segment with
 * REPEAT_MODE_ONE as a safety net. Before a segment would reach its loop seam, we
 * pick the next (random, non-repeating) segment, start it on the standby deck at
 * volume 0, and equal-power crossfade across [CROSSFADE_MS]. The seam is always
 * buried inside the overlap, so it is never heard — regardless of how cleanly the
 * files loop or join. New segments fade in (no pop on a loud start); removal fades
 * out (no hard cut on a loud moment).
 *
 * The crossfade is *triggered by real playback position* (an ExoPlayer
 * [androidx.media3.exoplayer.PlayerMessage] at duration − [CROSSFADE_MS]), not a
 * wall-clock timer, so the WebView crossfade-timer drift bug cannot recur. The
 * volume ramp itself is a short [Handler] loop, which is fine: it runs in a
 * foreground service holding a wake lock, and a late ramp tick is inaudible —
 * worst case a deck simply keeps looping seamlessly, never silence.
 */
class CrossfadeLayer(
    context: Context,
    val source: FileSoundSource,
) {

    private val items: List<MediaItem> = source.segmentResIds.map { resId ->
        MediaItem.fromUri(
            Uri.parse("android.resource://${context.packageName}/$resId"),
        )
    }

    private val handler = Handler(Looper.getMainLooper())

    /** Effective volume the audible deck ramps up to (already includes ducking). */
    private var targetVol: Float = 1f
    private var playing = false
    private var lastSeg = -1
    private var fading = false

    private inner class Deck(val player: ExoPlayer) {
        var crossfadeScheduled = false
        var fade: Runnable? = null

        val listener = object : Player.Listener {
            override fun onPlaybackStateChanged(state: Int) {
                // Once the audible deck is ready (duration known), arm its
                // position-accurate crossfade trigger.
                if (state == Player.STATE_READY &&
                    active === this@Deck &&
                    !crossfadeScheduled &&
                    items.size > 1
                ) {
                    scheduleCrossfade(this@Deck)
                }
            }
        }

        init {
            player.addListener(listener)
        }

        fun loadSegment(seg: Int, startVolume: Float) {
            crossfadeScheduled = false
            player.setMediaItem(items[seg])
            player.repeatMode = Player.REPEAT_MODE_ONE
            player.volume = startVolume
            player.playWhenReady = playing
            player.prepare()
        }

        fun cancelFade() {
            fade?.let(handler::removeCallbacks)
            fade = null
        }

        fun release() {
            cancelFade()
            player.removeListener(listener)
            player.release()
        }
    }

    private val deckA = Deck(buildPlayer(context))
    private val deckB = Deck(buildPlayer(context))
    private var active = deckA

    private fun buildPlayer(context: Context): ExoPlayer =
        ExoPlayer.Builder(context)
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(C.USAGE_MEDIA)
                    .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
                    .build(),
                /* handleAudioFocus = */ false, // focus is owned by the engine
            )
            .setWakeMode(C.WAKE_MODE_LOCAL)
            .build()

    // ── Engine-facing API ────────────────────────────────────────────────────

    fun start(play: Boolean, effectiveVolume: Float) {
        playing = play
        targetVol = effectiveVolume
        val seg = nextSeg()
        active.loadSegment(seg, startVolume = 0f)
        if (play) fadeIn(active)
    }

    fun setPlaying(play: Boolean) {
        if (play == playing) return
        playing = play
        deckA.player.playWhenReady = play
        deckB.player.playWhenReady = play
        // Resuming from a cold/paused start: ease the audible deck back up.
        if (play && !fading && active.player.volume <= 0.001f) fadeIn(active)
    }

    fun setEffectiveVolume(volume: Float) {
        targetVol = volume
        if (!fading) active.player.volume = volume
    }

    /** Fade out, then release both decks. */
    fun releaseWithFadeOut() {
        active.cancelFade()
        val deck = active
        val startVol = deck.player.volume
        ramp(durationMs = REMOVE_FADE_MS) { p ->
            deck.player.volume = startVol * cos(p * HALF_PI)
            if (p >= 1f) release()
        }
    }

    fun release() {
        handler.removeCallbacksAndMessages(null)
        deckA.release()
        deckB.release()
    }

    // ── Crossfade internals ───────────────────────────────────────────────────

    private fun scheduleCrossfade(deck: Deck) {
        val duration = deck.player.duration
        if (duration == C.TIME_UNSET) return
        deck.crossfadeScheduled = true
        val triggerAt = (duration - CROSSFADE_MS).coerceAtLeast(duration / 2)
        deck.player.createMessage { _, _ -> if (active === deck) crossfadeToNext() }
            .setLooper(handler.looper)
            .setPosition(triggerAt)
            .setDeleteAfterDelivery(true)
            .send()
    }

    private fun crossfadeToNext() {
        if (items.size <= 1) return
        val outgoing = active
        val incoming = if (active === deckA) deckB else deckA

        incoming.loadSegment(nextSeg(), startVolume = 0f)
        active = incoming // its READY will arm the next crossfade

        outgoing.cancelFade()
        incoming.cancelFade()
        fading = true
        val outStart = outgoing.player.volume
        val inTarget = targetVol
        ramp(durationMs = CROSSFADE_MS) { p ->
            outgoing.player.volume = outStart * cos(p * HALF_PI) // equal-power out
            incoming.player.volume = inTarget * sin(p * HALF_PI)  // equal-power in
            if (p >= 1f) {
                outgoing.player.volume = 0f
                outgoing.player.playWhenReady = false // park the standby deck
                incoming.player.volume = inTarget
                fading = false
            }
        }
    }

    private fun fadeIn(deck: Deck) {
        deck.cancelFade()
        fading = true
        val target = targetVol
        ramp(durationMs = FADE_IN_MS) { p ->
            deck.player.volume = target * sin(p * HALF_PI)
            if (p >= 1f) {
                deck.player.volume = target
                fading = false
            }
        }
    }

    /** Steps [onProgress] from 0f to 1f over [durationMs] on the main thread. */
    private fun ramp(durationMs: Long, onProgress: (Float) -> Unit) {
        val start = SystemClock.uptimeMillis()
        val runnable = object : Runnable {
            override fun run() {
                val elapsed = SystemClock.uptimeMillis() - start
                val p = (elapsed.toFloat() / durationMs).coerceIn(0f, 1f)
                onProgress(p)
                if (p < 1f) handler.postDelayed(this, STEP_MS)
            }
        }
        handler.post(runnable)
    }

    /** A random segment, never the same as the previous one. */
    private fun nextSeg(): Int {
        if (items.size <= 1) return 0
        var seg = Random.nextInt(items.size)
        if (seg == lastSeg) seg = (seg + 1) % items.size
        lastSeg = seg
        return seg
    }

    private companion object {
        const val CROSSFADE_MS = 2500L
        const val FADE_IN_MS = 1500L
        const val REMOVE_FADE_MS = 800L
        const val STEP_MS = 40L
        const val HALF_PI = (PI / 2).toFloat()
    }
}
