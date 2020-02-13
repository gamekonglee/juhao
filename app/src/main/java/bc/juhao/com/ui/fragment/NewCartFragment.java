package bc.juhao.com.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


import bc.juhao.com.R;
import bc.juhao.com.common.BaseFragment;
import bc.juhao.com.controller.buy.NewCartController;
import bc.juhao.com.ui.activity.newbuy.NewEditOrderActivity;
import hxp.view.ListViewForScrollView;

/**
 * author: cjt
 * date:  2019-12-11$
 * ClassName: NewCartFragment$
 * Description:
 */
public class NewCartFragment extends BaseFragment {

    private NewCartController mController;
    private ListViewForScrollView lv_cart;
    private TextView tv_edit, tv_settle;
    private LinearLayout ll_isedit, ll_notedit;
    private Boolean isEditing = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return  inflater.inflate(R.layout.fm_newcart, null);
    }

    @Override
    protected void initController() {
        mController = new NewCartController(this);
    }

    @Override
    protected void initViewData() {
        lv_cart.setAdapter(lv_adapter);
    }

    @Override
    protected void initView() {
        lv_cart = getActivity().findViewById(R.id.lv_cart);
        tv_edit = getActivity().findViewById(R.id.tv_edit);
        tv_settle = getActivity().findViewById(R.id.tv_settle);
        ll_isedit = getActivity().findViewById(R.id.ll_isedit);
        ll_notedit = getActivity().findViewById(R.id.ll_notedit);
        tv_edit.setOnClickListener(onClickListener);
        tv_settle.setOnClickListener(onClickListener);
    }

    @Override
    protected void initData() {

    }


    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.tv_edit:
                    isEditing = !isEditing;
                    if (isEditing){
                        ll_isedit.setVisibility(View.VISIBLE);
                        ll_notedit.setVisibility(View.GONE);
                        tv_edit.setText(getResources().getString(R.string.complete));
                    }else {
                        ll_isedit.setVisibility(View.GONE);
                        ll_notedit.setVisibility(View.VISIBLE);
                        tv_edit.setText(getResources().getString(R.string.edit));
                    }
                    break;
                case R.id.tv_settle:
                    startActivity(new Intent(getActivity(), NewEditOrderActivity.class));
                    break;
                case R.id.ll_num:

                    break;
            }
        }
    };

    private BaseAdapter lv_adapter = new BaseAdapter() {
        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            View v = View.inflate(getContext(), R.layout.item_new_cart, null);
            ListViewForScrollView lv_items = v.findViewById(R.id.lv_items);
            lv_items.setAdapter(new ItemsAdapter(i));
            return v;
        }

    };

    private class ItemsAdapter extends BaseAdapter{

        int i;
        ItemsAdapter(int i){
            this.i = i;
        };

        @Override
        public int getCount() {
            return i+1;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View v = View.inflate(getContext(), R.layout.item_new_cart_item, null);
            mController.resetNumBind(v.findViewById(R.id.ll_num), (TextView) v.findViewById(R.id.tv_num), 20, 7, 100);
            return v;
        }
    }
}
