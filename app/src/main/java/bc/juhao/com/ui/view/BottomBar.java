package bc.juhao.com.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import bc.juhao.com.R;


/**
 * @author Jun
 * @time 2016/11/2 15:54
 */
public class BottomBar extends LinearLayout implements View.OnClickListener {
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
    private IBottomBarItemClickListener mListener;

    private int mCurrenTabId;

    public BottomBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
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

        findViewById(R.id.frag_top_ll).setOnClickListener(this);
        findViewById(R.id.frag_product_ll).setOnClickListener(this);
        findViewById(R.id.frag_match_ll).setOnClickListener(this);
        findViewById(R.id.frag_cart_ll).setOnClickListener(this);
        findViewById(R.id.frag_mine_ll).setOnClickListener(this);

        findViewById(R.id.frag_top_ll).performClick();
    }

    @Override
    public void onClick(View v) {
        //	设置 如果电机的是当前的的按钮 再次点击无效
        if (mCurrenTabId != 0 && mCurrenTabId == v.getId()) {
            return;
        }
        //点击前先默认全部不被选中
        defaultTabStyle();

        mCurrenTabId = v.getId();
        switch (v.getId()) {
            case R.id.frag_top_ll:
                frag_top_tv.setSelected(true);
                frag_top_iv.setSelected(true);
                break;
            case R.id.frag_product_ll:
                frag_product_tv.setSelected(true);
                frag_product_iv.setSelected(true);
                break;
            case R.id.frag_match_ll:
                frag_match_tv.setSelected(true);
                frag_match_iv.setSelected(true);
                break;
            case R.id.frag_cart_ll:
                frag_cart_tv.setSelected(true);
                frag_cart_iv.setSelected(true);
                break;
            case R.id.frag_mine_ll:
                frag_mine_tv.setSelected(true);
                frag_mine_iv.setSelected(true);
                break;
        }

        if (mListener != null) {
            mListener.OnItemClickListener(v.getId());
        }

    }
    /**
     * 选择指定的item
     * @param currenTabId
     */
    public void selectItem(int currenTabId){
        //	设置 如果电机的是当前的的按钮 再次点击无效
        if (mCurrenTabId != 0 && mCurrenTabId ==currenTabId) {
            return;
        }
        //点击前先默认全部不被选中
        defaultTabStyle();

        mCurrenTabId = currenTabId;
        switch (currenTabId) {
            case R.id.frag_top_ll:
                frag_top_tv.setSelected(true);
                frag_top_iv.setSelected(true);
                break;
            case R.id.frag_product_ll:
                frag_product_tv.setSelected(true);
                frag_product_iv.setSelected(true);
                break;
            case R.id.frag_match_ll:
                frag_match_tv.setSelected(true);
                frag_match_iv.setSelected(true);
                break;
            case R.id.frag_cart_ll:
                frag_cart_tv.setSelected(true);
                frag_cart_iv.setSelected(true);
                break;
            case R.id.frag_mine_ll:
                frag_mine_tv.setSelected(true);
                frag_mine_iv.setSelected(true);
                break;
        }

        if (mListener != null) {
            mListener.OnItemClickListener(currenTabId);
        }
    }



    public void setOnClickListener(IBottomBarItemClickListener listener) {
        this.mListener = listener;
    }

    public interface IBottomBarItemClickListener {
        void OnItemClickListener(int resId);
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
}
