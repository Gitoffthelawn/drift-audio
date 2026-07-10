package io.github.probably_oxy.drift.data

import android.content.Context
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf

/**
 * Global, non-preset settings persisted to SharedPreferences: the theme preset and a
 * set of **independent** animation toggles (each graphical effect can be switched off
 * on its own, rather than one master "reduce motion" switch).
 *
 * Each flag is a Compose state mirror of the stored value, so reading it in a composable
 * subscribes to changes. Reuses the `"drift_settings"` file shared with PlaybackService.
 */
class SettingsStore(context: Context) {
    private val prefs =
        context.applicationContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE)

    var scanlines by mutableStateOf(prefs.getBoolean(KEY_SCANLINES, false)); private set
    var spinner by mutableStateOf(prefs.getBoolean(KEY_SPINNER, true)); private set
    var vuFlicker by mutableStateOf(prefs.getBoolean(KEY_VU_FLICKER, false)); private set
    var glowPulse by mutableStateOf(prefs.getBoolean(KEY_GLOW_PULSE, false)); private set
    var entrance by mutableStateOf(prefs.getBoolean(KEY_ENTRANCE, false)); private set

    fun updateScanlines(v: Boolean) { scanlines = v; put(KEY_SCANLINES, v) }
    fun updateSpinner(v: Boolean) { spinner = v; put(KEY_SPINNER, v) }
    fun updateVuFlicker(v: Boolean) { vuFlicker = v; put(KEY_VU_FLICKER, v) }
    fun updateGlowPulse(v: Boolean) { glowPulse = v; put(KEY_GLOW_PULSE, v) }
    fun updateEntrance(v: Boolean) { entrance = v; put(KEY_ENTRANCE, v) }

    /** Snapshot of the animation flags for [LocalDriftAnim]; reads subscribe to each flag. */
    val anim: DriftAnim
        get() = DriftAnim(scanlines, spinner, vuFlicker, glowPulse, entrance)

    /** Selected theme preset, stored as the [io.github.probably_oxy.drift.ui.theme.ThemePreset] name. */
    var themeName by mutableStateOf(prefs.getString(KEY_THEME, DEFAULT_THEME) ?: DEFAULT_THEME)
        private set

    fun updateTheme(name: String) {
        themeName = name
        prefs.edit().putString(KEY_THEME, name).apply()
    }

    private fun put(key: String, value: Boolean) = prefs.edit().putBoolean(key, value).apply()

    private companion object {
        const val PREFS = "drift_settings"
        const val KEY_SCANLINES = "fx_scanlines"
        const val KEY_SPINNER = "fx_spinner"
        const val KEY_VU_FLICKER = "fx_vu_flicker"
        const val KEY_GLOW_PULSE = "fx_glow_pulse"
        const val KEY_ENTRANCE = "fx_entrance"
        const val KEY_THEME = "theme_preset"
        const val DEFAULT_THEME = "PHOSPHOR"
    }
}

/**
 * Independent animation toggles read by the animated atoms (each `true` = effect on).
 * Defaults all-on so previews / any composable outside a provided store still animate.
 */
@Immutable
data class DriftAnim(
    val scanlines: Boolean = true,
    val spinner: Boolean = true,
    val vuFlicker: Boolean = true,
    val glowPulse: Boolean = true,
    val entrance: Boolean = true,
)

/** Tree-wide animation flags. Real values are provided at the app top from [SettingsStore.anim]. */
val LocalDriftAnim = staticCompositionLocalOf { DriftAnim() }
