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
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
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
import io.github.probably_oxy.drift.data.Preset
import io.github.probably_oxy.drift.data.PresetLayer
import io.github.probably_oxy.drift.data.PresetLibrary
import io.github.probably_oxy.drift.data.PresetStore
import io.github.probably_oxy.drift.data.Sound
import io.github.probably_oxy.drift.data.SoundType
import kotlinx.serialization.encodeToString
import io.github.probably_oxy.drift.ui.theme.Accent
import io.github.probably_oxy.drift.ui.theme.Bg
import io.github.probably_oxy.drift.ui.theme.Border
import io.github.probably_oxy.drift.ui.theme.BorderActive
import io.github.probably_oxy.drift.ui.theme.BorderBadge
import io.github.probably_oxy.drift.ui.theme.BorderPanel
import io.github.probably_oxy.drift.ui.theme.Danger
import io.github.probably_oxy.drift.ui.theme.DangerText
import io.github.probably_oxy.drift.ui.theme.DriftTheme
import io.github.probably_oxy.drift.ui.theme.Surface
import io.github.probably_oxy.drift.ui.theme.SurfaceActive
import io.github.probably_oxy.drift.ui.theme.TextActive
import io.github.probably_oxy.drift.ui.theme.TextBadge
import io.github.probably_oxy.drift.ui.theme.TextDim
import io.github.probably_oxy.drift.ui.theme.TextEyebrow
import io.github.probably_oxy.drift.ui.theme.TextMuted
import io.github.probably_oxy.drift.ui.theme.TextPrimary
import io.github.probably_oxy.drift.ui.theme.TextSection
import io.github.probably_oxy.drift.ui.theme.Track

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DriftTheme {
                RequestNotificationPermission()
                Scaffold(
                    containerColor = Bg,
                    modifier = Modifier.fillMaxSize(),
                ) { innerPadding ->
                    MixerScreen(modifier = Modifier.padding(innerPadding))
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
 * Phase 3 main screen — the cockpit mixer. Each catalogue sound is a layer card;
 * tapping a playable (REC) card toggles it into the mix via a custom session
 * command. Active state is signalled on multiple channels at once (border, text
 * colour, status dot, glyph) so it never depends on colour alone.
 */
@Composable
fun MixerScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val sounds = remember { Catalogue.sounds }
    val presetStore = remember { PresetStore(context) }

    var controller by remember { mutableStateOf<MediaController?>(null) }
    var isPlaying by remember { mutableStateOf(false) }
    var timerRemainingMs by remember { mutableStateOf(PlaybackService.TIMER_INACTIVE) }
    var showSaveDialog by remember { mutableStateOf(false) }
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

    Column(modifier = modifier.fillMaxSize().padding(horizontal = 18.dp)) {
        Header(connected = controller != null)
        Spacer(Modifier.height(12.dp))

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            item {
                TimerPanel(
                    remainingMs = timerRemainingMs,
                    enabled = controller != null && (activeIds.isNotEmpty() || timerRemainingMs > 0),
                    onPick = { setTimer(it) },
                    onCancel = { cancelTimer() },
                )
            }
            item {
                Spacer(Modifier.height(6.dp))
                SectionLabel("SOUNDSCAPE")
            }
            items(sounds, key = { it.id }) { sound ->
                val playable = AudioFiles.isPlayable(sound)
                LayerCard(
                    sound = sound,
                    active = sound.id in activeIds,
                    enabled = playable && controller != null,
                    volume = volumes[sound.id] ?: 1f,
                    onToggle = { toggle(sound) },
                    onVolumeChange = { setVolume(sound, it) },
                )
            }
            item {
                Spacer(Modifier.height(6.dp))
                SectionLabel("PRESETS")
            }
            item {
                PresetsSection(
                    builtIn = PresetLibrary.builtIn,
                    userPresets = userPresets,
                    enabled = controller != null,
                    canSave = controller != null && activeIds.isNotEmpty(),
                    onApply = { applyPreset(it) },
                    onSaveClick = { showSaveDialog = true },
                    onDelete = { deletePreset(it) },
                )
            }
        }

        Spacer(Modifier.height(12.dp))
        MasterTransport(
            isPlaying = isPlaying,
            enabled = controller != null && activeIds.isNotEmpty(),
            onClick = {
                val c = controller ?: return@MasterTransport
                if (isPlaying) c.pause() else c.play()
            },
        )
        Spacer(Modifier.height(12.dp))
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
private fun Header(connected: Boolean) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = if (connected) "DRIFT · AMBIENT MIXER" else "DRIFT · CONNECTING…",
            color = TextEyebrow,
            fontSize = 10.sp,
            letterSpacing = 4.sp,
        )
    }
}

@Composable
private fun TimerPanel(
    remainingMs: Long,
    enabled: Boolean,
    onPick: (Int) -> Unit,
    onCancel: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Surface)
            .border(1.dp, BorderPanel, RoundedCornerShape(12.dp))
            .padding(14.dp),
    ) {
        Text(text = "SLEEP TIMER", color = TextSection, fontSize = 10.sp, letterSpacing = 3.sp)
        Spacer(Modifier.height(10.dp))
        if (remainingMs > 0) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = fmtDuration(remainingMs),
                    color = TextActive,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f),
                )
                TimerButton(label = "× CANCEL", enabled = true, onClick = onCancel)
            }
        } else {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(15, 30, 60, 90).forEach { mins ->
                    TimerButton(
                        label = "${mins}m",
                        enabled = enabled,
                        onClick = { onPick(mins) },
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }
    }
}

@Composable
private fun TimerButton(
    label: String,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .border(1.dp, Border, RoundedCornerShape(8.dp))
            .clickable(enabled = enabled, onClick = onClick)
            .padding(vertical = 10.dp, horizontal = 12.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = label,
            color = if (enabled) TextDim else TextMuted,
            fontSize = 12.sp,
            letterSpacing = 1.sp,
        )
    }
}

private fun fmtDuration(ms: Long): String {
    val total = (ms / 1000).toInt().coerceAtLeast(0)
    val h = total / 3600
    val m = (total % 3600) / 60
    val s = total % 60
    return if (h > 0) "%d:%02d:%02d".format(h, m, s) else "%02d:%02d".format(m, s)
}

@Composable
private fun SectionLabel(text: String) {
    Text(
        text = text,
        color = TextSection,
        fontSize = 11.sp,
        letterSpacing = 4.sp,
    )
}

@Composable
private fun LayerCard(
    sound: Sound,
    active: Boolean,
    enabled: Boolean,
    volume: Float,
    onToggle: () -> Unit,
    onVolumeChange: (Float) -> Unit,
) {
    val cardBg = if (active) SurfaceActive else Surface
    val borderColor = if (active) BorderActive else Border
    val nameColor = when {
        !enabled -> TextMuted
        active -> TextPrimary
        else -> TextDim
    }
    val glyph = when {
        !enabled -> "·"
        active -> "■"
        else -> "▶"
    }
    val glyphColor = if (active) TextActive else TextDim

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(cardBg)
            .border(2.dp, borderColor, RoundedCornerShape(12.dp))
            .clickable(enabled = enabled, onClick = onToggle)
            .padding(horizontal = 18.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(15.dp),
    ) {
        Box(modifier = Modifier.size(40.dp), contentAlignment = Alignment.Center) {
            Text(text = glyph, color = if (enabled) glyphColor else TextMuted, fontSize = 18.sp)
        }

        Column(modifier = Modifier.weight(1f)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = sound.name,
                    color = nameColor,
                    fontSize = 16.sp,
                    fontWeight = if (active) FontWeight.Bold else FontWeight.Normal,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false),
                )
                TypeBadge(sound.type)
                Spacer(Modifier.weight(1f))
                StatusDot(active)
            }
            Spacer(Modifier.height(4.dp))
            Text(text = sound.description, color = TextMuted, fontSize = 11.sp)
            if (active) {
                Spacer(Modifier.height(10.dp))
                VolumeSlider(value = volume, onValueChange = onVolumeChange)
            }
        }
    }
}

/**
 * A thin cockpit-styled volume control: a green fill on a dark track with a
 * round amber thumb. Tap or drag anywhere on the row to set the level.
 */
@Composable
private fun VolumeSlider(value: Float, onValueChange: (Float) -> Unit) {
    val thumb = 16.dp
    BoxWithConstraints(modifier = Modifier.fillMaxWidth().height(thumb)) {
        val density = LocalDensity.current
        val widthPx = with(density) { maxWidth.toPx() }
        val thumbPx = with(density) { thumb.toPx() }
        val clamped = value.coerceIn(0f, 1f)
        val fillDp = with(density) { (clamped * widthPx).toDp() }
        val thumbOffset = with(density) {
            (clamped * widthPx - thumbPx / 2f).coerceIn(0f, widthPx - thumbPx).toDp()
        }
        val setFromX: (Float) -> Unit = { x -> onValueChange((x / widthPx).coerceIn(0f, 1f)) }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(thumb)
                .pointerInput(Unit) { detectTapGestures { setFromX(it.x) } }
                .pointerInput(Unit) {
                    detectHorizontalDragGestures { change, _ -> setFromX(change.position.x) }
                },
        ) {
            Box(
                Modifier.align(Alignment.CenterStart).fillMaxWidth().height(5.dp)
                    .clip(RoundedCornerShape(5.dp)).background(Track),
            )
            Box(
                Modifier.align(Alignment.CenterStart).width(fillDp).height(5.dp)
                    .clip(RoundedCornerShape(5.dp)).background(BorderActive),
            )
            Box(
                Modifier.align(Alignment.CenterStart).offset(x = thumbOffset).size(thumb)
                    .clip(CircleShape).background(Accent),
            )
        }
    }
}

@Composable
private fun TypeBadge(type: SoundType) {
    Text(
        text = type.name,
        color = TextBadge,
        fontSize = 9.sp,
        letterSpacing = 0.5.sp,
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .border(1.dp, BorderBadge, RoundedCornerShape(4.dp))
            .padding(horizontal = 6.dp, vertical = 2.dp),
    )
}

@Composable
private fun StatusDot(active: Boolean) {
    Box(
        modifier = Modifier
            .size(8.dp)
            .clip(CircleShape)
            .background(if (active) BorderActive else Track),
    )
}

@Composable
private fun MasterTransport(
    isPlaying: Boolean,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    val color = if (enabled) (if (isPlaying) TextActive else Accent) else TextMuted
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Surface)
            .border(2.dp, if (enabled) color else Border, RoundedCornerShape(12.dp))
            .clickable(enabled = enabled, onClick = onClick)
            .padding(vertical = 18.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = if (isPlaying) "❚❚  PAUSE ALL" else "▶  RESUME ALL",
            color = color,
            fontSize = 14.sp,
            letterSpacing = 2.sp,
            fontWeight = FontWeight.Bold,
        )
    }
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
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
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
    val borderColor = when {
        !enabled -> Border
        danger -> Danger
        accent -> Accent
        else -> BorderPanel
    }
    val textColor = when {
        !enabled -> TextMuted
        danger -> DangerText
        accent -> Accent
        else -> TextDim
    }
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Surface)
            .border(1.dp, borderColor, RoundedCornerShape(8.dp))
            .clickable(enabled = enabled, onClick = onClick)
            .padding(vertical = 12.dp, horizontal = 14.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = label,
            color = textColor,
            fontSize = 12.sp,
            letterSpacing = 1.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun SavePresetDialog(onConfirm: (String) -> Unit, onDismiss: () -> Unit) {
    var name by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Surface,
        title = { Text("SAVE MIX", color = TextPrimary, fontSize = 14.sp, letterSpacing = 2.sp) },
        text = {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                singleLine = true,
                placeholder = { Text("Preset name", color = TextMuted) },
            )
        },
        confirmButton = { TextButton(onClick = { onConfirm(name) }) { Text("SAVE", color = TextActive) } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("CANCEL", color = TextMuted) } },
    )
}
