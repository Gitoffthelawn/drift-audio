package io.github.probably_oxy.drift;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import com.getcapacitor.BridgeActivity;

public class MainActivity extends BridgeActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        registerPlugin(BackgroundAudioPlugin.class);
        // Window configuration must happen BEFORE super.onCreate() so that
        // Capacitor's setContentView() receives an already-configured window.
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().setNavigationBarColor(Color.TRANSPARENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            getWindow().setStatusBarContrastEnforced(false);
            getWindow().setNavigationBarContrastEnforced(false);
        }
        super.onCreate(savedInstanceState);
        hideSystemUI();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Capacitor's Bridge installs a ViewCompat.setOnApplyWindowInsetsListener on
        // its WebView that explicitly pads it by the system bar inset values.
        // setFitsSystemWindows(false) does NOT override a custom listener, so we must
        // replace the listener itself and set the background colour so the WebView
        // fills the full screen with no inset gap.
        if (bridge != null && bridge.getWebView() != null) {
            bridge.getWebView().setFitsSystemWindows(false);
            bridge.getWebView().setBackgroundColor(Color.parseColor("#0a0f0a"));
            ViewCompat.setOnApplyWindowInsetsListener(bridge.getWebView(),
                (v, insets) -> WindowInsetsCompat.CONSUMED);
        }
        hideSystemUI();
    }

    private void hideSystemUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowInsetsController controller = getWindow().getInsetsController();
            if (controller != null) {
                controller.hide(WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars());
                controller.setSystemBarsBehavior(
                    WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
            }
        } else {
            View decorView = getWindow().getDecorView();
            //noinspection deprecation
            decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) hideSystemUI();
    }
}
