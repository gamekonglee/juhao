package bc.juhao.com.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import bc.juhao.com.R;
import bc.juhao.com.common.BaseFragment;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.controller.product.IntroduceGoodsController;
import bc.juhao.com.ui.activity.product.ProDetailActivity;
import bc.juhao.com.ui.view.PullUpToLoadMore;
import bocang.utils.AppUtils;

/**
 * @author: Jun
 * @date : 2017/2/14 17:57
 * @description :
 */
public class IntroduceGoodsFragment extends BaseFragment implements View.OnClickListener {
    private IntroduceGoodsController mController;
    public String productId;
    private RelativeLayout collect_rl, rl_2, rl_rl;
    private ConvenientBanner mConvenientBanner;
    private TextView mParamentTv;
    private TextView unPriceTv, proPriceTv;
    private ImageView top_iv;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fm_product_introduce, null);

    }

    @Override
    protected void initController() {
        mController = new IntroduceGoodsController(this);
    }

    @Override
    protected void initViewData() {


    }

    @Override
    protected void initView() {
        collect_rl = (RelativeLayout) getActivity().findViewById(R.id.collect_rl);
        rl_2 = (RelativeLayout) getActivity().findViewById(R.id.rl_2);
        collect_rl.setOnClickListener(this);
        rl_2.setOnClickListener(this);
        mConvenientBanner = (ConvenientBanner) getActivity().findViewById(R.id.convenientBanner);
        rl_rl = (RelativeLayout) getActivity().findViewById(R.id.rl_rl);
        mParamentTv = (TextView) getActivity().findViewById(R.id.type_tv);
        unPriceTv = (TextView) getActivity().findViewById(R.id.unPriceTv);
        proPriceTv = (TextView) getActivity().findViewById(R.id.proPriceTv);

        top_iv = (ImageView) getActivity().findViewById(R.id.top_iv);


        final PullUpToLoadMore ptlm = (PullUpToLoadMore) getActivity().findViewById(R.id.ptlm);
        ptlm.currPosition=0;
        ptlm.setmListener(new PullUpToLoadMore.ScrollListener() {
            @Override
            public void onScrollToBottom(int currPosition) {
                if (currPosition == 0) {
                   if(mListener!=null) mListener.onScrollToBottom(0);
                    top_iv.setVisibility(View.GONE);
                } else {
                    if(mListener!=null) mListener.onScrollToBottom(1);
                    top_iv.setVisibility(View.VISIBLE);
                }
            }
        });
        top_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ptlm.scrollToTop();
            }
        });


        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void initData() {
        productId = (String) getArguments().get(Constance.product);
//        isXianGou= (boolean) getArguments().get(Constance.is_xiangou);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.collect_rl:
                mController.sendCollectGoods();
                break;
            case R.id.rl_2:
                if (!isToken())
                    mController.selectParament();
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mController.send24Rexiao();
    }

    private ScrollListener mListener;

    public void setmListener(ScrollListener mListener) {
        this.mListener = mListener;
    }


    public interface ScrollListener {
        void onScrollToBottom(int currPosition);

    }

    //在主线程执行
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUserEvent(Integer action) {
        if (action == Constance.PROPERTY) {
            String property = ((ProDetailActivity) getActivity()).mPropertyValue;
            int price=((ProDetailActivity) getActivity()).mPrice;
            if (AppUtils.isEmpty(property))
                return;
            mParamentTv.setText("已选 " + (property));
//            unPriceTv.setText("￥"+(Double.parseDouble(mController.mOldPrice)+price));
//            proPriceTv.setText("￥"+(Double.parseDouble(mController.mCurrentPrice)+price));
        }
    }


}
