package bc.juhao.com.controller;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.bigkoo.convenientbanner.CBPageAdapter;
import com.bigkoo.convenientbanner.CBViewHolderCreator;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.google.gson.Gson;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.lib.common.hxp.view.GridViewForScrollView;
import com.lib.common.hxp.view.PullToRefreshLayout;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import bc.juhao.com.R;
import bc.juhao.com.adapter.BaseAdapterHelper;
import bc.juhao.com.adapter.QuickAdapter;
import bc.juhao.com.bean.ArticlesBean;
import bc.juhao.com.bean.AttrBean;
import bc.juhao.com.bean.CategoriesBean;
import bc.juhao.com.bean.FangAnBean;
import bc.juhao.com.bean.GoodsBean;
import bc.juhao.com.bean.GroupBuy;
import bc.juhao.com.bean.TheNewGoodsBean;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.cons.NetWorkConst;
import bc.juhao.com.listener.INetworkCallBack;
import bc.juhao.com.ui.activity.ArtiActivity;
import bc.juhao.com.ui.activity.IssueApplication;
import bc.juhao.com.ui.activity.product.ProDetailActivity;
import bc.juhao.com.ui.activity.product.SelectGoodsActivity;
import bc.juhao.com.ui.activity.product.TimeBuyActivity;
import bc.juhao.com.ui.activity.user.LoginActivity;
import bc.juhao.com.ui.activity.user.MessageDetailActivity;
import bc.juhao.com.ui.activity.user.SetInviteCodeActivity;
import bc.juhao.com.ui.fragment.HomeFragment;
import bc.juhao.com.ui.view.countdownview.CountdownView;
import bc.juhao.com.utils.ConvertUtil;
import bc.juhao.com.utils.DateUtils;
import bc.juhao.com.utils.ImageUtil;
import bc.juhao.com.utils.MyShare;
import bc.juhao.com.utils.UIUtils;
import bocang.json.JSONArray;
import bocang.json.JSONObject;
import bocang.utils.AppUtils;
import bocang.utils.IntentUtil;
import bocang.utils.LogUtils;
import bocang.utils.MyToast;
import bocang.utils.UniversalUtil;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import me.leolin.shortcutbadger.ShortcutBadger;

import static bc.juhao.com.R.id.old_price_tv;

/**
 * @author Jun
 * @time 2017/1/7  20:31
 * @desc ${TODD}
 */
public class HomeController extends BaseController implements INetworkCallBack, AdapterView.OnItemClickListener, PullToRefreshLayout.OnRefreshListener {
    private final HomeFragment mView;
    private TextSwitcher textSwitcher_title;
    private int curStr;
    private List<String> paths = new ArrayList<>();
    private List<String> ImageLinks = new ArrayList<>();
    private ConvenientBanner mConvenientBanner;
    public int mScreenWidth;
    private GridView mProGridView;
    private int page = 1;
    private List<String> iDList;
    private TextView yaoqing_name;//当前被选中的tab
//    private ProAdapter mProAdapter;
    private RecommendProAdapter mReProAdapter;
    private JSONArray goodses;
    public JSONArray recommendGoodses;
    public JSONArray mPopularityGoodses;
    private PullToRefreshLayout mPullToRefreshLayout;
    private Intent mIntent;
    private JSONArray mArticlesArray;
    private ProgressBar pd;
    private GridView priductGv01;
    private RelativeLayout yaoqing_rl;
    private ImageView popularity_01_iv, popularity_02_iv, popularity_03_iv;
    private List<ArticlesBean> mArticlesBeans = new ArrayList<>();
    //限时购
    private GridViewForScrollView time_buy_lv;
    private TimeBuyProAdapter mTimeBuyAdapter;
    private JSONArray mTimeBuyDatas = new JSONArray();
    private LinearLayout time_buy_ll;
    private CountdownView cv_countdownView;
    private ArrayList<Long> mEndTimeArry = new ArrayList<>();
    private List<AttrBean> attrBeans;
    private List<CategoriesBean> categoriesBeans;
    private LinearLayout ll_supermacket;
    private GridView gv_home_lamb;
    private View mNullView;
    int fangAnPage=1;
    private QuickAdapter lamb_adapter;
    private List<TheNewGoodsBean> goodsBeanList;
    private QuickAdapter newGoodsAdapter;
    private LinearLayout ll_my_product;
    private LinearLayout ll_my_product_list;
    private QuickAdapter likeGoods;
    private TextView tv_home_news_more;
    private List<FangAnBean> fangAnBeans;

    public HomeController(HomeFragment v) {
        super();
        mView = v;
        initView();
        initData();
        initViewData();
        mView.setShowDialog(true);
    }

    private void initData() {
    }


    private void initView() {
        textSwitcher_title = (TextSwitcher) mView.getActivity().findViewById(R.id.textSwitcher_title);
        mProGridView = (GridView) mView.getActivity().findViewById(R.id.priductGridView);
        mProGridView.setOnItemClickListener(this);
        yaoqing_name = (TextView) mView.getActivity().findViewById(R.id.yaoqing_name);
        time_buy_ll = (LinearLayout) mView.getActivity().findViewById(R.id.time_buy_ll);
        yaoqing_rl = (RelativeLayout) mView.getActivity().findViewById(R.id.yaoqing_rl);
        cv_countdownView = (CountdownView) mView.getActivity().findViewById(R.id.cv_countdownView);

        mPullToRefreshLayout = ((PullToRefreshLayout) mView.getActivity().findViewById(R.id.refresh_view));
        mPullToRefreshLayout.setOnRefreshListener(this);

        ll_supermacket = mView.getActivity().findViewById(R.id.ll_supermacket);

        mScreenWidth = mView.getActivity().getResources().getDisplayMetrics().widthPixels;
        mConvenientBanner = (ConvenientBanner) mView.getActivity().findViewById(R.id.convenientBanner);
        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) mConvenientBanner.getLayoutParams();
        rlp.width = mScreenWidth;
        rlp.height = (int) (mScreenWidth * (360f / 800f));
        mConvenientBanner.setLayoutParams(rlp);
        pd = (ProgressBar) mView.getActivity().findViewById(R.id.pd);
        priductGv01 = (GridView) mView.getActivity().findViewById(R.id.priductGv01);
        priductGv01.setOnItemClickListener(this);
        popularity_01_iv = (ImageView) mView.getActivity().findViewById(R.id.popularity_01_iv);
        popularity_02_iv = (ImageView) mView.getActivity().findViewById(R.id.popularity_02_iv);
        popularity_03_iv = (ImageView) mView.getActivity().findViewById(R.id.popularity_03_iv);
        ll_my_product = mView.getView().findViewById(R.id.ll_my_product);
        ll_my_product_list = mView.getView().findViewById(R.id.ll_my_product_list);
        tv_home_news_more = mView.getView().findViewById(R.id.tv_home_news_more);
        tv_home_news_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mView.startActivity(new Intent(mView.getContext(), ArtiActivity.class));
            }
        });

//        mProAdapter = new ProAdapter();
//        mProGridView.setAdapter(mProAdapter);

        time_buy_lv = (GridViewForScrollView) mView.getActivity().findViewById(R.id.time_buy_lv);
        mTimeBuyAdapter = new TimeBuyProAdapter();
        time_buy_lv.setAdapter(mTimeBuyAdapter);
        time_buy_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!isToken()) {
                    IntentUtil.startActivity(mView.getActivity(), TimeBuyActivity.class, false);
                }
            }
        });
        gv_home_lamb = mView.getActivity().findViewById(R.id.gv_home_lamb);
        mNullView = mView.getActivity().findViewById(R.id.null_programe_view);
        lamb_adapter = new QuickAdapter<FangAnBean>(mView.getActivity(), R.layout.item_fangan){
            @Override
            protected void convert(BaseAdapterHelper helper, FangAnBean item) {
                helper.setText(R.id.tv_name,item.getName());
                helper.setText(R.id.tv_nickname,"作者:" + item.getNickname()+ "(" + item.getParent_name() + ")");
                ImageView iv=helper.getView(R.id.iv);
                ImageLoader.getInstance().displayImage( NetWorkConst.SCENE_HOST +item.getPath(),iv,((IssueApplication)mView.getActivity().getApplicationContext()).getImageLoaderOption());
            }
        };
        gv_home_lamb.setAdapter(lamb_adapter);
        gv_home_lamb.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(mView.getActivity(), MessageDetailActivity.class);
                String SceenId = fangAnBeans.get(i).getId()+"";
                intent.putExtra(Constance.url, NetWorkConst.SHAREFANAN + SceenId);
                intent.putExtra(Constance.FROMTYPE, 1);
                mView.startActivity(intent);
            }
        });
        newGoodsAdapter = new QuickAdapter<TheNewGoodsBean>(mView.getActivity(), R.layout.item_new_goods){

            @Override
            protected void convert(final BaseAdapterHelper helper, TheNewGoodsBean item) {
                helper.setText(R.id.tv_name,item.getName());
                helper.setText( R.id.tv_price,item.getCurrent_price());
                ImageView imageView=helper.getView(R.id.iv);
                ImageLoader.getInstance().displayImage(NetWorkConst.SCENE_HOST+item.getOriginal_img(),imageView,((IssueApplication)mView.getActivity().getApplicationContext()).getImageLoaderOption());
                helper.setOnClickListener(R.id.btn_buy, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int productId = goodsBeanList.get(helper.getPosition()).getId();
                        getMoreActivity(productId);
                    }
                });
            }
        };
        priductGv01.setAdapter(newGoodsAdapter);
        likeGoods = new QuickAdapter<GoodsBean>(mView.getActivity(), R.layout.item_like_goods){
            @Override
            protected void convert(BaseAdapterHelper helper, GoodsBean item) {

                helper.setText(R.id.tv_name,""+item.getName());
                helper.setText(R.id.tv_price,"¥"+item.getCurrent_price());
                ImageView imageView=helper.getView(R.id.iv);
                ImageLoader.getInstance().displayImage(NetWorkConst.SCENE_HOST+item.getOriginal_img(),imageView,((IssueApplication)mView.getActivity().getApplicationContext()).getImageLoaderOption());

            }
        };
        mProGridView.setAdapter(likeGoods);
    }

    /**
     * 判断是否有toKen
     */
    public Boolean isToken() {
        String token = MyShare.get(mView.getActivity()).getString(Constance.TOKEN);
        if (AppUtils.isEmpty(token)) {
            Intent logoutIntent = new Intent(mView.getActivity(), LoginActivity.class);
            logoutIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            mView.startActivity(logoutIntent);
            return true;
        }
        return false;
    }

    /**
     * 初始化加载数据
     */
    private void initViewData() {
        mView.setShowDialog(true);
        mView.showLoading();
        sendBanner();
        page = 1;
        sendRecommendGoodsList(1, 9, null, null, null, null, null, null);
        sendPopularityGoodsList(1, null, null, null, null, null, null);
        selectProduct(page, "20");

        if (AppUtils.isEmpty(mArticlesArray)) {
            sendArticle();
        }


        sendGoodsStyle();
        String token = MyShare.get(mView.getActivity()).getString(Constance.TOKEN);
        if (!AppUtils.isEmpty(token)) {
            sendUser();
        } else {
            String user_name = MyShare.get(mView.getActivity()).getString(Constance.USERCODE);
            if (AppUtils.isEmpty(user_name)) {
//                yaoqing_rl.setVisibility(View.VISIBLE);
            } else {
//                yaoqing_rl.setVisibility(View.VISIBLE);
                yaoqing_name.setText(user_name);
            }
        }
        refreshUIWithMessage();

        sendGrouplist(page);
        sendGroup(page);
//        sendAttrList();
        sendGoodsType();
        sendJuHaoGoodsList();
        sendFangAnList();

    }
    public void getRefershData() {
//        mView.setShowDialog(true);
//        mView.showLoading();
        fangAnPage = 1;
        sendFangAnList();
    }
    public void sendFangAnList() {
        mNetWork.sendFangAnList(fangAnPage, 20, "", "", mView.mProgrammeType, new INetworkCallBack() {
            @Override
            public void onSuccessListener(String requestCode, JSONObject ans) {

//                LogUtils.logE("fangan",ans.getJSONArray(Constance.fangan).toString());
                if (null == mView || mView.getActivity().isFinishing())
                    return;

                dismissRefesh();
                JSONArray goodsList2 = ans.getJSONArray(Constance.fangan);
                fangAnBeans = new ArrayList<>();
                for(int i=0;i<goodsList2.length();i++){
                    try {

                        fangAnBeans.add(new Gson().fromJson(String.valueOf(goodsList2.getJSONObject(i)),FangAnBean.class));
                    }catch (Exception e){
                        FangAnBean fangAnBean=new FangAnBean();
                        fangAnBean.setId(goodsList2.getJSONObject(i).getInt(Constance.id));
                        fangAnBean.setName(goodsList2.getJSONObject(i).getString(Constance.name));
                        fangAnBean.setPath(goodsList2.getJSONObject(i).getString(Constance.path));
                        fangAnBean.setNickname(goodsList2.getJSONObject(i).getString(Constance.nickname));
                        fangAnBean.setParent_name(goodsList2.getJSONObject(i).getString(Constance.parent_name));
                        fangAnBeans.add(fangAnBean);
                    }
                }
                if (AppUtils.isEmpty(goodsList2) || goodsList2.length() == 0) {
                    if (fangAnPage == 1) {
                        mNullView.setVisibility(View.VISIBLE);
                        gv_home_lamb.setVisibility(View.GONE);
//                        mView.mPullToRefreshLayout.isMove = true;
//                        go_btn.setVisibility(View.VISIBLE);
//                        go_btn.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                IntentUtil.startActivity(mView.getActivity(), DiyActivity.class, false);
//                            }
//                        });
//
//                        mSchemes = new JSONArray();
//                        dismissRefesh();
//                        pd.setVisibility(View.GONE);
                        return;
                    } else {
                        MyToast.show(mView.getActivity(), "数据已经到底!");
                    }
                }
                lamb_adapter.replaceAll(fangAnBeans);
                lamb_adapter.notifyDataSetChanged();

//                getDataSuccess2(fangAnBeans);
//                pd.setVisibility(View.GONE);
            }

            @Override
            public void onFailureListener(String requestCode, JSONObject ans) {

            }
        });
    }
    private void sendJuHaoGoodsList() {
        mNetWork.sendGoodsList(1, "" + 20, null, "212", null, null, null, null, null, new INetworkCallBack() {
            @Override
            public void onSuccessListener(String requestCode, JSONObject ans) {
                JSONArray goodsList=ans.getJSONArray(Constance.goodsList);
                final List<GoodsBean> goodsBeans=new ArrayList<>();
                for(int i=0;i<goodsList.length();i++){
                    try {
                    goodsBeans.add(new Gson().fromJson(String.valueOf(goodsList.getJSONObject(i)),GoodsBean.class));

                    }catch (Exception e){
                        GoodsBean goodsBean=new GoodsBean();
                        JSONObject goods=goodsList.getJSONObject(i);
                        goodsBean.setName(goods.getString(Constance.name));
                        goodsBean.setId(goods.getInt(Constance.id));
                        goodsBean.setCurrent_price(goods.getString(Constance.current_price));
                        goodsBean.setPrice(goods.getString(Constance.price));
                        goodsBean.setDefault_photo(new Gson().fromJson(String.valueOf(goods.getJSONObject(Constance.default_photo)), GoodsBean.Default_photo.class));
                        goodsBean.setGroup_buy(new Gson().fromJson(String.valueOf(goods.getJSONObject(Constance.group_buy)), GroupBuy.class));
                        goodsBeans.add(goodsBean);

                    }
                }
                if(goodsBeans==null||goodsBeans.size()<=0)return;
                for(int i=0;i<goodsBeans.size();i++){
                    View view=View.inflate(mView.getActivity(),R.layout.item_home_supermarket,null);
                    TextView tv_name=view.findViewById(R.id.tv_name);
                    TextView tv_price=view.findViewById(R.id.tv_price);
                    ImageView iv=view.findViewById(R.id.iv);
                    tv_name.setText(""+goodsBeans.get(i).getName());
                    if(goodsBeans.get(i).getGroup_buy()==null||goodsBeans.get(i).getGroup_buy().equals("212")){
                    tv_price.setText(""+goodsBeans.get(i).getCurrent_price());
                    }else {
                        if(goodsBeans.get(i).getGroup_buy().getExt_info()!=null&&goodsBeans.get(i).getGroup_buy().getExt_info().getPrice_ladder()!=null&&goodsBeans.get(i).getGroup_buy().getExt_info().getPrice_ladder().size()>0)
                        tv_price.setText("¥"+goodsBeans.get(i).getGroup_buy().getExt_info().getPrice_ladder().get(0).getPrice());
                    }
                    ImageLoader.getInstance().displayImage(goodsBeans.get(i).getDefault_photo().getLarge(),iv);
                    final int finalI = i;
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent=new Intent(mView.getContext(),ProDetailActivity.class);
                            intent.putExtra(Constance.product,goodsBeans.get(finalI).getId());
                            mView.startActivity(intent);

                        }
                    });
                    LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    layoutParams.setMargins(10,0,0,0);

                    ll_supermacket.addView(view,layoutParams);
                }
            }

            @Override
            public void onFailureListener(String requestCode, JSONObject ans) {

            }
        });
    }

    private void sendAttrList() {
        mNetWork.sendAttrList(new INetworkCallBack() {
            @Override
            public void onSuccessListener(String requestCode, JSONObject ans) {
                JSONArray jsonObject=ans.getJSONArray(Constance.goods_attr_list);
                attrBeans = new ArrayList<>();
                for(int i=0;i<jsonObject.length();i++){
                    attrBeans.add(new Gson().fromJson(String.valueOf(jsonObject.getJSONObject(i)),AttrBean.class));
                }

            }

            @Override
            public void onFailureListener(String requestCode, JSONObject ans) {

            }
        });
    }

    /**
     * 获取新品列表
     *
     * @param page
     * @param per_page
     * @param brand
     * @param category
     * @param shop
     * @param keyword
     * @param sort_key
     * @param sort_value
     */
    private void sendRecommendGoodsList(int page, int per_page, String brand, String category, String shop, String keyword, String sort_key, String sort_value) {
        mNetWork.sendRecommendGoodsList(page, per_page, brand, category, null, shop, keyword, sort_key, sort_value, this);
    }

    public void refreshTime(long leftTime) {
        if (leftTime > 0) {
            cv_countdownView.start(leftTime);
        } else {
            cv_countdownView.stop();
            cv_countdownView.allShowZero();
        }
    }


    private void sendGrouplist(int page) {
        mNetWork.sendGrouplist(page, "20", null, null, null, null, null, new INetworkCallBack() {
            @Override
            public void onSuccessListener(String requestCode, JSONObject ans) {
                mTimeBuyDatas = ans.getJSONArray(Constance.products);


                if (mTimeBuyDatas.length() == 0) {
                    time_buy_ll.setVisibility(View.GONE);
                } else {
                    for (int i = 0; i < mTimeBuyDatas.length(); i++) {
                        JSONObject jsonObject = mTimeBuyDatas.getJSONObject(i).getJSONObject(Constance.group_buy);
                        String endTime = jsonObject.getString(Constance.end_time);
                        Long endTimeStamp = DateUtils.getTimeStamp(endTime, "yyyy-MM-dd HH:mm");
                        if (!mEndTimeArry.contains(endTimeStamp)) {
                            mEndTimeArry.add(endTimeStamp);
                        }
                    }

                    Collections.sort(mEndTimeArry);


                    time_buy_ll.setVisibility(View.VISIBLE);
                    long leftTime = mEndTimeArry.get(0) - System.currentTimeMillis();
                    refreshTime(leftTime);

                }
                mTimeBuyAdapter.notifyDataSetChanged();


            }

            @Override
            public void onFailureListener(String requestCode, JSONObject ans) {

            }
        })
        ;
    }

    private void sendGroup(int page) {
        mNetWork.sendGroup(page, "20", new INetworkCallBack() {
            @Override
            public void onSuccessListener(String requestCode, JSONObject ans) {

            }

            @Override
            public void onFailureListener(String requestCode, JSONObject ans) {

            }
        })
        ;
    }


    /**
     * 获取人气产品列表
     *
     * @param page
     * @param brand
     * @param category
     * @param shop
     * @param keyword
     * @param sort_key
     * @param sort_value
     */
    private void sendPopularityGoodsList(int page, String brand, String category, String shop, String keyword, String sort_key, String sort_value) {
        mNetWork.sendGoodsList(page, 3 + "", brand, category, null, shop, keyword, "2", "1", new INetworkCallBack() {
                    @Override
                    public void onSuccessListener(String requestCode, JSONObject ans) {
                        switch (requestCode) {
                            case NetWorkConst.PRODUCT:
                                mPopularityGoodses = ans.getJSONArray(Constance.goodsList);
                                String image01Path = mPopularityGoodses.getJSONObject(0).getJSONObject(Constance.app_img).getString(Constance.phone_img);
//                                ImageLoader.getInstance().displayImage(image01Path, popularity_01_iv);
                                String image02Path = mPopularityGoodses.getJSONObject(1).getJSONObject(Constance.app_img).getString(Constance.phone_img);
//                                ImageLoader.getInstance().displayImage(image02Path, popularity_02_iv);
                                String image03Path = mPopularityGoodses.getJSONObject(2).getJSONObject(Constance.app_img).getString(Constance.phone_img);
//                                ImageLoader.getInstance().displayImage(image03Path, popularity_03_iv);
                                break;

                        }
                    }

                    @Override
                    public void onFailureListener(String requestCode, JSONObject ans) {

                    }
                }

        );
    }

    /**
     * 获取产品列表
     *
     * @param page
     * @param per_page
     */
    public void selectProduct(int page, String per_page) {
        mNetWork.sendGoodsList(page, per_page, null, null, null, null, null, null, null, this);
    }

    /**
     * 获取用户信息
     */

    public void sendUser() {
        mNetWork.sendUser(this);
    }

    /**
     * 产品类别
     */
    private void sendGoodsType() {
        mNetWork.sendGoodsType(1, 20, null, null, this);
    }


    private void sendGoodsStyle() {
        mNetWork.sendGoodsStyle(this);
    }

    /**
     * 滚动新闻
     */
    private void getNews() {
        try {

            textSwitcher_title.setFactory(new ViewSwitcher.ViewFactory() {
                @Override
                public View makeView() {
                    final TextView tv = new TextView(mView.getActivity());
                    tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                    tv.setGravity(Gravity.CENTER_VERTICAL);
                    tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String url = mArticlesBeans.get(mNewsPoistion).getUrl();
                            Intent intent = new Intent(mView.getActivity(), MessageDetailActivity.class);
                            intent.putExtra(Constance.url, url);
                            mView.startActivity(intent);
                        }
                    });
                    return tv;
                }
            });

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mNewsPoistion = curStr++ % mArticlesBeans.size();
                    textSwitcher_title.setText(mArticlesBeans.get(mNewsPoistion).getTitle());
                    handler.postDelayed(this, 5000);
                }
            }, 1000);
        } catch (Exception e) {

        }
    }


    private int mNewsPoistion = 0;


    private void sendBanner() {
        mNetWork.sendBanner(this)
        ;
    }

    /**
     * 广告图
     */
    private void getAd() {
        mConvenientBanner.setPages(
                new CBViewHolderCreator<NetworkImageHolderView>() {
                    @Override
                    public NetworkImageHolderView createHolder() {
                        return new NetworkImageHolderView();
                    }
                }, paths);
        mConvenientBanner.setPageIndicator(new int[]{R.drawable.dot_unfocuse, R.drawable.dot_focuse});
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (parent == mProGridView) {
            int productId = goodses.getJSONObject(position).getInt(Constance.id);
            getMoreActivity(productId);
        } else if (parent == priductGv01) {
//            int productId = recommendGoodses.getJSONObject(position).getInt(Constance.id);
            int productId = goodsBeanList.get(position).getId();
            getMoreActivity(productId);
        }
    }

    public void getMoreGoods(String id) {
        Intent intent = new Intent(mView.getActivity(), SelectGoodsActivity.class);
        intent.putExtra(Constance.categories, id);
        mView.getActivity().startActivity(intent);
    }

    public void getMoreGoodsByCategory(String cateValue) {

        if(categoriesBeans==null||categoriesBeans.size()==0){
            MyToast.show(mView.getActivity(),"数据加载中，请稍等");
            return;
        }
        int cateId=0;
        for(int x=0;x<categoriesBeans.size();x++){
            for(int y=0;y<categoriesBeans.get(x).getCategories().size();y++){
                if(categoriesBeans.get(x).getCategories().get(y).getName().contains(cateValue)){
                    cateId=categoriesBeans.get(x).getCategories().get(y).getId();
                }
            }
        }
        Intent intent = new Intent(mView.getActivity(), SelectGoodsActivity.class);
        intent.putExtra(Constance.categories, ""+cateId);
        mView.getActivity().startActivity(intent);
    }
    public void getMoreGoodsByFilter(String filter_attr) {

        if(attrBeans==null||attrBeans.size()==0){
            MyToast.show(mView.getActivity(),"数据加载中，请稍等");
            return;
        }
        int index=0;
        int attrIndex=0;
        for(int i=0;i<attrBeans.size();i++){
            for(int j=0;j<attrBeans.get(i).getAttr_list().size();j++){
                if(attrBeans.get(i).getAttr_list().get(j).getAttr_value().contains(filter_attr)){
                    index=attrBeans.get(i).getIndex();
                    attrIndex=j;
                    break;
                }
            }
        }
        filter_attr="";
        for(int x=0;x<attrBeans.size();x++){
            if(x==index){
                filter_attr+=attrBeans.get(x).getAttr_list().get(attrIndex).getId();
            }else {
                filter_attr+="0";
            }
            if(x!=attrBeans.size()-1){
                filter_attr+=".";
            }
        }
        Intent intent = new Intent(mView.getActivity(), SelectGoodsActivity.class);
        intent.putExtra(Constance.filter_attr, filter_attr);
        mView.getActivity().startActivity(intent);
    }

    public void getMore02Goods(int sort) {
        Intent intent = new Intent(mView.getActivity(), SelectGoodsActivity.class);
        intent.putExtra(Constance.sort, sort);
        mView.getActivity().startActivity(intent);
    }


    public void getMoreActivity(int id) {
        Intent intent = new Intent(mView.getActivity(), ProDetailActivity.class);
        int productId = id;
        intent.putExtra(Constance.product, productId);
        mView.getActivity().startActivity(intent);
    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        sendGoodsStyle();
        page = 1;
        selectProduct(page, "20");
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        sendGoodsStyle();
        page = page + 1;
        selectProduct(page, "20");
    }

    /**
     * 跳转到文章信息
     *
     * @param id
     */
    public void getArticleDetail(int id) {
        String url = NetWorkConst.ARTICLE_URL + id;
        Intent intent = new Intent(mView.getActivity(), MessageDetailActivity.class);
        intent.putExtra(Constance.url, url);
        mView.startActivity(intent);
    }


    class NetworkImageHolderView implements CBPageAdapter.Holder<String> {
        private ImageView imageView;

        @Override
        public View createView(Context context) {
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            return imageView;
        }

        @Override
        public void UpdateUI(Context context, final int position, String data) {

            imageView.setImageBitmap(ImageUtil.getBitmapById(mView.getActivity(),R.drawable.bg_default));
            ImageLoader.getInstance().displayImage(data, imageView,((IssueApplication)mView.getActivity().getApplication()).getImageLoaderOption());
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //跳转到知道的网址
                    String link = ImageLinks.get(position);
                    if (!AppUtils.isEmpty(link)) {
                        mIntent = new Intent();
                        mIntent.setAction(Intent.ACTION_VIEW);
                        mIntent.setData(Uri.parse(ImageLinks.get(position)));
                        mView.startActivity(mIntent);
                    }

                }
            });
        }
    }

    public void setResume() {
        // 开始自动翻页
        mConvenientBanner.startTurning(UniversalUtil.randomA2B(3000, 5000));
    }

    public void setPause() {
        // 停止翻页
        mConvenientBanner.stopTurning();
    }


    @Override
    public void onSuccessListener(String requestCode, JSONObject ans) {
        mView.hideLoading();
        switch (requestCode) {
            case NetWorkConst.RECOMMENDPRODUCT:
                getRecommendGoodsList(ans);
                break;
            case NetWorkConst.BANNER:
                if (null == mView.getActivity() || mView.getActivity().isFinishing())
                    return;
                JSONArray babbersArray = ans.getJSONArray(Constance.banners);
                for (int i = 0; i < babbersArray.length(); i++) {
                    try {
                        String imageUri = babbersArray.getJSONObject(i).getString(Constance.url);
                        paths.add(imageUri);
                        ImageLinks.add(babbersArray.getJSONObject(i).getString(Constance.link));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                getAd();
                break;
            case NetWorkConst.CATEGORY:
                JSONArray jsonArray=ans.getJSONArray(Constance.categories);
                categoriesBeans = new ArrayList<>();
                for(int i=0;i<jsonArray.length();i++){
                    categoriesBeans.add(new Gson().fromJson(String.valueOf(jsonArray.getJSONObject(i)),CategoriesBean.class));
                }

                break;
            case NetWorkConst.ARTICLELIST:
                mArticlesArray = ans.getJSONArray(Constance.articles);
                for (int i = 0; i < mArticlesArray.length(); i++) {
                    JSONObject jsonObject = mArticlesArray.getJSONObject(i);
                    if (jsonObject.getInt(Constance.article_type) == 1) {
                        int id = jsonObject.getInt(Constance.id);
                        String title = jsonObject.getString(Constance.title);
                        String url = jsonObject.getString(Constance.url);
                        mArticlesBeans.add(new ArticlesBean(id, title, url));
                    }
                }
                if (mArticlesBeans.size() == 0)
                    return;

                getNews();
                break;
            case NetWorkConst.PRODUCT:
                if (null == mView || mView.getActivity().isFinishing())
                    return;
                if (null != mPullToRefreshLayout) {
                    dismissRefesh();
                }

                JSONArray goodsList = ans.getJSONArray(Constance.goodsList);
                if (AppUtils.isEmpty(goodsList) || goodsList.length() == 0) {
                    if (page == 1) {
                    } else {
                        MyToast.show(mView.getActivity(), "没有更多数据了!");
                    }

                    dismissRefesh();
                    return;
                }

                getDataSuccess(goodsList);
                break;
            case NetWorkConst.PROFILE:
                mUserObject = ans.getJSONObject(Constance.user);
                IssueApplication.mUserObject = mUserObject;
                if (!AppUtils.isEmpty(mUserObject.getString("parent_id"))) {
                    if (mUserObject.getInt("parent_id") == 0) {
                        MyShare.get(mView.getActivity()).putInt(Constance.USERCODEID, mUserObject.getInt("id"));
                    } else {
                        MyShare.get(mView.getActivity()).putInt(Constance.USERCODEID, mUserObject.getInt("parent_id"));
                    }

                }
                if (!AppUtils.isEmpty(mUserObject.getString("parent_name"))) {
                    MyShare.get(mView.getActivity()).putString(Constance.USERCODE, mUserObject.getString("parent_name"));
                } else {
                    MyShare.get(mView.getActivity()).putString(Constance.USERCODE, mUserObject.getString("nickname"));
                }

                String user_name = MyShare.get(mView.getActivity()).getString(Constance.USERCODE);
                String name = mUserObject.getString(Constance.username);
                if (AppUtils.isEmpty(user_name)) {
//                    yaoqing_rl.setVisibility(View.VISIBLE);
                    yaoqing_name.setText(name);
                } else {
//                    yaoqing_rl.setVisibility(View.VISIBLE);
                    yaoqing_name.setText(user_name);
                }

                String aliasId = IssueApplication.mUserObject.getString(Constance.id);
                JPushInterface.setAlias(mView.getActivity(), aliasId, new TagAliasCallback() {
                    @Override
                    public void gotResult(int responseCode, String s, Set<String> set) {
                    }
                });
                int level = mUserObject.getInt(Constance.level);
                String inviteCode = mUserObject.getString(Constance.invite_code);
                MyShare.get(mView.getActivity()).putString(Constance.invite_code, inviteCode);
                if (level < 3) {
                    if (AppUtils.isEmpty(inviteCode)) {
                        IntentUtil.startActivity(mView.getActivity(), SetInviteCodeActivity.class, false);
                    }
                }
                if(level==0){
                    ll_my_product.setVisibility(View.VISIBLE);
                    mNetWork.selectYijiProduct(1,20+"",null,null,null,null,null,null,null,null,this);
                }else {
                    ll_my_product.setVisibility(View.GONE);
                }


                break;
            case NetWorkConst.PRODUCTYIJI:
                final JSONArray yijiProducts=ans.getJSONArray(Constance.products);
                if(yijiProducts==null||yijiProducts.length()==0){
                    ImageView imageView=new ImageView(mView.getActivity());
                    LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(UIUtils.dip2PX(75),UIUtils.dip2PX(75));
                    imageView.setImageDrawable(mView.getResources().getDrawable(R.drawable.bg_default));
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            MyToast.show(mView.getActivity(),"请上传你的产品");
                        }
                    });
                    ll_my_product_list.addView(imageView,params);
                    return;
                }
                ll_my_product_list.removeAllViews();

                for(int i=0;i<yijiProducts.length();i++){
                   ImageView imageView=new ImageView(mView.getActivity());
                    LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(UIUtils.dip2PX(75),UIUtils.dip2PX(75));
                   ImageLoader.getInstance().displayImage(NetWorkConst.SCENE_HOST+yijiProducts.getJSONObject(i).getString(Constance.original_img),imageView,((IssueApplication)mView.getActivity().getApplicationContext()).getImageLoaderOption());
                    final int finalI = i;
                    imageView.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View view) {
                           Intent intent=new Intent(mView.getContext(),ProDetailActivity.class);
                           int id= Integer.parseInt(yijiProducts.getJSONObject(finalI).getString(Constance.id));
                           intent.putExtra(Constance.product,id);
                           mView.startActivity(intent);
                       }
                   });
                   ll_my_product_list.addView(imageView,params);
                }
                break;


        }
    }



    private void getDataSuccess(JSONArray array) {
        List<GoodsBean> goodsBeans=new ArrayList<>();
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
        if (1 == page)
            goodses = array;
        else if (null != goodses) {
            for (int i = 0; i < array.length(); i++) {
                goodses.add(array.getJSONObject(i));
            }

            if (AppUtils.isEmpty(array))
                MyToast.show(mView.getActivity(), "没有更多内容了");
        }
        likeGoods.replaceAll(goodsBeans);
        likeGoods.notifyDataSetChanged();
//        mProAdapter.notifyDataSetChanged();
    }


    private void dismissRefesh() {
        mPullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
        mPullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
    }

    public JSONObject mUserObject;

    /**
     * 获取文章列表
     */
    private void sendArticle() {
        int page = 1;
        int per_page = 20;
        mNetWork.sendArticle(page, per_page, this);
    }


    /**
     * 获取最新产品
     *
     * @param ans
     */
    private void getRecommendGoodsList(JSONObject ans) {

        JSONArray goodsList = ans.getJSONArray(Constance.goodsList);
        goodsBeanList = new ArrayList<>();
        for(int i=0;i<goodsList.length();i++){
            try
            {
                goodsBeanList.add(new Gson().fromJson(String.valueOf(goodsList.getJSONObject(i)),TheNewGoodsBean.class));
            }catch (Exception e){
                TheNewGoodsBean temp=new TheNewGoodsBean();
                temp.setId(goodsList.getJSONObject(i).getInt(Constance.id));
                temp.setName(goodsList.getJSONObject(i).getString(Constance.name));
                temp.setDefault_photo(new Gson().fromJson(String.valueOf(goodsList.getJSONObject(i).getJSONObject(Constance.default_photo)), GoodsBean.Default_photo.class));
                temp.setOriginal_img(goodsList.getJSONObject(i).getString(Constance.original_img));
                temp.setPrice(goodsList.getJSONObject(i).getString(Constance.price));
                temp.setCurrent_price(goodsList.getJSONObject(i).getString(Constance.current_price));
                goodsBeanList.add(temp);
            }
        }

        if (1 == page)
            recommendGoodses = goodsList;
        else if (null != recommendGoodses) {
            for (int i = 0; i < goodsList.length(); i++) {
                recommendGoodses.add(goodsList.getJSONObject(i));
            }

            if (AppUtils.isEmpty(goodsList))
                MyToast.show(mView.getActivity(), "没有更多内容了");
        }

//        mReProAdapter = new RecommendProAdapter(recommendGoodses);
//        mReProAdapter.notifyDataSetChanged();
          newGoodsAdapter.replaceAll(goodsBeanList);
          newGoodsAdapter.notifyDataSetChanged();
//        priductGv01.setAdapter(mReProAdapter);
//        mReProAdapter.notifyDataSetChanged();
    }


    @Override
    public void onFailureListener(String requestCode, JSONObject ans) {
        if (null == mView.getActivity() || mView.getActivity().isFinishing())
            return;
        this.page--;
        if (null != mPullToRefreshLayout) {
            mPullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
            mPullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
        }

        if (requestCode.equals(NetWorkConst.CATEGORY)) {
            pd.setVisibility(View.INVISIBLE);
            mView.hideLoading();
        }
    }

    private class ProAdapter extends BaseAdapter {

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
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(mView.getActivity(), R.layout.item_gridview_product, null);

                holder = new ViewHolder();
                holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
                RelativeLayout.LayoutParams lLp = (RelativeLayout.LayoutParams) holder.imageView.getLayoutParams();
                float h = (mScreenWidth - ConvertUtil.dp2px(mView.getActivity(), 45.8f)) / 2;
                lLp.height = (int) h;
                holder.imageView.setLayoutParams(lLp);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            try {
                String imagePath = goodses.getJSONObject(position).getJSONObject(Constance.app_img).getString(Constance.phone_img);
                ImageLoader.getInstance().displayImage(imagePath, holder.imageView,((IssueApplication)mView.getActivity().getApplicationContext()).getImageLoaderOption());

            } catch (Exception e) {
                e.printStackTrace();
            }


            return convertView;
        }

        class ViewHolder {
            ImageView imageView;
        }
    }

    private class TimeBuyProAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if (null == mTimeBuyDatas)
                return 0;
            if (mTimeBuyDatas.length() > 4) {
                return 4;
            } else {
                return mTimeBuyDatas.length();
            }

        }

        @Override
        public JSONObject getItem(int position) {
            if (null == mTimeBuyDatas)
                return null;
            return mTimeBuyDatas.getJSONObject(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(mView.getActivity(), R.layout.item_gridview_time_buy, null);

                holder = new ViewHolder();
                holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
                holder.price_tv = (TextView) convertView.findViewById(R.id.price_tv);
                holder.old_price_tv = (TextView) convertView.findViewById(old_price_tv);
                RelativeLayout.LayoutParams lLp = (RelativeLayout.LayoutParams) holder.imageView.getLayoutParams();
                float h = (mScreenWidth - ConvertUtil.dp2px(mView.getActivity(), 45.8f)) / 3;
                lLp.height = (int) h;
                holder.imageView.setLayoutParams(lLp);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            try {
                String imagePath = mTimeBuyDatas.getJSONObject(position).getJSONObject(Constance.app_img).getString(Constance.phone_img);
                ImageLoader.getInstance().displayImage(imagePath, holder.imageView,((IssueApplication)mView.getActivity().getApplicationContext()).getImageLoaderOption());

            } catch (Exception e) {
                e.printStackTrace();
            }

            JSONArray propertieArray = mTimeBuyDatas.getJSONObject(position).getJSONArray(Constance.properties);
            if (!AppUtils.isEmpty(propertieArray)) {
                JSONArray attrsArray = propertieArray.getJSONObject(0).getJSONArray(Constance.attrs);
                int price = attrsArray.getJSONObject(0).getInt(Constance.attr_price);
                double oldPrice = Double.parseDouble(mTimeBuyDatas.getJSONObject(position).getString(Constance.price)) + price;
                double currentPrice = Double.parseDouble(mTimeBuyDatas.getJSONObject(position).getString(Constance.current_price)) + price;
                holder.old_price_tv.setText("￥" + oldPrice);
                holder.old_price_tv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                //                holder.price_tv.setText("￥" + currentPrice);
            } else {
                holder.old_price_tv.setText("￥" + mTimeBuyDatas.getJSONObject(position).getString(Constance.price));
                holder.old_price_tv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                //                holder.price_tv.setText("￥" + mTimeBuyDatas.getJSONObject(position).getString(Constance.current_price));
            }

            JSONArray priceArray = mTimeBuyDatas.getJSONObject(position).getJSONObject(Constance.group_buy).getJSONObject(Constance.ext_info).getJSONArray(Constance.price_ladder);
            int price = priceArray.getJSONObject(0).getInt(Constance.price);
            holder.price_tv.setText("￥" + price);
            return convertView;
        }

        class ViewHolder {
            ImageView imageView;
            TextView price_tv, old_price_tv;
            ;
        }
    }

    public int unreadMsgCount = 0;

    public void refreshUIWithMessage() {
        mView.getActivity().runOnUiThread(new Runnable() {
            public void run() {
                // refresh unread count
                for (EMConversation conversation : EMClient.getInstance().chatManager().getAllConversations().values()) {
                    unreadMsgCount = conversation.getUnreadMsgCount();
                }
                //获取此会话在本地的所有的消息数量
                //如果只是获取当前在内存的消息数量，调用
                IssueApplication.unreadMsgCount = unreadMsgCount;
                if (unreadMsgCount == 0) {
                    //                    ShortcutBadger.applyCount(this, badgeCount); //for 1.1.4+
                    mView.unMessageTv.setVisibility(View.GONE);
                    ShortcutBadger.applyCount(mView.getActivity(), 0);
                } else {
                    ShortcutBadger.applyCount(mView.getActivity(), unreadMsgCount); //for 1.1.4+
                    mView.unMessageTv.setVisibility(View.VISIBLE);
                    mView.unMessageTv.setText(unreadMsgCount + "");
                }
            }
        });
    }


    @Override
    protected void handleMessage(int action, Object[] values) {

    }

    @Override
    protected void myHandleMessage(Message msg) {

    }


    private class RecommendProAdapter extends BaseAdapter {
        private JSONArray mDatas;

        public RecommendProAdapter(JSONArray datas) {
            mDatas = datas;
        }

        @Override
        public int getCount() {
            if (null == mDatas)
                return 0;
            return mDatas.length();
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
                convertView = View.inflate(mView.getActivity(), R.layout.item_gridview_product, null);
                holder = new ViewHolder();
                holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
                RelativeLayout.LayoutParams lLp = (RelativeLayout.LayoutParams) holder.imageView.getLayoutParams();
                float h = (mScreenWidth - ConvertUtil.dp2px(mView.getActivity(), 45.8f)) / 3;
                lLp.height = (int) h;
                holder.imageView.setLayoutParams(lLp);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            try {
                String imagePath = mDatas.getJSONObject(position).getJSONObject(Constance.app_img).getString(Constance.phone_img);
                ImageLoader.getInstance().displayImage(imagePath, holder.imageView,((IssueApplication)mView.getActivity().getApplicationContext()).getImageLoaderOption());

            } catch (Exception e) {
                e.printStackTrace();
            }


            return convertView;
        }

        class ViewHolder {
            ImageView imageView;
        }
    }
}
