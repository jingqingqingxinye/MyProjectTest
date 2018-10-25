package com.example.jing.myprojectfortest.network;

import com.example.jing.myprojectfortest.module.recommend.BaseRecommendModel;
import com.example.jing.myprojectfortest.module.update.UpdateModel;
import com.example.jing.myprojectfortest.module.user.User;
import com.example.mysdkforandroid.okhttp.CommonOkHttpClient;
import com.example.mysdkforandroid.okhttp.listener.DisposeDataHandle;
import com.example.mysdkforandroid.okhttp.listener.DisposeDataListener;
import com.example.mysdkforandroid.okhttp.listener.DisposeDownloadListener;
import com.example.mysdkforandroid.okhttp.okhttp.request.CommonRequest;
import com.example.mysdkforandroid.okhttp.okhttp.request.RequestParams;

/**
 * Created by jing on 2018/10/19.
 */

public class RequestCenter {

  //根据参数发送所有post请求
  public static void postRequest(String url, RequestParams params, DisposeDataListener listener, Class<?> cls) {
    CommonOkHttpClient.get(CommonRequest.createGetRequest(url, params), new DisposeDataHandle(listener, cls));
  }

  /**
   * 用户登录请求
   * @param userName
   * @param passwd
   * @param listener
   */
  public static void login(String userName, String passwd, DisposeDataListener listener) {
    RequestParams params = new RequestParams();
    params.put("mb", userName);
    params.put("pwd", passwd);
    RequestCenter.postRequest(HttpConstants.LOGIN, params, listener, User.class);
  }

  /**
   * 应用版本号请求
   * @param listener
   */
  public static void checkVersion(DisposeDataListener listener) {
    RequestCenter.postRequest(HttpConstants.CHECK_UPDATE, null, listener, UpdateModel.class);
  }

  public static void requestRecommendData(DisposeDataListener listener) {
    RequestCenter.postRequest(HttpConstants.HOME_RECOMMAND, null, listener, BaseRecommendModel.class);
  }

  public static void downloadFile(String url, String path, DisposeDownloadListener listener) {
    CommonOkHttpClient.downloadFile(CommonRequest.createGetRequest(url, null), new DisposeDataHandle(listener, path));
  }
}
