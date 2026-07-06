package io.github.probably_oxy.drift.audio

import android.media.audiofx.BassBoost
import android.media.audiofx.Equalizer
import io.github.probably_oxy.drift.data.OutputMode

/**
 * Applies the output-mode voicing to the shared audio session that all layer
 * players feed into, using platform [android.media.audiofx] effects. Because
 * every ExoPlayer shares one audio session id, effects attached here process
 * the *combined* mix — the native equivalent of the web app's master chain.
 *
 * SPEAKER lifts bass (BassBoost) and tames treble (top Equalizer band) to
 * approximate the web app's small-speaker voicing; STEREO/PHONES are flat.
 * Every effect is best-effort: some devices don't expose these, so failures are
 * swallowed and playback simply stays flat.
 *
 * Not a faithful clone of the web chain (no software limiter; EQ curve is
 * approximate), but a robust, device-friendly first cut.
 */
class OutputProcessor(private val audioSessionId: Int) {

    private var bassBoost: BassBoost? = null
    private var equalizer: Equalizer? = null

    fun apply(mode: OutputMode) {
        release()
        if (mode != OutputMode.SPEAKER) return // STEREO / PHONES: flat

        runCatching {
            bassBoost = BassBoost(EFFECT_PRIORITY, audioSessionId).apply {
                if (strengthSupported) setStrength(BASS_STRENGTH)
                enabled = true
            }
        }
        runCatching {
            equalizer = Equalizer(EFFECT_PRIORITY, audioSessionId).apply {
                val bands = numberOfBands.toInt()
                if (bands > 0) {
                    // Tame the highest band a touch (the web app's -4 dB high shelf).
                    val min = bandLevelRange[0]
                    setBandLevel((bands - 1).toShort(), maxOf(TREBLE_CUT_MB, min))
                }
                enabled = true
            }
        }
    }

    fun release() {
        runCatching { bassBoost?.release() }
        runCatching { equalizer?.release() }
        bassBoost = null
        equalizer = null
    }

    private companion object {
        const val EFFECT_PRIORITY = 0
        const val BASS_STRENGTH: Short = 600 // 0..1000
        const val TREBLE_CUT_MB: Short = -300 // millibels
    }
}
