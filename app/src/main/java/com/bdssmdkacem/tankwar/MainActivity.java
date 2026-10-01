package com.bdssmdkacem.tankwar;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebChromeClient;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
    private TextView status;
    private WebView view;
    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        FrameLayout root = new FrameLayout(this);
        root.setBackgroundColor(Color.rgb(45, 127, 43));

        view = new WebView(this);
        view.setBackgroundColor(Color.rgb(45, 127, 43));
        view.setWebChromeClient(new WebChromeClient());
        view.addJavascriptInterface(new Object() {
            @JavascriptInterface public void ready() {
                runOnUiThread(() -> status.setVisibility(android.view.View.GONE));
            }
            @JavascriptInterface public void error(String message) {
                runOnUiThread(() -> {
                    status.setVisibility(android.view.View.VISIBLE);
                    status.setText("TANK WAR JS ERROR\\n" + message);
                });
            }
        }, "TankBridge");

        final status = new TextView(this);
        status.setText("TANK WAR\nLoading game HTML…");
        status.setTextColor(Color.WHITE);
        status.setTextSize(17);
        status.setGravity(android.view.Gravity.CENTER);
        status.setBackgroundColor(Color.rgb(45, 127, 43));
        status.setPadding(24, 24, 24, 24);

        WebSettings s = view.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        s.setAllowFileAccess(true);
        s.setAllowContentAccess(true);\n        s.setLoadWithOverviewMode(true);\n        s.setUseWideViewPort(true);
        s.setCacheMode(WebSettings.LOAD_NO_CACHE);
        s.setMediaPlaybackRequiresUserGesture(false);

        view.setWebViewClient(new WebViewClient() {
            @Override public void onPageFinished(WebView v, String url) {
                status.setText("TANK WAR\nHTML loaded — checking JavaScript…");
                v.evaluateJavascript(
                    "(function(){try{return 'readyState='+document.readyState+';canvas='+(!!document.getElementById('c'))+';boot='+(!!document.getElementById('bootError'));}catch(e){return 'EVAL='+e;}})()",
                    value -> {
                        status.setText("TANK WAR\n" + value.replace("\\"", """));
                        v.postDelayed(() -> {
                            v.evaluateJavascript(
                                "(function(){return window.__tankWarReady?'GAME READY':'GAME NOT READY';})()",
                                ready -> {
                                    if (ready != null && ready.contains("GAME READY")) {
                                        status.setVisibility(android.view.View.GONE);
                                    } else {
                                        status.setText("TANK WAR\n" + ready);
                                    }
                                });
                        }, 1200);
                    });
            }

            @Override public void onReceivedError(WebView v, WebResourceRequest req, WebResourceError err) {
                status.setText("WEBVIEW ERROR\n" + err.getErrorCode() + "\n" + err.getDescription());
            }
        });

        view.setWebChromeClient(new WebChromeClient() {
            @Override public boolean onConsoleMessage(ConsoleMessage cm) {
                String msg = cm.messageLevel() + ": " + cm.message() + " (line " + cm.lineNumber() + ")";
                status.setText("WEB CONSOLE\n" + msg);
                return true;
            }
        });

        root.addView(view, new FrameLayout.LayoutParams(-1, -1));
        FrameLayout.LayoutParams sp = new FrameLayout.LayoutParams(-1, -1);
        root.addView(status, sp);
        setContentView(root);

        view.loadUrl("file:///android_asset/index.html");
    }

    @Override public void onBackPressed() { }
}
