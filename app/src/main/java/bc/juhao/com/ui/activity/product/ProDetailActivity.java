package bc.juhao.com.ui.activity.product;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import bc.juhao.com.R;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.controller.product.ProductDetailController;
import bc.juhao.com.ui.activity.MainActivity;
import bocang.json.JSONObject;
import bocang.view.BaseActivity;

/**
 * @author: Jun
 * @date : 2017/2/13 17:50
 * @description :
 */
public class ProDetailActivity extends BaseActivity {
    public static boolean isJuHao;
    public int mProductId=0;
    public int mOrderId=1;
    public String mOrderid="";
    public ProductDetailController mController;
    private LinearLayout product_ll, detail_ll, parament_ll, callLl, go_photo_Ll,sun_image_ll;
    private Button toDiyBtn, toCartBtn;
    private ImageView share_iv;
    public static JSONObject goodses;
    public String mProperty = "";
    public String mPropertyValue = "";
    public int mPrice = 0;
    private RelativeLayout shopping_cart_Ll;
    public static  boolean isXianGou;
    public com.alibaba.fastjson.JSONObject mProductObject;
    private ImageView iv_home;
    public LinearLayout tuijian_ll;
    private Button toBuyBtn;


    @Override
    protected void InitDataView() {
        //        mController.sendProductDetail();

    }

    @Override
    protected void initController() {
        mController = new ProductDetailController(this);
    }


    @Override
    protected void initView() {
        setContentView(R.layout.activity_product_detail);
        setColor(this, Color.WHITE);
        product_ll = getViewAndClick(R.id.product_ll);
        detail_ll = getViewAndClick(R.id.detail_ll);
        parament_ll = getViewAndClick(R.id.parament_ll);
        sun_image_ll = getViewAndClick(R.id.sun_image_ll);
        tuijian_ll = getViewAndClick(R.id.tuijian_ll);
        callLl = getViewAndClick(R.id.callLl);
        shopping_cart_Ll = getViewAndClick(R.id.shopping_cart_Ll);
        toDiyBtn = getViewAndClick(R.id.toDiyBtn);
        toCartBtn = getViewAndClick(R.id.toCartBtn);
        share_iv = getViewAndClick(R.id.share_iv);
        go_photo_Ll = getViewAndClick(R.id.go_photo_Ll);
        iv_home = getViewAndClick(R.id.iv_home);
        toBuyBtn = getViewAndClick(R.id.toBuyBtn);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mController.getCartMun();
    }


    @Override
    protected void initData() {
        Intent intent = getIntent();
        mProductId = intent.getIntExtra(Constance.product, 0);
        mOrderid = intent.getStringExtra(Constance.order_id);
        isJuHao=false;

    }

    @Override
    protected void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.product_ll:
                mController.selectProductType(R.id.product_ll);
                break;
            case R.id.detail_ll:
                mController.selectProductType(R.id.detail_ll);
                break;
//            case R.id.parament_ll:
//                mController.selectProductType(R.id.parament_ll);
//                break;
            case R.id.sun_image_ll:
                mController.selectProductType(R.id.sun_image_ll);
                break;
            case R.id.tuijian_ll:
                mController.selectProductType(R.id.tuijian_ll);
                break;
            case R.id.callLl:
                if (!isToken())
                mController.sendCall("尝试连接聊天服务..请连接?");
                break;
            case R.id.shopping_cart_Ll:
                if (!isToken())
                    mController.getShoopingCart();
                break;
            case R.id.toDiyBtn:
                mController.GoDiyProduct();
                break;
            case R.id.toCartBtn:
                if (!isToken())
                    mController.GoShoppingCart();
                break;
            case R.id.share_iv:
                mController.setShare();
//                IntentUtil.startActivity(this,ShareProductActivity.class,false);
                break;
            case R.id.go_photo_Ll:
                if (!isToken())
                    mController.GoDiyProduct();
                break;
            case R.id.iv_home:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.toBuyBtn:
                if (!isToken())
                mController.toBuy();
                break;
        }
    }


}
