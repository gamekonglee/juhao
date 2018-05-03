package bc.juhao.com.controller.product;

import android.graphics.Bitmap;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import bc.juhao.com.R;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.cons.NetWorkConst;
import bc.juhao.com.controller.BaseController;
import bc.juhao.com.ui.activity.product.ShareProductActivity;
import bc.juhao.com.ui.view.ScannerUtils;
import bc.juhao.com.ui.view.popwindow.ShareProductPopWindow;
import bc.juhao.com.utils.ImageUtil;
import bocang.utils.MyToast;

/**
 * @author: Jun
 * @date : 2017/7/29 14:43
 * @description :
 */
public class ShareProductController extends BaseController {
    private ShareProductActivity mView;
    private ImageView share_01_iv, share_02_iv, share_03_iv, share_04_iv;
    private String mId = "";
    private String mName = "";
    private String mImagePath = "";
    private int mSelectType = 0;
    private ShareProductPopWindow mProductPopWindow;
    private LinearLayout main_ll;
    private String mTitle = "";
    private String mCardPath = "";
    private DisplayImageOptions options;
    private ImageLoader imageLoader;
    private LinearLayout share_ll;
    public String mShareImagePath = "";

    public ShareProductController(ShareProductActivity v) {
        mView = v;
        initView();
        initViewData();
    }

    private void initViewData() {
        mId = mView.mGoodsObject.getString(Constance.id);
        mName = mView.mGoodsObject.getString(Constance.name);
        mImagePath = mView.mGoodsObject.getJSONObject(Constance.app_img).getString(Constance.img);
        mTitle = "来自 " + mName + " 产品的分享";
        mCardPath = NetWorkConst.WEB_PRODUCT_CARD + mId;
        mView.setShowDialog(true);
        mView.showLoading();
    }

    private void initView() {
        share_01_iv = (ImageView) mView.findViewById(R.id.share_01_iv);
        share_02_iv = (ImageView) mView.findViewById(R.id.share_02_iv);
        share_03_iv = (ImageView) mView.findViewById(R.id.share_03_iv);
        share_04_iv = (ImageView) mView.findViewById(R.id.share_04_iv);
        main_ll = (LinearLayout) mView.findViewById(R.id.main_ll);
        share_ll = (LinearLayout) mView.findViewById(R.id.share_ll);
        initImageLoader();
    }


    public void ShareType(int type) {
        share_02_iv.setVisibility(View.GONE);
        share_01_iv.setVisibility(View.GONE);
        share_03_iv.setVisibility(View.GONE);
        share_04_iv.setVisibility(View.GONE);
        switch (type) {
            case R.id.share_01_tv:
                mSelectType = 0;
                displayGoods(NetWorkConst.WEB_PRODUCT_CARD + mId);
                share_01_iv.setVisibility(View.VISIBLE);
                break;
            case R.id.share_02_tv:
                mSelectType = 1;
                ImageLoader.getInstance().displayImage(mImagePath, share_03_iv);
                share_04_iv.setImageBitmap(ImageUtil.getTowCodeImage(ImageUtil.createQRImage(NetWorkConst.SHAREPRODUCT + mId, 180, 180), mName));
                share_03_iv.setVisibility(View.VISIBLE);
                share_04_iv.setVisibility(View.VISIBLE);
                break;
            case R.id.share_03_tv:
                mSelectType = 2;
                share_02_iv.setImageBitmap(ImageUtil.getTowCodeImage(ImageUtil.createQRImage(NetWorkConst.SHAREPRODUCT + mId, 250, 250), mName));
                share_02_iv.setVisibility(View.VISIBLE);
                break;
            case R.id.share_04_tv:
                mSelectType = 3;
                ImageLoader.getInstance().displayImage(mImagePath, share_02_iv);
                share_02_iv.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    protected void handleMessage(int action, Object[] values) {

    }

    @Override
    protected void myHandleMessage(Message msg) {

    }

    private void getBitMap() {
        mView.setShowDialog(true);
        mView.showLoading();

    }

    /**
     * 分享产品
     */
    public void getShare() {
        mView.setShowDialog(true);
        mView.showLoading();
        switch (mSelectType) {
            case 0:
                mProductPopWindow = new ShareProductPopWindow(mView);
                mProductPopWindow.mActivity = mView;
                mProductPopWindow.mShareTitle = mTitle;
                mProductPopWindow.mShareImgPath = mImagePath;
                mProductPopWindow.mSharePath = mCardPath;
                mProductPopWindow.onShow(main_ll);
                mView.hideLoading();
                break;
            case 1:
                mProductPopWindow = new ShareProductPopWindow(mView);
                mProductPopWindow.mActivity = mView;
                mProductPopWindow.mShareTitle = mTitle;

                mProductPopWindow.mIsLocal = true;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mProductPopWindow.mBitmap = ImageUtil.createViewBitmap(share_ll);
                        mShareImagePath = ScannerUtils.saveImageToGallery02(mView, mProductPopWindow.mBitmap, ScannerUtils.ScannerType.RECEIVER);
                        mView.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mProductPopWindow.mShareImgPath = mShareImagePath;
                                mProductPopWindow.mSharePath = mShareImagePath;
                                mProductPopWindow.onShow(main_ll);
                                mView.hideLoading();
                            }
                        });
                    }
                }).start();

                break;
            case 2:
                mProductPopWindow = new ShareProductPopWindow(mView);
                mProductPopWindow.mActivity = mView;
                mProductPopWindow.mShareTitle = mTitle;
                mProductPopWindow.mShareImgPath = mCardPath;
                mProductPopWindow.mSharePath = mCardPath;
                mProductPopWindow.mIsLocal = true;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mProductPopWindow.mBitmap = ImageUtil.createViewBitmap(share_ll);
                        mShareImagePath = ScannerUtils.saveImageToGallery02(mView, mProductPopWindow.mBitmap, ScannerUtils.ScannerType.RECEIVER);
                        mView.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mProductPopWindow.mShareImgPath = mShareImagePath;
                                mProductPopWindow.mSharePath = mShareImagePath;
                                mProductPopWindow.onShow(main_ll);
                                mView.hideLoading();
                            }
                        });
                    }
                }).start();
                break;
            case 3:
                mProductPopWindow = new ShareProductPopWindow(mView);
                mProductPopWindow.mActivity = mView;
                mProductPopWindow.mShareTitle = mTitle;
                mProductPopWindow.mShareImgPath = mImagePath;
                mProductPopWindow.mSharePath = mImagePath;
                mProductPopWindow.onShow(main_ll);
                mView.hideLoading();
                break;
        }
    }

    private void displayGoods(String path) {
        imageLoader.loadImage(path, options,
                new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {

                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view,
                                                FailReason failReason) {
                        mView.hideLoading();
                        MyToast.show(mView, failReason.getCause() + "请重试！");
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        super.onLoadingComplete(imageUri, view, loadedImage);
                        mView.hideLoading();
                        share_01_iv.setImageBitmap(loadedImage);// 设置被点击的灯的图片
                    }

                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {
                        mView.hideLoading();

                    }
                });
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
}
