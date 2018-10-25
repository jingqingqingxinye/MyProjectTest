package com.example.mysdkforandroid.okhttp.okhttp;

import com.example.mysdkforandroid.okhttp.CommonOkHttpClient;
import com.example.mysdkforandroid.okhttp.listener.DisposeDataHandle;
import com.example.mysdkforandroid.okhttp.listener.DisposeDataListener;
import com.example.mysdkforandroid.okhttp.module.AdInstance;
import com.example.mysdkforandroid.okhttp.okhttp.request.CommonRequest;

/**
 * Created by jing on 2018/10/25.
 */

public class RequestCenter {
  /**
   * 发送广告请求
   * @param url
   * @param listener
   */
  public static void sendImageAdRequest(String url, DisposeDataListener listener) {
    CommonOkHttpClient.post(CommonRequest.createPostRequest(url, null),
        new DisposeDataHandle(listener, AdInstance.class));
  }
}
