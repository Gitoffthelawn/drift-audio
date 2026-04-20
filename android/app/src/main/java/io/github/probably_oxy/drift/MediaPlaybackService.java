package io.github.probably_oxy.drift;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import androidx.core.app.NotificationCompat;

/**
 * Foreground service that keeps the process alive while Drift is playing audio.
 * Without this, Android suspends the WebView (and stops audio) when the screen
 * turns off or the app is backgrounded.
 */
public class MediaPlaybackService extends Service {

    public static final String ACTION_START = "io.github.probably_oxy.drift.START_PLAYBACK";
    public static final String ACTION_STOP  = "io.github.probably_oxy.drift.STOP_PLAYBACK";

    private static final String CHANNEL_ID   = "drift_playback";
    private static final int    NOTIFICATION_ID = 1;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && ACTION_STOP.equals(intent.getAction())) {
            stopForeground(true);
            stopSelf();
            return START_NOT_STICKY;
        }

        createNotificationChannel();

        // Tapping the notification brings the app back to the foreground
        Intent openApp = new Intent(this, MainActivity.class);
        openApp.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingOpen = PendingIntent.getActivity(
            this, 0, openApp,
            PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // Stop action in notification
        Intent stopIntent = new Intent(this, MediaPlaybackService.class);
        stopIntent.setAction(ACTION_STOP);
        PendingIntent pendingStop = PendingIntent.getService(
            this, 0, stopIntent,
            PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        String title   = intent != null ? intent.getStringExtra("title")   : null;
        String content = intent != null ? intent.getStringExtra("content") : null;
        if (title   == null) title   = "Drift";
        if (content == null) content = "Playing ambient sounds";

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(content)
            .setSmallIcon(android.R.drawable.ic_media_play)
            .setContentIntent(pendingOpen)
            .addAction(android.R.drawable.ic_media_pause, "Stop", pendingStop)
            .setOngoing(true)
            .setSilent(true)
            .build();

        startForeground(NOTIFICATION_ID, notification);
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                "Drift Playback",
                NotificationManager.IMPORTANCE_LOW
            );
            channel.setDescription("Shows while Drift is playing");
            channel.setSound(null, null);
            NotificationManager nm = getSystemService(NotificationManager.class);
            if (nm != null) nm.createNotificationChannel(channel);
        }
    }
}
