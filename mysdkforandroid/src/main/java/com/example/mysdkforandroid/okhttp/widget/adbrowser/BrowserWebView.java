package com.example.mysdkforandroid.okhttp.widget.adbrowser;

import android.content.Context;
import android.webkit.WebSettings;
import android.webkit.WebView;

/**
 * Created by jing on 2018/10/22.
 * @function 自定义WebView,设置一些能用的参数
 */

public class BrowserWebView extends WebView {

  public BrowserWebView(Context context) {
    super(context);
    initSettings();
  }

  private void initSettings() {
    WebSettings webSettings = getSettings();
    webSettings.setJavaScriptEnabled(true);

    webSettings.setPluginState(WebSettings.PluginState.ON);
    webSettings.setBuiltInZoomControls(true);
    webSettings.setLoadWithOverviewMode(true);
    webSettings.setUseWideViewPort(true);
    setInitialScale(1);
  }
}
