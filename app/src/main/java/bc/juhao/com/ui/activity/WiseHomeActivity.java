package bc.juhao.com.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import bc.juhao.com.R;
import bc.juhao.com.common.BaseActivity;
import bc.juhao.com.ui.fragment.intelligence.ItExperiFragment;
import bc.juhao.com.ui.fragment.intelligence.ItHomeFragment;
import bc.juhao.com.ui.fragment.intelligence.ItMineFragment;
import bc.juhao.com.ui.fragment.intelligence.ItSceenFragment;

/**
 * Created by bocang on 18-2-8.
 */

public class WiseHomeActivity extends BaseActivity implements View.OnClickListener {


    private TextView frag_top_tv;
    private TextView frag_product_tv;
    private TextView frag_match_tv;
    private TextView frag_cart_tv;
    private TextView frag_mine_tv;
    private ImageView frag_top_iv;
    private ImageView frag_product_iv;
    private ImageView frag_match_iv;
    private ImageView frag_cart_iv;
    private ImageView frag_mine_iv;
    private ItHomeFragment mHomeFragment;
    private Fragment currentFragmen;
    private ItSceenFragment mProductFragment;
    private ItExperiFragment mCartFragment;
    private ItMineFragment mMineFragment;

    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {

    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_wise_home);
        frag_top_tv = (TextView) findViewById(R.id.frag_top_tv);
        frag_product_tv = (TextView) findViewById(R.id.frag_product_tv);
        frag_match_tv = (TextView) findViewById(R.id.frag_match_tv);
        frag_cart_tv = (TextView) findViewById(R.id.frag_cart_tv);
        frag_mine_tv = (TextView) findViewById(R.id.frag_mine_tv);
        frag_top_iv = (ImageView) findViewById(R.id.frag_top_iv);
        frag_product_iv = (ImageView) findViewById(R.id.frag_product_iv);
        frag_match_iv = (ImageView) findViewById(R.id.frag_match_iv);
        frag_cart_iv = (ImageView) findViewById(R.id.frag_cart_iv);
        frag_mine_iv = (ImageView) findViewById(R.id.frag_mine_iv);

        findViewById(R.id.frag_top_tv).setOnClickListener(this);
        findViewById(R.id.frag_product_tv).setOnClickListener(this);
        findViewById(R.id.frag_match_tv).setOnClickListener(this);
        findViewById(R.id.frag_cart_tv).setOnClickListener(this);
        findViewById(R.id.frag_mine_tv).setOnClickListener(this);
        findViewById(R.id.frag_top_ll).setOnClickListener(this);
        findViewById(R.id.frag_product_ll).setOnClickListener(this);
        findViewById(R.id.frag_match_ll).setOnClickListener(this);
        findViewById(R.id.frag_cart_ll).setOnClickListener(this);
        findViewById(R.id.frag_mine_ll).setOnClickListener(this);
        initTab();
        findViewById(R.id.frag_top_ll).performClick();

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fullScreen(WiseHomeActivity.this);
    }
    /**
     * 初始化底部标签
     */
    private void initTab() {
        if (mHomeFragment == null) {
            mHomeFragment = new ItHomeFragment();
        }
        if (!mHomeFragment.isAdded()) {
            // 提交事务
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.top_bar, mHomeFragment).commit();

            // 记录当前Fragment
            currentFragmen = mHomeFragment;
        }
    }
    @Override
    protected void initData() {

    }
    private int mCurrenTabId;

    /**
     * 选择指定的item
     *
     * @param currenTabId
     */
    public void selectItem(int currenTabId) {
        //	设置 如果电机的是当前的的按钮 再次点击无效
        if (mCurrenTabId != 0 && mCurrenTabId == currenTabId) {
            return;
        }
        //点击前先默认全部不被选中
        defaultTabStyle();

        mCurrenTabId = currenTabId;
        switch (currenTabId) {
            case R.id.frag_top_ll:
                frag_top_tv.setSelected(true);
                frag_top_iv.setSelected(true);
                clickTab1Layout();
                break;
            case R.id.frag_product_ll:
                frag_product_tv.setSelected(true);
                frag_product_iv.setSelected(true);
                clickTab2Layout();
                break;
            case R.id.frag_match_ll:
//                frag_match_tv.setSelected(true);
//                frag_match_iv.setSelected(true);
                clickTab3Layout();
                break;
            case R.id.frag_cart_ll:
                frag_cart_tv.setSelected(true);
                frag_cart_iv.setSelected(true);
                clickTab4Layout();
                break;
            case R.id.frag_mine_ll:
                frag_mine_tv.setSelected(true);
                frag_mine_iv.setSelected(true);
                clickTab5Layout();
                break;
            case R.id.frag_top_tv:
                frag_top_tv.setSelected(true);
                frag_top_iv.setSelected(true);
                clickTab1Layout();
                break;
            case R.id.frag_product_tv:
                frag_product_tv.setSelected(true);
                frag_product_iv.setSelected(true);
                clickTab2Layout();
                break;
            case R.id.frag_match_tv:
                frag_match_tv.setSelected(true);
                frag_match_iv.setSelected(true);
                clickTab3Layout();
                break;
            case R.id.frag_cart_tv:
                frag_cart_tv.setSelected(true);
                frag_cart_iv.setSelected(true);
                clickTab4Layout();
                break;
            case R.id.frag_mine_tv:
                frag_mine_tv.setSelected(true);
                frag_mine_iv.setSelected(true);
                clickTab5Layout();
                break;
        }
    }

    /**
     * 点击第1个tab
     */
    public void clickTab1Layout() {
        if (mHomeFragment == null) {
            mHomeFragment = new ItHomeFragment();
        }
        addOrShowFragment(getSupportFragmentManager().beginTransaction(), mHomeFragment);
    }
    /**
     * 点击第2个tab
     */
    public void clickTab2Layout() {
        if (mProductFragment == null) {
            mProductFragment = new ItSceenFragment();
        }
        addOrShowFragment(getSupportFragmentManager().beginTransaction(), mProductFragment);

    }

    /**
     * 点击第3个tab
     */
    public void clickTab3Layout() {
//        if (mMatchFragment == null) {
//            mMatchFragment = new ProgrammeFragment();
//        }
//        addOrShowFragment(getSupportFragmentManager().beginTransaction(), mMatchFragment);
        startActivity(new Intent(this,ItDevicesActivity.class));
    }

    /**
     * 点击第4个tab
     */
    private void clickTab4Layout() {
        if (mCartFragment == null) {
            mCartFragment = new ItExperiFragment();
        }
        addOrShowFragment(getSupportFragmentManager().beginTransaction(), mCartFragment);

    }

    /**
     * 点击第5个tab
     */
    public void clickTab5Layout() {
        if (mMineFragment == null) {
            mMineFragment = new ItMineFragment();
        }
        addOrShowFragment(getSupportFragmentManager().beginTransaction(), mMineFragment);

    }

    /**
     * 默认全部不被选中
     */
    private void defaultTabStyle() {
        frag_top_tv.setSelected(false);
        frag_top_iv.setSelected(false);
        frag_product_tv.setSelected(false);
        frag_product_iv.setSelected(false);
        frag_match_tv.setSelected(false);
        frag_match_iv.setSelected(false);
        frag_cart_tv.setSelected(false);
        frag_cart_iv.setSelected(false);
        frag_mine_tv.setSelected(false);
        frag_mine_iv.setSelected(false);
    }

    @Override
    public void onClick(View v) {
        //	设置 如果点击的是当前的的按钮 再次点击无效
        if (mCurrenTabId != 0 && mCurrenTabId == v.getId()) {
            return;
        }

        selectItem(v.getId());
    }

    /**
     * 添加或者显示碎片
     *
     * @param transaction
     * @param fragment
     */
    private void addOrShowFragment(FragmentTransaction transaction,
                                   Fragment fragment) {
        if (currentFragmen == fragment)
            return;

        if (!fragment.isAdded()) { // 如果当前fragment未被添加，则添加到Fragment管理器中
            transaction.hide(currentFragmen)
                    .add(R.id.top_bar, fragment).commit();
        } else {
            transaction.hide(currentFragmen).show(fragment).commit();
        }

        currentFragmen = fragment;
    }
}
