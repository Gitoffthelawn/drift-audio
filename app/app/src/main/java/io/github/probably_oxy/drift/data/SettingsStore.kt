package io.github.probably_oxy.drift.data

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf

/**
 * Global, non-preset listening settings persisted to SharedPreferences.
 *
 * Introduced in Phase 4b for the **reduce-motion** flag: the redesign is heavy on
 * looping animation (spinners, VU flicker, glow pulse, scanlines, typewriter), and
 * the design spec requires all of it to honor a reduced-motion preference. The flag
 * lives here, early, so the animated atoms built this phase can read it from the
 * start via [LocalReduceMotion] and skip straight to their end state.
 *
 * Reuses the same `"drift_settings"` prefs file that [io.github.probably_oxy.drift.audio.PlaybackService]
 * already uses for the output mode — one settings store, one file.
 *
 * [reduceMotion] is a Compose [androidx.compose.runtime.MutableState] mirror of the
 * stored value, so reading it in a composable subscribes to changes; writing through
 * [updateReduceMotion] updates both the live state and disk.
 */
class SettingsStore(context: Context) {
    private val prefs =
        context.applicationContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE)

    var reduceMotion by mutableStateOf(prefs.getBoolean(KEY_REDUCE_MOTION, false))
        private set

    fun updateReduceMotion(value: Boolean) {
        reduceMotion = value
        prefs.edit().putBoolean(KEY_REDUCE_MOTION, value).apply()
    }

    private companion object {
        const val PREFS = "drift_settings"
        const val KEY_REDUCE_MOTION = "reduce_motion"
    }
}

/**
 * Tree-wide "skip looping/entrance motion" flag. Defaults to `false` (motion on) so
 * `@Preview`s and any composable outside a provided [SettingsStore] still render. The
 * real value is provided at the top of the app once the store exists (Phase 4f).
 */
val LocalReduceMotion = staticCompositionLocalOf { false }
