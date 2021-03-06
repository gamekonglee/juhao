package bc.juhao.com.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import bc.juhao.com.R;

/**
 * @author Jun
 * @time 2017/1/9  10:49
 * @desc ${TODD}
 */
public class SunImageAddAdapter extends BaseAdapter {
    private List<String> mImageResList=new ArrayList<>();
    private Context mContext;

    public SunImageAddAdapter(Context context, List<String> imageResList){
        this.mContext=context;
        mImageResList=imageResList;
    }

    public void setDatas(List<String> imageResList){
        this.mImageResList=imageResList;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (null == mImageResList)
            return 0;
        return mImageResList.size();
    }

    @Override
    public String getItem(int position) {
        if (null == mImageResList)
            return null;
        return mImageResList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_sun_image_02, null);

            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
//        String imageUrl= NetWorkConst.SCENE_HOST+mImageResList.get(position);
//        ImageLoader.getInstance().displayImage(imageUrl, holder.imageView);
        if(position==0){
            holder.imageView.setImageResource(R.mipmap.spxq_icon_st);
        }else {
            holder.imageView.setImageResource(R.drawable.bg_default);
        }
//        holder.imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//        String Token= MyShare.get(mContext).getString(Constance.TOKEN);
//        if(!TextUtils.isEmpty(Token)) {
//            JSONObject mUserObject= IssueApplication.mUserObject;
//            if(mUserObject!=null){
//                int leve=mUserObject.getInt(Constance.level);
//                if(leve==0){
//                    if(position==2){
//
//                    }else {
////                        String imageUrl= NetWorkConst.SCENE_HOST+mImageResList.get(position);
////                        ImageLoader.getInstance().displayImage(imageUrl, holder.imageView);
//                    }
//                }
//            }
//        }

        return convertView;
    }

    class ViewHolder {
        ImageView imageView;
    }
}
