package bc.juhao.com.ui.activity.user;

import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.aliyun.iot.ilop.demo.DemoApplication;

import bc.juhao.com.R;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.ui.activity.IssueApplication;
import bc.juhao.com.ui.view.ScannerUtils;
import bc.juhao.com.ui.view.popwindow.ShareProductPopWindow;
import bc.juhao.com.utils.ImageUtil;
import bocang.utils.MyToast;
import bocang.view.BaseActivity;

/**
 * @author: Jun
 * @date : 2017/9/19 9:41
 * @description :我的邀请码
 */
public class InvitationCodeActivity extends BaseActivity implements View.OnLongClickListener {
    private TextView invitation_code_tv;
    private String mYaoQing = "";
    private View share_v;
    private ShareProductPopWindow mProductPopWindow;
    private RelativeLayout main_ll;
    private String mTitle = "";
    private String mCardPath = "";
    private ScrollView sv;
    private String mShareImagePath;
    private TextView tv_tips;
    private Bitmap mBitmap;

    @Override
    protected void InitDataView() {
        if(DemoApplication.mUserObject==null){
            MyToast.show(this,"请稍等，数据加载中");
            return;
        }
        mYaoQing = DemoApplication.mUserObject.getString(Constance.invite_code);
        invitation_code_tv.setText("邀请码:"+mYaoQing);
//        mBitmap = ImageUtil.getBitmapByView(sv);
    }

    @Override
    protected void initController() {

    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_invitation_post);
        setStatuTextColor(this, Color.WHITE);
        setFullScreenColor(Color.TRANSPARENT,this);
        invitation_code_tv = (TextView)findViewById(R.id.invitation_code_tv);
        invitation_code_tv.setOnLongClickListener(this);
        share_v = getViewAndClick(R.id.share_v);
        ImageView iv_code=findViewById(R.id.iv_code);
        iv_code.setOnLongClickListener(this);
        main_ll = (RelativeLayout) findViewById(R.id.main_ll);
        sv = (ScrollView)findViewById(R.id.sv);
        tv_tips = findViewById(R.id.tv_tips);
        SpannableStringBuilder spannable=new SpannableStringBuilder(tv_tips.getText());
        spannable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.green)),tv_tips.getText().toString().length()-4,tv_tips.getText().toString().length()
                , Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_tips.setText(spannable);
        showLoading();
        new Thread(){
            @Override
            public void run() {
                super.run();
                SystemClock.sleep(1000);
                handler.sendEmptyMessage(0);
            }
        }.start();
    }
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mBitmap=ImageUtil.getBitmapByView(sv);
            hideLoading();
        }
    };
    @Override
    protected void initData() {
    }

    @Override
    protected void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.share_v://分享
                getShare();
                break;
        }
    }

    private void getShare(){
        setShowDialog(true);
        showLoading();
        mProductPopWindow = new ShareProductPopWindow(this);
        mProductPopWindow.mActivity = this;
        mProductPopWindow.mShareTitle = mTitle;
        mProductPopWindow.mIsLocal = true;
        mProductPopWindow.mBitmap =  mBitmap;
        new Thread(new Runnable() {
            @Override
            public void run() {

                mShareImagePath = ScannerUtils.saveImageToGallery02(InvitationCodeActivity.this.getBaseContext(),
                        mProductPopWindow.mBitmap, ScannerUtils.ScannerType.RECEIVER);
                InvitationCodeActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mProductPopWindow.mShareImgPath = mShareImagePath;
                        mProductPopWindow.mSharePath = mShareImagePath;
                        mProductPopWindow.onShow(main_ll);
                        InvitationCodeActivity.this.hideLoading();
                    }
                });
            }
        }).start();
    }

    @Override
    public boolean onLongClick(View v) {
        ClipboardManager cm = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
        cm.setText(mYaoQing);
        MyToast.show(this, "复制成功!");
        return false;
    }
}
