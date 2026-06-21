package io.github.probably_oxy.drift.ui.cockpit

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

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
 * Soft phosphor glow behind a shape — the "energised" halo on an active card/control.
 *
 * Implemented as a coloured drop shadow (the design's suggested approach). This is the
 * static glow *primitive*; the slow 3.2s glow **pulse** on active cards is animated at
 * the call site in a later checkpoint by varying [color]'s alpha or [radius]. `clip` is
 * left off so the glow never crops the content it sits behind.
 */
fun Modifier.glow(
    color: Color,
    shape: Shape = RoundedCornerShape(7.dp),
    radius: Dp = 16.dp,
): Modifier = this.shadow(
    elevation = radius,
    shape = shape,
    clip = false,
    ambientColor = color,
    spotColor = color,
)
