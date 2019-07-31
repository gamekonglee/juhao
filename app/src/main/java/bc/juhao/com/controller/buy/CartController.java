package bc.juhao.com.controller.buy;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Message;
import android.text.InputType;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aliyun.iot.ilop.demo.DemoApplication;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.google.gson.Gson;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.NetUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.yjn.swipelistview.swipelistviewlibrary.widget.SwipeMenu;
import com.yjn.swipelistview.swipelistviewlibrary.widget.SwipeMenuCreator;
import com.yjn.swipelistview.swipelistviewlibrary.widget.SwipeMenuItem;
import com.yjn.swipelistview.swipelistviewlibrary.widget.SwipeMenuListView;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import bc.juhao.com.R;
import bc.juhao.com.adapter.BaseAdapterHelper;
import bc.juhao.com.adapter.QuickAdapter;
import bc.juhao.com.bean.Default_photo;
import bc.juhao.com.bean.GoodsBean;
import bc.juhao.com.chat.DemoHelper;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.cons.NetWorkConst;
import bc.juhao.com.controller.BaseController;
import bc.juhao.com.listener.INetworkCallBack;
import bc.juhao.com.ui.activity.ChartListActivity;
import bc.juhao.com.ui.activity.buy.ConfirmOrderActivity;
import bc.juhao.com.ui.activity.buy.ExInventoryActivity;
import bc.juhao.com.ui.activity.product.ProDetailActivity;
import bc.juhao.com.ui.activity.user.ChatActivity;
import bc.juhao.com.ui.fragment.CartFragment;
import bc.juhao.com.ui.view.NumberInputView;
import bc.juhao.com.ui.view.ShowDialog;
import bc.juhao.com.utils.MyShare;
import bc.juhao.com.utils.UIUtils;
import bocang.json.JSONArray;
import bocang.json.JSONObject;
import bocang.utils.AppDialog;
import bocang.utils.AppUtils;
import bocang.utils.IntentUtil;
import bocang.utils.MyLog;
import bocang.utils.MyToast;

/**
 * @author: Jun
 * @date : 2017/2/21 14:35
 * @description :
 */
public class CartController extends BaseController implements INetworkCallBack, AdapterView.OnItemClickListener {
    private CartFragment mView;
    private SwipeMenuListView mListView;
    private JSONArray goodses;
    private MyAdapter myAdapter;
    private CheckBox checkAll;
    private TextView money_tv,settle_tv,edit_tv,num_tv;
    private boolean isStart=false;
    private LinearLayout sum_ll;
    private Boolean isEdit=false;
    private JSONArray goods;
//    private ProgressBar pd;
    private JSONObject mAddressObject;
    private View null_view;
    private int normal;
    private int juhao;
    private GridView mProGridView;
    private QuickAdapter likeGoods;
    private List<GoodsBean> goodsBeans;
    private DecimalFormat df;

    public CartController(CartFragment v) {
        mView = v;
        initView();
        initViewData();
    }

    private void initViewData() {
//        mView.showLoadingPage("", R.drawable.ic_loading);
        mView.setShowDialog(true);
        mView.showLoading();
        sendAddressList();
        selectProduct(1,10+"");
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                mView.getActivity().getResources().getDisplayMetrics());
    }
    /**
     * 获取产品列表
     *
     * @param page
     * @param per_page
     */
    public void selectProduct(int page, String per_page) {
        Random random=new Random();
        String sortKey=(random.nextInt(5)+1)+"";
        String sortValue=(random.nextInt(2)+1)+"";
        mNetWork.sendGoodsList(page, per_page, null, null, null, null, null, sortKey, sortValue, new INetworkCallBack() {
            @Override
            public void onSuccessListener(String requestCode, JSONObject ans) {
                if (null == mView ||mView.getActivity()==null|| mView.getActivity().isFinishing())
                    return;

                JSONArray goodsList = ans.getJSONArray(Constance.goodsList);
                if (AppUtils.isEmpty(goodsList) || goodsList.length() == 0) {
                    return;
                }

                getDataSuccess(goodsList);
            }

            @Override
            public void onFailureListener(String requestCode, JSONObject ans) {

            }
        });
    }

    private void getDataSuccess(JSONArray array) {
        goodsBeans = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            try {

                goodsBeans.add(new Gson().fromJson(String.valueOf(array.getJSONObject(i)),GoodsBean.class));
            }catch (Exception e){
                GoodsBean goodsBean=new GoodsBean();
                goodsBean.setId(array.getJSONObject(i).getInt(Constance.id));
                goodsBean.setName(array.getJSONObject(i).getString(Constance.name));
                goodsBean.setCurrent_price(array.getJSONObject(i).getString(Constance.current_price));
                goodsBean.setOriginal_img(array.getJSONObject(i).getString(Constance.original_img));
                goodsBean.setDefault_photo(new Gson().fromJson(array.getJSONObject(i).getJSONObject(Constance.default_photo).toString(), Default_photo.class));

                goodsBeans.add(goodsBean);
            }
        }
        likeGoods.replaceAll(goodsBeans);
        likeGoods.notifyDataSetChanged();
    }

    private void initView() {
        mListView = (SwipeMenuListView) mView.getView().findViewById(R.id.cart_lv);
        mListView.setDivider(null);//去除listview的下划线
        myAdapter = new MyAdapter();
        mListView.setAdapter(myAdapter);

        final SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem deleteItem = new SwipeMenuItem(mView.getActivity());

                deleteItem.setBackground(new ColorDrawable(Color.parseColor("#fe3c3a")));
                deleteItem.setWidth(dp2px(80));
                deleteItem.setTitle("删除");
                deleteItem.setTitleColor(Color.WHITE);
                deleteItem.setTitleSize(20);
                menu.addMenuItem(deleteItem);
            }
        };
        mListView.setMenuCreator(creator);

        mListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                if (index == 0) {
                    mView.setShowDialog(true);
                    mView.setShowDialog("正在删除");
                    mView.showLoading();
                    String id = goodses.getJSONObject(position).getString(Constance.id);
                    //                    mDeleteIndex=position;
                    isLastDelete = true;
                    deleteShoppingCart(id);
                }
                return false;
            }
        });
        checkAll = (CheckBox) mView.getActivity().findViewById(R.id.checkAll);
        money_tv = (TextView) mView.getActivity().findViewById(R.id.money_tv);
        num_tv = (TextView) mView.getActivity().findViewById(R.id.num_tv);
        settle_tv = (TextView) mView.getActivity().findViewById(R.id.settle_tv);
        edit_tv = (TextView) mView.getActivity().findViewById(R.id.edit_tv);
        sum_ll = (LinearLayout) mView.getActivity().findViewById(R.id.sum_ll);
        null_view =  mView.getActivity().findViewById(R.id.null_cart_view);
        mProGridView = (GridView) mView.getView().findViewById(R.id.priductGridView);
        mProGridView.setOnItemClickListener(this);
        df = new DecimalFormat("#####.00");
//        pd = (ProgressBar) mView.getActivity().findViewById(R.id.pd);
//        pd.setVisibility(View.VISIBLE);
        likeGoods = new QuickAdapter<GoodsBean>(mView.getActivity(), R.layout.item_like_goods){
            @Override
            protected void convert(BaseAdapterHelper helper, GoodsBean item) {


                helper.setText(R.id.tv_name,""+item.getName());
                helper.setText(R.id.tv_price,"¥"+item.getCurrent_price());
                TextView tv_old_price=helper.getView(R.id.tv_old_price);
                helper.setText(R.id.tv_old_price,"¥"+(df.format(Double.parseDouble(item.getCurrent_price())*1.6)));
                tv_old_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                ImageView imageView=helper.getView(R.id.iv);
                ImageLoader.getInstance().displayImage(item.getDefault_photo().getThumb(),imageView,((DemoApplication)mView.getActivity().getApplicationContext()).getImageLoaderOption());

            }
        };
        mProGridView.setAdapter(likeGoods);
        mExtView= (ViewGroup) LayoutInflater.from(mView.getActivity()).inflate(R.layout.alertext_form, null);
        etNum = (EditText) mExtView.findViewById(R.id.etName);
        etNum.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        imm = (InputMethodManager)mView.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

    }



    int mDeleteIndex =-1;


    private void deleteShoppingCart(String goodId) {
        mNetWork.sendDeleteCart(goodId, this);
    }

    @Override
    protected void handleMessage(int action, Object[] values) {

    }

    @Override
    protected void myHandleMessage(Message msg) {

    }

    /**
     * 编辑
     */
    public void setEdit() {
        if(!isEdit){
            sum_ll.setVisibility(View.GONE);
            settle_tv.setText("删除");
            edit_tv.setText("完成");
            isEdit=true;

        }else{
            sum_ll.setVisibility(View.VISIBLE);
            settle_tv.setText("结算");
            edit_tv.setText("编辑");
            isEdit=false;
        }
    }

    public void sendShoppingCart() {
        null_view.setVisibility(View.GONE);
        mNetWork.sendShoppingCart(this);
    }

    public void sendUpdateCart(String good, String amount) {
        mNetWork.sendUpdateCart(good, amount, this);
    }

    @Override
    public void onSuccessListener(String requestCode, JSONObject ans) {
//        pd.setVisibility(View.INVISIBLE);
        try{
            mView.showContentView();
        }catch (Exception e){

        }
        switch (requestCode) {
            case NetWorkConst.DeleteCART:
//                if(isLastDelete){
//                    isLastDelete=false;
////                    isCheckList.remove(mDeleteIndex);
//                    return;
//                }
                    sendShoppingCart();
                mView.hideLoading();
                break;
            case NetWorkConst.UpdateCART:
                sendShoppingCart();
                break;
            case NetWorkConst.GETCART:
                mView.hideLoading();
                if (ans.getJSONArray(Constance.goods_groups).length() > 0) {
                    goodses = ans.getJSONArray(Constance.goods_groups).getJSONObject(0).getJSONArray(Constance.goods);
                    if(isCheckList==null||isCheckList.size()!=goodses.length()){
                    myAdapter.addIsCheckAll(false);
                    }
                    myAdapter.notifyDataSetChanged();
                    myAdapter.getTotalMoney();
                    DemoApplication.mCartCount = ans.getJSONArray(Constance.goods_groups)
                            .getJSONObject(0).getInt(Constance.total_amount);
                } else {
                    goodses = null;
                    isCheckList=new ArrayList<>();
                    myAdapter.notifyDataSetChanged();
                    myAdapter.getTotalMoney();
                    DemoApplication.mCartCount=0;
                    null_view.setVisibility(View.VISIBLE);
                }
                EventBus.getDefault().post(Constance.CARTCOUNT);

                isStart=true;
                break;
            case NetWorkConst.CONSIGNEELIST:
                JSONArray consigneeList = ans.getJSONArray(Constance.consignees);
                if (consigneeList.length() == 0)
                    return;
                mAddressObject=consigneeList.getJSONObject(0);
                break;
        }

    }



    @Override
    public void onFailureListener(String requestCode, JSONObject ans) {
        isLastDelete=false;
        mView.hideLoading();
        mView.showContentView();
        if(AppUtils.isEmpty(ans)){
            AppDialog.messageBox(UIUtils.getString(R.string.server_error));
            return;
        }
        AppDialog.messageBox(ans.getString(Constance.error_desc));
//        pd.setVisibility(View.INVISIBLE);
    }


//    private List<Integer> buyNum = new ArrayList<>();

    private ArrayList<Boolean> isCheckList = new ArrayList<>();

    public void setCkeckAll(Boolean isCheck) {
        myAdapter.setIsCheckAll(isCheck);
        myAdapter.getTotalMoney();
        myAdapter.notifyDataSetChanged();

    }



    /**
     * 结算/删除
     */
    public void sendSettle() {
        if(!isEdit){
            myAdapter.getCartGoodsCheck();
            if(goods==null||goods.length()==0){
                MyToast.show(mView.getActivity(),"请选择产品");
                return;
            }
            normal = 0;
            juhao = 0;
            if(myAdapter.getDifferentCheck()){
                showTypeSelectDialog(true,true);
                return;
            }
            Intent intent=new Intent(mView.getActivity(),ConfirmOrderActivity.class);
            intent.putExtra(Constance.goods,goods);
            intent.putExtra(Constance.money,mMoney);
            intent.putExtra(Constance.address,mAddressObject);

            mView.getActivity().startActivity(intent);
        }else{
            sendDeleteCart();
        }
    }

    private Boolean isLastDelete=false;

    /**
     * 删除购物车数据
     */
    public  void sendDeleteCart(){
        if(isCheckList.size()==0){
            MyToast.show(mView.getActivity(),"请选择产品");
            return;
        }
        mView.setShowDialog(true);
        mView.setShowDialog("正在删除");
        mView.showLoading();
        ArrayList<String> deleteList=new ArrayList<>();
        for(int i=0;i<isCheckList.size();i++){
            if(isCheckList.get(i)){
                String id = goodses.getJSONObject(i).getString(Constance.id);
                deleteList.add(id);
            }
        }
        for(int j=0;j<deleteList.size();j++){
            if(j==deleteList.size()-1){
                isLastDelete=true;
            }
//            mDeleteIndex=j;
            deleteShoppingCart(deleteList.get(j));
        }
    }

    float mMoney=0;

    /**
     * 获取收货地址
     */
    public void sendAddressList() {
        mNetWork.sendAddressList(this);
    }

    /**
     * 导出清单
     */
    public void exportData() {
        myAdapter.getCartGoodsCheck();
        if(goods==null||goods.length()==0){
            MyToast.show(mView.getActivity(),"请选择产品");
            return;
        }
        Intent intent=new Intent(mView.getActivity(),ExInventoryActivity.class);
        intent.putExtra(Constance.goods, goods);
        mView.getActivity().startActivity(intent);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        int productId = goodsBeans.get(i).getId();
        Intent intent = new Intent(mView.getActivity(), ProDetailActivity.class);
        intent.putExtra(Constance.product, productId);
        mView.getActivity().startActivity(intent);
    }


    private class MyAdapter extends BaseAdapter {
        private DisplayImageOptions options;
        private ImageLoader imageLoader;

        public MyAdapter() {
            options = new DisplayImageOptions.Builder()
                    // 设置图片下载期间显示的图片
                    .showImageOnLoading(R.drawable.bg_default)
                            // 设置图片Uri为空或是错误的时候显示的图片
                    .showImageForEmptyUri(R.drawable.bg_default)
                            // 设置图片加载或解码过程中发生错误显示的图片
                            // .showImageOnFail(R.drawable.ic_error)
                            // 设置下载的图片是否缓存在内存中
                    .cacheInMemory(true)
                            // 设置下载的图片是否缓存在SD卡中
                    .cacheOnDisk(true)
                            // .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
                            // 是否考虑JPEG图像EXIF参数（旋转，翻转）
                    .considerExifParams(true)
                    .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)// 设置图片可以放大（要填满ImageView必须配置memoryCacheExtraOptions大于Imageview）
                            // .displayer(new FadeInBitmapDisplayer(100))//
                            // 图片加载好后渐入的动画时间
                    .build(); // 构建完成

            // 得到ImageLoader的实例(使用的单例模式)
            imageLoader = ImageLoader.getInstance();
        }

        public void setIsCheckAll(Boolean isCheck) {
            if(AppUtils.isEmpty(goodses)) return;
            for (int i = 0; i < goodses.length(); i++) {
                isCheckList.set(i, isCheck);
            }
        }

        public void addIsCheckAll(Boolean isCheck) {
            isCheckList=new ArrayList<>();
            for (int i = 0; i < goodses.length(); i++) {
                isCheckList.add(isCheck);
            }

        }

        public void getCartGoodsCheck(){
            if(goodses==null)goodses=new JSONArray();
            if(goodses.length()!=isCheckList.size())return;
            goods=new JSONArray();
            for(int i = 0; i < isCheckList.size(); i++){
                if(isCheckList.get(i)){
                    goods.add(goodses.getJSONObject(i));
                }
            }
        }
        public boolean getDifferentCheck(){
            boolean hasXianGou=false;
            boolean hasNormal=false;

            for(int i=0;i<goods.length();i++){
//                JSONObject group_buy=((JSONObject)goods.get(i)).getJSONObject(Constance.product).getJSONObject(Constance.group_buy);
                int is_jh=((JSONObject)goods.get(i)).getJSONObject(Constance.product).getInt(Constance.is_jh);
                if(is_jh==1){
                    hasXianGou=true;
                    juhao++;
                }else {
                    hasNormal=true;
                    normal++;
                }
            }
            return hasXianGou&&hasNormal;
        }


        public void setIsCheck(int poistion, Boolean isCheck) {
            if(isCheckList.size()<=poistion) return;
            isCheckList.set(poistion, isCheck);
            getTotalMoney();


        }

        /**
         * 获取到总金额
         */
        public void getTotalMoney(){
            float isSumMoney = 0;
            int count=0;
            if(AppUtils.isEmpty(goodses)){
                checkAll.setChecked(false);
                money_tv.setText("￥" + 0 + "");
                num_tv.setText(0 + "件");
                return;
            }
            for (int i = 0; i < goodses.length(); i++) {
                if (isCheckList.get(i) == true) {
                    double price = Double.parseDouble(goodses.getJSONObject(i).getString(Constance.price));
                    int num=goodses.getJSONObject(i).getInt(Constance.amount);
                    isSumMoney += (num * price);
                    count+=num;
                }
            }
            mMoney=isSumMoney;

            num_tv.setText(count+ "件");
            money_tv.setText("￥" +isSumMoney + "");
        }

        @Override
        public int getCount() {
            if (null == goodses)
                return 0;
            return goodses.length();
        }


        @Override
        public JSONObject getItem(int position) {
            if (null == goodses)
                return null;
            return goodses.getJSONObject(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(mView.getActivity(), R.layout.item_lv_cart_new, null);

                holder = new ViewHolder();
                holder.checkBox = (TextView) convertView.findViewById(R.id.checkbox);
                holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
                holder.leftTv = (ImageView) convertView.findViewById(R.id.leftTv);
                holder.rightTv = (ImageView) convertView.findViewById(R.id.rightTv);
                holder.nameTv = (TextView) convertView.findViewById(R.id.nameTv);
                holder.contact_service_tv = (TextView) convertView.findViewById(R.id.contact_service_tv);
                holder.SpecificationsTv = (TextView) convertView.findViewById(R.id.SpecificationsTv);
                holder.numTv = (EditText) convertView.findViewById(R.id.numTv);
                holder.priceTv = (TextView) convertView.findViewById(R.id.priceTv);
                holder.old_priceTv = (TextView) convertView.findViewById(R.id.old_priceTv);
                holder.number_input_et = (NumberInputView) convertView.findViewById(R.id.number_input_et);
                holder.view_sc_item=convertView.findViewById(R.id.view_sc_item);
//                //取得设置好的drawable对象
//                Drawable drawable = mView.getResources().getDrawable(R.drawable.selector_checkbox03);
//                //设置drawable对象的大小
//                drawable.setBounds(0, 0, 80, 80);
//                //设置CheckBox对象的位置，对应为左、上、右、下
//                holder.checkBox.setCompoundDrawables(drawable,null,null,null);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final JSONObject goodsObject = goodses.getJSONObject(position);
            JSONObject product=goodsObject.getJSONObject(Constance.product);
            holder.nameTv.setText(product.getString(Constance.name));
            try{
                imageLoader.displayImage(goodsObject.getJSONObject(Constance.product).getJSONObject(Constance.default_photo).getString(Constance.thumb)
                        , holder.imageView, options);
            }catch (Exception e){
                e.printStackTrace();
            }
            holder.view_sc_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int productId = Integer.parseInt(goodsObject.getJSONObject(Constance.product).getString(Constance.id));
                    Intent intent = new Intent(mView.getActivity(), ProDetailActivity.class);
                    intent.putExtra(Constance.product, productId);
                    mView.getActivity().startActivity(intent);
                }
            });
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int productId = Integer.parseInt(goodsObject.getJSONObject(Constance.product).getString(Constance.id));
                    Intent intent = new Intent(mView.getActivity(), ProDetailActivity.class);
                    intent.putExtra(Constance.product, productId);
                    mView.getActivity().startActivity(intent);
                }
            });

            String property = goodsObject.getString(Constance.property);
            if(AppUtils.isEmpty(property)){
                holder.SpecificationsTv.setVisibility(View.GONE);
            }else{
                holder.SpecificationsTv.setVisibility(View.VISIBLE);
            }


            holder.SpecificationsTv.setText(property);
            String price = goodsObject.getString(Constance.price);
            holder.priceTv.setText("¥" + price+"元");
            String oldPrice ;
            JSONObject group_buy=product.getJSONObject(Constance.group_buy);

            if(null==group_buy||"212".equals(group_buy.toString())){
                //非限购商品
                oldPrice = goodsObject.getJSONObject(Constance.product).getString(Constance.price);
            }else {
                //限购商品
                oldPrice = goodsObject.getJSONObject(Constance.product).getString(Constance.current_price);
            }
            holder.old_priceTv.setText("零售价:" + oldPrice+"元");
            holder.number_input_et.setMax(10000);//设置数量的最大值

            holder.numTv.setText(goodsObject.getInt(Constance.amount)+"");

            holder.numTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mAlertViewExt==null){
                        mAlertViewExt = new AlertView("提示", "修改购买数量！", "取消", null, new String[]{"完成"},
                                mView.getActivity(), AlertView.Style.Alert, new OnItemClickListener() {
                            @Override
                            public void onItemClick(Object o, int position) {
                                if(position != AlertView.CANCELPOSITION){
                                    String num=etNum.getText().toString();
                                    if(num.equals("0")){
                                        MyToast.show(mView.getActivity(),"不能等于0");
                                        return;
                                    }
                                    mView.setShowDialog(true);
                                    mView.setShowDialog("正在处理中...");
                                    mView.showLoading();
                                    sendUpdateCart(goodsObject.getString(Constance.id), num);
                                }
                            }
                        });

                        etNum.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                            @Override
                            public void onFocusChange(View v, boolean hasFocus) {
                                //输入框出来则往上移动
                                boolean isOpen=imm.isActive();
                                mAlertViewExt.setMarginBottom(isOpen&&hasFocus ? 120 :0);
                                System.out.println(isOpen);
                            }
                        });
                        mAlertViewExt.addExtView(mExtView);
                    }
                    etNum.setText(goodsObject.getInt(Constance.amount)+"");
                    mAlertViewExt.show();

                }
            });

//            holder.checkBox.setChecked(isCheckList.get(position));
            if(!isCheckList.get(position)){
                Drawable drawable=mView.getActivity().getResources().getDrawable(R.mipmap.shopping_cart_unchecked);
                drawable.setBounds(0,0,UIUtils.dip2PX(20),UIUtils.dip2PX(20));
                holder.checkBox.setCompoundDrawables(null,drawable,null,null);
            }else {
                Drawable drawable=mView.getActivity().getResources().getDrawable(R.mipmap.shopping_cart_selected);
                drawable.setBounds(0,0,UIUtils.dip2PX(20),UIUtils.dip2PX(20));
                holder.checkBox.setCompoundDrawables(null,drawable,null,null);
            }
            holder.rightTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mView.setShowDialog(true);
                    mView.setShowDialog("正在处理中...");
                    mView.showLoading();
                    sendUpdateCart(goodsObject.getString(Constance.id),(goodsObject.getInt(Constance.amount)+1)+"");
                }
            });
            holder.leftTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(goodsObject.getInt(Constance.amount)==1){
                        MyToast.show(mView.getActivity(),"亲,已经到底啦!");
                        return;
                    }
                    mView.setShowDialog(true);
                    mView.setShowDialog("正在处理中...");
                    mView.showLoading();
                    sendUpdateCart(goodsObject.getString(Constance.id),(goodsObject.getInt(Constance.amount)-1)+"");
                }
            });


            holder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setIsCheck(position,!isCheckList.get(position));
                    myAdapter.notifyDataSetChanged();
                    getTotalMoney();
                }
            });
//            .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                    setIsCheck(position, isChecked);
//                    getTotalMoney();
//
//                }
//            });
            if(DemoApplication.mUserObject!=null){
                if(DemoApplication.mUserObject.getInt(Constance.level)==0){
                    holder.contact_service_tv.setVisibility(View.GONE);
                }else {
                    holder.contact_service_tv.setVisibility(View.VISIBLE);
                }
            }else {
                holder.contact_service_tv.setVisibility(View.GONE);
            }

            holder.contact_service_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int level = DemoApplication.mUserObject.getInt(Constance.level);
                    if(level==0){
                        if(!mView.isToken()){
                            IntentUtil.startActivity(mView.getActivity(), ChartListActivity.class, false);
                        }
                        return;
                    }

                    int id= MyShare.get(mView.getActivity()).getInt(Constance.USERCODEID);
                    if(id==0){
                        MyToast.show(mView.getActivity(),"该用户没有客服信息!");
                    }else{

                        sendCall(position,"尝试连接聊天服务..请连接?");
                    }

                }
            });

            return convertView;
        }

        class ViewHolder {
            TextView checkBox;
            ImageView imageView;
            TextView nameTv;
            TextView priceTv;
            TextView SpecificationsTv;
            NumberInputView number_input_et;
            EditText numTv;
            ImageView leftTv,rightTv;
            TextView contact_service_tv;
            TextView old_priceTv;
            View view_sc_item;
        }
    }

    private AlertView mAlertViewExt;//窗口拓展例子
    private EditText etNum;//拓展View内容
    private InputMethodManager imm;
    private ViewGroup mExtView;

    /**
     * 联系客服
     */
    public void sendCall(final int position, String msg) {
        try {
//            if (AppUtils.isEmpty(DemoApplication.mUserObject.getString("parent_name"))) {
//                MyToast.show(mView.getActivity(), "不能和自己聊天!");
//                return;
//            }

            String parent_name = DemoApplication.mUserObject.getString("parent_name");
            String parent_id = DemoApplication.mUserObject.getString("parent_id");
            int is_jh=myAdapter.getItem(position).getJSONObject(Constance.product).getInt(Constance.is_jh);
            if(is_jh==1){
                parent_name="钜豪超市";
                parent_id="37";
            }
            String userIcon = NetWorkConst.SCENE_HOST+ DemoApplication.mUserObject.getString("parent_avatar");
            EaseUser user=new EaseUser(parent_id);
            user.setNickname(parent_name);
            user.setNick(parent_name);
            user.setAvatar(userIcon);
            DemoHelper.getInstance().saveContact(user);

            if(!EMClient.getInstance().isLoggedInBefore()){
                ShowDialog mDialog=new ShowDialog();
                mDialog.show(mView.getActivity(), "提示", msg, new ShowDialog.OnBottomClickListener() {
                    @Override
                    public void positive() {
                        loginHX(position);
                    }
                    @Override
                    public void negtive() {

                    }
                });
            }else{
                EMClient.getInstance().contactManager().acceptInvitation(parent_id);
                mView.startActivity(new Intent(mView.getActivity(), ChatActivity.class).putExtra(EaseConstant.EXTRA_USER_ID, parent_id));
            }
        } catch (HyphenateException e) {
            e.printStackTrace();
        }
    }

    //登录环信
    private void loginHX(final int position) {
        final Toast toast = Toast.makeText(mView.getActivity(),"服务器连接中...!", Toast.LENGTH_SHORT);
        toast.show();
        if (NetUtils.hasNetwork(mView.getActivity())) {
            final String uid= MyShare.get(mView.getActivity()).getString(Constance.USERID);
            if(AppUtils.isEmpty(uid)){
                return;
            }
            if (!TextUtils.isEmpty(uid)) {

                new Thread(new Runnable() {
                    public void run() {
                        try {
                            EMClient.getInstance().createAccount(uid,uid);//同步方法
                            mView.getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    MyLog.e("注册成功!");
                                    getSuccessLogin(position,uid, toast);
                                }
                            });

                        } catch (final HyphenateException e) {
                            mView.getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    MyLog.e("注册失败!");
                                    getSuccessLogin(position,uid,toast);
                                }
                            });

                        }
                    }
                }).start();


            }
        }
    }

    private void getSuccessLogin(final int position, String uid, final Toast toast) {
        EMClient.getInstance().login(uid, uid, new EMCallBack() {
            @Override
            public void onSuccess() {
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                MyLog.e("登录环信成功!");
                toast.cancel();
                String parent_id = DemoApplication.mUserObject.getString("parent_id");
                try {
                    EMClient.getInstance().contactManager().acceptInvitation(parent_id);
                    mView.startActivity(new Intent(mView.getActivity(), ChatActivity.class).putExtra(EaseConstant.EXTRA_USER_ID, parent_id));
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(mView.getActivity(), "连接失败,请重试!", Toast.LENGTH_SHORT).show();
                MyLog.e("登录环信失败!");
                sendCall(position,"连接聊天失败,请重试?");
            }

            @Override
            public void onProgress(int i, String s) {
            }
        });
    }
    public Dialog showTypeSelectDialog(boolean hasNormal, boolean hasJuhao){
        final Dialog dialog = new Dialog(mView.getActivity(), R.style.customDialog);
        dialog.setContentView(R.layout.dialog_type_select);
        dialog.setCanceledOnTouchOutside(true);
        TextView tv_type_select_tip= (TextView) dialog.findViewById(R.id.tv_type_select_tip);
        LinearLayout ll_taxpay= (LinearLayout) dialog.findViewById(R.id.ll_taxpay);
        final CheckBox cb_taxpay= (CheckBox) dialog.findViewById(R.id.cb_taxpay);
        TextView tv_taxpay_count= (TextView) dialog.findViewById(R.id.tv_taxpay_count);
        LinearLayout ll_hellogou= (LinearLayout) dialog.findViewById(R.id.ll_hellogou);
        final CheckBox cb_hellegou= (CheckBox) dialog.findViewById(R.id.cb_hellegou);
        TextView tv_hellogou_count= (TextView) dialog.findViewById(R.id.tv_hellogou_count);
        Button btn_back_to_sc= (Button) dialog.findViewById(R.id.btn_back_to_sc);
        Button btn_balance= (Button) dialog.findViewById(R.id.btn_balance);
        View line=dialog.findViewById(R.id.line_2);

        tv_taxpay_count.setText(normal+"件");
        tv_hellogou_count.setText(juhao+"件");
        btn_back_to_sc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btn_balance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!cb_taxpay.isChecked()&&!cb_hellegou.isChecked()){
                    Toast.makeText(mView.getActivity(), "至少勾选一种商品结算", Toast.LENGTH_SHORT).show();
                    return;
                }
//                String seletType="";
                if(cb_taxpay.isChecked()){
//                    seletType =Constance.NORMAL_GOODS;
                    for(int i=0;i<goods.length();i++){
                        int is_jh=((JSONObject)goods.get(i)).getJSONObject(Constance.product).getInt(Constance.is_jh);
                        if(is_jh==1){
                           goods.delete(i);
                            isCheckList.set(i,false);
                            i--;
                        }
                    }
                }else if(cb_hellegou.isChecked()){
//                    seletType =Constance.JUGAO_GOODS;
                    for(int i=0;i<goods.length();i++){
                        int is_jh=((JSONObject)goods.get(i)).getJSONObject(Constance.product).getInt(Constance.is_jh);
                        if(is_jh==0){
                            goods.delete(i);
                            isCheckList.set(i,false);
                            i--;
                        }
                    }
                }
                myAdapter.getTotalMoney();
                Intent intent=new Intent(mView.getActivity(),ConfirmOrderActivity.class);
                intent.putExtra(Constance.goods,goods);
                intent.putExtra(Constance.money,mMoney);
                intent.putExtra(Constance.address,mAddressObject);

                mView.getActivity().startActivity(intent);
//                UIHelper.showOrderCreateHome(getActivity(), seletType);
                dialog.dismiss();
            }
        });
        ll_taxpay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb_taxpay.isChecked()){
                    return;
                }else {
                    cb_taxpay.setChecked(true);
                    cb_hellegou.setChecked(false);
                }
            }
        });
        ll_hellogou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb_hellegou.isChecked()){
                    return;
                }else {
                    cb_taxpay.setChecked(false);
                    cb_hellegou.setChecked(true);
                }
            }
        });

        String[] ss=new String[3];
        if(!hasNormal){
            ll_taxpay.setVisibility(View.GONE);
            ss[0]="";
        }else {
            ss[0]="普通商品";
        }
        if(!hasJuhao){
            ll_hellogou.setVisibility(View.GONE);
            ss[1]="";
        }else {
            ss[1]="超市商品";
        }

        StringBuilder sb=new StringBuilder();
        int count=(hasNormal?1:0)+(hasJuhao?1:0);
        if(count>1){
            line.setVisibility(View.VISIBLE);
        }else {
            line.setVisibility(View.GONE);
        }
        for(int j=0;j<2;j++){
            if(!TextUtils.isEmpty(ss[j])){
                sb.append(ss[j]);
                if(j!=count-1&&j<count){
                    sb.append("、");
                }
            }
        }
        tv_type_select_tip.setText("您的购物车包含："+ sb.toString()+"，需要分开结算。");
           /*
         * 获取圣诞框的窗口对象及参数对象以修改对话框的布局设置, 可以直接调用getWindow(),表示获得这个Activity的Window
         * 对象,这样这可以以同样的方式改变这个Activity的属性.
         */
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
        return dialog;
    }
}
