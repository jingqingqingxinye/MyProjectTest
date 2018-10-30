package com.example.mysdkforandroid.okhttp.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.example.mysdkforandroid.R;
import com.example.mysdkforandroid.okhttp.activity.AdBrowserActivity;
import com.example.mysdkforandroid.okhttp.adutil.LogUtils;
import com.example.mysdkforandroid.okhttp.adutil.Utils;
import com.example.mysdkforandroid.okhttp.constant.SDKConstant;
import com.example.mysdkforandroid.okhttp.core.video.VideoAdSlot;
import com.example.mysdkforandroid.okhttp.module.AdValue;
import com.example.mysdkforandroid.okhttp.report.ReportManager;

/**
 * Created by jing on 2018/10/25.
 */

public class VideoFullDialog extends Dialog implements CustomVideoView.ADVideoPlayerListener {

  private static final String TAG = VideoFullDialog.class.getSimpleName();
  private CustomVideoView mVideoView;

  private Context mContext;
  private RelativeLayout mRootView;
  private ViewGroup mParentView;
  private ImageView mBackButton;

  private AdValue mXAdInstance;
  private int mPosition;
  private boolean isFirst = true;
  //动画要执行的平移值
  private int deltaY;

  private VideoAdSlot.AdSDKSlotListener mSlotListener;
  private FullToSmallListener mListener;
  private Bundle mStartBundle;
  private Bundle mEndBundle; //用于Dialog出入场动画


  public VideoFullDialog(Context context, CustomVideoView mraidView, AdValue instance,
      int position) {
    super(context, R.style.dialog_full_screen);
    mContext = context;
    mXAdInstance = instance;
    mPosition = position;
    mVideoView = mraidView;
  }

  public void setSlotListener(VideoAdSlot.AdSDKSlotListener slotListener) {
    this.mSlotListener = slotListener;
  }

  public void setListener(FullToSmallListener listener) {
    this.mListener = listener;
  }


  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
        WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.xadsdk_dialog_video_layout);
    initVideoView();
  }

  private void initVideoView() {
    mParentView = (RelativeLayout) findViewById(R.id.content_layout);
    mBackButton = (ImageView) findViewById(R.id.xadsdk_player_close_btn);
    mBackButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onClickBackBtn();
      }
    });
    mRootView = (RelativeLayout) findViewById(R.id.root_view);
    mRootView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
          onClickVideo();
      }
    });
    mRootView.setVisibility(View.INVISIBLE);

    mVideoView.setListener(this);
    mVideoView.mute(false);
    mParentView.addView(mVideoView);
    mParentView.getViewTreeObserver().addOnPreDrawListener(
        new ViewTreeObserver.OnPreDrawListener() {
          @Override public boolean onPreDraw() {
            mParentView.getViewTreeObserver().removeOnPreDrawListener(this);
            prepareScene();
            runEnterAnimation();
            return true;
          }
        });
  }
  //准备入场动画
  private void runEnterAnimation() {
    mVideoView.animate()
        .setDuration(200)
        .setInterpolator(new LinearInterpolator())
        .translationY(0)
        .withStartAction(new Runnable() {
          @Override public void run() {
            mRootView.setVisibility(View.VISIBLE);
          }
        }).start();
  }

  //准备出场动画
  private void runExitAnimator() {
    mVideoView.animate()
        .setDuration(200)
        .setInterpolator(new LinearInterpolator())
        .translationY(deltaY)
        .withEndAction(new Runnable() {
          @Override
          public void run() {
            dismiss();
            try {
              ReportManager.exitfullScreenReport(mXAdInstance.event.exitFull.content, mVideoView.getCurrentPosition()
                  / SDKConstant.MILLION_UNIT);
            } catch (Exception e) {
              e.printStackTrace();
            }
            if (mListener != null) {
              mListener.getCurrentPlayPosition(mVideoView.getCurrentPosition());
            }
          }
        }).start();
  }

  //准备动画所需数据
  private void prepareScene() {
    mEndBundle = Utils.getViewProperty(mVideoView);
    /**
     * 将desationview移动到originalview位置处
     */
    deltaY = (mStartBundle.getInt(Utils.PROPNAME_SCREENLOCATION_TOP) 
        - mEndBundle.getInt(Utils.PROPNAME_SCREENLOCATION_TOP));
    mVideoView.setTranslationY(deltaY);
  }

  public void setViewBundle(Bundle bundle) {
    mStartBundle = bundle;
  }

  @Override public void onBufferUpdate(int time) {
    try {
      if (mXAdInstance != null) {
        ReportManager.suReport(mXAdInstance.middleMonitor, time / SDKConstant.MILLION_UNIT);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override public void onClickFullScreenBtn() {
    onClickVideo();
  }

  @Override public void onClickVideo() {
    String desationUrl = mXAdInstance.clickUrl;
    if (mSlotListener != null) {
      if (mVideoView.isFrameHidden() && !TextUtils.isEmpty(desationUrl)) {
        mSlotListener.onClickVideo(desationUrl);
        try {
          ReportManager.pauseVideoReport(mXAdInstance.clickMonitor, mVideoView.getCurrentPosition()
              / SDKConstant.MILLION_UNIT);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    } else {
      //走默认样式
      if (mVideoView.isFrameHidden() && !TextUtils.isEmpty(desationUrl)) {
        Intent intent = new Intent(mContext, AdBrowserActivity.class);
        intent.putExtra(AdBrowserActivity.KEY_URL, mXAdInstance.clickUrl);
        mContext.startActivity(intent);
        try {
          ReportManager.pauseVideoReport(mXAdInstance.clickMonitor, mVideoView.getCurrentPosition()
              / SDKConstant.MILLION_UNIT);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }

  }

  @Override public void onClickBackBtn() {
    runExitAnimator();
  }

  @Override public void onClickPlay() {

  }

  @Override public void onAdVideoLoadSuccess() {
    if (mVideoView != null) {
      mVideoView.resume();
    }
  }

  @Override public void onAdVideoLoadFaied() {

  }

  @Override public void onWindowFocusChanged(boolean hasFocus) {
    LogUtils.i(TAG, "onWindowFocusChanged");
    mVideoView.isShowFullBtn(false);
    if (!hasFocus) {
      mPosition = mVideoView.getCurrentPosition();
      mVideoView.pauseForFullScreen();
    } else {
      if (isFirst) {
        mVideoView.seekAndResume(mPosition);
      } else {
        mVideoView.resume();
      }
    }
    isFirst = false;
  }

  @Override
  public void dismiss() {
    LogUtils.e(TAG, "dismiss");
    mParentView.removeView(mVideoView);
    super.dismiss();
  }

  @Override
  public void onBackPressed() {
    onClickBackBtn();
    //super.onBackPressed(); 禁止掉返回键本身的关闭功能,转为自己的关闭效果
  }


  @Override public void onAdVideoLoadComplete() {
    try {
      int position = mVideoView.getDuration() / SDKConstant.MILLION_UNIT;
      ReportManager.sueReport(mXAdInstance.endMonitor, true, position);
    } catch (Exception e) {
      e.printStackTrace();
    }

    dismiss();
    if (mListener != null) {
      mListener.playComplete();
    }
  }

  public interface FullToSmallListener {
    void getCurrentPlayPosition(int position);

    void playComplete();//全屏播放结束时回调
  }
}
