package bc.juhao.com.controller;

import android.os.Message;

import bc.juhao.com.ui.activity.newbuy.CashierActivity;

/**
 * author: cjt
 * date:  2019-12-17$
 * ClassName: CashierController$
 * Description:
 */
public class CashierController extends BaseController {

    CashierActivity mView;

    public CashierController(CashierActivity cashierActivity){
        mView = cashierActivity;
    }

    @Override
    protected void handleMessage(int action, Object[] values) {

    }

    @Override
    protected void myHandleMessage(Message msg) {

    }
}
