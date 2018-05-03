package bc.juhao.com.controller.product;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baiiu.filter.util.UIUtil;
import com.bigkoo.convenientbanner.CBPageAdapter;
import com.bigkoo.convenientbanner.CBViewHolderCreator;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhy.http.okhttp.callback.FileCallBack;

import org.greenrobot.eventbus.EventBus;
import org.w3c.dom.Text;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import bc.juhao.com.R;
import bc.juhao.com.bean.Author;
import bc.juhao.com.bean.CommentBean;
import bc.juhao.com.bean.DaoMaster;
import bc.juhao.com.bean.DaoSession;
import bc.juhao.com.bean.DbGoodsBean;
import bc.juhao.com.bean.DbGoodsBeanDao;
import bc.juhao.com.bean.PostImageVideoBean;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.cons.NetWorkConst;
import bc.juhao.com.controller.BaseController;
import bc.juhao.com.listener.INetworkCallBack;
import bc.juhao.com.listener.INetworkCallBack02;
import bc.juhao.com.listener.IParamentChooseListener;
import bc.juhao.com.net.ApiClient;
import bc.juhao.com.ui.activity.CommentHomeActivity;
import bc.juhao.com.ui.activity.DetailPhotoActivity;
import bc.juhao.com.ui.activity.IssueApplication;
import bc.juhao.com.ui.activity.product.PostedImageActivity;
import bc.juhao.com.ui.activity.product.ProDetailActivity;
import bc.juhao.com.ui.activity.user.SuccessVideoActivity;
import bc.juhao.com.ui.adapter.ParamentAdapter;
import bc.juhao.com.ui.adapter.SunImageAdapter;
import bc.juhao.com.ui.adapter.SunImageAddAdapter;
import bc.juhao.com.ui.adapter.SunImageAddMovieAdapter;
import bc.juhao.com.ui.adapter.SunImageNoAddAdapter;
import bc.juhao.com.ui.fragment.IntroduceGoodsFragment;
import bc.juhao.com.ui.view.countdownview.CountdownView;
import bc.juhao.com.ui.view.popwindow.SelectParamentPopWindow;
import bc.juhao.com.utils.DateUtils;
import bc.juhao.com.utils.ImageLoadProxy;
import bc.juhao.com.utils.MyShare;
import bc.juhao.com.utils.UIUtils;
import bocang.json.JSONObject;
import bocang.json.JSONParser;
import bocang.utils.AppDialog;
import bocang.utils.AppUtils;
import bocang.utils.MyToast;
import okhttp3.Call;

/**
 * @author: Jun
 * @date : 2017/2/14 17:59
 * @description :
 */
public class IntroduceGoodsController extends BaseController implements INetworkCallBack, CountdownView.OnCountdownEndListener, View.OnClickListener {
    private IntroduceGoodsFragment mView;
    private ConvenientBanner mConvenientBanner;
    private List<String> paths = new ArrayList<>();
    private WebView mWebView;
    private TextView unPriceTv, proNameTv, proPriceTv;
    private TextView mParamentTv, time_tv, remaining_num_tv;
    private ImageView collect_iv;
    private int mIsLike = 0;
    private RelativeLayout rl_rl;
    private String mProperty;
    private CountdownView cv_countdownView;
    private LinearLayout time_ll;
    private TextView time_title_tv;
    private Boolean isStartTimeBuy=false;
    private LinearLayout ll_comment;
    private LinearLayout rl_comment_cotent;
    private LinearLayout ll_dajia;
    private TextView tv_dajia;
    private View v_dajia;
    private LinearLayout ll_24rexiao;
    private TextView tv_24rexiao;
    private View v_24rexiao;
    private LinearLayout ll_tuijian;
    private int page;
    private int type;
    private TextView tv_collect;
    private TextView tv_more;
    private TextView tv_time_price;
    private TextView tv_time_oldprice;
    private ListView lv_paramter;
    private LinearLayout ll_paramter;
    private com.alibaba.fastjson.JSONObject mProductionObject;

    public IntroduceGoodsController(IntroduceGoodsFragment v) {
        mView = v;
        page = 1;
        type = 0;
        initView();
        initViewData();
    }

    private void initViewData() {


        if (AppUtils.isEmpty(((ProDetailActivity) mView.getActivity()).mProductObject)) {
            sendProductDetail();
        } else {
            getProductDetail(((ProDetailActivity) mView.getActivity()).mProductObject);
        }

    }

    private void initView() {
        int mScreenWidth = mView.getActivity().getResources().getDisplayMetrics().widthPixels;
        mConvenientBanner = (ConvenientBanner) mView.getActivity().findViewById(R.id.convenientBanner);
        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) mConvenientBanner.getLayoutParams();
        rlp.width = mScreenWidth;
        rlp.height = mScreenWidth - 20;
        mConvenientBanner.setLayoutParams(rlp);
        ll_comment = mView.getView().findViewById(R.id.ll_comment);
        rl_comment_cotent = mView.getView().findViewById(R.id.rl_comment_cotent);
        ll_dajia = mView.getView().findViewById(R.id.ll_dajia);
        tv_dajia = mView.getView().findViewById(R.id.tv_dajia);
        v_dajia = mView.getView().findViewById(R.id.v_dajia);
        ll_24rexiao = mView.getView().findViewById(R.id.ll_24rexiao);
        tv_24rexiao = mView.getView().findViewById(R.id.tv_24rexiao);
        v_24rexiao = mView.getView().findViewById(R.id.v_24rexiao);
        ll_tuijian = mView.getView().findViewById(R.id.ll_tuijian);
        tv_collect = mView.getView().findViewById(R.id.tv_collect);
        tv_more = mView.getView().findViewById(R.id.tv_more);
        tv_time_price = mView.getView().findViewById(R.id.tv_time_price);
        tv_time_oldprice = mView.getView().findViewById(R.id.tv_time_oldprice);
        lv_paramter = mView.getView().findViewById(R.id.lv_paramter);
        ll_paramter = mView.getView().findViewById(R.id.ll_paramter);
        ll_24rexiao.setOnClickListener(this);
        ll_dajia.setOnClickListener(this);
        mWebView = (WebView) mView.getActivity().findViewById(R.id.webView);
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setDefaultTextEncodingName("utf-8");

        unPriceTv = (TextView) mView.getActivity().findViewById(R.id.unPriceTv);
        remaining_num_tv = (TextView) mView.getActivity().findViewById(R.id.remaining_num_tv);
        time_tv = (TextView) mView.getActivity().findViewById(R.id.time_tv);
        time_title_tv = (TextView) mView.getActivity().findViewById(R.id.time_title_tv);
        proNameTv = (TextView) mView.getActivity().findViewById(R.id.proNameTv);
        proPriceTv = (TextView) mView.getActivity().findViewById(R.id.proPriceTv);
        mParamentTv = (TextView) mView.getActivity().findViewById(R.id.type_tv);
        cv_countdownView = (CountdownView) mView.getActivity().findViewById(R.id.cv_countdownView);
        collect_iv = (ImageView) mView.getActivity().findViewById(R.id.collect_iv);
        time_ll =  mView.getActivity().findViewById(R.id.time_ll);
        rl_rl = (RelativeLayout) mView.getActivity().findViewById(R.id.rl_rl);
        cv_countdownView.setOnCountdownEndListener(this);

    }

    /**
     * 产品详情
     */
    public void sendProductDetail() {
        mView.setShowDialog(true);
        mView.setShowDialog("载入中...");
        mView.showLoading();

        String id = mView.productId;
        if (AppUtils.isEmpty(id))
            return;


        mNetWork.sendProductDetail02(id, new INetworkCallBack02() {
            @Override
            public void onSuccessListener(String requestCode, com.alibaba.fastjson.JSONObject ans) {
                mView.hideLoading();
                if((ProDetailActivity)mView.getActivity()==null)return;
                    ((ProDetailActivity) mView.getActivity()).mProductObject = ans.getJSONObject(Constance.product);
                getProductDetail(((ProDetailActivity) mView.getActivity()).mProductObject);
            }

            @Override
            public void onFailureListener(String requestCode, com.alibaba.fastjson.JSONObject ans) {
                mView.hideLoading();
                MyToast.show(mView.getActivity(), "网络异常，请重新加载!");
            }
        });
        reviceProductList();
    }
    private void reviceProductList() {

        mNetWork.reviceProductList(mView.productId, page, this);
    }
    public static String mCurrentPrice = "";
    public  String mOldPrice = "";

    /**
     * 获取产品详情信息
     */
    private void getProductDetail(com.alibaba.fastjson.JSONObject productObject) {
        mProductionObject = productObject;

        final String value = productObject.getString(Constance.goods_desc);
        mIsLike = productObject.getInteger(Constance.is_liked);
        final String productName = productObject.getString(Constance.name);
        int category=productObject.getInteger(Constance.category);
        mCurrentPrice = productObject.getString(Constance.current_price);
        int is_jh=productObject.getInteger(Constance.is_jh);
        com.alibaba.fastjson.JSONArray attachArray = productObject.getJSONArray(Constance.attachments);
        ParamentAdapter mAdapter = new ParamentAdapter(attachArray, mView.getActivity());
        lv_paramter.setAdapter(mAdapter);

        int height=UIUtils.dip2PX(60)+UIUtils.initListViewHeight(lv_paramter);
        LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
        ll_paramter.setLayoutParams(layoutParams);
        com.alibaba.fastjson.JSONObject group_buy=productObject.getJSONObject(Constance.group_buy);
        ProDetailActivity.isXianGou=false;
        if(is_jh==1){
            if(group_buy!=null){
            int isFinish=group_buy.getInteger(Constance.is_finished);
            if(isFinish==0){
                ProDetailActivity.isXianGou=true;
            }
            }else {
                ProDetailActivity.isXianGou=false;
            }
        }
        if(is_jh==1){
            ProDetailActivity.isJuHao=true;
        }else {
            ProDetailActivity.isJuHao=false;
        }
        if(ProDetailActivity.isXianGou) {
            time_ll.setVisibility(View.VISIBLE);
            mOldPrice = productObject.getString(Constance.current_price);

        }else {
            time_ll.setVisibility(View.GONE);
            mOldPrice=Double.parseDouble(productObject.getString(Constance.current_price))*1.6+"";
        }
        JSONArray array = productObject.getJSONArray(Constance.photos);
        if (!AppUtils.isEmpty(array)) {
            for (int i = 0; i < array.size(); i++) {
                paths.add(array.getJSONObject(0).getString(Constance.large));
            }
        }

        getWebView(value);
        mConvenientBanner.setPages(
                new CBViewHolderCreator<NetworkImageHolderView>() {
                    @Override
                    public NetworkImageHolderView createHolder() {
                        return new NetworkImageHolderView();
                    }
                }, paths);
        mConvenientBanner.setPageIndicator(new int[]{R.drawable.dot_unfocuse, R.drawable.dot_focuse});
        proNameTv.setText(productName);

        selectCollect();
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(mView.getContext(), "my-db", null);
        DaoMaster daoMaster = new DaoMaster(devOpenHelper.getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        DbGoodsBeanDao dbGoodsBeanDao=daoSession.getDbGoodsBeanDao();
        DbGoodsBean dbGoodsBean=new DbGoodsBean();
        dbGoodsBean.setId(productObject.getInteger(Constance.id));
        dbGoodsBean.setG_id(Long.valueOf(productObject.getInteger(Constance.id)));
        dbGoodsBean.setName(productObject.getString(Constance.name));
        dbGoodsBean.setPrice(productObject.getString(Constance.price));
        dbGoodsBean.setCurrent_price(productObject.getString(Constance.current_price));
        dbGoodsBean.setCreate_time(DateUtils.getStrTime(System.currentTimeMillis()+""));
        dbGoodsBean.setOriginal_img(productObject.getJSONArray(Constance.photos).getJSONObject(0).getString(Constance.large));
        DecimalFormat df=new DecimalFormat("###.00");

        JSONArray propertieArray = productObject.getJSONArray(Constance.properties);
        if (propertieArray.size() > 0) {
            JSONArray attrsArray = propertieArray.getJSONObject(0).getJSONArray(Constance.attrs);
            final String name = propertieArray.getJSONObject(0).getString(Constance.name);
            if (!AppUtils.isEmpty(attrsArray)) {
                int price = attrsArray.getJSONObject(0).getInteger(Constance.attr_price);
                String parament = attrsArray.getJSONObject(0).getString(Constance.attr_name);

                double oldPrice = Double.parseDouble(mOldPrice);
                double currentPrice = price;
                mCurrentPrice=""+currentPrice;
                mOldPrice=df.format(Double.parseDouble(mCurrentPrice)*1.6);
                if(oldPrice>Double.parseDouble(mOldPrice)){
                unPriceTv.setText("￥" + oldPrice);
                unPriceTv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                }
                dbGoodsBean.setPrice(oldPrice+"");
                dbGoodsBean.setCurrent_price(""+currentPrice);
                proPriceTv.setText("￥" + currentPrice);
                mParamentTv.setText("已选 " + name + ":" + parament);
                dbGoodsBeanDao.insertOrReplace(dbGoodsBean);
                return;
            }
        }
        proPriceTv.setText("￥" + mCurrentPrice);
//        proPriceTv.setText("￥" + 00000);
//


        unPriceTv.setText("￥" + df.format(Double.parseDouble(mOldPrice)));
//        unPriceTv.setText("￥" + 00001);
        unPriceTv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
//        time_ll.setVisibility(View.GONE);
        cv_countdownView.setVisibility(View.GONE);
        remaining_num_tv.setVisibility(View.GONE);
        try {
            com.alibaba.fastjson.JSONObject jsonObject = productObject.getJSONObject(Constance.group_buy);
            if(AppUtils.isEmpty(jsonObject))return;
            String endTime = jsonObject.getString(Constance.end_time);
            String startTime = jsonObject.getString(Constance.start_time);
            cv_countdownView.setVisibility(View.VISIBLE);
            remaining_num_tv.setVisibility(View.VISIBLE);
//            time_ll.setVisibility(View.VISIBLE);
            long startTimeMillis = DateUtils.getTimeStamp(startTime, "yyyy-MM-dd HH:mm") - System.currentTimeMillis();
            if (startTimeMillis > 0) {
                isStartTimeBuy=false;
                time_title_tv.setText("距离开始: ");
                refreshTime(startTimeMillis);
            } else {
                isStartTimeBuy=true;
                long leftTime = DateUtils.getTimeStamp(endTime, "yyyy-MM-dd HH:mm") - System.currentTimeMillis();
                refreshTime(leftTime);
                time_title_tv.setText("距离结束: ");
            }

//            time_ll.setVisibility(View.VISIBLE);
            if(isStartTimeBuy){
                String restrictAmount = jsonObject.getJSONObject(Constance.ext_info).getString(Constance.restrict_amount);
                remaining_num_tv.setText("剩余 " + restrictAmount + " 件");
                JSONArray priceArray = jsonObject.getJSONObject(Constance.ext_info).getJSONArray(Constance.price_ladder);
                int price = priceArray.getJSONObject(0).getInteger(Constance.price);
                mCurrentPrice=price+"";
                proPriceTv.setText("￥" + price);
                proPriceTv.setVisibility(View.GONE);
                tv_time_price.setText("￥" + price);
                unPriceTv.setVisibility(View.GONE);
                tv_time_oldprice.setText("￥" + mOldPrice);
                dbGoodsBean.setCurrent_price(mCurrentPrice);
                dbGoodsBean.setPrice(mOldPrice+"");
                tv_time_oldprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                remaining_num_tv.setVisibility(View.VISIBLE);
            }else{
                remaining_num_tv.setVisibility(View.GONE);
            }


        } catch (Exception e) {
            e.printStackTrace();
//            time_ll.setVisibility(View.GONE);
        }finally {
        dbGoodsBeanDao.insertOrReplace(dbGoodsBean);
        }

    }

    public void refreshTime(long leftTime) {
        if (leftTime > 0) {
            cv_countdownView.start(leftTime);
        } else {
            cv_countdownView.stop();
            cv_countdownView.allShowZero();
        }
    }


    /**
     * 收藏图标状态
     */
    private void selectCollect() {
        if (mIsLike != 0) {
            collect_iv.setImageResource(R.mipmap.ic_collect_normal_new);
            tv_collect.setText("已收藏");
        } else {
            collect_iv.setImageResource(R.mipmap.ic_collect_press_new);
            tv_collect.setText("收藏");
        }
    }

    /**
     * 加载网页
     *
     * @param htmlValue
     */
    private void getWebView(String htmlValue) {
        String html = htmlValue;
        html = html.replace("src=\"", "src=\"" + NetWorkConst.SCENE_HOST);
        html = html.replace("</p>", "");
        html = html.replace("<p>", "");

        //        html = html.replace("<p><img src=\"", "<img src=\"" + NetWorkConst.SCENE_HOST);
        //        html = html.replace("</p>", "");


        html = "<meta name=\"viewport\" content=\"width=device-width\">" + html;
        mWebView.loadData(html, "text/html; charset=UTF-8", null);//这种写法可以正确解析中文
    }

    /**
     * 加入收藏
     */
    public void sendCollectGoods() {
        mView.setShowDialog(true);
        mView.setShowDialog("正在收藏中!");
        mView.showLoading();
        String id = mView.productId;
        if (mIsLike == 0) {
            mNetWork.sendAddLikeCollect(id, this);
            mIsLike = 1;
        } else {
            mNetWork.sendUnLikeCollect(id, this);
            mIsLike = 0;
        }
    }

    private SelectParamentPopWindow mPopWindow;

    /*
     * 选择参数
     */
    public void selectParament() {
        if (AppUtils.isEmpty(((ProDetailActivity) mView.getActivity()).mProductObject))
            return;
        mPopWindow = new SelectParamentPopWindow(mView.getActivity(), ((ProDetailActivity) mView.getActivity()).mProductObject);
        mPopWindow.onShow(rl_rl);
        mPopWindow.setListener(new IParamentChooseListener() {
            @Override
            public void onParamentChanged(String text, Boolean isGoCart, String property, int mount, int price) {
                if (!AppUtils.isEmpty(text)) {
                    mParamentTv.setText("已选 " + text);
                    ((ProDetailActivity) mView.getActivity()).mProperty = property;
                    ((ProDetailActivity) mView.getActivity()).mPrice = price;
                    proPriceTv.setText("￥" +price);
                    DecimalFormat df=new DecimalFormat("###.00");
                    unPriceTv.setText("￥" + df.format(price*1.6));
                    unPriceTv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

                }
                if (isGoCart == true) {
                    mView.setShowDialog(true);
                    mView.setShowDialog("正在加入购物车中...");
                    mView.showLoading();
                    mProperty = property;
                    sendGoShoppingCart(mView.productId, property, mount);
                }
            }
        });

    }


    private void sendGoShoppingCart(String product, String property, int mount) {

        mNetWork.sendShoppingCart(product, property, mount, this);
    }


    @Override
    public void onSuccessListener(String requestCode, JSONObject ans) {
        mView.hideLoading();
        switch (requestCode) {
            case NetWorkConst.ADDLIKEDPRODUCT:
                selectCollect();
                break;
            case NetWorkConst.ULIKEDPRODUCT:
                selectCollect();
                break;
            case NetWorkConst.ADDCART:
                MyToast.show(mView.getActivity(), UIUtils.getString(R.string.go_cart_ok));
                sendShoppingCart();
                break;
            case NetWorkConst.GETCART:
                if (ans.getJSONArray(Constance.goods_groups).length() > 0) {
                    IssueApplication.mCartCount = ans.getJSONArray(Constance.goods_groups)
                            .getJSONObject(0).getJSONArray(Constance.goods).length();
                } else {
                    IssueApplication.mCartCount = 0;
                }
                EventBus.getDefault().post(Constance.CARTCOUNT);

                ((ProDetailActivity) mView.getActivity()).mController.goCartAnimator(ans);
                break;
            case NetWorkConst.REVICE_PRODUCT_LIST_URL:
                if (null == mView)
                    return;
                bocang.json.JSONArray dataList = ans.getJSONArray(Constance.reviews);
                if (AppUtils.isEmpty(dataList) || dataList.length() == 0) {

                    String token= MyShare.get(mView.getContext()).getString(Constance.TOKEN);
                    if(TextUtils.isEmpty(token)){
                    ll_comment.setVisibility(View.GONE);
                    return;
                    }else {
                        JSONObject mUserObject=IssueApplication.mUserObject;
                        if(mUserObject!=null){
                            int level=mUserObject.getInt(Constance.level);
                            if(level==0){
                                ll_comment.setVisibility(View.VISIBLE);
                                rl_comment_cotent.removeAllViews();
                                View view=View.inflate(mView.getContext(),R.layout.view_sunimage,null);
                                LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                                view.setLayoutParams(layoutParams);
                                ImageView iv_head=view.findViewById(R.id.iv_avard);
                                GridView gv_goods=view.findViewById(R.id.gv_goods_img);
                                TextView tv_name=view.findViewById(R.id.tv_nickname);
                                TextView tv_time=view.findViewById(R.id.tv_time);
                                TextView tv_comment=view.findViewById(R.id.tv_comment);
//                                final JSONObject object = dataList.getJSONObject(0);
                                String avatar = NetWorkConst.SCENE_HOST + mUserObject.getString(Constance.avatar);
                                if (!AppUtils.isEmpty(avatar))
                                    ImageLoadProxy.displayHeadIcon(avatar, iv_head);
                                String name=mUserObject.getString(Constance.username);
//                    String name="测试";

//                                String time=DateUtils.getStrTime02(object.getString(Constance.updated_at));
//                    String time="2018-02-01 13：23";

                                String content="一级用户";
//                    String content="测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试";
//
//                                final bocang.json.JSONArray jsonArray = object.getJSONArray(Constance.path);
                                final ArrayList<String> mImageResList = new ArrayList<>();
//                    mImageResList.add("http://new.juhao.com/data/catthumb/1522638044910142634.jpg");
//                    mImageResList.add("http://new.juhao.com/data/catthumb/1522638044910142634.jpg");
//                    mImageResList.add("http://new.juhao.com/data/catthumb/1522638044910142634.jpg");
                                tv_name.setText(name);
                                tv_time.setVisibility(View.INVISIBLE);
                                tv_comment.setText(content);
                                mImageResList.add("");
                                mImageResList.add("");
                                mImageResList.add("");
                                SunImageAddAdapter mdapter = new SunImageAddAdapter(mView.getContext(), mImageResList);
                                gv_goods.setAdapter(mdapter);
                                gv_goods.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        if(position!=0){
                                            return;
                                        }
                                        Intent intent = new Intent(mView.getContext(), PostedImageActivity.class);
                                        intent.putExtra(Constance.id,mView.productId);
//                                        intent.putExtra(Constance.order_id, mOrderId);
                                        if(mProductionObject==null){
                                            MyToast.show(mView.getContext(),"数据加载中，请稍等");
                                            return;
                                        }
                                        intent.putExtra(Constance.goods,mProductionObject.getString(Constance.name));
                                        intent.putExtra(Constance.property, mProductionObject.getString(Constance.sku));
                                        intent.putExtra(Constance.order_id,"1");
                                        try {
                                            intent.putExtra(Constance.img, mProductionObject.
                                                    getJSONArray(Constance.photos).getJSONObject(0).getString(Constance.thumb));
                                        } catch (Exception e) {
                                            intent.putExtra(Constance.img, NetWorkConst.SHAREIMAGE);
                                        }
                                        mView.startActivity(intent);
                                    }
                                });
//                                view.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View view) {
//                                        Intent intent=new Intent(mView.getContext(), CommentHomeActivity.class);
//                                        IssueApplication.setCommentList(commentBeans);
//                                        mView.startActivity(intent);
//                                    }
//                                });
                                tv_more.setVisibility(View.INVISIBLE);
                                rl_comment_cotent.addView(view);

//                                View view=View.inflate(mView.getContext(),R.layout.view_sunimage_add,null);
//                                view.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        //
//                                        Intent intent = new Intent(mView.getContext(), PostedImageActivity.class);
//                                        intent.putExtra(Constance.id,mView.productId);
////                                        intent.putExtra(Constance.order_id, mOrderId);
//                                        if(mProductionObject==null){
//                                            MyToast.show(mView.getContext(),"数据加载中，请稍等");
//                                            return;
//                                        }
//                                        intent.putExtra(Constance.goods,mProductionObject.getString(Constance.name));
//                                        intent.putExtra(Constance.property, mProductionObject.getString(Constance.sku));
//                                        intent.putExtra(Constance.order_id,"1");
//                                        try {
//                                            intent.putExtra(Constance.img, mProductionObject.
//                                                    getJSONArray(Constance.photos).getJSONObject(0).getString(Constance.thumb));
//                                        } catch (Exception e) {
//                                            intent.putExtra(Constance.img, NetWorkConst.SHAREIMAGE);
//                                        }
//                                        mView.startActivity(intent);
//                                    }
//                                });
//                                RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                                layoutParams.setMargins(UIUtils.dip2PX(20),UIUtils.dip2PX(20),0,0);
//                                layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
//                                view.setLayoutParams(layoutParams);
//                                rl_comment_cotent.setPadding(UIUtils.dip2PX(150),UIUtils.dip2PX(50),0,0);
//                                rl_comment_cotent.addView(view);
                            }else {
                                ll_comment.setVisibility(View.GONE);
                                return;
                            }
                        }else {
                            ll_comment.setVisibility(View.GONE);
                            return;
                        }
                    }
//                    dismissRefesh();
                    return;
                }
                final ArrayList<CommentBean> commentBeans=new ArrayList<>();
                for(int i=0;i<dataList.length();i++){
                    commentBeans.add(new Gson().fromJson(dataList.getJSONObject(i).toString(),CommentBean.class));
                }
//                for(int i=0;i<10;i++) {
//                    CommentBean commentBean = new CommentBean();
//                    Author author = new Author();
//                    author.setAvatar("http://new.juhao.com/data/catthumb/1522638044910142634.jpg");
//                    author.setUsername("nickname");
//                    commentBean.setAuthor(author);
//                    commentBean.setUpdated_at("1522654338");
//                    commentBean.setContent("测试测试");
//                    List<String> paths = new ArrayList<>();
//                    paths.add("http://new.juhao.com/data/catthumb/1522638044910142634.jpg");
//                    paths.add("http://new.juhao.com/data/catthumb/1522638044910142634.jpg");
//                    paths.add("http://new.juhao.com/data/catthumb/1522638044910142634.jpg");
//                    commentBean.setPath(paths);
//                    commentBeans.add(commentBean);
//                }
                ll_comment.setVisibility(View.VISIBLE);
                rl_comment_cotent.removeAllViews();
                rl_comment_cotent.setPadding(0,0,0,0);
//                for(int i=0;i<dataList.length();i++){
//                    View view=View.inflate(mView.getContext(),R.layout.view_goods_tuijian,null);
//                    ImageView iv_img=view.findViewById(R.id.iv_img);
//                    TextView tv_name=view.findViewById(R.id.tv_name);
//                    TextView tv_c_price=view.findViewById(R.id.tv_c_price);
//                    TextView tv_o_price=view.findViewById(R.id.tv_o_price);
                    View view=View.inflate(mView.getContext(),R.layout.view_sunimage,null);
                    LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    view.setLayoutParams(layoutParams);
                    ImageView iv_head=view.findViewById(R.id.iv_avard);
                    GridView gv_goods=view.findViewById(R.id.gv_goods_img);
                    TextView tv_name=view.findViewById(R.id.tv_nickname);
                    TextView tv_time=view.findViewById(R.id.tv_time);
                    TextView tv_comment=view.findViewById(R.id.tv_comment);
                    final JSONObject object = dataList.getJSONObject(0);
                    JSONObject authorObject = object.getJSONObject(Constance.author);
                    authorObject.getString(Constance.avatar);
                    String avatar = NetWorkConst.SCENE_HOST + authorObject.getString(Constance.avatar);
                    if (!AppUtils.isEmpty(avatar))
                        ImageLoadProxy.displayHeadIcon(avatar, iv_head);
                    String name=authorObject.getString(Constance.username);
//                    String name="测试";

                    String time=DateUtils.getStrTime02(object.getString(Constance.updated_at));
//                    String time="2018-02-01 13：23";

                    String content=object.getString(Constance.content);
//                    String content="测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试";
//
                    final bocang.json.JSONArray jsonArray = object.getJSONArray(Constance.path);
                    bocang.json.JSONArray movie=object.getJSONArray(Constance.movie);
                    final ArrayList<String> mImageResList = new ArrayList<>();
                    for (int j = 0; j < jsonArray.length()&&j<3; j++) {
                        mImageResList.add(jsonArray.getString(j));
                    }
//                    mImageResList.add("http://new.juhao.com/data/catthumb/1522638044910142634.jpg");
//                    mImageResList.add("http://new.juhao.com/data/catthumb/1522638044910142634.jpg");
//                    mImageResList.add("http://new.juhao.com/data/catthumb/1522638044910142634.jpg");
                    tv_name.setText(name);
                    tv_time.setText(time);
                    tv_comment.setText(content);
                final List<PostImageVideoBean> postImageVideoBeans=new ArrayList<>();
                for(int i=0;i<movie.length();i++){
                    PostImageVideoBean temp=new PostImageVideoBean();
                    temp.isVideo=true;
                    temp.url=movie.getString(i);
                    postImageVideoBeans.add(temp);
                }
                for(int j=0;j<jsonArray.length();j++){
                    PostImageVideoBean temp=new PostImageVideoBean();
                    temp.isVideo=false;
                    temp.url=jsonArray.getString(j);
                    postImageVideoBeans.add(temp);
                }
                SunImageAddMovieAdapter mdapter = new SunImageAddMovieAdapter(mView.getContext(), postImageVideoBeans);
                    gv_goods.setAdapter(mdapter);
                    gv_goods.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String Token= MyShare.get(mView.getContext()).getString(Constance.TOKEN);
                            if(!TextUtils.isEmpty(Token)) {
                                JSONObject mUserObject= IssueApplication.mUserObject;
                                if(mUserObject!=null){
                                    int leve=mUserObject.getInt(Constance.level);
                                    if(leve==0){
                                        if(position==(postImageVideoBeans.size()>=3?2:postImageVideoBeans.size())){
                                            Intent intent = new Intent(mView.getContext(), PostedImageActivity.class);
                                            intent.putExtra(Constance.id,mView.productId);
//                                        intent.putExtra(Constance.order_id, mOrderId);
                                            if(mProductionObject==null){
                                                MyToast.show(mView.getContext(),"数据加载中，请稍等");
                                                return;
                                            }
                                            intent.putExtra(Constance.goods,mProductionObject.getString(Constance.name));
                                            intent.putExtra(Constance.property, mProductionObject.getString(Constance.sku));
                                            intent.putExtra(Constance.order_id,"1");
                                            try {
                                                intent.putExtra(Constance.img, mProductionObject.
                                                        getJSONArray(Constance.photos).getJSONObject(0).getString(Constance.thumb));
                                            } catch (Exception e) {
                                                intent.putExtra(Constance.img, NetWorkConst.SHAREIMAGE);
                                            }
                                            mView.startActivity(intent);
                                                    return;
                                        }
                                    }
                                }
                            }
                            if(position>=postImageVideoBeans.size()){
                                return;
                            }
                            if(postImageVideoBeans.get(position).isVideo){
                                final ProgressDialog pd = ProgressDialog.show(mView.getContext(),"","加载中");
                                ApiClient.downloadMp4(NetWorkConst.SCENE_HOST+postImageVideoBeans.get(position).url, new FileCallBack("","") {
                                    @Override
                                    public void onError(Call call, Exception e, int id) {
                                        if(UIUtils.isValidContext(mView.getActivity())&&pd!=null&&pd.isShowing()){
                                            pd.dismiss();
                                        }
                                        MyToast.show(mView.getContext(),"网络异常");
                                    }

                                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
                                    @Override
                                    public String onResponse(File response, int id) {
                                        if(UIUtils.isValidContext(mView.getActivity())&&pd!=null&&pd.isShowing()){
                                            pd.dismiss();
                                        }
                                        Intent intent = new Intent(mView.getContext(), SuccessVideoActivity.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putString("text", response.getAbsolutePath());
                                        bundle.putBoolean(Constance.is_look,true);
                                        intent.putExtras(bundle);
                                        mView.startActivity(intent);
                                        return null;
                                    }
                                });

                            }else {

                                Intent intent = new Intent(mView.getContext(), DetailPhotoActivity.class);
                                intent.putExtra(Constance.IMAGESHOW, mImageResList);
                                intent.putExtra(Constance.IMAGEPOSITION, 0);
                                mView.startActivity(intent);
                            }
                        }
                    });
                    view.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    Intent intent=new Intent(mView.getContext(), CommentHomeActivity.class);
                                                    IssueApplication.setCommentList(commentBeans);
                                                    mView.startActivity(intent);
                                                }
                                            });
                    tv_more.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent=new Intent(mView.getContext(), CommentHomeActivity.class);
                            IssueApplication.setCommentList(commentBeans);
                            mView.startActivity(intent);
                        }
                    });
                    rl_comment_cotent.addView(view);

                break;
        }
    }


    public void sendShoppingCart() {
        mNetWork.sendShoppingCart(this);
    }

    @Override
    public void onFailureListener(String requestCode, JSONObject ans) {
        mView.hideLoading();
        if (null == mView ||mView.getActivity()==null|| mView.getActivity().isFinishing())
            return;
        if (AppUtils.isEmpty(ans)) {
            AppDialog.messageBox(UIUtils.getString(R.string.server_error));
            return;
        }
        AppDialog.messageBox(ans.getString(Constance.error_desc));
    }

    @Override
    public void onEnd(CountdownView cv) {
        //时间到刷新界面
        try{
            ((ProDetailActivity)mView.getActivity()).mController.initViewData();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void send24Rexiao() {
        Random random=new Random();
        String sortKey="6";
        String sortValue="2";
        if(type==0){
            sortKey="7";
        }
        mNetWork.sendGoodsList(1, "8", null, null, null, null, "", sortKey, sortValue, new INetworkCallBack() {
            @Override
            public void onSuccessListener(String requestCode, JSONObject ans) {
                if (null == mView || mView.getActivity()==null||mView.getActivity().isFinishing())
                    return;
                final bocang.json.JSONArray goodsList = ans.getJSONArray(Constance.goodsList);
                ll_tuijian.removeAllViews();
                for(int i=0;i<goodsList.length();i++){
                View view=View.inflate(mView.getContext(),R.layout.view_goods_tuijian,null);
                    ImageView iv_img=view.findViewById(R.id.iv_img);
                    TextView tv_name=view.findViewById(R.id.tv_name);
                    TextView tv_c_price=view.findViewById(R.id.tv_c_price);
                    TextView tv_o_price=view.findViewById(R.id.tv_o_price);
                    tv_name.setText(goodsList.getJSONObject(i).getString(Constance.name));
                    ImageLoader.getInstance().displayImage(goodsList.getJSONObject(i).getJSONObject(Constance.default_photo).getString(Constance.large)
                            , iv_img);
                    tv_o_price.setText("￥" + goodsList.getJSONObject(i).getString(Constance.price));
                    tv_o_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                    tv_c_price.setText("￥" + goodsList.getJSONObject(i).getString(Constance.current_price));
                    final int finalI = i;
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent mIntent = new Intent(mView.getContext(), ProDetailActivity.class);
                            int productId = goodsList.getJSONObject(finalI).getInt(Constance.id);
                            mIntent.putExtra(Constance.product, productId);
//            mIntent.putExtra(Constance.is_xiangou,false);
                            mView.startActivity(mIntent);
                            mView.getActivity().finish();
                        }
                    });
                    LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    layoutParams.setMargins(15,15,15,15);
                    ll_tuijian.addView(view,layoutParams);
                }
//                getDataSuccess(goodsList);
            }

            @Override
            public void onFailureListener(String requestCode, JSONObject ans) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ll_24rexiao:
                type=1;
                tv_24rexiao.setTextColor(mView.getResources().getColor(R.color.green));
                tv_dajia.setTextColor(mView.getResources().getColor(R.color.txt_black));
                v_24rexiao.setVisibility(View.VISIBLE);
                v_dajia.setVisibility(View.GONE);
                send24Rexiao();
                break;
            case R.id.ll_dajia:
                type=0;
                tv_24rexiao.setTextColor(mView.getResources().getColor(R.color.txt_black));
                tv_dajia.setTextColor(mView.getResources().getColor(R.color.green));
                v_24rexiao.setVisibility(View.GONE);
                v_dajia.setVisibility(View.VISIBLE);
                send24Rexiao();
                break;
        }
    }

    class NetworkImageHolderView implements CBPageAdapter.Holder<String> {
        private ImageView imageView;

        @Override
        public View createView(Context context) {
            // 你可以通过layout文件来创建，也可以像我一样用代码创建，不一定是Image，任何控件都可以进行翻页
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            return imageView;
        }

        @Override
        public void UpdateUI(Context context, final int position, String data) {
            imageView.setImageResource(R.drawable.bg_default);
            ImageLoader.getInstance().displayImage(data, imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Toast.makeText(view.getContext(), "点击了第" + position + "个", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    protected void handleMessage(int action, Object[] values) {

    }

    @Override
    protected void myHandleMessage(Message msg) {

    }
}
