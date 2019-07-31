package bc.juhao.com.controller.programme;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;

import java.io.IOException;

import bc.juhao.com.cons.Constance;
import bc.juhao.com.cons.NetWorkConst;
import bc.juhao.com.controller.BaseController;
import bc.juhao.com.ui.activity.programme.ShareProgrammeActivity;
import bc.juhao.com.ui.activity.user.MessageDetailActivity;
import bc.juhao.com.ui.view.ScannerUtils;
import bc.juhao.com.ui.view.ShowDialog;
import bc.juhao.com.utils.ImageUtil;
import bc.juhao.com.utils.ShareUtil;

/**
 * @author: Jun
 * @date : 2017/6/8 16:09
 * @description :
 */
public class ShareProgrammeController extends BaseController {
    private ShareProgrammeActivity mView;
    private Bitmap localBitmap;

    public ShareProgrammeController(ShareProgrammeActivity v) {
        mView = v;
        initView();
        initViewData();
    }

    private void initViewData() {

    }

    private void initView() {

    }

    @Override
    protected void handleMessage(int action, Object[] values) {

    }

    @Override
    protected void myHandleMessage(Message msg) {

    }

    /**
     * 查看方案
     */
    public void getFanganDetail() {
//        Intent mainIntent = new Intent(mView, MainActivity.class);
//        mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//        IssueApplication.isGoProgramme = true;
//        mView.startActivity(mainIntent);
        Intent intent = new Intent(mView, MessageDetailActivity.class);
        String SceenId = mView.id;
        intent.putExtra(Constance.url, NetWorkConst.SHAREFANAN_APP + SceenId);
        intent.putExtra(Constance.FROMTYPE, 1);
        mView.startActivity(intent);
    }

    /**
     * 保存图片
     */
    public void getSaveImage() {
        ActivityCompat.requestPermissions(mView,
                new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"},
                1);
        PackageManager packageManager = mView.getPackageManager();
        int permission = packageManager.checkPermission("android.permission.WRITE_EXTERNAL_STORAGE", "bc.juhao.com");
        if (PackageManager.PERMISSION_GRANTED != permission) {
            return;
        } else {

            ShowDialog mDialog = new ShowDialog();
            mDialog.show(mView, "提示", "是否保存该分享图片?", new ShowDialog.OnBottomClickListener() {
                @Override
                public void positive() {
                    PackageManager packageManager = mView.getPackageManager();
                    int permission = packageManager.checkPermission("android.permission.WRITE_EXTERNAL_STORAGE", "bc.juhao.com");
                    if (PackageManager.PERMISSION_GRANTED != permission) {
                        return;
                    } else {
                        mView.setShowDialog(true);
                        mView.setShowDialog("正在保存中..");
                        mView.showLoading();

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                localBitmap = null;
                                try {
                                    localBitmap = ImageUtil.decodeFile(mView.mShareImgPath);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                mView.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ScannerUtils.saveImageToGallery(mView, localBitmap, ScannerUtils.ScannerType.RECEIVER);
                                        if(localBitmap !=null&&!localBitmap.isRecycled()) localBitmap.recycle();
                                        mView.hideLoading();
                                    }
                                });
                            }
                        }).start();
                    }
                }

                @Override
                public void negtive() {

                }
            });
        }
    }

    /**
     * 分享数据
     */
    public void getShareData() {
        Toast.makeText(mView, "正在分享..", Toast.LENGTH_LONG).show();
        try {
            localBitmap = ImageUtil.decodeFile(mView.mShareImgPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(mView.mShareType==1){
            //pic
            if(mView.typeShare== SendMessageToWX.Req.WXSceneSession){
                ShareUtil.shareWxPic(mView,mView.mShareTitle,localBitmap,true);
            }else if(mView.typeShare==SendMessageToWX.Req.WXSceneTimeline){
                ShareUtil.shareWxPic(mView,mView.mShareTitle,localBitmap,false);
            }else {
                ShareUtil.shareQQLocalpic(mView,mView.mShareImgPath,mView.mShareTitle);
            }
        }else {
            if(mView.typeShare== SendMessageToWX.Req.WXSceneSession){
                ShareUtil.shareWx(mView,mView.mShareTitle,mView.mSharePath,mView.mShareImgPath);
            }else if(mView.typeShare==SendMessageToWX.Req.WXSceneTimeline){
                ShareUtil.sharePyq(mView,mView.mShareTitle,mView.mSharePath,mView.mShareImgPath);
            }else {
                ShareUtil.shareQQ(mView,mView.mShareTitle,mView.mSharePath,mView.mShareImgPath);
            }
        }

//        ShareUtil.showShareType02(mView, mView.mShareTitle, mView.mSharePath, mView.mShareImgPath, mView.mShareType, mView.typeShare,false,null);

    }
}
