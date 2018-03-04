package bc.juhao.com.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import bc.juhao.com.R;
import bc.juhao.com.cons.Constance;
import bocang.json.JSONArray;
import bocang.json.JSONObject;

/**
 * @author: Jun
 * @date : 2017/1/21 13:46
 * @description : 筛选2的选择
 */
public class FilterTypeAdapter extends BaseAdapter {
    private JSONArray mFilterGoodsLists;
    private Activity mContext;
    private Intent mIntent;

    public FilterTypeAdapter(Activity context) {
        mContext=context;
    }

    public void setData(JSONArray filterGoodsLists){
        mFilterGoodsLists=filterGoodsLists;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (null == mFilterGoodsLists)
            return 0;
        return mFilterGoodsLists.length();
    }

    @Override
    public JSONObject getItem(int position) {
        if (null == mFilterGoodsLists)
            return null;
        return mFilterGoodsLists.getJSONObject(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_gridview_filter_type, null);

            holder = new ViewHolder();
            holder.name_tv = (TextView) convertView.findViewById(R.id.name_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final JSONObject object = mFilterGoodsLists.getJSONObject(position);
        holder.name_tv.setText(object.getString(Constance.attr_value));
        return convertView;
    }

    class ViewHolder {
        TextView name_tv;
    }
}
