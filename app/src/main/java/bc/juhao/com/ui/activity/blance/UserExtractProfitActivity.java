package bc.juhao.com.ui.activity.blance;

import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import astuetz.MyPagerSlidingTabStrip;
import bc.juhao.com.R;
import bc.juhao.com.adapter.BaseAdapterHelper;
import bc.juhao.com.adapter.QuickAdapter;
import bc.juhao.com.bean.ExtractBean;
import bc.juhao.com.common.BaseActivity;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.cons.ProfitBean;
import bc.juhao.com.controller.UserExtractProfitController;
import bc.juhao.com.ui.activity.IssueApplication;
import bc.juhao.com.ui.view.EndOfListView;
import bc.juhao.com.ui.view.PMSwipeRefreshLayout;
import bc.juhao.com.utils.DateUtils;
import bc.juhao.com.utils.UIUtils;
import bocang.utils.LogUtils;
import bocang.utils.MyToast;

public class UserExtractProfitActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, EndOfListView.OnEndOfListListener {

    private MyPagerSlidingTabStrip tab;
    private ViewPager viewPager;
    String[] titles={"收益明细","提现明细"};
    private UserExtractProfitController mController;
    public int currnetPage;
    public QuickAdapter<ProfitBean> profitAdapter;
    public QuickAdapter<ExtractBean> extractAdapter;
    public PMSwipeRefreshLayout pullToRefresh;
    private EndOfListView listView;
    public boolean [] isBottom={false,false};
    public PMSwipeRefreshLayout[] pmSwipeRefreshLayouts={null,null};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {
        mController = new UserExtractProfitController(this);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_user_extract_profit);
        setColor(this, Color.WHITE);
        tab = findViewById(R.id.tab);
        tab.setIndicatorColor(getResources().getColor(R.color.green));
        tab.setIndicatorHeight(UIUtils.dip2PX(1));
        tab.setDividerColor(Color.TRANSPARENT);
        tab.setTextSize(UIUtils.dip2PX(15));
        tab.defaultSize=UIUtils.dip2PX(15);
        tab.selectSize=UIUtils.dip2PX(15);
        tab.linePadding=0;
        tab.setTabPaddingLeftRight(0);
        tab.setShouldExpand(true);
        tab.defaultColor=getResources().getColor(R.color.txt_black);
        tab.selectColor=getResources().getColor(R.color.green);
        viewPager = findViewById(R.id.viewPager);
        currnetPage = getIntent().getIntExtra("current",0);

        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view==object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View view=View.inflate(UserExtractProfitActivity.this,R.layout.page_profit_record,null);
                listView = view.findViewById(R.id.lv_profit);
                listView.setOnEndOfListListener(UserExtractProfitActivity.this);
                pullToRefresh = view.findViewById(R.id.pullToRefresh);
                pullToRefresh.setColorSchemeColors(android.R.color.holo_blue_bright,android.R.color.holo_green_light,
                        android.R.color.holo_orange_light,android.R.color.holo_red_light);
//                pullToRefresh.setRefreshing(false);

                pullToRefresh.setOnRefreshListener(UserExtractProfitActivity.this);
                if(profitAdapter==null)profitAdapter = new QuickAdapter<ProfitBean>(UserExtractProfitActivity.this, R.layout.item_profit) {
                    @Override
                    protected void convert(BaseAdapterHelper helper, ProfitBean item) {
                        String userId= IssueApplication.mUserObject.getString(Constance.id);
                        if(userId.equals(""+item.getUser_id())){
                            helper.setText(R.id.tv_level,"一级收益");
                            helper.setText(R.id.tv_profit,"+"+item.getMoney());
                            helper.setText(R.id.tv_reason,item.getCustomer()+getLevel(item.getLevel())+"购买了产品");
                        }else {
                            helper.setText(R.id.tv_level,"下级收益");
                            helper.setText(R.id.tv_profit,""+item.getMoney());
                            helper.setText(R.id.tv_reason,item.getCustomer() +getLevel(item.getCustomer_level())+ "购买了产品 您的"+item.getUser_name()+getLevel(item.getLevel())+"获得了收益");
                        }
                        helper.setText(R.id.tv_time, DateUtils.getStrTime(item.getCreated_at()+""));
                    }
                };
                if(extractAdapter==null)extractAdapter = new QuickAdapter<ExtractBean>(UserExtractProfitActivity.this, R.layout.item_extract) {
                    @Override
                    protected void convert(BaseAdapterHelper helper, ExtractBean item) {
                        if(item.getIs_paid()==1){
                            helper.setText(R.id.tv_state,"提现成功");
                        }else if(item.getIs_paid()==0){
                            helper.setText(R.id.tv_state,"提现中");
                        }else{
                            helper.setText(R.id.tv_state,"提现失败");
                        }
                        helper.setText(R.id.tv_time,DateUtils.getStrTime(item.getAdd_time()+""));
                        helper.setText(R.id.tv_money,item.getAmount()+"");

                    }
                };
                pmSwipeRefreshLayouts[position]=pullToRefresh;
                if(position==0){
                    listView.setAdapter(profitAdapter);
                    if(mController.profitBeans!=null&&mController.profitBeans.size()>0){
                    profitAdapter.replaceAll(mController.profitBeans);
                    }else {
//                    mController.sendProfitRecordList();
                    }
                }else {
                    listView.setAdapter(extractAdapter);
                    if(mController.extractBeans!=null&&mController.extractBeans.size()>0){
                     extractAdapter.replaceAll(mController.extractBeans);
                    }else {
//                    mController.sendExtract();
                    }
                }
                container.addView(view);
                return view;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                super.destroyItem(container, position, object);
                container.removeView((View) object);
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return titles[position];
            }
        });
        tab.setViewPager(viewPager);
        tab.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                currnetPage=position;
            }

            @Override
            public void onPageSelected(int position) {
                    currnetPage=position;
                    if(pmSwipeRefreshLayouts[position]==null)
                        return;
                    pmSwipeRefreshLayouts[position].setRefreshing(true);
                    if(position==1){
                        if(mController.extractBeans==null||mController.extractBeans.size()==0){
                        mController.page[1]=1;
                        mController.sendExtract();
                        }else {
                           mController.dismissRefesh();
                        }
                    }else {
                        if(mController.profitBeans==null||mController.profitBeans.size()==0){

                        mController.page[0]=1;
                        mController.sendProfitRecordList();
                        }else {
                            mController.dismissRefesh();
                        }
                    }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        if(currnetPage!=0){
            viewPager.setCurrentItem(currnetPage);
        }

    }

    @Override
    protected void initData() {

    }

    @Override
    public void onRefresh() {
        mController.onRefresh();
    }
    private String getLevel(int level) {
        String levelValue = "";
        switch (level) {
            case 0:
                levelValue = "(一级)";
                break;
            case 1:
                levelValue = "(二级)";
                break;
            case 2:
                levelValue = "(三级)";
                break;
            case 3:
                levelValue = "(消费者)";
                break;
            case 4:
                levelValue = "(消费者)";
                break;
        }
        return levelValue;
    }

    @Override
    public void onEndOfList(Object lastItem) {
        if(listView==null||listView.getAdapter()==null){
            return;
        }
        if(isBottom[currnetPage]){
            MyToast.show(this,"到底了");
            return;
        }
        mController.page[currnetPage]++;
        LogUtils.logE("EndOfList","current:"+currnetPage+",page:"+mController.page[currnetPage]);
        if(currnetPage==0){
//            if(mController.page[currnetPage]>1&&mController.profitBeans==null||mController.profitBeans.size()==0){
//                return;
//            }
            mController.sendProfitRecordList();
        }else {
            mController.sendExtract();
        }

    }
}
