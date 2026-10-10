package io.github.probably_oxy.drift.data

import android.content.Context
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import java.io.File

/**
 * Remembers each sound's last volume and variant across toggles and app
 * restarts, so switching a sound off and on again (or reopening Drift) brings it
 * back the way the user left it. Stored as a list of [PresetLayer] — the same
 * (id, variant, volume) shape — in the app's private storage. Best-effort like
 * [PresetStore]: a missing or corrupt file just means "no memory yet".
 */
class LayerMemoryStore(context: Context) {

    private val file = File(context.filesDir, "layer_memory.json")

    fun load(): List<PresetLayer> =
        if (file.exists()) {
            runCatching { PresetStore.DriftJson.decodeFromString<List<PresetLayer>>(file.readText()) }
                .getOrDefault(emptyList())
        } else {
            emptyList()
        }

    fun save(layers: List<PresetLayer>) {
        runCatching { file.writeText(PresetStore.DriftJson.encodeToString(layers)) }
    }
}
