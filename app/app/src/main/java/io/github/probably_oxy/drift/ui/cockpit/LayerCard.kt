package io.github.probably_oxy.drift.ui.cockpit

import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.probably_oxy.drift.data.Catalogue
import io.github.probably_oxy.drift.data.LocalReduceMotion
import io.github.probably_oxy.drift.data.License
import io.github.probably_oxy.drift.data.Sound
import io.github.probably_oxy.drift.ui.theme.DriftTheme
import io.github.probably_oxy.drift.ui.theme.JetBrainsMono
import io.github.probably_oxy.drift.ui.theme.LocalDriftColors
import kotlin.math.floor

/**
 * Telemetry layer card (Phase 4c front + 4d info readout). Constant ~138dp height
 * across off / active / info — the shell never resizes.
 *
 * Controlled: the parent owns which card (if any) shows its readout. [showInfo]
 * drives the front↔readout swap; [onInfo] is the toggle (the (i) opens it; tapping
 * `‹ BACK` or the readout body calls it again to return).
 *
 * @param channel  1-based catalogue index, e.g. 3 → "CH 03".
 * @param active   on AND not globally muted — drives the energised visual state.
 */
@Composable
fun LayerCard(
    sound: Sound,
    channel: Int,
    on: Boolean,
    volume: Float,
    active: Boolean,
    showInfo: Boolean,
    onToggle: () -> Unit,
    onVolume: (Float) -> Unit,
    onInfo: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = LocalDriftColors.current
    val reduceMotion = LocalReduceMotion.current
    val shape = RoundedCornerShape(7.dp)

    // Active glow pulse (3.2s), gated by reduced-motion.
    val pulse = rememberInfiniteTransition(label = "cardPulse")
    val pulseAlpha by pulse.animateFloat(
        initialValue = 0.28f,
        targetValue = 0.55f,
        animationSpec = infiniteRepeatable(tween(3200, easing = LinearEasing), RepeatMode.Reverse),
        label = "pulseAlpha",
    )
    val glowColor = colors.greenGlow.copy(alpha = if (reduceMotion) 0.4f else pulseAlpha)

    val borderColor = if (active) colors.greenBright else colors.cardLine
    val bracketColor = if (active) colors.greenLine else colors.greenFaint

    // Swap animation: front fades (140ms); readout wipes in left→right (stepped, 400ms).
    val frontAlpha by animateFloatAsState(
        targetValue = if (showInfo) 0f else 1f,
        animationSpec = tween(140, easing = LinearEasing),
        label = "frontAlpha",
    )
    val reveal by animateFloatAsState(
        targetValue = if (showInfo) 1f else 0f,
        animationSpec = tween(400, easing = if (reduceMotion) LinearEasing else SteppedReveal),
        label = "readoutReveal",
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(138.dp)
            .then(if (active) Modifier.glow(glowColor, shape) else Modifier)
            .clip(shape)
            .background(if (active) colors.cardBgOn else colors.cardBg)
            .border(if (active) 1.5.dp else 1.dp, borderColor, shape)
            .brackets(bracketColor),
    ) {
        // ── Front face ──────────────────────────────────────────
        if (frontAlpha > 0f) {
            CardFront(
                sound = sound,
                channel = channel,
                volume = volume,
                active = active,
                enabled = !showInfo,
                onToggle = onToggle,
                onVolume = onVolume,
                onInfo = onInfo,
                modifier = Modifier.fillMaxSize().alpha(frontAlpha),
            )
        }
        // ── Info readout ────────────────────────────────────────
        if (reveal > 0f) {
            CardReadout(
                sound = sound,
                channel = channel,
                volume = volume,
                active = showInfo,
                enabled = showInfo,
                onBack = onInfo,
                modifier = Modifier
                    .fillMaxSize()
                    .drawWithContent { clipRect(right = size.width * reveal) { this@drawWithContent.drawContent() } },
            )
        }
    }
}

@Composable
private fun CardFront(
    sound: Sound,
    channel: Int,
    volume: Float,
    active: Boolean,
    enabled: Boolean,
    onToggle: () -> Unit,
    onVolume: (Float) -> Unit,
    onInfo: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = LocalDriftColors.current
    Column(
        modifier = modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                enabled = enabled,
                onClick = onToggle,
            )
            .padding(horizontal = 18.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(13.dp, Alignment.CenterVertically),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            LayerIcon(soundId = sound.id, active = active)
            Spacer(Modifier.width(11.dp))
            Text(
                text = sound.name,
                color = if (active) colors.amber else colors.greenMid,
                fontFamily = JetBrainsMono,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                letterSpacing = 0.3.sp,
            )
            Spacer(Modifier.weight(1f))
            InfoButton(onClick = onInfo)
        }

        VuMeter(value = volume, active = active, onValueChange = onVolume)

        Row(verticalAlignment = Alignment.CenterVertically) {
            AsciiSpinner(active = active, fontSize = 11.sp)
            Spacer(Modifier.width(8.dp))
            Text(
                text = buildAnnotatedString {
                    append("CH ")
                    append(channel.toString().padStart(2, '0'))
                    append(" · ")
                    append(sound.type.name)
                    append(" · VOL ")
                    withStyle(SpanStyle(color = if (active) colors.amber else colors.greenFaint)) {
                        append("${(volume.coerceIn(0f, 1f) * 100).toInt()}%")
                    }
                },
                color = colors.greenFaint,
                fontFamily = JetBrainsMono,
                fontSize = 10.sp,
                letterSpacing = 1.sp,
            )
        }
    }
}

@Composable
private fun CardReadout(
    sound: Sound,
    channel: Int,
    volume: Float,
    active: Boolean,
    enabled: Boolean,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = LocalDriftColors.current
    val ch = channel.toString().padStart(2, '0')
    val pct = (volume.coerceIn(0f, 1f) * 100).toInt()
    val body =
        "SRC   ${sourceOf(sound)}\n" +
            "CH $ch · ${sound.type.name} · VOL $pct%\n\n" +
            sound.description
    val typed = rememberTypewriter(body, active, cps = 150)

    Column(
        modifier = modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                enabled = enabled,
                onClick = onBack,
            )
            .padding(horizontal = 18.dp, vertical = 13.dp),
        verticalArrangement = Arrangement.spacedBy(7.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "▸ ${sound.name.uppercase()}",
                color = colors.greenBright,
                fontFamily = JetBrainsMono,
                fontSize = 11.sp,
                letterSpacing = 2.sp,
            )
            BackButton(onClick = onBack)
        }
        Row {
            Text(
                text = typed.text,
                color = colors.textDim,
                fontFamily = JetBrainsMono,
                fontSize = 11.sp,
                lineHeight = 16.5.sp,
            )
            Cursor(show = !typed.done)
        }
    }
}

@Composable
private fun BackButton(onClick: () -> Unit) {
    val colors = LocalDriftColors.current
    Text(
        text = "‹ BACK",
        color = colors.greenMid,
        fontFamily = JetBrainsMono,
        fontSize = 9.5.sp,
        letterSpacing = 1.5.sp,
        modifier = Modifier
            .clip(RoundedCornerShape(3.dp))
            .border(1.dp, colors.greenFaint, RoundedCornerShape(3.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 3.dp),
    )
}

/** SRC line = the credited author; SYN/original sounds have none, so label them as synth. */
private fun sourceOf(sound: Sound): String =
    sound.license.author ?: if (sound.license.license == License.ORIGINAL) "Synth · original" else "—"

/** Stepped (steps-16) easing for the readout's left→right clip wipe. */
private val SteppedReveal = Easing { f -> floor(f * 16f) / 16f }

@Preview(showBackground = true, backgroundColor = 0xFF060A07)
@Composable
private fun LayerCardPreview() {
    DriftTheme {
        Column(
            Modifier
                .background(LocalDriftColors.current.bg)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            LayerCard(
                sound = Catalogue.byId("interstellarplasma")!!, channel = 5,
                on = true, volume = 0.4f, active = true, showInfo = false,
                onToggle = {}, onVolume = {}, onInfo = {},
            )
            LayerCard(
                sound = Catalogue.byId("rain")!!, channel = 1,
                on = false, volume = 0.6f, active = false, showInfo = true,
                onToggle = {}, onVolume = {}, onInfo = {},
            )
        }
    }
}
