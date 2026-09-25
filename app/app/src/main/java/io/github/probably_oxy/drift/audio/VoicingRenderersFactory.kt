package io.github.probably_oxy.drift.audio

import android.content.Context
import androidx.media3.exoplayer.DefaultRenderersFactory
import androidx.media3.exoplayer.audio.AudioSink
import androidx.media3.exoplayer.audio.DefaultAudioSink
import androidx.media3.exoplayer.audio.DefaultAudioTrackBufferSizeProvider

/**
 * Installs a single [VoicingAudioProcessor] into an ExoPlayer's audio pipeline
 * so gain trim and SPEAKER voicing run on the decoded samples themselves,
 * rather than via platform [android.media.audiofx] effects on the shared
 * session (see [VoicingAudioProcessor] for why).
 */
class VoicingRenderersFactory(
    context: Context,
    private val voicingProcessor: VoicingAudioProcessor,
) : DefaultRenderersFactory(context) {

    override fun buildAudioSink(
        context: Context,
        enableFloatOutput: Boolean,
        enableAudioTrackPlaybackParams: Boolean,
    ): AudioSink =
        DefaultAudioSink.Builder(context)
            .setAudioProcessorChain(DefaultAudioSink.DefaultAudioProcessorChain(voicingProcessor))
            .setEnableFloatOutput(enableFloatOutput)
            .setEnableAudioTrackPlaybackParams(enableAudioTrackPlaybackParams)
            // Battery (GitHub #12): ExoPlayer's default PCM buffer is
            // clamp(4 x AudioTrack.getMinBufferSize, 250ms, 750ms) — sized for
            // low-latency media. Drift plays ambient loops where latency is
            // irrelevant, and it runs one AudioTrack per active layer, so every
            // layer pays its own refill wakeup at that cadence. A larger buffer
            // means the same bytes are delivered in fewer, bigger bursts, so the
            // CPU wakes less often per layer.
            //
            // NOTE: the *floor* is the knob that matters. Raising only
            // setMaxPcmBufferDurationUs is a no-op, because for mono 16-bit
            // content 4 x getMinBufferSize is already well under the 750ms
            // ceiling — the max is not what binds. Setting min == max pins the
            // buffer to exactly this duration on every device.
            //
            // This reduces how often work happens, not how much work happens
            // per second: the MP3 decode and the per-sample voicing still run
            // continuously. Removing that needs the decode-once/PCM design.
            .setAudioTrackBufferSizeProvider(
                DefaultAudioTrackBufferSizeProvider.Builder()
                    .setMinPcmBufferDurationUs(PCM_BUFFER_US)
                    .setMaxPcmBufferDurationUs(PCM_BUFFER_US)
                    .build(),
            )
            .build()

    private companion object {
        /**
         * 2s of PCM per AudioTrack. Chosen as a deliberate middle ground: ~8x
         * the default floor, but still small enough that a volume change (the
         * crossfade ramp, the user's slider) stays responsive. Fades are applied
         * via ExoPlayer's volume, which the platform mixes in as samples are
         * consumed rather than baking it into queued bytes — so a bigger buffer
         * should not smear a crossfade. That is an assumption, not a documented
         * guarantee, so it was verified by ear on a OnePlus CPH2653 (Android 16)
         * on 2026-09-25: slider changes are immediate, crossfades across several
         * segment boundaries are clean, add/remove fades are smooth, and output
         * mode switches are clean. If a future device disagrees — laggy fades, or
         * the seam artefacts of GitHub #6/#9 returning — suspect this value first
         * and re-test at 500_000 before looking anywhere else.
         */
        const val PCM_BUFFER_US = 2_000_000
    }
}
