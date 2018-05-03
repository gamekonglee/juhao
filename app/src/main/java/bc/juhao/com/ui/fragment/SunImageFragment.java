package bc.juhao.com.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import bc.juhao.com.R;
import bc.juhao.com.common.BaseFragment;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.controller.product.SunImageController;
import bc.juhao.com.ui.activity.IssueApplication;
import bc.juhao.com.ui.activity.product.PostedImageActivity;
import bc.juhao.com.ui.activity.product.ProDetailActivity;
import bocang.utils.AppUtils;

/**
 * @author Jun
 * @time 2017/12/10  12:51
 * @desc ${TODD}
 */

public class SunImageFragment extends BaseFragment implements View.OnClickListener {
    private SunImageController mController;
    private ImageView go_sun_iv;
    public String productId;


    @Override
    protected void initController() {
        mController = new SunImageController(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fm_product_sun_image, null);

    }

    @Override
    protected void initViewData() {
        int id = ((ProDetailActivity) this.getActivity()).mOrderId;
        try{
            int level = IssueApplication.mUserObject.getInt(Constance.level);
            if (level == 0) {
                go_sun_iv.setVisibility(View.VISIBLE);
                ((ProDetailActivity) this.getActivity()).mOrderid = "1";
            } else {
                if (AppUtils.isEmpty(((ProDetailActivity) this.getActivity()).mOrderid)) {
                    go_sun_iv.setVisibility(View.GONE);
                } else {
                    go_sun_iv.setVisibility(View.VISIBLE);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            go_sun_iv.setVisibility(View.GONE);
        }

    }

    @Override
    protected void initView() {
        go_sun_iv = (ImageView) getActivity().findViewById(R.id.go_sun_iv);
        go_sun_iv.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        productId = (String) getArguments().get(Constance.product);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.go_sun_iv://去晒家
                Intent intent = new Intent(this.getActivity(), PostedImageActivity.class);
                intent.putExtra(Constance.id, ((ProDetailActivity) this.getActivity()).mProductId);
                intent.putExtra(Constance.order_id, ((ProDetailActivity) this.getActivity()).mOrderid);

                this.getActivity().startActivity(intent);
                break;
        }
    }
}
