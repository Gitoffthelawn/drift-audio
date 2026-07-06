package io.github.probably_oxy.drift.data

import kotlinx.serialization.Serializable

/**
 * One sound layer inside a preset. References the sound by id (not by
 * [Sound] object) so saved preset JSON survives catalogue changes.
 */
@Serializable
data class PresetLayer(
    val soundId: String,
    val variantId: String? = null,
    val volume: Float,
)

/**
 * A saved mix. Deliberately excludes output mode: that is a global
 * listening-context setting, never part of a preset.
 */
@Serializable
data class Preset(
    val name: String,
    val blurb: String = "",
    val layers: List<PresetLayer>,
)
