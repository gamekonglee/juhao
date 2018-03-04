package bc.juhao.com.ui.fragment;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.lib.common.hxp.view.PullToRefreshLayout;
import com.nostra13.universalimageloader.core.ImageLoader;

import bc.juhao.com.R;
import bc.juhao.com.common.BaseFragment;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.controller.HomeController;
import bc.juhao.com.listener.IScrollViewListener;
import bc.juhao.com.ui.activity.ChartListActivity;
import bc.juhao.com.ui.activity.IssueApplication;
import bc.juhao.com.ui.activity.MainActivity;
import bc.juhao.com.ui.activity.WebViewHomeActivity;
import bc.juhao.com.ui.activity.product.SelectGoodsActivity;
import bc.juhao.com.ui.activity.programme.DiyActivity;
import bc.juhao.com.ui.activity.programme.ProgrammerActivity;
import bc.juhao.com.ui.activity.user.MerchantInfoActivity;
import bc.juhao.com.ui.activity.user.SimpleScannerActivity;
import bc.juhao.com.ui.view.ObservableScrollView;
import bc.juhao.com.utils.ColorUtil;
import bc.juhao.com.utils.DensityUtil;
import bc.juhao.com.utils.MyShare;
import bocang.utils.AppUtils;
import bocang.utils.IntentUtil;

import static bc.juhao.com.R.id.keting_home_iv;

/**
 * @author Jun
 * @time 2017/1/5  12:00
 * @desc 首页面
 */
public class HomeFragment extends BaseFragment implements View.OnClickListener {
    private HomeController mController;
    private ImageView lineIv;
    private FrameLayout fl_ll;
    private ObservableScrollView scrollView;
    private ConvenientBanner convenientBanner;
    private EditText et_search;
    private TextView topLeftBtn;
    private TextView topRightBtn;
    private PullToRefreshLayout refresh_view;
    private RelativeLayout kefu_rl;
    private LinearLayout one_ll, two_ll, three_ll;
    public TextView unMessageTv;
    private LinearLayout xiandai_ll, yijia_ll, tianyuan_ll, zhongshi_ll, woshi_ll, oushi_ll, houxiandai_ll, xinzhongshi_ll, popularity_01_ll, popularity_02_ll, popularity_03_ll;
    private ImageView keting_iv, woshi_iv, ertong_iv, guangyuan_iv, canting_iv, gengduo_iv, rexiaotop_iv, yiji_iv,
            pinpaitehui_iv, maishoutuijian_iv, yijianpei, zhongshi_iv, meishi_iv, oushi_iv,
            houxiandai_iv, xinzhongshi_iv, yijia_iv, tianyuan_iv, popularity_01_iv, popularity_02_iv, popularity_03_iv;
    private LinearLayout yangtai_ll;
    private LinearLayout zoulang_ll;

    private LinearLayout square_ll,my_works_ll,select_ll;
    private TextView square_tv,my_works_tv,select_tv;
    private View square_view,my_works_view,select_view;
    private View add_rl;
    public int mProgrammeType=0;
    private LinearLayout ll_jingxuan;
    private TextView tv_new_product_more;
    private TextView tv_more_lamb;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        ((MainActivity)getActivity()).setColor(getActivity(), Color.WHITE);
        return inflater.inflate(R.layout.fm_home_new, null);
    }

    @Override
    protected void initController() {
        mController = new HomeController(this);
    }

    @Override
    protected void initViewData() {
        initImageView();


    }

    /**
     * 动态初始化界面图片
     */
    private void initImageView() {
//        ImageLoader.getInstance().displayImage(NetWorkConst.SCENE_HOST + "images/App/rexiaotop.jpg", rexiaotop_iv);
//        ImageLoader.getInstance().displayImage(NetWorkConst.SCENE_HOST + "images/App/pinpaitehui.jpg", pinpaitehui_iv);
//        ImageLoader.getInstance().displayImage(NetWorkConst.SCENE_HOST + "images/App/maishoutuijian.jpg", maishoutuijian_iv);
//        ImageLoader.getInstance().displayImage(NetWorkConst.SCENE_HOST + "images/App/zhongshi.png", zhongshi_iv);
//        ImageLoader.getInstance().displayImage(NetWorkConst.SCENE_HOST + "images/App/meishi.png", meishi_iv);
//        ImageLoader.getInstance().displayImage(NetWorkConst.SCENE_HOST + "images/App/oushi.png", oushi_iv);
//        ImageLoader.getInstance().displayImage(NetWorkConst.SCENE_HOST + "images/App/houxiandai.png", houxiandai_iv);
//        ImageLoader.getInstance().displayImage(NetWorkConst.SCENE_HOST + "images/App/xinzhongshi.png", xinzhongshi_iv);
//        ImageLoader.getInstance().displayImage(NetWorkConst.SCENE_HOST + "images/App/yijia.png", yijia_iv);
//        ImageLoader.getInstance().displayImage(NetWorkConst.SCENE_HOST + "images/App/tianyuan.png", tianyuan_iv);
//        ImageLoader.getInstance().displayImage(NetWorkConst.SCENE_HOST + "images/App/yijianpei.jpg", yijianpei);
        //        ImageLoader.getInstance().displayImage(NetWorkConst.SCENE_HOST+"images/App/keting.png", keting_iv);
        //        ImageLoader.getInstance().displayImage(NetWorkConst.SCENE_HOST+"images/App/woshi.png", woshi_iv);
        //        ImageLoader.getInstance().displayImage(NetWorkConst.SCENE_HOST+"images/App/ertong.png", ertong_iv);
        //        ImageLoader.getInstance().displayImage(NetWorkConst.SCENE_HOST+"images/App/guangyuan.png", guangyuan_iv);
        //        ImageLoader.getInstance().displayImage(NetWorkConst.SCENE_HOST+"images/App/canting.png", canting_iv);
        //        ImageLoader.getInstance().displayImage(NetWorkConst.SCENE_HOST+"images/App/gengduo.png", gengduo_iv);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void initView() {
        unMessageTv = (TextView) getActivity().findViewById(R.id.unMessageTv);
        lineIv = (ImageView) getActivity().findViewById(R.id.lineIv);
        et_search = (EditText) getActivity().findViewById(R.id.et_search);
        topLeftBtn =  getActivity().findViewById(R.id.topLeftBtn);
        topRightBtn =  getActivity().findViewById(R.id.topRightBtn);
        et_search.setOnClickListener(this);
        topLeftBtn.setOnClickListener(this);
        topRightBtn.setOnClickListener(this);
        one_ll = (LinearLayout) getActivity().findViewById(R.id.one_ll);
        two_ll = (LinearLayout) getActivity().findViewById(R.id.two_ll);
        three_ll = (LinearLayout) getActivity().findViewById(R.id.three_ll);
        one_ll.setOnClickListener(this);
        two_ll.setOnClickListener(this);
        three_ll.setOnClickListener(this);

        convenientBanner = (ConvenientBanner) getActivity().findViewById(R.id.convenientBanner);
        fl_ll = (FrameLayout) getActivity().findViewById(R.id.fl_ll);
        scrollView = (ObservableScrollView) getActivity().findViewById(R.id.scrollView);

        scrollView.setScrollViewListener(new IScrollViewListener() {
            @Override
            public void onScrollChanged(ObservableScrollView scrollView, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (convenientBanner != null) {

                    int y = scrollY - oldScrollY;
                    bannerViewTopMargin = DensityUtil.px2dip(getActivity(), (convenientBanner.getHeight() - scrollY));
//                    Log.v("520it", "bannerViewTopMargin:" + bannerViewTopMargin);
                    bannerViewHeight = DensityUtil.px2dip(getActivity(), convenientBanner.getHeight());
//                    Log.v("520it", "bannerViewHeight:" + bannerViewHeight);
                }

                handleTitleBarColorEvaluate();
            }
        });

        refresh_view = (PullToRefreshLayout) getActivity().findViewById(R.id.refresh_view);
        kefu_rl = (RelativeLayout) getActivity().findViewById(R.id.kefu_rl);
        kefu_rl.setOnClickListener(this);

        xiandai_ll = (LinearLayout) getActivity().findViewById(R.id.keting_ll);
        yijia_ll = (LinearLayout) getActivity().findViewById(R.id.bieshu_ll);
        tianyuan_ll = (LinearLayout) getActivity().findViewById(R.id.louti_ll);
        zhongshi_ll = (LinearLayout) getActivity().findViewById(R.id.canting_ll);
        woshi_ll = (LinearLayout) getActivity().findViewById(R.id.woshi_ll);
        oushi_ll = (LinearLayout) getActivity().findViewById(R.id.shufang_ll);
        houxiandai_ll = (LinearLayout) getActivity().findViewById(R.id.ertongfang_ll);
        xinzhongshi_ll = (LinearLayout) getActivity().findViewById(R.id.weiyujian_ll);
        yangtai_ll = getActivity().findViewById(R.id.yangtai_ll);
        zoulang_ll = getActivity().findViewById(R.id.zoulang_ll);
        ll_jingxuan = getActivity().findViewById(R.id.ll_jingxuan);

        xiandai_ll.setOnClickListener(this);
        yijia_ll.setOnClickListener(this);
        tianyuan_ll.setOnClickListener(this);
        zhongshi_ll.setOnClickListener(this);
        woshi_ll.setOnClickListener(this);
        oushi_ll.setOnClickListener(this);
        houxiandai_ll.setOnClickListener(this);
        xinzhongshi_ll.setOnClickListener(this);
        yangtai_ll.setOnClickListener(this);
        zoulang_ll.setOnClickListener(this);

        square_ll = (LinearLayout) getActivity().findViewById(R.id.square_ll);
        square_ll.setOnClickListener(this);
        add_rl = getActivity().findViewById(R.id.add_rl);
        add_rl.setOnClickListener(this);
        select_ll = (LinearLayout) getActivity().findViewById(R.id.select_ll);
        select_ll.setOnClickListener(this);
        my_works_ll = (LinearLayout) getActivity().findViewById(R.id.my_works_ll);
        my_works_ll.setOnClickListener(this);
        square_ll = (LinearLayout) getActivity().findViewById(R.id.square_ll);
        square_ll.setOnClickListener(this);
        square_view =  getView().findViewById(R.id.square_view);
        my_works_view =  getView().findViewById(R.id.my_works_view);
        select_view =   getView().findViewById(R.id.select_view);
        square_tv = (TextView) getView().findViewById(R.id.square_tv);
        my_works_tv = (TextView)  getView().findViewById(R.id.my_works_tv);
        select_tv = (TextView)  getView().findViewById(R.id.select_tv);


        keting_iv = (ImageView) getActivity().findViewById(R.id.keting_iv);
        woshi_iv = (ImageView) getActivity().findViewById(R.id.woshi_iv);
        ertong_iv = (ImageView) getActivity().findViewById(R.id.ertong_iv);
        guangyuan_iv = (ImageView) getActivity().findViewById(R.id.guangyuan_iv);
        canting_iv = (ImageView) getActivity().findViewById(R.id.canting_iv);
        gengduo_iv = (ImageView) getActivity().findViewById(R.id.gengduo_iv);
        yiji_iv = (ImageView) getActivity().findViewById(R.id.yiji_iv);
        keting_iv.setOnClickListener(this);
        woshi_iv.setOnClickListener(this);
        ertong_iv.setOnClickListener(this);
        guangyuan_iv.setOnClickListener(this);
        canting_iv.setOnClickListener(this);
        gengduo_iv.setOnClickListener(this);
        yiji_iv.setOnClickListener(this);

        rexiaotop_iv = (ImageView) getActivity().findViewById(R.id.rexiaotop_iv);
        pinpaitehui_iv = (ImageView) getActivity().findViewById(R.id.pinpaitehui_iv);
        maishoutuijian_iv = (ImageView) getActivity().findViewById(R.id.maishoutuijian_iv);
        rexiaotop_iv.setOnClickListener(this);
        pinpaitehui_iv.setOnClickListener(this);
        maishoutuijian_iv.setOnClickListener(this);
        yijianpei = (ImageView) getActivity().findViewById(R.id.yijianpei);
        yijianpei.setOnClickListener(this);

        zhongshi_iv = (ImageView) getActivity().findViewById(R.id.canting_home_iv);
        meishi_iv = (ImageView) getActivity().findViewById(R.id.meishi_iv);
        oushi_iv = (ImageView) getActivity().findViewById(R.id.shufang_home_iv);
        houxiandai_iv = (ImageView) getActivity().findViewById(R.id.ertongfang_iv);
        xinzhongshi_iv = (ImageView) getActivity().findViewById(keting_home_iv);
        yijia_iv = (ImageView) getActivity().findViewById(R.id.bieshu_iv);
        tianyuan_iv = (ImageView) getActivity().findViewById(R.id.louti_iv);

        popularity_01_iv = (ImageView) getActivity().findViewById(R.id.popularity_01_iv);
        popularity_02_iv = (ImageView) getActivity().findViewById(R.id.popularity_02_iv);
        popularity_03_iv = (ImageView) getActivity().findViewById(R.id.popularity_03_iv);
        popularity_01_iv.setOnClickListener(this);
        popularity_02_iv.setOnClickListener(this);
        popularity_03_iv.setOnClickListener(this);
        popularity_01_ll = (LinearLayout) getActivity().findViewById(R.id.popularity_01_ll);
        popularity_02_ll = (LinearLayout) getActivity().findViewById(R.id.popularity_02_ll);
        popularity_03_ll = (LinearLayout) getActivity().findViewById(R.id.popularity_03_ll);
        popularity_01_ll.setOnClickListener(this);
        popularity_02_ll.setOnClickListener(this);
        popularity_03_ll.setOnClickListener(this);

        tv_new_product_more = getView().findViewById(R.id.tv_new_product_more);
        tv_new_product_more.setOnClickListener(this);
        tv_more_lamb = getView().findViewById(R.id.tv_more_lamb);
        tv_more_lamb.setOnClickListener(this);
        ll_jingxuan.removeAllViews();
        for(int i=0;i<3;i++){
            View view=View.inflate(getActivity(),R.layout.item_jingxuan,null);
            LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(15,0,0,0);
            ImageView imageView=view.findViewById(R.id.iv);
            TextView tv_title=view.findViewById(R.id.tv_title);
            TextView tv_content=view.findViewById(R.id.tv_content);
            if(i==0){
                tv_title.setText("钜豪地插，乐享生活，舍我其谁！");
                tv_content.setText("地插，平常装修中不起眼的东西");
                ImageLoader.getInstance().displayImage("https://mmbiz.qpic.cn/mmbiz_jpg/qd3k7nL0re2ibiclbAUUicN2ztgo8j2IicxtshicR3AqwXT08VTupqsVgj7PEiaS7EQDUKDIbX5WoljaU4DIMoRKubcg/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1",imageView,((IssueApplication)getActivity().getApplicationContext()).getImageLoaderOption());
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(getContext(), WebViewHomeActivity.class);
                        intent.putExtra("url","https://mp.weixin.qq.com/s/3sNc8HuZ02FF-SxSScK0XQ");
                        startActivity(intent);
                    }
                });
            }else if(i==1){
                tv_title.setText("2018，让钜豪中式灯为您照亮幸福");
                tv_content.setText("新的起点是成功的开端，目标是行动的航标。");
                ImageLoader.getInstance().displayImage("https://mmbiz.qpic.cn/mmbiz_jpg/WF6o844xTr1MHgCd2BEVYO27KbDuxp696Cicn6AldAngM9EicKpiaDIiaJpQqh4WlcO7VkA2iamicicmOSBWiaTrib2poSA/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1",imageView,((IssueApplication)getActivity().getApplicationContext()).getImageLoaderOption());
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(getContext(), WebViewHomeActivity.class);
                        intent.putExtra("url","https://mp.weixin.qq.com/s/8QgFRTXC_lQQ2ujzQyokrg");
                        startActivity(intent);
                    }
                });

            }else {
                tv_title.setText("习近平主席新年贺词视频集锦");
                tv_content.setText("回首过往，展望新年");
                ImageLoader.getInstance().displayImage("https://mmbiz.qpic.cn/mmbiz_jpg/71vT6WeWUCW20zaC99CMbrQXFLaBwbaTpZKdRSuclC62P4gUlcavEcOKPWena0UZ6lw4ibQet3eBptAhzWD3VaA/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1",imageView,((IssueApplication)getActivity().getApplicationContext()).getImageLoaderOption());
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(getContext(), WebViewHomeActivity.class);
                        intent.putExtra("url","https://mp.weixin.qq.com/s/N2dVdTk7095t_hLWiziNyA");
                        startActivity(intent);
                    }
                });
            }
            ll_jingxuan.addView(view,layoutParams);
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onResume() {
        super.onResume();
        mController.setResume();
        mProgrammeType = 0;
        selectProgrammeType();
        mController.getRefershData();
    }

    @Override
    public void onPause() {
        super.onPause();
        mController.setPause();
    }

    @Override
    public void onClick(View v) {
        AnimationSet animationSet = new AnimationSet(true);
        TranslateAnimation translateAnimation = null;
        switch (v.getId()) {
            case R.id.topLeftBtn://扫描二维码

                IntentUtil.startActivity(this.getActivity(), SimpleScannerActivity.class, false);
                break;
            case R.id.topRightBtn://消息
                if (!isToken()) {
                    IntentUtil.startActivity(this.getActivity(), ChartListActivity.class, false);
                }
                break;
            case R.id.et_search://搜索产品
                IntentUtil.startActivity(this.getActivity(), SelectGoodsActivity.class, false);
                break;
            case R.id.one_ll://三十天无理由退货
                mController.getArticleDetail(21);
                break;
            case R.id.two_ll://服务条款
                mController.getArticleDetail(22);
                break;
            case R.id.three_ll://服务运营商
                IntentUtil.startActivity(getActivity(), MerchantInfoActivity.class, false);
                break;
            case R.id.kefu_rl://点击我
                IntentUtil.startActivity(getActivity(), MerchantInfoActivity.class, false);
                break;
            case R.id.keting_ll://客厅
                mController.getMoreGoodsByCategory("客厅");
                break;
            case R.id.bieshu_ll://别墅
                mController.getMoreGoodsByCategory("别墅");
                break;
            case R.id.louti_ll://楼梯
                mController.getMoreGoodsByCategory("楼梯");
                break;
            case R.id.canting_ll://餐厅
                mController.getMoreGoodsByCategory("餐厅");
                break;
            case R.id.woshi_ll://卧室
                mController.getMoreGoodsByCategory("卧室");
                break;
            case R.id.shufang_ll://书房
                mController.getMoreGoodsByCategory("书房");
                break;
            case R.id.ertongfang_ll://儿童房
                mController.getMoreGoodsByCategory("儿童房");
                break;
            case R.id.weiyujian_ll://卫浴间
                mController.getMoreGoodsByCategory("卫浴间");
                break;
            case R.id.yangtai_ll:
                mController.getMoreGoodsByCategory("阳台");
                break;
            case R.id.zoulang_ll:
                mController.getMoreGoodsByCategory("走廊");
                break;

            case R.id.keting_iv://客厅
                mController.getMoreGoods("77");
                break;
            case R.id.woshi_iv://卧室
                mController.getMoreGoods("126");
                break;
            case R.id.ertong_iv://儿童
                mController.getMoreGoods("139");//待定
                break;
            case R.id.guangyuan_iv://光源
                mController.getMoreGoods("139");//待定
                break;
            case R.id.canting_iv://餐厅
                mController.getMoreGoods("78");
                break;
            case R.id.gengduo_iv://更多
                mController.getMoreGoods("76");
                break;
            case R.id.yiji_iv://一级
                Intent intent = new Intent(getActivity(), SelectGoodsActivity.class);
                intent.putExtra(Constance.ISYIJI, true);
                getActivity().startActivity(intent);
                break;
            case R.id.rexiaotop_iv://热销
                mController.getMore02Goods(4);
                break;
            case R.id.pinpaitehui_iv://品牌推荐
                mController.getMore02Goods(5);
                break;
            case R.id.maishoutuijian_iv://卖手推荐
                mController.getMore02Goods(2);
                break;
            case R.id.yijianpei://私人定制
                IntentUtil.startActivity(this.getActivity(), DiyActivity.class, false);
                break;
            case R.id.popularity_01_ll://人气推荐产品1
                if (AppUtils.isEmpty(mController.mPopularityGoodses) && mController.mPopularityGoodses.length() > 0)
                    return;
                mController.getMoreActivity(mController.mPopularityGoodses.getJSONObject(0).getInt(Constance.id));
                break;
            case R.id.popularity_02_ll://人气推荐产品2
                if (AppUtils.isEmpty(mController.mPopularityGoodses) && mController.mPopularityGoodses.length() > 1)
                    return;
                mController.getMoreActivity(mController.mPopularityGoodses.getJSONObject(1).getInt(Constance.id));
                break;
            case R.id.popularity_03_ll://人气推荐产品3
                if (AppUtils.isEmpty(mController.mPopularityGoodses) && mController.mPopularityGoodses.length() > 2)
                    return;
                mController.getMoreActivity(mController.mPopularityGoodses.getJSONObject(2).getInt(Constance.id));
                break;
            case R.id.popularity_01_iv://人气推荐产品1
                if (AppUtils.isEmpty(mController.mPopularityGoodses) && mController.mPopularityGoodses.length() > 0)
                    return;
                mController.getMoreActivity(mController.mPopularityGoodses.getJSONObject(0).getInt(Constance.id));
                break;
            case R.id.popularity_02_iv://人气推荐产品2
                if (AppUtils.isEmpty(mController.mPopularityGoodses) && mController.mPopularityGoodses.length() > 1)
                    return;
                mController.getMoreActivity(mController.mPopularityGoodses.getJSONObject(1).getInt(Constance.id));
                break;
            case R.id.popularity_03_iv://人气推荐产品3
                if (AppUtils.isEmpty(mController.mPopularityGoodses) && mController.mPopularityGoodses.length() > 2)
                    return;
                mController.getMoreActivity(mController.mPopularityGoodses.getJSONObject(2).getInt(Constance.id));
                break;

            case R.id.add_rl://新增
                IntentUtil.startActivity(this.getActivity(), DiyActivity.class,false);
                break;
            case R.id.select_ll://筛选
                MainActivity.toFilter=true;
                IssueApplication.isGoProgramme=true;
                startActivity(new Intent(getActivity(), ProgrammerActivity.class));
                break;
            case R.id.my_works_ll://我的作品
                mProgrammeType = 1;
                selectProgrammeType();
                mController.getRefershData();
                break;
            case R.id.square_ll://广场
                mProgrammeType =0;
                selectProgrammeType();
                mController.getRefershData();
                break;
            case R.id.tv_more_lamb:
                startActivity(new Intent(getActivity(), ProgrammerActivity.class));
                break;
            case R.id.tv_new_product_more:
                Intent intent1=new Intent(getActivity(),SelectGoodsActivity.class);
                intent1.putExtra("news",true);
                startActivity(intent1);
                break;

        }
        if (AppUtils.isEmpty(translateAnimation))
            return;
        animationSet.addAnimation(translateAnimation);
        animationSet.setFillBefore(false);
        animationSet.setFillAfter(true);
        animationSet.setDuration(100);
        lineIv.startAnimation(animationSet);

        //        mCurrentCheckedRadioLeft = mController.getCurrentCheckedRadioLeft(v);//更新当前横条距离左边的距离
    }

    private int bannerViewTopMargin; // 广告视图距离顶部的距离
    private int bannerViewHeight = 150; // 广告视图的高度
    private int titleViewHeight = 65; // 标题栏的高度
    private boolean isStickyTop = false; // 是否吸附在顶部

    // 处理标题栏颜色渐变
    private void handleTitleBarColorEvaluate() {
        float fraction;
        if (bannerViewTopMargin > 0) {
            fraction = 1f - bannerViewTopMargin * 1f / 60;
            if (fraction < 0f)
                fraction = 0f;
            fl_ll.setAlpha(fraction);
            //            et_search.setAlpha(fraction+0.5f);
            return;
        }

        float space = Math.abs(bannerViewTopMargin) * 1f;
        fraction = space / (bannerViewHeight - titleViewHeight);
        if (fraction < 0f)
            fraction = 0f;
        if (fraction > 1f)
            fraction = 1f;
        fl_ll.setAlpha(1f);
        //        et_search.setAlpha(1f);


        if (fraction >= 1f || isStickyTop) {
            isStickyTop = true;
            fl_ll.setBackgroundColor(getActivity().getResources().getColor(R.color.green));
        } else {
            fl_ll.setBackgroundColor(ColorUtil.getNewColorByStartEndColor(getActivity(), fraction, R.color.transparent, R.color.green));
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        String token = MyShare.get(getActivity()).getString(Constance.TOKEN);
        if (AppUtils.checkNetwork() && !AppUtils.isEmpty(token)) {
            getSuccessLogin();
        }
    }

    /**
     * 登录成功处理事件
     */
    private void getSuccessLogin() {
        final String uid = MyShare.get(getActivity()).getString(Constance.USERID);
        if (AppUtils.isEmpty(uid)) {
            return;
        }

        if (!EMClient.getInstance().isLoggedInBefore() || !EMClient.getInstance().isConnected()) {
            EMClient.getInstance().login(uid, uid, new EMCallBack() {
                @Override
                public void onSuccess() {
                    Log.e("520it", "H登录成功");
                    EMClient.getInstance().groupManager().loadAllGroups();
                    EMClient.getInstance().chatManager().loadAllConversations();
                }

                @Override
                public void onProgress(int progress, String status) {
                }

                @Override
                public void onError(final int code, final String message) {
                    Log.e("520it", "H登录失败:" + message);
                    if (message.equals("User dosn't exist")) {
                        sendRegiestSuccess();
                    }
                }
            });
        }
    }

    /**
     * 环信注册
     */
    private void sendRegiestSuccess() {
        final String uid = MyShare.get(getActivity()).getString(Constance.USERID);
        if (AppUtils.isEmpty(uid)) {
            return;
        }
        new Thread(new Runnable() {
            public void run() {
                try {
                    EMClient.getInstance().createAccount(uid, uid);//同步方法
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.e("520it", "S注册成功!");
                            getSuccessLogin();

                        }
                    });

                } catch (final HyphenateException e) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.e("520it", "S注册失败!：" + e.getMessage());
                            //                            getSuccessLogin();
                        }
                    });

                }
            }
        }).start();
    }


    /**
     * 选择方案类型状态改变
     */
    private void selectProgrammeType(){
        square_tv.setTextColor(getActivity().getResources().getColor(R.color.txt_black));
        my_works_tv.setTextColor(getActivity().getResources().getColor(R.color.txt_black));
        square_view.setVisibility(View.GONE);
        my_works_view.setVisibility(View.GONE);
        switch (mProgrammeType){
            case 0://广场
                square_tv.setTextColor(getActivity().getResources().getColor(R.color.green));
                square_view.setVisibility(View.VISIBLE);
                break;
            case 1://我的作品
                my_works_tv.setTextColor(getActivity().getResources().getColor(R.color.green));
                my_works_view.setVisibility(View.VISIBLE);
                break;

        }
    }
}
