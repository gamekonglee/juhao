package bc.juhao.com.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import bc.juhao.com.R;
import bc.juhao.com.bean.AttrList;
import bc.juhao.com.cons.Constance;
import bocang.json.JSONArray;
import bocang.json.JSONObject;

/**
 * @author: Jun
 * @date : 2017/1/21 13:46
 * @description : 筛选2
 */
public class FilterGoodsAdapter extends BaseAdapter {
    private JSONArray mFilterGoodsLists;
    private Activity mContext;
    private Intent mIntent;
    public ArrayList<AttrList> mAttrList=new ArrayList<>();

    public FilterGoodsAdapter( Activity context) {
        mContext=context;
    }

    public void setData(JSONArray filterGoodsLists){
        mFilterGoodsLists=filterGoodsLists;
        for(int i=0;i<mFilterGoodsLists.length();i++){
            AttrList attrList1=new AttrList();
            attrList1.setId(0);
            attrList1.setAttr_value("");
            mAttrList.add(attrList1);
        }
        notifyDataSetChanged();
    }


    public void setAttrList(AttrList attr,int poistion){
        mAttrList.set(poistion,attr);
        notifyDataSetChanged();
    }


    public void setClearAttrList(){
        mAttrList=new ArrayList<>();
        for(int i=0;i<mFilterGoodsLists.length();i++){
            AttrList attrList1=new AttrList();
            attrList1.setId(0);
            attrList1.setAttr_value("");
            mAttrList.add(attrList1);
        }
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
            convertView = View.inflate(mContext, R.layout.item_gridview_fm_scene, null);

            holder = new ViewHolder();
            holder.name_tv = (TextView) convertView.findViewById(R.id.name_tv);
            holder.filter_name_tv = (TextView) convertView.findViewById(R.id.filter_name_tv);
            holder.filter_rl= (RelativeLayout) convertView.findViewById(R.id.filter_rl);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final JSONObject object = mFilterGoodsLists.getJSONObject(position);
        final String name=object.getString(Constance.filter_attr_name);
        holder.name_tv.setText(name);
        holder.filter_name_tv.setText(mAttrList.get(position).getAttr_value());
//        holder.filter_rl.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                JSONArray attrArray = object.getJSONArray(Constance.attr_list);
//                if(AppUtils.isEmpty(attrArray)) return;
//                mIntent =new Intent(mContext,FilterTypeActivity.class);
//                mIntent.putExtra(Constance.attr_list, attrArray);
//                mIntent.putExtra(Constance.filter_attr_name, name);
//                mContext.startActivityForResult(mIntent, Constance.FROMFILTER);
//            }
//        });
        return convertView;
    }

    class ViewHolder {
        TextView name_tv;
        TextView filter_name_tv;
        RelativeLayout filter_rl;
    }
}
