package com.bdssmdkacem.tankwar;

import android.app.Activity;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
    private TextView status;
    private WebView view;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        FrameLayout root = new FrameLayout(this);
        root.setBackgroundColor(Color.rgb(45, 127, 43));

        status = new TextView(this);
        status.setText("TANK WAR\nLoading game HTML...");
        status.setTextColor(Color.WHITE);
        status.setTextSize(17);
        status.setGravity(android.view.Gravity.CENTER);
        status.setBackgroundColor(Color.rgb(45, 127, 43));
        status.setPadding(24, 24, 24, 24);

        view = new WebView(this);
        view.setBackgroundColor(Color.rgb(45, 127, 43));

        view.addJavascriptInterface(new Object() {
            @JavascriptInterface
            public void ready() {
                runOnUiThread(() -> status.setVisibility(android.view.View.GONE));
            }

            @JavascriptInterface
            public void error(String message) {
                runOnUiThread(() -> {
                    status.setVisibility(android.view.View.VISIBLE);
                    status.setText("TANK WAR JS ERROR\n" + message);
                });
            }
        }, "TankBridge");

        WebSettings settings = view.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setAllowContentAccess(true);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setMediaPlaybackRequiresUserGesture(false);

        view.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView v, String url) {
                status.setText("TANK WAR\nHTML loaded - checking JavaScript...");
            }

            @Override
            public void onReceivedError(
                    WebView v,
                    WebResourceRequest request,
                    WebResourceError error
            ) {
                if (request.isForMainFrame()) {
                    status.setVisibility(android.view.View.VISIBLE);
                    status.setText(
                            "WEBVIEW ERROR\n"
                                    + error.getErrorCode()
                                    + "\n"
                                    + error.getDescription()
                    );
                }
            }
        });

        view.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onConsoleMessage(ConsoleMessage message) {
                String text = "WEB CONSOLE\n"
                        + message.messageLevel()
                        + ": "
                        + message.message()
                        + " (line "
                        + message.lineNumber()
                        + ")";
                status.setVisibility(android.view.View.VISIBLE);
                status.setText(text);
                return true;
            }
        });

        root.addView(view, new FrameLayout.LayoutParams(-1, -1));
        root.addView(status, new FrameLayout.LayoutParams(-1, -1));
        setContentView(root);

        try {
            InputStream input = getAssets().open("index.html");
            byte[] bytes = new byte[input.available()];
            int offset = 0;
            while (offset < bytes.length) {
                int read = input.read(bytes, offset, bytes.length - offset);
                if (read < 0) break;
                offset += read;
            }
            input.close();
            String html = new String(bytes, 0, offset, StandardCharsets.UTF_8);
            status.setText("TANK WAR\\nHTML asset read - starting WebView...");
            view.loadDataWithBaseURL(
                    "file:///android_asset/",
                    html,
                    "text/html",
                    "UTF-8",
                    null
            );
        } catch (Exception e) {
            status.setVisibility(android.view.View.VISIBLE);
            status.setText("ASSET LOAD ERROR\\n" + e.toString());
        }
    }

    @Override
    public void onBackPressed() {
    }
}
