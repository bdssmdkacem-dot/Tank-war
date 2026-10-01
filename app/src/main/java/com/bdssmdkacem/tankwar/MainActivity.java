package com.bdssmdkacem.tankwar;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings;
import android.widget.FrameLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        FrameLayout root = new FrameLayout(this);
        root.setBackgroundColor(Color.rgb(45, 127, 43));

        TextView nativeLabel = new TextView(this);
        nativeLabel.setText("TANK WAR\nWebView diagnostic");
        nativeLabel.setTextColor(Color.WHITE);
        nativeLabel.setTextSize(18);
        nativeLabel.setGravity(android.view.Gravity.CENTER);
        root.addView(nativeLabel, new FrameLayout.LayoutParams(-1, -1));

        WebView view = new WebView(this);
        view.setBackgroundColor(Color.WHITE);

        view.setWebViewClient(new WebViewClient() {
            @Override public void onPageFinished(WebView v, String url) {
                nativeLabel.setVisibility(android.view.View.GONE);
            }
        });

        WebSettings s = view.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);

        root.addView(view, new FrameLayout.LayoutParams(-1, -1));
        setContentView(root);

        String testHtml =
            "<!doctype html><html><head><meta name='viewport' content='width=device-width,initial-scale=1'>" +
            "<style>html,body{margin:0;width:100%;height:100%;background:#123a8a;color:white;font-family:sans-serif}" +
            "body{display:flex;align-items:center;justify-content:center;font-size:28px}</style></head>" +
            "<body>WEBVIEW OK</body></html>";

        view.loadDataWithBaseURL("https://tankwar.local/", testHtml,
                "text/html", "UTF-8", null);
    }

    @Override public void onBackPressed() { }
}
