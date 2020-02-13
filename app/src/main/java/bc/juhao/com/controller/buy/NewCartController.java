package bc.juhao.com.controller.buy;

import android.os.Message;
import android.view.View;
import android.widget.TextView;

import bc.juhao.com.R;
import bc.juhao.com.controller.BaseController;
import bc.juhao.com.ui.fragment.NewCartFragment;
import bc.juhao.com.ui.view.dialog.CartDialog;

import static bocang.utils.UIUtils.getResources;

/**
 * author: cjt
 * date:  2019-12-11$
 * ClassName: NewCartController$
 * Description:
 */
public class NewCartController extends BaseController {

    private NewCartFragment mView;

    public NewCartController(NewCartFragment v) {
        mView = v;
    }

    @Override
    protected void handleMessage(int action, Object[] values) {

    }

    @Override
    protected void myHandleMessage(Message msg) {

    }

    /**
     *
     * @param viewById
     * @param textView
     * @param num 当前数量
     * @param mul 倍数 同时为最小值
     * @param max 最大值
     */
    public void resetNumBind(View viewById, final TextView textView, final int num, final int mul, final int max) {
        viewById.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                CartDialog cartDialog = new CartDialog(mView.getContext(), getResources().getString(R.string.cart_delete), num, mul, max, new CartDialog.OnResultListener() {
                    @Override
                    public void onConfirm(int num) {
                        textView.setText(num+"");
                    }

                    @Override
                    public void onCancel() {
                    }
                });
                cartDialog.show();
            }
        });
    }
}
