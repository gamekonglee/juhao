package bc.juhao.com.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import bc.juhao.com.R;
import bc.juhao.com.bean.PostImageVideoBean;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.cons.NetWorkConst;
import bc.juhao.com.ui.activity.IssueApplication;
import bc.juhao.com.utils.MyShare;
import bocang.json.JSONObject;

/**
 * @author Jun
 * @time 2017/1/9  10:49
 * @desc ${TODD}
 */
public class SunImageAddMovieAdapter extends BaseAdapter {
    private List<PostImageVideoBean> mImageResList=new ArrayList<>();
    private Context mContext;

    public SunImageAddMovieAdapter(Context context, List<PostImageVideoBean> imageResList){
        this.mContext=context;
        mImageResList=imageResList;
    }

    public void setDatas(List<PostImageVideoBean> imageResList){
        this.mImageResList=imageResList;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (null == mImageResList)
            return 0;
        if(mImageResList.size()>3)return 3;
        return mImageResList.size()<3?2:mImageResList.size();
    }

    @Override
    public PostImageVideoBean getItem(int position) {
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
            holder.iv_video=convertView.findViewById(R.id.iv_video);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if(position==getCount()-1){
            String Token= MyShare.get(mContext).getString(Constance.TOKEN);
            if(!TextUtils.isEmpty(Token)) {
                JSONObject mUserObject= IssueApplication.mUserObject;
                if(mUserObject!=null){
                    int leve=mUserObject.getInt(Constance.level);
                    if(leve==0){
                        holder.imageView.setImageResource(R.mipmap.spxq_icon_st);
                    }else {
                        if(position>=mImageResList.size()){
                            holder.imageView.setVisibility(View.GONE);
                        }else {
                        if(mImageResList.get(position).isVideo){
                            holder.iv_video.setVisibility(View.VISIBLE);
                        }else {
                            holder.iv_video.setVisibility(View.GONE);
                            String imageUrl= NetWorkConst.SCENE_HOST+mImageResList.get(position).url;
                            ImageLoader.getInstance().displayImage(imageUrl, holder.imageView);
                        }
                        }
                    }
                }
            }
        }else {
            if(mImageResList.get(position).isVideo){
                holder.iv_video.setVisibility(View.VISIBLE);
            }else {
                holder.iv_video.setVisibility(View.GONE);
                String imageUrl= NetWorkConst.SCENE_HOST+mImageResList.get(position).url;
                ImageLoader.getInstance().displayImage(imageUrl, holder.imageView);
            }
        }


//        String Token= MyShare.get(mContext).getString(Constance.TOKEN);
//        if(!TextUtils.isEmpty(Token)) {
//            JSONObject mUserObject= IssueApplication.mUserObject;
//            if(mUserObject!=null){
//                int leve=mUserObject.getInt(Constance.level);
//                if(leve==0){
//                    if(position==2){
//                        holder.imageView.setImageResource(R.mipmap.spxq_icon_st);
//                    }else {
//
//                    }
//                }
//            }
//        }

        return convertView;
    }

    class ViewHolder {
        ImageView imageView;
        ImageView iv_video;
    }
}
