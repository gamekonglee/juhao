package bc.juhao.com.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import bc.juhao.com.R;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.cons.NetWorkConst;
import bc.juhao.com.utils.ConvertUtil;
import bocang.json.JSONArray;
import bocang.json.JSONObject;

/**
 * @author: Jun
 * @date : 2017/1/9 15:22
 * @description :
 */
public class ProAdapter extends BaseAdapter {
    private JSONArray mGoodses;
    private Context mContext;
    public  int mScreenWidth;

    public ProAdapter(Context context,int screenWidth) {
        this.mContext=context;
        this.mScreenWidth=screenWidth;
    }

    public void setDatas(JSONArray goodses){
        mGoodses=goodses;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (null == mGoodses)
            return 0;
        return mGoodses.length();
    }

    @Override
    public JSONObject getItem(int position) {
        if (null == mGoodses)
            return null;
        return mGoodses.getJSONObject(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_gridview_fm_product, null);

            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
            holder.textView = (TextView) convertView.findViewById(R.id.textView);
            RelativeLayout.LayoutParams lLp = (RelativeLayout.LayoutParams) holder.imageView.getLayoutParams();
            float h = (mScreenWidth - ConvertUtil.dp2px(mContext, 45.8f)) / 2;
            lLp.height = (int) h;
            holder.imageView.setLayoutParams(lLp);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.textView.setText(mGoodses.getJSONObject(position).getString(Constance.name));

        ImageLoader.getInstance().displayImage(NetWorkConst.PRODUCT_URL + mGoodses.getJSONObject(position).getString(Constance.img_url)
                + "!400X400.png", holder.imageView);

        return convertView;
    }

    class ViewHolder {
        ImageView imageView;
        TextView textView;
    }
}
