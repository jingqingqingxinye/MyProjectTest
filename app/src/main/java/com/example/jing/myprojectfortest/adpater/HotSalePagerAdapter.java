package com.example.jing.myprojectfortest.adpater;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.jing.myprojectfortest.R;
import com.example.jing.myprojectfortest.module.recommend.RecommendBodyValue;
import com.example.mysdkforandroid.okhttp.adutil.ImageLoaderUtil;
import java.util.ArrayList;

/**
 * Created by jing on 2018/10/22.
 */

public class HotSalePagerAdapter extends PagerAdapter {

  private Context mContext;
  private ArrayList<RecommendBodyValue> mData;
  private LayoutInflater mInflate;
  private ImageLoaderUtil mImageLoader;

  public HotSalePagerAdapter(Context context, ArrayList<RecommendBodyValue> list) {
    mContext = context;
    mData = list;
    mInflate = LayoutInflater.from(mContext);
    mImageLoader = ImageLoaderUtil.getInstance(mContext);
  }

  @Override public int getCount() {
    return Integer.MAX_VALUE;
  }

  @Override public boolean isViewFromObject(View view, Object object) {
    return view == object;
  }

  @Override public void destroyItem(ViewGroup container, int position, Object object) {
    container.removeView((View) object);
  }

  @Override public Object instantiateItem(ViewGroup container, int position) {
    RecommendBodyValue value = mData.get(position % mData.size());
    View rootView = mInflate.inflate(R.layout.item_hot_product_pager_layout, null);
    TextView titleView = rootView.findViewById(R.id.title_view);
    TextView infoView = rootView.findViewById(R.id.info_view);
    TextView gonggaoView = rootView.findViewById(R.id.gonggao_view);
    TextView saleView = rootView.findViewById(R.id.sale_num_view);
    ImageView[] imageViews = new ImageView[3];
    imageViews[0] = rootView.findViewById(R.id.image_one);
    imageViews[1] = rootView.findViewById(R.id.image_two);
    imageViews[2] = rootView.findViewById(R.id.image_three);
    rootView.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        //TODO
      }
    });
    titleView.setText(value.title);
    infoView.setText(value.price);
    gonggaoView.setText(value.info);
    saleView.setText(value.text);
    for (int i = 0; i < imageViews.length; i++) {
      mImageLoader.displayImage(imageViews[i], value.url.get(i));
    }
    container.addView(rootView, 0);
    return rootView;
  }

}
