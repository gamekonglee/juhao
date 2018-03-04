package bc.juhao.com.ui.activity.buy;

import android.content.Intent;
import android.view.View;

import bc.juhao.com.R;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.listener.ISearchListener;
import bc.juhao.com.ui.view.search.Search_View;
import bocang.view.BaseActivity;

/**
 * @author: Jun
 * @date : 2017/6/28 11:17
 * @description :
 */
public class SearchActivity extends BaseActivity {
    private Search_View search_layout;

    @Override
    protected void InitDataView() {
    }

    @Override
    protected void initController() {

    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_search);
        search_layout = (Search_View) findViewById(R.id.search_layout);
        search_layout.setListener(new ISearchListener() {
            @Override
            public void onTouchViewListener(String result) {
                Intent intent = new Intent(SearchActivity.this, SearchOrderActivity.class);
                intent.putExtra(Constance.SEARCH_ORDER, result);
                SearchActivity.this.startActivity(intent);
                SearchActivity.this.finish();
            }
        });
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void onViewClick(View v) {

    }
}
