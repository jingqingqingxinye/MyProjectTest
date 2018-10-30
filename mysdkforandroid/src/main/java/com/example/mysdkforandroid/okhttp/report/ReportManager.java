package com.example.mysdkforandroid.okhttp.report;

import com.example.mysdkforandroid.okhttp.CommonOkHttpClient;
import com.example.mysdkforandroid.okhttp.adutil.Utils;
import com.example.mysdkforandroid.okhttp.listener.DisposeDataHandle;
import com.example.mysdkforandroid.okhttp.listener.DisposeDataListener;
import com.example.mysdkforandroid.okhttp.module.monitor.Monitor;
import com.example.mysdkforandroid.okhttp.okhttp.request.CommonRequest;
import com.example.mysdkforandroid.okhttp.okhttp.request.RequestParams;
import com.example.mysdkforandroid.okhttp.okhttp.response.HttpConstant;
import com.example.mysdkforandroid.okhttp.okhttp.response.HttpConstant.Params;
import java.util.ArrayList;

/**
 * Created by jing on 2018/10/25.
 * @function: 负责所有监测请求的发送
 */

public class ReportManager {

  /**
   * 默认的时间回调处理
   */
  private static DisposeDataHandle handle = new DisposeDataHandle(new DisposeDataListener() {
    @Override public void onSuccess(Object responseObj) {

    }

    @Override public void onFailure(Object reasonObj) {

    }
  });
  /**
   * send the video pause monitor
   * @param monitors
   * @param playTime
   */
  public static void pauseVideoReport(ArrayList<Monitor> monitors, long playTime) {
    if (monitors != null && monitors.size() > 0) {
      for (Monitor monitor : monitors) {
        RequestParams params = new RequestParams();
        if (Utils.containString(monitor.url, HttpConstant.ATM_PRE)) {
          params.put("ve", String.valueOf(playTime));
        }
        CommonOkHttpClient.get(CommonRequest.createMonitorRequest(monitor.url, params), handle);
      }
    }
  }

  /**
   * send the sus monitor
   */
  public static void susReport(ArrayList<Monitor> monitors, boolean isAuto) {
    if (monitors != null && monitors.size() > 0) {
      for (Monitor monitor : monitors) {
        RequestParams params = new RequestParams();
        if (Utils.containString(monitor.url, HttpConstant.ATM_PRE)) {
          params.put("ve", "0");
          if (isAuto) {
            params.put("auto", "1");
          }
        }
        CommonOkHttpClient.get(
            CommonRequest.createMonitorRequest(monitor.url, params), handle);
      }
    }
  }

  /**
   * send the sueReport
   * @param monitors
   * @param isFull
   * @param playTime
   */
  public static void sueReport(ArrayList<Monitor> monitors, boolean isFull, long playTime) {
    if (monitors != null && monitors.size() > 0) {
      for (Monitor monitor : monitors) {
        RequestParams params = new RequestParams();
        if (Utils.containString(monitor.url, HttpConstant.ATM_PRE)) {
          if (isFull) {
            params.put("fu", "1");
          }
          params.put("ve", String.valueOf(playTime));
        }
        CommonOkHttpClient.get(CommonRequest.createMonitorRequest(monitor.url, params), handle);
      }
    }
  }

  /**
   * send the su report
   */
  public static void suReport(ArrayList<Monitor> monitors, long playTime) {
    if (monitors != null && monitors.size() > 0) {
      for (Monitor monitor : monitors) {
        RequestParams params = new RequestParams();
        if (monitor.time == playTime) {
          if (Utils.containString(monitor.url, HttpConstant.ATM_PRE)) {
            params.put("ve", String.valueOf(playTime));
          }
          CommonOkHttpClient.get(
              CommonRequest.createMonitorRequest(monitor.url, params), handle);
        }
      }
    }
  }

  /**
   * send the clicl full btn monitor
   *
   * @param monitors urls
   * @param playTime player time
   */
  public static void fullScreenReport(ArrayList<Monitor> monitors, long playTime) {
    if (monitors != null && monitors.size() > 0) {
      for (Monitor monitor : monitors) {
        RequestParams params = new RequestParams();
        if (Utils.containString(monitor.url, HttpConstant.ATM_PRE)) {
          params.put("ve", String.valueOf(playTime));
        }
        CommonOkHttpClient.get(
            CommonRequest.createMonitorRequest(monitor.url, params), handle);
      }
    }
  }

  /**
   * send the click back full btn monitor
   *
   * @param monitors urls
   * @param playTime player time
   */
  public static void exitfullScreenReport(ArrayList<Monitor> monitors, long playTime) {
    if (monitors != null && monitors.size() > 0) {
      for (Monitor monitor : monitors) {
        RequestParams params = new RequestParams();
        if (Utils.containString(monitor.url, HttpConstant.ATM_PRE)) {
          params.put("ve", String.valueOf(playTime));
        }
        CommonOkHttpClient.get(
            CommonRequest.createMonitorRequest(monitor.url, params), handle);
      }
    }
  }

  public static void sendAdMonitor(boolean isPad, String sid, String ie,
      String appVersion, Params step, String result) {
    RequestParams params = new RequestParams();
    params.put(Params.lvs.getKey(), Params.lvs.getValue());
    params.put(Params.st.getKey(), Params.st.getValue());
    params.put(Params.os.getKey(), Params.os.getValue());
    params.put(Params.p.getKey(), Params.p.getValue());
    params.put(Params.appid.getKey(), Params.appid.getValue());
    if (isPad) {
      params.put(Params.bt_pad.getKey(), Params.bt_pad.getValue());
    } else {
      params.put(Params.bt_phone.getKey(), Params.bt_phone.getValue());
    }
    params.put(step.getKey(),
        step.getValue());
    params.put(HttpConstant.STEP_CD, result);
    //params.put(HttpConstant.SID, sid);
    params.put(HttpConstant.IE, ie);
    params.put(HttpConstant.AVS, appVersion);

    CommonOkHttpClient.get(CommonRequest.createGetRequest(HttpConstant.ATM_MONITOR, params), handle);
  }
}
