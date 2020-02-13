package bc.juhao.com.ui.activity.newbuy;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import bc.juhao.com.R;
import bc.juhao.com.controller.newbuy.NewEditOrderController;
import bocang.view.BaseActivity;
import hxp.view.ListViewForScrollView;

import static android.graphics.Color.WHITE;


/**
 * author: cjt
 * date:  2019-12-16$
 * ClassName: NewEditOrderActivity$
 * Description:
 */
public class NewEditOrderActivity extends BaseActivity {

    private NewEditOrderController mController;
    private LinearLayout ll_address, ll_not_address;
    private ListViewForScrollView lv_goods;
    private TextView tv_commit;

    @Override
    protected void InitDataView() {
        lv_goods.setAdapter(lv_adapter);
    }

    @Override
    protected void initController() {
        mController = new NewEditOrderController(NewEditOrderActivity.this);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.neweditorder);
        setColor(this, Color.WHITE);
        lv_goods = findViewById(R.id.lv_goods);
        ll_address = findViewById(R.id.ll_address);
        ll_not_address = findViewById(R.id.ll_not_address);

        tv_commit = getViewAndClick(R.id.tv_commit);
        getViewAndClick(R.id.rl_address);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onViewClick(View v) {
        switch (v.getId()){
            case R.id.rl_address:
                startActivityForResult(new Intent(NewEditOrderActivity.this, AddressActivity.class), 1);
                break;
            case R.id.tv_commit:
                Toast.makeText(this, "111", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public void setAddress(Intent data) {
        ll_not_address.setVisibility(View.GONE);
        ll_address.setVisibility(View.VISIBLE);
        tv_commit.setTextColor(WHITE);
        tv_commit.setBackground(getResources().getDrawable(R.drawable.bg_editorder_btn_orange));
    }

    private BaseAdapter lv_adapter = new BaseAdapter() {
        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            View v = View.inflate(NewEditOrderActivity.this, R.layout.item_order_goods, null);
            ListViewForScrollView lv_items = v.findViewById(R.id.lv_items);
            lv_items.setAdapter(new ItemsAdapter(i));
            return v;
        }
    };

    private class ItemsAdapter extends BaseAdapter{

        int i;
        ItemsAdapter(int i){
            this.i = i;
        };

        @Override
        public int getCount() {
            return i+1;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View v = View.inflate(NewEditOrderActivity.this, R.layout.item_order_goods_item, null);
            return v;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 1){
            setAddress(data);
        }
    }
}
