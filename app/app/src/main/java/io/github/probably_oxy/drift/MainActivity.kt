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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionCommand
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.MoreExecutors
import io.github.probably_oxy.drift.audio.AudioFiles
import io.github.probably_oxy.drift.audio.PlaybackService
import io.github.probably_oxy.drift.data.Catalogue
import io.github.probably_oxy.drift.data.Sound
import io.github.probably_oxy.drift.ui.theme.DriftTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DriftTheme {
                RequestNotificationPermission()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
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
 * Phase 2c smoke test. Toggle any of the playable sounds into the mix (each
 * adds an ExoPlayer layer in the service) and use the master button to
 * pause/resume the whole mix. The master label is driven by a real
 * [Player.Listener] on the controller, so it never goes stale when audio focus
 * pauses us — fixing the Phase 2b deferred bug early.
 */
@Composable
fun MixerScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    // The playable catalogue entries (REC sounds with files); SYN sounds have
    // no audio yet and are omitted.
    val sounds = remember { Catalogue.sounds.filter { AudioFiles.isPlayable(it) } }

    var controller by remember { mutableStateOf<MediaController?>(null) }
    var isPlaying by remember { mutableStateOf(false) }

    // Which layers the user has toggled on. Tracked locally: this Activity is
    // the only controller, so its view of the mix stays in step with the engine.
    val activeIds = remember { mutableStateListOf<String>() }

    DisposableEffect(Unit) {
        val token = SessionToken(context, ComponentName(context, PlaybackService::class.java))
        val future = MediaController.Builder(context, token).buildAsync()
        var listener: Player.Listener? = null
        future.addListener(
            {
                val c = future.get()
                controller = c
                isPlaying = c.isPlaying
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

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(if (controller == null) "CONNECTING…" else "DRIFT — mixer test")

        Button(
            enabled = controller != null && activeIds.isNotEmpty(),
            onClick = {
                val c = controller ?: return@Button
                if (isPlaying) c.pause() else c.play()
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(if (isPlaying) "PAUSE ALL" else "RESUME ALL")
        }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            items(sounds, key = { it.id }) { sound ->
                val active = sound.id in activeIds
                LayerToggle(
                    sound = sound,
                    active = active,
                    enabled = controller != null,
                    onToggle = {
                        val c = controller ?: return@LayerToggle
                        c.sendCustomCommand(
                            SessionCommand(PlaybackService.ACTION_TOGGLE_LAYER, Bundle.EMPTY),
                            Bundle().apply { putString(PlaybackService.KEY_SOUND_ID, sound.id) },
                        )
                        if (active) activeIds.remove(sound.id) else activeIds.add(sound.id)
                    },
                )
            }
        }
    }
}

@Composable
private fun LayerToggle(
    sound: Sound,
    active: Boolean,
    enabled: Boolean,
    onToggle: () -> Unit,
) {
    val label = (if (active) "■ " else "  ") + sound.name
    if (active) {
        FilledTonalButton(onClick = onToggle, enabled = enabled, modifier = Modifier.fillMaxWidth()) {
            Text(label)
        }
    } else {
        OutlinedButton(onClick = onToggle, enabled = enabled, modifier = Modifier.fillMaxWidth()) {
            Text(label)
        }
    }
}
