package com.bdssmdkacem.tankwar;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class MainActivity extends Activity {
    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        WebView view = new WebView(this);

        // Keep the known-compatible rendering path for the Canvas game.
        // This was present in the last build where the WebView could render the game.
        view.setLayerType(android.view.View.LAYER_TYPE_SOFTWARE, null);
        view.setBackgroundColor(0xFF101010);

        view.setWebViewClient(new WebViewClient());
        view.setWebChromeClient(new WebChromeClient() {
            @Override public boolean onConsoleMessage(ConsoleMessage message) {
                android.util.Log.e("TankWar", message.message() + " @" + message.lineNumber());
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

        setContentView(view);
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

            view.loadDataWithBaseURL(
                "https://tankwar.local/",
                html,
                "text/html",
                "UTF-8",
                null
            );
        } catch (Exception e) {
            android.util.Log.e("TankWar", "Cannot load packaged game", e);
            String msg = "TANK WAR\n\nGAME ASSET ERROR\n" + e.getClass().getSimpleName() + ": " + e.getMessage();
            view.loadData(
                "<html><body style='background:#111;color:#fff;font:700 18px monospace;padding:30px;white-space:pre-wrap'>"
                + msg.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;")
                + "</body></html>",
                "text/html",
                "UTF-8"
            );
        }
    }

    @Override public void onBackPressed() { }
}
