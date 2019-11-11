package bc.juhao.com.ui.view.popwindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import bc.juhao.com.R;

/**
 * Created by gamekonglee on 2018/8/28.
 */

public class SystemCheckPopWindow extends BasePopwindown {

    private String mContent;
    private String mTitle;
    private TextView tv_title;
    private TextView tv_content;

    public SystemCheckPopWindow(Context context) {
        super(context);
    }
    public SystemCheckPopWindow(Context context,String title,String content) {
        super(context);
        mTitle = title;
        mContent = content;
        tv_title.setText(mTitle);
        tv_content.setText(mContent);
    }
    @Override
    protected void initView(Context context) {
        View contentView = View.inflate(mContext, R.layout.pop_system_check, null);
        tv_title = contentView.findViewById(R.id.tv_title);
        tv_content = contentView.findViewById(R.id.tv_content);

        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity)mContext).finish();
            }
        });
        mPopupWindow = new PopupWindow(contentView,-1, -1);
        // 1.让mPopupWindow内部的控件获取焦点
        mPopupWindow.setFocusable(false);
        // 2.mPopupWindow内部获取焦点后 外部的所有控件就失去了焦点
        mPopupWindow.setOutsideTouchable(false);
        //只有加载背景图还有效果
        mPopupWindow.setBackgroundDrawable(new ColorDrawable());
//        mPopupWindow.setBackgroundDrawable(mActivity.getResources().getDrawable(R.mipmap.bg_invite));
        // 3.如果不马上显示PopupWindow 一般建议刷新界面
        mPopupWindow.update();
        mPopupWindow.setAnimationStyle(R.style.AnimBottom);
//        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.M) {
////6.0之前，返回键的控制
//            mPopupWindow.getContentView().setOnKeyListener(new View.OnKeyListener() {
//                @Override
//                public boolean onKey(View v, int keyCode, KeyEvent event) {
//                    if (keyCode == KeyEvent.KEYCODE_BACK) {
//                        // popupWindow.dismiss();
//                        ((Activity) mContext).finish();
//                        return false;
//                    }
//                    return false;
//                }
//            });
//        }else {
//            //在Android 6.0以上 ，只能通过拦截事件来解决
//            mPopupWindow.setTouchInterceptor(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//
//                    final int x = (int) event.getX();
//                    final int y = (int) event.getY();
//                    ((Activity) mContext).finish();
//                    if ((event.getAction() == MotionEvent.ACTION_DOWN)) {
////                        && ((x < 0) || (x >= mWidth) || (y < 0) || (y >= mHeight))) {
//                        // donothing
//                        // 消费事件
//                        return false;
//                    } else if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
//                        return true;
//                    }
//                    ((Activity) mContext).finish();
//                    return false;
//                }
//
//            });
//        }

    }

    @Override
    public void onDismiss() {
        super.onDismiss();
        ((Activity)mContext).finish();
    }
}
