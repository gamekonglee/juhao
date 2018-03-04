package bc.juhao.com.controller.product;

import android.content.Context;
import android.graphics.Paint;
import android.os.Message;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.bigkoo.convenientbanner.CBPageAdapter;
import com.bigkoo.convenientbanner.CBViewHolderCreator;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import bc.juhao.com.R;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.cons.NetWorkConst;
import bc.juhao.com.controller.BaseController;
import bc.juhao.com.listener.INetworkCallBack;
import bc.juhao.com.listener.INetworkCallBack02;
import bc.juhao.com.listener.IParamentChooseListener;
import bc.juhao.com.ui.activity.IssueApplication;
import bc.juhao.com.ui.activity.product.ProDetailActivity;
import bc.juhao.com.ui.fragment.IntroduceGoodsFragment;
import bc.juhao.com.ui.view.countdownview.CountdownView;
import bc.juhao.com.ui.view.popwindow.SelectParamentPopWindow;
import bc.juhao.com.utils.DateUtils;
import bc.juhao.com.utils.UIUtils;
import bocang.json.JSONObject;
import bocang.utils.AppDialog;
import bocang.utils.AppUtils;
import bocang.utils.MyToast;

/**
 * @author: Jun
 * @date : 2017/2/14 17:59
 * @description :
 */
public class IntroduceGoodsController extends BaseController implements INetworkCallBack, CountdownView.OnCountdownEndListener {
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
    private RelativeLayout time_ll;
    private TextView time_title_tv;
    private Boolean isStartTimeBuy=false;

    public IntroduceGoodsController(IntroduceGoodsFragment v) {
        mView = v;
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
        time_ll = (RelativeLayout) mView.getActivity().findViewById(R.id.time_ll);
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
                ((ProDetailActivity) mView.getActivity()).mProductObject = ans.getJSONObject(Constance.product);
                getProductDetail(((ProDetailActivity) mView.getActivity()).mProductObject);
            }

            @Override
            public void onFailureListener(String requestCode, com.alibaba.fastjson.JSONObject ans) {
                mView.hideLoading();
                MyToast.show(mView.getActivity(), "网络异常，请重新加载!");
            }
        });

    }

    public String mCurrentPrice = "";
    public String mOldPrice = "";

    /**
     * 获取产品详情信息
     */
    private void getProductDetail(com.alibaba.fastjson.JSONObject productObject) {
        final String value = productObject.getString(Constance.goods_desc);
        mIsLike = productObject.getInteger(Constance.is_liked);
        final String productName = productObject.getString(Constance.name);
        int category=productObject.getInteger(Constance.category);
        mCurrentPrice = productObject.getString(Constance.current_price);
        com.alibaba.fastjson.JSONObject group_buy=productObject.getJSONObject(Constance.group_buy);
        ProDetailActivity.isXianGou=false;
        if(null!=group_buy&&!"212".equals(""+group_buy)){
            int isFinish=group_buy.getInteger(Constance.is_finished);
            if(isFinish==0){
                ProDetailActivity.isXianGou=true;
            }
        }
        if(212==category){
            ProDetailActivity.isJuHao=true;
        }else {
            ProDetailActivity.isJuHao=false;
        }
        if(ProDetailActivity.isXianGou) {
            time_ll.setVisibility(View.VISIBLE);
            mOldPrice = productObject.getString(Constance.current_price);
        }else {
            time_ll.setVisibility(View.GONE);
            mOldPrice=productObject.getString(Constance.price);
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

        JSONArray propertieArray = productObject.getJSONArray(Constance.properties);
        if (propertieArray.size() > 0) {
            JSONArray attrsArray = propertieArray.getJSONObject(0).getJSONArray(Constance.attrs);
            final String name = propertieArray.getJSONObject(0).getString(Constance.name);
            if (!AppUtils.isEmpty(attrsArray)) {
                int price = attrsArray.getJSONObject(0).getInteger(Constance.attr_price);
                String parament = attrsArray.getJSONObject(0).getString(Constance.attr_name);

                double oldPrice = Double.parseDouble(mOldPrice) + price;
                double currentPrice = Double.parseDouble(mCurrentPrice) + price;
                unPriceTv.setText("￥" + oldPrice);
                unPriceTv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                proPriceTv.setText("￥" + currentPrice);
                mParamentTv.setText("已选 " + name + ":" + parament);
                return;
            }
        }
        proPriceTv.setText("￥" + mCurrentPrice);
//        proPriceTv.setText("￥" + 00000);
//
        unPriceTv.setText("￥" + mOldPrice);
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
                proPriceTv.setText("￥" + price);
                remaining_num_tv.setVisibility(View.VISIBLE);
            }else{
                remaining_num_tv.setVisibility(View.GONE);
            }


        } catch (Exception e) {
            e.printStackTrace();
//            time_ll.setVisibility(View.GONE);
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
        if (mIsLike == 0) {
            collect_iv.setImageResource(R.drawable.ic_collect_normal);
        } else {
            collect_iv.setImageResource(R.drawable.ic_collect_press);
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
                    proPriceTv.setText("￥" + (Double.parseDouble(mCurrentPrice) + price));
                    unPriceTv.setText("￥" + (Double.parseDouble(mOldPrice) + price));

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
                            .getJSONObject(0).getInt(Constance.total_amount);
                } else {
                    IssueApplication.mCartCount = 0;
                }
                EventBus.getDefault().post(Constance.CARTCOUNT);

                ((ProDetailActivity) mView.getActivity()).mController.goCartAnimator(ans);
                break;
        }
    }


    public void sendShoppingCart() {
        mNetWork.sendShoppingCart(this);
    }

    @Override
    public void onFailureListener(String requestCode, JSONObject ans) {
        mView.hideLoading();
        if (null == mView || mView.getActivity().isFinishing())
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
                    Toast.makeText(view.getContext(), "点击了第" + position + "个", Toast.LENGTH_SHORT).show();
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
