package bc.juhao.com.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import bc.juhao.com.R;

/**
 * @author: Jun
 * @date : 2017/2/14 15:07
 * @description :
 */
public class ParamentAdapter02 extends BaseAdapter {
    private List<String> mParamentLists;
    private Context mContext;
    private int mShowType;

    public ParamentAdapter02(List<String> paramentLists, Context context,int showType) {
        mParamentLists = paramentLists;
        mContext = context;
        mShowType=showType;
    }

    @Override
    public int getCount() {
        if (null == mParamentLists)
            return 0;
        return mParamentLists.size();
    }

    @Override
    public String getItem(int position) {
        if (null == mParamentLists)
            return null;

        return mParamentLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            if(mShowType==0){
                convertView = View.inflate(mContext, R.layout.item_parameter_product, null);
            }else{
                convertView = View.inflate(mContext, R.layout.item_parameter_product_diy, null);
            }


            holder = new ViewHolder();
            holder.name_tv = (TextView) convertView.findViewById(R.id.name_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.name_tv.setText(mParamentLists.get(position));
        return convertView;
    }

    class ViewHolder {
        TextView name_tv;

    }
}
