package io.github.probably_oxy.drift.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

/**
 * Drift is dark-only and uses a fixed cockpit palette — no light theme and no
 * Android 12 dynamic color (which would recolor the phosphor aesthetic). The
 * ColorScheme maps the handful of Material tokens that built-in components fall
 * back to; bespoke composables reference the [Color] palette directly.
 */
private val DriftColorScheme = darkColorScheme(
    background = Bg,
    onBackground = TextDim,
    surface = Surface,
    onSurface = TextDim,
    primary = TextActive,
    onPrimary = Bg,
    secondary = Accent,
    onSecondary = Bg,
    error = Danger,
    onError = Bg,
    outline = Border,
)

@Composable
fun DriftTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DriftColorScheme,
        typography = Typography,
        content = content,
    )
}
