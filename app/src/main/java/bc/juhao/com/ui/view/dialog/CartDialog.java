package bc.juhao.com.ui.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.jsoup.select.Evaluator;
import org.mozilla.javascript.tools.jsc.Main;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.crypto.spec.IvParameterSpec;

import bc.juhao.com.R;

/**
 * author: cjt
 * date:  2019-12-06$
 * ClassName: CartDialog$
 * Description:
 */
public class CartDialog extends Dialog {

    private static final String TAG = "CartDialog";
    private View  contentView;
    private Button btn_cannal, btn_confirm;
    ImageView iv_reduce, iv_plus;
    LinearLayout ll_select;
    private TextView tv_title, tv_num;
    private String msg;
    private int num = 0, max = 999, mul = 1;
    private Boolean isWithNumSelect = false;


    public CartDialog(Context context, String msg, OnResultListener onResultListener) {
        super(context, R.style.customDialog);

        this.msg = msg;
        this.onResultListener = onResultListener;

        contentView = View.inflate(context, R.layout.cart_dialog, null);
        setContentView(contentView);
    }

    /**
     * @param context
     * @param msg
     * @param num 当前数量
     * @param mul 倍数 同时为最小值
     * @param max 最大值
     * @param onResultListener
     */
    public CartDialog(Context context, String msg, int num, int mul, int max, OnResultListener onResultListener) {
        super(context, R.style.customDialog);

        this.msg = msg;
        this.onResultListener = onResultListener;
        this.isWithNumSelect = true;
        this.num = num;
        this.mul = mul;
        this.max = max;

        contentView = View.inflate(context, R.layout.cart_dialog, null);
        setContentView(contentView);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: num:" + num + " max:" + max +" mul:" + mul);

        btn_cannal = contentView.findViewById(R.id.btn_cancel);
        btn_confirm = contentView.findViewById(R.id.btn_confirm);
        tv_title = contentView.findViewById(R.id.tv_title);

        btn_cannal.setOnClickListener(onClickListener);
        btn_confirm.setOnClickListener(onClickListener);
        tv_title.setText(msg);


        ll_select = findViewById(R.id.ll_select);
        iv_plus = findViewById(R.id.iv_plus);
        iv_reduce = findViewById(R.id.iv_reduce);
        tv_num = findViewById(R.id.tv_num);

        if (isWithNumSelect){
            ll_select.setVisibility(View.VISIBLE);
        }

        iv_plus.setOnTouchListener(onTouchListener);
        iv_reduce.setOnTouchListener(onTouchListener);
        tv_num .setText(getNum(num)+"");
    }

    private int getNum(int num){
        Log.d(TAG, "getNum: "+num);
        num = num < mul ? mul : num;
        num = num - num % mul;
        num = num > max ? num - mul : num;
        this.num = num;
        return num;
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_confirm:
                    onResultListener.onConfirm(num);
                    dismiss();
                    break;
                case R.id.btn_cancel:
                    onResultListener.onCancel();
                    dismiss();
                    break;
            }
        }
    };


    //长按连续事件
    View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if(event.getAction() == MotionEvent.ACTION_DOWN){
                updateAddOrSubtract(v.getId());    //手指按下时触发不停的发送消息
            }else if(event.getAction() == MotionEvent.ACTION_UP){
                stopAddOrSubtract();    //手指抬起时停止发送
            }
            return true;
        }
    };

    private ScheduledExecutorService scheduledExecutor;
    private void updateAddOrSubtract(int viewId) {
        final int vid = viewId;
        scheduledExecutor = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutor.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                Message msg = new Message();
                msg.what = vid;
                handler.sendMessage(msg);
            }
        }, 0, 100, TimeUnit.MILLISECONDS);    //每间隔100ms发送Message
    }

    private void stopAddOrSubtract() {
        if (scheduledExecutor != null) {
            scheduledExecutor.shutdownNow();
            scheduledExecutor = null;
        }
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            int viewId = msg.what;
            switch (viewId){
                case R.id.iv_reduce:
                    if (num == mul) {
                        return;
                    }
                    num -= mul;
                    tv_num.setText(getNum(num) + "");
                    break;
                case R.id.iv_plus:
                    if (num + mul > max) {
                        return;
                    }
                    num += mul;
                    tv_num.setText(getNum(num) + "");
                    break;
            }
        }
    };


    private OnResultListener onResultListener;

    public interface OnResultListener{
        void onConfirm(int num);
        void onCancel();
    }
}
