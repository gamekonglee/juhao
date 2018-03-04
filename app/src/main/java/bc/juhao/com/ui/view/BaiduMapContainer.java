package bc.juhao.com.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * @author: Jun
 * @date : 2017/5/25 9:49
 * @description :
 */
public class BaiduMapContainer extends LinearLayout {
    private ObservableScrollView scrollView;
    public BaiduMapContainer(Context context) {
        super(context);
    }

    public BaiduMapContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setScrollView(ObservableScrollView scrollView) {
        this.scrollView = scrollView;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            scrollView.requestDisallowInterceptTouchEvent(false);
        } else {
            scrollView.requestDisallowInterceptTouchEvent(true);
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }
}

