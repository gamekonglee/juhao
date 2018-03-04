package bc.juhao.com.controller.user;

import android.content.Intent;
import android.os.Message;
import android.widget.EditText;

import bc.juhao.com.R;
import bc.juhao.com.bean.Logistics;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.controller.BaseController;
import bc.juhao.com.data.LogisticDao;
import bc.juhao.com.ui.activity.user.UserLogAddActivity;
import bc.juhao.com.utils.UIUtils;
import bocang.utils.AppDialog;
import bocang.utils.AppUtils;
import bocang.utils.CommonUtil;
import bocang.utils.MyToast;

/**
 * @author: Jun
 * @date : 2017/1/19 17:08
 * @description :
 */
public class UserLogAddController extends BaseController {
    private UserLogAddActivity mView;
    private EditText logistics_name_tv, logistics_phone_tv, logistics_address_tv;
    private Intent mIntent;
    private String  mId="";

    public UserLogAddController(UserLogAddActivity v) {
        mView = v;
        initView();
        initViewData();
    }

    private void initViewData() {
        if(AppUtils.isEmpty(mView.mLogistics))return;
        logistics_name_tv.setText(mView.mLogistics.getName());
        logistics_address_tv.setText(mView.mLogistics.getAddress());
        logistics_phone_tv.setText(mView.mLogistics.getTel());
        mId=mView.mLogistics.getId()+"";
    }

    private void initView() {
        logistics_name_tv = (EditText) mView.findViewById(R.id.logistics_name_tv);
        logistics_phone_tv = (EditText) mView.findViewById(R.id.logistics_phone_tv);
        logistics_address_tv = (EditText) mView.findViewById(R.id.logistics_address_tv);
    }

    @Override
    protected void handleMessage(int action, Object[] values) {

    }

    @Override
    protected void myHandleMessage(Message msg) {

    }

    /**
     * 保存物流
     */
    public void sendAddLogistic() {
        String name=logistics_name_tv.getText().toString();
        String phone=logistics_phone_tv.getText().toString();
        String address=logistics_address_tv.getText().toString();
        if (AppUtils.isEmpty(name)) {
            MyToast.show(mView, "物流公司名称不能为空!");
            return;
        }
        if (AppUtils.isEmpty(phone)) {
            MyToast.show(mView, "电话不能为空!");
            return;
        }

        // 做个正则验证手机号
        if (!CommonUtil.isMobileNO(phone)) {
            AppDialog.messageBox(UIUtils.getString(R.string.mobile_assert));
            return;
        }
        if (AppUtils.isEmpty(address)) {
            MyToast.show(mView, "物流地址不能为空!");
            return;
        }

        LogisticDao dao=new LogisticDao(mView);
        Logistics logistics=new Logistics();
//        logistics.setpId(System.currentTimeMillis() + "");
        logistics.setName(name);
        logistics.setTel(phone);
        logistics.setAddress(address);
        long isSave = dao.replaceOne(logistics);
        if(isSave==-1){
            MyToast.show(mView,"保存失败");
        }else{
            MyToast.show(mView,"保存成功");
            if(!AppUtils.isEmpty(mId)){
                dao.deleteOne(Integer.parseInt(mId));
            }
            mIntent=new Intent();
            mView.setResult(Constance.FROMLOG, mIntent);//告诉原来的Activity 将数据传递给它
            mView.finish();//一定要调用该方法 关闭新的AC 此时 老是AC才能获取到Itent里面的值
        }


    }

}
