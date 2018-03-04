package bc.juhao.com.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.HashMap;

import bc.juhao.com.R;
import bc.juhao.com.listener.IShareCallBack;
import bocang.utils.MyToast;
import bocang.view.BaseActivity;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.favorite.WechatFavorite;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * @author: Jun
 * @date : 2017/3/15 15:54
 * @description :
 */
public class ShareUtil {
    /**
     * 分享操作
     */
    public static void showShare(final Activity activity, String title, final String path, final String imagePath) {
        if (TextUtils.isEmpty(path)) {
            return;
        }
        ShareSDK.initSDK(activity);
        HashMap<String, Object> wechat = new HashMap<String, Object>();
        wechat.put("Id", "2");
        wechat.put("SortId", "2");
        wechat.put("AppId", "wxe5dbf8785c4ec928");
        wechat.put("AppSecret", "51b49eb6c84cd25c58392c8164906968");
        wechat.put("BypassApproval", "false");
        wechat.put("Enable", "true");
        ShareSDK.setPlatformDevInfo(Wechat.NAME, wechat);
        ShareSDK.setPlatformDevInfo(WechatMoments.NAME, wechat);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(title);
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl(path);
        // text是分享文本，所有平台都需要这个字段
        oks.setText(title);
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(path);
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment(title);
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(UIUtils.getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(path);
        //图片地址
        //        mImgUrl= Constant.PRODUCT_URL+mImgUrl+ "!400X400.png";
        //        Log.v("520it","'分享:"+mImgUrl);
        //        Log.v("520it","产品地址:"+Constant.SHAREPLAN+"id="+id));
        oks.setImageUrl(imagePath);

        oks.setShareContentCustomizeCallback(new ShareContentCustomizeCallback() {
            @Override
            public void onShare(Platform platform, final Platform.ShareParams paramsToShare) {
                if ("QZone".equals(platform.getName())) {
                    paramsToShare.setTitle(null);
                    paramsToShare.setTitleUrl(null);
                }
                if ("SinaWeibo".equals(platform.getName())) {
                    paramsToShare.setUrl(null);
                    paramsToShare.setText("分享文本 " + path);
                }
                if ("Wechat".equals(platform.getName())) {
                    ImageView img = new ImageView(activity);
                    ImageLoader.getInstance().displayImage(imagePath, img);
                    paramsToShare.setImageData(img.getDrawingCache());
                }
                if ("WechatMoments".equals(platform.getName())) {
                    ImageView img = new ImageView(activity);
                    ImageLoader.getInstance().displayImage(imagePath, img);
                    paramsToShare.setImageData(img.getDrawingCache());
                }

            }
        });
        // 启动分享GUI
        oks.show(activity);
    }

    /**
     * 分享操作
     */
    public static void showShareType(final BaseActivity activity, String title, final String path, final String imagePath, final IShareCallBack shareCallBack) {
        if (TextUtils.isEmpty(path)) {
            return;
        }
        ShareSDK.initSDK(activity);

        Platform.ShareParams sp = new Platform.ShareParams();
        sp.setTitle("测试分享的标题");
        sp.setTitleUrl("http://sharesdk.cn"); // 标题的超链接
        sp.setText("测试分享的文本");
        sp.setImageUrl("http://www.someserver.com/测试图片网络地址.jpg");
        sp.setSite("发布分享的网站名称");
        sp.setSiteUrl("发布分享网站的地址");

        Platform qzone = ShareSDK.getPlatform(QQ.NAME);

        // 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
        qzone.setPlatformActionListener(new PlatformActionListener() {
            public void onError(Platform arg0, int arg1, Throwable arg2) {
                //失败的回调，arg:平台对象，arg1:表示当前的动作，arg2:异常信息
                //                activity.hideLoading();
                shareCallBack.onShareCallBackListener(false);
                MyToast.show(activity, "分享失败!");
            }

            public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                //分享成功的回调
                shareCallBack.onShareCallBackListener(true);
            }

            public void onCancel(Platform arg0, int arg1) {
                //取消分享的回调
                shareCallBack.onShareCallBackListener(false);
            }
        });
        // 执行图文分享
        qzone.share(sp);
    }

    /**
     * 分享操作
     */
    public static void showShareType02(final BaseActivity activity, String title, final String path, final String imagePath, int imageOrLink, String typeShare,boolean isLocal,Bitmap bitmap) {
        if (TextUtils.isEmpty(path)) {
            return;
        }
        ShareSDK.initSDK(activity);
        HashMap<String, Object> wechat = new HashMap<String, Object>();
        wechat.put("AppId", "wxe5dbf8785c4ec928");
        wechat.put("AppSecret", "51b49eb6c84cd25c58392c8164906968");
        wechat.put("BypassApproval", "false");
        wechat.put("Enable", "true");
        ShareSDK.setPlatformDevInfo(Wechat.NAME, wechat);
        ShareSDK.setPlatformDevInfo(WechatMoments.NAME, wechat);
        ShareSDK.setPlatformDevInfo(WechatFavorite.NAME, wechat);

        Platform.ShareParams sp = new Platform.ShareParams();
        if(isLocal){
            if (imageOrLink == 0) {
                sp.setTitle(title);
                sp.setTitleUrl(path); // 标题的超链接
                sp.setText(title);
                sp.setImagePath(imagePath);
            } else {
                sp.setImagePath(imagePath);
            }
        }else{
            if (imageOrLink == 0) {
                sp.setTitle(title);
                sp.setTitleUrl(path); // 标题的超链接
                sp.setText(title);
                sp.setImageUrl(imagePath);
            } else {
                sp.setImageUrl(imagePath);
            }
        }


        if (typeShare.equals(Wechat.NAME) || typeShare.equals(WechatMoments.NAME)) {
            if(imageOrLink==0){
                sp.setShareType(Platform.SHARE_TEXT);
            }
            sp.setShareType(Platform.SHARE_IMAGE);
        }

        Platform platform = ShareSDK.getPlatform(typeShare);

        // 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
        platform.setPlatformActionListener(new PlatformActionListener() {
            public void onError(Platform arg0, int arg1, Throwable arg2) {
                //失败的回调，arg:平台对象，arg1:表示当前的动作，arg2:异常信息
            }

            public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                //分享成功的回调
            }

            public void onCancel(Platform arg0, int arg1) {
                //取消分享的回调
            }
        });
        // 执行图文分享
        platform.share(sp);
    }

    /**
     * 分享操作
     */
    public static void showShareType03(final BaseActivity activity, String title, final String path, final String imagePath, int imageOrLink, String typeShare,boolean isFile,String filePath) {
        if (TextUtils.isEmpty(path)) {
            return;
        }
        ShareSDK.initSDK(activity);
        HashMap<String, Object> wechat = new HashMap<String, Object>();
        wechat.put("Id", "2");
        wechat.put("SortId", "2");
        wechat.put("AppId", "wxe5dbf8785c4ec928");
        wechat.put("AppSecret", "51b49eb6c84cd25c58392c8164906968");
        wechat.put("BypassApproval", "false");
        wechat.put("Enable", "true");
        ShareSDK.setPlatformDevInfo(Wechat.NAME, wechat);
        ShareSDK.setPlatformDevInfo(WechatMoments.NAME, wechat);
        ShareSDK.setPlatformDevInfo(WechatFavorite.NAME, wechat);

        Platform.ShareParams sp = new Platform.ShareParams();
        if(isFile){
            sp.setTitle(title);
            sp.setImageUrl(imagePath);
            sp.setText(title);
            sp.setFilePath(filePath);
            sp.setShareType(Platform.SHARE_FILE);

        } else{
            if (imageOrLink == 0) {
                sp.setTitle(title);
                sp.setTitleUrl(path); // 标题的超链接
                sp.setText(title);
                sp.setImagePath(imagePath);
            } else {
                sp.setImagePath(imagePath);
            }
            sp.setShareType(Platform.SHARE_IMAGE);
        }



        Platform platform = ShareSDK.getPlatform(typeShare);

        // 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
        platform.setPlatformActionListener(new PlatformActionListener() {
            public void onError(Platform arg0, int arg1, Throwable arg2) {
                //失败的回调，arg:平台对象，arg1:表示当前的动作，arg2:异常信息
            }

            public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                //分享成功的回调
            }

            public void onCancel(Platform arg0, int arg1) {
                //取消分享的回调
            }
        });
        // 执行图文分享
        platform.share(sp);
    }

    /**
     * 分享操作
     */
    public static void showShare01(final Activity activity, String title, final String path, final String imagePath) {
        if (TextUtils.isEmpty(path)) {
            return;
        }
        ShareSDK.initSDK(activity);
        HashMap<String, Object> wechat = new HashMap<String, Object>();
        wechat.put("Id", "2");
        wechat.put("SortId", "2");
        wechat.put("AppId", "wxe5dbf8785c4ec928");
        wechat.put("AppSecret", "51b49eb6c84cd25c58392c8164906968");
        wechat.put("BypassApproval", "false");
        wechat.put("Enable", "true");
        ShareSDK.setPlatformDevInfo(Wechat.NAME, wechat);
        ShareSDK.setPlatformDevInfo(WechatMoments.NAME, wechat);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(title);
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl(path);
        // text是分享文本，所有平台都需要这个字段
        oks.setText(title);
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(path);
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment(title);
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(UIUtils.getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(path);
        //图片地址
        //        mImgUrl= Constant.PRODUCT_URL+mImgUrl+ "!400X400.png";
        //        Log.v("520it","'分享:"+mImgUrl);
        //        Log.v("520it","产品地址:"+Constant.SHAREPLAN+"id="+id));
        oks.setImageUrl(imagePath);

        oks.setShareContentCustomizeCallback(new ShareContentCustomizeCallback() {
            @Override
            public void onShare(Platform platform, final Platform.ShareParams paramsToShare) {
                //                if ("QZone".equals(platform.getName())) {
                //                    paramsToShare.setTitle(null);
                //                    paramsToShare.setTitleUrl(null);
                //                }
                //                if ("SinaWeibo".equals(platform.getName())) {
                //                    paramsToShare.setUrl(null);
                //                    paramsToShare.setText("分享文本 " + path);
                //                }
                if ("Wechat".equals(platform.getName())) {

                    paramsToShare.setText(null);
                    paramsToShare.setTitle(null);
                    paramsToShare.setTitleUrl("");
                    paramsToShare.setImageUrl(imagePath);
                    //                    ImageView img = new ImageView(activity);
                    //                    ImageLoader.getInstance().displayImage(imagePath, img);
                    //                    paramsToShare.setImageData(img.getDrawingCache());
                }
                if ("WechatMoments".equals(platform.getName())) {
                    ImageView img = new ImageView(activity);
                    ImageLoader.getInstance().displayImage(imagePath, img);
                    paramsToShare.setImageData(img.getDrawingCache());
                }
                if (platform.getName().equalsIgnoreCase(QQ.NAME)) {
                    paramsToShare.setText(null);
                    paramsToShare.setTitle(null);
                    paramsToShare.setTitleUrl("");
                    paramsToShare.setImageUrl(imagePath);
                }

                platform.setPlatformActionListener(new PlatformActionListener() {
                    @Override
                    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                        activity.finish();
                    }

                    @Override
                    public void onError(Platform platform, int i, Throwable throwable) {
                        activity.finish();
                    }

                    @Override
                    public void onCancel(Platform platform, int i) {

                    }
                });

            }


        });
        // 启动分享GUI
        oks.show(activity);


    }
}
