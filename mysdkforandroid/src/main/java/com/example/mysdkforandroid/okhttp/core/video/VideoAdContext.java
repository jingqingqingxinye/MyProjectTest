package com.example.mysdkforandroid.okhttp.core.video;

import android.content.Intent;
import android.view.ViewGroup;
import com.example.mysdkforandroid.okhttp.activity.AdBrowserActivity;
import com.example.mysdkforandroid.okhttp.adutil.ResponseEntityToModule;
import com.example.mysdkforandroid.okhttp.adutil.Utils;
import com.example.mysdkforandroid.okhttp.core.AdContextInterface;
import com.example.mysdkforandroid.okhttp.module.AdValue;
import com.example.mysdkforandroid.okhttp.okhttp.response.HttpConstant;
import com.example.mysdkforandroid.okhttp.report.ReportManager;
import com.example.mysdkforandroid.okhttp.widget.CustomVideoView;

/**
 * Created by jing on 2018/10/26.
 */

public class VideoAdContext implements VideoAdSlot.AdSDKSlotListener {

  //the ad container
  private ViewGroup mParentView;
  private VideoAdSlot mAdSlot;
  private AdValue mInstance = null;
  //the listener to the app layer
  private AdContextInterface mListener;
  private CustomVideoView.ADFrameImageLoadListener mFrameLoadListener;

  public VideoAdContext(ViewGroup parentView, String instance,
      CustomVideoView.ADFrameImageLoadListener frameImageLoadListener) {
    this.mParentView = parentView;
    this.mInstance = (AdValue) ResponseEntityToModule.parseJsonToModule(instance, AdValue.class);
    this.mFrameLoadListener = frameImageLoadListener;
    load();
  }

  /**
   * init the ad,不调用则不会创建videoview
   */
  private void load() {
    if (mInstance != null && mInstance.resource != null) {
      mAdSlot = new VideoAdSlot(mInstance, this, mFrameLoadListener);
      //发送解析事件
      sendAnalizeReport(HttpConstant.Params.ad_analize, HttpConstant.AD_DATA_SUCCESS);
    } else {
      mAdSlot = new VideoAdSlot(null, this, mFrameLoadListener);//创建空的slot，不响应任何事件
      if (mListener != null) {
        mListener.onAdFailed();
      }
      sendAnalizeReport(HttpConstant.Params.ad_analize, HttpConstant.AD_DATA_FAILED);
    }
  }

  /**
   * 发送广告数据解析成功监测
   * @param step
   * @param result
   */
  private void sendAnalizeReport(HttpConstant.Params step, String result) {
    ReportManager.sendAdMonitor(Utils.isPad(mParentView.getContext().getApplicationContext()),
        mInstance == null ? "" : mInstance.resourceID,
        (mInstance == null ? "" : mInstance.adid),
        Utils.getAppVersion(mParentView.getContext().getApplicationContext()), step, result);

  }

  /**
   * release the ad
   */
  public void destroy() {
    mAdSlot.destroy();
  }

  public void setAdResultListener(AdContextInterface listener) {
    this.mListener = listener;
  }

  /**
   * 根据滑动距离来判断是否可以自动播放，出现超过50%自动播放，离开超过50%，自耦东暂停
   */
  public void updateAdInScrollView() {
    if (mAdSlot != null) {
      mAdSlot.updateAdInScrollView();
    }
  }

  @Override public ViewGroup getAdParent() {
    return mParentView;
  }

  @Override public void onAdVideoLoadSuccess() {
    if (mListener != null) {
      mListener.onAdSucess();
    }
    sendAnalizeReport(HttpConstant.Params.ad_load, HttpConstant.AD_DATA_SUCCESS);
  }

  @Override public void onAdVideoLoadFailed() {
    if (mListener != null) {
      mListener.onAdFailed();
    }
    sendAnalizeReport(HttpConstant.Params.ad_load, HttpConstant.AD_PLAY_FAILED);
  }

  @Override public void onAdVideoLoadComplete() {

  }

  @Override public void onClickVideo(String url) {
    if (mListener != null) {
      mListener.onClickVideo(url);
    } else {
      Intent intent = new Intent(mParentView.getContext(), AdBrowserActivity.class);
      intent.putExtra(AdBrowserActivity.KEY_URL, url);
      mParentView.getContext().startActivity(intent);
    }
  }
}
