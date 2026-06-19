package io.github.probably_oxy.drift.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * The "Shiv" cockpit palette — a Matrix-green terminal with amber phosphor
 * highlights on near-black, ported verbatim from the web app's style.css.
 * Used directly by composables (not just via MaterialTheme) because the design
 * relies on more roles than a Material ColorScheme exposes.
 */

// ── Core surfaces ───────────────────────────────────────────────────────────
val Bg = Color(0xFF0A0F0A)
val Surface = Color(0xFF111A0E)
val SurfaceActive = Color(0xFF0A2010)
val SurfaceMenu = Color(0xFF0D1A0A)
val Track = Color(0xFF1A2E14)

// ── Borders ─────────────────────────────────────────────────────────────────
val Border = Color(0xFF1E3014)
val BorderActive = Color(0xFF00CC33)
val BorderPanel = Color(0xFF2A3A1A)
val BorderBadge = Color(0xFF3A5828)
val Rule = Color(0xFF1E2E14)

// ── Text ────────────────────────────────────────────────────────────────────
val TextPrimary = Color(0xFFD4A830) // amber — active sound name
val TextDim = Color(0xFF4DD470)     // green — default body
val TextMuted = Color(0xFF2A8A3A)
val TextActive = Color(0xFF00FF41)  // bright matrix green — active accents
val TextBright = Color(0xFF80FF90)
val TextEyebrow = Color(0xFF4A6A30)
val TextSection = Color(0xFF7A9A50)
val TextBadge = Color(0xFF5A8840)
val TextFooter = Color(0xFF3A5028)

// ── Accent ──────────────────────────────────────────────────────────────────
val Accent = Color(0xFFC8A030)

// ── Danger / Stop ─────────────────────────────────────────────────────────────
val Danger = Color(0xFFC05030)
val DangerBg = Color(0xFF2A0A04)
val DangerText = Color(0xFFF07050)
val DangerTextActive = Color(0xFFFF8060)
