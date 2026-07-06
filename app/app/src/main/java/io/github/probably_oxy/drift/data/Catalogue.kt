package io.github.probably_oxy.drift.data

/**
 * The master sound catalogue: every IN APP sound, ported from
 * sounds/CATALOGUE.md and the per-sound LICENSE.txt files.
 * res/raw holds the audio; this holds everything else.
 */
object Catalogue {

    private val muges = LicenseInfo(
        license = License.CC0,
        author = "Muges",
        url = "https://github.com/Muges/ambientsounds",
    )

    private val original = LicenseInfo(license = License.ORIGINAL)

    val sounds: List<Sound> = listOf(

        // ── REC: planetside ──────────────────────────────────────────────
        Sound(
            id = "rain", name = "Rain", description = "Forest rain",
            type = SoundType.REC, license = muges, segmentCount = 3,
            variants = listOf(
                Variant("openair", "AIR"),
                Variant("ondeck", "DECK"),
                Variant("below", "HULL"),
                Variant("distant", "FAR"),
            ),
            defaultVariantId = "openair",
        ),
        Sound(
            id = "brook", name = "Brook", description = "Flowing stream",
            type = SoundType.REC, license = muges, segmentCount = 3,
        ),
        Sound(
            id = "fire", name = "Fireplace", description = "Crackling fire",
            type = SoundType.REC, license = muges, segmentCount = 3,
        ),
        Sound(
            id = "wind", name = "Wind", description = "Forest wind",
            type = SoundType.REC, license = muges, segmentCount = 3,
        ),
        Sound(
            id = "thunder", name = "Thunder", description = "Distant storm claps",
            type = SoundType.REC,
            license = LicenseInfo(
                license = License.CC0,
                author = "elmoustachio & onionbob (Freesound)",
                url = "https://freesound.org",
            ),
            segmentCount = 4,
        ),

        // ── REC: space ───────────────────────────────────────────────────
        Sound(
            id = "interstellarplasma", name = "Interstellar Plasma",
            description = "Voyager 1 plasma waves",
            type = SoundType.REC,
            license = LicenseInfo(
                license = License.PUBLIC_DOMAIN,
                author = "NASA / JPL",
                url = "https://www.nasa.gov/solar-system/nasas-voyager-1-spacecraft-discovers-strange-and-threatening-sounds-of-interstellar-space/",
            ),
            segmentCount = 3,
            variants = listOf(Variant("raw"), Variant("drift")),
            defaultVariantId = "raw",
        ),
        Sound(
            id = "marswind", name = "Mars Wind",
            description = "InSight lander recording",
            type = SoundType.REC,
            license = LicenseInfo(
                license = License.PUBLIC_DOMAIN,
                author = "NASA / JPL-Caltech",
                url = "https://www.nasa.gov/solar-system/nasa-insight-lander-detects-stunning-meteoroid-impact-on-mars/",
            ),
            segmentCount = 3,
        ),
        Sound(
            id = "spaceWhale", name = "Space Whale",
            description = "Something vast nearby",
            type = SoundType.REC,
            license = LicenseInfo(
                license = License.PUBLIC_DOMAIN,
                author = "NOAA / NPS / MBARI",
                url = "https://archive.org/details/WhaleSong_928",
            ),
            segmentCount = 3,
            variants = listOf(Variant("rare"), Variant("normal"), Variant("busy")),
            defaultVariantId = "normal",
        ),

        // ── SYN: no audio files yet (pre-render vs. synthesis deferred) ──
        Sound(
            id = "forest", name = "Forest", description = "Biome ambience",
            type = SoundType.SYN, license = original, segmentCount = 0,
            variants = listOf(Variant("day"), Variant("dusk"), Variant("night")),
            defaultVariantId = "dusk",
        ),
        Sound(
            id = "propulsion", name = "Propulsion", description = "Ship engine drone",
            type = SoundType.SYN, license = original, segmentCount = 0,
            variants = listOf(Variant("idle"), Variant("cruise"), Variant("burn")),
            defaultVariantId = "cruise",
        ),
        Sound(
            id = "warp", name = "Warp", description = "Warp transit hum",
            type = SoundType.SYN, license = original, segmentCount = 0,
            variants = listOf(Variant("engage"), Variant("transit")),
            defaultVariantId = "engage",
        ),
        Sound(
            id = "radio", name = "Radio Chatter", description = "Deep space comms",
            type = SoundType.SYN, license = original, segmentCount = 0,
            variants = listOf(Variant("distant"), Variant("near")),
            defaultVariantId = "distant",
        ),
    )

    fun byId(id: String): Sound? = sounds.find { it.id == id }
}
