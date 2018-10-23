package com.example.mysdkforandroid.okhttp.widget.adbrowser;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import com.example.mysdkforandroid.okhttp.adutil.Utils;

/**
 * Created by jing on 2018/10/22.
 */

public class AdBrowserLayout extends RelativeLayout {

  private static final int HEADER_HEIGHT_DP = 45;

  private RelativeLayout mFooterView;
  private ProgressBar mProgressBar;
  private Button mBackBtn;
  private Button mRefreshBtn;
  private Button mCloseBtn;
  private Button mNativeBtn;
  private BrowserWebView mAdBrowserWebView;

  private Base64Drawables mBase64Drawables = new Base64Drawables();

  public AdBrowserLayout(Context context) {
    super(context);
    LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    setLayoutParams(params);

    mAdBrowserWebView = new BrowserWebView(context);
    mAdBrowserWebView.setLayoutParams(params);
    addView(mAdBrowserWebView);

    mFooterView = new RelativeLayout(context);
    configFooterView();

    LinearLayout buttonContainer = new LinearLayout(context);
    configButtonsContainer(buttonContainer);

    int buttonWidth = Utils.getDisplayMetrics(context).widthPixels / 5;
    LayoutParams buttons_params = new LayoutParams(buttonWidth, ViewGroup.LayoutParams.MATCH_PARENT);

    int size = Utils.dip2px(context, HEADER_HEIGHT_DP) / 2;
    LayoutParams pb_params = new LayoutParams(size, size);
    pb_params.addRule(RelativeLayout.CENTER_IN_PARENT);

    mProgressBar = new ProgressBar(context);
    configProgressButton(context, buttonContainer, buttons_params, pb_params);

    mBackBtn = new Button(context);
    configBackButton(context, buttonContainer, buttons_params, pb_params);

    mRefreshBtn = new Button(context);
    configRefreshButton(context, buttonContainer, buttons_params, pb_params);

    mNativeBtn = new Button(context);
    configNativeButton(context, buttonContainer, buttons_params, pb_params);

    mCloseBtn = new Button(context);
    configCloseButton(context, buttonContainer, buttons_params, pb_params);

    mFooterView.addView(initBottomWhiteLineView(context));

  }

  private View initBottomWhiteLineView(Context context) {
    View whiteLine = new View(context);
    LayoutParams whiteLineParams = new LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT, 1);
    whiteLineParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
    whiteLine.setLayoutParams(whiteLineParams);
    whiteLine.setBackgroundColor(Color.WHITE);
    return whiteLine;
  }

  private void configCloseButton(Context context, LinearLayout buttonsContainer,
      LayoutParams buttons_params,
      LayoutParams pb_params) {
    RelativeLayout closeLayout = new RelativeLayout(context);
    closeLayout.setLayoutParams(buttons_params);
    if (Build.VERSION.SDK_INT < 16) {
      mCloseBtn.setBackgroundDrawable(Utils.decodeImage(mBase64Drawables.getClose()));
    } else {
      mCloseBtn.setBackground(Utils.decodeImage(mBase64Drawables.getClose()));
    }
    mCloseBtn.setLayoutParams(pb_params);
    closeLayout.addView(mCloseBtn);
    buttonsContainer.addView(closeLayout);
  }

  private void configNativeButton(Context context, LinearLayout buttonsContainer,
      LayoutParams buttons_params,
      LayoutParams pb_params) {
    RelativeLayout nativeLayout = new RelativeLayout(context);
    nativeLayout.setLayoutParams(buttons_params);
    if (Build.VERSION.SDK_INT < 16) {
      mNativeBtn.setBackgroundDrawable(Utils.decodeImage(mBase64Drawables.getNativeBrowser()));
    } else {
      mNativeBtn.setBackground(Utils.decodeImage(mBase64Drawables.getNativeBrowser()));
    }
    mNativeBtn.setLayoutParams(pb_params);
    nativeLayout.addView(mNativeBtn);
    buttonsContainer.addView(nativeLayout);
  }

  private void configRefreshButton(Context context, LinearLayout buttonContainer,
      LayoutParams buttons_params, LayoutParams pb_params) {
  }

  private void configBackButton(Context context, LinearLayout buttonContainer,
      LayoutParams buttons_params, LayoutParams pb_params) {
  }

  private void configProgressButton(Context context, LinearLayout buttonContainer,
      LayoutParams buttons_params, LayoutParams pb_params) {

    RelativeLayout progressLayout = new RelativeLayout(context);
    progressLayout.setLayoutParams(buttons_params);
    mProgressBar.setLayoutParams(pb_params);
    progressLayout.addView(mProgressBar);
    buttonContainer.addView(progressLayout);
  }

  private void configButtonsContainer(LinearLayout buttonsContainer) {
    LinearLayout.LayoutParams buttonsContainerParams = new LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    buttonsContainer.setLayoutParams(buttonsContainerParams);
    buttonsContainer.setBackgroundColor(Color.BLACK);
    mFooterView.addView(buttonsContainer);
  }

  private void configFooterView() {
    LayoutParams footer_params = new LayoutParams(LayoutParams.MATCH_PARENT, Utils.dip2px(getContext(), HEADER_HEIGHT_DP));
    footer_params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
    mFooterView.setLayoutParams(footer_params);
    addView(mFooterView);
  }

  public AdBrowserLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public AdBrowserLayout(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  public ProgressBar getProgressBar() {
    return mProgressBar;
  }

  public Button getBackButton() {
    return mBackBtn;
  }

  public Button getRefreshButton() {
    return mRefreshBtn;
  }

  public Button getCloseButton() {
    return mCloseBtn;
  }

  public Button getNativeButton() {
    return mNativeBtn;
  }

  public BrowserWebView getWebView() {
    return mAdBrowserWebView;
  }
}
