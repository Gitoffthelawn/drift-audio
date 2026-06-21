package io.github.probably_oxy.drift

import android.Manifest
import android.content.ComponentName
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionCommand
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.MoreExecutors
import io.github.probably_oxy.drift.audio.AudioFiles
import io.github.probably_oxy.drift.audio.PlaybackService
import io.github.probably_oxy.drift.data.Catalogue
import io.github.probably_oxy.drift.data.OutputMode
import io.github.probably_oxy.drift.data.Preset
import io.github.probably_oxy.drift.data.PresetLayer
import io.github.probably_oxy.drift.data.PresetLibrary
import io.github.probably_oxy.drift.data.PresetStore
import io.github.probably_oxy.drift.data.SettingsStore
import io.github.probably_oxy.drift.data.Sound
import kotlinx.serialization.encodeToString
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import io.github.probably_oxy.drift.data.LocalDriftAnim
import io.github.probably_oxy.drift.ui.cockpit.AsciiSpinner
import io.github.probably_oxy.drift.ui.cockpit.IconMenu
import io.github.probably_oxy.drift.ui.cockpit.IconVolume
import io.github.probably_oxy.drift.ui.cockpit.IconVolumeMute
import io.github.probably_oxy.drift.ui.cockpit.InfoButton
import io.github.probably_oxy.drift.ui.cockpit.InfoStrip
import io.github.probably_oxy.drift.ui.cockpit.LayerCard
import io.github.probably_oxy.drift.ui.cockpit.MenuDrawer
import io.github.probably_oxy.drift.ui.cockpit.OutputIcon
import io.github.probably_oxy.drift.ui.cockpit.ScanlineOverlay
import io.github.probably_oxy.drift.ui.cockpit.entrance
import io.github.probably_oxy.drift.ui.theme.DriftTheme
import io.github.probably_oxy.drift.ui.theme.JetBrainsMono
import io.github.probably_oxy.drift.ui.theme.LocalDriftColors
import io.github.probably_oxy.drift.ui.theme.ShareTechMono
import io.github.probably_oxy.drift.ui.theme.ThemePreset

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current
            val settings = remember { SettingsStore(context) }
            val preset = ThemePreset.entries.firstOrNull { it.name == settings.themeName }
                ?: ThemePreset.PHOSPHOR
            DriftTheme(colors = preset.colors) {
                CompositionLocalProvider(LocalDriftAnim provides settings.anim) {
                    RequestNotificationPermission()
                    Scaffold(
                        containerColor = LocalDriftColors.current.bg,
                        modifier = Modifier.fillMaxSize(),
                    ) { innerPadding ->
                        MixerScreen(
                            modifier = Modifier.padding(innerPadding),
                            settings = settings,
                            currentPreset = preset,
                        )
                    }
                }
            }
        }
    }
}

/**
 * On Android 13+ the media notification only shows if the user grants
 * POST_NOTIFICATIONS. Playback works regardless; without it you just lose
 * the lock-screen controls. We ask once on first composition.
 */
@Composable
fun RequestNotificationPermission() {
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { /* result ignored: the notification simply won't appear if denied */ }

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }
}

/**
 * The cockpit mixer screen. Each catalogue sound is a Telemetry layer card; tapping a
 * playable card toggles it into the mix via a custom session command. Active state is
 * signalled on multiple channels at once (border, amber title, VU, spinner) so it
 * never depends on colour alone.
 */
@Composable
fun MixerScreen(
    modifier: Modifier = Modifier,
    settings: SettingsStore,
    currentPreset: ThemePreset,
) {
    val context = LocalContext.current
    val sounds = remember { Catalogue.sounds }
    val presetStore = remember { PresetStore(context) }

    var controller by remember { mutableStateOf<MediaController?>(null) }
    var isPlaying by remember { mutableStateOf(false) }
    var timerRemainingMs by remember { mutableStateOf(PlaybackService.TIMER_INACTIVE) }
    var outputMode by remember { mutableStateOf(OutputMode.SPEAKER) }
    var showSaveDialog by remember { mutableStateOf(false) }
    var menuOpen by remember { mutableStateOf(false) }
    // Which layer card (if any) is showing its terminal readout — at most one.
    var flippedLayerId by remember { mutableStateOf<String?>(null) }
    val activeIds = remember { mutableStateListOf<String>() }
    val volumes = remember { mutableStateMapOf<String, Float>() }
    val userPresets = remember { mutableStateListOf<Preset>() }

    LaunchedEffect(Unit) { userPresets.addAll(presetStore.load()) }

    DisposableEffect(Unit) {
        val token = SessionToken(context, ComponentName(context, PlaybackService::class.java))
        val future = MediaController.Builder(context, token)
            // The service broadcasts the sleep-timer countdown via session extras.
            .setListener(object : MediaController.Listener {
                override fun onExtrasChanged(controller: MediaController, extras: Bundle) {
                    timerRemainingMs = extras.getLong(
                        PlaybackService.EXTRA_TIMER_REMAINING_MS,
                        PlaybackService.TIMER_INACTIVE,
                    )
                    outputMode = OutputMode.fromName(extras.getString(PlaybackService.EXTRA_OUTPUT_MODE))
                }
            })
            .buildAsync()
        var listener: Player.Listener? = null
        future.addListener(
            {
                val c = future.get()
                controller = c
                isPlaying = c.isPlaying
                timerRemainingMs = c.sessionExtras.getLong(
                    PlaybackService.EXTRA_TIMER_REMAINING_MS,
                    PlaybackService.TIMER_INACTIVE,
                )
                outputMode = OutputMode.fromName(
                    c.sessionExtras.getString(PlaybackService.EXTRA_OUTPUT_MODE),
                )
                listener = object : Player.Listener {
                    override fun onIsPlayingChanged(playing: Boolean) {
                        isPlaying = playing
                    }
                }.also(c::addListener)
            },
            MoreExecutors.directExecutor(),
        )
        onDispose {
            listener?.let { controller?.removeListener(it) }
            MediaController.releaseFuture(future)
            controller = null
        }
    }

    fun toggle(sound: Sound) {
        val c = controller ?: return
        c.sendCustomCommand(
            SessionCommand(PlaybackService.ACTION_TOGGLE_LAYER, Bundle.EMPTY),
            Bundle().apply { putString(PlaybackService.KEY_SOUND_ID, sound.id) },
        )
        if (sound.id in activeIds) {
            activeIds.remove(sound.id)
        } else {
            // A freshly added layer starts at full volume in the engine; keep
            // the slider in step.
            volumes[sound.id] = 1f
            activeIds.add(sound.id)
        }
    }

    fun setVolume(sound: Sound, volume: Float) {
        volumes[sound.id] = volume
        controller?.sendCustomCommand(
            SessionCommand(PlaybackService.ACTION_SET_VOLUME, Bundle.EMPTY),
            Bundle().apply {
                putString(PlaybackService.KEY_SOUND_ID, sound.id)
                putFloat(PlaybackService.KEY_VOLUME, volume)
            },
        )
    }

    fun setTimer(minutes: Int) {
        controller?.sendCustomCommand(
            SessionCommand(PlaybackService.ACTION_SET_TIMER, Bundle.EMPTY),
            Bundle().apply { putLong(PlaybackService.KEY_TIMER_MS, minutes * 60_000L) },
        )
    }

    fun cancelTimer() {
        controller?.sendCustomCommand(
            SessionCommand(PlaybackService.ACTION_CANCEL_TIMER, Bundle.EMPTY),
            Bundle.EMPTY,
        )
    }

    fun setOutput(mode: OutputMode) {
        outputMode = mode // optimistic; the service echoes it back via extras
        controller?.sendCustomCommand(
            SessionCommand(PlaybackService.ACTION_SET_OUTPUT_MODE, Bundle.EMPTY),
            Bundle().apply { putString(PlaybackService.KEY_OUTPUT_MODE, mode.name) },
        )
    }

    // Mute: silence the mix but keep every layer + volume (resume picks up where
    // it left off). Stop All: tear the whole mix down to nothing.
    fun toggleMute() {
        val c = controller ?: return
        if (isPlaying) c.pause() else c.play()
    }

    fun stopAll() {
        controller?.stop()
        activeIds.clear()
        volumes.clear()
    }

    fun applyPreset(preset: Preset) {
        controller?.sendCustomCommand(
            SessionCommand(PlaybackService.ACTION_APPLY_PRESET, Bundle.EMPTY),
            Bundle().apply {
                putString(PlaybackService.KEY_PRESET_JSON, PresetStore.DriftJson.encodeToString(preset))
            },
        )
        // Mirror the new mix locally; only playable layers actually sound.
        activeIds.clear()
        volumes.clear()
        preset.layers.forEach { layer ->
            val sound = Catalogue.byId(layer.soundId) ?: return@forEach
            if (AudioFiles.isPlayable(sound)) {
                activeIds.add(layer.soundId)
                volumes[layer.soundId] = layer.volume
            }
        }
    }

    fun saveCurrentMix(name: String) {
        val layers = activeIds.map { PresetLayer(it, variantId = null, volume = volumes[it] ?: 1f) }
        if (layers.isEmpty()) return
        val label = name.trim().ifBlank { "Mix ${userPresets.size + 1}" }
        userPresets.add(Preset(name = label, layers = layers))
        presetStore.save(userPresets.toList())
    }

    fun deletePreset(preset: Preset) {
        userPresets.remove(preset)
        presetStore.save(userPresets.toList())
    }

    // active = layer on AND not globally muted; muted (paused) de-energizes cards.
    val playingNow = isPlaying && activeIds.isNotEmpty()

    Box(modifier = modifier.fillMaxSize()) {
      Column(modifier = Modifier.fillMaxSize().padding(horizontal = 22.dp)) {
        Header(onMenu = { menuOpen = true })
        Spacer(Modifier.height(8.dp))
        StatusPanel(
            layerCount = activeIds.size,
            playing = playingNow,
            muted = activeIds.isNotEmpty() && !isPlaying,
            enabled = controller != null && activeIds.isNotEmpty(),
            onMute = { toggleMute() },
            onStop = { stopAll() },
            modifier = Modifier.entrance(0),
        )
        Spacer(Modifier.height(18.dp))

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            item {
                SleepPanel(
                    remainingMs = timerRemainingMs,
                    enabled = controller != null && (activeIds.isNotEmpty() || timerRemainingMs > 0),
                    onPick = { setTimer(it) },
                    onCancel = { cancelTimer() },
                    modifier = Modifier.entrance(45),
                )
            }
            item {
                OutputPanel(
                    selected = outputMode,
                    onSelect = { setOutput(it) },
                    modifier = Modifier.entrance(90),
                )
            }
            item { SectionLabel("SOUNDSCAPE", modifier = Modifier.entrance(120)) }
            itemsIndexed(sounds, key = { _, s -> s.id }) { index, sound ->
                val playable = AudioFiles.isPlayable(sound)
                val on = sound.id in activeIds
                LayerCard(
                    sound = sound,
                    channel = index + 1,
                    on = on,
                    volume = volumes[sound.id] ?: 1f,
                    active = on && playingNow,
                    showInfo = flippedLayerId == sound.id,
                    onToggle = { if (playable) toggle(sound) },
                    onVolume = { if (playable) setVolume(sound, it) },
                    onInfo = { flippedLayerId = if (flippedLayerId == sound.id) null else sound.id },
                    // Entrance stagger; SYN sounds (no audio yet) shown dimmed and inert.
                    modifier = Modifier
                        .entrance(150 + minOf(index, 8) * 35)
                        .alpha(if (playable) 1f else 0.5f),
                )
            }
            item { SectionLabel("PRESETS", modifier = Modifier.entrance(160)) }
            item {
                PresetsSection(
                    builtIn = PresetLibrary.builtIn,
                    userPresets = userPresets,
                    enabled = controller != null,
                    canSave = controller != null && activeIds.isNotEmpty(),
                    onApply = { applyPreset(it) },
                    onSaveClick = { showSaveDialog = true },
                    onDelete = { deletePreset(it) },
                    modifier = Modifier.entrance(185),
                )
            }
            item { Spacer(Modifier.height(10.dp)) }
        }

        Spacer(Modifier.height(8.dp))
      }

      // Subtle CRT scanlines + sweep, above content but below the drawer.
      ScanlineOverlay()

      // Left slide-in menu drawer (theme / about / credits / settings).
      MenuDrawer(
          open = menuOpen,
          currentPreset = currentPreset,
          settings = settings,
          onSelectPreset = { settings.updateTheme(it.name) },
          onClose = { menuOpen = false },
      )
    }

    if (showSaveDialog) {
        SavePresetDialog(
            onConfirm = { name ->
                saveCurrentMix(name)
                showSaveDialog = false
            },
            onDismiss = { showSaveDialog = false },
        )
    }
}

@Composable
private fun Header(onMenu: () -> Unit) {
    val colors = LocalDriftColors.current
    Box(
        modifier = Modifier.fillMaxWidth().padding(top = 18.dp, bottom = 4.dp),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .size(40.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onMenu,
                ),
            contentAlignment = Alignment.Center,
        ) {
            IconMenu(colors.greenDim, size = 26.dp)
        }
        Text(
            text = "DRIFT // AUDIO",
            color = colors.greenDim,
            fontFamily = ShareTechMono,
            fontSize = 15.sp,
            letterSpacing = 7.sp,
        )
    }
}

/** The SYS.STATUS multi-function panel: spinner + layer/state readout + MUTE / STOP ALL. */
@Composable
private fun StatusPanel(
    layerCount: Int,
    playing: Boolean,
    muted: Boolean,
    enabled: Boolean,
    onMute: () -> Unit,
    onStop: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = LocalDriftColors.current
    val state = when {
        playing -> "PLAYING"
        muted -> "MUTED"
        else -> "IDLE"
    }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(9.dp))
            .background(colors.cardBg)
            .border(1.dp, colors.cardLine, RoundedCornerShape(9.dp))
            .padding(20.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AsciiSpinner(active = playing, boxed = true, boxSize = 27.dp)
            Spacer(Modifier.width(10.dp))
            Text("SYS.STATUS", color = colors.greenDim, fontFamily = ShareTechMono, fontSize = 12.sp, letterSpacing = 5.sp)
            Spacer(Modifier.weight(1f))
            Text(
                text = "LAYERS: $layerCount / $state",
                color = if (playing) colors.greenBright else colors.greenDim,
                fontFamily = JetBrainsMono,
                fontSize = 12.5.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 0.5.sp,
            )
        }
        Spacer(Modifier.height(14.dp))
        Box(Modifier.fillMaxWidth().height(1.dp).background(colors.cardLine))
        Spacer(Modifier.height(14.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            MuteButton(muted = muted, enabled = enabled, onClick = onMute, modifier = Modifier.weight(1f))
            StopButton(enabled = enabled, onClick = onStop, modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun MuteButton(muted: Boolean, enabled: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val colors = LocalDriftColors.current
    val tint = if (enabled) colors.greenBright else colors.greenDim
    val border = when {
        !enabled -> colors.cardLine
        muted -> colors.greenBright
        else -> colors.greenLine
    }
    val bg = if (muted && enabled) colors.greenBright.copy(alpha = 0.08f) else Color.Transparent
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(7.dp))
            .background(bg)
            .border(1.dp, border, RoundedCornerShape(7.dp))
            .clickable(enabled = enabled, onClick = onClick)
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (muted) IconVolumeMute(tint, size = 18.dp) else IconVolume(tint, size = 18.dp)
        Spacer(Modifier.width(8.dp))
        Text(if (muted) "UNMUTE" else "MUTE", color = tint, fontFamily = JetBrainsMono, fontSize = 14.sp, letterSpacing = 2.sp)
    }
}

@Composable
private fun StopButton(enabled: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val colors = LocalDriftColors.current
    val tint = if (enabled) colors.red else colors.red.copy(alpha = 0.4f)
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(7.dp))
            .background(if (enabled) colors.redBg else Color.Transparent)
            .border(1.dp, if (enabled) colors.redLine else colors.cardLine, RoundedCornerShape(7.dp))
            .clickable(enabled = enabled, onClick = onClick)
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text("■ STOP ALL", color = tint, fontFamily = JetBrainsMono, fontSize = 14.sp, letterSpacing = 2.sp)
    }
}

/** Compact sleep-timer panel: moon + SLEEP, idle chips or running countdown, and an info strip. */
@Composable
private fun SleepPanel(
    remainingMs: Long,
    enabled: Boolean,
    onPick: (Int) -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = LocalDriftColors.current
    val running = remainingMs > 0
    var infoOpen by remember { mutableStateOf(false) }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(9.dp))
            .background(colors.cardBg)
            .border(1.dp, colors.cardLine, RoundedCornerShape(9.dp))
            .padding(14.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("SLEEP", color = colors.greenDim, fontFamily = ShareTechMono, fontSize = 13.sp, letterSpacing = 4.sp)
            Spacer(Modifier.weight(1f))
            if (running) {
                SleepCountdown(remainingMs)
                Spacer(Modifier.width(10.dp))
                Chip("✕", enabled = true, onClick = onCancel)
            } else {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf(15, 30, 60, 90).forEach { m ->
                        Chip("$m", enabled = enabled, onClick = { onPick(m) })
                    }
                }
            }
            Spacer(Modifier.width(8.dp))
            InfoButton(onClick = { infoOpen = !infoOpen })
        }
        InfoStrip(
            open = infoOpen,
            text = "▸ SLEEP TIMER · fades the mix out and stops playback when the countdown reaches zero.",
        )
    }
}

@Composable
private fun SleepCountdown(ms: Long) {
    val colors = LocalDriftColors.current
    val transition = rememberInfiniteTransition(label = "colon")
    val phase by transition.animateFloat(
        0f, 1f,
        infiniteRepeatable(tween(1000, easing = LinearEasing), RepeatMode.Restart),
        label = "colonBlink",
    )
    val showColon = phase < 0.5f
    Text(
        text = fmtDuration(ms, showColon),
        color = colors.greenBright,
        fontFamily = JetBrainsMono,
        fontSize = 21.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.sp,
    )
}

@Composable
private fun Chip(label: String, enabled: Boolean, onClick: () -> Unit) {
    val colors = LocalDriftColors.current
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .border(1.dp, colors.cardLine, RoundedCornerShape(6.dp))
            .clickable(enabled = enabled, onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 7.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(label, color = if (enabled) colors.greenDim else colors.greenFaint, fontFamily = JetBrainsMono, fontSize = 12.sp, letterSpacing = 1.sp)
    }
}

/** Compact output panel: OUTPUT + three sink icon buttons + an info strip describing the sink. */
@Composable
private fun OutputPanel(
    selected: OutputMode,
    onSelect: (OutputMode) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = LocalDriftColors.current
    var infoOpen by remember { mutableStateOf(false) }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(9.dp))
            .background(colors.cardBg)
            .border(1.dp, colors.cardLine, RoundedCornerShape(9.dp))
            .padding(14.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("OUTPUT", color = colors.greenDim, fontFamily = ShareTechMono, fontSize = 13.sp, letterSpacing = 4.sp)
            Spacer(Modifier.weight(1f))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutputMode.entries.forEach { mode ->
                    OutputButton(mode = mode, selected = mode == selected, onClick = { onSelect(mode) })
                }
            }
            Spacer(Modifier.width(8.dp))
            InfoButton(onClick = { infoOpen = !infoOpen })
        }
        InfoStrip(open = infoOpen, text = outputInfo(selected))
    }
}

@Composable
private fun OutputButton(mode: OutputMode, selected: Boolean, onClick: () -> Unit) {
    val colors = LocalDriftColors.current
    val tint = if (selected) colors.greenBright else colors.greenDim
    Box(
        modifier = Modifier
            .size(width = 46.dp, height = 40.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(if (selected) colors.greenBright.copy(alpha = 0.08f) else Color.Transparent)
            .border(1.dp, if (selected) colors.greenBright else colors.cardLine, RoundedCornerShape(6.dp))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        OutputIcon(mode = mode, tint = tint, size = 20.dp)
    }
}

private fun outputInfo(mode: OutputMode): String = when (mode) {
    OutputMode.SPEAKER -> "▸ SPEAKER · phone loudspeaker — mono, full-range playback."
    OutputMode.STEREO -> "▸ STEREO · external / cast stereo — wide L-R image."
    OutputMode.PHONES -> "▸ PHONES · headphones — spatialised binaural mix."
}

private fun fmtDuration(ms: Long, colon: Boolean = true): String {
    val total = (ms / 1000).toInt().coerceAtLeast(0)
    val h = total / 3600
    val m = (total % 3600) / 60
    val s = total % 60
    val sep = if (colon) ":" else " "
    return if (h > 0) "%d$sep%02d$sep%02d".format(h, m, s) else "%02d$sep%02d".format(m, s)
}

@Composable
private fun SectionLabel(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        color = LocalDriftColors.current.greenDim,
        fontFamily = ShareTechMono,
        fontSize = 12.sp,
        letterSpacing = 5.sp,
        modifier = modifier.padding(top = 4.dp),
    )
}

@Composable
private fun PresetsSection(
    builtIn: List<Preset>,
    userPresets: List<Preset>,
    enabled: Boolean,
    canSave: Boolean,
    onApply: (Preset) -> Unit,
    onSaveClick: () -> Unit,
    onDelete: (Preset) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        builtIn.chunked(2).forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                row.forEach { preset ->
                    PresetChip(
                        label = preset.name,
                        enabled = enabled,
                        onClick = { onApply(preset) },
                        modifier = Modifier.weight(1f),
                    )
                }
                if (row.size == 1) Spacer(Modifier.weight(1f))
            }
        }

        Spacer(Modifier.height(4.dp))
        PresetChip(
            label = "+ SAVE CURRENT MIX",
            enabled = canSave,
            onClick = onSaveClick,
            modifier = Modifier.fillMaxWidth(),
            accent = true,
        )
        userPresets.forEach { preset ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                PresetChip(
                    label = preset.name,
                    enabled = enabled,
                    onClick = { onApply(preset) },
                    modifier = Modifier.weight(1f),
                )
                PresetChip(
                    label = "×",
                    enabled = true,
                    onClick = { onDelete(preset) },
                    danger = true,
                )
            }
        }
    }
}

@Composable
private fun PresetChip(
    label: String,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    accent: Boolean = false,
    danger: Boolean = false,
) {
    val colors = LocalDriftColors.current
    val borderColor = when {
        !enabled -> colors.cardLine
        danger -> colors.redLine
        accent -> colors.greenLine
        else -> colors.cardLine
    }
    val textColor = when {
        !enabled -> colors.greenFaint
        danger -> colors.red
        accent -> colors.greenBright
        else -> colors.greenDim
    }
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(7.dp))
            .background(colors.cardBg)
            .border(1.dp, borderColor, RoundedCornerShape(7.dp))
            .clickable(enabled = enabled, onClick = onClick)
            .padding(vertical = 12.dp, horizontal = 14.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = label,
            color = textColor,
            fontFamily = JetBrainsMono,
            fontSize = 12.sp,
            letterSpacing = 1.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun SavePresetDialog(onConfirm: (String) -> Unit, onDismiss: () -> Unit) {
    val colors = LocalDriftColors.current
    var name by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = colors.bg,
        title = { Text("SAVE MIX", color = colors.greenBright, fontFamily = ShareTechMono, fontSize = 14.sp, letterSpacing = 2.sp) },
        text = {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                singleLine = true,
                placeholder = { Text("Preset name", color = colors.greenFaint) },
            )
        },
        confirmButton = { TextButton(onClick = { onConfirm(name) }) { Text("SAVE", color = colors.greenBright) } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("CANCEL", color = colors.greenFaint) } },
    )
}
