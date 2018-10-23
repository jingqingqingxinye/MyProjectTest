package com.example.jing.myprojectfortest.activity.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by jing on 2018/10/16.
 * @function 为所有activity提供公共行为
 */

public abstract class BaseActivity extends AppCompatActivity {

  /**
   * 输出日志，所需log
   */
  public String TAG;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    TAG = getComponentName().getShortClassName();
  }

  @Override protected void onStart() {
    super.onStart();
  }

  @Override protected void onDestroy() {
    super.onDestroy();
  }

  @Override protected void onPause() {
    super.onPause();
  }

  @Override protected void onResume() {
    super.onResume();
  }
}
