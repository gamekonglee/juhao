package bc.juhao.com.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

import java.util.Set;

import bc.juhao.com.R;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.controller.SettingController;
import bc.juhao.com.ui.activity.user.LoginActivity;
import bc.juhao.com.ui.activity.user.PerfectMydataActivity;
import bc.juhao.com.ui.view.ShowDialog;
import bc.juhao.com.utils.MyShare;
import bc.juhao.com.utils.UIUtils;
import bocang.view.BaseActivity;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * @author: Jun
 * @date : 2017/2/6 9:43
 * @description :
 */
public class SettingActivity extends BaseActivity {
    private SettingController mController;
    private RelativeLayout cache_rl;
    private Button outlogin_bt;
    private TextView tv_tuisong;
    private boolean isOpen;
    private TextView tv_edit_info;

    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {
        mController = new SettingController(this);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_setting);
        //沉浸式状态栏
        setColor(this, Color.WHITE);
        outlogin_bt = getViewAndClick(R.id.outlogin_bt);
        cache_rl = getViewAndClick(R.id.cache_rl);
        tv_tuisong = getViewAndClick(R.id.tv_tuisong);
        TextView version_tv=findViewById(R.id.version_tv);
        tv_edit_info = getViewAndClick(R.id.tv_edit_info);
        version_tv.setText(UIUtils.getVerName(this));
        isOpen = MyShare.get(this).getBoolean(Constance.push_control);
        Drawable drawable;
        if(isOpen){
            drawable=getResources().getDrawable(R.mipmap.my_xx_sel);
        }else {
            drawable=getResources().getDrawable(R.mipmap.my_xx_nor);
        }
        drawable.setBounds(0,0,UIUtils.dip2PX(25),UIUtils.dip2PX(13));
        tv_tuisong.setCompoundDrawables(null,null,drawable,null);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.outlogin_bt:
                ShowDialog mDialog=new ShowDialog();
                mDialog.show(this, "提示", "确定退出登录?", new ShowDialog.OnBottomClickListener() {
                    @Override
                    public void positive() {
                        EMClient.getInstance().logout(true, new EMCallBack() {

                            @Override
                            public void onSuccess() {
                                // TODO Auto-generated method stub
                                Log.e("520it", "Setting注销登录");
                            }

                            @Override
                            public void onProgress(int progress, String status) {
                                // TODO Auto-generated method stub

                            }

                            @Override
                            public void onError(int code, String message) {
                                // TODO Auto-generated method stub
                                Log.e("520it", "Setting注销失败:"+message);
                            }
                        });

                        JPushInterface.setAlias(SettingActivity.this, "no", new TagAliasCallback() {
                            @Override
                            public void gotResult(int responseCode, String s, Set<String> set) {
                                Log.e("520it", "结果:" + s + ":" + responseCode);
                            }
                        });

                        MyShare.get(SettingActivity.this).putString(Constance.TOKEN, "");
                        MyShare.get(SettingActivity.this).putString(Constance.USERNAME, "");
//                        Intent logoutIntent = new Intent(SettingActivity.this, LoginActivity.class);
//                        logoutIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(logoutIntent);
                        MainActivity.mFragmentPosition=0;
                        finish();


                    }

                    @Override
                    public void negtive() {

                    }
                });
                break;
            case R.id.cache_rl:
                mController.clearCache();
                break;
            case R.id.tv_tuisong:
                isOpen=!isOpen;
                MyShare.get(this).putBoolean(Constance.push_control,isOpen);
                Drawable drawable;
                if(isOpen){
                    drawable=getResources().getDrawable(R.mipmap.my_xx_sel);
                }else {
                    drawable=getResources().getDrawable(R.mipmap.my_xx_nor);
                }
                drawable.setBounds(0,0,UIUtils.dip2PX(25),UIUtils.dip2PX(13));
                tv_tuisong.setCompoundDrawables(null,null,drawable,null);
                break;
            case R.id.tv_edit_info:
                startActivity(new Intent(this, PerfectMydataActivity.class));
                break;
        }

    }
}
