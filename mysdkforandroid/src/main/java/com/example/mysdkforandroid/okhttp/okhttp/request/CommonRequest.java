package com.example.mysdkforandroid.okhttp.okhttp.request;

import java.util.Map;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.Request;

/**
 * Created by jing on 2018/10/17.
 * @function 接收请求参数，为我们生成Request请求参数
 */

public class CommonRequest {

  /**
   * post请求
   * @param url
   * @param params
   * @return
   */
  public static Request createPostRequest(String url, RequestParams params) {
    FormBody.Builder builder = new FormBody.Builder();
    if (params != null) {
      for (Map.Entry<String, String> entry : params.urlParams.entrySet()) {
        //将请求参数逐一遍历添加到我们的请求构建类中
        builder.add(entry.getKey(), entry.getValue());
      }
    }
    //通过构建类的builder方法获取真正的请求体对象
    FormBody formBody = builder.build();
    return new Request.Builder().url(url).post(formBody).build();
  }

  /**
   * get请求
   * @param url
   * @param params
   * @return
   */
  public static Request createGetRequest(String url, RequestParams params) {

    return createGetRequest(url, params, null);

  }

  private static Request createGetRequest(String url, RequestParams params, RequestParams headers) {
    StringBuilder urlBuilder = new StringBuilder(url).append("?");
    if (params != null) {
      for (Map.Entry<String, String> entry : params.urlParams.entrySet()) {
        urlBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
      }
    }
    //添加请求头
    Headers.Builder mHeaderBuild = new Headers.Builder();
    if (headers != null) {
      for (Map.Entry<String, String> entry : headers.urlParams.entrySet()) {
        mHeaderBuild.add(entry.getKey(), entry.getValue());
      }
    }
    Headers mHeader = mHeaderBuild.build();
    return new Request.Builder().
        url(urlBuilder.substring(0, urlBuilder.length() - 1))
        .get()
        .headers(mHeader)
        .build();
  }

  /**
   *
   * @param url
   * @param params
   * @return
   */
  public static Request createMonitorRequest(String url, RequestParams params) {
    StringBuilder urlBuilder = new StringBuilder(url).append("&");
    if (params != null && params.hasParams()) {
      for (Map.Entry<String, String> entry : params.urlParams.entrySet()) {
        urlBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
      }
    }
    return new Request.Builder().url(urlBuilder.substring(0, urlBuilder.length() - 1)).get().build();
  }
}
