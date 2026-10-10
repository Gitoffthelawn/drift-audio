package io.github.probably_oxy.drift.audio

import android.net.Uri
import androidx.media3.datasource.RawResourceDataSource
import io.github.probably_oxy.drift.R
import io.github.probably_oxy.drift.data.Sound
import io.github.probably_oxy.drift.data.SoundLibrary

/**
 * The single place that ties [Sound] ids to playable audio: built-in sounds to
 * their res/raw resources, user imports to their copied files (via
 * [SoundLibrary]). Kept apart from the pure-data [io.github.probably_oxy.drift.data]
 * package so that R.raw (an Android-generated reference) never leaks into the
 * catalogue model. The engine asks here for a sound's [FileSoundSource].
 *
 * SYN sounds have no files yet, so they are simply absent from the map and
 * [segmentsFor] returns null for them.
 */
object AudioFiles {

    /** id -> ordered R.raw resource ids, one per segment. */
    private val segments: Map<String, List<Int>> = mapOf(
        "rain" to listOf(R.raw.aud_rain_0, R.raw.aud_rain_1, R.raw.aud_rain_2),
        "brook" to listOf(R.raw.aud_brook_0, R.raw.aud_brook_1),
        "fire" to listOf(R.raw.aud_fire_0, R.raw.aud_fire_1, R.raw.aud_fire_2, R.raw.aud_fire_3),
        "wind" to listOf(R.raw.aud_wind_0, R.raw.aud_wind_1, R.raw.aud_wind_2),
        "crickets" to listOf(R.raw.aud_crickets_0),
        "thunder" to listOf(
            R.raw.aud_thunder_0,
            R.raw.aud_thunder_1,
            R.raw.aud_thunder_2,
            R.raw.aud_thunder_3,
        ),
        // segment 2 dropped: shipped silent in v0.3.1 (process_audio.sh's -ss/-i
        // ordering bug, fixed; original source is lost so it can't be regenerated)
        "interstellarplasma" to listOf(
            R.raw.aud_interstellarplasma_0,
            R.raw.aud_interstellarplasma_1,
        ),
        "marswind" to listOf(R.raw.aud_marswind_0, R.raw.aud_marswind_1, R.raw.aud_marswind_2),
        "lifesupport" to listOf(
            R.raw.aud_lifesupport_0,
            R.raw.aud_lifesupport_1,
            R.raw.aud_lifesupport_2,
        ),
        "spaceWhale" to listOf(R.raw.aud_spacewhale_0, R.raw.aud_spacewhale_1, R.raw.aud_spacewhale_2),
        "thruster" to listOf(R.raw.aud_thruster_0, R.raw.aud_thruster_1, R.raw.aud_thruster_2),
    )

    /** True for sounds that actually have audio to play (built-in or imported). */
    fun isPlayable(sound: Sound): Boolean =
        segments.containsKey(sound.id) || SoundLibrary.customById(sound.id) != null

    /**
     * Build a [FileSoundSource] for [sound], or null if it has no audio files
     * (e.g. a SYN sound, or an import that has since been deleted).
     */
    fun sourceFor(sound: Sound): FileSoundSource? {
        segments[sound.id]?.let { resIds ->
            return FileSoundSource(
                sound,
                resIds.map { Segment(RawResourceDataSource.buildRawResourceUri(it).toString()) },
            )
        }
        val custom = SoundLibrary.customById(sound.id) ?: return null
        return FileSoundSource(
            sound,
            custom.segments.map {
                Segment(Uri.fromFile(SoundLibrary.fileFor(custom, it.file)).toString(), it.startMs, it.endMs)
            },
        )
    }
}
