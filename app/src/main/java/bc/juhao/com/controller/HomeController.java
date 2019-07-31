package bc.juhao.com.controller;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
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

import com.aliyun.iot.ilop.demo.DemoApplication;
import com.bigkoo.convenientbanner.CBPageAdapter;
import com.bigkoo.convenientbanner.CBViewHolderCreator;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.google.gson.Gson;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.lib.common.hxp.view.GridViewForScrollView;
import com.lib.common.hxp.view.PullToRefreshLayout;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.text.DecimalFormat;
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
import bc.juhao.com.bean.Default_photo;
import bc.juhao.com.bean.FangAnBean;
import bc.juhao.com.bean.GoodsBean;
import bc.juhao.com.bean.GroupBuy;
import bc.juhao.com.bean.TheNewGoodsBean;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.cons.NetWorkConst;
import bc.juhao.com.listener.INetworkCallBack;
import bc.juhao.com.ui.activity.ArtiActivity;
import bc.juhao.com.ui.activity.NewsDetailActivity;
import bc.juhao.com.ui.activity.TaoCanHomeListActivity;
import bc.juhao.com.ui.activity.product.ProDetailActivity;
import bc.juhao.com.ui.activity.product.SelectGoodsActivity;
import bc.juhao.com.ui.activity.product.TimeBuyActivity;
import bc.juhao.com.ui.activity.user.MessageDetailActivity;
import bc.juhao.com.ui.activity.user.SetInviteCodeActivity;
import bc.juhao.com.ui.fragment.home.HomeFragment;
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
//    private int page = 1;
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
    private List<GoodsBean> goodsBeans;
    private LinearLayout ll_taocan;
    private TextView tv_more_taocan;
    private LinearLayout ll_xinpin;
    public ImageView iv_taocan;
    private ImageView iv_new_product;
    private ImageView iv_cnxh;
    private ImageView iv_tyd;
    private ImageView iv_diaodeng_product;
    private GridView gv_diaodeng;
    private QuickAdapter adapterDiaoDeng;
    private List<TheNewGoodsBean> diaodengList;
    public ImageView iv_xidingdeng_product;
    private GridView gv_xidingdeng;
    private List<TheNewGoodsBean> xidingList;
    private QuickAdapter adapterXdd;
    private ImageView iv_bideng_product;
    private ImageView iv_taideng_product;
    private ImageView iv_luodideng_product;
    private ImageView iv_diangong_product;
    private GridView gv_bideng;
    private GridView gv_taideng;
    private GridView gv_luodideng;
    private GridView gv_diangong;
    private List<TheNewGoodsBean> bidengList;
    private List<TheNewGoodsBean> taidengList;
    private List<TheNewGoodsBean> luodideng;
    private List<TheNewGoodsBean> diangong;
    private QuickAdapter adapterBideng;
    private QuickAdapter adapterTaiDeng;
    private QuickAdapter adapterLuoDiDeng;
    private QuickAdapter adapterDianGong;
    private String mDiaoDengTitleUrl;
    private String mXiDingDengUrl;
    private String mBiDeng;
    private String mTaiDeng;
    private String mLuoDiDeng;
    private String mDianGong;
    private String mChanPin;
    private String mZhuantiUrl;
    private String mCnxhUrl;
    private String mTyd;
    private String mTaoCan;
    private String mNewsProcut;
    DecimalFormat df=new DecimalFormat("#####.00");
    public  HomeController(HomeFragment v) {
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
        textSwitcher_title = (TextSwitcher) mView.getView().findViewById(R.id.textSwitcher_title);
        mProGridView = (GridView) mView.getView().findViewById(R.id.priductGridView);
        mProGridView.setOnItemClickListener(this);
        yaoqing_name = (TextView) mView.getView().findViewById(R.id.yaoqing_name);
        time_buy_ll = (LinearLayout) mView.getView().findViewById(R.id.time_buy_ll);
        yaoqing_rl = (RelativeLayout) mView.getView().findViewById(R.id.yaoqing_rl);
        cv_countdownView = (CountdownView) mView.getView().findViewById(R.id.cv_countdownView);

        mPullToRefreshLayout = ((PullToRefreshLayout) mView.getView().findViewById(R.id.refresh_view));
        mPullToRefreshLayout.setOnRefreshListener(this);

        ll_supermacket = mView.getView().findViewById(R.id.ll_supermacket);
        ll_taocan = mView.getView().findViewById(R.id.ll_taocan);
        mScreenWidth = mView.getActivity().getResources().getDisplayMetrics().widthPixels;
        mConvenientBanner = (ConvenientBanner) mView.getView().findViewById(R.id.convenientBanner);
        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) mConvenientBanner.getLayoutParams();
        rlp.width = mScreenWidth;
        rlp.height = (int) (mScreenWidth * (400f / 750f));
        mConvenientBanner.setLayoutParams(rlp);
        pd = (ProgressBar) mView.getView().findViewById(R.id.pd);
        priductGv01 = (GridView) mView.getView().findViewById(R.id.priductGv01);
        priductGv01.setOnItemClickListener(this);
        popularity_01_iv = (ImageView) mView.getView().findViewById(R.id.popularity_01_iv);
        popularity_02_iv = (ImageView) mView.getView().findViewById(R.id.popularity_02_iv);
        popularity_03_iv = (ImageView) mView.getView().findViewById(R.id.popularity_03_iv);
        ll_my_product = mView.getView().findViewById(R.id.ll_my_product);
        ll_my_product_list = mView.getView().findViewById(R.id.ll_my_product_list);
        tv_home_news_more = mView.getView().findViewById(R.id.tv_home_news_more);
        tv_home_news_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mView.startActivity(new Intent(mView.getActivity(), ArtiActivity.class));
            }
        });
        tv_more_taocan = mView.getView().findViewById(R.id.tv_more_taocan);
        tv_more_taocan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mView.startActivity(new Intent(mView.getActivity(), TaoCanHomeListActivity.class));
            }
        });
//        mProAdapter = new ProAdapter();
//        mProGridView.setAdapter(mProAdapter);

        time_buy_lv = (GridViewForScrollView) mView.getView().findViewById(R.id.time_buy_lv);
        mTimeBuyAdapter = new TimeBuyProAdapter();
        time_buy_lv.setAdapter(mTimeBuyAdapter);
        time_buy_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!mView.isToken()) {
                    IntentUtil.startActivity(mView.getActivity(), TimeBuyActivity.class, false);
                }
            }
        });
        gv_home_lamb = mView.getView().findViewById(R.id.gv_home_lamb);
        mNullView = mView.getView().findViewById(R.id.null_programe_view);
        lamb_adapter = new QuickAdapter<FangAnBean>(mView.getActivity(), R.layout.item_fangan){
            @Override
            protected void convert(BaseAdapterHelper helper, FangAnBean item) {
                helper.setText(R.id.tv_name,item.getName());
                helper.setText(R.id.tv_nickname,"作者:" + item.getNickname()+ "(" + item.getParent_name() + ")");
                ImageView iv=helper.getView(R.id.iv);
                ImageLoader.getInstance().displayImage( NetWorkConst.SCENE_HOST +item.getPath(),iv,((DemoApplication)mView.getActivity().getApplicationContext()).getImageLoaderOption());
            }
        };
        gv_home_lamb.setAdapter(lamb_adapter);
        gv_home_lamb.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(mView.getActivity(), MessageDetailActivity.class);
                String SceenId = fangAnBeans.get(i).getId()+"";
                intent.putExtra(Constance.url, NetWorkConst.SHAREFANAN_APP + SceenId);
                intent.putExtra(Constance.FROMTYPE, 1);
                mView.startActivity(intent);
            }
        });
        newGoodsAdapter = new QuickAdapter<TheNewGoodsBean>(mView.getActivity(), R.layout.item_new_goods){

            @Override
            protected void convert(final BaseAdapterHelper helper, TheNewGoodsBean item) {
                helper.setText(R.id.tv_name,item.getName());
                helper.setText( R.id.tv_price,"¥"+item.getCurrent_price());
                helper.setText(R.id.tv_old_price,"¥"+(df.format(Double.parseDouble(item.getCurrent_price())*1.6)));
                ((TextView)helper.getView(R.id.tv_old_price)).getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                ImageView imageView=helper.getView(R.id.iv);
                ImageLoader.getInstance().displayImage(item.getDefault_photo().getThumb(),imageView,((DemoApplication)mView.getActivity().getApplicationContext()).getImageLoaderOption());
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
                TextView tv_old_price=helper.getView(R.id.tv_old_price);
                helper.setText(R.id.tv_old_price,"¥"+(df.format(Double.parseDouble(item.getCurrent_price())*1.6)));
                tv_old_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                ImageView imageView=helper.getView(R.id.iv);
                ImageLoader.getInstance().displayImage(item.getDefault_photo().getThumb(),imageView,((DemoApplication)mView.getActivity().getApplicationContext()).getImageLoaderOption());

            }
        };
        mProGridView.setAdapter(likeGoods);
//        ll_xinpin = mView.getView().findViewById(R.id.ll_xinpin);
        iv_taocan = mView.getView().findViewById(R.id.iv_taocan);
        iv_new_product = mView.getView().findViewById(R.id.iv_new_product);
        iv_cnxh = mView.getView().findViewById(R.id.iv_cnxh);
        iv_tyd = mView.getView().findViewById(R.id.iv_tyd);
        iv_diaodeng_product = mView.getView().findViewById(R.id.iv_diaodeng_product);
        iv_xidingdeng_product = mView.getView().findViewById(R.id.iv_xidingdeng_product);
        iv_bideng_product = mView.getView().findViewById(R.id.iv_bideng_product);
        iv_taideng_product = mView.getView().findViewById(R.id.iv_taideng_product);
        iv_luodideng_product = mView.getView().findViewById(R.id.iv_luodideng_product);
        iv_diangong_product = mView.getView().findViewById(R.id.iv_diangong_product);

        gv_diaodeng = mView.getView().findViewById(R.id.gv_diaodeng);
        gv_xidingdeng = mView.getView().findViewById(R.id.gv_xidingdeng);
        gv_bideng = mView.getView().findViewById(R.id.gv_bideng);
        gv_taideng = mView.getView().findViewById(R.id.gv_taideng);
        gv_luodideng = mView.getView().findViewById(R.id.gv_luodideng);
        gv_diangong = mView.getView().findViewById(R.id.gv_diangong);

        diaodengList = new ArrayList<>();
        xidingList = new ArrayList<>();
        bidengList = new ArrayList<>();
        taidengList = new ArrayList<>();
        luodideng = new ArrayList<>();
        diangong = new ArrayList<>();

        adapterDiaoDeng = new QuickAdapter<TheNewGoodsBean>(mView.getActivity(), R.layout.item_new_goods){

            @Override
            protected void convert(final BaseAdapterHelper helper, TheNewGoodsBean item) {
                helper.setText(R.id.tv_name,item.getName());
                helper.setText( R.id.tv_price,"¥"+item.getCurrent_price());
                helper.setText(R.id.tv_old_price,"¥"+(df.format(Double.parseDouble(item.getCurrent_price())*1.6)));
                ((TextView)helper.getView(R.id.tv_old_price)).getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                ImageView imageView=helper.getView(R.id.iv);
                ImageLoader.getInstance().displayImage(item.getDefault_photo().getThumb(),imageView,((DemoApplication)mView.getActivity().getApplicationContext()).getImageLoaderOption());
                helper.setOnClickListener(R.id.btn_buy, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int productId = diaodengList.get(helper.getPosition()).getId();
                        getMoreActivity(productId);
                    }
                });
            }
        };
        adapterXdd = new QuickAdapter<TheNewGoodsBean>(mView.getActivity(), R.layout.item_new_goods){

            @Override
            protected void convert(final BaseAdapterHelper helper, TheNewGoodsBean item) {
                helper.setText(R.id.tv_name,item.getName());
                helper.setText( R.id.tv_price,"¥"+item.getCurrent_price());
                helper.setText(R.id.tv_old_price,"¥"+(df.format(Double.parseDouble(item.getCurrent_price())*1.6)));
                ((TextView)helper.getView(R.id.tv_old_price)).getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                ImageView imageView=helper.getView(R.id.iv);
                ImageLoader.getInstance().displayImage(item.getDefault_photo().getThumb(),imageView,((DemoApplication)mView.getActivity().getApplicationContext()).getImageLoaderOption());
                helper.setOnClickListener(R.id.btn_buy, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int productId = xidingList.get(helper.getPosition()).getId();
                        getMoreActivity(productId);
                    }
                });
            }
        };

        adapterBideng = new QuickAdapter<TheNewGoodsBean>(mView.getActivity(), R.layout.item_new_goods){

            @Override
            protected void convert(final BaseAdapterHelper helper, TheNewGoodsBean item) {
                helper.setText(R.id.tv_name,item.getName());
                helper.setText( R.id.tv_price,"¥"+item.getCurrent_price());
                helper.setText(R.id.tv_old_price,"¥"+(df.format(Double.parseDouble(item.getCurrent_price())*1.6)));
                ((TextView)helper.getView(R.id.tv_old_price)).getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                ImageView imageView=helper.getView(R.id.iv);
                ImageLoader.getInstance().displayImage(item.getDefault_photo().getThumb(),imageView,((DemoApplication)mView.getActivity().getApplicationContext()).getImageLoaderOption());
                helper.setOnClickListener(R.id.btn_buy, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int productId = bidengList.get(helper.getPosition()).getId();
                        getMoreActivity(productId);
                    }
                });
            }
        };
        adapterTaiDeng = new QuickAdapter<TheNewGoodsBean>(mView.getActivity(), R.layout.item_new_goods){

            @Override
            protected void convert(final BaseAdapterHelper helper, TheNewGoodsBean item) {
                helper.setText(R.id.tv_name,item.getName());
                helper.setText( R.id.tv_price,"¥"+item.getCurrent_price());
                helper.setText(R.id.tv_old_price,"¥"+(df.format(Double.parseDouble(item.getCurrent_price())*1.6)));
                ((TextView)helper.getView(R.id.tv_old_price)).getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                ImageView imageView=helper.getView(R.id.iv);
                ImageLoader.getInstance().displayImage(item.getDefault_photo().getThumb(),imageView,((DemoApplication)mView.getActivity().getApplicationContext()).getImageLoaderOption());
                helper.setOnClickListener(R.id.btn_buy, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int productId = taidengList.get(helper.getPosition()).getId();
                        getMoreActivity(productId);
                    }
                });
            }
        };
        adapterLuoDiDeng = new QuickAdapter<TheNewGoodsBean>(mView.getActivity(), R.layout.item_new_goods){

            @Override
            protected void convert(final BaseAdapterHelper helper, TheNewGoodsBean item) {
                helper.setText(R.id.tv_name,item.getName());
                helper.setText( R.id.tv_price,"¥"+item.getCurrent_price());
                helper.setText(R.id.tv_old_price,"¥"+(df.format(Double.parseDouble(item.getCurrent_price())*1.6)));
                ((TextView)helper.getView(R.id.tv_old_price)).getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                ImageView imageView=helper.getView(R.id.iv);
                ImageLoader.getInstance().displayImage(item.getDefault_photo().getThumb(),imageView,((DemoApplication)mView.getActivity().getApplicationContext()).getImageLoaderOption());
                helper.setOnClickListener(R.id.btn_buy, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int productId = luodideng.get(helper.getPosition()).getId();
                        getMoreActivity(productId);
                    }
                });
            }
        };
        adapterDianGong = new QuickAdapter<TheNewGoodsBean>(mView.getActivity(), R.layout.item_new_goods){

            @Override
            protected void convert(final BaseAdapterHelper helper, TheNewGoodsBean item) {
                helper.setText(R.id.tv_name,item.getName());
                helper.setText( R.id.tv_price,"¥"+item.getCurrent_price());
                helper.setText(R.id.tv_old_price,"¥"+(df.format(Double.parseDouble(item.getCurrent_price())*1.6)));
                ((TextView)helper.getView(R.id.tv_old_price)).getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                ImageView imageView=helper.getView(R.id.iv);
                ImageLoader.getInstance().displayImage(item.getDefault_photo().getThumb(),imageView,((DemoApplication)mView.getActivity().getApplicationContext()).getImageLoaderOption());
                helper.setOnClickListener(R.id.btn_buy, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int productId = diangong.get(helper.getPosition()).getId();
                        getMoreActivity(productId);
                    }
                });
            }
        };
        gv_diaodeng.setAdapter(adapterDiaoDeng);
        gv_xidingdeng.setAdapter(adapterXdd);
        gv_bideng.setAdapter(adapterBideng);
        gv_taideng.setAdapter(adapterTaiDeng);
        gv_luodideng.setAdapter(adapterLuoDiDeng);
        gv_diangong.setAdapter(adapterDianGong);

        gv_diaodeng.setOnItemClickListener(this);
        gv_xidingdeng.setOnItemClickListener(this);
        gv_bideng.setOnItemClickListener(this);
        gv_taideng.setOnItemClickListener(this);
        gv_luodideng.setOnItemClickListener(this);
        gv_diangong.setOnItemClickListener(this);

        iv_new_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(mView.getActivity(),SelectGoodsActivity.class);
                intent1.putExtra("news",true);
                mView.startActivity(intent1);
            }
        });
        iv_diaodeng_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(mView.getActivity(), SelectGoodsActivity.class);
                mIntent.putExtra(Constance.categories, "247");
                mView.startActivity(mIntent);
            }
        });
        iv_xidingdeng_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(mView.getActivity(), SelectGoodsActivity.class);
                mIntent.putExtra(Constance.categories, "248");
                mView.startActivity(mIntent);
            }
        });
        iv_bideng_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(mView.getActivity(), SelectGoodsActivity.class);
                mIntent.putExtra(Constance.categories, "263");
                mView.startActivity(mIntent);
            }
        });
        iv_taideng_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(mView.getActivity(), SelectGoodsActivity.class);
                mIntent.putExtra(Constance.categories, "230");
                mView.startActivity(mIntent);
            }
        });
        iv_luodideng_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(mView.getActivity(), SelectGoodsActivity.class);
                mIntent.putExtra(Constance.categories, "231");
                mView.startActivity(mIntent);
            }
        });
        iv_diangong_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(mView.getActivity(), SelectGoodsActivity.class);
                mIntent.putExtra(Constance.categories, "196");
                mView.startActivity(mIntent);
            }
        });
        iv_taocan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mView.startActivity(new Intent(mView.getActivity(),TaoCanHomeListActivity.class));
            }
        });
    }

//    /**
//     * 判断是否有toKen
//     */
//    public Boolean isToken() {
//        String token = MyShare.get(mView.getActivity()).getString(Constance.TOKEN);
//        if (AppUtils.isEmpty(token)) {
//            Intent logoutIntent = new Intent(mView.getActivity(), LoginActivity.class);
//            logoutIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//            mView.startActivity(logoutIntent);
//            return true;
//        }
//        return false;
//    }

    /**
     * 初始化加载数据
     */
    private void initViewData() {
        mView.setShowDialog(true);
        mView.showLoading();
        missionA();
//        missionB();
        String inviteCode= MyShare.get(mView.getActivity()).getString(Constance.invite_code);
        if(!TextUtils.isEmpty(inviteCode)&&inviteCode.equals("bocang")){
            ll_my_product.setVisibility(View.VISIBLE);
            mNetWork.selectYijiProduct(1,20+"",null,null,null,null,null,null,null,null,this);
        }
//        page = 1;
        setUserInfo();
        refreshUIWithMessage();
    }

    public void setUserInfo() {
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
    }

    public void missionB() {
//        mView.showLoading();

        ImageLoader.getInstance().loadImage(mDiaoDengTitleUrl, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {

            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                iv_diaodeng_product.setImageBitmap(bitmap);
                ImageLoader.getInstance().loadImage(mXiDingDengUrl, new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String s, View view) {

                    }

                    @Override
                    public void onLoadingFailed(String s, View view, FailReason failReason) {

                    }

                    @Override
                    public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                        iv_xidingdeng_product.setImageBitmap(bitmap);
//                        sendRecommendGoodsList(1, 4, null, null, null, null, null, null);
//                        sendJuHaoGoodsList();
                        sendDiaoDengListB();
                    }

                    @Override
                    public void onLoadingCancelled(String s, View view) {

                    }
                });
            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        });
//        ImageLoader.getInstance().displayImage(mDiaoDengTitleUrl,iv_diaodeng_product,DemoApplication.getImageLoaderOption());

//        ImageLoader.getInstance().displayImage(mXiDingDengUrl,iv_xidingdeng_product,DemoApplication.getImageLoaderOption());

//        sendDiaoDengList();
    }
    public void missionC(){
        ImageLoader.getInstance().loadImage(mBiDeng, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {

            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
            iv_bideng_product.setImageBitmap(bitmap);
            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        });
        ImageLoader.getInstance().loadImage(mTaiDeng, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {

            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
             iv_taideng_product.setImageBitmap(bitmap);
            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        });
        ImageLoader.getInstance().loadImage(mLuoDiDeng, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {

            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
            iv_luodideng_product.setImageBitmap(bitmap);
            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        });
        ImageLoader.getInstance().loadImage(mDianGong, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {

            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
            iv_diangong_product.setImageBitmap(bitmap);
//                ImageLoader.getInstance().displayImage(mZhuantiUrl,mView.iv_zhuanti,DemoApplication.getImageLoaderOption());
//                selectProduct(1, "20");
//                sendZhuanTi();
//                ImageLoader.getInstance().displayImage(mCnxhUrl,iv_cnxh,DemoApplication.getImageLoaderOption());
//                ImageLoader.getInstance().displayImage(mTyd,iv_tyd,DemoApplication.getImageLoaderOption());
                ImageLoader.getInstance().loadImage(mChanPin, new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String s, View view) {

                    }

                    @Override
                    public void onLoadingFailed(String s, View view, FailReason failReason) {

                    }

                    @Override
                    public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                    mView.iv_chanpin.setImageBitmap(bitmap);
                        sendDiaoDengListC();
                        sendFangAnList();

                    }

                    @Override
                    public void onLoadingCancelled(String s, View view) {

                    }
                });

            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        });

    }


    private void sendDiaoDengListC() {

        mNetWork.sendGoodsList(1, "4", null, "231", null, null, null,null, null, new INetworkCallBack() {
            @Override
            public void onSuccessListener(String requestCode, JSONObject ans) {
                JSONArray goodsList = ans.getJSONArray(Constance.goodsList);
                luodideng = new ArrayList<>();
//        ll_xinpin.removeAllViews();
                for(int i=0;i<goodsList.length()&&i<4;i++){
                    try
                    {
                        luodideng.add(new Gson().fromJson(String.valueOf(goodsList.getJSONObject(i)),TheNewGoodsBean.class));

                    }catch (Exception e){
                        TheNewGoodsBean temp=new TheNewGoodsBean();
                        temp.setId(goodsList.getJSONObject(i).getInt(Constance.id));
                        temp.setName(goodsList.getJSONObject(i).getString(Constance.name));
                        temp.setDefault_photo(new Gson().fromJson(String.valueOf(goodsList.getJSONObject(i).getJSONObject(Constance.default_photo)), Default_photo.class));
                        temp.setOriginal_img(goodsList.getJSONObject(i).getString(Constance.original_img));
                        Default_photo default_photo=new Default_photo();
                        if(goodsList.getJSONObject(i).getJSONObject(Constance.default_photo)!=null)default_photo.setThumb(goodsList.getJSONObject(i).getJSONObject(Constance.default_photo).getString(Constance.thumb));
                        temp.setDefault_photo(default_photo);
                        temp.setPrice(goodsList.getJSONObject(i).getString(Constance.price));
                        temp.setCurrent_price(goodsList.getJSONObject(i).getString(Constance.current_price));
                        luodideng.add(temp);
                    }

                    adapterLuoDiDeng.replaceAll(luodideng);
                    adapterLuoDiDeng.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailureListener(String requestCode, JSONObject ans) {

            }
        });

        mNetWork.sendGoodsList(1, "4", null, "196", null, null, null,null, null, new INetworkCallBack() {
            @Override
            public void onSuccessListener(String requestCode, JSONObject ans) {
                JSONArray goodsList = ans.getJSONArray(Constance.goodsList);
                diangong = new ArrayList<>();
//        ll_xinpin.removeAllViews();
                for(int i=0;i<goodsList.length()&&i<4;i++){
                    try
                    {
                        diangong.add(new Gson().fromJson(String.valueOf(goodsList.getJSONObject(i)),TheNewGoodsBean.class));

                    }catch (Exception e){
                        TheNewGoodsBean temp=new TheNewGoodsBean();
                        temp.setId(goodsList.getJSONObject(i).getInt(Constance.id));
                        temp.setName(goodsList.getJSONObject(i).getString(Constance.name));
                        temp.setDefault_photo(new Gson().fromJson(String.valueOf(goodsList.getJSONObject(i).getJSONObject(Constance.default_photo)), Default_photo.class));
                        temp.setOriginal_img(goodsList.getJSONObject(i).getString(Constance.original_img));
                        Default_photo default_photo=new Default_photo();
                        if(goodsList.getJSONObject(i).getJSONObject(Constance.default_photo)!=null)default_photo.setThumb(goodsList.getJSONObject(i).getJSONObject(Constance.default_photo).getString(Constance.thumb));
                        temp.setDefault_photo(default_photo);
                        temp.setPrice(goodsList.getJSONObject(i).getString(Constance.price));
                        temp.setCurrent_price(goodsList.getJSONObject(i).getString(Constance.current_price));
                        diangong.add(temp);
                    }

                    adapterDianGong.replaceAll(diangong);
                    adapterDianGong.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailureListener(String requestCode, JSONObject ans) {

            }
        });
    }

    public void missionA() {
        mView.showLoading();
        sendHomeImage();
    }

    private void sendDiaoDengList() {
        mNetWork.sendGoodsList(1, "4", null, "166", null, null, null,null, null, new INetworkCallBack() {
            @Override
            public void onSuccessListener(String requestCode, JSONObject ans) {
                mView.hideLoading();
                JSONArray goodsList = ans.getJSONArray(Constance.goodsList);
                diaodengList = new ArrayList<>();
//        ll_xinpin.removeAllViews();
                for(int i=0;i<goodsList.length()&&i<4;i++){
                    try
                    {
                        diaodengList.add(new Gson().fromJson(String.valueOf(goodsList.getJSONObject(i)),TheNewGoodsBean.class));

                    }catch (Exception e){
                        TheNewGoodsBean temp=new TheNewGoodsBean();
                        temp.setId(goodsList.getJSONObject(i).getInt(Constance.id));
                        temp.setName(goodsList.getJSONObject(i).getString(Constance.name));
                        temp.setDefault_photo(new Gson().fromJson(String.valueOf(goodsList.getJSONObject(i).getJSONObject(Constance.default_photo)), Default_photo.class));
                        temp.setOriginal_img(goodsList.getJSONObject(i).getString(Constance.original_img));
                        Default_photo default_photo=new Default_photo();
                        if(goodsList.getJSONObject(i).getJSONObject(Constance.default_photo)!=null)default_photo.setThumb(goodsList.getJSONObject(i).getJSONObject(Constance.default_photo).getString(Constance.thumb));
                        temp.setDefault_photo(default_photo);
                        temp.setPrice(goodsList.getJSONObject(i).getString(Constance.price));
                        temp.setCurrent_price(goodsList.getJSONObject(i).getString(Constance.current_price));
                        diaodengList.add(temp);
                    }

                    adapterDiaoDeng.replaceAll(diaodengList);
                    adapterDiaoDeng.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailureListener(String requestCode, JSONObject ans) {
                mView.hideLoading();
            }
        });
        mNetWork.sendGoodsList(1, "4", null, "170", null, null, null,null, null, new INetworkCallBack() {
            @Override
            public void onSuccessListener(String requestCode, JSONObject ans) {
                JSONArray goodsList = ans.getJSONArray(Constance.goodsList);
                xidingList = new ArrayList<>();
//        ll_xinpin.removeAllViews();
                for(int i=0;i<goodsList.length()&&i<4;i++){
                    try
                    {
                        xidingList.add(new Gson().fromJson(String.valueOf(goodsList.getJSONObject(i)),TheNewGoodsBean.class));

                    }catch (Exception e){
                        TheNewGoodsBean temp=new TheNewGoodsBean();
                        temp.setId(goodsList.getJSONObject(i).getInt(Constance.id));
                        temp.setName(goodsList.getJSONObject(i).getString(Constance.name));
                        temp.setDefault_photo(new Gson().fromJson(String.valueOf(goodsList.getJSONObject(i).getJSONObject(Constance.default_photo)), Default_photo.class));
                        temp.setOriginal_img(goodsList.getJSONObject(i).getString(Constance.original_img));
                        Default_photo default_photo=new Default_photo();
                        if(goodsList.getJSONObject(i).getJSONObject(Constance.default_photo)!=null)default_photo.setThumb(goodsList.getJSONObject(i).getJSONObject(Constance.default_photo).getString(Constance.thumb));
                        temp.setDefault_photo(default_photo);
                        temp.setPrice(goodsList.getJSONObject(i).getString(Constance.price));
                        temp.setCurrent_price(goodsList.getJSONObject(i).getString(Constance.current_price));
                        xidingList.add(temp);
                    }

                    adapterXdd.replaceAll(xidingList);
                    adapterXdd.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailureListener(String requestCode, JSONObject ans) {

            }
        });
    }

    private void sendDiaoDengListB() {
        mNetWork.sendGoodsList(1, "4", null, "263", null, null, null,null, null, new INetworkCallBack() {
            @Override
            public void onSuccessListener(String requestCode, JSONObject ans) {
                JSONArray goodsList = ans.getJSONArray(Constance.goodsList);
                bidengList = new ArrayList<>();
//        ll_xinpin.removeAllViews();
                for(int i=0;i<goodsList.length()&&i<4;i++){
                    try
                    {
                        bidengList.add(new Gson().fromJson(String.valueOf(goodsList.getJSONObject(i)),TheNewGoodsBean.class));

                    }catch (Exception e){
                        TheNewGoodsBean temp=new TheNewGoodsBean();
                        temp.setId(goodsList.getJSONObject(i).getInt(Constance.id));
                        temp.setName(goodsList.getJSONObject(i).getString(Constance.name));
                        temp.setDefault_photo(new Gson().fromJson(String.valueOf(goodsList.getJSONObject(i).getJSONObject(Constance.default_photo)), Default_photo.class));
                        temp.setOriginal_img(goodsList.getJSONObject(i).getString(Constance.original_img));
                        Default_photo default_photo=new Default_photo();
                        if(goodsList.getJSONObject(i).getJSONObject(Constance.default_photo)!=null)default_photo.setThumb(goodsList.getJSONObject(i).getJSONObject(Constance.default_photo).getString(Constance.thumb));
                        temp.setDefault_photo(default_photo);
                        temp.setPrice(goodsList.getJSONObject(i).getString(Constance.price));
                        temp.setCurrent_price(goodsList.getJSONObject(i).getString(Constance.current_price));
                        bidengList.add(temp);
                    }

                    adapterBideng.replaceAll(bidengList);
                    adapterBideng.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailureListener(String requestCode, JSONObject ans) {

            }
        });
        mNetWork.sendGoodsList(1, "4", null, "230", null, null, null,null, null, new INetworkCallBack() {
            @Override
            public void onSuccessListener(String requestCode, JSONObject ans) {
                JSONArray goodsList = ans.getJSONArray(Constance.goodsList);
                taidengList = new ArrayList<>();
//        ll_xinpin.removeAllViews();
                for(int i=0;i<goodsList.length()&&i<4;i++){
                    try
                    {
                        taidengList.add(new Gson().fromJson(String.valueOf(goodsList.getJSONObject(i)),TheNewGoodsBean.class));

                    }catch (Exception e){
                        TheNewGoodsBean temp=new TheNewGoodsBean();
                        temp.setId(goodsList.getJSONObject(i).getInt(Constance.id));
                        temp.setName(goodsList.getJSONObject(i).getString(Constance.name));
                        temp.setDefault_photo(new Gson().fromJson(String.valueOf(goodsList.getJSONObject(i).getJSONObject(Constance.default_photo)), Default_photo.class));
                        temp.setOriginal_img(goodsList.getJSONObject(i).getString(Constance.original_img));
                        Default_photo default_photo=new Default_photo();
                        if(goodsList.getJSONObject(i).getJSONObject(Constance.default_photo)!=null)default_photo.setThumb(goodsList.getJSONObject(i).getJSONObject(Constance.default_photo).getString(Constance.thumb));
                        temp.setDefault_photo(default_photo);
                        temp.setPrice(goodsList.getJSONObject(i).getString(Constance.price));
                        temp.setCurrent_price(goodsList.getJSONObject(i).getString(Constance.current_price));
                        taidengList.add(temp);
                    }

                    adapterTaiDeng.replaceAll(taidengList);
                    adapterTaiDeng.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailureListener(String requestCode, JSONObject ans) {

            }
        });
    }

    private void sendZhuanTi() {
        mNetWork.sendZhuanti(new INetworkCallBack() {
            @Override
            public void onSuccessListener(String requestCode, JSONObject ans) {
                LogUtils.logE("zhuanti",ans.toString());
                final JSONArray articles=ans.getJSONArray(Constance.articles);
                mView.ll_jingxuan.removeAllViews();
                for(int i=0;i<articles.length()&&i<4;i++){
                    View view=View.inflate(mView.getActivity(), R.layout.item_jingxuan,null);
                    LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    layoutParams.setMargins(15,0,0,0);
                    ImageView imageView=view.findViewById(R.id.iv);
                    TextView tv_title=view.findViewById(R.id.tv_title);
                    TextView tv_content=view.findViewById(R.id.tv_content);
                        tv_title.setText(articles.getJSONObject(i).getString(Constance.title));
                        tv_content.setText(articles.getJSONObject(i).getString(Constance.desc));
                        ImageLoader.getInstance().displayImage(NetWorkConst.SCENE_HOST+articles.getJSONObject(i).getString(Constance.file_url),imageView,((DemoApplication)mView.getActivity().getApplicationContext()).getImageLoaderOption());
                    final int finalI = i;
                    view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String url = articles.getJSONObject(finalI).getString(Constance.url);
                                Intent intent = new Intent(mView.getActivity(), NewsDetailActivity.class);
                                intent.putExtra(Constance.FROMTYPE,2);
                                intent.putExtra(Constance.url, url);
                                mView.startActivity(intent);
                            }
                        });
                    mView.ll_jingxuan.addView(view,layoutParams);
                }
            }

            @Override
            public void onFailureListener(String requestCode, JSONObject ans) {

            }
        });
    }

    private void sendHomeImage() {
        mNetWork.sendHomeIndex(new INetworkCallBack() {
            @Override
            public void onSuccessListener(String requestCode, JSONObject ans) {
                LogUtils.logE("banners,",ans.toString());
                JSONArray jsonArray=ans.getJSONArray(Constance.banners);

                for(int i=0;i<jsonArray.length();i++){
                    String url= NetWorkConst.SCENE_HOST+"/data/afficheimg/"+jsonArray.getJSONObject(i).getString(Constance.ad_code);
                    switch (i){
                        case 0:
                            mTaoCan = url;
//                            ImageLoader.getInstance().displayImage(url,iv_taocan,DemoApplication.getImageLoaderOption());
                            break;
                        case 1:
                            mNewsProcut = url;
//                            ImageLoader.getInstance().displayImage(url,iv_new_product,DemoApplication.getImageLoaderOption());
                            break;
                        case 2:
                            mDiaoDengTitleUrl = url;

                            break;
                        case 3:
                            mXiDingDengUrl = url;

                            break;
                        case 4:
                            mBiDeng = url;

                            break;
                        case 5:
                            mTaiDeng = url;

                            break;
                        case 6:
                            mLuoDiDeng = url;

                            break;
                        case 7:
                            mDianGong = url;

                            break;
                        case 8:
                            mChanPin = url;

                            break;
                        case 9:
                            mZhuantiUrl = url;

                            break;
                        case 10:
                            mCnxhUrl = url;
                            break;
                        case 11:
                            mTyd = url;
                            break;
                    }
                }
                ImageLoader.getInstance().loadImage(mTaoCan, new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String s, View view) {

                    }

                    @Override
                    public void onLoadingFailed(String s, View view, FailReason failReason) {

                    }

                    @Override
                    public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                        iv_taocan.setImageBitmap(bitmap);
                        ImageLoader.getInstance().loadImage(mNewsProcut, new ImageLoadingListener() {
                            @Override
                            public void onLoadingStarted(String s, View view) {

                            }

                            @Override
                            public void onLoadingFailed(String s, View view, FailReason failReason) {

                            }

                            @Override
                            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                                iv_new_product.setImageBitmap(bitmap);
                                sendTaoCanList();
                            }

                            @Override
                            public void onLoadingCancelled(String s, View view) {

                            }
                        });

                    }

                    @Override
                    public void onLoadingCancelled(String s, View view) {

                    }
                });

            }

            @Override
            public void onFailureListener(String requestCode, JSONObject ans) {

            }
        });
    }

    private void sendTaoCanList() {
        mNetWork.sendGoodsList(1, "10", null, "224", null, null, null, null, null, new INetworkCallBack() {
            @Override
            public void onSuccessListener(String requestCode, JSONObject ans) {
                ll_taocan.removeAllViews();
                final JSONArray goodsTc=ans.getJSONArray(Constance.products);
//                LogUtils.logE("goodstaocan",goodsTc.toString());
                int mScreenWidth=UIUtils.getScreenWidth(mView.getActivity());
                for(int i=0;i<5&&i<goodsTc.length();i++){
                    ImageView imageView=new ImageView(mView.getActivity());
                    ImageLoader.getInstance().displayImage(goodsTc.getJSONObject(i).getJSONObject(Constance.default_photo).getString(Constance.thumb)
                            , imageView, DemoApplication.getImageLoaderOption());
                    final int finalI = i;
                    LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(mScreenWidth-UIUtils.dip2PX(20),(mScreenWidth- UIUtils.dip2PX(20))*(170)/375);
                    layoutParams.setMargins(UIUtils.dip2PX(10),UIUtils.dip2PX(10),UIUtils.dip2PX(10),UIUtils.dip2PX(10));
                    imageView.setLayoutParams(layoutParams);
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent=new Intent(mView.getActivity(),ProDetailActivity.class);
                            intent.putExtra(Constance.product,goodsTc.getJSONObject(finalI).getInt(Constance.id));
                            mView.startActivity(intent);
                        }
                    });
                    ll_taocan.addView(imageView);

                }

                mView.hideLoading();
                sendBanner();
                if (AppUtils.isEmpty(mArticlesArray)) {
                    sendArticle();
                }

            }

            @Override
            public void onFailureListener(String requestCode, JSONObject ans) {

            }
        });
    }

    public void getRefershData() {
//        mView.setShowDialog(true);
//        mView.showLoading();
        fangAnPage = 1;
        if(fangAnBeans!=null&&fangAnBeans.size()>0){
            return;
        }
        sendFangAnList();
    }
    public void sendFangAnList() {
        mNetWork.sendFangAnList(1, 4, "", "", mView.mProgrammeType, new INetworkCallBack() {
            @Override
            public void onSuccessListener(String requestCode, JSONObject ans) {

//                LogUtils.logE("fangan",ans.getJSONArray(Constance.fangan).toString());
                if (null == mView ||mView.getActivity()==null|| mView.getActivity().isFinishing())
                    return;

                dismissRefesh();
                JSONArray goodsList2 = ans.getJSONArray(Constance.fangan);
                fangAnBeans = new ArrayList<>();
                for(int i=0;i<goodsList2.length()&&i<4;i++){
                    try {

                        fangAnBeans.add(new Gson().fromJson(String.valueOf(goodsList2.getJSONObject(i)),FangAnBean.class));
                    }catch (Exception e){
                        FangAnBean fangAnBean=new FangAnBean();
                        fangAnBean.setId(goodsList2.getJSONObject(i).getInt(Constance.id));
                        fangAnBean.setScene_id(goodsList2.getJSONObject(i).getInt(Constance.scene_id));
                        fangAnBean.setName(goodsList2.getJSONObject(i).getString(Constance.name));
                        fangAnBean.setPath(goodsList2.getJSONObject(i).getString(Constance.path));
                        fangAnBean.setNickname(goodsList2.getJSONObject(i).getString(Constance.nickname));
                        fangAnBean.setParent_name(goodsList2.getJSONObject(i).getString(Constance.parent_name));
                        fangAnBeans.add(fangAnBean);
                    }
                }
                if (AppUtils.isEmpty(goodsList2) || goodsList2.length() == 0) {
//                    if (fangAnPage == 1) {
//                        mNullView.setVisibility(View.VISIBLE);
//                        gv_home_lamb.setVisibility(View.GONE);
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
//                        return;
//                    } else {
//                        MyToast.show(mView.getActivity(), "数据已经到底!");
//                    }
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
                sendFangAnList();
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
                        goodsBean.setDefault_photo(new Gson().fromJson(String.valueOf(goods.getJSONObject(Constance.default_photo)), Default_photo.class));
                        goodsBean.setGroup_buy(new Gson().fromJson(String.valueOf(goods.getJSONObject(Constance.group_buy)), GroupBuy.class));
                        goodsBeans.add(goodsBean);

                    }
                }
                if(goodsBeans==null||goodsBeans.size()<=0)return;
                for(int i=0;i<goodsBeans.size();i++){
                    if(mView==null||mView.getActivity()==null||mView.getActivity().isFinishing())return;
                    View view=View.inflate(mView.getActivity(), R.layout.item_home_supermarket,null);
                    TextView tv_name=view.findViewById(R.id.tv_name);
                    TextView tv_price=view.findViewById(R.id.tv_price);
                    ImageView iv=view.findViewById(R.id.iv);
                    tv_name.setText(""+goodsBeans.get(i).getName());
                    if(goodsBeans.get(i).getGroup_buy()==null||goodsBeans.get(i).getGroup_buy().equals("212")){
                    tv_price.setText("¥"+goodsBeans.get(i).getCurrent_price());
                    }else {
                        if(goodsBeans.get(i).getGroup_buy().getExt_info()!=null&&goodsBeans.get(i).getGroup_buy().getExt_info().getPrice_ladder()!=null&&goodsBeans.get(i).getGroup_buy().getExt_info().getPrice_ladder().size()>0)
                        tv_price.setText("¥"+goodsBeans.get(i).getGroup_buy().getExt_info().getPrice_ladder().get(0).getPrice());
                    }
                    ImageLoader.getInstance().displayImage(goodsBeans.get(i).getDefault_photo().getThumb(),iv, DemoApplication.getImageLoaderOption());
                    final int finalI = i;
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent=new Intent(mView.getActivity(),ProDetailActivity.class);
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
        mNetWork.sendGoodsList(page, "" + per_page, brand, category, null, shop, keyword, "5", "2", new INetworkCallBack() {
            @Override
            public void onSuccessListener(String requestCode, JSONObject ans) {
                getNewGoodsList(ans);
            }

            @Override
            public void onFailureListener(String requestCode, JSONObject ans) {

            }
        });
    }

    public void refreshTime(long leftTime) {
        if (leftTime > 0) {
            cv_countdownView.start(leftTime);
        } else {
            cv_countdownView.stop();
            cv_countdownView.allShowZero();
        }
    }


    private void sendTimeBuylist(int page) {
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

//    private void sendGroup(int page) {
//        mNetWork.sendGroup(page, "20", new INetworkCallBack() {
//            @Override
//            public void onSuccessListener(String requestCode, JSONObject ans) {
//
//            }
//
//            @Override
//            public void onFailureListener(String requestCode, JSONObject ans) {
//
//            }
//        })
//        ;
//    }


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
//        Random random=new Random();
        String sortKey="6";
        String sortValue="2";
        mNetWork.sendGoodsList(page, per_page, null, null, null, null, null, sortKey, sortValue , this);
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
                    tv.setLines(1);
                    tv.setEllipsize(TextUtils.TruncateAt.END);
                    tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String url = mArticlesBeans.get(mNewsPoistion).getUrl();
                            Intent intent = new Intent(mView.getActivity(), NewsDetailActivity.class);
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
        mNetWork.sendBanner(this);
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
        sendRecommendGoodsList(1,4,null,null,null,null,null,null);
        sendDiaoDengList();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (parent == mProGridView) {
            int productId = goodsBeans.get(position).getId();
            getMoreActivity(productId);
        } else if (parent == priductGv01) {
//            int productId = recommendGoodses.getJSONObject(position).getInt(Constance.id);
            int productId = goodsBeanList.get(position).getId();
            getMoreActivity(productId);
        }else if(parent==gv_diaodeng){
            int productId = diaodengList.get(position).getId();
            getMoreActivity(productId);
        }else if(parent==gv_xidingdeng){
            int productId = xidingList.get(position).getId();
            getMoreActivity(productId);
        }else if(parent==gv_bideng){
            int productId = bidengList.get(position).getId();
            getMoreActivity(productId);
        }else if(parent==gv_taideng){
            int productId = taidengList.get(position).getId();
            getMoreActivity(productId);
        }else if(parent==gv_luodideng){
            int productId = luodideng.get(position).getId();
            getMoreActivity(productId);
        }else if(parent==gv_diangong){
            int productId = diangong.get(position).getId();
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
//        page = 1;
        selectProduct(1, "20");
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
//        sendGoodsStyle();
//        page = page + 1;
//        selectProduct(1, "20");
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

    public void missionD() {
        ImageLoader.getInstance().displayImage(mZhuantiUrl,mView.iv_zhuanti, DemoApplication.getImageLoaderOption());
        selectProduct(1, "20");
        sendZhuanTi();
        ImageLoader.getInstance().displayImage(mCnxhUrl,iv_cnxh, DemoApplication.getImageLoaderOption());
        ImageLoader.getInstance().displayImage(mTyd,iv_tyd, DemoApplication.getImageLoaderOption());
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

            imageView.setImageBitmap(ImageUtil.getBitmapById(mView.getActivity(), R.drawable.bg_default));
            ImageLoader.getInstance().displayImage(data, imageView,((DemoApplication)mView.getActivity().getApplication()).getImageLoaderOption());
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
//                sendPopularityGoodsList(1, null, null, null, null, null, null);
//                sendGoodsType();
                getNewGoodsList(ans);
                break;
            case NetWorkConst.BANNER:
                if (null == mView.getActivity() ||mView.getActivity()==null|| mView.getActivity().isFinishing())
                    return;

                JSONArray babbersArray = ans.getJSONArray(Constance.banners);
                paths=new ArrayList<>();
                ImageLinks=new ArrayList<>();
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
//                sendJuHaoGoodsList();
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
                if (null == mView || mView.getActivity()==null||mView.getActivity().isFinishing())
                    return;

                if (null != mPullToRefreshLayout) {
                    dismissRefesh();
                }
//                sendBanner();
//                sendGoodsStyle();

//                sendGroup(page);
//              sendAttrList();
                JSONArray goodsList = ans.getJSONArray(Constance.goodsList);
//                if (AppUtils.isEmpty(goodsList) || goodsList.length() == 0) {
//                    if (page == 1) {
//                    } else {
//                        MyToast.show(mView.getActivity(), "没有更多数据了!");
//                    }
//
//                    dismissRefesh();
//                    return;
//                }

                getDataSuccess(goodsList);
                break;
            case NetWorkConst.PROFILE:
                mUserObject = ans.getJSONObject(Constance.user);
                DemoApplication.mUserObject = mUserObject;
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

                String aliasId = DemoApplication.mUserObject.getString(Constance.id);
                if(mView.getActivity()==null||mView.getActivity().isFinishing())return;
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

                    ll_my_product.setVisibility(View.VISIBLE);
                    mNetWork.selectYijiProduct(1,20+"",null,null,null,null,null,null,null,null,this);
//                if(level==0){
//                }else {
//                    ll_my_product.setVisibility(View.GONE);
//                }


                break;
            case NetWorkConst.PRODUCTYIJI:
                final JSONArray yijiProducts=ans.getJSONArray(Constance.products);
                if(yijiProducts==null||yijiProducts.length()==0){
                    ImageView imageView=new ImageView(mView.getActivity());
                    imageView.setBackgroundColor(Color.WHITE);
                    LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(UIUtils.dip2PX(mView.getActivity(),75),UIUtils.dip2PX(mView.getActivity(),75));
                    params.setMargins(UIUtils.dip2PX(15),UIUtils.dip2PX(15),0,UIUtils.dip2PX(15));
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
                    imageView.setBackgroundColor(Color.WHITE);
                    LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(UIUtils.dip2PX(mView.getActivity(),75),UIUtils.dip2PX(mView.getActivity(),75));
                    params.setMargins(UIUtils.dip2PX(10),UIUtils.dip2PX(10),0,UIUtils.dip2PX(10));
                   ImageLoader.getInstance().displayImage(NetWorkConst.SCENE_HOST+yijiProducts.getJSONObject(i).getString(Constance.original_img),imageView,((DemoApplication)mView.getActivity().getApplicationContext()).getImageLoaderOption());
                    final int finalI = i;
                    imageView.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View view) {
                           Intent intent=new Intent(mView.getActivity(),ProDetailActivity.class);
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
        goodsBeans = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            try {

                goodsBeans.add(new Gson().fromJson(String.valueOf(array.getJSONObject(i)),GoodsBean.class));
            }catch (Exception e){
                GoodsBean goodsBean=new GoodsBean();
                goodsBean.setId(array.getJSONObject(i).getInt(Constance.id));
                goodsBean.setName(array.getJSONObject(i).getString(Constance.name));
                goodsBean.setCurrent_price(array.getJSONObject(i).getString(Constance.current_price));
                goodsBean.setPrice(array.getJSONObject(i).getString(Constance.price));
                goodsBean.setOriginal_img(array.getJSONObject(i).getString(Constance.original_img));
                Default_photo default_photo=new Default_photo();
                if(array.getJSONObject(i).getJSONObject(Constance.default_photo)!=null){
                default_photo.setThumb(array.getJSONObject(i).getJSONObject(Constance.default_photo).getString(Constance.thumb));
                }
                goodsBean.setDefault_photo(default_photo);
                goodsBeans.add(goodsBean);
            }
        }
//        if (1 == page)
//            goodses = array;
//        else if (null != goodses) {
//            for (int i = 0; i < array.length(); i++) {
//                goodses.add(array.getJSONObject(i));
//            }
//
//            if (AppUtils.isEmpty(array))
//                MyToast.show(mView.getActivity(), "没有更多内容了");
//        }
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
    private void getNewGoodsList(JSONObject ans) {

        JSONArray goodsList = ans.getJSONArray(Constance.goodsList);
        goodsBeanList = new ArrayList<>();
//        ll_xinpin.removeAllViews();
        for(int i=0;i<goodsList.length()&&i<4;i++){
            try
            {
                goodsBeanList.add(new Gson().fromJson(String.valueOf(goodsList.getJSONObject(i)),TheNewGoodsBean.class));

            }catch (Exception e){
                TheNewGoodsBean temp=new TheNewGoodsBean();
                temp.setId(goodsList.getJSONObject(i).getInt(Constance.id));
                temp.setName(goodsList.getJSONObject(i).getString(Constance.name));
                temp.setDefault_photo(new Gson().fromJson(String.valueOf(goodsList.getJSONObject(i).getJSONObject(Constance.default_photo)), Default_photo.class));
                temp.setOriginal_img(goodsList.getJSONObject(i).getString(Constance.original_img));
                Default_photo default_photo=new Default_photo();
                if(goodsList.getJSONObject(i).getJSONObject(Constance.default_photo)!=null)default_photo.setThumb(goodsList.getJSONObject(i).getJSONObject(Constance.default_photo).getString(Constance.thumb));
                temp.setDefault_photo(default_photo);
                temp.setPrice(goodsList.getJSONObject(i).getString(Constance.price));
                temp.setCurrent_price(goodsList.getJSONObject(i).getString(Constance.current_price));
                goodsBeanList.add(temp);
            }
//            View view=View.inflate(mView.getActivity(),R.layout.view_goods_xinpin,null);
//            ImageView iv_img=view.findViewById(R.id.iv_img);
//            TextView tv_name=view.findViewById(R.id.tv_name);
//            TextView tv_c_price=view.findViewById(R.id.tv_c_price);
//            TextView tv_o_price=view.findViewById(R.id.tv_o_price);
//            tv_name.setText(goodsList.getJSONObject(i).getString(Constance.name));
//            ImageLoader.getInstance().displayImage(goodsList.getJSONObject(i).getJSONObject(Constance.default_photo).getString(Constance.large)
//                    , iv_img);
//            tv_o_price.setText("￥" + goodsList.getJSONObject(i).getString(Constance.price));
//            tv_o_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
//            tv_c_price.setText("￥" + goodsList.getJSONObject(i).getString(Constance.current_price));
//            final int finalI = i;
//            view.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent mIntent = new Intent(mView.getActivity(), ProDetailActivity.class);
//                    int productId = goodsBeanList.get(finalI).getId();
//                    mIntent.putExtra(Constance.product, productId);
////            mIntent.putExtra(Constance.is_xiangou,false);
//                    mView.startActivity(mIntent);
//                    mView.getActivity().finish();
//                }
//            });
//            LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            layoutParams.setMargins(15,15,15,15);
//            ll_xinpin.addView(view);
        }

//        if (1 == page)
//            recommendGoodses = goodsList;
//        else if (null != recommendGoodses) {
//            for (int i = 0; i < goodsList.length(); i++) {
//                recommendGoodses.add(goodsList.getJSONObject(i));
//            }
//
//            if (AppUtils.isEmpty(goodsList))
//                MyToast.show(mView.getActivity(), "没有更多内容了");
//        }

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
//        this.page--;
        if (null != mPullToRefreshLayout) {
            mPullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
            mPullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
        }

        if (requestCode.equals(NetWorkConst.CATEGORY)) {
            if(pd!=null)pd.setVisibility(View.INVISIBLE);
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
                ImageLoader.getInstance().displayImage(imagePath, holder.imageView,((DemoApplication)mView.getActivity().getApplicationContext()).getImageLoaderOption());

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
                ImageLoader.getInstance().displayImage(imagePath, holder.imageView,((DemoApplication)mView.getActivity().getApplicationContext()).getImageLoaderOption());

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
                DemoApplication.unreadMsgCount = unreadMsgCount;
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
                ImageLoader.getInstance().displayImage(imagePath, holder.imageView,((DemoApplication)mView.getActivity().getApplicationContext()).getImageLoaderOption());

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
