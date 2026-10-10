package io.github.probably_oxy.drift.audio

import io.github.probably_oxy.drift.data.Sound

/**
 * One playable stretch of audio: a whole file, or a window of one when a long
 * user import was auto-split. [uri] is a plain string (android.resource:// for
 * built-ins, file:// for imports) so this class stays free of Android types.
 * [endMs] null = play to the end of the file.
 */
data class Segment(
    val uri: String,
    val startMs: Long = 0L,
    val endMs: Long? = null,
)

/**
 * A [SoundSource] backed by looping audio files — built-in res/raw files or the
 * user's own imports. [segments] are resolved by [AudioFiles] so this class
 * stays free of Android dependencies.
 */
class FileSoundSource(
    private val sound: Sound,
    val segments: List<Segment>,
) : SoundSource {

    init {
        require(segments.size == sound.segmentCount) {
            "Sound '${sound.id}' expects ${sound.segmentCount} segments, got ${segments.size}"
        }
    }

    override val id = sound.id
    override val displayName = sound.name
    override val variants = sound.variants

    /** See [Sound.gainTrim]. Mutable for user imports, whose trim is set live. */
    var gainTrim = sound.gainTrim

    override var variantId: String? = sound.defaultVariantId
        set(value) {
            require(value == null || variants.any { it.id == value }) {
                "Unknown variant '$value' for sound '$id'"
            }
            field = value
        }

    override var volume: Float = 1f
        set(value) {
            field = value.coerceIn(0f, 1f)
        }
}
