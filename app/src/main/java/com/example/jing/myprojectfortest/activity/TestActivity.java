package com.example.jing.myprojectfortest.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import com.example.jing.myprojectfortest.R;
import com.example.mysdkforandroid.okhttp.widget.CustomVideoView;

/**
 * Created by jing on 2018/10/24.
 */

public class TestActivity extends AppCompatActivity {
  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_test_layout);

    LinearLayout linearLayout = findViewById(R.id.root_view);
    CustomVideoView customVideoView = new CustomVideoView(this, linearLayout);
    customVideoView.setDataSource("http://v9-hs.ixigua.com/82a2ba1a8fdc8981b8b5063789abc584/5bcfe214/video/m/2206d7f9dbbf3514d7f856b01910f048cd81142b98000005811497c0ab/");
    linearLayout.addView(customVideoView);
  }
}
