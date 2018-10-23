package com.example.jing.myprojectfortest.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.jing.myprojectfortest.R;
import com.example.jing.myprojectfortest.activity.base.BaseActivity;
import com.example.jing.myprojectfortest.view.fragment.HomeFragment;
import com.example.jing.myprojectfortest.view.fragment.MessageFragment;
import com.example.jing.myprojectfortest.view.fragment.MineFragment;

/**
 * Created by jing on 2018/10/17.
 */

public class HomeActivity extends BaseActivity implements View.OnClickListener{

  private FragmentManager fm;
  private HomeFragment mHomeFragment;
  private MessageFragment mMessageFragment;
  private MineFragment mMineFragment;
  private Fragment mCurrentFragment;

  private LinearLayout mHomeLayout;
  private LinearLayout mMessageLayout;
  private LinearLayout mMineLayout;

  private TextView mHomeView;
  private TextView mMessageView;
  private TextView mMineView;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home_layout);
    initView();

    mHomeFragment = new HomeFragment();
    fm = getFragmentManager();
    //打开事务
    FragmentTransaction fragmentTransaction = fm.beginTransaction();
    fragmentTransaction.replace(R.id.content_layout, mHomeFragment);
    fragmentTransaction.commit();
  }

  @Override protected void onDestroy() {
    super.onDestroy();
  }

  private void initView() {
    mHomeLayout = findViewById(R.id.home_layout_view);
    mHomeLayout.setOnClickListener(this);
    mMessageLayout = findViewById(R.id.message_layout_view);
    mMessageLayout.setOnClickListener(this);
    mMineLayout = findViewById(R.id.mine_layout_view);
    mMineLayout.setOnClickListener(this);

    mHomeView = findViewById(R.id.home_image_view);
    mMessageView = findViewById(R.id.message_image_view);
    mMineView = findViewById(R.id.mine_image_view);
    mHomeView.setBackgroundResource(R.drawable.comui_tab_home_selected);
  }

  @Override public void onClick(View v) {
    FragmentTransaction fragmentTransaction = fm.beginTransaction();
    switch (v.getId()) {
      case R.id.home_layout_view:
        mHomeView.setBackgroundResource(R.drawable.comui_tab_home_selected);
        mMessageView.setBackgroundResource(R.drawable.comui_tab_message);
        mMineView.setBackgroundResource(R.drawable.comui_tab_person);
        hideFragment(mMessageFragment, fragmentTransaction);
        hideFragment(mMineFragment, fragmentTransaction);
        showFragment(mHomeFragment, fragmentTransaction);
        break;
      case R.id.message_layout_view:
        mMessageView.setBackgroundResource(R.drawable.comui_tab_message_selected);
        mHomeView.setBackgroundResource(R.drawable.comui_tab_home);
        mMineView.setBackgroundResource(R.drawable.comui_tab_person);

        hideFragment(mHomeFragment, fragmentTransaction);
        hideFragment(mMineFragment, fragmentTransaction);
        showFragment(mMessageFragment, fragmentTransaction);
        break;
      case R.id.mine_layout_view:
        mMineView.setBackgroundResource(R.drawable.comui_tab_person_selected);
        mHomeView.setBackgroundResource(R.drawable.comui_tab_home);
        mMessageView.setBackgroundResource(R.drawable.comui_tab_message);
        hideFragment(mMessageFragment, fragmentTransaction);
        hideFragment(mHomeFragment, fragmentTransaction);
        showFragment(mMineFragment, fragmentTransaction);
        break;
      default:
        break;
    }
    fragmentTransaction.commit();
  }

  private void showFragment(Fragment fragment, FragmentTransaction fragmentTransaction) {
    if (fragment == null) {
      fragment = new HomeFragment();
      fragmentTransaction.add(R.id.content_layout, fragment);
    } else {
      mCurrentFragment = fragment;
      fragmentTransaction.show(fragment);
    }
  }

  private void hideFragment(Fragment fragment, FragmentTransaction fragmentTransaction) {
    if (fragment != null) {
      fragmentTransaction.hide(fragment);
    }
  }
}
