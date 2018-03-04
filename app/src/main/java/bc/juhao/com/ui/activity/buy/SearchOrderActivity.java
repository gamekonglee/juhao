package bc.juhao.com.ui.activity.buy;

import android.content.Intent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import bc.juhao.com.R;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.ui.fragment.Order02Fragment;
import bocang.view.BaseActivity;

/**
 * @author: Jun
 * @date : 2017/6/28 11:17
 * @description :
 */
public class SearchOrderActivity extends BaseActivity {
    private Order02Fragment mFragment;
    private List<String> contentList = new ArrayList<String>(); //内容链表
    private   String orderSn;
    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {

    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_search_order);
        contentList.add("-1");
        mFragment=new Order02Fragment().newInstance(contentList, 0);
        getSupportFragmentManager().beginTransaction().replace(R.id.container,
                mFragment).commitAllowingStateLoss();
        mFragment.mOrdersn= orderSn;
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        orderSn=intent.getStringExtra(Constance.SEARCH_ORDER);

    }

    @Override
    protected void onViewClick(View v) {

    }
}
