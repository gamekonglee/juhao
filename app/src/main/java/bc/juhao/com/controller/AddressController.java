package bc.juhao.com.controller;

import android.content.Intent;
import android.os.Message;

import bc.juhao.com.ui.activity.newbuy.AddressActivity;

/**
 * author: cjt
 * date:  2019-12-05$
 * ClassName: AddressController$
 * Description:
 */
public class AddressController extends BaseController  {
    private AddressActivity mView;
    Intent intent;

    public AddressController(AddressActivity addressActivity) {
        mView = addressActivity;
        intent = mView.getIntent();
    }

    @Override
    protected void handleMessage(int action, Object[] values) {

    }

    @Override
    protected void myHandleMessage(Message msg) {

    }

    public void setResult() {
        intent.putExtra("data", "data");
        mView.setResult(1, intent);
        mView.finish();
    }
}
