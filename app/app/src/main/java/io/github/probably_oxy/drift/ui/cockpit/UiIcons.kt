package io.github.probably_oxy.drift.ui.cockpit

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.probably_oxy.drift.data.OutputMode

/** Shared cockpit chrome icons (status / sleep / output controls), same 1.6dp line style. */
@Composable
private fun CockpitIcon(
    tint: Color,
    size: Dp,
    modifier: Modifier = Modifier,
    draw: IconScope.() -> Unit,
) {
    Canvas(modifier = modifier.size(size)) { iconScope(tint).draw() }
}

/** Hamburger / menu (3 horizontal lines). */
@Composable
fun IconMenu(tint: Color, modifier: Modifier = Modifier, size: Dp = 26.dp) =
    CockpitIcon(tint, size, modifier) {
        line(4f, 7f, 20f, 7f)
        line(4f, 12f, 20f, 12f)
        line(4f, 17f, 20f, 17f)
    }

/** Speaker + sound waves (MUTE button, not-muted state). */
@Composable
fun IconVolume(tint: Color, modifier: Modifier = Modifier, size: Dp = 18.dp) =
    CockpitIcon(tint, size, modifier) {
        path {
            moveTo24(4f, 9f); lineTo24(4f, 15f); lineTo24(7f, 15f)
            lineTo24(12f, 19f); lineTo24(12f, 5f); lineTo24(7f, 9f); close()
        }
        arc(13f, 12f, 3.5f, -55f, 110f)
        arc(13f, 12f, 6.5f, -58f, 116f)
    }

/** Speaker + ✕ (MUTE button, muted state). */
@Composable
fun IconVolumeMute(tint: Color, modifier: Modifier = Modifier, size: Dp = 18.dp) =
    CockpitIcon(tint, size, modifier) {
        path {
            moveTo24(4f, 9f); lineTo24(4f, 15f); lineTo24(7f, 15f)
            lineTo24(12f, 19f); lineTo24(12f, 5f); lineTo24(7f, 9f); close()
        }
        line(15.5f, 9.5f, 20.5f, 14.5f)
        line(20.5f, 9.5f, 15.5f, 14.5f)
    }

/** Crescent moon (sleep timer). */
@Composable
fun IconMoon(tint: Color, modifier: Modifier = Modifier, size: Dp = 18.dp) =
    CockpitIcon(tint, size, modifier) {
        arc(13.5f, 12f, 8f, 55f, 250f)
    }

/** The output sink icon for a given mode: SPEAKER=phone, STEREO=twin speakers, PHONES=headphones. */
@Composable
fun OutputIcon(mode: OutputMode, tint: Color, modifier: Modifier = Modifier, size: Dp = 20.dp) =
    CockpitIcon(tint, size, modifier) {
        when (mode) {
            OutputMode.SPEAKER -> {
                rrect(7f, 3f, 10f, 18f, 2.6f)
                line(10.4f, 6f, 13.6f, 6f)
                circle(12f, 18f, 0.9f, fill = true)
            }
            OutputMode.STEREO -> {
                rrect(3f, 5f, 7f, 14f, 1.6f)
                rrect(14f, 5f, 7f, 14f, 1.6f)
                circle(6.5f, 13.5f, 2f)
                circle(17.5f, 13.5f, 2f)
                circle(6.5f, 8f, 0.7f, fill = true)
                circle(17.5f, 8f, 0.7f, fill = true)
            }
            OutputMode.PHONES -> {
                arc(12f, 12.5f, 7f, 180f, 180f)
                rrect(3f, 13f, 4.2f, 7.2f, 1.6f)
                rrect(16.8f, 13f, 4.2f, 7.2f, 1.6f)
            }
        }
    }
