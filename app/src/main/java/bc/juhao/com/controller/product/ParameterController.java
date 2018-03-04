package bc.juhao.com.controller.product;

import android.os.Message;
import android.widget.ListView;

import bc.juhao.com.R;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.controller.BaseController;
import bc.juhao.com.ui.activity.product.ProDetailActivity;
import bc.juhao.com.ui.adapter.ParamentAdapter;
import bc.juhao.com.ui.fragment.ParameterFragment;
import bocang.utils.AppUtils;

/**
 * @author: Jun
 * @date : 2017/2/14 11:05
 * @description :
 */
public class ParameterController extends BaseController {
    private ParameterFragment mView;
    private ParamentAdapter mAdapter;
    private ListView parameter_lv;
    private com.alibaba.fastjson.JSONObject mProductObject;


    public ParameterController(ParameterFragment v) {
        mView = v;
        initView();
        initViewData();
    }

    private void initViewData() {
        sendProductDetail();
    }


    private void initView() {
        parameter_lv = (ListView) mView.getActivity().findViewById(R.id.parameter_lv);
        parameter_lv.setDivider(null);//去除listview的下划线
    }

    @Override
    protected void handleMessage(int action, Object[] values) {

    }

    @Override
    protected void myHandleMessage(Message msg) {

    }

    /**
     * 产品详情
     */
    public void sendProductDetail() {
        mProductObject= ((ProDetailActivity)mView.getActivity()).mProductObject;
       if(AppUtils.isEmpty(mProductObject))
        return;
        com.alibaba.fastjson.JSONArray attachArray = mProductObject.getJSONArray(Constance.attachments);
        mAdapter = new ParamentAdapter(attachArray, mView.getActivity());
        parameter_lv.setAdapter(mAdapter);
    }
}
