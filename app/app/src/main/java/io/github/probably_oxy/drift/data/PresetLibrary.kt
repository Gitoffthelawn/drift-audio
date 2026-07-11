package io.github.probably_oxy.drift.data

/**
 * Built-in starter presets. These are the web app's named mixes, but reduced to
 * layers that are actually playable today: the SYN sounds (forest, propulsion,
 * radio, warp) have no audio yet, so presets that leaned on them are trimmed to
 * their REC layers, and the all-SYN ones (e.g. "Warp Corridor") are omitted.
 * They will fill back out when the synth sounds return as pre-rendered files.
 *
 * variantId is carried for forward-compatibility; variants aren't audibly
 * applied yet (that lands with the output/filter work).
 */
object PresetLibrary {
    val builtIn: List<Preset> = listOf(
        Preset(
            name = "Hull Storm", blurb = "Rain below decks",
            layers = listOf(
                PresetLayer("rain", "below", 0.65f),
                PresetLayer("wind", null, 0.30f),
            ),
        ),
        Preset(
            name = "Planetfall", blurb = "Wind over water",
            layers = listOf(
                PresetLayer("wind", null, 0.22f),
                PresetLayer("brook", null, 0.45f),
            ),
        ),
        Preset(
            name = "Contact", blurb = "Something vast nearby",
            layers = listOf(
                PresetLayer("spaceWhale", "normal", 0.65f),
                PresetLayer("interstellarplasma", "raw", 0.45f),
            ),
        ),
        Preset(
            name = "Green Sector", blurb = "Stream at midday",
            layers = listOf(
                PresetLayer("brook", null, 0.40f),
                PresetLayer("rain", "openair", 0.35f),
            ),
        ),
        Preset(
            name = "Canopy Dark", blurb = "Rain on the hull",
            layers = listOf(
                PresetLayer("rain", "below", 0.45f),
                PresetLayer("brook", null, 0.35f),
            ),
        ),
        Preset(
            name = "Drift Orbit", blurb = "Mars wind, vents humming",
            layers = listOf(
                PresetLayer("marswind", null, 0.35f),
                PresetLayer("lifesupport", null, 0.40f),
            ),
        ),
    )
}
