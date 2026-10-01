package com.bdssmdkacem.tankwar;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.os.Handler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
    private TextView status;
    private WebView view;
    private final Handler handler = new Handler();

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

        view = new WebView(this);
        view.setBackgroundColor(Color.rgb(45, 127, 43));

        status = new TextView(this);
        status.setText("TANK WAR  |  Loading...");
        status.setTextColor(Color.WHITE);
        status.setTextSize(11);
        status.setGravity(Gravity.CENTER_VERTICAL);
        status.setPadding(10, 0, 10, 0);
        status.setBackgroundColor(Color.rgb(25, 25, 25));

        view.addJavascriptInterface(new Object() {
            @JavascriptInterface
            public void ready() {
                runOnUiThread(() -> status.setVisibility(android.view.View.GONE));
            }

            @JavascriptInterface
            public void error(String message) {
                runOnUiThread(() -> {
                    status.setVisibility(android.view.View.VISIBLE);
                    status.setText("TANK WAR JS ERROR: " + message);
                });
            }
        }, "TankBridge");

        WebSettings settings = view.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setAllowContentAccess(true);
        settings.setAllowFileAccessFromFileURLs(true);
        settings.setAllowUniversalAccessFromFileURLs(false);
        settings.setLoadWithOverviewMode(false);
        settings.setUseWideViewPort(false);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setMediaPlaybackRequiresUserGesture(false);

        view.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView v, String url, android.graphics.Bitmap favicon) {
                status.setVisibility(android.view.View.VISIBLE);
                status.setText("TANK WAR  |  Page started");
            }

            @Override
            public void onPageFinished(WebView v, String url) {
                status.setVisibility(android.view.View.VISIBLE);
                status.setText("TANK WAR  |  HTML loaded");
                v.postDelayed(() -> v.evaluateJavascript(
                        "(function(){return JSON.stringify({ready:document.readyState,canvas:!!document.getElementById('c'),tankReady:!!window.__tankWarReady,w:innerWidth,h:innerHeight});})()",
                        value -> {
                            if (value != null) {
                                status.setText("TANK WAR  |  JS STATE: " + value);
                            }
                        }
                ), 800);
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
                            "WEBVIEW ERROR  |  "
                                    + error.getErrorCode()
                                    + "  |  "
                                    + error.getDescription()
                    );
                }
            }
        });

        view.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onConsoleMessage(ConsoleMessage message) {
                status.setVisibility(android.view.View.VISIBLE);
                status.setText(
                        "JS  |  "
                                + message.message()
                                + "  | line "
                                + message.lineNumber()
                );
                return true;
            }
        });

        root.addView(view, new FrameLayout.LayoutParams(
                -1, -1, Gravity.TOP
        ));

        FrameLayout.LayoutParams statusParams = new FrameLayout.LayoutParams(
                -1, dp(34), Gravity.TOP
        );
        root.addView(status, statusParams);
        setContentView(root);

        status.setText("TANK WAR  |  Loading asset...");
        view.loadUrl("file:///android_asset/index.html");

        handler.postDelayed(() -> {
            if (status.getVisibility() == android.view.View.VISIBLE
                    && status.getText().toString().contains("Loading")) {
                status.setText("TANK WAR  |  LOAD TIMEOUT | URL=" + view.getUrl());
            }
        }, 5000);
    }

    private int dp(int value) {
        return Math.round(value * getResources().getDisplayMetrics().density);
    }

    @Override
    public void onBackPressed() {
    }
}
