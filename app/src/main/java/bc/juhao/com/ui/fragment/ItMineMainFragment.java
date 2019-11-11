package bc.juhao.com.ui.fragment;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aliyun.iot.aep.component.router.Router;

import bc.juhao.com.R;
import bc.juhao.com.common.BaseFragment;
import bc.juhao.com.ui.activity.intelligence.AboutActivity;
import bc.juhao.com.ui.activity.intelligence.DeviceShareActivity;
import bc.juhao.com.ui.activity.intelligence.NoticeActivity;
import bc.juhao.com.ui.activity.intelligence.SettingActivity;

/**
 * Created by gamekonglee on 2018/7/7.
 */

public class ItMineMainFragment extends BaseFragment implements View.OnClickListener {
    @Override
    protected void initController() {

    }

    @Override
    protected void initViewData() {

    }

    @Override
    protected void initView() {
        TextView tv_componet=getView().findViewById(R.id.tv_component);
        TextView tv_notice=getView().findViewById(R.id.tv_notice);
        TextView tv_share=getView().findViewById(R.id.tv_share);
        TextView tv_3th=getView().findViewById(R.id.tv_3th);
        TextView tv_suggestion=getView().findViewById(R.id.tv_suggestion);
        TextView tv_setting=getView().findViewById(R.id.tv_setting);
        TextView tv_about=getView().findViewById(R.id.tv_about);
        View iv_scale=getView().findViewById(R.id.iv_scale);
        tv_componet.setOnClickListener(this);
        tv_notice.setOnClickListener(this);
        tv_share.setOnClickListener(this);
        tv_3th.setOnClickListener(this);
        tv_suggestion.setOnClickListener(this);
        tv_setting.setOnClickListener(this);
        tv_about.setOnClickListener(this);
        iv_scale.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_it_main_mine,null);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_scale:
                if(Build.VERSION.SDK_INT>=23){
                    checkPermission();
                }else {
                    camear();
                }
                break;
            case R.id.tv_suggestion:
                String code = "link://router/feedback";
                Bundle bundle = new Bundle();
//                bundle.putString("sceneType","ilop"); // 传入插件参数，没有参数则不需要这一行
                Router.getInstance().toUrlForResult(getActivity(), code, 1, bundle);
                break;
            case R.id.tv_setting:
                startActivity(new Intent(getActivity(), SettingActivity.class));
                break;
            case R.id.tv_about:
                startActivity(new Intent(getActivity(), AboutActivity.class));
                break;
            case R.id.tv_notice:
                startActivity(new Intent(getActivity(), NoticeActivity.class));
                break;
            case R.id.tv_share:
                startActivity(new Intent(getActivity(), DeviceShareActivity.class));
                break;
        }

    }
    public void checkPermission(){
        if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            //权限发生了改变 true  //  false 小米
            if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),Manifest.permission.CAMERA)){

                new AlertDialog.Builder(getActivity()).setTitle("title")
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 请求授权
                                ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.CAMERA},1);

                            }
                        }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create().show();



            }else {
                ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.CAMERA},1);

            }

        }else{

            camear();

        }

    }

    private void camear() {
        Router.getInstance().toUrl(getActivity(), "page/scan");
    }

    /**
     *
     * @param requestCode
     * @param permissions 请求的权限
     * @param grantResults 请求权限返回的结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 1){
            // camear 权限回调

            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                // 表示用户授权
//                Toast.makeText(getActivity(), " user Permission" , Toast.LENGTH_SHORT).show();

                camear();


            } else {

                //用户拒绝权限
//                Toast.makeText(getActivity(), " no Permission" , Toast.LENGTH_SHORT).show();

            }



        }
    }
}
