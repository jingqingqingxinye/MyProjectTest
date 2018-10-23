package com.example.mysdkforandroid.okhttp.adutil;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import com.example.mysdkforandroid.okhttp.constant.SDKConstant;
import java.io.ByteArrayInputStream;

/**
 * Created by jing on 2018/10/22.
 */

public class Utils {

  public static int dip2px(Context context, float dpValue) {
    final float scale = context.getResources().getDisplayMetrics().density;
    return (int) (dpValue / scale);
  }

  public static DisplayMetrics getDisplayMetrics(Context context) {
    DisplayMetrics displayMetrics = new DisplayMetrics();
    WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    if (windowManager == null) {
      return displayMetrics;
    }
    windowManager.getDefaultDisplay().getMetrics(displayMetrics);
    return displayMetrics;
  }

  public static BitmapDrawable decodeImage(String base64drawable) {
    byte[] rawImageData = Base64.decode(base64drawable, 0);
    return new BitmapDrawable(null, new ByteArrayInputStream(rawImageData));
  }

  public static int getVisiblePersent(View pView) {
    if (pView != null && pView.isShown()) {
      DisplayMetrics displayMetrics = pView.getContext().getResources().getDisplayMetrics();
      int dispalyWidth = displayMetrics.widthPixels;
      Rect rect = new Rect();
      pView.getGlobalVisibleRect(rect);
      if ((rect.top > 0) && (rect.left < dispalyWidth)) {
        double areaVisable = rect.width() * rect.height();
        double areaTotal = pView.getWidth() * pView.getHeight();
        return (int) ((areaVisable / areaTotal) / 100);
      } else {
        return -1;
      }
    }
    return -1;
  }

  public static boolean canAutoPlay(Context context, SDKConstant.AutoPlaySetting seting) {
    boolean result = true;
    switch (seting) {
      case AUTO_PLAY_3G_4G_WIFI:
        result = true;
        break;
      case AUTO_PLAY_ONLY_WIFI:
        if (isWifiConnected(context)) {
          result = true;
        } else {
          result = false;
        }
        break;
      case AUTO_PLAY_NEVER:
        result = false;
        break;
      default:
        break;
    }
    return result;
  }

  /**
   * is wifi connected
   * @param context
   * @return
   */
  private static boolean isWifiConnected(Context context) {
    if (context.checkCallingOrSelfPermission(Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED) {
      return false;
    }
    ConnectivityManager connectivityManager =
        (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo info = connectivityManager.getActiveNetworkInfo();
    if (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_WIFI) {
      return true;
    }
    return false;
  }
}
