package io.github.probably_oxy.drift.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

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
fun DriftTheme(
    colors: DriftColors = PhosphorColors,
    content: @Composable () -> Unit,
) {
    // Hand the active palette down the tree. Only Phosphor is provided today; when
    // preset switching is wired, this single argument changes and the whole UI re-tints.
    CompositionLocalProvider(LocalDriftColors provides colors) {
        MaterialTheme(
            colorScheme = DriftColorScheme,
            typography = Typography,
            content = content,
        )
    }
}
