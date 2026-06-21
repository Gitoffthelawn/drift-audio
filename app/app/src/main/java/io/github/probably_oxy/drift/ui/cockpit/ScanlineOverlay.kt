package io.github.probably_oxy.drift.ui.cockpit

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.github.probably_oxy.drift.data.LocalDriftAnim
import io.github.probably_oxy.drift.ui.theme.LocalDriftColors

/**
 * Subtle CRT overlay (Phase 4g): faint static horizontal scanlines + a soft green
 * gradient band sweeping top→bottom (~7s). Purely decorative — draws behind no content
 * and has no pointer input, so it never intercepts touches. The whole overlay is gated
 * by the scanlines animation flag.
 *
 * Place it above the screen content but below the menu drawer.
 */
@Composable
fun ScanlineOverlay(modifier: Modifier = Modifier) {
    val colors = LocalDriftColors.current
    if (!LocalDriftAnim.current.scanlines) return
    val transition = rememberInfiniteTransition(label = "scanline")
    val sweep by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(7000, easing = LinearEasing), RepeatMode.Restart),
        label = "sweep",
    )

    Box(
        modifier
            .fillMaxSize()
            .drawBehind {
                val gap = 3.dp.toPx()
                val line = Color.Black.copy(alpha = 0.045f)
                var y = 0f
                while (y < size.height) {
                    drawLine(line, Offset(0f, y), Offset(size.width, y), 1f)
                    y += gap
                }
                run {
                    val band = size.height * 0.4f
                    val top = sweep * (size.height + band) - band
                    drawRect(
                        brush = Brush.verticalGradient(
                            0f to Color.Transparent,
                            0.5f to colors.greenGlowSoft.copy(alpha = 0.09f),
                            1f to Color.Transparent,
                            startY = top,
                            endY = top + band,
                        ),
                        topLeft = Offset(0f, top),
                        size = Size(size.width, band),
                    )
                }
            },
    )
}
