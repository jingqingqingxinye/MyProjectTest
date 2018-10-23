package com.example.mysdkforandroid.okhttp.adutil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * Created by jing on 2018/10/18.
 * @function:初始化UniverImageLoader,并用来加载网络图片
 */

public class ImageLoaderUtil {

  private static final int THREAD_COUNT = 2;//表明UIL最多可以有多少线程
  private static final int PRIORITY = 2;//标明图片加载的优先级
  private static final int MEMORY_CACHE_SIZE = 2 * 1024 * 1024;
  private static final int DISK_CACHE_SIZE = 50 * 1024 * 1024;
  private static final int CONNECTION_TIME_OUT = 5 * 1000;
  private static final int READ_TIME_OUT = 30 * 1000;

  private static ImageLoaderUtil mInstance = null;
  private static ImageLoader mLoader = null;

  public static ImageLoaderUtil getInstance(Context context) {
    if (mInstance == null) {
      synchronized (ImageLoaderUtil.class) {
        if (mInstance == null) {
          mInstance = new ImageLoaderUtil(context);
        }
      }
    }
    return mInstance;
  }

  /**
   * 私有构造方法完成初始化工作
   * @param context
   */
  private ImageLoaderUtil(Context context) {
    ImageLoaderConfiguration config = new ImageLoaderConfiguration
        .Builder(context)
        .threadPoolSize(THREAD_COUNT)
        .threadPriority(Thread.NORM_PRIORITY - PRIORITY)
        .denyCacheImageMultipleSizesInMemory()
        .memoryCache(new WeakMemoryCache())//使用弱引用
        .diskCacheSize(DISK_CACHE_SIZE)
        .diskCacheFileNameGenerator(new Md5FileNameGenerator())
        .tasksProcessingOrder(QueueProcessingType.FIFO)
        .defaultDisplayImageOptions(getDefaultOptions())
        .imageDownloader(new BaseImageDownloader(context, CONNECTION_TIME_OUT, READ_TIME_OUT))
        .writeDebugLogs()
        .build();
  }

  private DisplayImageOptions getDefaultOptions() {
    DisplayImageOptions options = new DisplayImageOptions.Builder()
        .cacheInMemory(true)
        .cacheOnDisk(true)
        .considerExifParams(true)
        .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
        .bitmapConfig(Bitmap.Config.RGB_565)
        .decodingOptions(new BitmapFactory.Options())
        .resetViewBeforeLoading(true)
        .build();
    return options;
  }

  //load the image
  public void displayImage(ImageView imageView, String path,
      ImageLoadingListener listener, DisplayImageOptions options) {
    if (mLoader != null) {
      mLoader.displayImage(path, imageView, listener);
    }
  }

  //load the image
  public void displayImage(ImageView imageView, String path, ImageLoadingListener listener) {
    if (mLoader != null) {
      mLoader.displayImage(path, imageView, listener);
    }
  }

  //load the image
  public void displayImage(ImageView imageView, String path) {
    displayImage(imageView, path, null);
  }
}
