package bc.juhao.com.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import bc.juhao.com.R;
import bc.juhao.com.bean.PostImageVideoBean;

/**
 * @author Jun
 * @time 2016/12/11  21:33
 * @desc ${TODD}
 */
public class GridViewPostAdapter extends BaseAdapter {

    private Context mContext;
    private List<PostImageVideoBean> list = new ArrayList<PostImageVideoBean>();
    boolean[] isVideo=new boolean[]{false,false,false};
    public GridViewPostAdapter() {
        super();
    }
    /**
     * 获取列表数据
     * @param list
     */
    public void setList(List<PostImageVideoBean> list){
        this.list = list;
        this.notifyDataSetChanged();
    }

    public GridViewPostAdapter(Context mContext, List<PostImageVideoBean> list) {
        super();
        this.mContext = mContext;
        this.list = list;
    }

    @Override
    public int getCount() {
        Log.e("  ", list.size()+"");
        if(list==null){
            return 1;
        }else if(list.size()==9){
            return 9;
        }else if(list.size()==3) {
            return 3;
        }else {
            return list.size()+1;
        }
    }
    public void setIsVideo(boolean[] isVideo){
        this.isVideo=isVideo;
    }
    @Override
    public Object getItem(int position) {
        if (list != null
                && list.size() == 9)
        {
            return list.get(position);
        }

        else if (list == null || position - 1 < 0
                || position > list.size())
        {
            return null;
        }
        else
        {
            return list.get(position - 1);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView==null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_published_grida, null);
            holder = new ViewHolder();
            holder.item_grida_image = (ImageView) convertView.findViewById(R.id.item_grida_image);
            holder.iv_start=convertView.findViewById(R.id.iv_start);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        if (isShowAddItem(position))
        {
            holder.item_grida_image.setImageResource(R.mipmap.my_icon_sc);
            holder.iv_start.setVisibility(View.GONE);
        }else {
            holder.item_grida_image.setImageBitmap(list.get(position).bitmap);
            if(list.get(position).isVideo){
                holder.iv_start.setVisibility(View.VISIBLE);
            }else {
                holder.iv_start.setVisibility(View.GONE);
            }
        }

        return convertView;
    }
    /**
     * 判断当前下标是否是最大值
     * @param position  当前下标
     * @return
     */
    private boolean isShowAddItem(int position)
    {
        int size = list == null ? 0 : list.size();
        return position == size;
    }

    class ViewHolder{
        ImageView item_grida_image;
        ImageView iv_start;
    }

}
