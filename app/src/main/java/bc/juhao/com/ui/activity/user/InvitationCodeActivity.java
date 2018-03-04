package bc.juhao.com.ui.activity.user;

import android.content.ClipboardManager;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

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
    private LinearLayout main_ll;
    private String mTitle = "";
    private String mCardPath = "";
    private ScrollView sv;
    private String mShareImagePath;

    @Override
    protected void InitDataView() {
        mYaoQing = IssueApplication.mUserObject.getString(Constance.invite_code);
        invitation_code_tv.setText("邀请码:"+mYaoQing);
    }

    @Override
    protected void initController() {

    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_invitation_post);
        invitation_code_tv = (TextView)findViewById(R.id.invitation_code_tv);
        invitation_code_tv.setOnLongClickListener(this);
        share_v = getViewAndClick(R.id.share_v);
        main_ll = (LinearLayout)findViewById(R.id.main_ll);
        sv = (ScrollView)findViewById(R.id.sv);

    }

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
        mProductPopWindow.mBitmap = ImageUtil.getBitmapByView(sv);
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
