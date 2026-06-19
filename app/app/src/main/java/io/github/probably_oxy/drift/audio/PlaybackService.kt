package io.github.probably_oxy.drift.audio

import android.os.Bundle
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import androidx.media3.session.SessionCommand
import androidx.media3.session.SessionResult
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import io.github.probably_oxy.drift.data.Catalogue

/**
 * Phase 2c: the MediaSessionService now hosts a [PlaybackEngine] (many layers)
 * fronted by a [MixerPlayer] (one transport for the system). Standard
 * play/pause/stop flow through the MixerPlayer; selecting which sounds are in
 * the mix uses a custom session command, since "toggle this layer" isn't a
 * standard Player operation.
 */
class PlaybackService : MediaSessionService() {

    private var mediaSession: MediaSession? = null
    private lateinit var engine: PlaybackEngine

    override fun onCreate() {
        super.onCreate()
        engine = PlaybackEngine(this)
        val player = MixerPlayer(engine)
        mediaSession = MediaSession.Builder(this, player)
            .setCallback(MixerCallback())
            .build()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? =
        mediaSession

    override fun onDestroy() {
        mediaSession?.run {
            player.release() // MixerPlayer.handleRelease() releases the engine
            release()
        }
        mediaSession = null
        super.onDestroy()
    }

    /** Grants and handles Drift's custom session commands. */
    private inner class MixerCallback : MediaSession.Callback {

        override fun onConnect(
            session: MediaSession,
            controller: MediaSession.ControllerInfo,
        ): MediaSession.ConnectionResult {
            val sessionCommands =
                MediaSession.ConnectionResult.DEFAULT_SESSION_COMMANDS.buildUpon()
                    .add(SessionCommand(ACTION_TOGGLE_LAYER, Bundle.EMPTY))
                    .add(SessionCommand(ACTION_SET_VOLUME, Bundle.EMPTY))
                    .build()
            return MediaSession.ConnectionResult.AcceptedResultBuilder(session)
                .setAvailableSessionCommands(sessionCommands)
                .build()
        }

        override fun onCustomCommand(
            session: MediaSession,
            controller: MediaSession.ControllerInfo,
            customCommand: SessionCommand,
            args: Bundle,
        ): ListenableFuture<SessionResult> {
            when (customCommand.customAction) {
                ACTION_TOGGLE_LAYER -> {
                    val id = args.getString(KEY_SOUND_ID)
                    val source = id?.let { Catalogue.byId(it) }?.let { AudioFiles.sourceFor(it) }
                    if (source != null) {
                        engine.toggleLayer(source)
                        return Futures.immediateFuture(SessionResult(SessionResult.RESULT_SUCCESS))
                    }
                }
                ACTION_SET_VOLUME -> {
                    val id = args.getString(KEY_SOUND_ID)
                    if (id != null && args.containsKey(KEY_VOLUME)) {
                        engine.setVolume(id, args.getFloat(KEY_VOLUME))
                        return Futures.immediateFuture(SessionResult(SessionResult.RESULT_SUCCESS))
                    }
                }
            }
            return Futures.immediateFuture(SessionResult(SessionResult.RESULT_ERROR_NOT_SUPPORTED))
        }
    }

    companion object {
        const val ACTION_TOGGLE_LAYER = "io.github.probably_oxy.drift.TOGGLE_LAYER"
        const val ACTION_SET_VOLUME = "io.github.probably_oxy.drift.SET_VOLUME"
        const val KEY_SOUND_ID = "soundId"
        const val KEY_VOLUME = "volume"
    }
}
