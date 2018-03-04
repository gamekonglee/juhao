package bc.juhao.com.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import bc.juhao.com.R;


/**
 * @time 2016/8/23  15:15
 * @desc 1 在宽度(已知)精确的情况下,按照一个规定的比例求出高度
 * @desc 2 在高地(已知)精确的情况下,按照一个规定的比例求出宽度
 */
public class RatioLayout extends FrameLayout {
    float mRatio = 2.43f;//宽度/高度的值

    public static final int RELATIVTE_WIDTH=0;//相对于宽度
    public static final  int RELATIVE_HEIGHT=1;//相等高度

    public  int mCurrentRelative =RELATIVTE_WIDTH;

    //作为工具方法,可以随便调用修改
    public void setRatio(float ratio) {
        mRatio = ratio;
    }

    public void setCurrentRelative(int state) {
        if(state>1 || state<0) {//只能等于0或者1
            return;
        }
        mCurrentRelative=state;
       // this.mCurrentRelative = mCurrentRelative;
    }

    public RatioLayout(Context context) {
        super(context);

    }

    public RatioLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RatioLayout);
        mRatio = a.getFloat(R.styleable.RatioLayout_ratio, 0f);

        mCurrentRelative=a.getInt(R.styleable.RatioLayout_relative,0);

        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        /**模式分为2种
         * 精确 matcherparent 180dp
         * wrap_content 不精确
         */
       //获得宽度的模式和值
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        //获得高度的模式和值
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heighSize = MeasureSpec.getSize(heightMeasureSpec);

        //宽度一定
        if(widthMode==MeasureSpec.EXACTLY && mCurrentRelative ==RELATIVTE_WIDTH &&mRatio!=0 ) {
            //子控件的宽度
           int width=widthSize-getPaddingLeft()-getPaddingRight();
           int height= (int) (width/mRatio+.5f);

            //如果要测量孩子,先获得孩子的宽高规格

            //子孩子的宽高规格
            int childWidthMeasureSpec=MeasureSpec.makeMeasureSpec(width,MeasureSpec.EXACTLY);
            int childHeightMeasureSpec=MeasureSpec.makeMeasureSpec(height,MeasureSpec.EXACTLY);
            //测量子孩子
            measureChildren(childWidthMeasureSpec,childHeightMeasureSpec);
            //设置会当前父容器的值
            int measuWidth=widthSize;
            int measuHight=height+getPaddingTop()+getPaddingBottom();
            setMeasuredDimension(measuWidth,measuHight);
         //高度确定
        }else if(heightMode==MeasureSpec.EXACTLY&& mCurrentRelative ==RELATIVE_HEIGHT &&mRatio!=0){

              int height= heighSize-getPaddingBottom()-getPaddingTop();
              int width= (int) (height*mRatio+.5f);

            //如果要测量孩子,先获得孩子的宽高规格

            //子孩子的宽高规格
            int childWidthMeasureSpec=MeasureSpec.makeMeasureSpec(width,MeasureSpec.EXACTLY);
            int childHeightMeasureSpec=MeasureSpec.makeMeasureSpec(height,MeasureSpec.EXACTLY);
            //测量子孩子
            measureChildren(childWidthMeasureSpec,childHeightMeasureSpec);
            //设置会当前父容器的值
            int measuWidth=width+getPaddingRight()+getPaddingLeft();
            int measuHight=heighSize;
            setMeasuredDimension(measuWidth,measuHight);

        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }



    }
}
