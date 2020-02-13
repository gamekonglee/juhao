package bc.juhao.com.ui.activity.newbuy;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import bc.juhao.com.R;
import bc.juhao.com.controller.AddressController;
import bocang.view.BaseActivity;

/**
 * author: cjt
 * date:  2019-12-05$
 * ClassName: AddressActivity$
 * Description:
 */
public class AddressActivity extends BaseActivity {

    private AddressController mController;
    private ListView lv_adress;
    private TextView tv_add_address;

    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {
        mController = new AddressController(this);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.editaddress);
        setColor(this, Color.WHITE);
        tv_add_address = getViewAndClick(R.id.tv_add_address);
        lv_adress = findViewById(R.id.lv_address);
        lv_adress.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return 6;
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
            public View getView(int i, View convertView, ViewGroup viewGroup) {
                ViewHolder holder;
                View view;
                if (convertView == null) {
                    view = View.inflate(AddressActivity.this, R.layout.item_address, null);
                    holder = new ViewHolder();
                    holder.iv_tick = view.findViewById(R.id.iv_tick);
                    holder.id_iv_icon_edit = view.findViewById(R.id.id_iv_icon_edit);
                    view.setTag(holder);
                }else {
                    view = convertView;
                    holder = (ViewHolder) view.getTag();
                }

                holder.iv_tick.setVisibility(View.GONE);
                if (i == checkitem_position){
                    holder.iv_tick.setVisibility(View.VISIBLE);
                }
                holder.id_iv_icon_edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(AddressActivity.this, EditAddressActivity.class));
                    }
                });
                return view;
            }
            class ViewHolder {
                ImageView iv_tick;
                ImageView id_iv_icon_edit;
            }
        });
        lv_adress.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                checkitem_position = i;
                ((BaseAdapter)lv_adress.getAdapter()).notifyDataSetChanged();
                mController.setResult();
            }
        });
    }

    int checkitem_position = 0;

    @Override
    protected void initData() {

    }

    @Override
    protected void onViewClick(View v) {
        switch (v.getId()){
            case R.id.tv_add_address:
                startActivity(new Intent(AddressActivity.this, EditAddressActivity.class));
                break;
        }
    }
}
