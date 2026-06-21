package io.github.probably_oxy.drift.ui.cockpit

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.probably_oxy.drift.data.LocalReduceMotion
import io.github.probably_oxy.drift.ui.theme.DriftTheme
import io.github.probably_oxy.drift.ui.theme.JetBrainsMono
import io.github.probably_oxy.drift.ui.theme.LocalDriftColors
import kotlinx.coroutines.delay

/** Immutable snapshot of a typewriter's progress. */
data class TypewriterState(val text: String, val done: Boolean)

/**
 * Drives a "prints in character-by-character" reveal of [full]. While [active] it
 * advances a substring index at ~[cps] chars/sec; when inactive it resets to empty.
 * Honors [LocalReduceMotion] by jumping straight to the full string.
 *
 * Returns a fresh [TypewriterState] each recomposition (the index is the observed state).
 */
@Composable
fun rememberTypewriter(full: String, active: Boolean, cps: Int = 150): TypewriterState {
    val reduceMotion = LocalReduceMotion.current
    var n by remember(full) { mutableIntStateOf(if (active) full.length else 0) }

    LaunchedEffect(full, active, reduceMotion) {
        when {
            !active -> n = 0
            reduceMotion -> n = full.length
            else -> {
                n = 0
                val step = (1000L / cps).coerceAtLeast(1L)
                var i = 0
                while (i < full.length) {
                    delay(step)
                    i++
                    n = i
                }
            }
        }
    }

    val shown = n.coerceIn(0, full.length)
    return TypewriterState(full.take(shown), shown >= full.length)
}

/**
 * Blinking block cursor `█` (1Hz). The infinite transition is always created so the
 * composition shape stays stable regardless of [show]; under reduced-motion the cursor
 * is solid (no blink). Hidden entirely when [show] is false.
 */
@Composable
fun Cursor(show: Boolean = true) {
    val colors = LocalDriftColors.current
    val reduceMotion = LocalReduceMotion.current
    val transition = rememberInfiniteTransition(label = "cursor")
    val phase by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(1000, easing = LinearEasing), RepeatMode.Restart),
        label = "blink",
    )
    val visible = show && (reduceMotion || phase < 0.5f)
    Text(
        text = "█",
        color = if (visible) colors.greenBright else Color.Transparent,
        fontFamily = JetBrainsMono,
        fontSize = 10.5.sp,
    )
}

/**
 * Inline expanding terminal description used by the Sleep / Output panels. Collapsed
 * to zero height when closed; when [open] it expands and types [text] in beneath a
 * hairline divider, trailing a blinking cursor until the line finishes.
 */
@Composable
fun InfoStrip(open: Boolean, text: String, modifier: Modifier = Modifier) {
    val colors = LocalDriftColors.current
    val typed = rememberTypewriter(text, open, cps = 130)

    AnimatedVisibility(
        visible = open,
        enter = expandVertically() + fadeIn(),
        exit = shrinkVertically() + fadeOut(),
        modifier = modifier,
    ) {
        Column(Modifier.padding(top = 12.dp)) {
            Box(Modifier.fillMaxWidth().height(1.dp).background(colors.cardLine))
            Row(Modifier.padding(top = 10.dp)) {
                Text(
                    text = typed.text,
                    color = colors.textDim,
                    fontFamily = JetBrainsMono,
                    fontSize = 10.5.sp,
                    lineHeight = 16.sp,
                )
                Cursor(show = !typed.done)
            }
        }
    }
}

// ── Preview ──────────────────────────────────────────────────────────────────

@Preview(showBackground = true, backgroundColor = 0xFF060A07)
@Composable
private fun InfoStripPreview() {
    DriftTheme {
        Column(
            Modifier
                .background(LocalDriftColors.current.bg)
                .padding(20.dp),
        ) {
            InfoStrip(
                open = true,
                text = "▸ SLEEP TIMER · fades the mix out and stops playback when the countdown reaches zero.",
            )
        }
    }
}
