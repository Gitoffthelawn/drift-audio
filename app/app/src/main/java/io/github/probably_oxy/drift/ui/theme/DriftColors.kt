package io.github.probably_oxy.drift.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * The redesigned "starship MFD" palette (Claude Design handoff, 2026-06).
 *
 * Unlike the legacy top-level vals in [Color.kt], colours here are bundled into
 * one [DriftColors] holder and handed down the tree via [LocalDriftColors]. That
 * is the whole point of the "structure now, wire later" theme work: a composable
 * reads `LocalDriftColors.current.greenBright` instead of a hard-coded val, so
 * when we later let the user pick Amber/Ice/Mono we just provide a different
 * instance at the top and the entire UI re-tints with zero edits downstream.
 *
 * Hex values are the design tokens reproduced precisely. rgba() tokens are packed
 * as 0xAARRGGBB (alpha first), e.g. rgba(47,224,74,0.55) -> 0x8C2FE04A.
 */
@Immutable
data class DriftColors(
    // ── surfaces ──────────────────────────────────────────────
    val bg: Color,            // app background
    val cardBg: Color,        // layer card / panel fill (off)
    val cardBgOn: Color,      // layer card fill (active)
    // ── lines / borders ───────────────────────────────────────
    val cardLine: Color,      // default 1px borders / dividers
    val greenLine: Color,     // active / emphasis borders, info-button outline
    // ── greens (primary hue + its ramp) ───────────────────────
    val greenBright: Color,   // primary phosphor: active text, lit VU, glow source
    val greenMid: Color,      // layer title (off), secondary labels
    val greenDim: Color,      // section labels, idle controls
    val greenFaint: Color,    // tertiary text, status line, unlit accents
    val greenGlow: Color,     // strong glow (shadow / blur)
    val greenGlowSoft: Color, // soft glow
    // ── amber accent ──────────────────────────────────────────
    val amber: Color,         // active layer title, VOL value, peak VU, slider value
    val amberGlow: Color,
    // ── text / meter / danger ─────────────────────────────────
    val textDim: Color,       // body / readout text, status-bar text
    val vuOff: Color,         // unlit VU segment
    val red: Color,           // STOP ALL text / icon
    val redBg: Color,         // STOP ALL fill
    val redLine: Color,       // STOP ALL border
)

/** PHOSPHOR — the default green-on-black theme (the only one wired for now). */
val PhosphorColors = DriftColors(
    bg = Color(0xFF060A07),
    cardBg = Color(0x29162A1A),
    cardBgOn = Color(0x291E4828),
    cardLine = Color(0x38408450),
    greenLine = Color(0x8050B464),
    greenBright = Color(0xFF2FE04A),
    greenMid = Color(0xFF3F9A52),
    greenDim = Color(0xFF357A44),
    greenFaint = Color(0xFF2C5436),
    greenGlow = Color(0x8C2FE04A),
    greenGlowSoft = Color(0x472FE04A),
    amber = Color(0xFFD9A82E),
    amberGlow = Color(0x99D9A82E),
    textDim = Color(0xFF5A8A64),
    vuOff = Color(0xFF173A22),
    red = Color(0xFFE0532F),
    redBg = Color(0x38782216),
    redLine = Color(0x8CBE422A),
)

/**
 * The four menu presets. Only [PhosphorColors] has a real [colors] instance today;
 * the others carry their primary swatch hue so the menu's 2x2 grid can render now,
 * with live re-tinting wired in a later checkpoint. Per the handoff, each preset
 * keeps the same dark bg + structure and only swaps the "bright/primary" hue:
 * Phosphor #2FE04A · Amber #D9A82E · Ice #5AD0E0 · Mono #C8D4C8.
 */
enum class ThemePreset(val label: String, val swatch: Color, val colors: DriftColors?) {
    PHOSPHOR("PHOSPHOR", Color(0xFF2FE04A), PhosphorColors),
    AMBER("AMBER", Color(0xFFD9A82E), null),
    ICE("ICE", Color(0xFF5AD0E0), null),
    MONO("MONO", Color(0xFFC8D4C8), null),
}

/** Tree-wide handle for the active palette. Defaults to Phosphor. */
val LocalDriftColors = staticCompositionLocalOf { PhosphorColors }
