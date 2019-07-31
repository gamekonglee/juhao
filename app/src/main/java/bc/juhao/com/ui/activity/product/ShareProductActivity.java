package bc.juhao.com.ui.activity.product;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import bc.juhao.com.R;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.controller.product.ShareProductController;
import bocang.json.JSONObject;
import bocang.view.BaseActivity;

/**
 * @author: Jun
 * @date : 2017/7/29 14:42
 * @description :
 */
public class ShareProductActivity extends BaseActivity {
    private ShareProductController mController;
    private TextView share_01_tv, share_02_tv, share_03_tv, share_04_tv;
    public JSONObject mGoodsObject;
    private Button save_bt;


    @Override
    protected void InitDataView() {
        getShareProductType(share_01_tv);
        mController.ShareType(R.id.share_01_tv);
    }

    @Override
    protected void initController() {
        mController = new ShareProductController(this);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_share_product);
        setColor(this, Color.TRANSPARENT);
        share_01_tv = getViewAndClick(R.id.share_01_tv);
        share_02_tv = getViewAndClick(R.id.share_02_tv);
        share_03_tv = getViewAndClick(R.id.share_03_tv);
        share_04_tv = getViewAndClick(R.id.share_04_tv);
        save_bt = getViewAndClick(R.id.save_bt);
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        mGoodsObject = (JSONObject) intent.getSerializableExtra(Constance.product);
    }

    @Override
    protected void onViewClick(View v) {
        if (v.getId() == R.id.save_bt) {
            mController.getShare();
            return;
        }
        getShareProductType((TextView) v);
        mController.ShareType(v.getId());
    }

    /**
     * 选择分享方式
     */
    private void getShareProductType(TextView tv) {
        share_01_tv.setTextColor(this.getResources().getColor(R.color.txt_black));
        share_02_tv.setTextColor(this.getResources().getColor(R.color.txt_black));
        share_03_tv.setTextColor(this.getResources().getColor(R.color.txt_black));
        share_04_tv.setTextColor(this.getResources().getColor(R.color.txt_black));
        share_01_tv.setBackgroundResource(R.color.white);
        share_02_tv.setBackgroundResource(R.color.white);
        share_03_tv.setBackgroundResource(R.color.white);
        share_04_tv.setBackgroundResource(R.color.white);
        tv.setTextColor(this.getResources().getColor(R.color.white));
        tv.setBackgroundResource(R.color.red);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mController.onActivityResult(requestCode,resultCode,data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
