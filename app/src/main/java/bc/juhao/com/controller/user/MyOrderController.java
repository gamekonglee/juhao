package bc.juhao.com.controller.user;

import android.content.Intent;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.aliyun.iot.ilop.demo.DemoApplication;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import astuetz.MyPagerSlidingTabStrip;
import bc.juhao.com.R;
import bc.juhao.com.adapter.BaseAdapterHelper;
import bc.juhao.com.adapter.QuickAdapter;
import bc.juhao.com.bean.GoodsBean;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.cons.NetWorkConst;
import bc.juhao.com.controller.BaseController;
import bc.juhao.com.listener.INetworkCallBack;
import bc.juhao.com.ui.activity.IssueApplication;
import bc.juhao.com.ui.activity.buy.SearchActivity;
import bc.juhao.com.ui.activity.product.ProDetailActivity;
import bc.juhao.com.ui.activity.user.MyOrderActivity;
import bc.juhao.com.ui.adapter.FragmentVPAdapter;
import bc.juhao.com.ui.fragment.OrderFragment;
import bc.juhao.com.utils.UIUtils;
import bocang.json.JSONArray;
import bocang.json.JSONObject;
import bocang.utils.AppUtils;

/**
 * @author: Jun
 * @date : 2017/2/6 11:10
 * @description :我的订单
 */
public class MyOrderController extends BaseController implements INetworkCallBack, AdapterView.OnItemClickListener {
    private MyOrderActivity mView;
    private MyPagerSlidingTabStrip mtabs;
    private ViewPager main_viewpager;
    private String[] titleArrs;
    private MyPageChangeListener mListener;
    private OrderFragment mOrderFragment;
    public ArrayList<OrderFragment> listFragment;
    private GridView priductGridView;
    private QuickAdapter likeGoods;
    private List<GoodsBean> goodsBeans;

    /**
     * 支付订单
     */
    private void sendPaymentInfo(){
        mNetWork.sendPaymentInfo(this);
    }


    private List<OrderFragment> fragmentList = new ArrayList<OrderFragment>(); //碎片链表
    private List<String> contentList = new ArrayList<String>(); //内容链表

    public MyOrderController(MyOrderActivity v){
        mView=v;
        initView();
        initViewData();
    }

    private void initViewData() {
//        sendPaymentInfo();
//        selectProduct(1,"12");
    }

    private void initView() {
        titleArrs = UIUtils.getStringArr(R.array.order_titles);
        main_viewpager = (ViewPager)mView.findViewById(R.id.main_viewpager);
        mtabs = (MyPagerSlidingTabStrip) mView.findViewById(R.id.tabs);
        priductGridView = mView.findViewById(R.id.priductGridView);
        priductGridView.setOnItemClickListener(this);

//        pd = (ProgressBar) mView.getActivity().findViewById(R.id.pd);
//        pd.setVisibility(View.VISIBLE);
        likeGoods = new QuickAdapter<GoodsBean>(mView, R.layout.item_like_goods){
            @Override
            protected void convert(BaseAdapterHelper helper, GoodsBean item) {

                helper.setText(R.id.tv_name,""+item.getName());
                helper.setText(R.id.tv_price,"¥"+item.getCurrent_price());
                ImageView imageView=helper.getView(R.id.iv);
                ImageLoader.getInstance().displayImage(NetWorkConst.SCENE_HOST+item.getOriginal_img(),imageView,((DemoApplication)mView.getApplicationContext()).getImageLoaderOption());
            }
        };
        priductGridView.setAdapter(likeGoods);
        listFragment = new ArrayList<>();
        contentList.add("-1");
        contentList.add("0");
        contentList.add("1");
        contentList.add("2");
        contentList.add("4");
        contentList.add("3");

        //有多少个标题就有多少个碎片，动态添加
        for(int i=0;i<titleArrs.length;i++){
            OrderFragment testFm = new OrderFragment().newInstance(contentList, i);
            fragmentList.add(testFm);
        }
        main_viewpager.setAdapter(new FragmentVPAdapter(mView.getSupportFragmentManager(), (ArrayList<OrderFragment>) fragmentList, titleArrs));
        mtabs.setViewPager(main_viewpager);
        mListener=new MyPageChangeListener();
        mtabs.setOnPageChangeListener(mListener);
        main_viewpager.setCurrentItem(mView.mOrderType);

    }

    @Override
    public void onSuccessListener(String requestCode, JSONObject ans) {
        switch (requestCode) {
            case NetWorkConst.PAYMENTINFO:
                 int errorCode=ans.getInt(Constance.error_code);
                if(errorCode==0){
                    mView.PARTNER=ans.getString(Constance.partner);
                    mView.SELLER=ans.getString(Constance.seller_id);
                    mView.RSA_PRIVATE=ans.getString(Constance.private_key);
                }
                break;
        }
    }

    @Override
    public void onFailureListener(String requestCode, JSONObject ans) {

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
                if (null == mView || mView.isFinishing())
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
                goodsBeans.add(goodsBean);
            }
        }
        likeGoods.replaceAll(goodsBeans);
        likeGoods.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int productId = goodsBeans.get(position).getId();
        Intent intent = new Intent(mView, ProDetailActivity.class);
        intent.putExtra(Constance.product, productId);
        mView.startActivity(intent);
    }

    class MyPageChangeListener implements ViewPager.OnPageChangeListener{
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            //当选择后才进行获取数据，而不是预加载
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    /**
     * FragmentPagerAdapter能够预加载，
     * FragmentPagerAdapter不能够预加载，一切换就会销毁掉以前的Fragment
     */
    class TestAdapter extends FragmentPagerAdapter {

        public TestAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return listFragment.get(position);
        }


        @Override
        public int getCount() {
            if(titleArrs!=null){
                return titleArrs.length;
            }
            return 0;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position>titleArrs.length){
                return "暂未加载";
            }
            return titleArrs[position];
        }
    }

    @Override
    protected void handleMessage(int action, Object[] values) {
        
    }

    @Override
    protected void myHandleMessage(Message msg) {

    }

    /**
     * 查询订单号
     * @param orderCode
     */
    public void SearchOrder(String orderCode){
        Intent intent=new Intent(mView, SearchActivity.class);
        mView.startActivity(intent);
//        mView.setShowDialog(true);
//        mView.setShowDialog("正在查找中...");
//        mView.showLoading();
//        mNetWork.semdOrderSearch(orderCode, new INetworkCallBack02() {
//            @Override
//            public void onSuccessListener(String requestCode, com.alibaba.fastjson.JSONObject ans) {
//                mView.hideLoading();
//                com.alibaba.fastjson.JSONObject orderObject = ans.getJSONObject(Constance.orders);
//                if(AppUtils.isEmpty(orderObject)){
//                    MyToast.show(mView,"该订单查询不到!");
//                    return;
//                }
//                Intent intent=new Intent(mView, SearchActivity.class);
//                intent.putExtra(Constance.order, ans.getJSONObject(Constance.orders).toJSONString());
//                mView.startActivity(intent);
//            }
//
//            @Override
//            public void onFailureListener(String requestCode, com.alibaba.fastjson.JSONObject ans) {
//                mView.hideLoading();
//            }
//        })
//        ;
    }

}
