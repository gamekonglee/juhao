package bc.juhao.com.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import bc.juhao.com.R;


/**
 * @author: Jun
 * @date : 2017/1/21 13:46
 * @description : 选择方案类型
 */
public class SchemeTypeGvAdapter extends BaseAdapter {
    private List<String> mProgrammes;
    private Activity mContext;
    private Intent mIntent;
    private List<Boolean> mIsClick;

    public SchemeTypeGvAdapter(Activity context,List<String> programmes) {
        mContext=context;
        mProgrammes=programmes;
        mIsClick = new ArrayList<>();
        for (int i = 0; i < mProgrammes.size(); i++) {
            if (i == 0) {
                mIsClick.add(true);
            } else {
                mIsClick.add(false);
            }
        }

    }

    public void setIsClick(int poistion,Boolean isclick){
        for(int i=0;i<mProgrammes.size();i++){
            mIsClick.set(i,false);
        }
        mIsClick.set(poistion,isclick);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (null == mProgrammes)
            return 0;
        return mProgrammes.size();
    }

    @Override
    public String getItem(int position) {
        if (null == mProgrammes)
            return null;
        return mProgrammes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_scheme_type02, null);

            holder = new ViewHolder();
            holder.name_tv = (TextView) convertView.findViewById(R.id.name_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String name=mProgrammes.get(position);
        holder.name_tv.setText(name);
        holder.name_tv.setBackgroundResource(mIsClick.get(position)==false?R.color.EC:R.color.green);
        return convertView;
    }

    class ViewHolder {
        TextView name_tv;
    }




}
