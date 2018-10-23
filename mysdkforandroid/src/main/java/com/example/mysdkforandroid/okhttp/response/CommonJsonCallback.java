package com.example.mysdkforandroid.okhttp.response;

import android.os.Handler;
import android.os.Looper;
import com.example.mysdkforandroid.okhttp.adutil.ResponseEntityToModule;
import com.example.mysdkforandroid.okhttp.exception.OkHttpException;
import com.example.mysdkforandroid.okhttp.listener.DisposeDataHandle;
import com.example.mysdkforandroid.okhttp.listener.DisposeDataListener;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.json.JSONObject;

/**
 * Created by jing on 2018/10/17.
 * @function
 */

public class CommonJsonCallback implements Callback{

  protected final String RESULT_CODE = "ecode";
  protected final int RESULT_CODE_VALUE = 0;
  protected final String EMPTY_MSG = "";
  protected final String COOKIE_STORE ="Set-Cookie";


  protected final int NETWORK_ERROR = -1;// the network relative error
  protected final int JSON_ERROR = -2;// the JSON relative error
  protected final int OTHER_ERROR = -3;//the unkonw error
  /**
   * 将其它线程的数据转发到UI线程
   */
  private Handler mDeliveryHandler;
  private DisposeDataListener mListener;
  private Class<?> mClass;

  public CommonJsonCallback(DisposeDataHandle handle) {
    this.mListener = handle.mListener;
    this.mClass = handle.mClass;
    this.mDeliveryHandler = new Handler(Looper.getMainLooper());
  }

  @Override public void onFailure(Call call, IOException e) {

  }

  @Override public void onResponse(Call call, Response response) throws IOException {
    final String result = response.body().string();
    mDeliveryHandler.post(new Runnable() {
      @Override public void run() {
        handleResponse(result);
      }
    });

  }

  private void handleResponse(String resultObj) {
    if (resultObj == null) {
      mListener.onFailure(new OkHttpException(NETWORK_ERROR, EMPTY_MSG));
      return;
    }

    try {
      /**
       * 协议确定后看这里如何修改
       */
      JSONObject result = new JSONObject(resultObj.toString());
      if (mClass == null) {
        mListener.onSuccess(result);
      } else {
        Object obj = ResponseEntityToModule.parseJsonObjectToModule(result, mClass);
        if (obj != null) {
          mListener.onSuccess(obj);
        } else {
          mListener.onFailure(new OkHttpException(JSON_ERROR, EMPTY_MSG));
        }
      }
    } catch (Exception e) {
      mListener.onFailure(new OkHttpException(OTHER_ERROR, e.getMessage()));
      e.printStackTrace();
    }
  }
}
