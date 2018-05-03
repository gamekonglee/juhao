package bc.juhao.com.ui.activity.programme;

import android.os.Bundle;

import bc.juhao.com.R;
import bc.juhao.com.common.BaseActivity;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.ui.fragment.CartFragment;
import bc.juhao.com.ui.fragment.ProgrammeFragment;

/**
 * Created by bocang on 18-2-8.
 */

public class ProgrammerActivity extends BaseActivity {

    private ProgrammeFragment cartFragment;

    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {

    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_programmer);
        setColor(this,getResources().getColor(R.color.green));
        if(cartFragment ==null) cartFragment =new ProgrammeFragment();
//        Bundle bundle = new Bundle();
//        bundle.putBoolean(Constance.product, true);
//        cartFragment.setArguments(bundle);

    }

    @Override
    protected void initData() {

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(!cartFragment.isAdded())
            getSupportFragmentManager().beginTransaction().replace(R.id.container, cartFragment).commitAllowingStateLoss();
    }
}
