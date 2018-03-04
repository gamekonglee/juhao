package bc.juhao.com.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import bc.juhao.com.R;
import bc.juhao.com.cons.Constance;
import bocang.json.JSONArray;
import bocang.json.JSONObject;

/**
 * @author: Jun
 * @date : 2017/1/21 13:46
 * @description : 方案列表产品
 */
public class MatchItemAdapter extends BaseAdapter {
    private JSONArray mGoods;
    private Activity mContext;
    private Intent mIntent;

    public MatchItemAdapter(Activity context, JSONArray goods) {
        mContext=context;
        mGoods=goods;
    }



    @Override
    public int getCount() {
        if (null == mGoods)
            return 0;
        return mGoods.length();
    }

    @Override
    public JSONObject getItem(int position) {
        if (null == mGoods)
            return null;
        return mGoods.getJSONObject(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_match_02, null);

            holder = new ViewHolder();
            holder.name_tv = (TextView) convertView.findViewById(R.id.name_tv);
            holder.price_tv = (TextView) convertView.findViewById(R.id.price_tv);
            holder.goods_iv = (ImageView) convertView.findViewById(R.id.goods_iv);
            holder.good_ll = (LinearLayout) convertView.findViewById(R.id.good_ll);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        try{
            String name=mGoods.getJSONObject(position).getString(Constance.name);
            String price=mGoods.getJSONObject(position).getString(Constance.price);
            holder.name_tv.setText(name);
            holder.price_tv.setText("￥"+price);
            JSONObject imageObject= mGoods.getJSONObject(position). getJSONObject(Constance.default_photo);
            String path=imageObject.getString(Constance.large);
            ImageLoader.getInstance().displayImage(path, holder.goods_iv);
        }catch (Exception e){
            e.printStackTrace();
        }


        return convertView;
    }

    class ViewHolder {
        TextView name_tv;
        ImageView goods_iv;
        LinearLayout good_ll;
        TextView price_tv;
    }



}
