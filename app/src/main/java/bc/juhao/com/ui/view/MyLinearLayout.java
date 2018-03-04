package bc.juhao.com.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import bc.juhao.com.listener.ILinearLayoutListener;

/**
 * @author: Jun
 * @date : 2017/8/10 8:59
 * @description :
 */
public class MyLinearLayout extends LinearLayout {
    //是否需要测量
    private boolean isMeasure = false;
    public int mMeasuredHeight = 0;
    public int mTopHeight = 0;
    private ILinearLayoutListener mListener;

    public void setListener(ILinearLayoutListener listener) {
        mListener = listener;
    }

    public MyLinearLayout(Context context) {
        super(context);
    }

    public MyLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (isMeasure) {
//            setMeasuredDimension(getMeasuredWidth(), getMeasuredHeight()
//                    + ((SearchNestedScrollParent) getParent()).getTopViewHeight()+1000);
            setMeasuredDimension(getMeasuredWidth(), getMeasuredHeight()
                    + ((SearchNestedScrollParent) getParent()).getTopViewHeight());

            mMeasuredHeight = getMeasuredHeight()
                    + ((SearchNestedScrollParent) getParent()).getTopViewHeight();
            mTopHeight=((SearchNestedScrollParent) getParent()).getTopViewHeight();
            if (mMeasuredHeight != 0) {
                mListener.onLinearLayoutListener(true);
            }
        }
    }

    public void setOnMeasure(boolean isMeasure) {
        this.isMeasure = isMeasure;
    }


}
