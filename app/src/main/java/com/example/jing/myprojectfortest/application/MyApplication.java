package com.example.jing.myprojectfortest.application;

import android.app.Application;

/**
 * @Function 1.程序的入口 2.做一些初始化工作 3.为其他模块提供上下文对象
 * Created by jing on 2018/10/16.
 */

public class MyApplication extends Application {

  private static MyApplication myApplication = null;

  public static MyApplication getInstance() {
    if (myApplication == null) {
      synchronized (MyApplication.class) {
        if (myApplication == null) {
          myApplication = new MyApplication();
        }
      }
    }
    return myApplication;
  }
  @Override public void onCreate() {
    super.onCreate();
    myApplication = this;
  }
}
