package bc.juhao.com.ui.view;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import bc.juhao.com.R;
import bc.juhao.com.common.BaseDialog;


/**
 * Copyright (C) 2016
 * <p/>
 * filename :
 * action : 网络耗时加载框
 *
 * @author :  Jun
 * @version : 7.1
 * @date : 2016-11-10
 * modify :
 */

public class LoadingDialog extends BaseDialog {

    private TextView txtLoadMsg;

    private String loadMsg;

    public LoadingDialog(Context context) {
        this(context, null);
    }

    public LoadingDialog(Context context, String loadMsg) {
        super(context);
        this.loadMsg = loadMsg;
    }

    @Override
    protected int initLayoutId() {
        return R.layout.dlg_loading;
    }
    private Window window = null;
    @Override
    protected void initWindow() {
                windowDeploy(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
    }

    @Override
    protected void initView() {
        txtLoadMsg = getView(R.id.loading_txtLoadMsg);
    }

    @Override
    protected void initData() {
        if (loadMsg != null)
            txtLoadMsg.setText(loadMsg);
    }

    @Override
    protected void onViewClick(View v) {

    }

    @Override
    public void show() {
        if (txtLoadMsg != null) {
            if (loadMsg != null) {
                txtLoadMsg.setText(loadMsg);
                loadMsg = null;
            }else{
                txtLoadMsg.setText(R.string.loading);
            }
        }
        super.show();
    }

    public void setLoadMsg(String loadMsg) {
        this.loadMsg = loadMsg;
    }

}
