package io.github.probably_oxy.drift.ui.cockpit

import android.graphics.BlurMaskFilter
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.probably_oxy.drift.data.LocalDriftAnim
import kotlinx.coroutines.delay

/**
 * MFD bracket corners — the four L-shaped marks that frame a card/panel like a
 * heads-up display reticle. Ports the prototype's `Brackets` atom: each corner is a
 * small square region inset from the edge, with only its two outward edges stroked.
 *
 * Drawn behind the content so it never affects layout or intercepts touches.
 *
 * @param color  stroke colour — `cardLine` when the frame is idle, `greenLine` when active.
 * @param inset  distance of each bracket from the card edge.
 * @param len    arm length of each bracket.
 * @param strokeWidth  line thickness.
 */
fun Modifier.brackets(
    color: Color,
    inset: Dp = 6.dp,
    len: Dp = 12.dp,
    strokeWidth: Dp = 1.4.dp,
): Modifier = this.drawBehind {
    val i = inset.toPx()
    val l = len.toPx()
    val sw = strokeWidth.toPx()
    val w = size.width
    val h = size.height

    // Each corner draws its horizontal arm then its vertical arm, meeting at the corner point.
    fun bracket(cornerX: Float, cornerY: Float, dirX: Float, dirY: Float) {
        drawLine(color, Offset(cornerX + dirX * l, cornerY), Offset(cornerX, cornerY), sw, StrokeCap.Round)
        drawLine(color, Offset(cornerX, cornerY), Offset(cornerX, cornerY + dirY * l), sw, StrokeCap.Round)
    }

    bracket(i, i, +1f, +1f)             // top-left
    bracket(w - i, i, -1f, +1f)         // top-right
    bracket(i, h - i, +1f, -1f)         // bottom-left
    bracket(w - i, h - i, -1f, -1f)     // bottom-right
}

/**
 * Soft phosphor glow ring around a card/control — the "energised" halo on an active
 * layer. Drawn as a *blurred stroke* tracing the card's rounded outline, so it only
 * adds light at the perimeter and never darkens the centre (unlike `Modifier.shadow`,
 * whose offset elevation shadow showed through the translucent card fill as a dark
 * rectangle). Pulse the [color] alpha at the call site for the breathing effect.
 *
 * BlurMaskFilter blurs on API 28+; below that it degrades to a crisp green ring.
 */
fun Modifier.glow(
    color: Color,
    cornerRadius: Dp = 7.dp,
    blurRadius: Dp = 13.dp,
): Modifier = this.drawBehind {
    drawIntoCanvas { canvas ->
        val paint = Paint()
        paint.asFrameworkPaint().apply {
            isAntiAlias = true
            this.color = color.toArgb()
            style = android.graphics.Paint.Style.STROKE
            strokeWidth = 3.dp.toPx()
            maskFilter = BlurMaskFilter(blurRadius.toPx(), BlurMaskFilter.Blur.NORMAL)
        }
        val cr = cornerRadius.toPx()
        canvas.drawRoundRect(0f, 0f, size.width, size.height, cr, cr, paint)
    }
}

/**
 * One-shot boot entrance: the element fades + translates up into place on first
 * composition, after [delayMillis] (use ascending delays across siblings for a
 * stagger). Disabled (snaps to final state) when the boot-animation flag is off.
 */
@Composable
fun Modifier.entrance(delayMillis: Int = 0): Modifier {
    val enabled = LocalDriftAnim.current.entrance
    var shown by remember { mutableStateOf(!enabled) }
    LaunchedEffect(Unit) {
        if (enabled) {
            delay(delayMillis.toLong())
            shown = true
        }
    }
    val progress by animateFloatAsState(
        targetValue = if (shown) 1f else 0f,
        animationSpec = tween(380, easing = FastOutSlowInEasing),
        label = "entrance",
    )
    return this.graphicsLayer {
        alpha = progress
        translationY = (1f - progress) * 22.dp.toPx()
    }
}
