package com.example.jing.myprojectfortest;

import android.Manifest;
import android.os.Environment;

/**
 * Created by jing on 2018/10/19.
 */

public class Constant {

  /**
   * 常量相关
   */
  public static final int WRITE_READ_EXTERNAL_CODE = 0x01;
  public static final String[] WRITE_READ_EXTERNAL_PERMISSION =
      new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
  public static final int HARDWEAR_CAMERA_CODE = 0x02;
  public static final String[] HARDWEAR_CAMERA_PERMISSION =
      new String[]{Manifest.permission.CAMERA};
  //整个应用文件的下载保存路径
  public static String APP_PHOTO_DIR = Environment.getExternalStorageDirectory().getAbsolutePath().concat("/imooc_business/photo");
}
