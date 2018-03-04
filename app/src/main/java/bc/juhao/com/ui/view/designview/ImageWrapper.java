package bc.juhao.com.ui.view.designview;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Log;

import bc.juhao.com.ui.activity.IssueApplication;

public class ImageWrapper {
	private static final String tag = "imageWrapper中";
	public static Paint paint = new Paint(1);
	protected float angle = 0.0F;
	private RectF borderRect;
	protected float displayXSize;
	protected float displayYSize;
	private boolean isChecked;



	protected Bitmap mDrawBitmap = null;
	private Matrix matrix = new Matrix();
	public int mLightCount; // 该灯的编号


	protected float scale = 0.25F;
	// protected float x;
	// protected float y;

	/**** 中心点 ***/
	public PointF mCenterPoint;
	/******** 左上 */
	public PointF mLTPoint;
	/******** 右上 */
	public PointF mRTPoint;
	/******** 左下 */
	public PointF mLBPoint;
	/******** 右下 */
	public PointF mRBPoint;



	
	/****** 单指时,移动前的坐标 */
	public PointF mPreMovePointF = new PointF();

	/**** 移动后的坐标 */
	public PointF mCurMovePointF = new PointF();

	public void setScale(float scale) {
		this.scale = scale;
	}

	public Bitmap getDrawBitmap() {
		return mDrawBitmap;
	}

	public void setmLightCount(int mLightCount) {
		this.mLightCount = mLightCount;
	}

	public ImageWrapper(float x, float y, Bitmap bitmap)// 中心坐标和位图
	{
		mCenterPoint = new PointF(0, 0);// 几个点初始化
		mLTPoint = new PointF(0, 0);
		mRTPoint = new PointF(0, 0);
		mLBPoint = new PointF(0, 0);
		mRBPoint = new PointF(0, 0);
		paint.setStrokeWidth(5.0F);
		paint.setColor(-1);
		paint.setStyle(Paint.Style.STROKE);
		this.borderRect = new RectF();
		this.mCenterPoint.x = x;
		this.mCenterPoint.y = y;
		this.displayXSize = bitmap.getWidth();
		this.displayYSize = bitmap.getHeight();
		this.mDrawBitmap = bitmap;
		this.borderRect.top = 0.0F;
		this.borderRect.left = 0.0F;
		this.borderRect.right = (this.mDrawBitmap.getWidth());
		this.borderRect.bottom = (this.mDrawBitmap.getHeight());
		Log.i(tag, "初始化时候x=" + this.mCenterPoint.x + ",y=" + this.mCenterPoint.y);

	}

	private Matrix getDrawMatrix() {
		
		this.matrix.reset();
		this.matrix.setTranslate(mCenterPoint.x - displayXSize / 2,mCenterPoint.y - displayYSize / 2);
		this.matrix.postScale(scale, scale,mCenterPoint.x,mCenterPoint.y);
		this.matrix.postRotate(angle,mCenterPoint.x,mCenterPoint.y);
		return this.matrix;

	}

	public ImageWrapper copyToNew() {
		
		ImageWrapper localImageWrapper = new ImageWrapper(mCenterPoint.x,mCenterPoint.y, mDrawBitmap);
		localImageWrapper.angle =angle;
		localImageWrapper.scale =scale;
		
		return localImageWrapper;
	}

	public boolean isChecked() {
		return this.isChecked;
	}

	/********* 拖动, */
	public void onDrag(float x, float y) {

		this.mCenterPoint.x += x;
		this.mCenterPoint.y += y;

	}

	public void onDraw(Canvas canvas) {
		canvas.save();

		if ((this.mDrawBitmap != null) && (!this.mDrawBitmap.isRecycled())) {
			canvas.concat(getDrawMatrix());// 整个画布矩阵

			if (this.isChecked) {
				canvas.drawRect(this.borderRect, paint);
			}

			canvas.drawBitmap(this.mDrawBitmap, 0.0F, 0.0F, paint);
		}

		canvas.restore();
		
		adjustlayout();//根据中心点坐标,缩放比例,角度,调整其他点坐标
	}

	public void onRotation(float degree) {
		this.angle += degree%360;
		//this.angle %=360;
	}

	public void onScale(float scale, float paramFloat2, float paramFloat3) {
		this.scale *= scale;
	}

	public boolean onTap(float x, float y) {
		if (testPosition(x, y)) {
			this.isChecked = true;
			IssueApplication.mLightIndex=mLightCount;
			return true;
		}
		this.isChecked = false;
		return false;
	}

	public void setBitmap(Bitmap mdrawbitmap) {
		this.mDrawBitmap = mdrawbitmap;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}

	/*******判断触摸点是否在区域内*/
	public boolean testPosition(float tx, float ty) {
		boolean is = false;
		float halfx = this.displayXSize * this.scale / 2;
		if (tx <= this.mCenterPoint.x + halfx&& tx >= this.mCenterPoint.x - halfx){// 在中心x轴的左右区域内
			float halfy = this.displayYSize * this.scale / 2;
			if (ty <= this.mCenterPoint.y + halfy&& ty >= this.mCenterPoint.y - halfy){//在中心y轴的上下区域内
				is = true; // 在该区域内
			}
		}
		
		return is;
	}

	/**
	 * 调整View的大小，确定各个顶点未旋转的坐标
	 * 根据缩放,角度,中心点
	 */
	public void adjustlayout() {
		try {
			
			if (this.mDrawBitmap != null){
				//左上角(未)
				mLTPoint.x=mCenterPoint.x-this.displayXSize * this.scale / 2 ;
				mLTPoint.y=mCenterPoint.y-this.displayYSize * this.scale / 2 ;
				//右上角(未)
				mRTPoint.x=mCenterPoint.x+this.displayXSize * this.scale / 2 ;
				mRTPoint.y=mCenterPoint.y-this.displayYSize * this.scale / 2 ;
				//左下角
				mLBPoint.x=mCenterPoint.x-this.displayXSize * this.scale / 2 ;
				mLBPoint.y=mCenterPoint.y+this.displayYSize * this.scale / 2 ;
				//右下角
				mRBPoint.x=mCenterPoint.x+this.displayXSize * this.scale / 2 ;
				mRBPoint.y=mCenterPoint.y+this.displayYSize * this.scale / 2 ;
				
				
				if(this.angle!=0){
					
					this.mLTPoint = obtainRoationPoint(this.mCenterPoint,this.mLTPoint,this.angle);
					this.mRTPoint = obtainRoationPoint(this.mCenterPoint,this.mRTPoint,this.angle);
					this.mLBPoint = obtainRoationPoint(this.mCenterPoint,this.mLBPoint,this.angle);
					this.mRBPoint = obtainRoationPoint(this.mCenterPoint,this.mRBPoint,this.angle);
				}
				
				
				
		   }
		} catch (Exception e) {

			e.printStackTrace();

		}

	}

	
	
	/**
	 * 两个点之间的距离
	 * @return
	 */
	private float distance4PointF(PointF p1, PointF p2) {
		
		float disX = p2.x - p1.x;
		float disY = p2.y - p1.y;
		return (float) Math.sqrt(disX * disX + disY * disY);
	}
	
	/**
	 * 获取旋转某个角度之后的点
	 * @param source
	 * @param degree
	 * @return
	 */
	public static PointF obtainRoationPoint(PointF center, PointF source, float degree) {
		//两者之间的距离
		PointF disPoint = new PointF(0,0);
		disPoint.x = source.x - center.x;
		disPoint.y = source.y - center.y;
		
		//没旋转之前的弧度
		double originRadian = 0;

		//没旋转之前的角度
		double originDegree = 0;
		
		//旋转之后的角度
		double resultDegree = 0;
		
		//旋转之后的弧度
		double resultRadian = 0;
		
		//经过旋转之后点的坐标
		PointF resultPoint = new PointF();
		
		double distance = Math.sqrt(disPoint.x * disPoint.x + disPoint.y * disPoint.y);
		if (disPoint.x == 0 && disPoint.y == 0) {
			return center;
			// 第一象限
		} else if (disPoint.x >= 0 && disPoint.y >= 0) {
			// 计算与x正方向的夹角
			originRadian = Math.asin(disPoint.y / distance);
			
			// 第二象限
		} else if (disPoint.x < 0 && disPoint.y >= 0) {
			// 计算与x正方向的夹角
			originRadian = Math.asin(Math.abs(disPoint.x) / distance);
			originRadian = originRadian + Math.PI / 2;
			
			// 第三象限
		} else if (disPoint.x < 0 && disPoint.y < 0) {
			// 计算与x正方向的夹角
			originRadian = Math.asin(Math.abs(disPoint.y) / distance);
			originRadian = originRadian + Math.PI;
		} else if (disPoint.x >= 0 && disPoint.y < 0) {
			// 计算与x正方向的夹角
			originRadian = Math.asin(disPoint.x / distance);
			originRadian = originRadian + Math.PI * 3 / 2;
		}
		
		// 弧度换算成角度
		originDegree = radianToDegree(originRadian);
		resultDegree = originDegree + degree;
		
		// 角度转弧度
		resultRadian = degreeToRadian(resultDegree);
		
		resultPoint.x = (int) Math.round(distance * Math.cos(resultRadian));
		resultPoint.y = (int) Math.round(distance * Math.sin(resultRadian));
		resultPoint.x += center.x;
		resultPoint.y += center.y;

		return resultPoint;
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
	 * @param degree
	 * @return
	 */
	public static double degreeToRadian(double degree) {
		return degree * Math.PI / 180;
	}
	
	/**
	 * 设置Matrix, 强制刷新,displayxsize跟y一样
	 */
	public void transformDraw() {

		// computeRect(-framePadding, -framePadding, bitmapWidth + framePadding,
		// bitmapHeight + framePadding, mDegree);
		// this.displayXSize = this.mDrawBitmap.getWidth() * this.scale;
		// 左上角 缩放
		mLTPoint.x = mCenterPoint.x - this.displayXSize * this.scale / 2;
		mLTPoint.y = mCenterPoint.y - this.displayYSize * this.scale / 2;

		// 右上角(未)
		mRTPoint.x = mCenterPoint.x + this.displayXSize * this.scale / 2;
		mRTPoint.y = mCenterPoint.y - this.displayYSize * this.scale / 2;
		// 左下角
		mLBPoint.x = mCenterPoint.x - this.displayXSize * this.scale / 2;
		mLBPoint.y = mCenterPoint.y + this.displayYSize * this.scale / 2;
		// 右下角
		mRBPoint.x = mCenterPoint.x + this.displayXSize * this.scale / 2;
		mRBPoint.y = mCenterPoint.y + this.displayYSize * this.scale / 2;

		if (this.angle != 0) {

			this.mLTPoint = obtainRoationPoint(this.mCenterPoint,
					this.mLTPoint, this.angle);
			this.mRTPoint = obtainRoationPoint(this.mCenterPoint,
					this.mRTPoint, this.angle);
			this.mLBPoint = obtainRoationPoint(this.mCenterPoint,
					this.mLBPoint, this.angle);
			this.mRBPoint = obtainRoationPoint(this.mCenterPoint,
					this.mRBPoint, this.angle);

		}

		// 设置缩放比例
		matrix.setScale(scale, scale);
		// 绕着图片中心进行旋转
		matrix.postRotate(angle % 360, mCenterPoint.x, mCenterPoint.y);
		// 设置画该图片的起始点
		matrix.postTranslate(mCenterPoint.x, mCenterPoint.y);

		//invalidate();
	}
	
	
}