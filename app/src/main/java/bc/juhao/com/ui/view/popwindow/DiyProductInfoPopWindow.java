package bc.juhao.com.ui.view.popwindow;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import bc.juhao.com.R;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.listener.IDiyProductInfoListener;
import bc.juhao.com.listener.INetworkCallBack;
import bc.juhao.com.ui.activity.user.UserLogActivity;
import bc.juhao.com.ui.adapter.ParamentAdapter02;
import bocang.json.JSONArray;
import bocang.json.JSONObject;


/**
 * @author: Jun
 * @date : 2017/2/16 15:12
 * @description :
 */
public class DiyProductInfoPopWindow extends BasePopwindown implements View.OnClickListener, INetworkCallBack {
    private Activity mActivity;
    private IDiyProductInfoListener mListener;
    private ImageView product_iv, close_iv;
    private RelativeLayout two_bar_codes_rl, parameter_rl, logo_rl, card_rl;
    public JSONObject productObject;
    private ListView attr_lv;
    private ParamentAdapter02 mParamentAdapter;
    private StringBuffer mParamMsg;

    public void setListener(IDiyProductInfoListener listener) {
        mListener = listener;
    }

    public DiyProductInfoPopWindow(Context context, Activity activity) {
        super(context);
        mActivity = activity;
    }

    @Override
    protected void initView(Context context) {
        View contentView = View.inflate(mContext, R.layout.pop_diy_product_info, null);
        initUI(contentView);
    }

    public void initViewData() {
        String path = productObject.getJSONObject(Constance.app_img).getString(Constance.img);
        ImageLoader.getInstance().displayImage(path, product_iv);
        getDetail();
    }


    private void initUI(View contentView) {

        product_iv = (ImageView) contentView.findViewById(R.id.product_iv);
        two_bar_codes_rl = (RelativeLayout) contentView.findViewById(R.id.two_bar_codes_rl);
        parameter_rl = (RelativeLayout) contentView.findViewById(R.id.parameter_rl);
        logo_rl = (RelativeLayout) contentView.findViewById(R.id.logo_rl);
        card_rl = (RelativeLayout) contentView.findViewById(R.id.card_rl);
        close_iv = (ImageView) contentView.findViewById(R.id.close_iv);
        attr_lv = (ListView) contentView.findViewById(R.id.attr_lv);
        product_iv.setOnClickListener(this);
        two_bar_codes_rl.setOnClickListener(this);
        parameter_rl.setOnClickListener(this);
        logo_rl.setOnClickListener(this);
        card_rl.setOnClickListener(this);
        close_iv.setOnClickListener(this);

        mPopupWindow = new PopupWindow(contentView, -1, -1);
        // 1.让mPopupWindow内部的控件获取焦点
        mPopupWindow.setFocusable(true);
        // 2.mPopupWindow内部获取焦点后 外部的所有控件就失去了焦点
        mPopupWindow.setOutsideTouchable(true);
        //只有加载背景图还有效果
        // 3.如果不马上显示PopupWindow 一般建议刷新界面
        mPopupWindow.update();
        // 设置弹出窗体显示时的动画，从底部向上弹出
        mPopupWindow.setAnimationStyle(R.style.AnimBottom);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.close_iv:
                onDismiss();
                break;
            case R.id.bg_ll:
                onDismiss();
                break;
            case R.id.btn_logistic:
                Intent intent = new Intent(mContext, UserLogActivity.class);
                intent.putExtra(Constance.isSelectLogistice, true);
                mActivity.startActivityForResult(intent, Constance.FROMLOG);
                onDismiss();
                break;
            case R.id.two_bar_codes_rl://二维码
                mListener.onDiyProductInfo(0,null);
                onDismiss();
                break;
            case R.id.parameter_rl://参数
                mListener.onDiyProductInfo(1,mParamMsg.toString());
                onDismiss();
                break;
            case R.id.logo_rl://LOGO
                mListener.onDiyProductInfo(2,null);
                onDismiss();
                break;
            case R.id.card_rl://产品卡
                mListener.onDiyProductInfo(3,null);
                onDismiss();
                break;
        }
    }

    /**
     * 产品详情
     */
    public void getDetail() {
        mParamMsg=new StringBuffer();
        JSONArray attachmentArray = productObject.getJSONArray(Constance.attachments);
        List<String> attachs = new ArrayList<>();
        String name=productObject.getString(Constance.name);
        if(name.length()>=8){
            name=name.substring(0,8);
        }
        attachs.add("名称: " + name);
        attachs.add("价格: " +"￥"+ productObject.getString(Constance.current_price) );
        mParamMsg.append("名称: " + name+ "\n");
        mParamMsg.append("价格: ￥" + productObject.getString(Constance.current_price) + "\n");
        for (int i = 0; i < attachmentArray.length(); i++) {
            JSONObject jsonObject = attachmentArray.getJSONObject(i);
            JSONArray attrs = jsonObject.getJSONArray(Constance.attrs);
            attachs.add(jsonObject.getString(Constance.name) + ": " + attrs.getJSONObject(0).getString(Constance.attr_name));
            if(i<attachmentArray.length()-1){
                mParamMsg.append(jsonObject.getString(Constance.name) + ": " + attrs.getJSONObject(0).getString(Constance.attr_name)+"\n");
            }else{
                mParamMsg.append(jsonObject.getString(Constance.name) + ": " + attrs.getJSONObject(0).getString(Constance.attr_name));
            }

        }



        mParamentAdapter = new ParamentAdapter02(attachs, mContext, 1);
        attr_lv.setAdapter(mParamentAdapter);
        attr_lv.setDivider(null);//去除listview的下划线
    }


    @Override
    public void onSuccessListener(String requestCode, JSONObject ans) {
    }

    @Override
    public void onFailureListener(String requestCode, JSONObject ans) {
    }

}
