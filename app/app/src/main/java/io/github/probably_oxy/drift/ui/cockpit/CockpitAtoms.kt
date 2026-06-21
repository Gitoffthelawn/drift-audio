package io.github.probably_oxy.drift.ui.cockpit

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.probably_oxy.drift.data.LocalReduceMotion
import io.github.probably_oxy.drift.ui.theme.DriftTheme
import io.github.probably_oxy.drift.ui.theme.InfoSerif
import io.github.probably_oxy.drift.ui.theme.JetBrainsMono
import io.github.probably_oxy.drift.ui.theme.LocalDriftColors
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

/**
 * Terminal ASCII spinner — the `\ | / -` cycle that signals "running/active".
 *
 * NB: this deliberately keeps the original terminal frames, **not** the design's
 * Braille frames — oxy prefers the terminal spinner (locked decision). When inactive
 * (or reduced-motion) it shows a static dot `•` and never animates.
 *
 * @param boxed  wrap the glyph in a small bordered slot (the Status panel indicator).
 */
@Composable
fun AsciiSpinner(
    active: Boolean,
    modifier: Modifier = Modifier,
    fontSize: androidx.compose.ui.unit.TextUnit = 13.sp,
    boxed: Boolean = false,
    boxSize: Dp = 27.dp,
) {
    val colors = LocalDriftColors.current
    val reduceMotion = LocalReduceMotion.current
    var frame by remember { mutableIntStateOf(0) }

    LaunchedEffect(active, reduceMotion) {
        if (!active || reduceMotion) return@LaunchedEffect
        while (true) {
            delay(SPINNER_INTERVAL_MS)
            frame = (frame + 1) % SPINNER_FRAMES.size
        }
    }

    val glyph = if (active) SPINNER_FRAMES[frame % SPINNER_FRAMES.size] else "•"
    val glyphColor = if (active) colors.greenBright else colors.greenFaint

    val text = @Composable {
        androidx.compose.material3.Text(
            text = glyph,
            color = glyphColor,
            fontFamily = JetBrainsMono,
            fontSize = fontSize,
            textAlign = TextAlign.Center,
        )
    }

    if (boxed) {
        Box(
            modifier = modifier
                .size(boxSize)
                .border(1.dp, if (active) colors.greenLine else colors.greenFaint, RoundedCornerShape(3.dp)),
            contentAlignment = Alignment.Center,
        ) { text() }
    } else {
        Box(modifier, contentAlignment = Alignment.Center) { text() }
    }
}

/**
 * Segmented VU meter — and the per-layer **volume control**. 14 thin segments; the
 * lit count is `round(value * 14)`. The top three lit segments read "hot" (amber),
 * the rest bright green when active / dim green when off.
 *
 * When [onValueChange] is supplied, a horizontal tap or drag across the strip sets the
 * value (clamped 0.05–1) and consumes the gesture so it never toggles the parent card.
 *
 * The top lit segment flickers (0.4↔1, ~0.5s, stepped) when active — gated by
 * [LocalReduceMotion]. The infinite transition is always created (so composition shape
 * is stable) but only consulted while active & motion is allowed.
 */
@Composable
fun VuMeter(
    value: Float,
    active: Boolean,
    modifier: Modifier = Modifier,
    segments: Int = 14,
    height: Dp = 18.dp,
    onValueChange: ((Float) -> Unit)? = null,
) {
    val colors = LocalDriftColors.current
    val reduceMotion = LocalReduceMotion.current
    val lit = (value.coerceIn(0f, 1f) * segments).roundToInt()

    val transition = rememberInfiniteTransition(label = "vu")
    val flickerPhase by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(500, easing = LinearEasing), RepeatMode.Reverse),
        label = "vuFlicker",
    )
    // Stepped (square-wave) flicker: the top segment is dimmed for half the cycle.
    val topDimmed = active && !reduceMotion && flickerPhase > 0.5f

    BoxWithConstraints(modifier.fillMaxWidth().height(height)) {
        val widthPx = with(LocalDensity.current) { maxWidth.toPx() }
        val gesture = if (onValueChange != null) {
            Modifier
                .pointerInput(Unit) {
                    detectTapGestures { onValueChange((it.x / widthPx).coerceIn(0.05f, 1f)) }
                }
                .pointerInput(Unit) {
                    detectHorizontalDragGestures { change, _ ->
                        onValueChange((change.position.x / widthPx).coerceIn(0.05f, 1f))
                    }
                }
        } else {
            Modifier
        }

        Row(
            modifier = Modifier.fillMaxSize().then(gesture),
            horizontalArrangement = Arrangement.spacedBy(3.dp),
        ) {
            for (i in 0 until segments) {
                val on = i < lit
                val hot = i >= segments - 3
                val isTop = i == lit - 1
                val litColor = if (active) (if (hot) colors.amber else colors.greenBright) else colors.greenDim
                var alpha = if (on) (if (active) 1f else 0.72f) else 0.5f
                if (on && isTop && topDimmed) alpha = 0.4f
                val segColor = (if (on) litColor else colors.vuOff).copy(alpha = alpha)
                Box(
                    Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(1.dp))
                        .background(segColor),
                )
            }
        }
    }
}

/**
 * The square `(i)` info button — a 22dp bracketed slot with a serif-italic lowercase
 * `i`. A separate hit target from the card body: tapping it reveals the readout, never
 * toggles the layer.
 */
@Composable
fun InfoButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    val colors = LocalDriftColors.current
    Box(
        modifier = modifier
            .size(22.dp)
            .clip(RoundedCornerShape(3.dp))
            .border(1.dp, colors.greenLine, RoundedCornerShape(3.dp))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        androidx.compose.material3.Text(
            text = "i",
            color = colors.greenMid,
            fontFamily = InfoSerif,
            fontStyle = FontStyle.Italic,
            fontSize = 13.sp,
        )
    }
}

/** Small REC / SYN kind badge. */
@Composable
fun Badge(text: String, active: Boolean, modifier: Modifier = Modifier) {
    val colors = LocalDriftColors.current
    val line = if (active) colors.greenLine else colors.greenFaint
    androidx.compose.material3.Text(
        text = text,
        color = if (active) colors.greenMid else colors.greenFaint,
        fontFamily = JetBrainsMono,
        fontSize = 9.5.sp,
        letterSpacing = 1.5.sp,
        modifier = modifier
            .clip(RoundedCornerShape(3.dp))
            .border(1.dp, line, RoundedCornerShape(3.dp))
            .padding(horizontal = 5.dp, vertical = 2.dp),
    )
}

/** Terminal spinner frames — original `\ | / -`, faster than the shipped 250ms cadence. */
private val SPINNER_FRAMES = listOf("\\", "|", "/", "-")
private const val SPINNER_INTERVAL_MS = 120L

// ── Previews ─────────────────────────────────────────────────────────────────

@Preview(showBackground = true, backgroundColor = 0xFF060A07)
@Composable
private fun AtomsPreview() {
    DriftTheme {
        Column(
            Modifier
                .background(LocalDriftColors.current.bg)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                AsciiSpinner(active = true, boxed = true)
                AsciiSpinner(active = false, boxed = true)
                AsciiSpinner(active = true)
                Badge("REC", active = true)
                Badge("SYN", active = false)
                InfoButton(onClick = {})
            }
            Spacer(Modifier.height(2.dp))
            VuMeter(value = 0.72f, active = true, onValueChange = {})
            VuMeter(value = 0.45f, active = false, onValueChange = {})
            VuMeter(value = 0.95f, active = true, onValueChange = {})
        }
    }
}
