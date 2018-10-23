package com.example.mysdkforandroid.okhttp;

import com.example.mysdkforandroid.okhttp.https.HttpsUtils;
import com.example.mysdkforandroid.okhttp.listener.DisposeDataHandle;
import com.example.mysdkforandroid.okhttp.listener.DisposeDataListener;
import com.example.mysdkforandroid.okhttp.response.CommonFileCallback;
import com.example.mysdkforandroid.okhttp.response.CommonJsonCallback;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by jing on 2018/10/17.
 * @function 请求的发送， 请求参数的配置， https的支持
 */

public class CommonOkHttpClient {

  private static final int TIME_OUT = 30;//请求超时
  private static OkHttpClient mOkhttpClient;

  //为我们的client配置参数
  static {
    //创建我们client对象的构建者
    OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
    //为构建者添加超时时间
    okHttpClientBuilder.connectTimeout(TIME_OUT, TimeUnit.SECONDS);
    okHttpClientBuilder.readTimeout(TIME_OUT, TimeUnit.SECONDS);
    okHttpClientBuilder.writeTimeout(TIME_OUT, TimeUnit.SECONDS);
    okHttpClientBuilder.followRedirects(true);//重定向

    //https支持
    okHttpClientBuilder.hostnameVerifier(new HostnameVerifier() {
      @Override public boolean verify(String hostname, SSLSession session) {
        return true;
      }
    });
    okHttpClientBuilder.sslSocketFactory(HttpsUtils.initSSLSocketFactory(), HttpsUtils.initTrustManager());
    //生成我们的client对象
    mOkhttpClient = okHttpClientBuilder.build();
  }

  /**
   * 发送具体的请求
   * @param request
   * @param callback
   * @return
   */
  public static Call sendRequest(Request request, Callback callback) {
    Call call = mOkhttpClient.newCall(request);
    call.enqueue(callback);
    return call;
  }

  public static Call get(Request request, DisposeDataHandle handle) {
    Call call = mOkhttpClient.newCall(request);
    call.enqueue(new CommonJsonCallback(handle));
    return call;
  }

  public static Call downloadFile(Request request, DisposeDataHandle handle) {
    Call call = mOkhttpClient.newCall(request);
    call.enqueue(new CommonFileCallback(handle));
    return call;
  }
}
