package bc.juhao.com.ui.activity.programme;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tencent.connect.share.QQShare;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;

import bc.juhao.com.R;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.controller.programme.ShareProgrammeController;
import bocang.view.BaseActivity;

/**
 * @author: Jun
 * @date : 2017/6/8 16:08
 * @description :
 */
public class ShareProgrammeActivity extends BaseActivity {
    private ShareProgrammeController mController;
    private Button look_fangan_tv, go_diy_tv;
    private LinearLayout share_link_ll, share_image_ll, save_image_ll, wechat_ll, wechatmoments_ll, wechat_collect_ll, share_qq_ll;
    private ImageView share_link_iv,share_image_iv;
    private TextView share_link_tv,share_image_tv;
    public int mShareType=0;
    public String mSharePath="";
    public String mShareImgPath="";
    public String mShareTitle="";
    public int typeShare=1;
    public String id;


    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {
        mController = new ShareProgrammeController(this);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_share_programme);
        look_fangan_tv = getViewAndClick(R.id.look_fangan_tv);
        go_diy_tv = getViewAndClick(R.id.go_diy_tv);
        share_link_ll = getViewAndClick(R.id.share_link_ll);
        share_image_ll = getViewAndClick(R.id.share_image_ll);
        save_image_ll = getViewAndClick(R.id.save_image_ll);
        wechat_ll = getViewAndClick(R.id.wechat_ll);
        wechatmoments_ll = getViewAndClick(R.id.wechatmoments_ll);
        wechat_collect_ll = getViewAndClick(R.id.wechat_collect_ll);
        share_qq_ll = getViewAndClick(R.id.share_qq_ll);
        share_link_tv = (TextView)findViewById(R.id.share_link_tv);
        share_image_tv = (TextView)findViewById(R.id.share_image_tv);
        share_link_iv = (ImageView)findViewById(R.id.share_link_iv);
        share_image_iv = (ImageView)findViewById(R.id.share_image_iv);
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        mSharePath=intent.getStringExtra(Constance.SHARE_PATH);
        mShareImgPath=intent.getStringExtra(Constance.SHARE_IMG_PATH);
        mShareTitle=intent.getStringExtra(Constance.TITLE);
        id = intent.getStringExtra(Constance.id);

    }

    @Override
    protected void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.look_fangan_tv:
                mController.getFanganDetail();
                break;
            case R.id.go_diy_tv:
                finish();
                break;
            case R.id.share_link_ll:
                mShareType=0;
                getShareType(mShareType);
                break;
            case R.id.share_image_ll:
                mShareType=1;
                getShareType(mShareType);
                break;
            case R.id.save_image_ll:
                mController.getSaveImage();
                break;
            case R.id.wechat_ll:
                typeShare= SendMessageToWX.Req.WXSceneSession;
                mController.getShareData();
                break;
            case R.id.wechatmoments_ll:
                typeShare= SendMessageToWX.Req.WXSceneTimeline;
                mController.getShareData();
                break;
            case R.id.wechat_collect_ll:
                typeShare= SendMessageToWX.Req.WXSceneFavorite;
                mController.getShareData();
                break;
            case R.id.share_qq_ll:
                typeShare= QQShare.SHARE_TO_QQ_TYPE_IMAGE;
                mController.getShareData();
                break;

        }
    }




    @Override
    protected void onStart() {
        super.onStart();
        hideLoading();
    }

    /**
     * 分享类型
     * @param type
     */
    public void getShareType(int type) {
        share_link_tv.setTextColor(this.getResources().getColor(R.color.txt_black));
        share_image_tv.setTextColor(this.getResources().getColor(R.color.txt_black));
        share_link_iv.setImageResource(R.drawable.design_share_web);
        share_image_iv.setImageResource(R.drawable.design_share_image);
        switch (type){
            case 0:
                share_link_tv.setTextColor(this.getResources().getColor(R.color.red));
                share_link_iv.setImageResource(R.drawable.design_share_web_select);
                break;
            case 1:
                share_image_tv.setTextColor(this.getResources().getColor(R.color.red));
                share_image_iv.setImageResource(R.drawable.design_share_image_select);
                break;
        }
    }


}
