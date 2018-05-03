package bc.juhao.com.ui.activity.programme;

import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import bc.juhao.com.R;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.controller.programme.SelectSceneController;
import bc.juhao.com.ui.activity.IssueApplication;
import bocang.view.BaseActivity;

/**
 * @author: Jun
 * @date : 2017/2/18 11:55
 * @description :
 */
public class SelectSceneActivity extends BaseActivity {
    private SelectSceneController mController;
    private TextView tv_album;
    public TextView select_num_tv;
    private Intent mIntent;
    private RelativeLayout select_rl;

    @Override
    protected void InitDataView() {
        select_num_tv.setText(IssueApplication.mSelectScreens.length()+"");
    }

    @Override
    protected void initController() {
        mController = new SelectSceneController(this);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_select_scene);
        tv_album = getViewAndClick(R.id.tv_album);
        select_num_tv = (TextView)findViewById(R.id.select_num_tv);
        select_rl = getViewAndClick(R.id.select_rl);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.tv_album:
                mController.goPhoto();
                break;
            case R.id.select_rl:
                mIntent = new Intent();
//                ArrayList<String> data=new ArrayList<>();
//                for(int i=0;i>IssueApplication.mSelectScreens.length();i++){
//                    data.add(IssueApplication.mSelectScreens.getJSONObject(i).getJSONObject(Constance.scene).getString(Constance.original_img));
//                }
//                mIntent.putStringArrayListExtra(Constance.SCENE,data);
                setResult(Constance.FROMDIY02, mIntent);//告诉原来的Activity 将数据传递给它
                finish();//一定要调用该方法 关闭新的AC 此时 老是AC才能获取到Itent里面的值
                break;

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mController.onBackPressed();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mController.ActivityResult(requestCode, resultCode, data);
    }
}
