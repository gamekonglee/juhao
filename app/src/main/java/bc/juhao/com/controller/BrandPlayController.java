package bc.juhao.com.controller;

import android.os.Message;

import bc.juhao.com.ui.activity.BrandPlayActivity;


/**
 * @author: Jun
 * @date : 2017/5/9 10:30
 * @description :公司视频
 */
public class BrandPlayController extends BaseController {
    private BrandPlayActivity mView;



    public BrandPlayController(BrandPlayActivity v){
        mView=v;
        initView();
        initViewData();

    }




    private void initViewData() {
    }

    private void initView() {

    }

    @Override
    protected void handleMessage(int action, Object[] values) {

    }

    @Override
    protected void myHandleMessage(Message msg) {

    }
}
