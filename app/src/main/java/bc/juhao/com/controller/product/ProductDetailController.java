package bc.juhao.com.controller.product;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aliyun.iot.ilop.demo.DemoApplication;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.NetUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import bc.juhao.com.R;
import bc.juhao.com.chat.DemoHelper;
import bc.juhao.com.common.BaseFragment;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.cons.NetWorkConst;
import bc.juhao.com.controller.BaseController;
import bc.juhao.com.listener.INetworkCallBack;
import bc.juhao.com.listener.IParamentChooseListener;
import bc.juhao.com.ui.activity.ChartListActivity;
import bc.juhao.com.ui.activity.buy.ConfirmOrderActivity;
import bc.juhao.com.ui.activity.buy.ShoppingCartActivity;
import bc.juhao.com.ui.activity.product.ProDetailActivity;
import bc.juhao.com.ui.activity.product.ShareProductActivity;
import bc.juhao.com.ui.activity.programme.DiyActivity;
import bc.juhao.com.ui.activity.programme.camera.TestCameraActivity;
import bc.juhao.com.ui.activity.user.ChatActivity;
import bc.juhao.com.ui.fragment.DetailGoodsFragmemt;
import bc.juhao.com.ui.fragment.IntroduceGoodsFragment;
import bc.juhao.com.ui.fragment.ParameterFragment;
import bc.juhao.com.ui.fragment.SunImageFragment;
import bc.juhao.com.ui.view.ShowDialog;
import bc.juhao.com.ui.view.popwindow.SelectParamentPopWindow;
import bc.juhao.com.utils.CartAnimator;
import bc.juhao.com.utils.MyShare;
import bc.juhao.com.utils.UIUtils;
import bocang.json.JSONArray;
import bocang.json.JSONObject;
import bocang.utils.AppDialog;
import bocang.utils.AppUtils;
import bocang.utils.IntentUtil;
import bocang.utils.LogUtils;
import bocang.utils.MyLog;
import bocang.utils.MyToast;

/**
 * @author: Jun
 * @date : 2017/2/13 17:58
 * @description :
 */
public class ProductDetailController extends BaseController implements INetworkCallBack {

    private ProDetailActivity mView;
    private Intent mIntent;

    private TextView product_tv, detail_tv, parament_tv, sun_image_tv;
    private ViewPager container_vp;
    private LinearLayout title_ll, product_ll, detail_ll, main_ll, sun_image_ll;
    private ImageView collectIv;
    private RelativeLayout main_rl;
    private TextView unMessageReadTv;
    private TextView tuijian_tv;

    private ProductContainerAdapter mContainerAdapter;
    private JSONObject mAddressObject;
    private ArrayList<BaseFragment> mFragments;

    public ProductDetailController(ProDetailActivity v) {
        mView = v;
        initView();
        initViewData();
    }

    public void initViewData() {
        sendProductDetail();
        sendCustom();
    }

    private void initView() {

        product_tv = (TextView) mView.findViewById(R.id.product_tv);
        detail_tv = (TextView) mView.findViewById(R.id.detail_tv);
//        parament_tv = (TextView) mView.findViewById(R.id.parament_tv);
        sun_image_tv = (TextView) mView.findViewById(R.id.sun_image_tv);
        tuijian_tv = mView.findViewById(R.id.tuijian_tv);
        unMessageReadTv = (TextView) mView.findViewById(R.id.unMessageReadTv);
        container_vp = (ViewPager) mView.findViewById(R.id.container_vp);

        mContainerAdapter = new ProductContainerAdapter(mView.getSupportFragmentManager());
        container_vp.setAdapter(mContainerAdapter);
//        container_vp.setOnPageChangeListener(this);
        container_vp.setCurrentItem(0);

        title_ll = (LinearLayout) mView.findViewById(R.id.title_ll);
        product_ll = (LinearLayout) mView.findViewById(R.id.product_ll);
        main_ll = (LinearLayout) mView.findViewById(R.id.main_ll);
        detail_ll = (LinearLayout) mView.findViewById(R.id.detail_ll);
//        parament_ll = (LinearLayout) mView.findViewById(R.id.parament_ll);
        sun_image_ll = (LinearLayout) mView.findViewById(R.id.sun_image_ll);

        main_rl = (RelativeLayout) mView.findViewById(R.id.main_rl);
        collectIv = (ImageView) mView.findViewById(R.id.collectIv);

        container_vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                switch (position) {
                    case 0:
                        selectProductType(R.id.product_ll);
                        break;
                    case 1:
                        selectProductType(R.id.detail_ll);
                        mFragments.get(position).onStart();
                        break;
                    case 2:
                        selectProductType(R.id.tuijian_ll);
                        break;
                    case 3:
                        selectProductType(R.id.sun_image_ll);
                        break;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    /**
     * 获取产品详情
     */
    public void sendProductDetail() {
        LogUtils.logE(TAG, "产品id:" + mView.mProductId);
        mNetWork.sendProductDetail(mView.mProductId, this);
    }

    /**
     * 客服QQ
     */
    private void sendCustom() {
        mNetWork.sendCustom(this);
    }


    /**
     * 购物车列表
     */
    public void sendShoppingCart() {
        mNetWork.sendShoppingCart(this);
    }

    /*
     * 选择参数
     */
    public void selectParament() {
        if (AppUtils.isEmpty(mView.mProductObject))
            return;
        mPopWindow = new SelectParamentPopWindow(mView, mView.mProductObject);
        mPopWindow.onShow(main_ll);
        mPopWindow.setListener(new IParamentChooseListener() {
            @Override
            public void onParamentChanged(String text, Boolean isGoCart, String property, int mount, int price) {
                if (!AppUtils.isEmpty(text)) {
                    mView.mProperty = property;
                    mView.mPrice = price;
                    mView.mPropertyValue = text;
                }
                if (isGoCart == true) {
                    mView.setShowDialog(true);
                    mView.setShowDialog("正在加入购物车中...");
                    mView.showLoading();
                    sendGoShoppingCart(mView.mProductId + "", property, mount);
                }

                EventBus.getDefault().post(Constance.PROPERTY);
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
            case NetWorkConst.PRODUCTDETAIL://产品详情
                mView.goodses = ans.getJSONObject(Constance.product);
                try {
                    int level = DemoApplication.mUserObject.getInt(Constance.level);
                    if (level > 0) {
                        mView.mOrderId = mView.goodses.getInt(Constance.order_id);
                    } else {
                        mView.mOrderId = 1;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            case NetWorkConst.CUSTOM://客服qq
                NetWorkConst.QQ = ans.getString(Constance.custom);
                break;
            case NetWorkConst.ADDCART://立即购买/加入购物车
                //                MyToast.show(mView, UIUtils.getString(R.string.go_cart_ok));
                sendShoppingCart();

                break;
            case NetWorkConst.GETCART://购物车列表
                //                int level = DemoApplication.mUserObject.getInt(Constance.level);
                //                if (level == 0) {
                //                    if (!mView.isToken()) {
                //                        IntentUtil.startActivity(mView, ChartListActivity.class, false);
                //                    }
                //                    return;
                //                }

                goCartAnimator(ans);

                EventBus.getDefault().post(Constance.CARTCOUNT);
                break;
        }
    }

    @Override
    public void onFailureListener(String requestCode, JSONObject ans) {
        if (null == mView || mView.isFinishing())
            return;
        if (AppUtils.isEmpty(ans)) {
            AppDialog.messageBox(UIUtils.getString(R.string.server_error));
            return;
        }
        AppDialog.messageBox(ans.getString(Constance.error_desc));
    }

    /**
     * 加入购物车动画效果
     */
    public void goCartAnimator(JSONObject ans) {
        CartAnimator cartAnimator = new CartAnimator(mView);
        ImageView iv = new ImageView(mView);
        String path = mView.goodses.getJSONObject(Constance.app_img).getString(Constance.img);
        ImageLoader.getInstance().displayImage(path, iv);
        cartAnimator.setParentView(main_rl);
        cartAnimator.setCartView(collectIv);
        cartAnimator.startCartAnimator(iv, container_vp);


        if (ans.getJSONArray(Constance.goods_groups).length() > 0) {
            DemoApplication.mCartCount = ans.getJSONArray(Constance.goods_groups)
                    .getJSONObject(0).getJSONArray(Constance.goods).length();
            unMessageReadTv.setVisibility(View.VISIBLE);
            unMessageReadTv.setText(DemoApplication.mCartCount + "");
        } else {
            DemoApplication.mCartCount = 0;
            unMessageReadTv.setVisibility(View.GONE);
        }

        Toast.makeText(mView, "加入购物车成功!", Toast.LENGTH_SHORT).show();
    }

    //登录环信
    private void loginHX() {
        final Toast toast = Toast.makeText(mView, "服务器连接中...!", Toast.LENGTH_SHORT);
        toast.show();
        if (NetUtils.hasNetwork(mView)) {
            String uid = MyShare.get(mView).getString(Constance.USERNAME);

            if (AppUtils.isEmpty(uid)) {
                return;
            }
            if (!TextUtils.isEmpty(uid)) {
                getSuccessLogin(uid, toast);

            }
        }
    }

    private void getSuccessLogin(final String uid, final Toast toast) {
        EMClient.getInstance().login(uid, uid, new EMCallBack() {
            @Override
            public void onSuccess() {
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                MyLog.e("登录环信成功!");
                toast.cancel();
                String parent_id = DemoApplication.mUserObject.getString("parent_id");
                if (ProDetailActivity.isJuHao) {
                    parent_id = "37";
                }
//                System.out.println("parentid:"+parent_id);
                try {
                    EMClient.getInstance().contactManager().acceptInvitation(parent_id);
                    mView.startActivity(new Intent(mView, ChatActivity.class).putExtra(EaseConstant.EXTRA_USER_ID, parent_id));
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(int i, String s) {
                if (s.equals("User dosn't exist")) {
                    new Thread(new Runnable() {
                        public void run() {
                            try {
                                EMClient.getInstance().createAccount(uid, uid);//同步方法
                                mView.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        MyLog.e("注册成功!");
                                        getSuccessLogin(uid, toast);
                                    }
                                });

                            } catch (final HyphenateException e) {
                                mView.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        MyLog.e("注册失败!");
                                        getSuccessLogin(uid, toast);
                                    }
                                });

                            }
                        }
                    }).start();
                } else {
                    Toast.makeText(mView, "连接失败,请重试!", Toast.LENGTH_SHORT).show();
                    MyLog.e("登录环信失败!");
                    sendCall("连接聊天失败,请重试?");
                }
            }

            @Override
            public void onProgress(int i, String s) {
            }
        });
    }


    private SelectParamentPopWindow mPopWindow;

    public class ProductContainerAdapter extends FragmentPagerAdapter {


        public ProductContainerAdapter(FragmentManager fm) {
            super(fm);
            initFragment();
        }

        private void initFragment() {
            mFragments = new ArrayList<>();
            Bundle bundle = new Bundle();
            bundle.putString(Constance.product, mView.mProductId + "");
//            if(mView.getIntent().hasExtra(Constance.is_xiangou)){
//                ProDetailActivity.isXianGou=true;
//            }else {
//                ProDetailActivity.isXianGou=false;
//            }
//            bundle.putBoolean(Constance.is_xiangou,ProDetailActivity.isXianGou);
            IntroduceGoodsFragment matchFragment = new IntroduceGoodsFragment();
            matchFragment.setArguments(bundle);
            matchFragment.setmListener(new IntroduceGoodsFragment.ScrollListener() {
                @Override
                public void onScrollToBottom(int currPosition) {
                    if (currPosition == 0) {
                        title_ll.setVisibility(View.GONE);
//                        sun_image_ll.setVisibility(View.VISIBLE);
                        product_ll.setVisibility(View.VISIBLE);
                        detail_ll.setVisibility(View.VISIBLE);
                        mView.tuijian_ll.setVisibility(View.VISIBLE);
                    } else {
                        title_ll.setVisibility(View.VISIBLE);
                        product_ll.setVisibility(View.GONE);
//                        sun_image_ll.setVisibility(View.GONE);
                        detail_ll.setVisibility(View.GONE);
                        mView.tuijian_ll.setVisibility(View.GONE);
                    }
                }
            });


            DetailGoodsFragmemt detailFragment = new DetailGoodsFragmemt();
            detailFragment.setArguments(bundle);

            ParameterFragment parameterFragment = new ParameterFragment();
            parameterFragment.setArguments(bundle);

            SunImageFragment sunImageFragment = new SunImageFragment();
            sunImageFragment.setArguments(bundle);


            mFragments.add(matchFragment);
            mFragments.add(detailFragment);
            mFragments.add(parameterFragment);
            mFragments.add(sunImageFragment);

        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

    }


    /**********************************
     * ProduceDetailActivity调用方法
     * ********************************
     */

    /**
     * 购物车数量显示
     */
    public void getCartMun() {
        if (DemoApplication.mCartCount == 0) {
            unMessageReadTv.setVisibility(View.GONE);
        } else {
            unMessageReadTv.setVisibility(View.VISIBLE);
            unMessageReadTv.setText(DemoApplication.mCartCount + "");
        }
    }

    /**
     * 产品详情title不同选择
     *
     * @param type
     */
    public void selectProductType(int type) {

        sun_image_tv.setTextColor(mView.getResources().getColor(R.color.txt_black));
        tuijian_tv.setTextColor(mView.getResources().getColor(R.color.txt_black));
        detail_tv.setTextColor(mView.getResources().getColor(R.color.txt_black));
        product_tv.setTextColor(mView.getResources().getColor(R.color.txt_black));
        detail_tv.setBackgroundResource((R.drawable.bg_goods_detail_intro_corner_normal));
        product_tv.setBackgroundResource((R.drawable.bg_goods_detail_intro_corner_left_normal));
        tuijian_tv.setBackgroundResource(R.drawable.bg_goods_detail_intro_corner_normal);
        sun_image_tv.setBackgroundResource(R.drawable.bg_goods_detail_intro_corner_right_normal);
        switch (type) {
            case R.id.product_ll:
//                product_view.setVisibility(View.VISIBLE);
                product_tv.setBackgroundResource((R.drawable.bg_goods_detail_intro_corner_left));
                product_tv.setTextColor(Color.WHITE);
                container_vp.setCurrentItem(0, true);
                break;
            case R.id.detail_ll:
//                detail_view.setVisibility(View.VISIBLE);
                detail_tv.setBackgroundResource((R.drawable.bg_goods_detail_intro_corner));
                detail_tv.setTextColor(Color.WHITE);
                container_vp.setCurrentItem(1, true);
                break;
            case R.id.tuijian_ll:
                tuijian_tv.setTextColor(Color.WHITE);
                tuijian_tv.setBackgroundResource(R.drawable.bg_goods_detail_intro_corner);
                container_vp.setCurrentItem(2, true);
                break;
            case R.id.sun_image_ll:
//                sun_image_view.setVisibility(View.VISIBLE);
                sun_image_tv.setTextColor(Color.WHITE);
                sun_image_tv.setBackgroundResource(R.drawable.bg_goods_detail_intro_corner_right);
                container_vp.setCurrentItem(3, true);
                break;

        }

    }

    /**
     * 联系客服
     */
    public void sendCall(String msg) {
        try {
            if (DemoApplication.mUserObject == null) {
                return;
            }
            int level = DemoApplication.mUserObject.getInt(Constance.level);
            if (level == 0) {
                if (!mView.isToken()) {
                    IntentUtil.startActivity(mView, ChartListActivity.class, false);
                }
                return;
            }

            String parent_name = DemoApplication.mUserObject.getString("parent_name");
            String parent_id = DemoApplication.mUserObject.getString("parent_id");
            if (ProDetailActivity.isJuHao) {
                parent_id = "37";
                parent_name = "钜豪超市";
            }
            String userIcon = NetWorkConst.SCENE_HOST + DemoApplication.mUserObject.getString("parent_avatar");
            EaseUser user = new EaseUser(parent_id);
            user.setNickname(parent_name);
            user.setNick(parent_name);
            user.setAvatar(userIcon);
            DemoHelper.getInstance().saveContact(user);

            if (!EMClient.getInstance().isLoggedInBefore()) {
                ShowDialog mDialog = new ShowDialog();
                mDialog.show(mView, "提示", msg, new ShowDialog.OnBottomClickListener() {
                    @Override
                    public void positive() {
                        loginHX();
                    }

                    @Override
                    public void negtive() {

                    }
                });
            } else {
                EMClient.getInstance().contactManager().acceptInvitation(parent_id);
                mView.startActivity(new Intent(mView, ChatActivity.class).putExtra(EaseConstant.EXTRA_USER_ID, parent_id));
            }


        } catch (HyphenateException e) {
            e.printStackTrace();
        }
    }

    /**
     * 进入购物车页面
     */
    public void getShoopingCart() {
        IntentUtil.startActivity(mView, ShoppingCartActivity.class, false);
    }

    /**
     * 加入购物车
     */
    public void GoShoppingCart() {

        if (AppUtils.isEmpty(mView.mProductObject))
            return;
        if (mView.mProductObject.getJSONArray(Constance.properties).size() == 0) {
            mView.setShowDialog(true);
            mView.setShowDialog("正在加入购物车中...");
            mView.showLoading();
            sendGoShoppingCart(mView.mProductId + "", "", 1);
        } else {
            selectParament();
        }
    }

    /**
     * 分享
     */
    public void setShare() {

        if (AppUtils.isEmpty(mView.goodses)) {
            MyToast.show(mView, "还没加载完毕，请稍后再试");
            return;
        }
        mIntent = new Intent(mView, ShareProductActivity.class);
        mIntent.putExtra(Constance.product, mView.goodses);
        mIntent.putExtra(Constance.property, mView.mProperty);
        mView.startActivity(mIntent);

    }

    /**
     * 随心配
     */
    public void GoDiyProduct() {
        if (AppUtils.isEmpty(mView.goodses)) {
            MyToast.show(mView, "还没加载完毕，请稍后再试");
            return;
        }
        mIntent = new Intent(mView, DiyActivity.class);
        mIntent.putExtra(Constance.product, mView.goodses);
        mIntent.putExtra(Constance.property, mView.mProperty);
        DemoApplication.mSelectProducts.add(mView.goodses);
        mView.startActivity(mIntent);
    }

    /**
     * 立即购买
     */
    public void toBuy() {

        if (AppUtils.isEmpty(mView.mProductObject))
            return;
        mView.showLoading();
        mNetWork.sendShoppingCart(mView.mProductId + "", mView.mProperty, 1, new INetworkCallBack() {
            @Override
            public void onSuccessListener(String requestCode, JSONObject ans) {
                mNetWork.sendAddressList(new INetworkCallBack() {
                    @Override
                    public void onSuccessListener(String requestCode, JSONObject ans) {
                        JSONArray consigneeList = ans.getJSONArray(Constance.consignees);
                        if (consigneeList.length() == 0) {
                            MyToast.show(mView, "请先添加收货地址");
                            mView.hideLoading();
                            return;
                        }
                        mAddressObject = consigneeList.getJSONObject(0);
                        mNetWork.sendShoppingCart(new INetworkCallBack() {
                            @Override
                            public void onSuccessListener(String requestCode, JSONObject ans) {
                                JSONArray goodses = ans.getJSONArray(Constance.goods_groups).getJSONObject(0).getJSONArray(Constance.goods);
                                final JSONArray goods = new JSONArray();
                                goods.add(goodses.getJSONObject(0));
                                mView.hideLoading();
                                Intent intent = new Intent(mView, ConfirmOrderActivity.class);
                                intent.putExtra(Constance.goods, goods);
                                Float total = Float.parseFloat(goods.getJSONObject(0).getString(Constance.price)) * goods.getJSONObject(0).getInt(Constance.amount);
                                intent.putExtra(Constance.money, total);
                                intent.putExtra(Constance.address, mAddressObject);
                                mView.startActivity(intent);
//                                    mNetWork.sendUpdateCart(goods.getJSONObject(0).getString(Constance.id), "1", new INetworkCallBack() {
//                                        @Override
//                                        public void onSuccessListener(String requestCode, JSONObject ans) {
//
//                                        }
//
//                                        @Override
//                                        public void onFailureListener(String requestCode, JSONObject ans) {
//                                            mView.hideLoading();
//                                        }
//                                    });

                            }

                            @Override
                            public void onFailureListener(String requestCode, JSONObject ans) {
                                mView.hideLoading();
                            }
                        });
                    }

                    @Override
                    public void onFailureListener(String requestCode, JSONObject ans) {
                        mView.hideLoading();
                    }
                });

            }

            @Override
            public void onFailureListener(String requestCode, JSONObject ans) {
                mView.hideLoading();
            }
        });

    }

    /**
     * 随心配
     */
    public void goPhoto() {
        if (AppUtils.isEmpty(mView.goodses)) {
            MyToast.show(mView, "还没加载完毕，请稍后再试");
            return;
        }
        mIntent = new Intent(mView, TestCameraActivity.class);
        mIntent.putExtra(Constance.product, mView.goodses);
        mIntent.putExtra(Constance.property, mView.mProperty);
        mView.startActivity(mIntent);
    }

    @Override
    protected void handleMessage(int action, Object[] values) {

    }

    @Override
    protected void myHandleMessage(Message msg) {

    }
}
