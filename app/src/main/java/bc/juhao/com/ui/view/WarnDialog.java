package bc.juhao.com.ui.view;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import bc.juhao.com.R;
import bocang.utils.HyUtil;

/**
 * 警告提示
 *
 * @author HeYan
 * @time 2016/6/6 11:46
 */
public class WarnDialog extends BaseDialog {

    private ImageView imgClose;
    private TextView txtContent;
    private Button btnConfirm;
    private String content, confirm;
    private boolean showCancel;

    public WarnDialog(Context context, String content) {
        this(context, content, null, false);
    }

    public WarnDialog(Context context, String content, String confirm, boolean showCancel) {
        this(context, content, confirm, showCancel, false, false);
    }

    public WarnDialog(Context context, String content, String confirm, boolean showCancel, boolean cancelable, boolean onTouchOutside) {
        super(context);
        this.content = content;
        this.confirm = confirm;
        this.showCancel = showCancel;
        setCancelable(cancelable);
        setCanceledOnTouchOutside(onTouchOutside);
    }

    @Override
    protected int initLayoutId() {
        return R.layout.dlg_warn;
    }

    @Override
    protected void initWindow() {
        windowDeploy(0.8f, WindowManager.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
    }

    @Override
    protected void initView() {
        imgClose = getViewAndClick(R.id.warn_imgClose);
        btnConfirm = getViewAndClick(R.id.warn_btnConfirm);
        txtContent = getView(R.id.warn_txtContent);
    }

    @Override
    protected void initData() {
        if (HyUtil.isNoEmpty(content)) txtContent.setText(content);
        if (HyUtil.isNoEmpty(confirm)) btnConfirm.setText(confirm);
        imgClose.setVisibility(showCancel ? View.VISIBLE : View.GONE);
    }


    @Override
    protected void onViewClick(View v) {
        this.dismiss();
        switch (v.getId()) {
            case R.id.warn_imgClose:
                if (getListener() != null) {
                    getListener().onDlgConfirm(this , -1);
                }
                break;
            case R.id.warn_btnConfirm:
                if (getListener() != null) {
                    getListener().onDlgConfirm(this , 0);
                }
                break;
            case R.id.warn_txtContent:
                if (getListener() != null) {
                    getListener().onDlgConfirm(this , 1);
                }
                break;
        }
    }
}
