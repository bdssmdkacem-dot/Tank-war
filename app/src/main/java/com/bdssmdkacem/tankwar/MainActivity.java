package com.bdssmdkacem.tankwar;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.TextView;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class MainActivity extends Activity {
    private TextView status;

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        FrameLayout root = new FrameLayout(this);
        root.setBackgroundColor(Color.rgb(45, 127, 43));

        status = new TextView(this);
        status.setText("TANK WAR\nLoading game engine…");
        status.setTextColor(Color.WHITE);
        status.setTextSize(18);
        status.setGravity(android.view.Gravity.CENTER);
        status.setTypeface(android.graphics.Typeface.MONOSPACE, android.graphics.Typeface.BOLD);
        root.addView(status, new FrameLayout.LayoutParams(-1, -1));

        WebView view = new WebView(this);
        // Keep the default hardware-accelerated WebView path.
        // Software WebView rendering caused a black surface on affected devices.
        view.setBackgroundColor(Color.rgb(45, 127, 43));

        view.setWebViewClient(new WebViewClient() {
            @Override public void onPageFinished(WebView v, String url) {
                status.setVisibility(android.view.View.GONE);
            }

            @Override public void onReceivedError(WebView v, int errorCode,
                                                   String description, String failingUrl) {
                status.setVisibility(android.view.View.VISIBLE);
                status.setText("TANK WAR\nWEBVIEW ERROR\n" + errorCode + "\n" + description);
            }
        });

        view.setWebChromeClient(new WebChromeClient() {
            @Override public boolean onConsoleMessage(ConsoleMessage message) {
                android.util.Log.e("TankWar",
                        message.message() + " @" + message.lineNumber());
                return true;
            }
        });

        WebSettings s = view.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        s.setAllowFileAccess(true);
        s.setAllowContentAccess(true);
        s.setBuiltInZoomControls(false);
        s.setDisplayZoomControls(false);
        s.setUseWideViewPort(true);
        s.setLoadWithOverviewMode(true);

        root.addView(view, new FrameLayout.LayoutParams(-1, -1));
        setContentView(root);

        loadGame(view);
    }

    private void loadGame(WebView view) {
        try {
            InputStream in = getAssets().open("index.html");
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[8192];
            int n;
            while ((n = in.read(buffer)) != -1) out.write(buffer, 0, n);
            in.close();

            String html = out.toString(StandardCharsets.UTF_8.name());
            if (html.length() < 1000) {
                throw new IllegalStateException("index.html is unexpectedly small");
            }

            view.loadDataWithBaseURL(
                    "file:///android_asset/",
                    html,
                    "text/html",
                    "UTF-8",
                    null
            );
        } catch (Exception e) {
            android.util.Log.e("TankWar", "Cannot load packaged game", e);
            status.setVisibility(android.view.View.VISIBLE);
            status.setText("TANK WAR\nASSET ERROR\n" +
                    e.getClass().getSimpleName() + "\n" + String.valueOf(e.getMessage()));
        }
    }

    @Override public void onBackPressed() { }
}
