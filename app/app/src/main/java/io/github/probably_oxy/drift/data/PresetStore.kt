package io.github.probably_oxy.drift.data

import android.content.Context
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

/**
 * Persists the user's saved mixes as a JSON file in the app's private storage —
 * the native equivalent of the web app's localStorage[drift_user_presets].
 * Reads/writes are best-effort: a corrupt or missing file yields an empty list
 * rather than crashing.
 */
class PresetStore(context: Context) {

    private val file = File(context.filesDir, "user_presets.json")

    fun load(): List<Preset> =
        if (file.exists()) {
            runCatching { DriftJson.decodeFromString<List<Preset>>(file.readText()) }
                .getOrDefault(emptyList())
        } else {
            emptyList()
        }

    fun save(presets: List<Preset>) {
        runCatching { file.writeText(DriftJson.encodeToString(presets)) }
    }

    companion object {
        /** Shared across UI and service so preset JSON round-trips identically. */
        val DriftJson = Json { ignoreUnknownKeys = true }
    }
}
