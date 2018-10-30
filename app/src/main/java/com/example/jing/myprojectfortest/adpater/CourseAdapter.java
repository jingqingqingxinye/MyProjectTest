package com.example.jing.myprojectfortest.adpater;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.jing.myprojectfortest.R;
import com.example.jing.myprojectfortest.Util;
import com.example.jing.myprojectfortest.activity.PhotoViewActivity;
import com.example.jing.myprojectfortest.module.recommend.RecommendBodyValue;
import com.example.jing.myprojectfortest.presenter.BasePresenter;
import com.example.mysdkforandroid.okhttp.activity.AdBrowserActivity;
import com.example.mysdkforandroid.okhttp.adutil.ImageLoaderUtil;
import com.example.mysdkforandroid.okhttp.adutil.Utils;
import com.example.mysdkforandroid.okhttp.core.AdContextInterface;
import com.example.mysdkforandroid.okhttp.core.video.VideoAdContext;
import com.google.gson.Gson;
import de.hdodenhof.circleimageview.CircleImageView;
import java.util.ArrayList;

/**
 * Created by jing on 2018/10/19.
 */

public class CourseAdapter extends BaseAdapter {

  private static final int CARD_COUNT = 4;
  private static final int VIDEO_TYPE = 0x00;
  private static final int CARD_TYPE_ONE = 0x01;
  private static final int CARD_TYPE_TWO =  0x02;
  private static final int CARD_TYPE_THREE = 0x03;

  private LayoutInflater mInflate;
  private Context mContext;
  private ArrayList<RecommendBodyValue> mData;
  private ViewHolder mViewHolder;
  private VideoAdContext mAdsdkContext;
  private ImageLoaderUtil mImageLoder;

  public CourseAdapter(Context context, ArrayList<RecommendBodyValue> data) {
    mContext = context;
    mData = data;
    mInflate = LayoutInflater.from(mContext);
    mImageLoder = ImageLoaderUtil.getInstance(mContext);
  }

  @Override public int getCount() {
    return mData.size();
  }

  @Override public Object getItem(int position) {
    return mData.get(position);
  }

  @Override public long getItemId(int position) {
    return position;
  }

  @Override public int getViewTypeCount() {
    return CARD_COUNT;
  }

  @Override public int getItemViewType(int position) {
    RecommendBodyValue value = (RecommendBodyValue) getItem(position);
    return value.type;
  }

  @Override public View getView(int position, View convertView, ViewGroup parent) {
    int type = getItemViewType(position);
    final RecommendBodyValue value = (RecommendBodyValue) getItem(position);
    //无tag时
    if (convertView == null) {
      switch (type) {
        case VIDEO_TYPE:
          //显示video卡片
          mViewHolder = new ViewHolder();
          convertView = mInflate.inflate(R.layout.item_video_layout, parent, false);
          mViewHolder.mVieoContentLayout = convertView.findViewById(R.id.video_ad_layout);
          mViewHolder.mLogoView = convertView.findViewById(R.id.item_logo_view);
          mViewHolder.mTitleView = convertView.findViewById(R.id.item_title_view);
          mViewHolder.mInfoView = convertView.findViewById(R.id.item_info_view);
          mViewHolder.mFooterView = convertView.findViewById(R.id.item_footer_view);
          mViewHolder.mShareView = convertView.findViewById(R.id.item_share_view);
          //为对应布局创建播放器
          mAdsdkContext = new VideoAdContext(mViewHolder.mVieoContentLayout, new Gson().toJson(value), null);
          mAdsdkContext.setAdResultListener(new AdContextInterface() {
            @Override public void onAdSucess() {

            }

            @Override public void onAdFailed() {

            }

            @Override public void onClickVideo(String url) {
              Intent intent = new Intent(mContext, AdBrowserActivity.class);
              intent.putExtra(AdBrowserActivity.KEY_URL, url);
              mContext.startActivity(intent);
            }
          });
          break;
        case CARD_TYPE_ONE:
          mViewHolder = new ViewHolder();
          convertView = mInflate.inflate(R.layout.item_product_card_one_layout, parent, false);
          mViewHolder.mLogoView = convertView.findViewById(R.id.item_logo_view);
          mViewHolder.mTitleView = convertView.findViewById(R.id.item_title_view);
          mViewHolder.mInfoView = convertView.findViewById(R.id.item_info_view);;
          mViewHolder.mFooterView = convertView.findViewById(R.id.item_footer_view);
          mViewHolder.mPriceView = convertView.findViewById(R.id.item_price_view);
          mViewHolder.mFromView = convertView.findViewById(R.id.item_from_view);
          mViewHolder.mZanView = convertView.findViewById(R.id.item_zan_view);
          mViewHolder.mProductLayout = (LinearLayout) convertView.findViewById(R.id.product_photo_layout);
          break;
        case CARD_TYPE_TWO:
          mViewHolder = new ViewHolder();
          convertView = mInflate.inflate(R.layout.item_product_card_two_layout, parent, false);
          mViewHolder.mLogoView = (CircleImageView) convertView.findViewById(R.id.item_logo_view);
          mViewHolder.mTitleView = (TextView) convertView.findViewById(R.id.item_title_view);
          mViewHolder.mInfoView = (TextView) convertView.findViewById(R.id.item_info_view);
          mViewHolder.mFooterView = (TextView) convertView.findViewById(R.id.item_footer_view);
          mViewHolder.mProductView = (ImageView) convertView.findViewById(R.id.product_photo_view);
          mViewHolder.mPriceView = (TextView) convertView.findViewById(R.id.item_price_view);
          mViewHolder.mFromView = (TextView) convertView.findViewById(R.id.item_from_view);
          mViewHolder.mZanView = (TextView) convertView.findViewById(R.id.item_zan_view);
          break;
        case CARD_TYPE_THREE:
          mViewHolder = new ViewHolder();
          convertView = mInflate.inflate(R.layout.item_product_card_three_layout, null, false);
          mViewHolder.mViewPager = (ViewPager) convertView.findViewById(R.id.pager);
          //add data
          ArrayList<RecommendBodyValue> recommandList = Util.handleData(value);
          mViewHolder.mViewPager.setPageMargin(Utils.dip2px(mContext, 12));
          mViewHolder.mViewPager.setAdapter(new HotSalePagerAdapter(mContext, recommandList));
          mViewHolder.mViewPager.setCurrentItem(recommandList.size() * 100);
          break;
        default:
          break;
      }
    } else {
      mViewHolder = (ViewHolder) convertView.getTag();
    }
    //填充item数据
    switch (type) {
      case CARD_TYPE_ONE:
        mImageLoder.displayImage(mViewHolder.mLogoView, value.logo);
        mViewHolder.mTitleView.setText(value.title);
        mViewHolder.mInfoView.setText(value.info.concat("天前"));
        mViewHolder.mFooterView.setText(value.text);
        mViewHolder.mPriceView.setText(value.price);
        mViewHolder.mFromView.setText(value.from);
        //mViewHolder.mZanView.setText("点赞".concat(value.zan));
        mViewHolder.mProductLayout.setOnClickListener(new View.OnClickListener() {
          @Override public void onClick(View v) {
            Intent intent = new Intent(mContext, PhotoViewActivity.class);
            intent.putStringArrayListExtra(PhotoViewActivity.PHOTO_LIST, value.url);
            mContext.startActivity(intent);
          }
        });
        mViewHolder.mProductLayout.removeAllViews();
        //动态添加多个imageview
        for (String url : value.url) {
          mViewHolder.mProductLayout.addView(createImageView(url));
        }
        break;
      case CARD_TYPE_TWO:
        mImageLoder.displayImage(mViewHolder.mLogoView, value.logo);
        mViewHolder.mTitleView.setText(value.title);
        mViewHolder.mInfoView.setText(value.info.concat("天前"));
        mViewHolder.mFooterView.setText(value.text);
        mViewHolder.mPriceView.setText(value.price);
        mViewHolder.mFromView.setText(value.from);
        mViewHolder.mZanView.setText("点赞".concat(value.zan));
        //为单个ImageView加载远程图片
        mImageLoder.displayImage(mViewHolder.mProductView, value.url.get(0));
        break;
      case VIDEO_TYPE:
        mImageLoder.displayImage(mViewHolder.mLogoView, value.logo);
        mViewHolder.mTitleView.setText(value.title);
        mViewHolder.mInfoView.setText(value.info.concat("天前"));
        mViewHolder.mFooterView.setText(value.text);
        mViewHolder.mShareView.setOnClickListener(new View.OnClickListener() {
          @Override public void onClick(View v) {
            //todo
          }
        });
        break;
      default:
        break;
    }
    return convertView;
  }

  //自动播放方法
  public void updateAdInScrollView() {
    if (mAdsdkContext != null) {
      mAdsdkContext.updateAdInScrollView();
    }
  }
  private View createImageView(String url) {
    ImageView photoView = new ImageView(mContext);
    LinearLayout.LayoutParams params = new LinearLayout.
        LayoutParams(Utils.dip2px(mContext, 100),
        LinearLayout.LayoutParams.MATCH_PARENT);
    params.leftMargin = Utils.dip2px(mContext, 5);
    photoView.setLayoutParams(params);
    mImageLoder.displayImage(photoView, url);
    return photoView;
  }

  public static class ViewHolder {
    //所有Card共有属性
    private CircleImageView mLogoView;
    private TextView mTitleView;
    private TextView mInfoView;
    private TextView mFooterView;
    //Video Card特有属性
    private RelativeLayout mVieoContentLayout;
    private ImageView mShareView;

    //Video Card外所有Card具有属性
    private TextView mPriceView;
    private TextView mFromView;
    private TextView mZanView;
    //Card One特有属性
    private LinearLayout mProductLayout;
    //Card Two特有属性
    private ImageView mProductView;
    //Card Three特有属性
    private ViewPager mViewPager;

  }
}
