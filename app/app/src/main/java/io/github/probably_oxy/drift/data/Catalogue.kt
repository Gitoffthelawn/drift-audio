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

    private val xkeril = LicenseInfo(
        license = License.CC0,
        author = "xkeril",
        url = "https://freesound.org/people/xkeril/sounds/609895/",
    )

    private val sanderboah = LicenseInfo(
        license = License.CC0,
        author = "Sanderboah",
        url = "https://freesound.org/people/Sanderboah/sounds/803852/",
    )

    private val bia12 = LicenseInfo(
        license = License.CC0,
        author = "Bia12",
        url = "https://freesound.org/people/Bia12/sounds/583754/",
    )

    private val fireBlend = LicenseInfo(
        license = License.CC0,
        author = "silencyo & PagDev (Freesound & OpenGameArt)",
    )

    private val wolfgang = LicenseInfo(
        license = License.CC0,
        author = "Wolfgang_ (OpenGameArt)",
        url = "https://opengameart.org/content/crickets-ambient-noise-loopable",
    )

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
            type = SoundType.REC, license = bia12, segmentCount = 2,
        ),
        Sound(
            id = "fire", name = "Fireplace", description = "Crackling fire",
            type = SoundType.REC, license = fireBlend, segmentCount = 4,
        ),
        Sound(
            id = "wind", name = "Wind", description = "Forest wind",
            type = SoundType.REC, license = muges, segmentCount = 3,
        ),
        Sound(
            id = "crickets", name = "Crickets", description = "Night insect chorus",
            type = SoundType.REC, license = wolfgang, segmentCount = 1,
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
            variants = listOf(
                Variant("busy", "BUSY", kind = VariantKind.FREQUENCY),
                Variant(
                    "normal", "NORM", kind = VariantKind.FREQUENCY,
                    minGapMs = 8_000L, maxGapMs = 25_000L,
                ),
                Variant(
                    "rare", "RARE", kind = VariantKind.FREQUENCY,
                    minGapMs = 20_000L, maxGapMs = 90_000L,
                ),
            ),
            defaultVariantId = "busy",
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
            segmentCount = 2,
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
            id = "lifesupport", name = "Life Support",
            description = "Vents and machinery humming below decks",
            type = SoundType.REC, license = xkeril, segmentCount = 3,
        ),
        Sound(
            id = "thruster", name = "Thruster", description = "Ship engine roar",
            type = SoundType.REC, license = sanderboah, segmentCount = 3,
            variants = listOf(
                Variant("busy", "BUSY", kind = VariantKind.FREQUENCY),
                Variant(
                    "rare", "RARE", kind = VariantKind.FREQUENCY,
                    minGapMs = 15_000L, maxGapMs = 60_000L,
                ),
            ),
            defaultVariantId = "busy",
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
            variants = listOf(
                Variant(
                    "busy", "BUSY", kind = VariantKind.FREQUENCY,
                    minGapMs = 0L, maxGapMs = 0L,
                ),
                Variant(
                    "normal", "NORM", kind = VariantKind.FREQUENCY,
                    minGapMs = 10_000L, maxGapMs = 40_000L,
                ),
                Variant(
                    "rare", "RARE", kind = VariantKind.FREQUENCY,
                    minGapMs = 45_000L, maxGapMs = 120_000L,
                ),
            ),
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
