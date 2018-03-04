package bc.juhao.com.ui.activity.programme.camera;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.os.Build;
import android.util.Log;
import android.util.SparseArray;
import android.view.OrientationEventListener;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import bc.juhao.com.R;
import bc.juhao.com.bean.GoodsShape;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.controller.programme.TestCameraController;
import bc.juhao.com.ui.activity.programme.DiyActivity;
import bc.juhao.com.ui.view.TouchView;
import bocang.json.JSONObject;
import bocang.view.BaseActivity;

public class TestCameraActivity extends BaseActivity
        implements OnClickListener,
        SurfaceHolder.Callback {
    private static final String TAG = "TestCameraActivity";
    public static final String KEY_FILENAME = "filename";
    private Button mTakePhoto;
    private SurfaceView mSurfaceView;
    private Camera mCamera;
    private String mFileName;
    private OrientationEventListener mOrEventListener; // 设备方向监听器
    private Boolean mCurrentOrientation; // 当前设备方向 横屏false,竖屏true

    ImageView mThumbnail, mSelectProduct;
    Button mButton;
    public SparseArray<JSONObject> mSelectedLightSA = new SparseArray<>();
    public JSONObject mGoodsObject;
    public List<JSONObject> mGoodsList;
    public String mPath;
    public TestCameraController mController;
    private  SurfaceHolder holder;


    /* 图像数据处理还未完成时的回调函数 */
    private Camera.ShutterCallback mShutter = new Camera.ShutterCallback() {
        @Override
        public void onShutter() {
            // 一般显示进度条
        }
    };

    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {
        mController = new TestCameraController(this);
    }

    @Override
    protected void initView() {
        //去除title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去掉Activity上面的状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_test_camera);



        mTakePhoto = (Button) findViewById(R.id.take_photo);
        mTakePhoto.setOnClickListener(this); // 拍照按钮监听器

        startOrientationChangeListener(); // 启动设备方向监听器
        mSurfaceView = (SurfaceView) findViewById(R.id.my_surfaceView);
        holder= mSurfaceView.getHolder();
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        holder.addCallback(this); // 回调接口

        mButton = (Button) findViewById(R.id.btn_takepic);
        mThumbnail = (ImageView) findViewById(R.id.iv_Thumbnail);
        mSelectProduct = (ImageView) findViewById(R.id.iv_select_product);
        mThumbnail.setOnClickListener(this);
        mButton.setOnClickListener(this);
        mSelectProduct.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        //		mGoodsObject = ((CameraActivity) this).mGoodsObject;
        //		mGoodsList=new ArrayList<>();
        //		mGoodsList.add(mGoodsObject);
        Intent intent = getIntent();
        mPath = intent.getStringExtra(Constance.photo);
        //		mProperty=intent.getStringExtra(Constance.property);
        mGoodsObject = (JSONObject) intent.getSerializableExtra(Constance.product);
        mGoodsList = new ArrayList<>();
        mGoodsList.add(mGoodsObject);

        //		if(AppUtils.isEmpty(mGoodsObject)) return;
        //		mProductId=mGoodsObject.getString(Constance.id);
    }

    /* 图像数据处理完成后的回调函数 */
    private Camera.PictureCallback mJpeg = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            // 保存图片
            mFileName = UUID.randomUUID().toString() + ".jpg";
            Log.i(TAG, mFileName);
            FileOutputStream out = null;


//            File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
//                    .getAbsoluteFile();
//            if (!dir.exists()) {
//                dir.mkdirs();
//            }
//            final File file = new File(dir, System.currentTimeMillis() + ".jpg");

            try {
                out = openFileOutput(mFileName, Context.MODE_PRIVATE);
//                out = new FileOutputStream(file);
                byte[] newData = null;
                if (mCurrentOrientation) {
                    // 竖屏时，旋转图片再保存
                    Bitmap oldBitmap = BitmapFactory.decodeByteArray(data, 0,
                            data.length);
                    Matrix matrix = new Matrix();
                    matrix.setRotate(0);
                    Bitmap newBitmap = Bitmap.createBitmap(oldBitmap, 0, 0,
                            oldBitmap.getWidth(), oldBitmap.getHeight(),
                            matrix, true);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    newBitmap.compress(Bitmap.CompressFormat.JPEG, 85, baos);
                    newData = baos.toByteArray();
                    out.write(newData);
                } else {
                    out.write(data);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (out != null)
                        out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


//            String bmPath = "file://" + file.getAbsolutePath();
            String bmPath = "file://" + getFileStreamPath(mFileName).getAbsolutePath();;
            Intent intent = new Intent(TestCameraActivity.this, DiyActivity.class);
            intent.putExtra(Constance.img_path, bmPath);
            intent.putExtra(Constance.FROMPHOTO, true);
            intent.putExtra(Constance.productlist
                    , (Serializable) mGoodsList);
            List<GoodsShape> goodsShapeList = new ArrayList<>();
            for (int i = 0; i < mController.mFrameLayout.getChildCount(); i++) {
                GoodsShape shape = new GoodsShape();
                TouchView view = (TouchView) ((FrameLayout) mController.mFrameLayout.getChildAt(i)).getChildAt(0);
                shape.setHeight(view.getHeight());
                shape.setWidth(view.getWidth());
                shape.setX((int) view.getX());
                shape.setY((int) view.getY());
                shape.setRotate(view.getRotation());
                goodsShapeList.add(shape);
            }

            intent.putExtra(Constance.productshape, (Serializable) goodsShapeList);
            startActivity(intent);
            finish();

            //			MyToast.show(TestCameraActivity.this,"12312321");
            //			Intent i = new Intent(TestCameraActivity.this, ShowPicture.class);
            //			i.putExtra(KEY_FILENAME, mFileName);
            //			startActivity(i);
            //			finish();
        }
    };
    //
    //	@SuppressWarnings("deprecation")
    //	@Override
    //	protected void onCreate(Bundle savedInstanceState) {
    //		super.onCreate(savedInstanceState);
    //
    //
    //	}


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mController.ActivityResult(requestCode, resultCode, data);
    }

    private final void startOrientationChangeListener() {
        mOrEventListener = new OrientationEventListener(this) {
            @Override
            public void onOrientationChanged(int rotation) {
                if (((rotation >= 0) && (rotation <= 45)) || (rotation >= 315)
                        || ((rotation >= 135) && (rotation <= 225))) {// portrait
                    mCurrentOrientation = true;
                    Log.i(TAG, "竖屏");
                } else if (((rotation > 45) && (rotation < 135))
                        || ((rotation > 225) && (rotation < 315))) {// landscape
                    mCurrentOrientation = true;
                    Log.i(TAG, "横屏");
                }
            }
        };
        mOrEventListener.enable();
    }

    @Override
    public void onClick(View v) {
        // 点击拍照
        switch (v.getId()) {
            case R.id.btn_takepic://拍照
                Toast.makeText(this, "正在拍照中..", Toast.LENGTH_LONG);
                mCamera.takePicture(mShutter, null, mJpeg);
                break;
            case R.id.iv_Thumbnail://选相册
                mController.selectAlbum();
                break;
            case R.id.iv_select_product://选产品
                mController.selectProduct();
                break;
            default:
                break;
        }

    }

    @Override
    protected void onViewClick(View v) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        // SurfaceView尺寸发生改变时（首次在屏幕上显示同样会调用此方法），初始化mCamera参数，启动Camera预览

        Parameters parameters = mCamera.getParameters();// 获取mCamera的参数对象
        Size largestSize = getBestSupportedSize(parameters
                .getSupportedPreviewSizes());

        parameters.setPreviewSize(largestSize.width, largestSize.height);// 设置预览图片尺寸
        largestSize = getBestSupportedSize(parameters
                .getSupportedPictureSizes());// 设置捕捉图片尺寸
        parameters.setPictureSize(largestSize.width, largestSize.height);
        mCamera.setParameters(parameters);

        try {
            mCamera.startPreview();
        } catch (Exception e) {
            if (mCamera != null) {
                mCamera.release();
                mCamera = null;
            }
        }

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // SurfaceView创建时，建立Camera和SurfaceView的联系
        if (mCamera != null) {
            try {
                mCamera.setPreviewDisplay(holder);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // SurfaceView销毁时，取消Camera预览
        if (mCamera != null) {
            mCamera.stopPreview();
        }
    }

    @SuppressLint("NewApi")
    @Override
    public void onResume() {
        super.onResume();
        // 开启相机
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            mCamera = Camera.open(0);
            // i=0 表示后置相机
        } else
            mCamera = Camera.open();

    }

    @Override
    public void onPause() {
        super.onPause();
        // 释放相机
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }

    }

    private Size getBestSupportedSize(List<Size> sizes) {
        // 取能适用的最大的SIZE
        Size largestSize = sizes.get(0);
        int largestArea = sizes.get(0).height * sizes.get(0).width;
        for (Size s : sizes) {
            int area = s.width * s.height;
            if (area > largestArea) {
                largestArea = area;
                largestSize = s;
            }
        }
        return largestSize;
    }
}
