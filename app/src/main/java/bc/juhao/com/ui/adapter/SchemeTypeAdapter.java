package bc.juhao.com.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import java.util.List;

import bc.juhao.com.R;
import bc.juhao.com.bean.Programme;
import bc.juhao.com.listener.ISchemeChooseListener;

/**
 * @author: Jun
 * @date : 2017/1/21 13:46
 * @description : 选择方案类型
 */
public class SchemeTypeAdapter extends BaseAdapter {
    private List<Programme> mProgrammes;
    private Activity mContext;
    private Intent mIntent;
    private ISchemeChooseListener mListener;
    private String mStyle="";
    private String mSplace="";

    public void setListener(ISchemeChooseListener listener) {
        mListener = listener;
    }

    public SchemeTypeAdapter(Activity context) {
        mContext=context;
    }

    public void setData(List<Programme> programmes){
        mProgrammes=programmes;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (null == mProgrammes)
            return 0;
        return mProgrammes.size();
    }

    @Override
    public Programme getItem(int position) {
        if (null == mProgrammes)
            return null;
        return mProgrammes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView( final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_scheme_type, null);

            holder = new ViewHolder();
            holder.name_tv = (TextView) convertView.findViewById(R.id.type_name_tv);
            holder.type_gv = (GridView) convertView.findViewById(R.id.type_gv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String name=mProgrammes.get(position).getAttr_name();
        holder.name_tv.setText(name);
        final SchemeTypeGvAdapter schemeTypeGvAdapter=new SchemeTypeGvAdapter(mContext,mProgrammes.get(position).getAttrVal());
        holder.type_gv.setAdapter(schemeTypeGvAdapter);
        holder.type_gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position2, long id) {
                schemeTypeGvAdapter.setIsClick(position2,true);
                if(position==0){
                    mStyle=mProgrammes.get(position).getAttrVal().get(position2);
                }else{
                    mSplace=mProgrammes.get(position).getAttrVal().get(position2);
                }
                mListener.onSchemeChanged(mStyle,mSplace);
            }
        });
        return convertView;
    }

    class ViewHolder {
        TextView name_tv;
        GridView type_gv;
    }




}
