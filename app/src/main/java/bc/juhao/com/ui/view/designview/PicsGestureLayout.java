package bc.juhao.com.ui.view.designview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.nostra13.universalimageloader.utils.L;

import java.util.ArrayList;
import java.util.List;

import bc.juhao.com.R;
import bc.juhao.com.ui.activity.programme.DiyActivity;
import bc.juhao.com.ui.view.gesture.VersionedGestureDetector;
import bc.juhao.com.ui.view.gesture.VersionedGestureDetector.OnGestureListener;


public class PicsGestureLayout extends FrameLayout implements
		OnGestureListener, OnDoubleTapListener {
	private VersionedGestureDetector mScaleDragDetector;// 缩放手势监听类
	private GestureDetector mGestureDetector; // 手势识别类

	float x, y;
	// PointF mCenterPoint;
	public Canvas canvas;
	public Paint paint;
	private Bitmap bitmap, bitmap1, bitmap2, bitmap3; // 当前被渲染的位图
	boolean longpressdialog = true;
	private DiyActivity newSheJi;

	private IPicsGestureListener mIPicsGestureListener;

	public void setPicsGestureListener(IPicsGestureListener IPicsGestureListener) {
		mIPicsGestureListener = IPicsGestureListener;
	}

	public PicsGestureLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		newSheJi = (DiyActivity) getContext();
		init();
	}

	public int action = ACTION_NORMAL;
	public static final int ACTION_DRAWLINE = 1;
	public static final int ACTION_NORMAL = 2;
	protected static final String TAG = "PicGestureLayout";
	private int width;
	private int height;
	int bgColor;

	private boolean locked = false;// 判断当前的素材是否被锁住

	// 声明一个数组进行存储imagewrapper对象
	private final List<ImageWrapper> childList = new ArrayList<ImageWrapper>();

	private void init() {
		// L.i(TAG, "PicsGustureLayout中的一些参数");
		mScaleDragDetector = VersionedGestureDetector.newInstance(getContext(),
				this);
		mGestureDetector = new GestureDetector(getContext(),
				new GestureDetector.SimpleOnGestureListener() {
					@Override
					public void onLongPress(MotionEvent e) {// 长安
						// if(longpressdialog){
						// L.i(TAG, "判断为真,进入长按事件");
						// newSheJi.showLongClickView(PicsGestureLayout.this);
						// }
					}

					@Override
					public boolean onDown(MotionEvent event) {
						action = ACTION_NORMAL;

						// 获取选中子img对象
						ImageWrapper imageWrapper = getSelectedChildImage();
						if (imageWrapper != null) {
							invalidate();
							return true;
						}
						return false;
					}
				});
		mGestureDetector.setIsLongpressEnabled(true);
		mGestureDetector.setOnDoubleTapListener(this);
		// L.i(TAG, "初始化设置双击监听,");
		WindowManager wm = (WindowManager) getContext().getSystemService(
				Context.WINDOW_SERVICE);
		width = wm.getDefaultDisplay().getHeight();
		height = wm.getDefaultDisplay().getWidth();
		// L.i(TAG, "调用系统服务获取宽:" + width + ",高:" + height);
		bgColor = Color.BLACK; // 设置背景颜色
		// L.i(TAG, "设置背景颜色 Color.BLACK:" + bgColor);
		// L.i(TAG, "第100行,容易出现内存溢出,");
		// argb8888支持透明
		beijingbitmap();
		// bitmap = Bitmap.createBitmap(height, width, Bitmap.Config.ARGB_8888);
		// //设置位图，线就画在位图上面，第一二个参数是位图宽和高

		// L.i(TAG, "创建bitmap:" + bitmap);
		canvas = new Canvas(bitmap);
		paint = new Paint(Paint.DITHER_FLAG);

		paint.setAntiAlias(true); // 设置抗锯齿，一般设为true
		// p.setARGB(0, 30, 46, 79); //
		paint.setColor(Color.RED); // 设置默认线的颜色
		paint.setStrokeCap(Paint.Cap.ROUND); // 设置默认线的类型
		paint.setStrokeWidth(5); // 设置线的宽度
		// L.i(TAG, "设置线宽:5");
		Drawable dd = newSheJi.getResources().getDrawable(
				R.drawable.bussiness_edit_del);
		bitmap = drawableToBitmap(dd);

//		Drawable dd1 = newSheJi.getResources().getDrawable(
//				R.drawable.bussiness_edit_symmetry);
//		bitmap1 = drawableToBitmap(dd1);

//		Drawable dd2 = newSheJi.getResources().getDrawable(
//				R.drawable.busniness_lib_photopicker_icon_select_pressed);
//		bitmap2 = drawableToBitmap(dd2);

		Drawable dd3 = newSheJi.getResources().getDrawable(
				R.drawable.bussiness_edit_control);
		bitmap3 = drawableToBitmap(dd3);

	}

	public void beijingbitmap() {
		// L.i(TAG, "第100行,容易出现内存溢出,");
		if (bitmap == null) {
			// L.i(TAG, "第100行,位图不为空,");
			bitmap = Bitmap.createBitmap(height, width, Bitmap.Config.ARGB_8888); // 设置位图，线就画在位图上面，第一二个参数是位图宽和高
		}
	}

	@SuppressLint("WrongCall")
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		for (ImageWrapper child : childList) {
			child.onDraw(canvas);
		}
		try {

			if (null != getSelectedChildImage()) {

			// 中心坐标
			//	canvas.drawBitmap(bitmap,getSelectedChildImage().mCenterPoint.x- bitmap.getWidth() / 2,
			//			getSelectedChildImage().mCenterPoint.y- bitmap.getHeight() / 2, paint);

				
				// 左上角 删除
				canvas.drawBitmap(bitmap, getSelectedChildImage().mLTPoint.x
						- bitmap.getWidth() / 2,
						getSelectedChildImage().mLTPoint.y - bitmap.getHeight()
								/ 2, paint);

//				// 右上角 反转
//				canvas.drawBitmap(bitmap1, getSelectedChildImage().mRTPoint.x
//						- bitmap.getWidth() / 2,
//						getSelectedChildImage().mRTPoint.y - bitmap.getHeight()
//								/ 2, paint);

//				// 左下角 自定义
//				canvas.drawBitmap(bitmap2, getSelectedChildImage().mLBPoint.x
//						- bitmap.getWidth() / 2,
//						getSelectedChildImage().mLBPoint.y - bitmap.getHeight()
//								/ 2, paint);

				// 右下角 拉伸变形
				canvas.drawBitmap(bitmap3, getSelectedChildImage().mRBPoint.x
						- bitmap.getWidth() / 2,
						getSelectedChildImage().mRBPoint.y - bitmap.getHeight()
								/ 2, paint);
			}

		} catch (Exception e) {
		}
		// canvas.drawBitmap(bitmap, 0, 0, null);//不知道为什么加上了
		// L.i(TAG, "onDraw()循环调用子类的ondraw进行绘制");
	}

	public static Bitmap drawableToBitmap(Drawable drawable) {

		Bitmap bitmap = Bitmap
				.createBitmap(
						drawable.getIntrinsicWidth(),
						drawable.getIntrinsicHeight(),
						drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
								: Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		// canvas.setBitmap(bitmap);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight());
		drawable.draw(canvas);
		return bitmap;
	}

	private float startRotation = 0.0f;
	private float mPreAngle = 0.0f;
	private boolean inRotation = false; // 能不能旋转
	private boolean suoInRotation = false; // 判断锁定的地方,为了旋转正常
	private boolean la = false;//
	

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
	}

	/**
	 * 
	 * 触摸事件
	 * 
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (action == ACTION_NORMAL) { // 用来移动

			switch (event.getAction() & MotionEvent.ACTION_MASK) {

			case MotionEvent.ACTION_DOWN:// 第一个点按下
			//	inRotation = true; // 能不能旋转,锁的锁定之一
				try {

					// L.i(TAG, "单击时:x=" +
					// getSelectedChildImage().mCenterPoint.x + ",y="+
					// getSelectedChildImage().mCenterPoint.y);
					// 点中中心点区域
					if (event.getX() > getSelectedChildImage().mCenterPoint.x
							- bitmap.getWidth() / 2
							&& event.getX() < getSelectedChildImage().mCenterPoint.x
									+ bitmap.getWidth() / 2) {

						if (event.getY() > getSelectedChildImage().mCenterPoint.y
								- bitmap.getHeight()
								&& event.getY() < getSelectedChildImage().mCenterPoint.y
										+ bitmap.getHeight()) {

							// L.i(TAG, "点中中心");
							//Toast.makeText(newSheJi, "点中中心", Toast.LENGTH_SHORT).show();
						}

					}
					// 点中左上角区域
					if (event.getX() > getSelectedChildImage().mLTPoint.x- bitmap.getWidth() / 2
							&& event.getX() < getSelectedChildImage().mLTPoint.x+ bitmap.getWidth() / 2) {

						if (event.getY() > getSelectedChildImage().mLTPoint.y- bitmap.getHeight()
								&& event.getY() < getSelectedChildImage().mLTPoint.y+ bitmap.getHeight()) {

							// L.i(TAG, "点中中心");
							//Toast.makeText(newSheJi, "点中左上角",Toast.LENGTH_SHORT).show();

							try {

								this.deleteChildImage(this.getSelectedChildImage());
								mIPicsGestureListener.onPicsGesturDelete(true);
							} catch (Exception e) {

							}
						}

					}
					// 点中右上角区域
					if (event.getX() > getSelectedChildImage().mRTPoint.x- bitmap.getWidth() / 2
							&& event.getX() < getSelectedChildImage().mRTPoint.x+ bitmap.getWidth() / 2) {

						if (event.getY() > getSelectedChildImage().mRTPoint.y- bitmap.getHeight()
								&& event.getY() < getSelectedChildImage().mRTPoint.y+ bitmap.getHeight()) {

							// L.i(TAG, "点中中心");
//							Toast.makeText(newSheJi, "点中右上角",Toast.LENGTH_SHORT).show();
							
							
							
						}

					}

					// 点中左下角区域
					if (event.getX() > getSelectedChildImage().mLBPoint.x- bitmap.getWidth() / 2
							&& event.getX() < getSelectedChildImage().mLBPoint.x+ bitmap.getWidth() / 2) {

						if (event.getY() > getSelectedChildImage().mLBPoint.y- bitmap.getHeight()
								&& event.getY() < getSelectedChildImage().mLBPoint.y+ bitmap.getHeight()) {

							// L.i(TAG, "点中中心");
//							Toast.makeText(newSheJi, "点中左下角",Toast.LENGTH_SHORT).show();
						}

					}

					// 点中右下角区域
					if (event.getX() > getSelectedChildImage().mRBPoint.x- bitmap.getWidth() / 2
							&& event.getX() < getSelectedChildImage().mRBPoint.x+ bitmap.getWidth() / 2) {

						if (event.getY() > getSelectedChildImage().mRBPoint.y- bitmap.getHeight()
								&& event.getY() < getSelectedChildImage().mRBPoint.y+ bitmap.getHeight()) {
							
							//Toast.makeText(newSheJi, "点中右下角",Toast.LENGTH_SHORT).show();
						    la = true;//
						    
						
						}

					}
					
					try {
						
						getSelectedChildImage().mPreMovePointF.set(event.getX(),event.getY());// 最后再设置
					
					} catch (Exception e) {

						e.printStackTrace();

					}
					
					
					
					

				} catch (Exception e) {
					
					e.printStackTrace();
					
				} catch (Error err) {
					
					err.printStackTrace();
			
				}

				break;

			case MotionEvent.ACTION_POINTER_DOWN: // 第二个手指按下

				inRotation = true; // 能不能旋转,锁的锁定之一
				suoInRotation = true;// 锁定状态
				startRotation = rotation(event);// 取旋转角
				mPreAngle = 0;// 旋转角?
				// L.i(TAG, "触摸事件按下");
				break;

			case MotionEvent.ACTION_MOVE:// 移动
				// L.i(TAG, "触摸事件移动");

				// inRotation=false;
				
				if (la) {//这里重写右下角 拉伸事件
					L.i("移动2---", "移动2---");
					getSelectedChildImage().mCurMovePointF.set(event.getX(),event.getY());// 设置子image移动后坐标为当前触摸点
					float scale = 1f;
					
					int halfBitmapWidth = (int) getSelectedChildImage().displayXSize / 2;
					int halfBitmapHeight = (int) getSelectedChildImage().displayYSize / 2;

					// 图片某个点到图片中心的距离
					float bitmapToCenterDistance = (float) Math.sqrt(halfBitmapWidth * halfBitmapWidth+ halfBitmapHeight * halfBitmapHeight);

					// 移动的点到图片中心的距离
					float moveToCenterDistance = distance4PointF(
							getSelectedChildImage().mCenterPoint,
							getSelectedChildImage().mCurMovePointF);

					// 计算缩放比例
					scale = moveToCenterDistance / bitmapToCenterDistance;

					// 缩放比例的界限判断
					/*
					 * if (scale <= MIN_SCALE) { scale = MIN_SCALE; } else if
					 * (scale >= MAX_SCALE) { scale = MAX_SCALE; }
					 */

					// 角度
					double a = distance4PointF(
							getSelectedChildImage().mCenterPoint,
							getSelectedChildImage().mPreMovePointF);
					double b = distance4PointF(
							getSelectedChildImage().mPreMovePointF,
							getSelectedChildImage().mCurMovePointF);
					double c = distance4PointF(
							getSelectedChildImage().mCenterPoint,
							getSelectedChildImage().mCurMovePointF);

					double cosb = (a * a + c * c - b * b) / (2 * a * c);

					if (cosb >= 1) {
						cosb = 1f;
					}

					double radian = Math.acos(cosb);
					float newDegree = (float) radianToDegree(radian);

					// center -> proMove的向量， 我们使用PointF来实现
					PointF centerToProMove = new PointF(
							(getSelectedChildImage().mPreMovePointF.x - getSelectedChildImage().mCenterPoint.x),
							(getSelectedChildImage().mPreMovePointF.y - getSelectedChildImage().mCenterPoint.y));

					// center -> curMove 的向量
					PointF centerToCurMove = new PointF(
							(getSelectedChildImage().mCurMovePointF.x - getSelectedChildImage().mCenterPoint.x),
							(getSelectedChildImage().mCurMovePointF.y - getSelectedChildImage().mCenterPoint.y));

					// 向量叉乘结果,如果结果为负数， 表示为逆时针， 结果为正数表示顺时针
					float result = centerToProMove.x * centerToCurMove.y
							- centerToProMove.y * centerToCurMove.x;

					if (result < 0) {
						newDegree = -newDegree;
					}

					getSelectedChildImage().angle += newDegree;
					getSelectedChildImage().scale = scale;
				//	 getSelectedChildImage().adjustlayout();
					getSelectedChildImage().transformDraw();
					getSelectedChildImage().mPreMovePointF.set(getSelectedChildImage().mCurMovePointF);
					invalidate();

				}
				
				
				if (inRotation) {// 能旋转

					float nowAngle = rotation(event) - startRotation;
					// nowAngle=nowAngle % 360;//
					/** 旋转角 */
					onRotation(nowAngle - mPreAngle);
					mPreAngle = nowAngle % 360;
					try {

						if (null != getSelectedChildImage()) {

							L.i(TAG, "进行旋转,角度 nowAngle="+ getSelectedChildImage().angle);// 角度

						}

					} catch (Exception e) {

					}
				}

				break;

			case MotionEvent.ACTION_UP:// 第一个手指抬起
			case MotionEvent.ACTION_POINTER_UP:// 第二个手指抬起
			case MotionEvent.ACTION_CANCEL://

				inRotation = false;
				suoInRotation = false; // 锁定状态
				startRotation = 0;
				mPreAngle = 0;
                 la=false;
				// /L.i(TAG, "触摸事件停止.");

				// getSelectedChildImage().la=false;

				break;
			}

			// setLongClickable(locked);

			// L.i(TAG, "设置长按事件为locked:" + locked);

			boolean handled = false;

			if (flag) {

				// L.i(TAG, "判断需要拖动flag=true");
				if (null != mGestureDetector
						&& mGestureDetector.onTouchEvent(event)) {

					// L.i("TAG","mGestureDetector不为空,且有ontouchevent事件"+ (null
					// != mGestureDetector &&
					// mGestureDetector.onTouchEvent(event)));

					handled = true;
				}
				if (null != mScaleDragDetector
						&& mScaleDragDetector.onTouchEvent(event)) {
					handled = true;
				}
			}
			return handled;

		} else if (action == ACTION_DRAWLINE) {// 用来划线
			// L.i(TAG, "判断画线事件发生");
			if (event.getAction() == MotionEvent.ACTION_MOVE) { // 拖动屏幕

				// L.i(TAG, "判断拖动屏幕发生");
				canvas.drawLine(x, y, event.getX(), event.getY(), paint); // 画线，x，y是上次的坐标，event.getX(),
				// L.i(TAG,"拖动屏幕,上次坐标:(" + x + "," + y + ")....点击坐标:(" +
				// event.getX() + "," + event.getY() + ")");

				invalidate();

				// L.i(TAG, "画线之后更新视图");
			}

			if (event.getAction() == MotionEvent.ACTION_DOWN) { // 按下屏幕

				x = event.getX();
				y = event.getY();

				canvas.drawPoint(x, y, paint);// 画点

				// L.i(TAG, "按下屏幕事件发生,画点,坐标(" + x + "," + y + ")");
				invalidate();
				// L.i(TAG, "画点之后更新视图");
			}
			if (event.getAction() == MotionEvent.ACTION_UP) { // 松开屏幕
				// L.i(TAG, "松开屏幕");
			}
			x = event.getX(); // 记录坐标
			y = event.getY();
			// L.i(TAG, "记录最后坐标位置,(" + x + "," + y + ")");
			return true;
		}
		// L.i(TAG, "触摸事件结束。。");
		return super.onTouchEvent(event);
	}// TouchEvent事件结束

	private boolean flag = true;// 判断是否需要拖动操作

	public void setActionDrawline(boolean bool, int num, boolean mark) {
		// L.i(TAG, "根据传入参数进行判定是画线还是正常状态，红蓝绿灰黑白012345：颜色为：" + num + "参数bool为:"+
		// bool);
		flag = mark;
		action = bool ? ACTION_DRAWLINE : ACTION_NORMAL;

		switch (num) {
		case 0:
			// mSoundPool.play(soundMap.get(1), 1, 1, 0, 0, 1);//播放音效
			paint.setColor(Color.RED);
			paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC)); // 显示上层绘制图片
			paint.setStrokeWidth(5); // 设置线的宽度
			// L.i(TAG, "画笔颜色红色。。");
			break;
		case 1:
			// mSoundPool.play(soundMap.get(1), 1, 1, 0, 0, 1);//播放音效
			paint.setColor(Color.BLUE);
			paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC)); // 显示上层绘制图片
			paint.setStrokeWidth(5); // 设置线的宽度
			// L.i(TAG, "画笔颜色蓝色。。");
			break;
		case 2:
			paint.setColor(Color.GREEN);
			paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC)); // 显示上层绘制图片
			paint.setStrokeWidth(5); // 设置线的宽度
			// L.i(TAG, "画笔颜色绿色。。");
			break;
		case 3:
			paint.setColor(Color.MAGENTA);
			paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC)); // 显示上层绘制图片
			paint.setStrokeWidth(5); // 设置线的宽度
			// L.i(TAG, "画笔颜色灰色。。");
			break;
		case 4:
			paint.setColor(Color.BLACK);
			paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC)); // 显示上层绘制图片
			paint.setStrokeWidth(5); // 设置线的宽度
			// L.i(TAG, "画笔颜色黑色。。");
			break;
		case 5:
			paint.setColor(Color.WHITE);
			paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC)); // 显示上层绘制图片
			paint.setStrokeWidth(5); // 设置线的宽度
			// L.i(TAG, "画笔颜色白色。。");
			break;
		case -1:
			paint.setAlpha(0);
			paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN)); // 设置画笔的痕迹是透明的，从而可以看到背景图片
			paint.setStrokeWidth(88); // 设置线的宽度
			// L.i(TAG, "默认,画笔痕迹透明,只显示背景图片。。");
			break;
		default:
			break;
		}
	}

	// 取旋转角,以弧度
	private float rotation(MotionEvent event) {

		double delta_x = (event.getX(0) - event.getX(1));
		double delta_y = (event.getY(0) - event.getY(1));

		/*
		 * L.i(TAG, "按下屏幕事件发生,画点,坐标(" + x + "," + y + ")"); L.i(TAG,
		 * "rotation中取旋转角原始坐标get0(x0,y0)值:(" + event.getX(0) + "," +
		 * event.getY(0) + ")"); L.i(TAG,
		 * "rotation中取旋转角坐标        get1(x1,y1)值:(" + event.getX(1) + "," +
		 * event.getY(1) + ")"); L.i(TAG,
		 * "rotation中取旋转角坐标差delta_(x0-x1,y0-y1)值:(" + (event.getX(0) -
		 * event.getX(1)) + "," + (event.getY(0) - event.getY(1)) + ")");
		 * L.i(TAG, "(dalta_x,delta_y)的坐标:(" + delta_x + "," + delta_y + ")");
		 */

		/****** 弧度 */
		double radians = Math.atan2(delta_y, delta_x);

		/*
		 * L.i(TAG, "然后进行Math.atan2(delta_y, delta_x),值:(" + radians + ")");
		 * L.i(TAG, "rotation方法中最后返回 (float) Math.toDegrees(radians)值:(" +
		 * (float) Math.toDegrees(radians) + ")");
		 */
		return (float) Math.toDegrees(radians);// 转换为角度
	}

	/** 旋转 */
	void onRotation(float rotation) {
		L.i(TAG, "旋转=========>onRotation参数:(" + rotation + ")");
		for (ImageWrapper child : childList) {
			if (child.isChecked()) {// 有选中项,调用子类的旋转
				child.onRotation(rotation);
				// L.i(TAG,"增强循环遍历ImageWrapper数组中的子类调用该onRotation方法,判断依据child.isChecked():("+
				// child.isChecked() + ")");
			}
		}
		invalidate();
		// L.i(TAG, "348旋转结束更新视图");
	}

	/** 拖拽 */
	@Override
	public void onDrag(float dx, float dy) {
		if (la) {

			L.i("拖拽ondrag()", "拖拽onDraw()");
			return;
			/*
			 * float x1,x2,y1,y2;
			 * x1=distance4PointF(getSelectedChildImage().mLBPoint
			 * ,getSelectedChildImage().mRBPoint);//最开始两点距离
			 * y1=distance4PointF(getSelectedChildImage
			 * ().mRTPoint,getSelectedChildImage().mRBPoint);//最开始两点距离
			 * 
			 * x2=distance4PointF(getSelectedChildImage().mLBPoint,
			 * getSelectedChildImage().mRBPoint);
			 * y2=distance4PointF(getSelectedChildImage
			 * ().mRTPoint,getSelectedChildImage().mRBPoint);
			 * 
			 * getSelectedChildImage().mRBPoint.x+= dx;
			 * getSelectedChildImage().mRBPoint.y+= dy;
			 * 
			 * getSelectedChildImage().mRTPoint.x+= dx;
			 * getSelectedChildImage().mRTPoint.y+= dy;
			 * 
			 * getSelectedChildImage().mLBPoint.x+= dx;
			 * getSelectedChildImage().mLBPoint.y+= dy; //判断x y轴各移动了多少,右上跟左下
			 * 均跟随右下 平移 getSelectedChildImage().scale = x2/x1;
			 * getSelectedChildImage().scale_y= y2/y1;
			 * getSelectedChildImage().invalidate();
			 */

		}

		// L.i(TAG, "onDrag()拖拽判断inRotation是否为真,拖动坐标: dx:" + dx + ",dy:" + dy);

		else if (!inRotation && !la) {// 不能旋转
			// L.i(TAG, "onDrag方法,(!inRotation)为真");
			for (ImageWrapper child : childList) {
				if (child.isChecked() ) {// 找到选中子对象,如果有进行拖拽
																// 拖拽锁 有所定义对象
																// ,没有锁
					child.onDrag(dx, dy);// 拖动木

					// child.updataXy();//更新坐标
					// L.i(TAG, "onDrag()中的参数:(" + dx + "," + dy + ")");
					// L.i(TAG,"增强循环遍历ImageWrapper数组中的子类调用该onDrag方法,判断依据child.isChecked():("+
					// child.isChecked() + ")");

				}
			}

			invalidate();
			L.i(TAG, "onDrag()拖拽,中更新视图");
		}
	}

	/** 滑动的最后手指抬起时触发,四个参数 开始的x坐标,开始的y坐标,每秒x轴方向移动的像素,每秒y轴方向移动的像素 */
	@Override
	public void onFling(float startX, float startY, float velocityX,
			float velocityY) {

		L.i(TAG, "onFling()飞");

		/*
		 * L.i(TAG,
		 * "onFling中,(float startX, float startY, float velocityX,float velocityY):"
		 * + "(" + startX + "," + start + "," + velocityX + "," + velocityY +
		 * ")");
		 */
	}

	/** 测量焦点坐标,进行缩放 */
	@Override
	public void onScale(float scaleFactor, float focusX, float focusY) {
		// L.i(TAG, "onScale()测量子对象坐标,中的参数:(" + scaleFactor + "," + focusX +
		// ","+ focusY + ")");

		for (ImageWrapper child : childList) {

			if (child.isChecked()) {// 找到选中子对象,进行缩放 缩放的判断
				child.onScale(scaleFactor, focusX, focusY);
				// L.i(TAG, "onScale()保持沉默:(" + scaleFactor + "," + focusX +
				// ","+ focusY + ")");

			}

		}

		mGestureDetector.setIsLongpressEnabled(true);// 设置可以进行长按事件

		invalidate();
		L.i(TAG, "onScale()缩放");
	}

	@Override
	public boolean onDoubleTap(MotionEvent e) {

		return false;

	}

	@Override
	public boolean onDoubleTapEvent(MotionEvent e) {
		return false;
	}

	/** 单击,找到数组大小,并把选中子对象,提到最顶层,判断选中对象是根据最顶层坐标 */
	@Override
	public boolean onSingleTapConfirmed(MotionEvent e) {
		L.i("单击事件", "单击事件中");
		Log.e("520it", "单击事件A");

		if (la) {

			la = false;
			//ls = false;
			L.i("单击事件", "单击事件中ls=true");
			Log.e("520it", "单击事件B");
			return true;

		} else {

			L.i("单击事件", "单击事件中ls=false,需要进行判断");
			Log.e("520it", "单击事件C");
			try {
				// 点中右上角区域
				if (e.getX() > getSelectedChildImage().mRTPoint.x- bitmap.getWidth() / 2
						&& e.getX() < getSelectedChildImage().mRTPoint.x+ bitmap.getWidth() / 2) {

					if (e.getY() > getSelectedChildImage().mRTPoint.y- bitmap.getHeight()
							&& e.getY() < getSelectedChildImage().mRTPoint.y+ bitmap.getHeight()) {

						// L.i("点中右上角", "坐标,x:" + xx + ",yy:" + yy);
						// la = true;
						// ls = true;
						// Toast.makeText(newSheJi,
						// "点中右上角",Toast.LENGTH_SHORT).show();

						this.left(this.getSelectedChildImage()); // 反转
						this.invalidate();
						return true;

					}

				}
			} catch (Exception e1) {
				e1.printStackTrace();
			} catch (Error e2) {
				e2.printStackTrace();
			}

			
			int handleIndex = -1;
			ImageWrapper imageWrapper = getSelectedChildImage();// 获取选中的图像

			L.i(TAG, "handleIndex初始化为:" + handleIndex);

			if (imageWrapper != null) {// 进行判断点击位置 是否在四个点中

			}

			//
			for (int i = childList.size() - 1; i >= 0; i--) {

				if (childList.get(i).onTap(e.getX(), e.getY())) {// 循环判断该列表中所有子对象是否在顶层(当前的屏幕坐标为参数)

					handleIndex = i; // 获取最顶层下标
					break;
				}
			}

			for (int i = childList.size() - 1; i >= 0; i--) {

				childList.get(i).setChecked(i == handleIndex); // 设置选中项,

			}

			if (imageWrapper != null) {// 如果有选中位图,
			//	locked = imageWrapper.isLocked();// 获取锁定状态
			//	newSheJi.changeSuo(locked);// 改变对应图标显示
				// longpressdialog =!locked;
				// mGestureDetector.setLongClickable(locked);//设置对应的长按事件
				mGestureDetector.setIsLongpressEnabled(!locked); // 设置长按 锁
				Log.e("520it", "单击事件D");
				mIPicsGestureListener.onPicsGestureScreenChanged(true);//如果有选中位图,单击触发
			}else{
				Log.e("520it", "单击事件E");
				mIPicsGestureListener.onPicsGestureScreenChanged(false);//如果没选中位图,单击触发
			}

			invalidate();

			return handleIndex != -1;// 设置好之后,返回true

		}
	}

	/** 列表中所有选项设置为false,然后刷新view视图 */
	public void clearSelectedImage() {
		// L.i(TAG,"clearSelectedImage()中,执行清空选中image图片(所有子img对象设置setchecked为false)");
		for (ImageWrapper child : childList) {

			child.setChecked(false);
			// L.i(TAG,"clearSelectedImage()中的增强循环遍历childList数组类型ImageWrapper,设置所有项检查为false");

		}
		invalidate();
		L.i(TAG, "清空选中项");
	}

	public void addChildImage(ImageWrapper imageWrapper) {
		// L.i(TAG,"addChildImage(ImageWrapper)中依次进行添加子项,参数类型ImageWrapper,该数组类长度:"+
		// childList.size() + ",该素材id:"
		// + imageWrapper.getImgid() + ",父类id:"
		// + imageWrapper.getMid()
		// );
		this.childList.add(imageWrapper);
		mGestureDetector.setIsLongpressEnabled(true);// 刚添加的设计图片设置长按事件为true

		invalidate();
		L.i(TAG, "添加子view");
	}

	/** 上移实现 */
	public boolean upSelectedImageLayout() {

		int index = getSelectedIndex();
		// L.i(TAG, "上移,获取选择的image页码:" + index);

		if (index != -1 && index < this.childList.size() - 1) {

			// L.i(TAG, "如果有选项,并且选项 < 该对象数组列表大小-1");
			// L.i(TAG, "选项为index:" + index + ",该对象数组大小:" +
			// this.childList.size());
			ImageWrapper imageWrapper = this.childList.remove(index);
			// L.i(TAG, "移除当前对象childList列表中指定的项:" + index + "该imagewrapper对象为:"+
			// imageWrapper);

			this.childList.add(index + 1, imageWrapper);
			// L.i(TAG, "下移,在该位置后面(上面)重新添加imageWrapper对象" + index + 1);

			invalidate();//
			L.i(TAG, "上移成功");
			return true;
		} else {
			L.i(TAG, "上移失败");
			return false;
		}
	}

	/** 下移实现,获取选中项页码 ,有且不在最下面,移除当前对象childList列表对应的选中项 */
	public boolean downSelectedImageLayout() {
		int index = getSelectedIndex();
		L.i(TAG, "下移");
		if (index != -1 && index > 0) {
			ImageWrapper imageWrapper = this.childList.remove(index);
			// L.i(TAG, "移除当前对象childList列表中指定的项:" + index +
			// ",该imagewrapper对象为:"+ imageWrapper);
			this.childList.add(index - 1, imageWrapper);
			// L.i(TAG, "下移,childList中在当前项之前(下面)重新添加该imageWrapper对象");
			invalidate();
			// L.i(TAG, "下移刷新视图,显示效果");
			return true;
		} else {
			return false;
		}
	}

	/** 左右旋转的调用 */
	public void left(ImageWrapper imagewrapper) {
		// imagewrapper.Leftto(0, imagewrapper);
		invalidate();// 刷新视图
		L.i(TAG, "执行了左旋转");
	}

	/** 右旋转的调用 */
	public void right(ImageWrapper imagewrapper) {
		// imagewrapper.Leftto(1, imagewrapper);
		invalidate();
		L.i(TAG, "执行了右旋转");

	}

	/** 设置选择的页码 */
	private int setSelectedIndex(int index) {
		int selectedIndex = -1;// 默认选择页码为-
		for (int i = childList.size() - 1; i >= 0; i--) {
			// L.i(TAG, "递减遍历素材中的数组获取选择的页码,数组大小:" + childList.size() + ",这是第" +
			// i+ "项");
			if (i == index) {
				selectedIndex = i;
				childList.get(i).setChecked(true);
				// L.i(TAG, "返回最后选择的页码selectedInedx:" + selectedIndex);
				// break;
			} else {
				childList.get(i).setChecked(false);

			}
		}
		// L.i(TAG, "设置选中项:" + selectedIndex);
		return selectedIndex;
	}

	/** 获取选择的页码 */
	private int getSelectedIndex() {
		int selectedIndex = -1;// 默认选择页码为-
		for (int i = childList.size() - 1; i >= 0; i--) {
			// L.i(TAG, "递减遍历素材中的数组获取选择的页码,数组大小:" + childList.size() + ",这是第" +
			// i+ "项");
			if (childList.get(i).isChecked()) {
				selectedIndex = i;
				// L.i(TAG, "返回最后选择的页码selectedInedx:" + selectedIndex);
				break;
			}
		}
		// L.i(TAG, "获取选中项:" + selectedIndex);
		return selectedIndex;
	}

	/** 清空选中的封装imagewrapper子类 */
	public void clearChildImage() {
		this.childList.clear();
		invalidate();
		L.i(TAG, "清空所有子view");
	}

	/** 获取选择的列表中封装的子imagewrapper对象 */
	public ImageWrapper getSelectedChildImage() {
		// 获取选择的子image,赋页码给变量
		int index = getSelectedIndex();

		// L.i(TAG,"获取选中的childList数组中的具体imageWrapper对象,首先获取选中页,如果不等于-1(默认值)通过childList.get(index)获取当前选中imagewrapper对象"+
		// ",这是第" + index + "页");

		if (index != -1) {// 如果有
			// L.i(TAG, "获取选中项:" + index);
			return childList.get(index);
		}
		// L.i(TAG, "没有选中项");
		return null;
	}

	/** 删除所选封装素材(类型ImageWrapper),实际方法是调用对象的remove方法 移除出对象的childList并更新视图 */
	public void deleteChildImage(ImageWrapper target) {
		this.childList.remove(target);
		setSelectedIndex(this.childList.size() - 1);// 删除当前对象之后,选择为下一个

		// L.i(TAG, "删除选中项,并设置选中项为数组最后一个");
		invalidate();

	}

	/** 获取当前对象的封装imagewrapper数组 */
	public List<ImageWrapper> getChildImageList() {
		// L.i(TAG, "获取子view数组大小:" + this.childList.size());
		return this.childList;
	}

	/****** 设置是否支持长按事件 */
	public boolean longPress(boolean b) {
		if (b) {
			mGestureDetector.setIsLongpressEnabled(b);
			return b;
		} else
			mGestureDetector.setIsLongpressEnabled(b);
		return b;
	}
	
	/**
	 * 两个点之间的距离
	 * 
	 * @return
	 */
	private float distance4PointF(PointF p1, PointF p2) {

		float disX = p2.x - p1.x;
		float disY = p2.y - p1.y;
		return (float) Math.sqrt(disX * disX + disY * disY);
	}

	/**
	 * 弧度换算成角度
	 * 
	 * @return
	 */
	public static double radianToDegree(double radian) {
		return radian * 180 / Math.PI;
	}

	/**
	 * 角度换算成弧度
	 * 
	 * @param degree
	 * @return
	 */
	public static double degreeToRadian(double degree) {
		return degree * Math.PI / 180;
	}
	

}
