package bc.juhao.com.ui.fragment.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import bc.juhao.com.R;
import bc.juhao.com.common.BaseFragment;
import bc.juhao.com.controller.VideoController;

/**
 * Created by gamekonglee on 2018/4/16.
 */

public class VideoHomeFragment extends BaseFragment implements View.OnClickListener {

    private VideoController mController;
    private TextView tv_xuanchuan;
    private TextView tv_jiaoxue;
    public int currentType;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_home_video, null);

    }

    @Override
    protected void initController() {
        mController = new VideoController(this);

    }

    @Override
    protected void initViewData() {

    }

    @Override
    protected void initView() {
        tv_xuanchuan = getView().findViewById(R.id.tv_xuanchuan);
        tv_jiaoxue = getView().findViewById(R.id.tv_jiaoxue);
        tv_jiaoxue.setOnClickListener(this);
        tv_xuanchuan.setOnClickListener(this);
        currentType = 0;
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_xuanchuan:
                swichType(0);
                break;
            case R.id.tv_jiaoxue:
                swichType(1);
                break;
            default:
                break;

        }
    }

    private void swichType(int type) {
        if (currentType == type) {
            return;
        }
        currentType = type;
        if (currentType == 0) {
            tv_xuanchuan.setTextColor(getActivity().getResources().getColor(R.color.green));
            tv_jiaoxue.setTextColor(getActivity().getResources().getColor(R.color.txt_black));
            tv_jiaoxue.setBackground(null);
            tv_xuanchuan.setBackground(getActivity().getResources().getDrawable(R.drawable.bg_bottom_line));
            mController.articlesBeans = new ArrayList<>();
            mController.sendVideoA();
        } else {
            tv_xuanchuan.setTextColor(getActivity().getResources().getColor(R.color.txt_black));
            tv_xuanchuan.setBackground(null);
            tv_jiaoxue.setBackground(getActivity().getResources().getDrawable(R.drawable.bg_bottom_line));
            tv_jiaoxue.setTextColor(getActivity().getResources().getColor(R.color.green));
            mController.articlesBeans = new ArrayList<>();
            mController.sendVideoB();
        }

    }
}
