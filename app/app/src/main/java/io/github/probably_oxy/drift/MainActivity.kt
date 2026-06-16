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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.MoreExecutors
import io.github.probably_oxy.drift.audio.PlaybackService
import io.github.probably_oxy.drift.ui.theme.DriftTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DriftTheme {
                RequestNotificationPermission()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PlaybackTest(modifier = Modifier.padding(innerPadding))
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
 * Phase 2b smoke test. The Activity no longer owns a player; it connects to
 * [PlaybackService] through a MediaController and sends it play/pause.
 */
@Composable
fun PlaybackTest(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    // The controller arrives asynchronously (connecting to a service takes a
    // moment), so it starts null and we fill it in once connected.
    var controller by remember { mutableStateOf<MediaController?>(null) }

    DisposableEffect(Unit) {
        val token = SessionToken(context, ComponentName(context, PlaybackService::class.java))
        // buildAsync() returns a ListenableFuture — a result that arrives later.
        val future = MediaController.Builder(context, token).buildAsync()
        future.addListener(
            { controller = future.get() },
            MoreExecutors.directExecutor(),
        )
        // Always release the controller connection when this leaves the screen.
        onDispose {
            MediaController.releaseFuture(future)
            controller = null
        }
    }

    var isPlaying by remember { mutableStateOf(false) }

    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Button(
            enabled = controller != null,
            onClick = {
                val c = controller ?: return@Button
                if (isPlaying) c.pause() else c.play()
                isPlaying = !isPlaying
            },
        ) {
            Text(
                when {
                    controller == null -> "CONNECTING…"
                    isPlaying -> "PAUSE"
                    else -> "PLAY RAIN"
                },
            )
        }
    }
}
