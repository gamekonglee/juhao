package bc.juhao.com.ui.activity.user;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import bc.juhao.com.R;
import bc.juhao.com.common.BaseActivity;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.ui.view.FullScreenVideoView;

/**
 * Created by gamekonglee on 2018/4/19.
 */

public class SuccessVideoActivity extends BaseActivity implements View.OnClickListener {
    private TextView text;//视频保存的路径
    private Button button1;//播放开关
    private Button button2;//暂停开关
    private Button button3;//重新播放开关
    private Button button4;//视频大小开关
    private FullScreenVideoView videoView1;//视频播放控件
    private String file;//视频路径
    private Button btn_yes;

    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {

    }

    @Override
    protected void initView() {
    setContentView(R.layout.activity_video_success);
//    setColor(this, Color.WHITE);
    fullScreen(this);
        Bundle bundle = getIntent().getExtras();
        file = bundle.getString("text");//获得拍摄的短视频保存地址
        init();
        setValue();
        boolean islook=getIntent().getBooleanExtra(Constance.is_look,false);
        if(islook){
            btn_yes.setVisibility(View.GONE);
        }
    }

    //初始化
    private void init() {
        text = (TextView) findViewById(R.id.text);
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        button4 = (Button) findViewById(R.id.button4);
        videoView1 =  findViewById(R.id.videoView1);
//        videoView1.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, UIUtils.getScreenHeight(this)));
        btn_yes = findViewById(R.id.btn_yes);
    }

    //设置
    private void setValue() {
        text.setText(file);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        videoView1.setVideoPath(file);
        videoView1.start();
        videoView1.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
                mp.setLooping(true);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1:
                videoView1.start();
                break;

            case R.id.button2:
                videoView1.pause();
                break;

            case R.id.button3:
                videoView1.resume();
                videoView1.start();
                break;

            case R.id.button4:
                Toast.makeText(this, "视频长度："+(videoView1.getDuration()/1024)+"M", Toast.LENGTH_SHORT).show();
                break;

            default:
                break;
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
    public void no(View v){
        finish();
    }
    public void yes(View v){
        Intent intent=new Intent();
        intent.putExtra(Constance.path,file);
        setResult(300,intent);
        finish();
    }
}
