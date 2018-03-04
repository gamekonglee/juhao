package bc.juhao.com.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import bc.juhao.com.R;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.utils.ImageLoadProxy;
import bocang.json.JSONArray;
import bocang.json.JSONObject;

/**
 * @author Jun
 * @time 2017/1/9  10:49
 * @desc ${TODD}
 */
public class ItemClassifyAdapter extends BaseAdapter {
    private JSONArray mClassifyGoodses;
    private Context mContext;

    public ItemClassifyAdapter(Context context) {
        this.mContext = context;
    }

    public void setDatas(JSONArray classifyGoodses) {
        this.mClassifyGoodses = classifyGoodses;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (null == mClassifyGoodses)
            return 0;
        return mClassifyGoodses.length();
    }

    @Override
    public JSONObject getItem(int position) {
        if (null == mClassifyGoodses)
            return null;
        return mClassifyGoodses.getJSONObject(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_gridview_fm_classify_new, null);

            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.classify_iv);
            holder.textView = (TextView) convertView.findViewById(R.id.classify_tv);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        JSONObject object = mClassifyGoodses.getJSONObject(position);
        String name = object.getString(Constance.name);
        String thumbs = object.getString(Constance.thumbs);

        if (name.length() > 10) {
            holder.textView.setTextSize(13);
            holder.textView.setText(name);
            ImageLoadProxy.displayImage(thumbs, holder.imageView);
        } else {
            holder.textView.setTextSize(15);
            holder.textView.setText(name);
            ImageLoadProxy.displayImage(thumbs, holder.imageView);
        }

        return convertView;
    }

    class ViewHolder {
        ImageView imageView;
        TextView textView;
    }
}
