package io.github.probably_oxy.drift.audio

import io.github.probably_oxy.drift.R
import io.github.probably_oxy.drift.data.Sound

/**
 * The single place that ties catalogue [Sound] ids to their res/raw audio
 * resources. Kept apart from the pure-data [io.github.probably_oxy.drift.data]
 * package so that R.raw (an Android-generated reference) never leaks into the
 * catalogue model. The engine asks here for a sound's segment resource ids.
 *
 * SYN sounds have no files yet, so they are simply absent from the map and
 * [segmentsFor] returns null for them.
 */
object AudioFiles {

    /** id -> ordered R.raw resource ids, one per segment. */
    private val segments: Map<String, List<Int>> = mapOf(
        "rain" to listOf(R.raw.aud_rain_0, R.raw.aud_rain_1, R.raw.aud_rain_2),
        "brook" to listOf(R.raw.aud_brook_0, R.raw.aud_brook_1, R.raw.aud_brook_2),
        "fire" to listOf(R.raw.aud_fire_0, R.raw.aud_fire_1, R.raw.aud_fire_2),
        "wind" to listOf(R.raw.aud_wind_0, R.raw.aud_wind_1, R.raw.aud_wind_2),
        "interstellarplasma" to listOf(
            R.raw.aud_interstellarplasma_0,
            R.raw.aud_interstellarplasma_1,
            R.raw.aud_interstellarplasma_2,
        ),
        "marswind" to listOf(R.raw.aud_marswind_0, R.raw.aud_marswind_1, R.raw.aud_marswind_2),
        "spaceWhale" to listOf(R.raw.aud_spacewhale_0, R.raw.aud_spacewhale_1, R.raw.aud_spacewhale_2),
    )

    /** Resource ids for [soundId]'s segments, or null if it has no files. */
    fun segmentsFor(soundId: String): List<Int>? = segments[soundId]

    /** True for catalogue sounds that actually have audio to play. */
    fun isPlayable(sound: Sound): Boolean = segments.containsKey(sound.id)

    /**
     * Build a [FileSoundSource] for [sound], or null if it has no audio files
     * (e.g. a SYN sound). The source carries every segment so 2d's gapless
     * concatenation needs no rewiring; for now the engine loops segment 0.
     */
    fun sourceFor(sound: Sound): FileSoundSource? {
        val resIds = segments[sound.id] ?: return null
        return FileSoundSource(sound, resIds)
    }
}
