package bc.juhao.com.ui.fragment.home;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import bc.juhao.com.R;
import bc.juhao.com.common.BaseFragment;
import bc.juhao.com.controller.NewsHomeController;
import bc.juhao.com.ui.view.EndOfGridView;
import bc.juhao.com.ui.view.PMSwipeRefreshLayout;

/**
 * Created by gamekonglee on 2018/4/16.
 */

public class NewsHomeFragment extends BaseFragment {

    private NewsHomeController mController;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_home_news,null);

    }

    @Override
    protected void initController() {
        mController = new NewsHomeController(this);
    }

    @Override
    protected void initViewData() {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }
}
