package bc.juhao.com.controller.product;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import bc.juhao.com.R;
import bc.juhao.com.adapter.BaseAdapterHelper;
import bc.juhao.com.adapter.QuickAdapter;
import bc.juhao.com.bean.Default_photo;
import bc.juhao.com.bean.GoodsBean;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.controller.BaseController;
import bc.juhao.com.listener.INetworkCallBack;
import bc.juhao.com.ui.activity.IssueApplication;
import bc.juhao.com.ui.activity.product.ProDetailActivity;
import bc.juhao.com.ui.adapter.ParamentAdapter;
import bc.juhao.com.ui.fragment.ParameterFragment;
import bc.juhao.com.utils.ImageLoadProxy;
import bocang.json.JSONArray;
import bocang.json.JSONObject;
import bocang.utils.AppUtils;

/**
 * @author: Jun
 * @date : 2017/2/14 11:05
 * @description :
 */
public class ParameterController extends BaseController {
    private ParameterFragment mView;
    private ParamentAdapter mAdapter;
    private GridView parameter_lv;
    private com.alibaba.fastjson.JSONObject mProductObject;
    private List<GoodsBean> goodsBeans;
    private QuickAdapter<GoodsBean> adapter;


    public ParameterController(ParameterFragment v) {
        mView = v;
        goodsBeans = new ArrayList<>();
        initView();
        initViewData();
    }

    private void initViewData() {
        sendProductDetail();
    }


    private void initView() {
        parameter_lv = (GridView) mView.getActivity().findViewById(R.id.parameter_lv);
//        parameter_lv.setDivider(null);//去除listview的下划线
        parameter_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(mView.getContext(),ProDetailActivity.class);
                intent.putExtra(Constance.product, goodsBeans.get(i).getId());
                mView.startActivity(intent);
                mView.getActivity().finish();
            }
        });
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
        mNetWork.sendproductLink(mView.productId, new INetworkCallBack() {
            @Override
            public void onSuccessListener(String requestCode, JSONObject ans) {
                JSONArray jsonArray=ans.getJSONArray(Constance.products);
                if(jsonArray==null||jsonArray.length()==0)return;
                Log.e("size",jsonArray.length()+"");
                try {
                    for(int i=0;i<jsonArray.length();i++){
                        goodsBeans.add(new Gson().fromJson(jsonArray.getJSONObject(i).toString(),GoodsBean.class));
                    }

                }catch (Exception e){
                    goodsBeans=new ArrayList<>();
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        GoodsBean goodsBean=new GoodsBean();
                        goodsBean.setId(jsonObject.getInt(Constance.id));
                        goodsBean.setName(jsonObject.getString(Constance.name));
                        goodsBean.setPrice(jsonObject.getString(Constance.price));
                        goodsBean.setCurrent_price(jsonObject.getString(Constance.current_price));
                        Default_photo default_photo=new Default_photo();
                        default_photo.setThumb(jsonObject.getJSONObject(Constance.default_photo).getString(Constance.thumb));
                        goodsBean.setDefault_photo(default_photo);
                        goodsBeans.add(goodsBean);
                    }

                }
                parameter_lv.setAdapter(adapter);
                adapter.replaceAll(goodsBeans);
                adapter.notifyDataSetChanged();
//                Log.e("ans",ans.toString());
            }

            @Override
            public void onFailureListener(String requestCode, JSONObject ans) {
//            Log.e("error",ans.toString());
            }
        });
        mProductObject= ((ProDetailActivity)mView.getActivity()).mProductObject;
       if(AppUtils.isEmpty(mProductObject))
        return;
        com.alibaba.fastjson.JSONArray attachArray = mProductObject.getJSONArray(Constance.attachments);
        mAdapter = new ParamentAdapter(attachArray, mView.getActivity());
        adapter = new QuickAdapter<GoodsBean>(mView.getContext(), R.layout.item_gridview_fm_product) {
            @Override
            protected void convert(BaseAdapterHelper helper, GoodsBean item) {
                helper.setText(R.id.name_tv, (String) item.getName());

                ImageLoader.getInstance().displayImage(item.getDefault_photo().getThumb(), (ImageView) helper.getView(R.id.imageView));
                helper.setText(R.id.old_price_tv,"¥"+item.getPrice()+"");
                ((TextView)helper.getView(R.id.old_price_tv)).getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                helper.setText(R.id.price_tv,"¥"+item.getCurrent_price()+"");
            }
        };

    }
}
