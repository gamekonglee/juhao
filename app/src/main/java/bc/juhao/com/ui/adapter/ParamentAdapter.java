package bc.juhao.com.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import bc.juhao.com.R;
import bc.juhao.com.cons.Constance;

/**
 * @author: Jun
 * @date : 2017/2/14 15:07
 * @description :
 */
public class ParamentAdapter extends BaseAdapter {
    private com.alibaba.fastjson.JSONArray mParamentLists;
    private Context mContext;

    public ParamentAdapter(com.alibaba.fastjson.JSONArray paramentLists, Context context) {
        mParamentLists = paramentLists;
        mContext = context;
    }

    @Override
    public int getCount() {
        if (null == mParamentLists)
            return 0;
        return mParamentLists.size();
    }

    @Override
    public Object getItem(int position) {
        if (null == mParamentLists)
            return null;
        return mParamentLists.getJSONObject(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_parameter_product, null);

            holder = new ViewHolder();
            holder.name_tv = (TextView) convertView.findViewById(R.id.name_tv);
            holder.value_tv = (TextView) convertView.findViewById(R.id.value_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.name_tv.setText(mParamentLists.getJSONObject(position).getString(Constance.name)+":");
        if (mParamentLists.getJSONObject(position).getJSONArray(Constance.attrs).size() > 0) {
            holder.value_tv.setText(mParamentLists.getJSONObject(position).getJSONArray(Constance.attrs)
                    .getJSONObject(0).getString(Constance.attr_name));
        }
        return convertView;
    }

    class ViewHolder {
        TextView name_tv;
        TextView value_tv;

    }
}
