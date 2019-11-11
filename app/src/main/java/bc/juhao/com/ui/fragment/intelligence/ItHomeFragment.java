package bc.juhao.com.ui.fragment.intelligence;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import bc.juhao.com.R;
import bc.juhao.com.common.BaseFragment;
import bc.juhao.com.ui.activity.ItDevicesActivity;

/**
 * Created by bocang on 18-2-8.
 */

public class ItHomeFragment extends BaseFragment implements View.OnClickListener{

    private Button btn_add_device;

    @Override
    protected void initController() {

    }

    @Override
    protected void initViewData() {

    }

    @Override
    protected void initView() {
        btn_add_device = getView().findViewById(R.id.btn_add_device);
        btn_add_device.setOnClickListener(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_it_home,null);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View view) {
        startActivity(new Intent(getActivity(), ItDevicesActivity.class));
    }
}
