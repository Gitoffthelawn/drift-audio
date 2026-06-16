package io.github.probably_oxy.drift.data

enum class SoundType { REC, SYN }

enum class License { CC0, PUBLIC_DOMAIN, CC_BY_3_0, CC_BY_4_0, ORIGINAL }

data class LicenseInfo(
    val license: License,
    val author: String? = null,
    val url: String? = null,
    /** Exact credit string to show verbatim; null = no attribution required. */
    val attribution: String? = null,
)

/** One selectable mode of a sound, e.g. Rain's ("openair", "AIR"). */
data class Variant(
    val id: String,
    val label: String = id.uppercase(),
)

/**
 * Placeholder for the future 3-axis tag system (decided, not yet designed).
 * Empty on every sound until the tagging phase.
 */
data class SoundTags(
    val texture: List<String> = emptyList(),
    val mood: List<String> = emptyList(),
    val environment: List<String> = emptyList(),
)

/**
 * One catalogue entry. Pure metadata: no playback state lives here.
 * Deliberately has no category field — grouping comes from tags later.
 */
data class Sound(
    val id: String,
    val name: String,
    val description: String,
    val type: SoundType,
    val license: LicenseInfo,
    /** Number of audio files backing this sound; 0 for SYN sounds (no files yet). */
    val segmentCount: Int,
    val variants: List<Variant> = emptyList(),
    val defaultVariantId: String? = null,
    val tags: SoundTags = SoundTags(),
)
