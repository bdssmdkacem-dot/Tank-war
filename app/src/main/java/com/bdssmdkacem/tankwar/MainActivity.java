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

        TextView status = new TextView(this);
        status.setText("TANK WAR\nLoading packaged HTML…");
        status.setTextColor(Color.WHITE);
        status.setTextSize(18);
        status.setGravity(android.view.Gravity.CENTER);
        root.addView(status, new FrameLayout.LayoutParams(-1, -1));

        WebView view = new WebView(this);
        view.setBackgroundColor(Color.WHITE);
        view.setWebViewClient(new WebViewClient() {
            @Override public void onPageFinished(WebView v, String url) {
                status.setVisibility(android.view.View.GONE);
            }
            @Override public void onReceivedError(WebView v, int code, String desc, String url) {
                status.setVisibility(android.view.View.VISIBLE);
                status.setText("ASSET LOAD ERROR\n" + code + "\n" + desc);
            }
        });

        WebSettings s = view.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        s.setAllowFileAccess(true);
        s.setAllowContentAccess(true);

        root.addView(view, new FrameLayout.LayoutParams(-1, -1));
        setContentView(root);

        view.loadUrl("file:///android_asset/diagnostic.html");
    }

    @Override public void onBackPressed() { }
}
