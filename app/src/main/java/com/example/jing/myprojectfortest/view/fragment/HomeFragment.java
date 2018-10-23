package com.example.jing.myprojectfortest.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.jing.myprojectfortest.Constant;
import com.example.jing.myprojectfortest.R;
import com.example.jing.myprojectfortest.activity.PhotoViewActivity;
import com.example.jing.myprojectfortest.adpater.CourseAdapter;
import com.example.jing.myprojectfortest.contract.HomeContract;
import com.example.jing.myprojectfortest.module.recommend.BaseRecommendModel;
import com.example.jing.myprojectfortest.presenter.HomePresenter;
import com.example.jing.myprojectfortest.view.home.HomeHeadLayout;
import com.example.jing.myprojectfortest.zxing.app.CaptureActivity;
import com.example.mysdkforandroid.okhttp.activity.AdBrowserActivity;
import com.trello.rxlifecycle2.LifecycleTransformer;

/**
 * Created by jing on 2018/10/16.
 */

public class HomeFragment extends BaseFragment implements View.OnClickListener,
    AdapterView.OnItemClickListener, HomeContract.View {

  private static final int REQEST_QRCOOD = 0x01;

  /**
   * UI
   */
  private View mContentView;
  private ListView mListView;
  private TextView mQRCodeView;
  private TextView mCategoryView;
  private TextView mSearchView;
  private ImageView mLoadingView;

  /**
   * data
   */
  private CourseAdapter mAdapter;

  private HomeContract.Presenter mPresenter;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setPresenter(new HomePresenter(this));
    mPresenter.start();
  }


  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      Bundle savedInstanceState) {
    mContext = getActivity();
    mContentView = inflater.inflate(R.layout.fragment_home_layout, container, false);
    initView();
    return mContentView;
  }

  private void initView() {
    mQRCodeView = mContentView.findViewById(R.id.qrcode_view);
    mQRCodeView.setOnClickListener(this);
    mCategoryView = mContentView.findViewById(R.id.category_view);
    mCategoryView.setOnClickListener(this);
    mSearchView = mContentView.findViewById(R.id.search_view);
    mSearchView.setOnClickListener(this);
    mListView = mContentView.findViewById(R.id.list_view);
    mListView.setOnItemClickListener(this);
    mLoadingView = mContentView.findViewById(R.id.loading_view);
    //启动动画
    AnimationDrawable anim = (AnimationDrawable) mLoadingView.getDrawable();
    anim.start();

    mPresenter.requestRecommendData();

  }

  @Override public void doOpenCamera() {
    Intent intent = new Intent(mContext, CaptureActivity.class);
    startActivityForResult(intent, REQEST_QRCOOD);
  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    switch (requestCode) {
      case REQEST_QRCOOD:
        if (requestCode == Activity.RESULT_OK) {
         String code = data.getStringExtra("SCAN_RESULT");
         if (code.contains("https") || code.contains("http")) {
           Intent intent = new Intent(mContext, AdBrowserActivity.class);
           intent.putExtra(AdBrowserActivity.KEY_URL, code);
           startActivity(intent);
         } else {
           Toast.makeText(mContext, code, Toast.LENGTH_SHORT).show();
         }
        }
        break;
      default:
        break;
    }
  }

  @Override public void onClick(View v) {
    switch (v.getId()) {
      case R.id.qrcode_view:
        if (hasPermission(Constant.HARDWEAR_CAMERA_PERMISSION)) {
          doOpenCamera();
        } else {
          requestPermission(Constant.HARDWEAR_CAMERA_CODE, Constant.HARDWEAR_CAMERA_PERMISSION);
        }
      default:
        break;
    }

  }

  @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

  }

  @Override public void setPresenter(HomeContract.Presenter presenter) {
    mPresenter = presenter;
  }

  @Override public void showToast(String message) {

  }

  @Override public <T> LifecycleTransformer<T> getDestroyEvent() {
    return null;
  }

  @Override public void showSuccessView(BaseRecommendModel baseRecommendModel) {
    if (baseRecommendModel != null && baseRecommendModel.data != null
        && baseRecommendModel.data.list != null && baseRecommendModel.data.list.size() > 0) {
      mLoadingView.setVisibility(View.GONE);
      mListView.setVisibility(View.VISIBLE);
      //mListView.addHeaderView(new HomeHeadLayout(mContext, baseRecommendModel.data.head));
      mAdapter = new CourseAdapter(mContext, baseRecommendModel.data.list);
      mListView.setAdapter(mAdapter);
      mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
        @Override public void onScrollStateChanged(AbsListView view, int scrollState) {

        }

        @Override public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
            int totalItemCount) {
          //TODO
          //mAdapter.updateAdInScrollView();

        }
      });
    } else {
      showToast("show error view");
    }
  }

}
