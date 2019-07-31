package bc.juhao.com.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import bc.juhao.com.R;
import bc.juhao.com.bean.Classify;


/**
 * Gridview的适配器
 */
public class GridviewAdapter extends BaseAdapter {

    List<String> list = new ArrayList<>();//用来存放选中属性的集合
    private List<Classify> data;

    Context context;

    public GridviewAdapter(List<Classify> data, Context context) {
        this.data = data;
        this.context=context;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder holder;
        if (convertView == null) {
            holder = new MyViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_scheme_type02, null);
            //利用view对象，找到布局中的组件
            holder.mGridContent = (TextView) convertView.findViewById(R.id.name_tv);
            convertView.setTag(holder);
        } else {
            holder = (MyViewHolder) convertView.getTag();
        }
        if (data != null && data.size() > 0) {
            holder.mGridContent.setText(data.get(position).getStr());
            if (data.get(position).isChecked()) {
                list.add(data.get(position) + "");
            } else {
                list.remove(data.get(position) + "");
            }

            holder.mGridContent.setBackgroundResource(data.get(position).isChecked()==false? R.drawable.shape_recode_btn_active: R.drawable.shape_recode_btn_pressed);
        }
        return convertView;
    }

    class MyViewHolder {
        public TextView mGridContent;
    }


    public   List  getSaveList(){

        return list;
    }
}
