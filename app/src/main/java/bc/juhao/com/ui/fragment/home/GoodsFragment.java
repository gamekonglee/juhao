package bc.juhao.com.ui.fragment.home;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aliyun.iot.ilop.demo.DemoApplication;

import bc.juhao.com.R;
import bc.juhao.com.common.BaseFragment;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.controller.product.SelectGoodsController;
import bc.juhao.com.controller.product.SelectGoodsFragController;
import bocang.utils.AppUtils;
import bocang.view.BaseActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aliyun.iot.ilop.demo.DemoApplication;

import bc.juhao.com.R;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.controller.product.SelectGoodsController;
import bc.juhao.com.ui.activity.IssueApplication;
import bocang.utils.AppUtils;
import bocang.view.BaseActivity;


public class GoodsFragment extends BaseFragment implements View.OnClickListener {
    /**
     * @author: Jun
     * @date : 1217/2/16 17:30
     * @description :选择产品
     */

    public SelectGoodsFragController mController;
    private Intent mIntent;

    private TextView topRightBtn;
    public TextView popularityTv, newTv, saleTv;
    private LinearLayout stylell;
    public TextView select_num_tv;
    private RelativeLayout select_rl;
//    private LinearLayout ll_grid_or_list;
    private TextView tv_ensure;
    private TextView tv_reset;

    public String mCategoriesId="";
    public boolean isSelectGoods = false;
    public String mFilterAttr = "";
    public int mSort= -1;
    public boolean mIsYiJI=false;
    public String keyword="";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_home_goods,null);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        topRightBtn = getView().findViewById(R.id.topRightBtn);

        popularityTv = getView().findViewById(R.id.popularityTv);
        newTv = getView().findViewById(R.id.newTv);
        saleTv = getView().findViewById(R.id.saleTv);
        stylell = getView().findViewById(R.id.stylell);
        select_num_tv = (TextView)getView().findViewById(R.id.select_num_tv);
        select_rl = getView().findViewById(R.id.select_rl);
//        ll_grid_or_list = getView().findViewById(R.id.ll_grid_or_list);
        tv_ensure = getView().findViewById(R.id.tv_ensure);
        tv_reset = getView().findViewById(R.id.tv_reset);

        topRightBtn.setOnClickListener(this);
        popularityTv.setOnClickListener(this);
        newTv.setOnClickListener(this);
        saleTv.setOnClickListener(this);
        stylell.setOnClickListener(this);
        select_num_tv.setOnClickListener(this);
        select_rl.setOnClickListener(this);
//        ll_grid_or_list.setOnClickListener(this);
        tv_ensure.setOnClickListener(this);
        tv_reset.setOnClickListener(this);

    }

    @Override
        protected void initController() {
            mController = new SelectGoodsFragController(this);
        topRightBtn.performClick();
        }

    @Override
    protected void initViewData() {

    }

        @Override
        protected void initView() {
        }

        @Override
        protected void initData() {
//            Intent intent = getIntent();
//            mCategoriesId = intent.getStringExtra(Constance.categories);
//            isSelectGoods = intent.getBooleanExtra(Constance.ISSELECTGOODS, false);
//            mFilterAttr = intent.getStringExtra(Constance.filter_attr);
//            mIsYiJI=intent.getBooleanExtra(Constance.ISYIJI,false);
//            mSort = intent.getIntExtra(Constance.sort,0);

        }

        private boolean isPriceSort = false;
//
//        @Override
//        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//            super.onActivityResult(requestCode, resultCode, data);
//            if(requestCode==103){
//                if(AppUtils.isEmpty(data))return;
//                String value = data.getStringExtra(Constance.filter_attr);
//                if (AppUtils.isEmpty(value))
//                    return;
//                mFilterAttr=value;
//                mController.onRefresh();
//            }else if(requestCode==120&&resultCode==120){
//                keyword = data.getStringExtra("key");
//                mCategoriesId="";
//                mController.et_search.setText(keyword);
//                if (mIsYiJI) {
//                    mController.selectYijiProduct(1, "12", null, null, null);
//                } else {
//                    mController.selectProduct(1, "12", null, null, null);
//                }
//            }
//        }
    public void topRightClick(){
        topRightBtn.performClick();
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.topRightBtn:
                mController.openDrawerLayout();
//                mController.openClassify();
                break;
            case R.id.newTv:
                mController.selectSortType(R.id.newTv);
                break;
            case R.id.saleTv:
                mController.selectSortType(R.id.saleTv);
                break;
            case R.id.popularityTv:
                mController.selectSortType(R.id.popularityTv);
                break;
            case R.id.select_rl:
                mIntent = new Intent();
//                setResult(Constance.FROMDIY, mIntent);//告诉原来的Activity 将数据传递给它
//                finish();//一定要调用该方法 关闭新的AC 此时 老是AC才能获取到Itent里面的值
                break;
            case R.id.stylell:
                if (isPriceSort) {
                    isPriceSort = false;
                    mController.selectSortType(2);
                } else {
                    isPriceSort = true;
                    mController.selectSortType(R.id.stylell);
                }
                break;
            case R.id.ll_grid_or_list:
                mController.changeToGridOrList();
                break;
            case R.id.tv_ensure:
                mController.ensureFilter(1);
                break;
            case R.id.tv_reset:
                mController.resetFilter();
                break;
        }
    }
}
