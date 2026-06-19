package io.github.probably_oxy.drift.data

/**
 * Global listening-context setting — how the mix is voiced for the output
 * device. Deliberately NOT part of a [Preset] (an existing architecture rule:
 * output mode is a global setting, not saved per-mix).
 *
 * SPEAKER applies a small-speaker voicing (bass lift + treble tame); STEREO and
 * PHONES are both flat pass-through — same as the web app, where the two were
 * identical. They stay distinct labels so the choice reads naturally to users.
 */
enum class OutputMode(val label: String) {
    SPEAKER("SPEAKER"),
    STEREO("STEREO"),
    PHONES("PHONES");

    companion object {
        fun fromName(name: String?): OutputMode =
            entries.firstOrNull { it.name == name } ?: SPEAKER
    }
}
