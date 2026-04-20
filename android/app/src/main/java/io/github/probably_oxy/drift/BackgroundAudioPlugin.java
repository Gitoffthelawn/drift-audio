package io.github.probably_oxy.drift;

import android.content.Intent;
import android.os.Build;
import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

/**
 * Capacitor plugin that starts/stops the MediaPlaybackService from JavaScript.
 *
 * Usage in app.js:
 *   BackgroundAudio.enable({ title: 'Drift', content: 'Rain · Fireplace' });
 *   BackgroundAudio.disable();
 */
@CapacitorPlugin(name = "BackgroundAudio")
public class BackgroundAudioPlugin extends Plugin {

    @PluginMethod
    public void enable(PluginCall call) {
        String title   = call.getString("title",   "Drift");
        String content = call.getString("content", "Playing ambient sounds");

        Intent intent = new Intent(getContext(), MediaPlaybackService.class);
        intent.setAction(MediaPlaybackService.ACTION_START);
        intent.putExtra("title",   title);
        intent.putExtra("content", content);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getContext().startForegroundService(intent);
        } else {
            getContext().startService(intent);
        }
        call.resolve();
    }

    @PluginMethod
    public void disable(PluginCall call) {
        Intent intent = new Intent(getContext(), MediaPlaybackService.class);
        intent.setAction(MediaPlaybackService.ACTION_STOP);
        getContext().startService(intent);
        call.resolve();
    }
}
