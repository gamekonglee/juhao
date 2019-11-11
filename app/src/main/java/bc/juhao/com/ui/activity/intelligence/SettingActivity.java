package bc.juhao.com.ui.activity.intelligence;

import android.view.View;

import com.aliyun.iot.aep.sdk.login.ILogoutCallback;
import com.aliyun.iot.aep.sdk.login.LoginBusiness;
import com.aliyun.iot.ilop.demo.DemoApplication;

import bc.juhao.com.R;
import bc.juhao.com.common.BaseActivity;
import bc.juhao.com.utils.UIUtils;
import bocang.utils.MyToast;

public class SettingActivity extends BaseActivity {
    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {

    }

    @Override
    protected void initView() {
    setContentView(R.layout.activity_iot_setting);
    View tv_logout=findViewById(R.id.tv_logout);
        tv_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UIUtils.showSingleWordDialog(SettingActivity.this, "确定要退出吗？", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        LoginBusiness.logout(new ILogoutCallback() {
                            @Override
                            public void onLogoutSuccess() {
                                MyToast.show(SettingActivity.this,"已成功退出账号");
                                DemoApplication.activityList.get(0).finish();
                                SettingActivity.this.finish();
//                                System.exit(0);
//                                Log.i(TAG,"登出成功");
                            }

                            @Override
                            public void onLogoutFailed(int code, String error) {
//                                Log.i(TAG,"登出失败");
                            }
                        });
                    }
                });
            }
        });
    }

    @Override
    protected void initData() {

    }
}
