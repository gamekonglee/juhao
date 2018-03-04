package bc.juhao.com.ui.view.popwindow;

import android.content.ClipboardManager;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;

import bc.juhao.com.R;
import bc.juhao.com.listener.ITwoCodeListener;
import bc.juhao.com.utils.ImageUtil;


/**
 * @author: Jun
 * @date : 2017/2/16 15:12
 * @description :
 */
public class TwoCodeSharePopWindow extends BasePopwindown implements View.OnClickListener, View.OnLongClickListener {
    private ImageView two_code_iv;
    private RelativeLayout main_rl;
    private Timer timer;
    private ITwoCodeListener mListener;
    private TextView remark_tv;
    private String mSharePath="";


    public void setListener(ITwoCodeListener listener) {
        mListener = listener;
    }

    public TwoCodeSharePopWindow(Context context) {
        super(context);


    }

    @Override
    protected void initView(Context context) {
        View contentView = View.inflate(mContext, R.layout.pop_two_code, null);
        initUI(contentView);

    }

    public void getTwoCode(String sharePath,String shareRemark) {
        two_code_iv.setVisibility(View.VISIBLE);
        two_code_iv.setImageBitmap(ImageUtil.createQRImage(sharePath, 700, 700));
        remark_tv.setText(shareRemark);
        mSharePath=sharePath;
    }

    private void initUI(View contentView) {
        two_code_iv = (ImageView) contentView.findViewById(R.id.two_code_iv);
        remark_tv = (TextView) contentView.findViewById(R.id.remark_tv);
        main_rl = (RelativeLayout) contentView.findViewById(R.id.main_rl);
        two_code_iv.setOnLongClickListener(this);
        main_rl.setOnClickListener(this);
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
        }
    }


    @Override
    public boolean onLongClick(View v) {
        ClipboardManager cm = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
        cm.setText(mSharePath);
        Toast.makeText(mContext, "复制成功!",Toast.LENGTH_LONG);
        return false;
    }
}
