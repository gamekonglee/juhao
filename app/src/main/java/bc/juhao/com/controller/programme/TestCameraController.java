package bc.juhao.com.controller.programme;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Message;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import bc.juhao.com.R;
import bc.juhao.com.bean.GoodsShape;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.controller.BaseController;
import bc.juhao.com.ui.activity.product.SelectGoodsActivity;
import bc.juhao.com.ui.activity.programme.DiyActivity;
import bc.juhao.com.ui.activity.programme.camera.TestCameraActivity;
import bc.juhao.com.ui.view.TouchView;
import bc.juhao.com.utils.FileUtil;
import bocang.json.JSONObject;
import bocang.utils.AppUtils;
import bocang.utils.MyToast;

/**
 * @author: Jun
 * @date : 2017/6/21 10:40
 * @description :
 */
public class TestCameraController extends BaseController {
    private TestCameraActivity mView;
    private ProgressBar pd2;
    private DisplayImageOptions options;
    private ImageLoader imageLoader;
    private int mLightNumber = -1;// 点出来的灯的编号
    private int mLightId = 0;//点出来的灯的序号
    private int leftMargin = 0;
    private int mScreenWidth;
    private Intent mIntent;
    public FrameLayout mFrameLayout;

    public TestCameraController(TestCameraActivity v){
        mView=v;
        initView();
        initViewData();
    }

    private void initViewData() {
        if (!AppUtils.isEmpty(mView.mGoodsObject)) {
            displayCheckedGoods(mView.mGoodsObject);
        }
    }

    private void initView() {
        mFrameLayout = (FrameLayout) mView.findViewById(R.id.sceneFrameLayout);
        pd2 = (ProgressBar) mView.findViewById(R.id.pd2);
        mScreenWidth = mView.getResources().getDisplayMetrics().widthPixels;
        initImageLoader();
    }

    private void initImageLoader() {
        options = new DisplayImageOptions.Builder()
                // 设置图片下载期间显示的图片
                .showImageOnLoading(R.drawable.bg_default)
                        // 设置图片Uri为空或是错误的时候显示的图片
                .showImageForEmptyUri(R.drawable.bg_default)
                        // 设置图片加载或解码过程中发生错误显示的图片
                .showImageOnFail(R.drawable.bg_default)
                        // 设置下载的图片是否缓存在内存中
                .cacheInMemory(false)
                        //设置图片的质量
                .bitmapConfig(Bitmap.Config.RGB_565)
                        // 设置下载的图片是否缓存在SD卡中
                .cacheOnDisk(true)
                        // .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
                        // 是否考虑JPEG图像EXIF参数（旋转，翻转）
                        //                .considerExifParams(true)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)// 设置图片可以放大（要填满ImageView必须配置memoryCacheExtraOptions大于Imageview）
                        // 图片加载好后渐入的动画时间
                        // .displayer(new FadeInBitmapDisplayer(100))
                .build(); // 构建完成

        // 得到ImageLoader的实例(使用的单例模式)
        imageLoader = ImageLoader.getInstance();
    }

    @Override
    protected void handleMessage(int action, Object[] values) {

    }

    @Override
    protected void myHandleMessage(Message msg) {

    }

    private void displayCheckedGoods(final JSONObject goods) {
        if (AppUtils.isEmpty(goods))
            return;
        String path = goods.getJSONObject(Constance.app_img).getString(Constance.img);
        imageLoader.loadImage(path, options,
                new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        pd2.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view,
                                                FailReason failReason) {
                        pd2.setVisibility(View.GONE);
                        MyToast.show(mView, failReason.getCause() + "请重试！");
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        super.onLoadingComplete(imageUri, view, loadedImage);
                        pd2.setVisibility(View.GONE);
                        // 被点击的灯的编号加1
                        mLightNumber++;
                        // 把点击的灯放到集合里
                        mView.mSelectedLightSA.put(mLightNumber, goods);

//                        // 设置灯图的ImageView的初始宽高和位置
//                        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
//                                mScreenWidth / 2,
//                                (mScreenWidth / 2 * loadedImage.getHeight()) / loadedImage.getWidth());
//                        // 设置灯点击出来的位置
//                        if (mView.mSelectedLightSA.size() == 1) {
//                            leftMargin = mScreenWidth / 3 * 2 / 3;
//                        } else if (mView.mSelectedLightSA.size() == 2) {
//                            leftMargin = mScreenWidth / 3 * 2 / 3 * 2;
//                        } else if (mView.mSelectedLightSA.size() == 3) {
//                            leftMargin = 0;
//                        }
////                        lp.setMargins(loadedImage.getWidth() / 3, mScreenWidth / 2, 0, 0);
//                                                lp.setMargins(63, 44, 0, 0);

                        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                                mScreenWidth / 3 * 2 / 3,
                                (mScreenWidth / 3 * 2 / 3 * loadedImage.getHeight()) / loadedImage.getWidth());
                        // 设置灯点击出来的位置
                        if (mView.mSelectedLightSA.size() == 1) {
                            leftMargin = mScreenWidth / 3 * 2 / 3;
                        } else if (mView.mSelectedLightSA.size() == 2) {
                            leftMargin = mScreenWidth / 3 * 2 / 3 * 2;
                        } else if (mView.mSelectedLightSA.size() == 3) {
                            leftMargin = 0;
                        }
                        lp.setMargins(mScreenWidth/2-(mScreenWidth / 3 * 2 / 6), 20, 0, 0);


                        TouchView touchView = new TouchView(mView);
                        touchView.setLayoutParams(lp);

//                        Matrix matrix = new Matrix();
//                        // 设置旋转角度
//                        matrix.setRotate(90);
//                        // 重新绘制Bitmap
//                        loadedImage = Bitmap.createBitmap(loadedImage, 0, 0, loadedImage.getWidth(), loadedImage.getHeight(), matrix, true);


                        touchView.setImageBitmap(loadedImage);// 设置被点击的灯的图片
                        touchView.setmLightCount(mLightNumber);// 设置被点击的灯的编号
                        touchView.setTag(mLightNumber);
                        FrameLayout.LayoutParams newLp = new FrameLayout.LayoutParams(
                                FrameLayout.LayoutParams.MATCH_PARENT,
                                FrameLayout.LayoutParams.MATCH_PARENT);
                        FrameLayout newFrameLayout = new FrameLayout(mView);
                        newFrameLayout.setLayoutParams(newLp);
                        newFrameLayout.addView(touchView);
                        newFrameLayout.setTag(mLightNumber);
                        mFrameLayout.addView(newFrameLayout);
                        touchView.setContainer(mFrameLayout, newFrameLayout);
                    }

                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {
                        pd2.setVisibility(View.GONE);
                    }
                });
    }

    /**
     * 选择相册
     */
    public void selectAlbum() {
        FileUtil.getPickPhoto(mView);
    }

    /**
     * 选择产品
     */
    public void selectProduct() {
        mIntent = new Intent(mView, SelectGoodsActivity.class);
        mIntent.putExtra(Constance.ISSELECTGOODS, true);
        mView.startActivityForResult(mIntent, Constance.FROMDIY);
    }


    public void goDIY(String path){
        Intent intent=new Intent(mView, DiyActivity.class);
        intent.putExtra(Constance.img_path,path);
        intent.putExtra(Constance.FROMPHOTO, true);
        intent.putExtra(Constance.scaleType
                , 1);
        intent.putExtra(Constance.productlist
                , (Serializable)mView.mGoodsList);
        List<GoodsShape> goodsShapeList=new ArrayList<>();
        for (int i = 0; i <mFrameLayout.getChildCount(); i++) {
            GoodsShape shape=new GoodsShape();
            TouchView view = (TouchView) ((FrameLayout)mFrameLayout.getChildAt(i)).getChildAt(0);
            shape.setHeight(view.getHeight());
            shape.setWidth(view.getWidth());
            shape.setX((int) view.getX());
            shape.setY((int) view.getY());
            shape.setRotate(view.getRotation());
            goodsShapeList.add(shape);
        }

        intent.putExtra(Constance.productshape, (Serializable)goodsShapeList);
        mView.startActivity(intent);
        mView.finish();
    }

    public void ActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == mView.RESULT_OK) { // 返回成功
            switch (requestCode) {

                case Constance.PHOTO_WITH_DATA: // 从图库中选择图片
                    // 照片的原始资源地址
                    String bmPath = data.getData().toString();
                    Intent intent=new Intent(mView, DiyActivity.class);
                    intent.putExtra(Constance.img_path,bmPath);
                    intent.putExtra(Constance.FROMPHOTO, true);
                    intent.putExtra(Constance.productlist
                            , (Serializable)mView.mGoodsList);
                    List<GoodsShape> goodsShapeList=new ArrayList<>();
                    for (int i = 0; i <mFrameLayout.getChildCount(); i++) {
                        GoodsShape shape=new GoodsShape();
                        TouchView view = (TouchView) ((FrameLayout)mFrameLayout.getChildAt(i)).getChildAt(0);
                        shape.setHeight(view.getHeight());
                        shape.setWidth(view.getWidth());
                        shape.setX((int) view.getX());
                        shape.setY((int) view.getY());
                        shape.setRotate(view.getRotation());
                        goodsShapeList.add(shape);
                    }

                    intent.putExtra(Constance.productshape, (Serializable)goodsShapeList);
                    mView.startActivity(intent);
                    mView.finish();


                    break;
            }
        } else if (requestCode == Constance.FROMDIY) {
            if (AppUtils.isEmpty(data))
                return;
            mView.mGoodsObject = (JSONObject) data.getSerializableExtra(Constance.product);
            displayCheckedGoods(mView.mGoodsObject);
            mView.mGoodsList.add(mView.mGoodsObject);
        }
    }
}
