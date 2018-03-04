package bc.juhao.com.ui.activity.user;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;

import bc.juhao.com.R;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.controller.user.MyOrderController;
import bc.juhao.com.ui.activity.buy.SearchActivity;
import bocang.view.BaseActivity;

/**
 * @author: Jun
 * @date : 2017/2/6 11:08
 * @description :我的订单
 */
public class MyOrderActivity extends BaseActivity implements OnItemClickListener {
    private MyOrderController mController;
    public int mOrderType = 0;

    public static String PARTNER;
    public static String SELLER;
    public static String RSA_PRIVATE;
    public static RelativeLayout search_rl;
    private AlertView mAlertViewExt;//窗口拓展例子
    private EditText etName;//拓展View内容
    private InputMethodManager imm;

    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {
        mController = new MyOrderController(this);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_myorder);
        //沉浸式状态栏
//        setColor(this, getResources().getColor(R.color.colorPrimary));
        search_rl = getViewAndClick(R.id.search_rl);

        imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        //拓展窗口
        mAlertViewExt = new AlertView("提示", "请输入要查询的订单号！", "取消", null, new String[]{"完成"}, this, AlertView.Style.Alert, this);
        ViewGroup extView = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.alertext_form,null);
        etName = (EditText) extView.findViewById(R.id.etName);
        etName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focus) {
                //输入框出来则往上移动
                boolean isOpen = imm.isActive();
                mAlertViewExt.setMarginBottom(isOpen && focus ? 120 : 0);
                System.out.println(isOpen);
            }
        });
        mAlertViewExt.addExtView(extView);
    }

    private void closeKeyboard() {
        //关闭软键盘
        imm.hideSoftInputFromWindow(etName.getWindowToken(), 0);
        //恢复位置
        mAlertViewExt.setMarginBottom(0);
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        mOrderType = intent.getIntExtra(Constance.order_type, 0);
    }

    @Override
    protected void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.search_rl:
                Intent intent=new Intent(this, SearchActivity.class);
                this.startActivity(intent);
                break;
        }
    }

    @Override
    public void onItemClick(Object o, int position) {
//        closeKeyboard();
//        //判断是否是拓展窗口View，而且点击的是非取消按钮
//        if(o == mAlertViewExt && position != AlertView.CANCELPOSITION){
//            String name = etName.getText().toString();
//            if(name.isEmpty()){
//                MyToast.show(this,"请输入需要查询的订单号!");
//            }
//            else{
//                mController.SearchOrder(name);
//            }
//
//            return;
//        }
    }
}
