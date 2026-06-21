package io.github.probably_oxy.drift.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import io.github.probably_oxy.drift.R

/** JetBrains Mono (OFL) — the cockpit's monospace voice (body, numbers, readouts). */
val JetBrainsMono = FontFamily(
    Font(R.font.jetbrains_mono_regular, FontWeight.Normal),
    Font(R.font.jetbrains_mono_bold, FontWeight.Bold),
)

/**
 * Share Tech Mono (OFL) — the wide letter-spaced display caps: the `DRIFT // AUDIO`
 * wordmark and section labels (SYS.STATUS, OUTPUT, SLEEP, SOUNDSCAPE). One weight only.
 */
val ShareTechMono = FontFamily(
    Font(R.font.share_tech_mono_regular, FontWeight.Normal),
)

/**
 * Serif italic — used *only* for the lowercase italic "i" glyph inside the square
 * info button. Android's built-in serif (Noto/Droid Serif) covers this; no asset needed.
 */
val InfoSerif = FontFamily.Serif

/** Everything is monospace; sizes/spacing tuned per use site in the UI. */
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = JetBrainsMono,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.3.sp,
    ),
    bodyMedium = TextStyle(
        fontFamily = JetBrainsMono,
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp,
        lineHeight = 18.sp,
        letterSpacing = 0.3.sp,
    ),
    labelSmall = TextStyle(
        fontFamily = JetBrainsMono,
        fontWeight = FontWeight.Normal,
        fontSize = 11.sp,
        lineHeight = 14.sp,
        letterSpacing = 1.5.sp,
    ),
)
