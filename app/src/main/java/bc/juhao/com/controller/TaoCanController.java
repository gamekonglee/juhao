package bc.juhao.com.controller;

import android.graphics.Paint;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import bc.juhao.com.R;
import bc.juhao.com.adapter.BaseAdapterHelper;
import bc.juhao.com.adapter.QuickAdapter;
import bc.juhao.com.bean.TaoCanBean;
import bc.juhao.com.ui.activity.TaoCanListActivity;

/**
 * Created by gamekonglee on 2018/5/17.
 */

public class TaoCanController extends BaseController {

    private final TaoCanListActivity mView;
    private ListView lv_taocan;
    private QuickAdapter<TaoCanBean> adapter;
    private List<TaoCanBean> taoCanBeans;

    @Override
    protected void handleMessage(int action, Object[] values) {

    }

    @Override
    protected void myHandleMessage(Message msg) {

    }
    public TaoCanController(TaoCanListActivity activity){
        mView = activity;
        initView();
    }

    private void initView() {
        lv_taocan = mView.findViewById(R.id.lv_taocan);
        adapter = new QuickAdapter<TaoCanBean>(mView, R.layout.item_taocan) {
            @Override
            protected void convert(BaseAdapterHelper helper, TaoCanBean item) {
                LinearLayout ll_caotan=helper.getView(R.id.ll_caotan);
                ll_caotan.removeAllViews();

                View view=View.inflate(mView, R.layout.view_taocan,null);
                ImageView iv_img=view.findViewById(R.id.iv_img);
                TextView tv_price=view.findViewById(R.id.tv_price);
                TextView tv_old_price=view.findViewById(R.id.tv_old_price);
                tv_old_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

                ll_caotan.addView(view);
            }
        };
        lv_taocan.setAdapter(adapter);
        taoCanBeans = new ArrayList<>();

    }
}
