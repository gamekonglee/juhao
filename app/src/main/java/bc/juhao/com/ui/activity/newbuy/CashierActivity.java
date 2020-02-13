package bc.juhao.com.ui.activity.newbuy;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.TextView;

import bc.juhao.com.R;
import bc.juhao.com.controller.CashierController;
import bocang.view.BaseActivity;

/**
 * author: cjt
 * date:  2019-12-17$
 * ClassName: CashierActivity$
 * Description:
 */
public class CashierActivity extends BaseActivity {

    private CashierController mController;
    private TextView tv_prize;

    @Override
    protected void InitDataView() {
        SpannableString spannableString = new SpannableString(tv_prize.getText());
        spannableString.setSpan(new RelativeSizeSpan(1.3f), 0, tv_prize.getText().length()-3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_prize.setText(spannableString);
    }

    @Override
    protected void initController() {
        mController = new CashierController(this);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.cashier);
        tv_prize = findViewById(R.id.tv_prize);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onViewClick(View v) {

    }
}
