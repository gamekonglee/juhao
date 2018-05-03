package bc.juhao.com.ui.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.StateListDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import bc.juhao.com.cons.Constance;
import bc.juhao.com.utils.Mp4DownloadUtils;

/**
 * Created by gamekonglee on 2018/4/20.
 */

public class VideoPlayerActivity extends Activity implements View.OnClickListener,
        SeekBar.OnSeekBarChangeListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, View.OnTouchListener {

        private String url = null;// 播放地址
        private SurfaceView surfaceView = null;
        private SurfaceHolder surfaceHolder = null;
        private MediaPlayer mediaPlayer = null;
        private SeekBar seekBar = null;// 播放进度条
        private Button playBtn = null;// 播放
        private Button pauseBtn = null;// 暂停
        private TextView tv_current = null;
        private TextView tv_duration = null;
        private TextView divideTxt = null;
        private RelativeLayout controlLayout = null;// 操控界面布局
        private RelativeLayout mediaPlayerLayout = null;
        private LayoutParams mediaLayoutParams = null;
        private LinearLayout loadPrgLayout = null;

        private boolean isPlay = false;// 播放状态
        private boolean isPause = false;
        private boolean isShow = true;
        private boolean isEnd = false;
        private boolean isStartPlayer = false;
        private boolean isCustomStyle = false;// 开启自定义seekBar样式
        private int mDuration = 0;
        private int mSeekBarMax = 0;
        private int currentPosition = 0;// 当前播放位置
        private File mDownloadMP4File = null;
        private MediaPlayerCallback mCallback = null;

        private static final int playBtnID = 0x001;// 播放按钮id
        private static final int pauseBtnID = 0x002;// 暂停按钮id
        private static final int btnLayoutID = 0x003;// 按钮布局层id
        private static final int clockLayoutID = 0x004;// 计时布局层id
        private static final int surfaceViewID = 0x005;// surfaceView id
        /** 分秒 */
        private static final String DateFormatMS = "mm:ss";
        private static final int UPDATE_PROGRESS_MSG = 0x011;// 更新进度条和计时
        private static final int SHOW_CONTROL_MSG = 0x012;// 显示控制
        private static final int COMPLETION_PLAY_MSG = 0x013;// 播放完成

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN); // 全屏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

            // 测试视频地址
            url =getIntent().getStringExtra(Constance.url);
            String videoFileName = getRootPath() + getVideo(url);

            mDownloadMP4File = Mp4DownloadUtils.downloadMp4File(url, videoFileName, videoHandler);
            initPlayer();
        }

        /**
         * 获取外部存储根路径
         * @return
         */
    private String getRootPath() {
        String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        return rootPath;
    }

    /**
     * 获取视频文件名字
     * @param videoUrl
     * @return
     */
    private String getVideo(String videoUrl) {
        int startIndex = videoUrl.lastIndexOf("/");
        return url.substring(startIndex, videoUrl.length());
    }

    /**
     * 视频下载时的handler
     */
    private Handler videoHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                // 有MP4的mdat数据时，创建播放器
                case Mp4DownloadUtils.PLAYER_MP4_MSG:
                    isStartPlayer = true;
                    createMediaPlayer(mDownloadMP4File);
                    break;
                case Mp4DownloadUtils.DOWNLOAD_MP4_COMPLETION:

                    break;
                case Mp4DownloadUtils.DOWNLOAD_MP4_FAIL:

                    break;

                default:
                    break;
            }
        }

    };

    @SuppressWarnings("deprecation")
    private void initPlayer() {
        if (surfaceView == null) {
            surfaceView = new SurfaceView(this);
            surfaceView.getHolder()
                    .setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);// 不缓冲
            surfaceView.getHolder().setKeepScreenOn(true);// 保持屏幕高亮
            surfaceView.getHolder().addCallback(new SurfaceViewCallback());
            surfaceView.setId(surfaceViewID);
            surfaceView.setOnClickListener(this);
            surfaceView.setEnabled(false);
            surfaceHolder = surfaceView.getHolder();
        }

        initPlayerButton();
        RelativeLayout btnLayout = new RelativeLayout(this);
        btnLayout.setId(btnLayoutID);
        btnLayout.setGravity(Gravity.CENTER);
        LayoutParams btnParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        btnParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        btnParams.addRule(RelativeLayout.CENTER_VERTICAL);
        btnLayout.setLayoutParams(btnParams);
        btnLayout.addView(playBtn);
        btnLayout.addView(pauseBtn);

        initClockTextView();
        LinearLayout clockLayout = new LinearLayout(this);
        clockLayout.setId(clockLayoutID);
        clockLayout.setOrientation(LinearLayout.HORIZONTAL);
        LayoutParams clockTVLayoutParams = new LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        clockTVLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        clockTVLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
        clockLayout.setLayoutParams(clockTVLayoutParams);
        clockLayout.addView(tv_duration);
        clockLayout.addView(divideTxt);
        clockLayout.addView(tv_current);
        // 进度条必须在btnLayout和prgLayout后面
        initProgressSeekBar();
        // 下载进度
        initProgressBar();

        controlLayout = new RelativeLayout(this);
        LayoutParams rlParams = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        rlParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        int marginLeft = 5;
        int marginRight = 5;
        rlParams.setMargins(marginLeft, 0, marginRight, 0);
        controlLayout.setLayoutParams(rlParams);
        controlLayout.setVisibility(View.GONE);
        controlLayout.setBackgroundColor(Color.GRAY);
        controlLayout.addView(btnLayout);
        controlLayout.addView(seekBar);
        controlLayout.addView(clockLayout);

        setViewDrawableStyle();

        mediaPlayerLayout = new RelativeLayout(this);
        mediaLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        mediaLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        mediaPlayerLayout.setLayoutParams(mediaLayoutParams);
        mediaPlayerLayout.addView(surfaceView);
        mediaPlayerLayout.addView(controlLayout);
        mediaPlayerLayout.addView(loadPrgLayout);
        RelativeLayout layout = new RelativeLayout(this);
        layout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT));
        layout.addView(mediaPlayerLayout);
        setContentView(layout);
    }

    private void initPlayerButton() {
        int btnWidth = 60;
        int btnHeight = 60;
        // 开始按钮
        playBtn = new Button(this);
        playBtn.setId(playBtnID);
        playBtn.setOnClickListener(this);
        RelativeLayout.LayoutParams playParams = new RelativeLayout.LayoutParams(btnWidth, btnHeight);
        playBtn.setLayoutParams(playParams);
        playBtn.setEnabled(false);

        // 暂停按钮
        pauseBtn = new Button(this);
        pauseBtn.setId(pauseBtnID);
        pauseBtn.setOnClickListener(this);
        RelativeLayout.LayoutParams pauseParams = new RelativeLayout.LayoutParams(btnWidth, btnHeight);
        pauseBtn.setLayoutParams(pauseParams);
        pauseBtn.setVisibility(View.GONE);
    }

    private void initClockTextView() {
        // 播放进度
        tv_current = new TextView(this);
        tv_current.setTextColor(Color.WHITE);
        tv_current.setText(getStringByFormat(currentPosition, DateFormatMS));
        // 播放持续时间
        tv_duration = new TextView(this);
        tv_duration.setTextColor(Color.WHITE);
        tv_duration.setText(getStringByFormat(mDuration, DateFormatMS));
        // 分隔线
        divideTxt = new TextView(this);
        divideTxt.setText("/");
        divideTxt.setTextColor(Color.WHITE);
    }

    private void initProgressSeekBar() {
        // 播放进度条
        seekBar = new SeekBar(this);
        seekBar.setOnSeekBarChangeListener(this);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.RIGHT_OF, btnLayoutID);
        params.addRule(RelativeLayout.LEFT_OF, clockLayoutID);
        params.addRule(RelativeLayout.CENTER_VERTICAL);
        int leftpadding = 15;
        int rightpadding = 15;
        seekBar.setLayoutParams(params);
        seekBar.setPadding(leftpadding, 0, rightpadding, 0);
        seekBar.setOnTouchListener(this);
    }

    private void initProgressBar() {
        LayoutParams layoutParams = new LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);

        loadPrgLayout = new LinearLayout(this);
        loadPrgLayout.setLayoutParams(layoutParams);
        loadPrgLayout.setOrientation(LinearLayout.VERTICAL);
        loadPrgLayout.setVisibility(View.VISIBLE);
        loadPrgLayout.setGravity(Gravity.CENTER);

        LayoutParams params = new LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        ProgressBar loadPrgBar = new ProgressBar(this);
        loadPrgBar.setLayoutParams(params);
        loadPrgLayout.addView(loadPrgBar);
    }

    /**
     * 设置view的样式
     */
    private void setViewDrawableStyle() {
        try {
            // setBackground报错需要Android版本号在16以上
            playBtn.setBackground(createSelectorDrawable(
                    getAssetDrawable(this, "btn_play_normal.png"),
                    getAssetDrawable(this, "btn_play_pressed.png")));
            pauseBtn.setBackground(createSelectorDrawable(
                    getAssetDrawable(this, "btn_pause_normal.png"),
                    getAssetDrawable(this, "btn_pause_pressed.png")));
            if (isCustomStyle) {
                seekBar.setThumb(createSeekbarThumbDrawable(
                        getAssetDrawable(this, "progress_thumb_normal.png"),
                        getAssetDrawable(this, "progress_thumb_pressed.png")));
                seekBar.setProgressDrawable(createSeekbarProgressDrawable(
                        getAssetDrawable(this, "progress_bg.png"),
                        getAssetDrawable(this, "progress_secondary.png"),
                        getAssetDrawable(this, "progress.png")));
                controlLayout.setBackground(getAssetDrawable(this, "ctrl_layout_bg.png"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取accets目录下的图片
     * @param context
     * @param imageName
     * @return
     * @throws IOException
     */
    public Drawable getAssetDrawable(Context context, String imageName) throws IOException {
        return BitmapDrawable.createFromStream(context.getAssets().open(imageName), imageName);
    }

    /**
     * 创建图片选择器
     * @param normal
     * @param pressed
     * @return
     */
    private Drawable createSelectorDrawable(Drawable normal, Drawable pressed) {
        StateListDrawable drawable = new StateListDrawable();
        drawable.addState(new int[] { android.R.attr.state_focused },
                normal);
        drawable.addState(new int[] { android.R.attr.state_pressed },
                pressed);
        drawable.addState(new int[] {}, normal);
        return drawable;
    }

    /**
     * 创建seekBar拖动点样式图
     *
     * @param normal
     *            正常
     * @param pressed
     *            下压
     * @return
     */
    private Drawable createSeekbarThumbDrawable(Drawable normal, Drawable pressed) {
        StateListDrawable drawable = new StateListDrawable();
        drawable.addState(new int[] { android.R.attr.state_pressed }, pressed);
        drawable.addState(new int[] { android.R.attr.state_focused }, normal);
        drawable.addState(new int[] {}, normal);
        return drawable;
    }

    /**
     * 创建seekBar进度样式图
     *
     * @param background
     *            背景图
     * @param secondaryProgress
     *            第二进度能量图
     * @param progress
     *            进度能量图
     * @return
     */
    private Drawable createSeekbarProgressDrawable(Drawable background,
                                                   Drawable secondaryProgress, Drawable progress) {
        LayerDrawable progressDrawable = (LayerDrawable) seekBar
                .getProgressDrawable();
        int itmeSize = progressDrawable.getNumberOfLayers();
        Drawable[] outDrawables = new Drawable[itmeSize];
        for (int i = 0; i < itmeSize; i++) {
            switch (progressDrawable.getId(i)) {
                case android.R.id.background:// 设置进度条背景
                    outDrawables[i] = background;
                    break;
                case android.R.id.secondaryProgress:// 设置二级进度条
                    outDrawables[i] = secondaryProgress;
                    break;
                case android.R.id.progress:// 设置进度条
                    ClipDrawable oidDrawable = (ClipDrawable) progressDrawable
                            .getDrawable(i);
                    Drawable drawable = progress;
                    ClipDrawable proDrawable = new ClipDrawable(drawable,
                            Gravity.LEFT, ClipDrawable.HORIZONTAL);
                    proDrawable.setLevel(oidDrawable.getLevel());
                    outDrawables[i] = proDrawable;
                    break;
                default:
                    break;
            }
        }
        progressDrawable = new LayerDrawable(outDrawables);
        return progressDrawable;
    }

    private class SurfaceViewCallback implements
            android.view.SurfaceHolder.Callback {

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            createMediaPlayer();
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width,
                                   int height) {
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            destroy();
        }
    }

    private void createMediaPlayer() {
        createMediaPlayer(null);
    }

    private void createMediaPlayer(File videoFile) {
        if (url == null && videoFile == null && !isStartPlayer) {
            return;
        }

        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        }
        mediaPlayer.reset();
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnPreparedListener(this);// 设置监听事件
        mediaPlayer.setDisplay(surfaceHolder);// 把视频显示到surfaceView上
        if (videoFile != null) {
            setDataSource(videoFile);
        } else {
            setDataSource(url);
        }
        try {

        mediaPlayer.prepareAsync();// 准备播放
        }catch (Exception e){

        }
    }

    private void setDataSource(String src) {
        try {
            if (src.startsWith("http://")) {
                mediaPlayer.setDataSource(this, Uri.parse(src));
            } else {
                setDataSource(new File(src));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setDataSource(File file) {
        try {
            FileInputStream fis = new FileInputStream(file);
            mediaPlayer.setDataSource(fis.getFD());// 设置播放路径
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        if (isPause) {

        } else {
            mediaPlayer.start();
            playBtn.setEnabled(true);
            playBtn.setVisibility(View.GONE);
            pauseBtn.setVisibility(View.VISIBLE);

            if (currentPosition > 0) {
                mediaPlayer.seekTo(currentPosition);
            }
        }
        loadPrgLayout.setVisibility(View.GONE);
        controlLayout.setVisibility(View.VISIBLE);
        surfaceView.setEnabled(true);
        mHandler.post(showControlRunnable);
        mHandler.post(updateProgressRunnable);

        if (mSeekBarMax == 0 || mDuration == 0) {
            mSeekBarMax = seekBar.getMax();
            mDuration = mediaPlayer.getDuration();
            mediaPlayerLayout.setMinimumHeight(520);
            mediaPlayerLayout.invalidate();
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        removeHandlerTask();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress,
                                  boolean fromUser) {
        if (fromUser) {
            int milliseconds = countPosition(progress);
            tv_current.setText(getStringByFormat(milliseconds, DateFormatMS));
        }
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (mediaPlayer != null) {
            currentPosition = countPosition(seekBar.getProgress());
            mediaPlayer.seekTo(currentPosition);
            restartHandlerTack();
        }
    }

    private void start() {
        if (mediaPlayer != null && isPause) {
            isPlay = true;
            isPause = false;
            if (currentPosition > 0) {
                mediaPlayer.seekTo(currentPosition);
            }
            mediaPlayer.start();
            restartHandlerTack();
            playBtn.setVisibility(View.GONE);
            pauseBtn.setVisibility(View.VISIBLE);
        }
    }

    private void pause() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            isPlay = false;
            isPause = true;
            mediaPlayer.pause();
            playBtn.setVisibility(View.VISIBLE);
            pauseBtn.setVisibility(View.GONE);
            controlLayout.setVisibility(View.VISIBLE);
            currentPosition = mediaPlayer.getCurrentPosition();
            removeHandlerTask();
        }
    }

    private void destroy() {
        if (mediaPlayer != null && isPlay) {
            mediaPlayer.pause();
            controlLayout.setVisibility(View.VISIBLE);
            currentPosition = mediaPlayer.getCurrentPosition();
            removeHandlerTask();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case playBtnID:// 播放
                if (mediaPlayer == null) {
                    createMediaPlayer();
                } else {
                    start();
                }
                break;
            case pauseBtnID:// 暂停
                pause();
                break;
            case surfaceViewID:
                // 先移除任务，如果显示，重新post一次任务
                mHandler.removeCallbacks(showControlRunnable);
                if (isShow) {
                    controlLayout.setVisibility(View.GONE);
                    isShow = false;
                } else {
                    controlLayout.setVisibility(View.VISIBLE);
                    isShow = true;
                    mHandler.postDelayed(showControlRunnable, 5000);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v == seekBar && isEnd) {
            return true;
        }
        return false;
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        // 完成播放
        try {
            isEnd = true;
            isPlay = false;
            seekBar.setProgress(countProgress(mDuration));
            tv_current.setText(getStringByFormat(mDuration, DateFormatMS));
            playBtn.setVisibility(View.VISIBLE);
            pauseBtn.setVisibility(View.GONE);
            controlLayout.setVisibility(View.VISIBLE);
            playBtn.setEnabled(false);
            removeHandlerTask();
            mHandler.sendEmptyMessageDelayed(COMPLETION_PLAY_MSG, 500);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface MediaPlayerCallback {
        public void onCompletion();
    }

    public void setMediaPlayerCallback(MediaPlayerCallback callback) {
        mCallback = callback;
    }

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UPDATE_PROGRESS_MSG:// 更新播放进度和计时器
                    if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                        isPlay = true;
                        currentPosition = mediaPlayer.getCurrentPosition();
                        seekBar.setProgress(countProgress(currentPosition));
                        tv_current
                                .setText(getStringByFormat(currentPosition, DateFormatMS));
                        tv_duration.setText(getStringByFormat(mDuration, DateFormatMS));
                    }
                    break;
                case SHOW_CONTROL_MSG:// 显示播放控制控件
                    if (isPlay) {
                        isShow = false;
                        controlLayout.setVisibility(View.GONE);
                        mHandler.removeCallbacks(showControlRunnable);
                    } else {
                        isShow = true;
                        controlLayout.setVisibility(View.VISIBLE);
                    }
                    break;
                case COMPLETION_PLAY_MSG:// 完成播放
                    finish();
                    if (mCallback != null) {
                        mCallback.onCompletion();
                    }
                    Settings.System.getInt(getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 0);
                    break;

                default:
                    break;
            }
        }

    };

    /**
     * 重新启动任务
     */
    private void restartHandlerTack() {
        mHandler.post(updateProgressRunnable);
        mHandler.postDelayed(showControlRunnable, 5000);
    }

    /**
     * 移除任务
     */
    private void removeHandlerTask() {
        mHandler.removeCallbacks(showControlRunnable);
        mHandler.removeCallbacks(updateProgressRunnable);
    }

    /**
     * 更新进度条线程
     */
    private Runnable updateProgressRunnable = new Runnable() {

        @Override
        public void run() {
            mHandler.sendEmptyMessage(UPDATE_PROGRESS_MSG);
            mHandler.postDelayed(updateProgressRunnable, 1000);
        }
    };

    /**
     * 显示控制线程
     */
    private Runnable showControlRunnable = new Runnable() {

        @Override
        public void run() {
            mHandler.sendEmptyMessage(SHOW_CONTROL_MSG);
            mHandler.postDelayed(showControlRunnable, 5000);
        }
    };

    /**
     * 计算播放进度
     *
     * @param position
     * @return
     */
    private int countProgress(int position) {
        if (mDuration == 0) {
            return 0;
        }

        return position * mSeekBarMax / mDuration;
    }

    /**
     * 计算播放位置
     *
     * @param progress
     * @return
     */
    private int countPosition(int progress) {
        if (mSeekBarMax == 0) {
            return 0;
        }

        return progress * mDuration / mSeekBarMax;
    }

    /**
     * 描述：获取milliseconds表示的日期时间的字符串.
     *
     * @param milliseconds
     * @param format
     *            格式化字符串，如："yyyy-MM-dd HH:mm:ss"
     * @return String 日期时间字符串
     */
    private String getStringByFormat(long milliseconds, String format) {
        String thisDateTime = null;
        try {
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
            mSimpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+0"));
            thisDateTime = mSimpleDateFormat.format(milliseconds);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return thisDateTime;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            currentPosition = 0;
        }
        System.gc();
    }

}
