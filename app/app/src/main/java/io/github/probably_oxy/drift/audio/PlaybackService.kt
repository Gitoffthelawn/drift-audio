package io.github.probably_oxy.drift.audio

import android.net.Uri
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import io.github.probably_oxy.drift.R

/**
 * Phase 2b: hosts the ExoPlayer inside a MediaSessionService so playback
 * survives screen-off and the app being backgrounded, and Android shows a
 * media notification with lock-screen controls.
 *
 * Still just the single looping rain file from 2a — only its *home* moved
 * (Activity -> Service). Multiple layers and catalogue wiring come in 2c.
 */
class PlaybackService : MediaSessionService() {

    // Nullable + released in onDestroy: the session/player are heavy native
    // resources tied to this service's lifetime, not the whole app's.
    private var mediaSession: MediaSession? = null

    override fun onCreate() {
        super.onCreate()

        val player = ExoPlayer.Builder(this)
            // Declare we are media playback and let ExoPlayer manage audio
            // focus: it pauses on phone calls / headphone unplug and (by
            // default) interrupts other apps' audio when we start. See the
            // note in chat about coexisting with other media.
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(C.USAGE_MEDIA)
                    .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
                    .build(),
                /* handleAudioFocus = */ true,
            )
            .build()
            .apply {
                val uri = Uri.parse("android.resource://$packageName/${R.raw.aud_rain_0}")
                setMediaItem(MediaItem.fromUri(uri))
                repeatMode = Player.REPEAT_MODE_ALL
                prepare()
            }

        // The MediaSession is the bridge that exposes our player to the
        // system (notification, lock screen, Bluetooth, other apps).
        mediaSession = MediaSession.Builder(this, player).build()
    }

    // Called when a controller (our Activity) wants to connect.
    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? =
        mediaSession

    override fun onDestroy() {
        mediaSession?.run {
            player.release()
            release()
        }
        mediaSession = null
        super.onDestroy()
    }
}
