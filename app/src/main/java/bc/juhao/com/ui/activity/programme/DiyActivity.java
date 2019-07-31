package bc.juhao.com.ui.activity.programme;


import android.content.Intent;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aliyun.iot.ilop.demo.DemoApplication;

import java.util.ArrayList;
import java.util.List;

import bc.juhao.com.R;
import bc.juhao.com.bean.GoodsShape;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.controller.programme.DiyController;
import bc.juhao.com.ui.view.ShowDialog;
import bc.juhao.com.ui.view.designview.IPicsGestureListener;
import bc.juhao.com.utils.MyShare;
import bocang.json.JSONArray;
import bocang.json.JSONObject;
import bocang.utils.AppUtils;
import bocang.view.BaseActivity;

/**
 * @author: Jun
 * @date : 2017/2/18 10:31
 * @description :场景配灯
 */
public class DiyActivity extends BaseActivity implements IPicsGestureListener {
    private DiyController mController;
    private ImageView goSaveIv, goproductIv, goscreenctIv, jingxian_iv, goBrightnessIv, goHelpIv,goBackBtn01,sceneBgIv;
    private LinearLayout pro_jingxiang_ll, goCart_ll, detail_ll, delete_ll,add_data_ll;
    private View left_ll, right_ll;
    private FrameLayout sceneFrameLayout,main_fl;
    public String mPath;
    public SparseArray<JSONObject> mSelectedLightSA = new SparseArray<>();
    public JSONObject mGoodsObject;
    public String mProperty = "";
    public String mProductId = "";
    public boolean isFromPhoto;
    public List<GoodsShape> goodsShapeList;
    public List<JSONObject> goodsList;
    public int mScaleType = 0;

    private List<Integer> mHelpImages;
    private int mIndexHelp = 0;
    private ImageView yindao_iv;
    public RelativeLayout yindao_rl;
    public LinearLayout select_ll;
    private TextView select_product_tv,select_diy_tv,add_data_tv;
    private ListView product_lv,diy_lv;
//    private RelativeLayout ContainerRl;

    public int mSelectType=0;


//    private PicsGestureLayout picsGestureLayout = null;

    @Override
    protected void InitDataView() {
        mSelectType=0;
        switchProOrDiy();
    }

    @Override
    protected void initController() {
        mController = new DiyController(this);
    }

    @Override
    protected void initView() {
        //去除title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去掉Activity上面的状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_diy02);
        //沉浸式状态栏
        goSaveIv = getViewAndClick(R.id.goSaveIv);
        goBrightnessIv = getViewAndClick(R.id.goBrightnessIv);
        goHelpIv = getViewAndClick(R.id.goHelpIv);
        goproductIv = getViewAndClick(R.id.goproductIv);
        goscreenctIv = getViewAndClick(R.id.goscreenctIv);
        sceneFrameLayout = getViewAndClick(R.id.sceneFrameLayout);
        main_fl = getViewAndClick(R.id.main_fl);
        jingxian_iv = getViewAndClick(R.id.jingxian_iv);

        detail_ll = getViewAndClick(R.id.detail_ll);
        delete_ll = getViewAndClick(R.id.delete_ll);
        goCart_ll = getViewAndClick(R.id.goCart_ll);
        goBackBtn01 = getViewAndClick(R.id.goBackBtn01);
        left_ll = getViewAndClick(R.id.left_ll);
        right_ll = getViewAndClick(R.id.right_ll);
        select_product_tv = getViewAndClick(R.id.select_product_tv);
        select_diy_tv = getViewAndClick(R.id.select_diy_tv);
        pro_jingxiang_ll = getViewAndClick(R.id.pro_jingxiang_ll);
//        ContainerRl = getViewAndClick(R.id.ContainerRl);
        add_data_ll = getViewAndClick(R.id.add_data_ll);
        yindao_iv = (ImageView) findViewById(R.id.yindao_iv);
        yindao_rl = (RelativeLayout) findViewById(R.id.yindao_rl);
        select_ll = (LinearLayout) findViewById(R.id.select_ll);

        boolean showHelp= MyShare.get(this).getBoolean(Constance.SHOWHELP);
        if(!showHelp){
            yindao_rl.setVisibility(View.VISIBLE);
            MyShare.get(this).putBoolean(Constance.SHOWHELP, true);
            select_ll.setVisibility(View.GONE);
        }else{
            yindao_rl.setVisibility(View.GONE);
            select_ll.setVisibility(View.VISIBLE);
        }
        left_ll = getViewAndClick(R.id.left_ll);
        right_ll = getViewAndClick(R.id.right_ll);
        sceneBgIv = getViewAndClick(R.id.sceneBgIv);
        product_lv = (ListView)findViewById(R.id.product_lv);
        diy_lv = (ListView)findViewById(R.id.diy_lv);
        add_data_tv = (TextView)findViewById(R.id.add_data_tv);
//        picsGestureLayout = getViewAndClick(R.id.picsGestureLayout);
//        picsGestureLayout.setPicsGestureListener(this);


    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        mPath = intent.getStringExtra(Constance.photo);
        mProperty = intent.getStringExtra(Constance.property);
        mGoodsObject = (JSONObject) intent.getSerializableExtra(Constance.product);
        isFromPhoto = intent.getBooleanExtra(Constance.FROMPHOTO, false);

        mHelpImages = new ArrayList<>();
        mHelpImages.add(R.mipmap.design1);
        mHelpImages.add(R.mipmap.design2);
        mHelpImages.add(R.mipmap.design3);
        mHelpImages.add(R.mipmap.design4);
        mHelpImages.add(R.mipmap.design5);
        mHelpImages.add(R.mipmap.design6);
        mHelpImages.add(R.mipmap.design7);

        if (isFromPhoto == true) {
            mPath = intent.getStringExtra(Constance.img_path);
            goodsShapeList = (List<GoodsShape>) intent.getSerializableExtra(Constance.productshape);
            goodsList = (List<JSONObject>) intent.getSerializableExtra(Constance.productlist);
            mScaleType = intent.getIntExtra(Constance.scaleType, 0);
        }

        if (AppUtils.isEmpty(mGoodsObject))
            return;
        mProductId = mGoodsObject.getString(Constance.id);



    }

    @Override
    protected void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.goSaveIv:
                mController.saveDiy();
                break;
            case R.id.photographIv:
                mController.goPhoto();
                break;
            case R.id.goBrightnessIv:
                mController.goBrightness();
                break;
            case R.id.goHelpIv:
                yindao_rl.setVisibility(View.VISIBLE);
                mIndexHelp=0;
                yindao_iv.setImageResource(mHelpImages.get(mIndexHelp));
                select_ll.setVisibility(View.GONE);
                break;
            case R.id.delete_ll:
                mController.goDetele();
                break;
            case R.id.goCart_ll:
                mController.goShoppingCart();
                break;
            case R.id.goproductIv:
                mController.selectProduct();
                break;
            case R.id.goscreenctIv:
                mController.selectScreen();
                break;
            case R.id.sceneFrameLayout:
                mController.selectIsFullScreen();
                break;
            case R.id.main_fl:
                mController.selectIsFullScreen();
                break;
            case R.id.sceneBgIv:
                mController.selectIsFullScreen();
                break;
            case R.id.jingxian_iv:
                mController.sendBackgroudImage();
                break;
            case R.id.detail_ll:
                mController.getProductDetail();
                break;
            case R.id.pro_jingxiang_ll:
                mController.sendProductJinxianImage();
                break;
            case R.id.left_ll:
                getYindaoImage(0);
                break;
            case R.id.right_ll:
                getYindaoImage(1);
                break;
            case R.id.select_product_tv://切换产品
                mSelectType=0;
                switchProOrDiy();
                mController.selectShowData();
                break;
            case R.id.select_diy_tv://切换场景
                mSelectType=1;
                switchProOrDiy();
                mController.selectShowData();
                break;
            case R.id.add_data_ll://添加数据
                if(mSelectType==0){
                    mController.selectProduct();
                }else{
                    mController.selectSceneDatas();
                }
                break;
            case R.id.goBackBtn01://退出
                getFinish();
                break;

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        getFinish();
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 退出
     */
    private void getFinish(){
        ShowDialog mDialog=new ShowDialog();
        mDialog.show(this, "提示", "是否退出配灯界面?", new ShowDialog.OnBottomClickListener() {
            @Override
            public void positive() {
                finish();
                DemoApplication.mSelectProducts = new JSONArray();
                DemoApplication.mSelectScreens = new JSONArray();
            }

            @Override
            public void negtive() {

            }
        });
    }

    /**
     * 切换产品或场景
     */
    public void switchProOrDiy(){
        select_product_tv.setBackgroundResource(R.color.deep_transparent);
        select_diy_tv.setBackgroundResource(R.color.deep_transparent);
        product_lv.setVisibility(View.GONE);
        diy_lv.setVisibility(View.GONE);
        if(mSelectType==0){
            select_product_tv.setBackgroundResource(R.color.transparent);
            product_lv.setVisibility(View.VISIBLE);
            add_data_tv.setText("选择产品");

        }else{
            select_diy_tv.setBackgroundResource(R.color.transparent);
            product_lv.setVisibility(View.VISIBLE);
            add_data_tv.setText("选择场景");

        }

    }

    private void getYindaoImage(int type) {
        if (type == 0) {//左边
            if (mIndexHelp != 0) {
                mIndexHelp = mIndexHelp - 1;
                yindao_iv.setImageResource(mHelpImages.get(mIndexHelp));
            } else {
                yindao_rl.setVisibility(View.GONE);
                select_ll.setVisibility(View.VISIBLE);
            }

        } else {//右边
            if (mIndexHelp != mHelpImages.size() - 1) {
                mIndexHelp = mIndexHelp + 1;
                yindao_iv.setImageResource(mHelpImages.get(mIndexHelp));
            } else {
                yindao_rl.setVisibility(View.GONE);
                select_ll.setVisibility(View.VISIBLE);
            }

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mController.ActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //        PermissionUtils.requestPermissionsResult(this, requestCode, permissions, grantResults, new PermissionUtils.PermissionGrant() {
        //            @Override
        //            public void onPermissionGranted(int requestCode) {
        //                FileUtil.openImage(DiyActivity.this);
        //            }
        //        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mController.setRestart()
        ;
    }

    @Override
    public void onPicsGestureScreenChanged(Boolean isShow) {
        if(!isShow){
            mController.selectIsFullScreen();
        }
    }

    @Override
    public void onPicsGesturDelete(Boolean isShow) {
        mSelectedLightSA.remove(DemoApplication.mLightIndex);
    }
}
