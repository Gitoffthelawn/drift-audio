package io.github.probably_oxy.drift.audio

import io.github.probably_oxy.drift.data.Variant

/**
 * Contract for one playable sound layer, independent of how the audio is
 * produced. v1 ships [FileSoundSource] only; a future SynthSoundSource
 * implements this same interface without the engine or UI changing.
 *
 * Playback lifecycle (play/stop/prepare) is deliberately absent: it gets
 * added in Phase 2 once Media3 dictates its real shape.
 */
interface SoundSource {
    val id: String
    val displayName: String

    /** Available variants; empty if this sound has none. */
    val variants: List<Variant>

    /** Currently selected variant id, or null if the sound has no variants. */
    var variantId: String?

    /** 0.0–1.0. Implementations clamp out-of-range values. */
    var volume: Float
}
