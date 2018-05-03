package bc.juhao.com.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import bc.juhao.com.R;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.cons.NetWorkConst;
import bc.juhao.com.listener.IUpdateProductPriceListener;
import bc.juhao.com.ui.activity.IssueApplication;
import bc.juhao.com.ui.activity.product.PostedImageActivity;
import bc.juhao.com.ui.activity.product.ProDetailActivity;
import bocang.utils.AppUtils;

import static bc.juhao.com.cons.Constance.id;
import static bc.juhao.com.cons.Constance.product_price;

/**
 * @author: Jun
 * @date : 2017/1/21 13:46
 * @description : 选择方案类型
 */
public class OrderGvAdapter extends BaseAdapter {
    private JSONArray mOrderes;
    private Activity mContext;
    private Intent mIntent;
    private List<Boolean> mIsClick;
    int  mOrderLevel;
    int state;
    private IUpdateProductPriceListener mListener;
    private final String mOrderId;

    public void setUpdateProductPriceListener(IUpdateProductPriceListener mListener) {
        this.mListener = mListener;
    }

    public OrderGvAdapter(Activity context, JSONArray orderes, int  orderLevel,int state,String order_id) {
        mContext = context;
        mOrderes = orderes;
        mOrderLevel = orderLevel;
        mOrderId = order_id;
       this.state=state;
        mIsClick = new ArrayList<>();
        for (int i = 0; i < mOrderes.size(); i++) {
            mIsClick.add(false);
        }

    }


    @Override
    public int getCount() {
        if (null == mOrderes)
            return 0;
        return mOrderes.size();
    }

    @Override
    public JSONObject getItem(int position) {
        if (null == mOrderes)
            return null;
        return mOrderes.getJSONObject(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_order, null);

            holder = new ViewHolder();

            holder.name_tv = (TextView) convertView.findViewById(R.id.name_tv);
            holder.goods_summoney_tv = (TextView) convertView.findViewById(R.id.goods_summoney_tv);
            holder.goods_sum_tv = (TextView) convertView.findViewById(R.id.goods_sum_tv);
            holder.property_tv = (TextView) convertView.findViewById(R.id.property_tv);
            holder.old_priceTv = (TextView) convertView.findViewById(R.id.old_priceTv);
            holder.agio_priceTv = (TextView) convertView.findViewById(R.id.agio_priceTv);
            holder.update_product_money_tv = (TextView) convertView.findViewById(R.id.update_product_money_tv);
            holder.comment_tv=convertView.findViewById(R.id.comment_tv);
            holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final JSONObject object = mOrderes.getJSONObject(position);
//        com.alibaba.fastjson.JSONArray goods=orderobject.getJSONArray(Constance.goods);
        com.alibaba.fastjson.JSONObject group_buy=new com.alibaba.fastjson.JSONObject();
        int group_buyint=-1;
            try {
                group_buy= (com.alibaba.fastjson.JSONObject)object.get(Constance.group_buy);
            }catch (Exception e){
                group_buyint=object.getInteger(Constance.group_buy);
            }

        boolean isJuhao=false;
        if(!AppUtils.isEmpty(group_buy)&&group_buyint!=0||group_buyint==212&&group_buyint!=0){
            isJuhao=true;
        }
        String num = object.getString(Constance.total_amount);
        String totalMoney = object.getString(Constance.total_price);
        holder.goods_sum_tv.setText("X" + num + "件");


        final String productPrice = object.getString(product_price);
        String originalPrice = object.getString(Constance.original_price);
        if(TextUtils.isEmpty(originalPrice)){
            originalPrice=productPrice;
        }
        holder.goods_summoney_tv.setText("优惠价:" + originalPrice + "元");
        Double avg = ((Double.parseDouble(productPrice) / Double.parseDouble(originalPrice)) * 100);
        if (avg == 100) {
            String str = "折扣价:" + productPrice;
            holder.agio_priceTv.setText(str);
        } else {
            DecimalFormat decimalFormat = new DecimalFormat(".0");//构造方法的字符格式这里如果小数不足2位,会以0补足.
            String p = decimalFormat.format(avg * 0.1);//format 返回的是字符串
            String val = "(" + p + "折)";
            String str = "折扣价:" +productPrice + val;
            int fstart = str.indexOf(val);
            int fend = fstart + val.length();
            SpannableStringBuilder style = new SpannableStringBuilder(str);
            style.setSpan(new ForegroundColorSpan(Color.RED), fstart, fend, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            holder.agio_priceTv.setText(style);
        }

        Float old_price = Float.parseFloat(object.getString(product_price));
        DecimalFormat decimalFormat = new DecimalFormat(".0");//构造方法的字符格式这里如果小数不足2位,会以0补足.
        String p = decimalFormat.format(old_price * 1.6);//format 返回的是字符串
        holder.old_priceTv.setText("零售价:" + p + "元");
        holder.name_tv.setText(object.getJSONObject(Constance.product).getString(Constance.name));
        String property = object.getString(Constance.property);
        int is_discount=object.getInteger(Constance.is_discount);
        if (!AppUtils.isEmpty(property)) {
            holder.property_tv.setText(property);
            holder.property_tv.setVisibility(View.VISIBLE);
        } else {
            holder.property_tv.setVisibility(View.GONE);
        }
        try {
            ImageLoader.getInstance().displayImage(object.getJSONObject(Constance.product).
                    getJSONArray(Constance.photos).getJSONObject(0).getString(Constance.thumb), holder.imageView);
        } catch (Exception e) {

        }
        int mLevel = IssueApplication.mUserObject.getInt(Constance.level);
        if (mLevel == 0) {

            if ( state == 0) {
                if(isJuhao){
                    holder.update_product_money_tv.setVisibility(View.GONE);
                }else {
                        holder.update_product_money_tv.setVisibility(View.VISIBLE);
                }

            } else {
                holder.update_product_money_tv.setVisibility(View.GONE);
            }



        } else {
            holder.update_product_money_tv.setVisibility(View.GONE);
        }
//        if (mIsUpdate) {
//            holder.update_product_money_tv.setVisibility(View.VISIBLE);
//        } else {
//            holder.update_product_money_tv.setVisibility(View.GONE);
//        }
        holder.update_product_money_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onUpdateProductPriceListener(position, object);
            }
        });
        holder.comment_tv.setVisibility(View.GONE);
        switch (state) {
            case 3:
//                do_tv.setVisibility(View.VISIBLE);
//                do_tv.setText("联系商家");
//                stateValue = "【已完成】";
                    holder.comment_tv.setVisibility(View.VISIBLE);
                break;
            case 4:
//                do_tv.setVisibility(View.VISIBLE);
//                do_tv.setText("联系商家");
//                stateValue = "【已完成】";
                    holder.comment_tv.setVisibility(View.VISIBLE);
                break;
        }
        holder.comment_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, PostedImageActivity.class);
                intent.putExtra(Constance.id, object.getString(Constance.id));
                intent.putExtra(Constance.order_id, mOrderId);
                intent.putExtra(Constance.goods,object.getJSONObject(Constance.product).getString(Constance.name));
                intent.putExtra(Constance.property,object.getString(Constance.property));
                try{
                    intent.putExtra(Constance.img,object.getJSONObject(Constance.product).
                            getJSONArray(Constance.photos).getJSONObject(0).getString(Constance.thumb));
                }catch (Exception e){
                    intent.putExtra(Constance.img, NetWorkConst.SHAREIMAGE);
                }
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }

    class ViewHolder {
        ImageView imageView;
        TextView name_tv;
        TextView goods_summoney_tv;
        TextView old_priceTv;
        TextView goods_sum_tv;
        TextView property_tv;
        TextView update_product_money_tv;
        TextView agio_priceTv;
        TextView comment_tv;
    }


}
