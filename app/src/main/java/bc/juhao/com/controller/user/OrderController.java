package bc.juhao.com.controller.user;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alipay.sdk.app.PayTask;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.NetUtils;
import com.lib.common.hxp.view.ListViewForScrollView;
import com.lib.common.hxp.view.PullToRefreshLayout;

import java.text.DecimalFormat;

import bc.juhao.com.R;
import bc.juhao.com.bean.OrderInfo;
import bc.juhao.com.bean.PayResult;
import bc.juhao.com.chat.DemoHelper;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.cons.NetWorkConst;
import bc.juhao.com.controller.BaseController;
import bc.juhao.com.listener.INetworkCallBack;
import bc.juhao.com.listener.INetworkCallBack02;
import bc.juhao.com.listener.IUpdateProductPriceListener;
import bc.juhao.com.ui.activity.ChartListActivity;
import bc.juhao.com.ui.activity.IssueApplication;
import bc.juhao.com.ui.activity.buy.ExInventoryActivity;
import bc.juhao.com.ui.activity.user.ChatActivity;
import bc.juhao.com.ui.activity.user.ConsignmentOrderActivity;
import bc.juhao.com.ui.activity.user.OrderDetailActivity;
import bc.juhao.com.ui.adapter.OrderGvAdapter;
import bc.juhao.com.ui.fragment.OrderFragment;
import bc.juhao.com.ui.view.ShowDialog;
import bc.juhao.com.utils.DateUtils;
import bc.juhao.com.utils.MyShare;
import bc.juhao.com.utils.UIUtils;
import bocang.json.JSONObject;
import bocang.utils.AppDialog;
import bocang.utils.AppUtils;
import bocang.utils.IntentUtil;
import bocang.utils.LogUtils;
import bocang.utils.MyLog;
import bocang.utils.MyToast;

import static bc.juhao.com.cons.Constance.category;
import static bc.juhao.com.cons.Constance.goods;
import static bc.juhao.com.cons.Constance.total_amount;

/**
 * @author: Jun
 * @date : 2017/2/6 15:13
 * @description :
 */
public class OrderController extends BaseController implements PullToRefreshLayout.OnRefreshListener, INetworkCallBack, OnItemClickListener {
    private OrderFragment mView;
    public JSONArray goodses;
    private PullToRefreshLayout mPullToRefreshLayout;
    private ProAdapter mProAdapter;
    private ListViewForScrollView order_sv;
    private int page = 1;

    private View mNullView;
    private View mNullNet;
    private Button mRefeshBtn;
    private TextView mNullNetTv;
    private TextView mNullViewTv;
    private int per_pag = 20;
    private ProgressBar pd;
    private int mPosition;
    private ImageView iv;
    private Button go_btn;
    private Boolean mIsUpdate = false;
    //    private int mProductAgio=80;
    private int mProductPosition = 0;
    private JSONArray mProductArray;
    private float mDiscount;
    private float mProductDiscount=0;//返回产品打折范围


    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    /**
                     * 同步返回的结果必须上传到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                     * docType=1) 建议商户依赖异步通知
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    Log.d("TAG", "resultStatus=" + resultStatus);
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        page = 1;
                        sendOrderList(page);
                        Intent intent = new Intent(mView.getActivity(), OrderDetailActivity.class);
                        intent.putExtra(Constance.order, goodses.getJSONObject(mPosition).toJSONString());
                        intent.putExtra(Constance.state, 1);
                        mView.getActivity().startActivity(intent);

                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            AppDialog.messageBox("支付结果确认中");

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            AppDialog.messageBox("支付失败");
                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };


    public OrderController(OrderFragment v) {
        try {
            mView = v;
            initView();
            initViewData();
        } catch (Exception e) {

        }
    }

    public void initViewData() {
        page = 1;
        //        pd.setVisibility(View.VISIBLE);
        mView.setShowDialog(true);
        mView.showLoading();
        sendOrderList(page);

    }

    private void sendOrderList(final int page) {
        mNetWork.sendorderList(page, per_pag, mView.list.get(mView.flag), new INetworkCallBack02() {
            @Override
            public void onSuccessListener(String requestCode, com.alibaba.fastjson.JSONObject ans) {
                mView.hideLoading();
                switch (requestCode) {
                    case NetWorkConst.ORDERLIST:
                        if (null == mView.getActivity() || mView.getActivity().isFinishing())
                            return;
                        if (null != mPullToRefreshLayout) {
                            dismissRefesh();
                        }
                        break;
                }

                JSONArray goodsList = ans.getJSONArray(Constance.orders);
                if (AppUtils.isEmpty(goodsList) || goodsList.size() == 0) {
                    if (page == 1) {
                        mNullView.setVisibility(View.VISIBLE);
                        iv.setImageResource(R.drawable.icon_no_order);
                        go_btn.setVisibility(View.GONE);
                    }

                    dismissRefesh();
                    return;
                }

                mNullView.setVisibility(View.GONE);
                mNullNet.setVisibility(View.GONE);
                go_btn.setVisibility(View.GONE);
                getDataSuccess(goodsList);
            }

            @Override
            public void onFailureListener(String requestCode, com.alibaba.fastjson.JSONObject ans) {
                mView.hideLoading();
                MyToast.show(mView.getActivity(), "网络异常，请重新加载");
            }
        });
    }

    private void initView() {

        mPullToRefreshLayout = ((PullToRefreshLayout) mView.getView().findViewById(R.id.refresh_view));
        mPullToRefreshLayout.setOnRefreshListener(this);
        order_sv = (ListViewForScrollView) mView.getView().findViewById(R.id.order_sv);
        order_sv.setDivider(null);//去除listview的下划线
        mProAdapter = new ProAdapter();
        order_sv.setAdapter(mProAdapter);
        mNullView = mView.getView().findViewById(R.id.null_view);
        mNullNet = mView.getView().findViewById(R.id.null_net);

        mRefeshBtn = (Button) mNullNet.findViewById(R.id.refesh_btn);
        mNullNetTv = (TextView) mNullNet.findViewById(R.id.tv);
        mNullViewTv = (TextView) mNullView.findViewById(R.id.tv);
        go_btn = (Button) mNullView.findViewById(R.id.go_btn);
        go_btn.setText("去逛逛");
        iv = (ImageView) mNullView.findViewById(R.id.iv);
        pd = (ProgressBar) mView.getView().findViewById(R.id.pd);


        imm = (InputMethodManager) mView.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        //拓展窗口
        mAlertViewExt = new AlertView("提示", "请输入价格折扣(不能低于8折)！", "取消", null, new String[]{"完成"}, mView.getActivity(), AlertView.Style.Alert, this);
        ViewGroup extView = (ViewGroup) LayoutInflater.from(mView.getActivity()).inflate(R.layout.alertext_form, null);
        etName = (EditText) extView.findViewById(R.id.etName);
        etName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focus) {
                //输入框出来则往上移动
                boolean isOpen = imm.isActive();
                mAlertViewExt.setMarginBottom(isOpen && focus ? 120 : 0);
                System.out.println(isOpen);
            }
        });
        mAlertViewExt.addExtView(extView);
        imm02 = (InputMethodManager) mView.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        //拓展窗口
        mAlertViewExt02 = new AlertView("提示", "请输入价格折扣！", "取消", null, new String[]{"完成"}, mView.getActivity(), AlertView.Style.Alert, this);
        ViewGroup extView02 = (ViewGroup) LayoutInflater.from(mView.getActivity()).inflate(R.layout.alertext_form, null);
        etName02 = (EditText) extView02.findViewById(R.id.etName);
        etName02.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focus) {
                //输入框出来则往上移动
                boolean isOpen = imm02.isActive();
                mAlertViewExt02.setMarginBottom(isOpen && focus ? 120 : 0);
                System.out.println(isOpen);
            }
        });
        mAlertViewExt02.addExtView(extView02);


    }

    @Override
    protected void handleMessage(int action, Object[] values) {

    }

    @Override
    protected void myHandleMessage(Message msg) {

    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        page = 1;
        sendOrderList(page);
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        sendOrderList(++page);
    }

    @Override
    public void onSuccessListener(String requestCode, JSONObject ans) {
    }

    private void dismissRefesh() {
        mPullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
        mPullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
    }

    private void getDataSuccess(JSONArray array) {
        if (1 == page)
            goodses = array;
        else if (null != goodses) {
            for (int i = 0; i < array.size(); i++) {
                goodses.add(array.getJSONObject(i));
            }

            if (AppUtils.isEmpty(array))
                MyToast.show(mView.getActivity(), "没有更多内容了");
        }
        mProAdapter.notifyDataSetChanged();
    }

    private View.OnClickListener mRefeshBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onRefresh();
        }
    };

    public void onRefresh() {
        page = 1;
        sendOrderList(page);
        //        sendGoodsList(IssueApplication.mCId, page, 1, "is_best", null);
    }


    @Override
    public void onFailureListener(String requestCode, JSONObject ans) {
        mView.hideLoading();
        if (null == mView.getActivity() || mView.getActivity().isFinishing())
            return;
        this.page--;

        if (AppUtils.isEmpty(ans)) {
            mNullNet.setVisibility(View.VISIBLE);
            mRefeshBtn.setOnClickListener(mRefeshBtnListener);
            return;
        }

        if (null != mPullToRefreshLayout) {
            mPullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
            mPullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
        }
    }

    /**
     * 支付订单
     *
     * @param order
     * @param code
     */
    private void sendPayment(String order, String code) {
        mNetWork.sendPayment(order, code, new INetworkCallBack02() {
            @Override
            public void onSuccessListener(String requestCode, com.alibaba.fastjson.JSONObject ans) {
                mView.hideLoading();
                String notify_url = ans.getString(Constance.alipay);
                if (AppUtils.isEmpty(notify_url))
                    return;
                SubmitAliPay(notify_url);
            }

            @Override
            public void onFailureListener(String requestCode, com.alibaba.fastjson.JSONObject ans) {
                mView.hideLoading();
                if (null == mView)
                    return;
                if (AppUtils.isEmpty(ans)) {
                    AppDialog.messageBox(UIUtils.getString(R.string.server_error));
                    return;
                }
                AppDialog.messageBox(ans.getString(Constance.error_desc));
            }
        });

    }

    //标记是支付
    private static final int SDK_PAY_FLAG = 1;
    private static final String TAG = "PayActivity";

    /**
     * 支付宝支付
     */
    private void SubmitAliPay(String notifyUrl) {
        //开始支付
        aliPay(notifyUrl);
    }

    /**
     * create the order info. 创建订单信息
     */
    private String createOrderInfo(OrderInfo order) {

        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + order.getPartner() + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + order.getSeller_id() + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + order.getOut_trade_no() + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + order.getSubject() + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + order.getBody() + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + order.getTotal_fee() + "\"";

        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + order.getNotify_url() + "\"";

        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";

        return orderInfo;
    }

    /**
     * 开始-支付宝支付
     */
    private void aliPay(final String ss) {
        //        try {
        //            /**
        //             * 仅需对sign 做URL编码
        //             */
        //            sign = URLEncoder.encode(sign, "UTF-8");
        //        } catch (UnsupportedEncodingException e) {
        //            e.printStackTrace();
        //        }
        //
        //        /**
        //         * 完整的符合支付宝参数规范的订单信息
        //         */
        //        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + getSignType();

        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(mView.getActivity());
                // 调用支付接口，获取支付结果
                String result = alipay.pay(ss, true);

                //异步处理支付结果
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * get the sign type we use. 获取签名方式
     */
    private String getSignType() {
        return "sign_type=\"RSA\"";
    }

    String mBody = "";
    int mLevel = -1;
    int mOrderLevel = -1;

    private class ProAdapter extends BaseAdapter implements INetworkCallBack {
        public ProAdapter() {
        }

        @Override
        public int getCount() {
            if (null == goodses)
                return 0;
            return goodses.size();
        }

        @Override
        public com.alibaba.fastjson.JSONObject getItem(int position) {
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
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(mView.getActivity(), R.layout.item_order_one, null);

                holder = new ViewHolder();
                holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
                holder.state_tv = (TextView) convertView.findViewById(R.id.state_tv);
                holder.time_tv = (TextView) convertView.findViewById(R.id.time_tv);
                holder.do_tv = (TextView) convertView.findViewById(R.id.do_tv);
                holder.do02_tv = (TextView) convertView.findViewById(R.id.do02_tv);
                holder.consigment_tv = (TextView) convertView.findViewById(R.id.consigment_tv);
                holder.do03_tv = (TextView) convertView.findViewById(R.id.do03_tv);
                holder.chat_buy_tv = (TextView) convertView.findViewById(R.id.chat_buy_tv);
                holder.code_tv = (TextView) convertView.findViewById(R.id.code_tv);
                holder.total_tv = (TextView) convertView.findViewById(R.id.total_tv);
                holder.remark_tv = (TextView) convertView.findViewById(R.id.remark_tv);
                holder.lv = (ListView) convertView.findViewById(R.id.lv);
                holder.order_lv = (LinearLayout) convertView.findViewById(R.id.order_lv);
                holder.update_money_tv = (TextView) convertView.findViewById(R.id.update_money_tv);
                holder.old_money = (TextView) convertView.findViewById(R.id.old_money);
                holder.new_money = (TextView) convertView.findViewById(R.id.new_money);
                holder.pdf_tv = (TextView) convertView.findViewById(R.id.pdf_tv);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final com.alibaba.fastjson.JSONObject orderobject = goodses.getJSONObject(position);
            com.alibaba.fastjson.JSONArray goods=orderobject.getJSONArray(Constance.goods);
            com.alibaba.fastjson.JSONObject group_buy=new com.alibaba.fastjson.JSONObject();
            int group_buyint=-1;
            if(!AppUtils.isEmpty(goods)){
                for(int i=0;i<goods.size();i++){
                try {
                    group_buy= (com.alibaba.fastjson.JSONObject) goods.getJSONObject(i).get(Constance.group_buy);
                    if(!AppUtils.isEmpty(group_buy)&&group_buyint!=0)break;
                }catch (Exception e){
                   group_buyint=goods.getJSONObject(i).getInteger(Constance.group_buy);
                    if(!AppUtils.isEmpty(group_buy)&&group_buyint!=0||group_buyint==212)break;
                }
                }
            }
            String isJh=goods.getJSONObject(0).getString(Constance.is_jh);
            boolean isJuhao=false;
            /*if(!AppUtils.isEmpty(group_buy)&&group_buy.size()>0&&!group_buy.equals("0")||group_buyint==212){
                isJuhao=true;
            }*/
            if(isJh.equals("1")){
                isJuhao=true;
            }
//            LogUtils.logE("isJuhao",isJuhao+"");
            final int state = orderobject.getInteger(Constance.status);
            int tatalNum = 0;
            final String total = orderobject.getString(Constance.total);
            final String orderId = orderobject.getString(Constance.id);

            final String sn = orderobject.getString(Constance.sn);
            final String id = orderobject.getString(Constance.id);
            holder.code_tv.setText("订单号:" + orderobject.getString(Constance.sn));
            final String discount = orderobject.getString(Constance.discount);
            final JSONArray array = orderobject.getJSONArray(Constance.goods);
            holder.time_tv.setText(DateUtils.getStrTime(orderobject.getString(Constance.created_at)));
            final String score = orderobject.getJSONObject("score").getString("score");

            String str1 = "市场价:￥" + score;
            holder.old_money.setText(str1);

            Double avg = ((Double.parseDouble(total) / Double.parseDouble(score)) * 100);
            Log.e("520it", position + ":" + avg + "  :" + Double.parseDouble(total) + "  :" + (Double.parseDouble(score)) + "  :" + (Double.parseDouble(total) / Double.parseDouble(score)));
            if (avg == 100) {
                String str = "优惠价:￥" + total;
                int fstart = str.indexOf(total);
                int fend = fstart + total.length();
                SpannableStringBuilder style = new SpannableStringBuilder(str);
                style.setSpan(new ForegroundColorSpan(Color.RED), fstart, fend, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                holder.new_money.setText(style);
            } else {
                DecimalFormat decimalFormat = new DecimalFormat(".0");//构造方法的字符格式这里如果小数不足2位,会以0补足.
                String p = decimalFormat.format(avg * 0.1);//format 返回的是字符串
                String val = total ;
                String str = "优惠价:￥" + val;
                int fstart = str.indexOf(val);
                int fend = fstart + val.length();
                SpannableStringBuilder style = new SpannableStringBuilder(str);
                style.setSpan(new ForegroundColorSpan(Color.RED), fstart, fend, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                holder.new_money.setText(style);
            }

            for (int i = 0; i < array.size(); i++) {
                tatalNum += array.getJSONObject(i).getInteger(total_amount);
            }

            String str = "共计 " + tatalNum + " 件商品 合计￥" + total + "";
            holder.total_tv.setText(str);
            mOrderLevel = orderobject.getInteger(Constance.level);
            OrderGvAdapter maGvAdapter = new OrderGvAdapter(mView.getActivity(), array, mOrderLevel, state);
            holder.lv.setAdapter(maGvAdapter);
            mLevel = IssueApplication.mUserObject.getInt(Constance.level);
            maGvAdapter.setUpdateProductPriceListener(new IUpdateProductPriceListener() {
                @Override
                public void onUpdateProductPriceListener(int position, com.alibaba.fastjson.JSONObject object) {
                     String itemDiscount=array.getJSONObject(position).getString(Constance.discount);
                    if(AppUtils.isEmpty(itemDiscount) || "0".equals(itemDiscount)){
                        mProductDiscount= (float) 0.8;
                    }else{
                        mProductDiscount=Float.parseFloat(itemDiscount);
                    }
                    mProductArray = array;
                    mUpdateorderId = id;
                    mUpdateorderSn = sn;
                    mDiscount = Float.parseFloat(discount);
                    mProductPosition = position;
                    mAlertViewExt02.show();
                }
            });

            String user_name = orderobject.getString(Constance.user_name);

            String levelValue = "";
            if (mOrderLevel == 0) {
                levelValue = "一级";
            } else if (mOrderLevel == 1) {
                levelValue = "二级";
            } else if (mOrderLevel == 2) {
                levelValue = "三级";
            } else {
                levelValue = "消费者";
            }
            getState(state, holder.state_tv, holder.do_tv, holder.do02_tv, holder.do03_tv);

            levelValue = levelValue + "(" + user_name + ")";
            holder.remark_tv.setText(levelValue);
            holder.remark_tv.setVisibility(View.VISIBLE);

            if (mLevel == 0) {

                if (mLevel != mOrderLevel && state == 0) {
                    mIsUpdate = true;
                        holder.update_money_tv.setVisibility(View.VISIBLE);
                    if (state == 0) {
                        holder.do_tv.setVisibility(View.GONE);
                        holder.do02_tv.setVisibility(View.GONE);
                        holder.do03_tv.setVisibility(View.GONE);
                        holder.chat_buy_tv.setVisibility(View.VISIBLE);

                    }

                } else {
                    holder.update_money_tv.setVisibility(View.GONE);
                    mIsUpdate = false;
                    holder.chat_buy_tv.setVisibility(View.GONE);
                }

                if (state == 1&&!isJuhao) {
                    holder.consigment_tv.setVisibility(View.VISIBLE);
                } else {
                    holder.consigment_tv.setVisibility(View.GONE);
                }

                if(isJuhao)
                {
                    holder.update_money_tv.setVisibility(View.GONE);
                }

            } else {
                holder.update_money_tv.setVisibility(View.GONE);
                mIsUpdate = false;
                holder.consigment_tv.setVisibility(View.GONE);
            }

            //修改价格
            holder.update_money_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mProductArray = array;
                    mUpdateOrderMoney = Float.parseFloat(score);
                    mUpdateorderSn = sn;
                    mDiscount = Float.parseFloat(discount);
                    mAlertViewExt.show();
                }
            });


            final boolean finalIsJuhao = isJuhao;
            holder.do03_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mLevel == 0) {
                        if (!mView.isToken()) {
                            IntentUtil.startActivity(mView.getActivity(), ChartListActivity.class, false);
                        }
                        return;
                    }
                    int id = MyShare.get(mView.getActivity()).getInt(Constance.USERCODEID);
                    if (id == 0) {
                        MyToast.show(mView.getActivity(), "该用户没有客服信息!");
                    } else {


                        String parent_name = IssueApplication.mUserObject.getString("parent_name");
                        String parent_id = IssueApplication.mUserObject.getString("parent_id");
                        if(finalIsJuhao){
                            parent_id="37";
                            parent_name="钜豪超市";
                        }
                        String userIcon = NetWorkConst.SCENE_HOST + IssueApplication.mUserObject.getString("parent_avatar");
                        sendCall("尝试连接聊天服务..请连接?", parent_id, parent_name, userIcon);
                        //                        IntentUtil.startActivity(mView.getActivity(), MerchantInfoActivity.class, false);
                    }
                    //                    mView.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(NetWorkConst.QQURL)));
                }
            });
            holder.do_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO
                    if (state == 1) {
                        if (mLevel == 0) {
                            if (!mView.isToken()) {
                                IntentUtil.startActivity(mView.getActivity(), ChartListActivity.class, false);
                            }
                            return;
                        }
                        int id = MyShare.get(mView.getActivity()).getInt(Constance.USERCODEID);
                        if (id == 0) {
                            MyToast.show(mView.getActivity(), "该用户没有客服信息!");
                        } else {
                            String parent_name = IssueApplication.mUserObject.getString("parent_name");
                            String parent_id = IssueApplication.mUserObject.getString("parent_id");
                            String userIcon = NetWorkConst.SCENE_HOST + IssueApplication.mUserObject.getString("parent_avatar");
                            sendCall("尝试连接聊天服务..请连接?", parent_id, parent_name, userIcon);
                            //                            IntentUtil.startActivity(mView.getActivity(), MerchantInfoActivity.class, false);
                        }
                        //                        mView.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(NetWorkConst.QQURL)));
                    } else if (state == 0) {

                        for (int i = 0; i < array.size(); i++) {
                            mBody += array.getJSONObject(i).getJSONObject(Constance.product).getString(Constance.name) + "  ";
                        }
                        mPosition = position;
                        mView.setShowDialog(true);
                        mView.setShowDialog("正在付款中!");
                        mView.showLoading();
                        sendPayment(orderId, "alipay.app");
                    } else if (state == 2) {
                        //TODO 确认收货
                        sendConfirmReceipt(orderId);
                    } else if (state == 5) {
                        int id = MyShare.get(mView.getActivity()).getInt(Constance.USERCODEID);
                        if (id == 0) {
                            MyToast.show(mView.getActivity(), "该用户没有客服信息!");
                        } else {
                            String parent_name = IssueApplication.mUserObject.getString("parent_name");
                            String parent_id = IssueApplication.mUserObject.getString("parent_id");
                            String userIcon = NetWorkConst.SCENE_HOST + IssueApplication.mUserObject.getString("parent_avatar");
                            sendCall("尝试连接聊天服务..请连接?", parent_id, parent_name, userIcon);
                        }
                        //                        mView.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(NetWorkConst.QQURL)));
                    } else if (state == 4 || state == 3) {
                        if (mLevel == 0) {
                            if (!mView.isToken()) {
                                IntentUtil.startActivity(mView.getActivity(), ChartListActivity.class, false);
                            }
                            return;
                        }
                        int id = MyShare.get(mView.getActivity()).getInt(Constance.USERCODEID);
                        if (id == 0) {
                            MyToast.show(mView.getActivity(), "该用户没有客服信息!");
                        } else {
                            String parent_name = IssueApplication.mUserObject.getString("parent_name");
                            String parent_id = IssueApplication.mUserObject.getString("parent_id");
                            String userIcon = NetWorkConst.SCENE_HOST + IssueApplication.mUserObject.getString("parent_avatar");
                            sendCall("尝试连接聊天服务..请连接?", parent_id, parent_name, userIcon);
                        }
                    }
                }
            });
            holder.do02_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (state == 0) {
                        mView.setShowDialog(true);
                        mView.setShowDialog("正在取消中!");
                        mView.showLoading();
                        sendOrderCancel(orderId, "1");
                    }

                }
            });

            holder.order_lv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mView.getActivity(), OrderDetailActivity.class);
                    intent.putExtra(Constance.order, orderobject.toJSONString());
                    mView.getActivity().startActivity(intent);
                }
            });

            holder.lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(mView.getActivity(), OrderDetailActivity.class);
                    intent.putExtra(Constance.order, orderobject.toJSONString());
                    mView.getActivity().startActivity(intent);
                }
            });

            /**
             * 联系买家
             */
            holder.chat_buy_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String user_name = orderobject.getString(Constance.user_name);
                    String user_id = orderobject.getString(Constance.user_id);
                    String user_icon = NetWorkConst.SCENE_HOST + orderobject.getJSONObject(Constance.avatar).getString(Constance.avatar);
                    sendCall("尝试连接聊天服务..请连接?", user_id, user_name, user_icon);
                }
            });

            holder.pdf_tv.setOnClickListener(new View.OnClickListener() {//生成PDF
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mView.getActivity(), ExInventoryActivity.class);
                    intent.putExtra(Constance.goods, orderobject.toJSONString());
                    intent.putExtra(Constance.pdfType, 1);
                    mView.getActivity().startActivity(intent);
                }
            });

            //确认发货
            holder.consigment_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mView.getActivity(), ConsignmentOrderActivity.class);
                    intent.putExtra(Constance.order, orderobject.toJSONString());
                    intent.putExtra(Constance.type, true);
                    mView.getActivity().startActivity(intent);
                }
            });

            return convertView;
        }

        /**
         * 确认收货
         *
         * @param order
         */
        private void sendConfirmReceipt(final String order) {
            ShowDialog mDialog = new ShowDialog();
            mDialog.show(mView.getActivity(), "提示", "是否确认收货?", new ShowDialog.OnBottomClickListener() {
                @Override
                public void positive() {
                    mView.setShowDialog(true);
                    mView.setShowDialog("确认收货中!");
                    mView.showLoading();
                    mNetWork.sendConfirmReceipt(order, new INetworkCallBack02() {
                        @Override
                        public void onSuccessListener(String requestCode, com.alibaba.fastjson.JSONObject ans) {
                            mView.hideLoading();
                            MyToast.show(mView.getActivity(), "确认收货成功!");
                            page = 1;
                            sendOrderList(page);
                            Intent intent = new Intent(mView.getActivity(), OrderDetailActivity.class);
                            intent.putExtra(Constance.order, goodses.getJSONObject(mPosition).toJSONString());
                            intent.putExtra(Constance.state, 3);
                            mView.getActivity().startActivity(intent);
                        }

                        @Override
                        public void onFailureListener(String requestCode, com.alibaba.fastjson.JSONObject ans) {
                            mView.hideLoading();
                            MyToast.show(mView.getActivity(), "确认收货失败!");
                        }
                    });
                }

                @Override
                public void negtive() {

                }
            });

        }


        private void sendOrderCancel(String order, String reason) {
            mNetWork.sendOrderCancel(order, reason, this);
        }

        /**
         * 订单状态
         *
         * @param type
         * @param state_tv
         * @param do_tv
         * @param do02_tv
         */
        private void getState(int type, TextView state_tv, TextView do_tv, TextView do02_tv, TextView do03_tv) {
            do_tv.setVisibility(View.GONE);
            do02_tv.setVisibility(View.GONE);
            do03_tv.setVisibility(View.GONE);
            String stateValue = "";
            switch (type) {
                case 0:
                    do03_tv.setVisibility(View.VISIBLE);
                    stateValue = "【待付款】";
                    do_tv.setVisibility(View.VISIBLE);
                    do02_tv.setVisibility(View.VISIBLE);
                    do_tv.setText("付款");
                    do02_tv.setText("取消订单");

                    break;
                case 1:
                    stateValue = "【待发货】";
                    do_tv.setVisibility(View.VISIBLE);
                    do_tv.setText("联系商家");

                    break;
                case 2:
                    do03_tv.setVisibility(View.VISIBLE);
                    stateValue = "【待收货】";

                    if (mLevel == mOrderLevel) {
                        do_tv.setVisibility(View.VISIBLE);
                    }
                    do_tv.setText("确认收货");
                    break;
                case 3:
                    do_tv.setVisibility(View.VISIBLE);
                    do_tv.setText("联系商家");
                    stateValue = "【已完成】";
                    break;
                case 4:
                    do_tv.setVisibility(View.VISIBLE);
                    do_tv.setText("联系商家");
                    stateValue = "【已完成】";
                    break;
                case 5:
                    do_tv.setVisibility(View.VISIBLE);
                    do_tv.setText("联系商家");
                    stateValue = "【已取消】";
                    break;
            }
            state_tv.setText(stateValue);
        }

        @Override
        public void onSuccessListener(String requestCode, JSONObject ans) {
            switch (requestCode) {
                case NetWorkConst.ORDERCANCEL:
                    if (ans.getInt(Constance.error_code) == 0) {
                        page = 1;
                        sendOrderList(page);
                    } else {
                        mView.hideLoading();
                        MyToast.show(mView.getActivity(), "订单取消失败!");
                    }

                    break;
                case NetWorkConst.PAYMENT:
                    break;


            }
        }

        @Override
        public void onFailureListener(String requestCode, JSONObject ans) {
            MyToast.show(mView.getActivity(), "2");
            mView.hideLoading();
            MyToast.show(mView.getActivity(), "支付失败!");

        }

        class ViewHolder {
            ImageView imageView;
            TextView state_tv;
            TextView time_tv;
            TextView do_tv;
            TextView do02_tv;
            TextView do03_tv;
            TextView consigment_tv;
            TextView pdf_tv;
            TextView chat_buy_tv;
            TextView code_tv;
            TextView total_tv;
            ListView lv;
            LinearLayout order_lv;
            TextView remark_tv;
            TextView update_money_tv;
            TextView old_money;
            TextView new_money;

        }
    }

    private AlertView mAlertViewExt;//窗口拓展例子
    private EditText etName;//拓展View内容
    private InputMethodManager imm;
    private AlertView mAlertViewExt02;//窗口拓展例子
    private EditText etName02;//拓展View内容
    private InputMethodManager imm02;

    private void closeKeyboard() {
        //关闭软键盘
        imm.hideSoftInputFromWindow(etName.getWindowToken(), 0);
        //恢复位置
        mAlertViewExt.setMarginBottom(0);
    }

    private void closeKeyboard02() {
        //关闭软键盘
        imm02.hideSoftInputFromWindow(etName02.getWindowToken(), 0);
        //恢复位置
        mAlertViewExt02.setMarginBottom(0);
    }

    private float mUpdateOrderMoney = 0;
    private String mUpdateorderId = "";
    private String mUpdateorderSn = "";

    @Override
    public void onItemClick(Object o, int position) {
        closeKeyboard();
        closeKeyboard02();
        //判断是否是拓展窗口View，而且点击的是非取消按钮
        if (o == mAlertViewExt && position != AlertView.CANCELPOSITION) {
            String name = etName.getText().toString();
            if (name.isEmpty()) {
                //                        MyToast.show(mView.getActivity(),"请输入需要查询的订单号!");
            } else {
                Float ratio = Float.parseFloat(name);
                if (ratio < 8) {
                    MyToast.show(mView.getActivity(), "输入的价格折扣不能小于8折");
                    return;
                } else if (ratio > 10) {
                    MyToast.show(mView.getActivity(), "输入的价格折扣不能大于10折");
                    return;
                }
                mDiscount = (float) (ratio * 0.1);
                mTotalProductMoney = 0;
                for (int i = 0; i < mProductArray.size(); i++) {
                    com.alibaba.fastjson.JSONObject itemObject = mProductArray.getJSONObject(i);
                    String total_price = itemObject.getString(Constance.total_price);
                    mTotalProductMoney = mTotalProductMoney + Float.parseFloat(total_price);
                }
                float money = (float) (mTotalProductMoney * mDiscount);

                mView.setShowDialog(true);
                mView.setShowDialog("正在修改订单价格!");
                mView.showLoading();
                updatePrice(mUpdateorderSn, money, mDiscount + "");
                //                        mController.SearchOrder(name);
            }

            return;
        } else if (o == mAlertViewExt02 && position != AlertView.CANCELPOSITION) {
            String name = etName02.getText().toString();
            if (name.isEmpty()) {
                //                        MyToast.show(mView.getActivity(),"请输入需要查询的订单号!");
            } else {
                Float ratio = Float.parseFloat(name);
                if (ratio < mProductDiscount*10) {
                    int num= (int) (mProductDiscount*10);
                    MyToast.show(mView.getActivity(), "输入的价格折扣不能小于"+num+"折");
                    return;
                } else if (ratio > 10) {
                    MyToast.show(mView.getActivity(), "输入的价格折扣不能大于10折");
                    return;
                }

                com.alibaba.fastjson.JSONObject jsonObject = mProductArray.getJSONObject(mProductPosition);
                String id = jsonObject.getString(Constance.id);
                String price = jsonObject.getString(Constance.original_price);
                int total_amount = jsonObject.getInteger(Constance.total_amount);

                float productPrice = (float) (Float.parseFloat(price) * (ratio * 0.1));

                mView.setShowDialog(true);
                mView.setShowDialog("正在修改订单产品价格!");
                mView.showLoading();
                mTotalProductMoney = 0;
                for (int i = 0; i < mProductArray.size(); i++) {
                    if (i == mProductPosition) {
                        mTotalProductMoney = mTotalProductMoney + productPrice * total_amount;
                    } else {
                        com.alibaba.fastjson.JSONObject itemObject = mProductArray.getJSONObject(i);
                        String total_price = itemObject.getString(Constance.total_price);
                        mTotalProductMoney = mTotalProductMoney + Float.parseFloat(total_price);
                    }
                }
                //判断是否有折扣
                if (mDiscount != 0) {
                    mTotalProductMoney = mTotalProductMoney * mDiscount;
                } else {
                    mDiscount = 1;
                }

                updateProductPrice(mUpdateorderId, id, productPrice + "");

            }
        }
    }

    public double mTotalProductMoney = 0;

    /**
     * 修改订单价格
     *
     * @param orderId 订单号
     * @param money   价格
     */
    private void updatePrice(String orderId, double money, String discount) {
        mNetWork.updatePrice(orderId, money, discount, new INetworkCallBack02() {
            @Override
            public void onSuccessListener(String requestCode, com.alibaba.fastjson.JSONObject ans) {
                mView.hideLoading();
                MyToast.show(mView.getActivity(), "修改成功!");
                initViewData();
            }

            @Override
            public void onFailureListener(String requestCode, com.alibaba.fastjson.JSONObject ans) {
                mView.hideLoading();
                MyToast.show(mView.getActivity(), "修改价格失败!");
            }
        });
    }


    /**
     * 修改订单产品价格
     *
     * @param orderId
     * @param goods_id
     * @param goods_amount
     */
    private void updateProductPrice(String orderId, String goods_id, String goods_amount) {
        mNetWork.updateProductPrice(orderId, goods_id, goods_amount, new INetworkCallBack02() {
            @Override
            public void onSuccessListener(String requestCode, com.alibaba.fastjson.JSONObject ans) {
                mView.hideLoading();
                updatePrice(mUpdateorderSn, mTotalProductMoney, mDiscount + "");
            }

            @Override
            public void onFailureListener(String requestCode, com.alibaba.fastjson.JSONObject ans) {
                mView.hideLoading();
                MyToast.show(mView.getActivity(), "修改价格失败!");
            }
        });
    }


    /**
     * 联系客服
     */
    public void sendCall(String msg, final String parent_id, final String parent_name, final String userIcon) {
        try {
            //            if (AppUtils.isEmpty(IssueApplication.mUserObject.getString("parent_name"))) {
            //                MyToast.show(mView.getActivity(), "不能和自己聊天!");
            //                return;
            //            }

            EaseUser user = new EaseUser(parent_id);
            user.setNickname(parent_name);
            user.setNick(parent_name);
            user.setAvatar(userIcon);
            DemoHelper.getInstance().saveContact(user);


            if (!EMClient.getInstance().isLoggedInBefore()) {
                ShowDialog mDialog = new ShowDialog();
                mDialog.show(mView.getActivity(), "提示", msg, new ShowDialog.OnBottomClickListener() {
                    @Override
                    public void positive() {
                        loginHX(parent_id, parent_name, userIcon);
                    }

                    @Override
                    public void negtive() {

                    }
                });
            } else {
                EMClient.getInstance().contactManager().acceptInvitation(parent_id);
                mView.startActivity(new Intent(mView.getActivity(), ChatActivity.class).putExtra(EaseConstant.EXTRA_USER_ID, parent_id));
            }
        } catch (HyphenateException e) {
            e.printStackTrace();
        }
    }

    //登录环信
    private void loginHX(final String parent_id, final String parent_name, final String userIcon) {
        final Toast toast = Toast.makeText(mView.getActivity(), "服务器连接中...!", Toast.LENGTH_SHORT);
        toast.show();
        if (NetUtils.hasNetwork(mView.getActivity())) {
            final String uid = MyShare.get(mView.getActivity()).getString(Constance.USERID);
            if (AppUtils.isEmpty(uid)) {
                return;
            }
            if (!TextUtils.isEmpty(uid)) {
                getSuccessLogin(uid, toast, parent_id, parent_name, userIcon);


            }
        }
    }

    private void getSuccessLogin(final String uid, final Toast toast, final String parent_id, final String parent_name, final String userIcon) {
        EMClient.getInstance().login(uid, uid, new EMCallBack() {
            @Override
            public void onSuccess() {
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                MyLog.e("登录环信成功!");
                toast.cancel();
                String parent_name = IssueApplication.mUserObject.getString("parent_name");
                try {
                    EMClient.getInstance().contactManager().acceptInvitation(parent_id);
                    mView.startActivity(new Intent(mView.getActivity(), ChatActivity.class).putExtra(EaseConstant.EXTRA_USER_ID, parent_id));
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
                                mView.getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        MyLog.e("注册成功!");
                                        getSuccessLogin(uid, toast, parent_id, parent_name, userIcon);
                                    }
                                });

                            } catch (final HyphenateException e) {
                                mView.getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        MyLog.e("注册失败!");
                                        getSuccessLogin(uid, toast, parent_id, parent_name, userIcon);
                                    }
                                });

                            }
                        }
                    }).start();
                } else {
                    Toast.makeText(mView.getActivity(), "连接失败,请重试!", Toast.LENGTH_SHORT).show();
                    MyLog.e("登录环信失败!");
                    sendCall("连接聊天失败,请重试?", parent_id, parent_name, userIcon);
                }


            }

            @Override
            public void onProgress(int i, String s) {
            }
        });
    }


}
