package com.example.mysdkforandroid.okhttp.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.example.mysdkforandroid.R;
import com.example.mysdkforandroid.okhttp.adutil.ImageLoaderUtil;
import com.example.mysdkforandroid.okhttp.adutil.LogUtils;
import com.example.mysdkforandroid.okhttp.adutil.Utils;
import com.example.mysdkforandroid.okhttp.constant.SDKConstant;
import com.example.mysdkforandroid.okhttp.core.AdParameters;
import java.io.IOException;

/**
 * Created by jing on 2018/10/22.
 */

public class CustomVideoView extends RelativeLayout implements View.OnClickListener,
    MediaPlayer.OnPreparedListener, MediaPlayer.OnInfoListener, MediaPlayer.OnErrorListener,
    MediaPlayer.OnCompletionListener, MediaPlayer.OnBufferingUpdateListener, TextureView.SurfaceTextureListener {
  /**
   * Constant
   */
  private static final String TAG = "MraidVideoView";
  private static final int TIME_MSG = 0x01;
  private static final int TIME_INVAL = 1000;
  private static final int STATE_ERROR = -1;
  private static final int STATE_IDLE = 0;
  private static final int STATE_PLAYING = 1;
  private static final int STATE_PAUSING = 2;
  private static final int LOAD_TOTAL_COUNT = 3;
  /**
   * UI
   */
  private ViewGroup mParentContainer;
  private RelativeLayout mPlayView;
  private TextureView mVideoView;
  private Button mMinPlayBtn;
  private ImageView mFullBtn;
  private ImageView mLoadingBar;
  private ImageView mFrameView;
  private AudioManager audioManager;
  private Surface videoSurface;
  /**
   * Data
   */
  private String mUrl;
  private String mFrameURI;
  private boolean isMute;
  private int mScreenWidth, mDestationHeight;
  /**
   * 状态保护
   */
  private boolean canPlay;
  private boolean mIsRealPause;
  private boolean mIsComplete;
  private int mCurrentCount;
  private int playerState = STATE_IDLE;//默认处于空闲状态

  private MediaPlayer mediaPlayer;
  private ADVideoPlayerListener listener;//事件监听回调
  private ScreenEventReceiver mScreenReceiver;//监听屏幕是否锁屏

  private ADFrameImageLoadListener mFrameLoadListener;

  //这里选择handler，最好不用timer，因为timer很容易出现内存泄漏
  private Handler mHandler = new Handler(Looper.getMainLooper()) {
    @Override public void handleMessage(Message msg) {
      switch (msg.what) {
        case TIME_MSG:
          if (isPlaying()) {
            //还可以在这里更新progressbar
            listener.onBufferUpdate(getCurrentPosition());
            sendEmptyMessageDelayed(TIME_MSG, TIME_INVAL);
          }
          break;
        default:
          break;
      }
    }
  };

  public int getCurrentPosition() {
    if (this.mediaPlayer != null) {
      return mediaPlayer.getCurrentPosition();
    }
    return 0;
  }

  public boolean isPlaying() {
    if (mediaPlayer != null && mediaPlayer.isPlaying()) {
      return true;
    }
    return false;
  }

  public CustomVideoView(Context context, ViewGroup parentContainer) {
    super(context);
    mParentContainer = parentContainer;
    audioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
    initData();
    initView();
    registerBroadcastReceiver();
  }

  @Override public boolean onTouchEvent(MotionEvent event) {
    return true;
  }

  private void initData() {
    DisplayMetrics dm = new DisplayMetrics();
    WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
    wm.getDefaultDisplay().getMetrics(dm);
    mScreenWidth = dm.widthPixels;
    mDestationHeight = (int) (mScreenWidth * SDKConstant.VIDEO_HEIGHT_PERCENT);
  }

  private void initView() {
    LayoutInflater inflater = LayoutInflater.from(this.getContext());
    mPlayView = (RelativeLayout) inflater.inflate(R.layout.xadsdk_video_player, this);
    mVideoView = mPlayView.findViewById(R.id.xadsdk_player_video_textureView);
    mVideoView.setOnClickListener(this);
    mVideoView.setKeepScreenOn(true);
    mVideoView.setSurfaceTextureListener(this);
    initSmallLayoutMode();//init the small mode
  }

  //小模式状态
  private void initSmallLayoutMode() {
    LayoutParams params = new LayoutParams(mScreenWidth, mDestationHeight);
    params.addRule(RelativeLayout.CENTER_IN_PARENT);
    mPlayView.setLayoutParams(params);

    mMinPlayBtn = mPlayView.findViewById(R.id.xadsdk_small_play_btn);
    mFullBtn = mPlayView.findViewById(R.id.xadsdk_to_full_view);
    mLoadingBar = mPlayView.findViewById(R.id.loading_bar);
    mFrameView = mPlayView.findViewById(R.id.framing_view);
    mMinPlayBtn.setOnClickListener(this);
    mFullBtn.setOnClickListener(this);
  }

  public void isShowFullBtn(boolean isShow) {
    mFullBtn.setImageResource(isShow ? R.drawable.xadsdk_ad_mini : R.drawable.xadsdk_ad_mini_null);
    mFullBtn.setVisibility(isShow ? View.VISIBLE : View.GONE);
  }

  public boolean isRealPause() {
    return mIsRealPause;
  }

  public boolean isComplete() {
    return mIsComplete;
  }

  @Override protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
    LogUtils.e(TAG, "onVisibilityChanged" + visibility);
    super.onVisibilityChanged(changedView, visibility);
    if (visibility == VISIBLE && playerState == STATE_PAUSING) {
      if (isRealPause() || isComplete()) {
        pause();
      } else {
        decideCanPlay();
      }
    } else {
      pause();
    }
  }

  private void registerBroadcastReceiver() {
    if (mScreenReceiver == null) {
      mScreenReceiver = new ScreenEventReceiver();
      IntentFilter intentFilter = new IntentFilter();
      intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
      intentFilter.addAction(Intent.ACTION_USER_PRESENT);
      getContext().registerReceiver(mScreenReceiver, intentFilter);
    }
  }

  @Override protected void onDetachedFromWindow() {
    LogUtils.i(TAG, "onDetachedFromWindow");
    super.onDetachedFromWindow();
  }

  public CustomVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override public void onBufferingUpdate(MediaPlayer mp, int percent) {

  }

  @Override public void onCompletion(MediaPlayer mp) {
    if (listener != null) {
      listener.onAdVideoLoadComplete();
    }
    playBack();
    setIsComplete(true);
    setIsRealPause(true);
  }

  private void playBack() {
    LogUtils.d(TAG, "do playBack");
    setCurrentPlayState(STATE_PAUSING);
    mHandler.removeCallbacksAndMessages(null);
    if (mediaPlayer != null) {
      mediaPlayer.setOnSeekCompleteListener(null);
      mediaPlayer.seekTo(0);
      mediaPlayer.pause();
    }
    this.showPauseView(false);
  }

  @Override public boolean onError(MediaPlayer mp, int what, int extra) {
    LogUtils.e(TAG, "do error" + what);
    this.playerState = STATE_ERROR;
    mediaPlayer = mp;
    if (mediaPlayer != null) {
      mediaPlayer.reset();
    }
    if (mCurrentCount >= LOAD_TOTAL_COUNT) {
      showPauseView(false);
      if (this.listener != null) {
        listener.onAdVideoLoadFaied();
      }
    }
    this.stop();//重新load
    return true;
  }

  @Override public boolean onInfo(MediaPlayer mp, int what, int extra) {
    return true;
  }

  @Override public void onPrepared(MediaPlayer mp) {
      LogUtils.i(TAG, "onPrepared");
      showPlayView();
      mediaPlayer = mp;
      if (mediaPlayer != null) {
        mediaPlayer.setOnBufferingUpdateListener(this);
        mCurrentCount = 0;
        if (listener != null) {
          listener.onAdVideoLoadSuccess();
        }
        //满足自动播放条件，则直接播放
        if (Utils.canAutoPlay(getContext(), AdParameters.getCurrentSetting()) &&
        Utils.getVisiblePersent(mParentContainer) > SDKConstant.VIDEO_SCREEN_PERCENT) {
        } else {
          setCurrentPlayState(STATE_PAUSING);
          pause();
        }
      }
  }

  private void showPlayView() {
    mLoadingBar.clearAnimation();
    mLoadingBar.setVisibility(GONE);
    mMinPlayBtn.setVisibility(GONE);
    mFrameView.setVisibility(GONE);
  }

  public void setListener(ADVideoPlayerListener listener) {
    this.listener = listener;
  }

  public void setFrameLoadListener(ADFrameImageLoadListener frameLoadListener) {
    this.mFrameLoadListener = frameLoadListener;
  }



  @Override public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
    LogUtils.i(TAG, "onSurfaceTextureAvailable");
    videoSurface = new Surface(surface);
    checkMediaPlayer();
    mediaPlayer.setSurface(videoSurface);
    load();

  }

  @Override public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
    LogUtils.i(TAG, "onSurfaceTextureSizeChanged");
  }

  @Override public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
    return false;
  }

  @Override public void onSurfaceTextureUpdated(SurfaceTexture surface) {

  }

  @Override public void onClick(View v) {
    if (v == this.mMinPlayBtn) {
      if (this.playerState == STATE_PAUSING) {
        if (Utils.getVisiblePersent(mParentContainer) > SDKConstant.VIDEO_SCREEN_PERCENT) {
          resume();
          this.listener.onClickPlay();
        }
      } else {
        load();
      }
    } else if (v == this.mFullBtn) {
      this.listener.onClickFullScreenBtn();
    } else if (v == mVideoView) {
      this.listener.onCLickVideo();
    }
  }


  /**
   * 监听锁屏事件的广播接收器
   */
  private class ScreenEventReceiver extends BroadcastReceiver {

    @Override public void onReceive(Context context, Intent intent) {
      //主动锁屏时 pause，主动锁屏时，resume
      switch (intent.getAction()) {
        case Intent.ACTION_USER_PRESENT:
          if (playerState == STATE_PAUSING) {
            if (mIsRealPause) {
              //手动点的暂停，回来后还暂停
              pause();
            } else {
              decideCanPlay();
            }
          }
          break;
        case Intent.ACTION_SCREEN_OFF:
          if (playerState == STATE_PLAYING) {
            pause();
          }
          break;
        default:
          break;
      }
    }
  }

  private void decideCanPlay() {
    if (Utils.getVisiblePersent(mParentContainer) > SDKConstant.VIDEO_SCREEN_PERCENT) {
      resume();
    } else {
      pause();
    }
  }

  public void resume() {
    if (this.playerState != STATE_PAUSING) {
      return;
    }
    LogUtils.d(TAG, "do resume");
    if (!isPlaying()) {
      entryResumeState();
      mediaPlayer.setOnSeekCompleteListener(null);
      mediaPlayer.start();
      mHandler.sendEmptyMessage(TIME_MSG);
      showPauseView(true);
    } else {
      showPauseView(false);
    }
  }

  private void showPauseView(boolean show) {
    mFullBtn.setVisibility(show ? View.VISIBLE : View.GONE);
    mMinPlayBtn.setVisibility(show? View.GONE : View.VISIBLE);
    mLoadingBar.clearAnimation();
    mLoadingBar.setVisibility(GONE);
    if (!show) {
      mFrameView.setVisibility(VISIBLE);
      loadFrameImage();
    } else {
      mFrameView.setVisibility(GONE);
    }
  }

  /**
   * 进入播放状态时的更新
   */
  private void entryResumeState() {
    canPlay = true;
    setCurrentPlayState(STATE_PAUSING);
    setIsRealPause(false);
    setIsComplete(false);
  }

  public void setIsComplete(boolean isComplete) {
    mIsComplete = isComplete;
  }

  public void setIsRealPause(boolean isRealPause) {
    this.mIsRealPause = isRealPause;
  }


  public void pause() {
    if (this.playerState != STATE_PLAYING) {
      return;
    }
    LogUtils.d(TAG, "do pause");
    setCurrentPlayState(STATE_PAUSING);
    if (isPlaying()) {
      mediaPlayer.pause();
      if (!this.canPlay) {
        this.mediaPlayer.seekTo(0);
      }
    }
    this.showPauseView(false);
    mHandler.removeCallbacksAndMessages(null);
  }

  //全屏不显示暂停状态，后续可以整合，不必单独一个方法
  public void pauseForFullScreen() {
    if (playerState != STATE_PLAYING) {
      return;
    }
    LogUtils.d(TAG, "do full pause");
    setCurrentPlayState(STATE_PAUSING);
    if (isPlaying()) {
      mediaPlayer.pause();
      if (!this.canPlay) {
        mediaPlayer.seekTo(0);
      }
    }
    mHandler.removeCallbacksAndMessages(null);
  }


  public int getDuration() {
    if (mediaPlayer != null) {
      return mediaPlayer.getDuration();
    }
    return 0;
  }

  //跳到指定点播放视频
  public void seekAndResume(int position) {
    if (mediaPlayer != null) {
      showPauseView(true);
      entryResumeState();
      mediaPlayer.seekTo(position);
      mediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
        @Override
        public void onSeekComplete(MediaPlayer mp) {
          LogUtils.d(TAG, "do seek and resume");
          mediaPlayer.start();
          mHandler.sendEmptyMessage(TIME_MSG);
        }
      });
    }
  }

  //跳到指定点暂停视频
  public void seekAndPause(int position) {
    if (this.playerState != STATE_PLAYING) {
      return;
    }
    showPauseView(false);
    setCurrentPlayState(STATE_PAUSING);
    if (isPlaying()) {
      mediaPlayer.seekTo(position);
      mediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
        @Override
        public void onSeekComplete(MediaPlayer mp) {
          LogUtils.d(TAG, "do seek and pause");
          mediaPlayer.pause();
          mHandler.removeCallbacksAndMessages(null);
        }
      });
    }
  }

  public void load() {
    if (this.playerState != STATE_IDLE) {
      return;
    }
    LogUtils.d(TAG, "do play url" + this.mUrl);
    showLoadingView();
    try {
      setCurrentPlayState(STATE_IDLE);
      checkMediaPlayer();
      mute(true);
      mediaPlayer.setDataSource(this.mUrl);
      mediaPlayer.prepareAsync();
    } catch (IOException e) {
      LogUtils.e(TAG, e.getMessage());
      e.printStackTrace();
      stop();
    }
  }

  public void stop() {
    LogUtils.d(TAG, "do stop");
    if (this.mediaPlayer != null) {
      this.mediaPlayer.reset();
      this.mediaPlayer.setOnSeekCompleteListener(null);
      this.mediaPlayer.stop();
      this.mediaPlayer.release();
      this.mediaPlayer = null;
    }
    mHandler.removeCallbacksAndMessages(null);
    setCurrentPlayState(STATE_IDLE);
    if (mCurrentCount < LOAD_TOTAL_COUNT) {
      mCurrentCount += 1;
      load();
    } else {
      showPauseView(false);//显示暂停状态
    }
  }

  public void destroy() {
    LogUtils.d(TAG, " do destroy");
    if (this.mediaPlayer != null) {
      this.mediaPlayer.setOnSeekCompleteListener(null);
      this.mediaPlayer.stop();
      this.mediaPlayer.release();
      this.mediaPlayer = null;
    }
    setCurrentPlayState(STATE_IDLE);
    mCurrentCount = 0;
    setIsComplete(false);
    setIsRealPause(false);
    unRegisterBroadcastReceiver();
    mHandler.removeCallbacksAndMessages(null); //release all message and runnable
    showPauseView(false); //除了播放和loading外其余任何状态都显示pause
  }

  private void unRegisterBroadcastReceiver() {
    if (mScreenReceiver != null) {
      getContext().unregisterReceiver(mScreenReceiver);
    }
  }

  public boolean isFrameHidden() {
    return mFrameView.getVisibility() == View.VISIBLE ? false : true;
  }


  /**
   * true is no voice
   *
   * @param mute
   */
  public void mute(boolean mute) {
    LogUtils.d(TAG, "mute");
    isMute = mute;
    if (mediaPlayer != null && this.audioManager != null) {
      float volume = isMute ? 0.0f : 1.0f;
      mediaPlayer.setVolume(volume, volume);
    }
  }
  private void setCurrentPlayState(int state) {
    playerState = state;
  }

  private synchronized void checkMediaPlayer() {
    if (mediaPlayer == null) {
      mediaPlayer = createMediaPlayer();//每次都重新创建一个新的播放器
    }
  }

  private MediaPlayer createMediaPlayer() {
    mediaPlayer = new MediaPlayer();
    mediaPlayer.reset();
    mediaPlayer.setOnPreparedListener(this);
    mediaPlayer.setOnCompletionListener(this);
    mediaPlayer.setOnInfoListener(this);
    mediaPlayer.setOnErrorListener(this);
    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    if (videoSurface != null && videoSurface.isValid()) {
      mediaPlayer.setSurface(videoSurface);
    } else {
      stop();
    }
    return mediaPlayer;
  }

  private void showLoadingView() {
    mFullBtn.setVisibility(View.GONE);
    mLoadingBar.setVisibility(VISIBLE);
    AnimationDrawable anim = (AnimationDrawable) mLoadingBar.getBackground();
    anim.start();
    mMinPlayBtn.setVisibility(GONE);
    mFrameView.setVisibility(GONE);
    loadFrameImage();
  }

  /**
   * 异步加载图片
   */
  private void loadFrameImage() {
    if (mFrameLoadListener != null) {
      mFrameLoadListener.onStartFrameLoad(mFrameURI, new ImageLoaderListenr() {
        @Override public void onLoadingComplete(Bitmap loadedImage) {
          if (loadedImage != null) {
            mFrameView.setScaleType(ImageView.ScaleType.FIT_XY);
            mFrameView.setImageBitmap(loadedImage);
          } else {
            mFrameView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            mFrameView.setImageResource(R.drawable.xadsdk_img_error);
          }
        }
      });
    }
  }

  public void setDataSource(String url) {
    this.mUrl = url;
  }

  public void setFrameURI(String url) {
    mFrameURI = url;
  }

  /**
   * 供slot层来实现具体点击逻辑，具体逻辑还会变，
   * 如果对UI的点击没有具体监测的话可以不回调
   */
  public interface ADVideoPlayerListener {

    void onBufferUpdate(int time);

    void onClickFullScreenBtn();

    void onCLickVideo();

    void onClickBackBtn();

    void onClickPlay();

    void onAdVideoLoadSuccess();

    void onAdVideoLoadFaied();

    void onAdVideoLoadComplete();
  }

  public interface ADFrameImageLoadListener {

    void onStartFrameLoad(String url, ImageLoaderListenr loaderListenr);
  }

  public interface ImageLoaderListenr {
    /**
     * 如果图片下载不成功，传null
     */
    void onLoadingComplete(Bitmap loadedImage);
  }
}
