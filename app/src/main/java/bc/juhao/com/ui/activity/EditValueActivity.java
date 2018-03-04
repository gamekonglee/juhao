package bc.juhao.com.ui.activity;

import android.content.Intent;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import bc.juhao.com.R;
import bc.juhao.com.cons.Constance;
import bocang.view.BaseActivity;

/**
 * @author: Jun
 * @date : 2017/1/21 15:54
 * @description :编辑信息
 */
public class EditValueActivity extends BaseActivity implements View.OnClickListener {
    private TextView title_tv;
    private RelativeLayout save_rl;
    private EditText et_value;
    private String mTitle = "";
    private int mCode;
    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {

    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_editvalue);
        title_tv = (TextView) findViewById(R.id.title_tv);
        save_rl = (RelativeLayout) findViewById(R.id.save_rl);
        et_value = (EditText) findViewById(R.id.et_value);
        title_tv.setText(mTitle);
        selectEditType();
        save_rl = (RelativeLayout) findViewById(R.id.save_rl);
        save_rl.setOnClickListener(this);
    }

    /**
     * 设置编辑类型
     */
    private void selectEditType() {
        switch (mCode) {
            case 001:
                et_value.setHint("请写下你的真实姓名");
                break;
            case 004:
                et_value.setHint("请写下你的手机号");
                et_value.setInputType(InputType.TYPE_CLASS_NUMBER);
                break;
            case 005:
                et_value.setHint("请写下你的固定电话");
                break;
            case 006:
                et_value.setHint("请写下你的电子邮箱");
                break;
            case 007:
                et_value.setHint("请写下你的订单备注");
                break;
        }
    }


    @Override
    protected void initData() {
        Intent intent = getIntent();
        mTitle = intent.getStringExtra(Constance.TITLE);
        mCode = intent.getIntExtra(Constance.CODE, 0);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save_rl:
                Intent intent = new Intent();
                intent.putExtra(Constance.VALUE, et_value.getText().toString());
                setResult(mCode, intent);
                finish();
                break;
        }
    }

    @Override
    protected void onViewClick(View v) {

    }

}
