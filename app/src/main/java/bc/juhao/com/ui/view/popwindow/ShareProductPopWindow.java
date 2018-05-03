package bc.juhao.com.ui.view.popwindow;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.tencent.connect.share.QQShare;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;

import bc.juhao.com.R;
import bc.juhao.com.listener.IShareProductListener;
import bc.juhao.com.ui.view.ScannerUtils;
import bc.juhao.com.utils.ImageUtil;
import bc.juhao.com.utils.ShareUtil;
import bocang.view.BaseActivity;


/**
 * @author: Jun
 * @date : 2017/2/16 15:12
 * @description :
 */
public class ShareProductPopWindow extends BasePopwindown implements View.OnClickListener {
    private IShareProductListener mListener;
    private RelativeLayout pop_rl;
    private LinearLayout wechat_ll,wechatmoments_ll,share_qq_ll,save_ll;
    public int mShareType=1;
    public String mSharePath="";
    public String mShareImgPath="";
    public String mShareTitle="";
    public int typeShare=0;
    public BaseActivity mActivity;
    public boolean mIsLocal=false;
    public Bitmap mBitmap;


    public void setListener(IShareProductListener listener) {
        mListener = listener;
    }

    public ShareProductPopWindow(Context context) {
        super(context);
        initViewData();

    }

    private void initViewData() {

    }

    @Override
    protected void initView(Context context) {
        View contentView = View.inflate(mContext, R.layout.pop_product_share, null);
        pop_rl = (RelativeLayout) contentView.findViewById(R.id.pop_rl);
        pop_rl.setOnClickListener(this);
        wechat_ll = (LinearLayout) contentView.findViewById(R.id.wechat_ll);
        wechat_ll.setOnClickListener(this);
        wechatmoments_ll = (LinearLayout) contentView.findViewById(R.id.wechatmoments_ll);
        wechatmoments_ll.setOnClickListener(this);
        share_qq_ll = (LinearLayout) contentView.findViewById(R.id.share_qq_ll);
        share_qq_ll.setOnClickListener(this);
        save_ll = (LinearLayout) contentView.findViewById(R.id.save_ll);
        save_ll.setOnClickListener(this);
        initUI(contentView);
    }

    private void initUI(View contentView) {

        mPopupWindow = new PopupWindow(contentView,-1, -1);
        // 1.让mPopupWindow内部的控件获取焦点
        mPopupWindow.setFocusable(true);
        // 2.mPopupWindow内部获取焦点后 外部的所有控件就失去了焦点
        mPopupWindow.setOutsideTouchable(true);
        //只有加载背景图还有效果
        mPopupWindow.setBackgroundDrawable(new ColorDrawable());
        // 3.如果不马上显示PopupWindow 一般建议刷新界面
        mPopupWindow.update();
        mPopupWindow.setAnimationStyle(R.style.AnimBottom);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pop_rl:
                onDismiss();
            break;
            case R.id.wechat_ll://分享微信
                typeShare= SendMessageToWX.Req.WXSceneSession;
                getShareData();
                onDismiss();
            break;
            case R.id.wechatmoments_ll://分享朋友圈
                typeShare= SendMessageToWX.Req.WXSceneTimeline;
                getShareData();
                onDismiss();
            break;
            case R.id.share_qq_ll://分享QQ
                typeShare= 3;
                getShareData();
                onDismiss();
            break;
            case R.id.save_ll://保存
                getSaveImage();
                onDismiss();
            break;
        }
    }




    /**
     * 保存图片
     */
    public void getSaveImage() {
        ActivityCompat.requestPermissions(mActivity,
                new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"},
                1);
        PackageManager packageManager = mActivity.getPackageManager();
        int permission = packageManager.checkPermission("android.permission.WRITE_EXTERNAL_STORAGE", "bc.juhao.com");
        if (PackageManager.PERMISSION_GRANTED != permission) {
            return;
        } else {
            mActivity.setShowDialog(true);
//            mActivity.setShowDialog("正在保存中..");
            mActivity.showLoading();
            new Thread(runnable).start();
        }
    }
    Runnable runnable=new Runnable() {
        @Override
        public void run() {
            if(mIsLocal){
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final Bitmap bitmap = ImageUtil.getbitmap(mShareImgPath);
                        ScannerUtils.saveImageToGallery(mActivity, bitmap, ScannerUtils.ScannerType.RECEIVER);
                        mActivity.hideLoading();
                    }
                });
            }else{
                final Bitmap bitmap = ImageUtil.getbitmap(mShareImgPath);
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ScannerUtils.saveImageToGallery(mActivity, bitmap, ScannerUtils.ScannerType.RECEIVER);
                        bitmap.recycle();
                        mActivity.hideLoading();
                    }
                });
            }

        }
    };
    /**
     * 分享数据
     */
    public void getShareData() {
        Toast.makeText(this.mContext, "正在分享..", Toast.LENGTH_LONG).show();
        if(mIsLocal){
            if(typeShare==SendMessageToWX.Req.WXSceneSession){
            ShareUtil.shareWxPic(mActivity,mShareTitle,mBitmap,true);
            }else if(typeShare==SendMessageToWX.Req.WXSceneTimeline){
                ShareUtil.shareWxPic(mActivity,mShareTitle,mBitmap,false);
            }else {
                ShareUtil.shareQQLocalpic(mActivity,mShareImgPath,mShareTitle);
            }
//            ShareUtil.showShareType02(mActivity, mShareTitle, mSharePath, mShareImgPath, this.mShareType, this.typeShare,true,mBitmap);
        }else{
            ImageLoader.getInstance().loadImage(mSharePath, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String s, View view) {

                }

                @Override
                public void onLoadingFailed(String s, View view, FailReason failReason) {

                }

                @Override
                public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                    if(typeShare==SendMessageToWX.Req.WXSceneSession){
                        ShareUtil.shareWxPic(mActivity,mShareTitle,bitmap,true);
                    }else if(typeShare==SendMessageToWX.Req.WXSceneTimeline){
                        ShareUtil.shareWxPic(mActivity,mShareTitle,bitmap,false);
                    }else {
                        mShareImgPath=ScannerUtils.saveImageToGallery(mContext,bitmap, ScannerUtils.ScannerType.RECEIVER);
                        ShareUtil.shareQQLocalpic(mActivity,mShareImgPath,mShareTitle);
                    }
                }

                @Override
                public void onLoadingCancelled(String s, View view) {

                }
            });

//            ShareUtil.showShareType02(mActivity, mShareTitle, mSharePath, mShareImgPath, this.mShareType, this.typeShare,false,null);
        }

    }
}
