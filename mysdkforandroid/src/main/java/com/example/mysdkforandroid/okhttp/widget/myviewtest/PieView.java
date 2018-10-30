package com.example.mysdkforandroid.okhttp.widget.myviewtest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import java.util.ArrayList;

/**
 * Created by jing on 2018/10/26.
 */

public class PieView extends View {

  private int[] mColors = {0xFFCCFF00, 0xFF6495ED, 0xFFE32636, 0xFF800000, 0xFF808000, 0xFFFF8C69, 0xFF808080,
      0xFFE6B800, 0xFF7CFC00};
  private float mStartAngle = 0;
  private ArrayList<PieData> mData;
  private int mWidth, mHeight;
  private Paint mPaint = new Paint();

  public PieView(Context context) {
    this(context, null);
  }

  public PieView(Context context,
      @Nullable AttributeSet attrs) {
    super(context, attrs);
    mPaint.setStyle(Paint.Style.FILL);
    mPaint.setAntiAlias(true);
  }

  @Override protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);
    mWidth = w;
    mHeight = h;
  }

  @Override protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    if (mData == null) {
      return;
    }
    float currentStartAngle = mStartAngle;
    canvas.translate(mWidth / 2, mHeight / 2);//将画笔移动到中心位置
    float r = (float) (Math.min(mWidth, mHeight) / 2 * 0.8);//饼状图半径
    RectF rectF = new RectF(-r, -r, r, r);
    for (int i = 0; i < mData.size(); i++) {
      PieData pie = mData.get(i);
      mPaint.setColor(pie.getColor());
      canvas.drawArc(rectF, currentStartAngle, pie.getAngle(), true, mPaint);
      currentStartAngle += pie.getAngle();
    }
  }

  public void setStartAngle(int startAngle) {
    mStartAngle = startAngle;
    invalidate();
  }

  // 设置数据
  public void setData(ArrayList<PieData> mData) {
    this.mData = mData;
    initData(mData);
    invalidate();   // 刷新
  }

  // 初始化数据
  private void initData(ArrayList<PieData> mData) {
    if (null == mData || mData.size() == 0)   // 数据有问题 直接返回
      return;

    float sumValue = 0;
    for (int i = 0; i < mData.size(); i++) {
      PieData pie = mData.get(i);

      sumValue += pie.getValue();       //计算数值和

      int j = i % mColors.length;       //设置颜色
      pie.setColor(mColors[j]);
    }

    float sumAngle = 0;
    for (int i = 0; i < mData.size(); i++) {
      PieData pie = mData.get(i);

      float percentage = pie.getValue() / sumValue;   // 百分比
      float angle = percentage * 360;                 // 对应的角度

      pie.setPercentage(percentage);                  // 记录百分比
      pie.setAngle(angle);                            // 记录角度大小
      sumAngle += angle;

      Log.i("angle", "" + pie.getAngle());
    }
  }



  class PieData {
    // 用户关心数据
    private String name;        // 名字
    private float value;        // 数值
    private float percentage;   // 百分比

    // 非用户关心数据
    private int color = 0;      // 颜色
    private float angle = 0;    // 角度

    public PieData(@NonNull String name, @NonNull float value) {
      this.name = name;
      this.value = value;
    }

    public int getColor() {
      return color;
    }

    public float getAngle() {
      return angle;
    }

    public float getValue() {
      return value;
    }

    public void setPercentage(float percentage) {
      this.percentage = percentage;
    }

    public void setColor(int color) {
      this.color = color;
    }

    public void setAngle(float angle) {
      this.angle = angle;
    }
  }


}


