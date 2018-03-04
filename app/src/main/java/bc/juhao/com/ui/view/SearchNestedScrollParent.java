package bc.juhao.com.ui.view;

import android.content.Context;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

/**
 * @author JUN
 * @time 2017/4/22  10:26
 * @desc ${TODD}
 */


public class SearchNestedScrollParent extends LinearLayout implements NestedScrollingParent {
    private View mTopView;
    //中间View 在此滑动过渡的地方
    private View mCenterView;
    private View mContentView;

    private NestedScrollingParentHelper mParentHelper;
    private int mTopHeight;
    private int mLayoutHeight;
    private int mLastTouchY;

    public SearchNestedScrollParent(Context context, AttributeSet attrs) {
        super(context, attrs);

        initHelper();
    }

    public SearchNestedScrollParent(Context context) {
        super(context);
    }

    public void removeOnGlobalLayoutListener() {
        if (!mTopView.getViewTreeObserver().isAlive()) return;
        mTopView.getViewTreeObserver().removeOnGlobalLayoutListener(mTopGlobalLayoutListener);
        if (!mCenterView.getViewTreeObserver().isAlive()) return;
        mCenterView.getViewTreeObserver().removeOnGlobalLayoutListener(mCenterGlobalLayoutListener);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        initView();
    }

    private void initView() {
        mTopView = getChildAt(0);
        mCenterView = getChildAt(1);
        mContentView = getChildAt(2);

        mTopView.getViewTreeObserver().addOnGlobalLayoutListener(mTopGlobalLayoutListener);
        mCenterView.getViewTreeObserver().addOnGlobalLayoutListener(mCenterGlobalLayoutListener);
    }

    private ViewTreeObserver.OnGlobalLayoutListener mTopGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            if (mTopHeight <= 0) {
                mTopHeight = mTopView.getMeasuredHeight();
            }
        }
    };

    private ViewTreeObserver.OnGlobalLayoutListener mCenterGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            if (mLayoutHeight <= 0) {
                mLayoutHeight = mCenterView.getMeasuredHeight();
            }
        }
    };

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), mTopView.getMeasuredHeight() +
                mCenterView.getMeasuredHeight() + mContentView.getMeasuredHeight());
    }

    public int getTopViewHeight() {
        return mTopView.getMeasuredHeight();
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return true;
    }

    private void initHelper() {
        mParentHelper = new NestedScrollingParentHelper(this);
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int nestedScrollAxes) {
        mParentHelper.onNestedScrollAccepted(child, target, nestedScrollAxes);
    }

    @Override
    public void onStopNestedScroll(View target) {
        mParentHelper.onStopNestedScroll(target);
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {

    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        if (showLayout(dy) || hideLayout(dy)) {
            consumed[1] = dy;
            scrollBy(0, dy);

        }


    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        return false;
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        return false;
    }

    @Override
    public int getNestedScrollAxes() {
        return 0;
    }


    @Override
    public void scrollTo(int x, int y) {
        if (y < 0) {
            y = 0;
        }
        if (y > mTopHeight) {
            y = mTopHeight;
        }

        super.scrollTo(x, y);
    }

    /**
     * 下拉 向下滑动显示布局
     */
    public boolean showLayout(int dy) {
        if (dy < 0) {
            if (getScrollY() > 0) {
                return true;
            }
        }

        return false;
    }

    /**
     * 上拉 向上滑动，隐藏布局
     *
     * @return
     */
    public boolean hideLayout(int dy) {
        if (dy > 0) {
            if (getScrollY() < mTopHeight) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastTouchY = (int) (event.getRawY() + 0.5f);

                break;
            case MotionEvent.ACTION_MOVE:
                int y = (int) (event.getRawY() + 0.5f);
                int dy = mLastTouchY - y;
                mLastTouchY = y;
                if (showLayout(dy) || hideLayout(dy)) {
                    //父View要滑动
                    scrollBy(0, dy);
                }
                break;
            case MotionEvent.ACTION_UP:

                break;
        }

        return super.dispatchTouchEvent(event);
    }
}
