package bc.juhao.com.ui.view.popwindow;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lib.common.hxp.view.GridViewForScrollView;
import com.lib.common.hxp.view.ListViewForScrollView;

import bc.juhao.com.R;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.listener.INumberInputListener;
import bc.juhao.com.listener.IParamentChooseListener;
import bc.juhao.com.ui.view.NumberInputView;
import bc.juhao.com.utils.ImageLoadProxy;
import bocang.utils.AppUtils;


/**
 * @author: Jun
 * @date : 2017/2/16 15:12
 * @description :
 */
public class SelectParamentPopWindow extends BasePopwindown implements View.OnClickListener {
    private ImageView close_iv,goods_iv;
    private LinearLayout bg_ll;
    private TextView goods_name_tv,proPriceTv;
    private Button btn_goShoppingCart;
    private JSONObject mGoodObject;
    private ListViewForScrollView properties_lv;
    private JSONArray propertiesList;
    private ProAdapter mAdapter;
    private NumberInputView mNumberInputView;
    private JSONObject itemObject;
    private int mAmount=1;
    private String mParamentValue="";
    private int mPrice=0;

    private IParamentChooseListener mListener;

    public void setListener(IParamentChooseListener listener) {
        mListener = listener;
    }

    public SelectParamentPopWindow(Context context,JSONObject goodObject) {
        super(context);
        mGoodObject=goodObject;
        initViewData();

    }

    @Override
    protected void initView(Context context) {
        View contentView = View.inflate(mContext, R.layout.pop_select_parament, null);
        initUI(contentView);

    }

    private void initViewData() {
        goods_name_tv.setText(mGoodObject.getString(Constance.name));

        if(!AppUtils.isEmpty(mGoodObject.getJSONObject(Constance.default_photo))){
            String imageUrl= mGoodObject.getJSONObject(Constance.default_photo).getString(Constance.large);
            ImageLoadProxy.displayImage(imageUrl, goods_iv);
        }
        propertiesList=mGoodObject.getJSONArray(Constance.properties);
        mAdapter=new ProAdapter();
        properties_lv.setAdapter(mAdapter);
        if(!AppUtils.isEmpty(propertiesList)&&propertiesList.size()>0){
            final JSONArray attrsList=propertiesList.getJSONObject(0).getJSONArray(Constance.attrs);
            itemObject = attrsList.getJSONObject(0);
            mPrice = itemObject.getInteger(Constance.attr_price);
            proPriceTv.setText("￥" + (Double.parseDouble(mGoodObject.getString(Constance.current_price))+mPrice));
            return;
        }
        proPriceTv.setText("￥" + mGoodObject.getString(Constance.current_price));
    }

    private void initUI(View contentView) {
        close_iv = (ImageView) contentView.findViewById(R.id.close_iv);
        bg_ll = (LinearLayout) contentView.findViewById(R.id.bg_ll);
        close_iv.setOnClickListener(this);
        bg_ll.setOnClickListener(this);
        goods_iv = (ImageView) contentView.findViewById(R.id.goods_iv);
        goods_name_tv = (TextView) contentView.findViewById(R.id.goods_name_tv);
        proPriceTv = (TextView) contentView.findViewById(R.id.proPriceTv);
        btn_goShoppingCart = (Button) contentView.findViewById(R.id.btn_goShoppingCart);
        btn_goShoppingCart.setOnClickListener(this);
        properties_lv = (ListViewForScrollView) contentView.findViewById(R.id.properties_lv);
        properties_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });


        mNumberInputView = (NumberInputView) contentView.findViewById(R.id.number_input_et);
        mNumberInputView.setMax(10000);
        mNumberInputView.setListener(new INumberInputListener() {
            @Override
            public void onTextChange(int index) {
                mAmount=index;
            }
        });

//        mPopupWindow = new PopupWindow(contentView, -1, -1);
//        // 1.让mPopupWindow内部的控件获取焦点
////        mPopupWindow.setFocusable(true);
////        // 2.mPopupWindow内部获取焦点后 外部的所有控件就失去了焦点
////        mPopupWindow.setOutsideTouchable(true);
//        //只有加载背景图还有效果
//        // 3.如果不马上显示PopupWindow 一般建议刷新界面
//        mPopupWindow.update();
//        // 设置弹出窗体显示时的动画，从底部向上弹出
//        mPopupWindow.setAnimationStyle(R.style.AnimBottom);
        mPopupWindow = new PopupWindow(contentView,-1, -1);
        // 1.让mPopupWindow内部的控件获取焦点
        mPopupWindow.setFocusable(true);
        // 2.mPopupWindow内部获取焦点后 外部的所有控件就失去了焦点
        mPopupWindow.setOutsideTouchable(true);
        //只有加载背景图还有效果
        mPopupWindow.setBackgroundDrawable(new ColorDrawable());
        // 3.如果不马上显示PopupWindow 一般建议刷新界面
        mPopupWindow.update();
        mPopupWindow.setAnimationStyle(R.style.AnimBottom);
    }

    @Override
    public void onClick(View v) {
        String property="";
        if(!AppUtils.isEmpty(itemObject)){
            property=itemObject.toJSONString();
        }else{
            onDismiss();
        }
        switch (v.getId()) {
            case R.id.close_iv:
                mListener.onParamentChanged(mParamentValue,false,property,mAmount,mPrice);
                onDismiss();
                break;
            case R.id.bg_ll:
                mListener.onParamentChanged(mParamentValue,false,property,mAmount,mPrice);
                onDismiss();
                break;
            case R.id.btn_goShoppingCart:
//                if(AppUtils.isEmpty(property)) {
//                    MyToast.show(mContext,"请先选择规格");
//                    return;
//                }
                mListener.onParamentChanged(mParamentValue, true, property,mAmount,mPrice);
                onDismiss();
                break;
        }
    }

    private class ProAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            if (null == propertiesList)
                return 0;
            return propertiesList.size();
        }


        @Override
        public JSONObject getItem(int position) {
            if (null == propertiesList)
                return null;
            return propertiesList.getJSONObject(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.item_properties, null);
        //
                holder = new ViewHolder();
                holder.properties_name = (TextView) convertView.findViewById(R.id.properties_name);
                holder.itemGridView = (GridViewForScrollView) convertView.findViewById(R.id.itemGridView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final String name=propertiesList.getJSONObject(position).getString(Constance.name);
            holder.properties_name.setText(name);

            if (holder.itemGridView != null) {
               final JSONArray attrsList=propertiesList.getJSONObject(position).getJSONArray(Constance.attrs);
                final ItemProAdapter gridViewAdapter=new ItemProAdapter(attrsList);
                holder.itemGridView.setAdapter(gridViewAdapter);
                holder.itemGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        itemObject = attrsList.getJSONObject(position);
                        mPrice = itemObject.getInteger(Constance.attr_price);
                        proPriceTv.setText("￥" + (mPrice + mGoodObject.getDouble(Constance.current_price)));
                        gridViewAdapter.mCurrentPoistion = position;
                        gridViewAdapter.notifyDataSetChanged();

                        mParamentValue = name + ":" + itemObject.getString(Constance.attr_name) + "";
                    }
                });
//                holder.itemGridView.performItemClick(null, 0, 0);
            }

            return convertView;
        }




        class ViewHolder {
            TextView properties_name;
            GridViewForScrollView itemGridView;
        }
    }

    private class ItemProAdapter extends BaseAdapter{
        public int mCurrentPoistion=0;
        JSONArray mDatas;
        public ItemProAdapter(JSONArray datas){
            this.mDatas=datas;
        }
        @Override
        public int getCount() {
            if (null == mDatas)
                return 0;
            return mDatas.size();
        }


        @Override
        public JSONObject getItem(int position) {
            if (null == mDatas)
                return null;
            return mDatas.getJSONObject(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.item_properties02, null);
                //
                holder = new ViewHolder();
                holder.item_tv = (TextView) convertView.findViewById(R.id.item_tv);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.item_tv.setText(mDatas.getJSONObject(position).getString(Constance.attr_name));
            holder.item_tv.setSelected(mCurrentPoistion==position?true:false);

            return convertView;
        }

        class ViewHolder {
            TextView item_tv;
        }
    }

}
