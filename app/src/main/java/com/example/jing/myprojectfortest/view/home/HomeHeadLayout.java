package com.example.jing.myprojectfortest.view.home;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;

/**
 * Created by jing on 2018/10/22.
 */

public class HomeHeadLayout extends RelativeLayout{
  /**
   * UI
   */
  private RelativeLayout mRootView;
  private AutoScrollViewPager mViewPager;
  //private CirclePageIndicator mPagerIndictor;
  private TextView mHotView;

  public HomeHeadLayout(Context context) {
    super(context);
  }

  public HomeHeadLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public HomeHeadLayout(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }
}
