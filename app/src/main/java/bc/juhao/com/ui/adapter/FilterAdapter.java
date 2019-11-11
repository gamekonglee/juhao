package bc.juhao.com.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import bc.juhao.com.R;
import bc.juhao.com.cons.Constance;
import bocang.json.JSONArray;
import bocang.json.JSONObject;

/**
 * @author: Jun
 * @date : 2017/1/21 13:46
 * @description :
 */
public class FilterAdapter extends BaseAdapter {

    private JSONArray mClassifyGoodsLists;
    private Context mContext;
    public int mCurrTabIndex=-1;


    public FilterAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setData(JSONArray classifyGoodsLists){
        mClassifyGoodsLists=classifyGoodsLists;

        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (null == mClassifyGoodsLists)
            return 0;
        return mClassifyGoodsLists.length();
    }

    @Override
    public JSONObject getItem(int position) {
        if (null == mClassifyGoodsLists)
            return null;
        return mClassifyGoodsLists.getJSONObject(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_filter, null);

            holder = new ViewHolder();
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.gv_item = convertView.findViewById(R.id.gv_item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv_name.setText(mClassifyGoodsLists.getJSONObject(position).getString("name"));


//        holder.gv_item.setAdapter( mClassifyGoodsLists.getJSONObject(position).getJSONArray(Constance.categories));
        return convertView;
    }

    class ViewHolder {
        TextView tv_name;
        GridView gv_item;
    }
}
