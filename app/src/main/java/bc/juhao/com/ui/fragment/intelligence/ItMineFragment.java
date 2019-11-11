package bc.juhao.com.ui.fragment.intelligence;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import bc.juhao.com.R;
import bc.juhao.com.common.BaseFragment;
import bc.juhao.com.ui.activity.ErrorTipsActivity;

/**
 * Created by bocang on 18-2-8.
 */

public class ItMineFragment extends BaseFragment {
    @Override
    protected void initController() {

    }

    @Override
    protected void initViewData() {

    }

    @Override
    protected void initView() {
        LinearLayout ll_it_mine=getView().findViewById(R.id.ll_it_mine);
        ll_it_mine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ErrorTipsActivity.class));
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         return inflater.inflate(R.layout.frag_it_mine,null);
    }
}
