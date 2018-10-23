package com.example.jing.myprojectfortest.view.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import com.example.jing.myprojectfortest.Constant;

/**
 * Created by jing on 2018/10/16.
 */

public class BaseFragment extends Fragment {

  protected Activity mContext;

  /**
   * 申请指定权限
   */
  public void requestPermission(int code, String... permissions) {
    if (Build.VERSION.SDK_INT >= 23) {
      requestPermissions(permissions, code);
    }
  }

  /**
   * 判断是否有指定的权限
   */
  public boolean hasPermission(String... permissions) {
    for (String permission : permissions) {
      if (ContextCompat.checkSelfPermission(getActivity(), permission) != PackageManager.PERMISSION_GRANTED) {
        return false;
      }
    }
    return true;
  }

  @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {
    switch (requestCode) {
      case Constant.HARDWEAR_CAMERA_CODE:
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
          doOpenCamera();
        }
        break;
      case Constant.WRITE_READ_EXTERNAL_CODE:
        if (grantResults.length > 0
            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
          doWriteSDCard();
        }
        break;
      default:
        break;
    }
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
  }

  public void doWriteSDCard() {
  }

  public void doOpenCamera() {
  }
}

