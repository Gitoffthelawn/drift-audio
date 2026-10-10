package io.github.probably_oxy.drift.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

/**
 * Drift uses its own cockpit palettes — no Android 12 dynamic color (which would
 * recolor the phosphor aesthetic). The ColorScheme maps the handful of Material
 * tokens that built-in components (dialogs, text fields, switches) fall back to;
 * bespoke composables read [LocalDriftColors] directly. The dark themes share the
 * fixed scheme below; the light DAYLIGHT palette gets one derived from its tokens.
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

private fun lightSchemeFor(c: DriftColors) = lightColorScheme(
    background = c.bg,
    onBackground = c.textDim,
    surface = c.cardBg,
    onSurface = c.textDim,
    primary = c.greenBright,
    onPrimary = c.bg,
    secondary = c.amber,
    onSecondary = c.bg,
    error = c.red,
    onError = c.bg,
    outline = c.greenDim,
)

@Composable
fun DriftTheme(
    colors: DriftColors = PhosphorColors,
    content: @Composable () -> Unit,
) {
    // Hand the active palette down the tree; switching preset re-tints the whole UI.
    CompositionLocalProvider(LocalDriftColors provides colors) {
        MaterialTheme(
            colorScheme = if (colors.isLight) lightSchemeFor(colors) else DriftColorScheme,
            typography = Typography,
            content = content,
        )
    }
}
