package bc.juhao.com.ui.activity.product;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import bc.juhao.com.R;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.controller.product.SelectGoodsController;
import bc.juhao.com.ui.activity.IssueApplication;
import bocang.utils.AppUtils;
import bocang.view.BaseActivity;

/**
 * @author: Jun
 * @date : 2017/2/16 17:30
 * @description :选择产品
 */
public class SelectGoodsActivity extends BaseActivity {
    private SelectGoodsController mController;
    private TextView topRightBtn;
    private TextView popularityTv, newTv, saleTv;
    private LinearLayout stylell;
    public String mCategoriesId;
    public boolean isSelectGoods = false;
    public String mFilterAttr = "";
    public TextView select_num_tv;
    private Intent mIntent;
    private RelativeLayout select_rl;
    public int mSort=-1;
    public boolean mIsYiJI=false;

    @Override
    protected void InitDataView() {
        if (isSelectGoods) {
            select_rl.setVisibility(View.VISIBLE);
        }
        select_num_tv.setText(IssueApplication.mSelectProducts.length()+"");
    }

    @Override
    protected void initController() {
        mController = new SelectGoodsController(this);
        if(getIntent().hasExtra("news")){
            newTv.performClick();
        }
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_select_product);
        topRightBtn = getViewAndClick(R.id.topRightBtn);
        popularityTv = getViewAndClick(R.id.popularityTv);
        newTv = getViewAndClick(R.id.newTv);
        saleTv = getViewAndClick(R.id.saleTv);
        stylell = getViewAndClick(R.id.stylell);
        select_num_tv = (TextView)findViewById(R.id.select_num_tv);
        select_rl = getViewAndClick(R.id.select_rl);


    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        mCategoriesId = intent.getStringExtra(Constance.categories);
        isSelectGoods = intent.getBooleanExtra(Constance.ISSELECTGOODS, false);
        mFilterAttr = intent.getStringExtra(Constance.filter_attr);
        mIsYiJI=intent.getBooleanExtra(Constance.ISYIJI,false);
        mSort = intent.getIntExtra(Constance.sort,0);

    }

    private boolean isPriceSort = false;

    @Override
    protected void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.topRightBtn:
                mController.openClassify();
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
                setResult(Constance.FROMDIY, mIntent);//告诉原来的Activity 将数据传递给它
                finish();//一定要调用该方法 关闭新的AC 此时 老是AC才能获取到Itent里面的值
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
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==103){
            if(AppUtils.isEmpty(data))return;
            String value = data.getStringExtra(Constance.filter_attr);
            if (AppUtils.isEmpty(value))
                return;
            mFilterAttr=value;
            mController.onRefresh();
        }
    }
}
