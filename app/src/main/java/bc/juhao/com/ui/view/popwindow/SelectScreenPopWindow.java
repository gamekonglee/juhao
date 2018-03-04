package bc.juhao.com.ui.view.popwindow;

import android.content.Context;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import bc.juhao.com.R;
import bc.juhao.com.listener.ISelectScreenListener;


/**
 * @author: Jun
 * @date : 2017/2/16 15:12
 * @description :
 */
public class SelectScreenPopWindow extends BasePopwindown implements View.OnClickListener {
    private RelativeLayout select_image_rl,select_photo_rl,select_screen_rl;
    private ISelectScreenListener mListener;

    public void setListener(ISelectScreenListener listener) {
        mListener = listener;
    }

    public SelectScreenPopWindow(Context context) {
        super(context);

    }

    @Override
    protected void initView(Context context) {
        View contentView = View.inflate(mContext, R.layout.pop_select_screen, null);
        initUI(contentView);
        initViewData();

    }

    private void initViewData() {
    }



    private void initUI(View contentView) {
        select_image_rl = (RelativeLayout) contentView.findViewById(R.id.select_image_rl);
        select_image_rl.setOnClickListener(this);
        select_screen_rl = (RelativeLayout) contentView.findViewById(R.id.select_screen_rl);
        select_screen_rl.setOnClickListener(this);
        select_photo_rl = (RelativeLayout) contentView.findViewById(R.id.select_photo_rl);
        select_photo_rl.setOnClickListener(this);
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
            case R.id.main_rl:
                onDismiss();
                break;
            case R.id.select_screen_rl://选择场景
                mListener.onSelectScreenChanged(0);
                onDismiss();
                break;
            case R.id.select_photo_rl://拍照
                mListener.onSelectScreenChanged(1);
                onDismiss();
                break;
            case R.id.select_image_rl://选择相册
                mListener.onSelectScreenChanged(2);
                onDismiss();
                break;

        }
    }




}
