package bc.juhao.com.utils;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * @author shaungyu.he
 * @version : 7.1
 *          modify :
 * @time 2017/2/17  15:59
 * <p/>
 * filename :
 * action :总之 动画分为开始点 结束点 以及中间的控制点
 */
public class CartAnimator {
    private Context mContext;
    //路径测量
    private PathMeasure mPathMeasure;
    //中间点的坐标
    private float[] mCenterPosition = new float[2];
    //父布局
    private RelativeLayout mParentView;

    public void setParentView(RelativeLayout view) {
        this.mParentView = view;
    }

    //购物车
    private View mCart;

    public void setCartView(View view) {
        this.mCart = view;
    }

    public CartAnimator(Context context) {
        mContext = context;
    }

    public void startCartAnimator(ImageView iv, View startIv) {
        if (mContext == null) return;
        final ImageView imageView = new ImageView(mContext);
        imageView.setImageDrawable(iv.getDrawable());

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        mParentView.addView(imageView, params);

        //得到父布局的起始点绝对坐标
        int[] parentLocation = new int[2];
        mParentView.getLocationInWindow(parentLocation);

        //得到图片(加号)的绝对坐标 用于计算动画开始的坐标
        int startLoc[] = new int[2];
        startIv.getLocationInWindow(startLoc);

        //得到购物车图片的绝对坐标用于计算动画结束后的坐标
        int endLoc[] = new int[2];
        mCart.getLocationInWindow(endLoc);
        //计算动画开始.结束的坐标
        //开始掉落的商品的起始点(可进行微调)
        float startX = startLoc[0] - parentLocation[0] + startIv.getWidth() / 5;
        float startY = startLoc[1] - parentLocation[1] + startIv.getHeight() / 5;
        //商品掉落的终点坐标
        float toX = endLoc[0] - parentLocation[0] + 10 * (mCart.getWidth()) / 39;
        float toY = endLoc[1] - parentLocation[1];

        //开始绘制贝塞尔曲线
        Path path = new Path();
        //移动到起始点
        path.moveTo(startX, startY);
        //控制点x.y坐标值 和 结束点的坐标
        path.quadTo((startX + toX) / 2.2f, startY / 1.6f, toX, toY);
        //mPathMeasure用来计算贝塞尔曲线的曲线长度和贝塞尔曲线中间插值的坐标，
        //如果是true，path会形成一个闭环
        mPathMeasure = new PathMeasure(path, false);

        //属性动画实现（从0到贝塞尔曲线的长度之间进行插值计算,获取中间过程的距离值）
        ValueAnimator valueAnimator = ValueAnimator.
                ofFloat(0, mPathMeasure.getLength());

        valueAnimator.setDuration(500);
        //开启图片的组合动画
        groupAnimRun(imageView);
        // 匀速线性插值器
        valueAnimator.setInterpolator(new LinearInterpolator());
        //valueAnimator设定值的动画后 具体ImageView的动画展示
        //在addUpdateListener中
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // 当插值计算进行时，获取中间的每个值，
                // 这里这个值是中间过程中的曲线长度（下面根据这个值来得出中间点的坐标值）
                float value = (Float) animation.getAnimatedValue();
                //Log.i("058t", "value=" + value);
                mPathMeasure.getPosTan(value, mCenterPosition, null);
                // 移动的商品图片的坐标设置为该中间点的坐标
                imageView.setTranslationX(mCenterPosition[0]);
                imageView.setTranslationY(mCenterPosition[1]);
            }
        });

        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                // 把移动的imageView从父布局里移除
                if (mParentView != null) {
                    mParentView.removeView(imageView);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        valueAnimator.start();
    }

    /**
     * 组合动画
     *
     * @param view
     */
    private void groupAnimRun(View view) {
        ObjectAnimator anim1 = ObjectAnimator.ofFloat(view, "scaleX",
                1.5f, 0.8f, 0.4f, 0.3f, 0.2f, 0.1f);
        ObjectAnimator anim2 = ObjectAnimator.ofFloat(view, "scaleY",
                1.5f, 0.8f, 0.4f, 0.3f, 0.2f, 0.1f);
        ObjectAnimator anim3 = ObjectAnimator.ofFloat(view, "rotation", 0.0F, 360.0F);

        AnimatorSet set = new AnimatorSet();

        set.setDuration(1000);
        set.setInterpolator(new LinearInterpolator());
        set.playTogether(anim1, anim2, anim3);
        set.start();
    }
}
