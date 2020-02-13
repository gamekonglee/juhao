package bc.juhao.com.controller;

import android.os.Message;
import android.util.Log;

import bc.juhao.com.ui.activity.newbuy.EditAddressActivity;

/**
 * author: cjt
 * date:  2019-12-05$
 * ClassName: AddressController$
 * Description:
 */
public class EditAddressController extends BaseController  {
    private EditAddressActivity mView;

    public EditAddressController(EditAddressActivity editAddressActivity) {
        mView = editAddressActivity;
    }

    @Override
    protected void handleMessage(int action, Object[] values) {

    }

    @Override
    protected void myHandleMessage(Message msg) {

    }

    public void commit(String s, String s1, String s2, String s3, boolean flag_default) {
        Log.d(TAG, "commit: "+flag_default);
    }
}
