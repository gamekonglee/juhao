package bc.juhao.com.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yjn.swipelistview.swipelistviewlibrary.widget.SwipeMenuListView;

import bc.juhao.com.R;
import bc.juhao.com.common.BaseFragment;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.controller.buy.CartController;

/**
 * @author Jun
 * @time 2017/1/5  12:00
 * @desc 购物车
 */
public class CartFragment extends BaseFragment implements View.OnClickListener {
    private CartController mController;
    private TextView edit_tv, settle_tv,export_tv;
    private SwipeMenuListView listView;
    private CheckBox checkAll;
    private Boolean mIsBack=false;
    private LinearLayout back_ll;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        ((MainActivity)getActivity()).setColor(getActivity(), Color.WHITE);
        return inflater.inflate(R.layout.fm_cart_new, null);
    }

    @Override
    protected void initController() {
        mController = new CartController(this);
    }

    @Override
    protected void initViewData() {
        isToken();
    }

    @Override
    protected void initView() {
        edit_tv = (TextView)  getActivity().findViewById(R.id.edit_tv);
        settle_tv = (TextView) getActivity().findViewById(R.id.settle_tv);
        checkAll = (CheckBox) getActivity().findViewById(R.id.checkAll);
        settle_tv.setOnClickListener(this);
        edit_tv.setOnClickListener(this);
        checkAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mController.setCkeckAll(isChecked);
            }
        });
        back_ll = (LinearLayout) getActivity().findViewById(R.id.back_ll);
        if(mIsBack==false){
            back_ll.setVisibility(View.GONE);
        }else {
            back_ll.setVisibility(View.VISIBLE);
        }
//        //取得设置好的drawable对象
//        Drawable drawable = this.getResources().getDrawable(R.drawable.selector_checkbox03);
//
//        //设置drawable对象的大小
//        drawable.setBounds(0, 0, 80, 80);
//
//        //设置CheckBox对象的位置，对应为左、上、右、下
//        checkAll.setCompoundDrawables(drawable,null,null,null);
        export_tv = (TextView) getActivity().findViewById(R.id.export_tv);
        export_tv.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        if(getArguments()==null) return;
        if(getArguments().get(Constance.product)==null) return;
        mIsBack= (Boolean) getArguments().get(Constance.product);
    }

    @Override
    public void onStart() {
        super.onStart();
        mController.sendShoppingCart();
        checkAll.setChecked(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit_tv:
                mController.setEdit();
                break;
            case R.id.settle_tv:
                mController.sendSettle();
                break;
            case R.id.export_tv:
                mController.exportData();
                break;

        }
    }
}
