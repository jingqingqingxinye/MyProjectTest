package com.example.jing.myprojectfortest.presenter;

import android.util.Log;
import com.example.jing.myprojectfortest.contract.HomeContract;
import com.example.jing.myprojectfortest.module.recommend.BaseRecommendModel;
import com.example.jing.myprojectfortest.network.RequestCenter;
import com.example.mysdkforandroid.okhttp.listener.DisposeDataListener;

/**
 * Created by jing on 2018/10/19.
 */

public class HomePresenter implements HomeContract.Presenter {

  private BaseRecommendModel mRecommendData;
  private HomeContract.View mView;

  public HomePresenter(HomeContract.View view) {
    mView = view;
  }

  @Override public void start() {

  }

  @Override public void requestRecommendData() {
    RequestCenter.requestRecommendData(new DisposeDataListener() {
      @Override public void onSuccess(Object responseObj) {
        mRecommendData = (BaseRecommendModel) responseObj;
        Log.d("JINGQINGQING", "SUCCESS");
        //更新UI
        mView.showSuccessView(mRecommendData);
      }

      @Override public void onFailure(Object reasonObj) {
        Log.d("JINGQINGQING", "ERROR");
        mView.showToast("error");
      }
    });
  }
}
