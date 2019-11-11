package bc.juhao.com.ui.view.dialog;

import android.content.Context;

/**
 * Created by gamekonglee on 2018/4/3.
 */

public class ShowSureDialog {
    private SureDialog selfDialog ;
    public ShowSureDialog() {
    }
    public void show(final Context ctx, String title, String message, final OnBottomClickListener onBottomClickListener){
        selfDialog = new SureDialog(ctx);
        selfDialog.setTitle(title);
        selfDialog.setMessage(message);
        selfDialog.setYesOnclickListener("确定", new SureDialog.onYesOnclickListener() {
            @Override
            public void onYesClick() {
                if (onBottomClickListener != null) {
                    onBottomClickListener.positive();
                }
                selfDialog.dismiss();
            }
        });
        selfDialog.show();
    }

    public interface OnBottomClickListener{
        void positive();
    }
}
