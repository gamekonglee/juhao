package bc.juhao.com.controller.product;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.convenientbanner.CBPageAdapter;
import com.bigkoo.convenientbanner.CBViewHolderCreator;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.lib.common.hxp.view.GridViewForScrollView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bc.juhao.com.R;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.cons.NetWorkConst;
import bc.juhao.com.controller.BaseController;
import bc.juhao.com.listener.INetworkCallBack;
import bc.juhao.com.ui.activity.product.ProDetailActivity;
import bc.juhao.com.ui.activity.product.TimeBuyActivity;
import bc.juhao.com.ui.fragment.home.TimeBuyFragment;
import bc.juhao.com.ui.view.HorizontalListView;
import bc.juhao.com.ui.view.countdownview.CountdownView;
import bc.juhao.com.utils.DateUtils;
import bc.juhao.com.utils.UIUtils;
import bocang.json.JSONArray;
import bocang.json.JSONObject;
import bocang.utils.AppDialog;
import bocang.utils.AppUtils;
import bocang.view.BaseActivity;

import static bc.juhao.com.R.id.name_tv;
import static bc.juhao.com.R.id.price_tv;
import static bc.juhao.com.R.id.remaining_num_tv;

/**
 * @author Jun
 * @time 2018/1/8  21:03
 * @desc ${TODD}
 */

public class TimeBuyController extends BaseController implements CountdownView.OnCountdownEndListener {
    private bc.juhao.com.common.BaseActivity mView;
    private ConvenientBanner mConvenientBanner;
    public int mScreenWidth;
    private List<String> paths = new ArrayList<>();
    private HorizontalListView time_hv;
    private TimeProAdapter mTimeBuyAdapter;
    private JSONArray mTimeBuyDatas = new JSONArray();
    private Map<String, ArrayList<String>> mTimeBuyMap = new HashMap<>();
    private ArrayList<String> mStartTimeArry = new ArrayList<>();
    private ArrayList<String> mEndTimeArry = new ArrayList<>();
    private TextView end_time_tv;
    private CountdownView cv_countdownView;
    private RelativeLayout time_end_rl;
    private GridViewForScrollView priductGv01;
    private TimeBuyProAdapter mTimeBuyProAdapter;
    private ArrayList<String> mDatas;
    private int mPoistion = 0;

    public TimeBuyController(bc.juhao.com.common.BaseActivity v) {
        mView = v;
        initView();
        initViewData();
    }

//    public TimeBuyController(TimeBuyFragment timeBuyFragment) {
//        mView= (bc.juhao.com.common.BaseActivity) timeBuyFragment.getActivity();
//    }

    private void initViewData() {
        sendTimeBuyBanner();
        sendGrouplist(1);
    }

    private void initView() {
        mScreenWidth = mView.getResources().getDisplayMetrics().widthPixels;
        mConvenientBanner = (ConvenientBanner) mView.findViewById(R.id.convenientBanner);
        time_hv = (HorizontalListView) mView.findViewById(R.id.time_hv);
        end_time_tv = (TextView) mView.findViewById(R.id.end_time_tv);
        cv_countdownView = (CountdownView) mView.findViewById(R.id.cv_countdownView);
        time_end_rl = (RelativeLayout) mView.findViewById(R.id.time_end_rl);
        mTimeBuyAdapter = new TimeProAdapter();
        time_hv.setAdapter(mTimeBuyAdapter);
        time_hv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mPoistion=position;
                mDatas = mTimeBuyMap.get(mStartTimeArry.get(mPoistion));
                mTimeBuyProAdapter.notifyDataSetChanged();
                getEndTimeShow(mPoistion);
                mTimeBuyAdapter.notifyDataSetChanged();
            }
        });
        priductGv01 = (GridViewForScrollView) mView.findViewById(R.id.priductGv01);
        mTimeBuyProAdapter = new TimeBuyProAdapter();
        priductGv01.setAdapter(mTimeBuyProAdapter);
        priductGv01.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                JSONObject itemObject = new JSONObject(mDatas.get(position));
                Intent intent = new Intent(mView, ProDetailActivity.class);
                int productId = itemObject.getInt(Constance.id);
                intent.putExtra(Constance.is_xiangou,true);
                intent.putExtra(Constance.product, productId);
                mView.startActivity(intent);
            }
        });
        cv_countdownView.setOnCountdownEndListener(this);

    }

    @Override
    protected void handleMessage(int action, Object[] values) {

    }

    @Override
    protected void myHandleMessage(Message msg) {

    }

    /**
     * 抢购广告
     */
    private void sendTimeBuyBanner() {

        mNetWork.sendTimeBuyBanner(new INetworkCallBack() {
            @Override
            public void onSuccessListener(String requestCode, JSONObject ans) {
                JSONArray jsonArray = ans.getJSONArray(Constance.banners);
                for (int i = 0; i < jsonArray.length(); i++) {
                    String path = NetWorkConst.TIME_BUY_BANNER_HOST + jsonArray.getJSONObject(i).getString(Constance.ad_code);
                    paths.add(path);
                }
                getAd();
            }

            @Override
            public void onFailureListener(String requestCode, JSONObject ans) {

            }
        })
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


    private void sendGrouplist(int page) {
//        mView.setShowDialog(true);
//        mView.setShowDialog("正在获取中!");
//        mView.showLoading();
        mNetWork.sendGrouplist(page, "100", null, null, null, null, null, new INetworkCallBack() {
            @Override
            public void onSuccessListener(String requestCode, JSONObject ans) {
//                mView.hideLoading();
                mTimeBuyDatas = ans.getJSONArray(Constance.products);
                mStartTimeArry = new ArrayList<String>();
                mEndTimeArry = new ArrayList<String>();
                mTimeBuyMap = new HashMap<String, ArrayList<String>>();
                if (mTimeBuyDatas.length() == 0) {
                } else {
                    for (int i = 0; i < mTimeBuyDatas.length(); i++) {
                        JSONObject jsonObject = mTimeBuyDatas.getJSONObject(i).getJSONObject(Constance.group_buy);
                        String startTime = jsonObject.getString(Constance.start_time);
                        String endTime = jsonObject.getString(Constance.end_time);
                        if (!mStartTimeArry.contains(startTime)) {
                            mStartTimeArry.add(startTime);
                            mEndTimeArry.add(endTime);
                        }
                    }

                    Collections.sort(mStartTimeArry);
                    Collections.sort(mEndTimeArry);

                    for (int i = 0; i < mStartTimeArry.size(); i++) {
                        ArrayList<String> times = new ArrayList<String>();
                        for (int j = 0; j < mTimeBuyDatas.length(); j++) {
                            JSONObject jsonObject = mTimeBuyDatas.getJSONObject(j).getJSONObject(Constance.group_buy);
                            String startTime = jsonObject.getString(Constance.start_time);
                            if (startTime.equals(mStartTimeArry.get(i))) {
                                times.add(mTimeBuyDatas.getJSONObject(j).toString());
                            }
                        }
                        mTimeBuyMap.put(mStartTimeArry.get(i), times);
                    }
                }
                mTimeBuyAdapter.notifyDataSetChanged();
                if(mTimeBuyMap.size()==0)return;
                mDatas = mTimeBuyMap.get(mStartTimeArry.get(0));
                mTimeBuyProAdapter.notifyDataSetChanged();
                getEndTimeShow(0);

            }

            @Override
            public void onFailureListener(String requestCode, JSONObject ans) {
//                mView.hideLoading();
                if(AppUtils.isEmpty(ans)){
                    AppDialog.messageBox(UIUtils.getString(R.string.server_error));
                    return;
                }
//                AppDialog.messageBox(ans.getString(Constance.error_desc));
            }
        })
        ;
    }

    /**
     * 显示剩余时间
     *
     * @param poisition
     */
    private void getEndTimeShow(int poisition) {
        try{
            time_end_rl.setVisibility(View.VISIBLE);
            long leftTime = DateUtils.getTimeStamp(mEndTimeArry.get(poisition), "yyyy-MM-dd HH:mm")
                    - System.currentTimeMillis();
            refreshTime(leftTime);
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    public void refreshTime(long leftTime) {
        if (leftTime > 0) {
            cv_countdownView.start(leftTime);
        } else {
            time_end_rl.setVisibility(View.GONE);
            cv_countdownView.stop();
            cv_countdownView.allShowZero();
        }
    }

    @Override
    public void onEnd(CountdownView cv) {
        initViewData();
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
            imageView.setImageResource(R.drawable.bg_default);
            ImageLoader.getInstance().displayImage(data, imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }
    }

    private class TimeProAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if (null == mStartTimeArry)
                return 0;
            return mStartTimeArry.size();

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
                convertView = View.inflate(mView, R.layout.item_time, null);
                holder = new ViewHolder();
                holder.time_01_tv = (TextView) convertView.findViewById(R.id.time_01_tv);
                holder.time_02_tv = (TextView) convertView.findViewById(R.id.time_02_tv);
                holder.main_rl = (RelativeLayout) convertView.findViewById(R.id.main_rl);
                holder.state_tv = (TextView) convertView.findViewById(R.id.state_tv);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            String time = mStartTimeArry.get(position);
            holder.time_01_tv.setText(time.substring(0, 10));
            holder.time_02_tv.setText(time.substring(10));
            holder.time_01_tv.setTextColor(mView.getResources().getColor(R.color.txt_black));
            holder.time_02_tv.setTextColor(mView.getResources().getColor(R.color.txt_black));
            holder.state_tv.setTextColor(mView.getResources().getColor(R.color.fontColor6));
            holder.main_rl.setBackgroundColor(mView.getResources().getColor(R.color.item_time_bg));
            if (DateUtils.getTimeStamp(time, "yyyy-MM-dd HH:mm") - System.currentTimeMillis() > 0) {
                holder.state_tv.setText("即将开始");

            } else {
                holder.state_tv.setText("已开始");

            }
            if (mPoistion == position) {
                holder.time_01_tv.setTextColor(mView.getResources().getColor(R.color.white));
                holder.time_02_tv.setTextColor(mView.getResources().getColor(R.color.white));
                holder.state_tv.setTextColor(mView.getResources().getColor(R.color.white));
                holder.main_rl.setBackgroundColor(mView.getResources().getColor(R.color.green));
            }

            return convertView;
        }

        class ViewHolder {
            TextView time_01_tv, time_02_tv, state_tv;
            RelativeLayout main_rl;
        }
    }


    private class TimeBuyProAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if (null == mDatas)
                return 0;
            else {
                return mDatas.size();
            }

        }

        @Override
        public String getItem(int position) {
            if (null == mDatas)
                return null;
            return mDatas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(mView, R.layout.item_time_buy_product, null);
                holder = new ViewHolder();
                holder.product_iv = (ImageView) convertView.findViewById(R.id.product_iv);
                holder.name_tv = (TextView) convertView.findViewById(name_tv);
                holder.price_tv = (TextView) convertView.findViewById(price_tv);
                holder.price_old_tv = (TextView) convertView.findViewById(R.id.price_old_tv);
                holder.remaining_num_tv = (TextView) convertView.findViewById(remaining_num_tv);
                holder.go_buy_tv = (TextView) convertView.findViewById(R.id.go_buy_tv);
                holder.tv_end=convertView.findViewById(R.id.tv_end);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            JSONObject itemObject = new JSONObject(mDatas.get(position));

            try {
                String imagePath = itemObject.getJSONObject(Constance.app_img).getString(Constance.phone_img);
                ImageLoader.getInstance().displayImage(imagePath, holder.product_iv);

            } catch (Exception e) {
                e.printStackTrace();
            }


            JSONArray propertieArray = itemObject.getJSONArray(Constance.properties);
            if (!AppUtils.isEmpty(propertieArray)) {
                JSONArray attrsArray = propertieArray.getJSONObject(0).getJSONArray(Constance.attrs);
                int price = attrsArray.getJSONObject(0).getInt(Constance.attr_price);
                double oldPrice = Double.parseDouble(itemObject.getString(Constance.price)) + price;
                holder.price_old_tv.setText("￥" + (int)oldPrice);
                holder.price_old_tv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                holder.price_old_tv.setText("￥" + (int)Double.parseDouble(itemObject.getString(Constance.price)));
                holder.price_old_tv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            }

            String restrictAmount = itemObject.getJSONObject(Constance.group_buy).getJSONObject(Constance.ext_info).getString(Constance.restrict_amount);
            if("0".equals(restrictAmount)){
                holder.tv_end.setVisibility(View.VISIBLE);
                holder.go_buy_tv.setText("去看看");
            }else {
                holder.tv_end.setVisibility(View.GONE);
                holder.go_buy_tv.setText("立即抢购");
            }
            holder.remaining_num_tv.setText("剩余 " + restrictAmount + " 件");
            JSONArray priceArray = itemObject.getJSONObject(Constance.group_buy).getJSONObject(Constance.ext_info).getJSONArray(Constance.price_ladder);
            int price = priceArray.getJSONObject(0).getInt(Constance.price);
            holder.price_tv.setText("￥" + price);

            holder.name_tv.setText(itemObject.getString(Constance.name));
            return convertView;
        }

        class ViewHolder {
            ImageView product_iv;
            TextView name_tv, price_tv, price_old_tv, remaining_num_tv, go_buy_tv,tv_end;
        }
    }

}
