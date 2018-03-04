package bc.juhao.com.ui.activity.programme.camera;

import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import bc.juhao.com.R;
import bc.juhao.com.cons.Constance;
import bocang.json.JSONObject;
import bocang.utils.AppUtils;
import bocang.utils.PermissionUtils;
import bocang.view.BaseActivity;

/**
 * @author: Jun
 * @date : 2017/6/12 16:36
 * @description :
 */
public class CameraActivity extends BaseActivity {
    public String mPath;
    public JSONObject mGoodsObject;
    public String mProperty="";
    public String mProductId="";
    private Camera2Fragment mFragment;

    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {
    }

    @Override
    protected void initView() {
        //去除title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去掉Activity上面的状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_camera);
//        mFragment=new Camera2Fragment();

//        PermissionUtils.requestPermission(CameraActivity.this, PermissionUtils.CODE_CAMERA, new PermissionUtils.PermissionGrant() {
//            @Override
//            public void onPermissionGranted(int requestCode) {
//
//
//                //必需继承FragmentActivity,嵌套fragment只需要这行代码
//                getSupportFragmentManager().beginTransaction().replace(R.id.container,
//                        mFragment).commitAllowingStateLoss();
//            }
//        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtils.requestPermissionsResult(this, requestCode, permissions, grantResults, new PermissionUtils.PermissionGrant() {
            @Override
            public void onPermissionGranted(int requestCode) {


                //必需继承FragmentActivity,嵌套fragment只需要这行代码
                getSupportFragmentManager().beginTransaction().replace(R.id.container,
                        mFragment).commitAllowingStateLoss();
            }
        });
    }


    @Override
    protected void initData() {
        Intent intent = getIntent();
        mPath=intent.getStringExtra(Constance.photo);
        mProperty=intent.getStringExtra(Constance.property);
        mGoodsObject= (JSONObject) intent.getSerializableExtra(Constance.product);
        if(AppUtils.isEmpty(mGoodsObject)) return;
        mProductId=mGoodsObject.getString(Constance.id);
    }

    @Override
    protected void onViewClick(View v) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_OK) { // 返回成功
            switch (requestCode) {
                case Constance.PHOTO_WITH_DATA: // 从图库中选择图片
                    String bmPath = data.getData().toString();
                    mFragment.mController.goDIY(bmPath);
                    break;
            }
        }

    }
}
