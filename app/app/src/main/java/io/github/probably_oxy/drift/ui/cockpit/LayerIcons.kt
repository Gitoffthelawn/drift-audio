package io.github.probably_oxy.drift.ui.cockpit

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.probably_oxy.drift.ui.theme.LocalDriftColors

/**
 * Per-sound line glyphs for the Telemetry layer cards, drawn in the design's
 * 1.6dp round-cap style on a 24-unit viewport. The id→glyph mapping lives here
 * (NOT on Sound.kt — readout/icon data is derived, per the Phase 4 decisions).
 *
 * Each sound's glyph is a redraw of its icon from the earlier Web Audio app,
 * except where a prototype glyph fit better: wind = `wave`, interstellarplasma = `target`,
 * thruster = `nozzle` (carried over from the retired `propulsion` id). Approved
 * by oxy 2026-06-21.
 */
@Composable
fun LayerIcon(
    soundId: String,
    active: Boolean,
    modifier: Modifier = Modifier,
    size: Dp = 19.dp,
) {
    val colors = LocalDriftColors.current
    val tint = if (active) colors.greenBright else colors.greenDim
    Canvas(modifier = modifier.size(size)) {
        val draw = ICON_DRAWERS[soundId] ?: ICON_DRAWERS.getValue("_default")
        draw(iconScope(tint))
    }
}

/** Build an [IconScope] in the current [DrawScope], scaled from the 24-unit viewport. */
internal fun DrawScope.iconScope(color: Color): IconScope {
    val s = size.minDimension / 24f
    val stroke = Stroke(width = 1.6f * s, cap = StrokeCap.Round, join = StrokeJoin.Round)
    return IconScope(this, s, color, stroke)
}

/** Thin context handed to each glyph drawer so it can work in 24-unit coordinates. */
internal class IconScope(
    val ds: DrawScope,
    val s: Float,
    val color: Color,
    val stroke: Stroke,
) {
    fun line(x1: Float, y1: Float, x2: Float, y2: Float) {
        ds.drawLine(color, Offset(x1 * s, y1 * s), Offset(x2 * s, y2 * s), stroke.width, stroke.cap)
    }

    fun path(block: Path.() -> Unit) {
        val p = Path().apply(block)
        ds.drawPath(p, color, style = stroke)
    }

    fun circle(cx: Float, cy: Float, r: Float, fill: Boolean = false) {
        if (fill) {
            ds.drawCircle(color, r * s, Offset(cx * s, cy * s))
        } else {
            ds.drawCircle(color, r * s, Offset(cx * s, cy * s), style = stroke)
        }
    }

    /** Stroked rounded rectangle. */
    fun rrect(x: Float, y: Float, w: Float, h: Float, r: Float) {
        ds.drawRoundRect(
            color, Offset(x * s, y * s), Size(w * s, h * s),
            CornerRadius(r * s, r * s), style = stroke,
        )
    }

    /** Stroked arc (no center) around (cx,cy), radius r; angles in degrees, 0°=east, CW. */
    fun arc(cx: Float, cy: Float, r: Float, startDeg: Float, sweepDeg: Float) {
        ds.drawArc(
            color, startDeg, sweepDeg, useCenter = false,
            topLeft = Offset((cx - r) * s, (cy - r) * s),
            size = Size(2 * r * s, 2 * r * s), style = stroke,
        )
    }

    /** Move/line/quad in 24-unit space, scaling to px. */
    fun Path.moveTo24(x: Float, y: Float) = moveTo(x * s, y * s)
    fun Path.lineTo24(x: Float, y: Float) = lineTo(x * s, y * s)
    fun Path.quadTo24(cx: Float, cy: Float, x: Float, y: Float) =
        quadraticTo(cx * s, cy * s, x * s, y * s)
    fun Path.cubicTo24(c1x: Float, c1y: Float, c2x: Float, c2y: Float, x: Float, y: Float) =
        cubicTo(c1x * s, c1y * s, c2x * s, c2y * s, x * s, y * s)
}

private typealias IconDrawer = IconScope.() -> Unit

@Suppress("UNUSED_PARAMETER")
private val ICON_DRAWERS: Map<String, IconDrawer> = mapOf(

    // 01 Rain — three diagonal falling streaks
    "rain" to {
        line(6f, 4f, 4f, 20f)
        line(12f, 4f, 10f, 20f)
        line(18f, 4f, 16f, 20f)
    },

    // 02 Brook — a single gentle water wave
    "brook" to {
        path {
            moveTo24(3f, 13f); quadTo24(9f, 9f, 12f, 13f); quadTo24(15f, 17f, 21f, 13f)
        }
    },

    // 03 Fireplace — flame outline
    "fire" to {
        path {
            moveTo24(3f, 22f); lineTo24(7f, 12f); lineTo24(11f, 16f)
            lineTo24(15f, 5f); lineTo24(18f, 13f); lineTo24(21f, 22f)
        }
    },

    // 04 Wind — three stacked sine lines (prototype `wave`)
    "wind" to {
        path { moveTo24(2f, 8f); quadTo24(5f, 5f, 7f, 8f); quadTo24(9f, 11f, 12f, 8f); quadTo24(15f, 5f, 17f, 8f); quadTo24(19f, 11f, 22f, 8f) }
        path { moveTo24(2f, 12f); quadTo24(5f, 9f, 7f, 12f); quadTo24(9f, 15f, 12f, 12f); quadTo24(15f, 9f, 17f, 12f); quadTo24(19f, 15f, 22f, 12f) }
        path { moveTo24(2f, 16f); quadTo24(5f, 13f, 7f, 16f); quadTo24(9f, 19f, 12f, 16f); quadTo24(15f, 13f, 17f, 16f); quadTo24(19f, 19f, 22f, 16f) }
    },

    // 05 Interstellar Plasma — concentric rings + dot (prototype `target`)
    "interstellarplasma" to {
        circle(12f, 12f, 9f)
        circle(12f, 12f, 5f)
        circle(12f, 12f, 1.3f, fill = true)
    },

    // 06 Mars Wind — wind streaks + a small planet disc
    "marswind" to {
        path { moveTo24(2f, 15f); quadTo24(7f, 11f, 12f, 14f); quadTo24(17f, 17f, 22f, 13f) }
        path { moveTo24(5f, 10f); quadTo24(8f, 8f, 11f, 10f) }
        circle(18f, 7f, 2.5f)
    },

    // 07 Space Whale — whale-tail curve + flukes
    "spaceWhale" to {
        path { moveTo24(2f, 15f); cubicTo24(6f, 8f, 11f, 8f, 14f, 13f); quadTo24(18f, 17f, 22f, 11f) }
        line(19f, 9f, 22f, 11f)
        line(22f, 11f, 19f, 13f)
    },

    // 08 Forest — pine tree
    "forest" to {
        path {
            moveTo24(12f, 3f); lineTo24(5f, 13f); lineTo24(9f, 13f); lineTo24(7f, 19f)
            lineTo24(17f, 19f); lineTo24(15f, 13f); lineTo24(19f, 13f); close()
        }
        line(12f, 19f, 12f, 22f)
    },

    // 09 Thruster — engine nozzle bell + thrust lines (prototype `nozzle` feel)
    "thruster" to {
        path {
            moveTo24(5f, 9f); lineTo24(5f, 15f); lineTo24(14f, 17f); lineTo24(14f, 7f); close()
        }
        line(14f, 9f, 20f, 6f)
        line(14f, 12f, 22f, 12f)
        line(14f, 15f, 20f, 18f)
    },

    // 10 Warp — star + radiating spokes
    "warp" to {
        circle(12f, 12f, 2f)
        line(12f, 2f, 12f, 8f)
        line(12f, 16f, 12f, 22f)
        line(2f, 12f, 8f, 12f)
        line(16f, 12f, 22f, 12f)
        line(5.5f, 5.5f, 8.8f, 8.8f)
        line(15.2f, 15.2f, 18.5f, 18.5f)
        line(18.5f, 5.5f, 15.2f, 8.8f)
        line(8.8f, 15.2f, 5.5f, 18.5f)
    },

    // 11 Radio Chatter — emitter dot + broadcast arcs
    "radio" to {
        circle(12f, 13f, 1.5f, fill = true)
        path { moveTo24(9f, 10f); quadTo24(7f, 13f, 9f, 16f) }
        path { moveTo24(15f, 10f); quadTo24(17f, 13f, 15f, 16f) }
        path { moveTo24(6f, 7f); quadTo24(2f, 13f, 6f, 19f) }
        path { moveTo24(18f, 7f); quadTo24(22f, 13f, 18f, 19f) }
    },

    // fallback — gentle double wave
    "_default" to {
        path { moveTo24(3f, 12f); quadTo24(8f, 8f, 12f, 12f); quadTo24(16f, 16f, 21f, 12f) }
        path { moveTo24(3f, 16f); quadTo24(8f, 12f, 12f, 16f); quadTo24(16f, 20f, 21f, 16f) }
    },
)
