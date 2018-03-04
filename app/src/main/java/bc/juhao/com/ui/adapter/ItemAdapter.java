package bc.juhao.com.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import bc.juhao.com.R;
import bocang.utils.AppUtils;

/**
 * @author Jun
 * @time 2017/1/9  10:49
 * @desc ${TODD}
 */
public class ItemAdapter extends BaseAdapter {
    private List<String> mNameList=new ArrayList<>();
    private List<String> mImageResList=new ArrayList<>();
    private Context mContext;

    public ItemAdapter(Context context){
        this.mContext=context;
    }

    public void setDatas(List<String> nameList,List<String> imageResList){
        this.mNameList=nameList;
        this.mImageResList=imageResList;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (null == mNameList)
            return 0;
        return mNameList.size();
    }

    @Override
    public String getItem(int position) {
        if (null == mNameList)
            return null;
        return mNameList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_gridview_fm_home, null);

            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
            holder.textView = (TextView) convertView.findViewById(R.id.textView);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if(mNameList.get(position).length()>10){
            holder.textView.setTextSize(13);
            holder.textView.setText(mNameList.get(position));
//            holder.imageView.setImageResource(mImageResList.get(position));
        }else{
            holder.textView.setTextSize(15);
            holder.textView.setText(mNameList.get(position));
//            holder.imageView.setImageResource(mImageResList.get(position));
        }
        if(AppUtils.isEmpty(mImageResList.get(position)))
        {
            holder.imageView.setImageResource(R.mipmap.type_7);
        }else{
            ImageLoader.getInstance().displayImage(mImageResList.get(position), holder.imageView);
        }

        return convertView;
    }

    class ViewHolder {
        ImageView imageView;
        TextView textView;
    }
}
