package bc.juhao.com.ui.activity.buy;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.iot.ilop.demo.DemoApplication;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;
import com.pgyersdk.crash.PgyCrashManager;
import com.tencent.connect.share.QQShare;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.smtt.sdk.WebView;
import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import bc.juhao.com.R;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.cons.NetWorkConst;
import bc.juhao.com.controller.ExInventoryConntroller;
import bc.juhao.com.net.ApiClient;
import bc.juhao.com.ui.activity.IssueApplication;
import bc.juhao.com.ui.view.ScannerUtils;
import bc.juhao.com.utils.FileUtil;
import bc.juhao.com.utils.ImageLoadProxy;
import bc.juhao.com.utils.ImageUtil;
import bc.juhao.com.utils.MyShare;
import bc.juhao.com.utils.ShareUtil;
import bc.juhao.com.utils.UIUtils;
import bocang.json.JSONArray;
import bocang.utils.AppUtils;
import bocang.utils.MyToast;
import bocang.view.BaseActivity;
import okhttp3.Call;
import okhttp3.Response;

/**
 * @author: Jun
 * @date : 2017/8/7 9:27
 * @description :
 */
public class ExInventoryActivity extends BaseActivity {
    private RelativeLayout save_rl;
    private LinearLayout cotent_ll;
    public JSONArray goodsList;
    public JSONObject orderObject;
    private LinearLayout wechat_ll, wechatmoments_ll, share_qq_ll, save_ll;
    private ScrollView sv;
    private int mShareType = 0;
    private Bitmap bitmap;
    private String mParentPhone;
    private String mAddress;
    private ExInventoryConntroller mController;
    public int usercodeid;
    private String mParentName;

    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {
        mController = new ExInventoryConntroller(this);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_cart_inventory);
        setColor(this, Color.WHITE);
        save_rl = getViewAndClick(R.id.save_rl);
        wechat_ll = getViewAndClick(R.id.wechat_ll);
        wechatmoments_ll = getViewAndClick(R.id.wechatmoments_ll);
        share_qq_ll = getViewAndClick(R.id.share_qq_ll);
        save_ll = getViewAndClick(R.id.save_ll);
        sv = (ScrollView) findViewById(R.id.sv);
        cotent_ll = (LinearLayout) findViewById(R.id.cotent_ll);
        usercodeid = MyShare.get(this).getInt(Constance.USERCODEID);

    }


    private int mPage = 1;
    private List<String> mUids = new ArrayList<>();
    private List<String> mNumbers = new ArrayList<>();

    @Override
    protected void initData() {
        Intent intent = getIntent();
        mShareType = intent.getIntExtra(Constance.pdfType, 0);
        if (mShareType == 1) {
            orderObject = JSON.parseObject(intent.getStringExtra(Constance.goods));
        } else {
            goodsList = (JSONArray) intent.getSerializableExtra(Constance.goods);
        }

    }

    String morderUserUrl = "";
    public void fillData(org.json.JSONObject shop){
        try {
                mParentName = DemoApplication.mUserObject.getString(Constance.parent_name);
            if(shop!=null){
                mParentPhone = shop.getString(Constance.phone);
                mAddress = shop.getString(Constance.address);
            }
            if (mShareType != 0) {
                if(TextUtils.isEmpty(mParentName)){
                    mParentName=orderObject.getJSONObject(Constance.consignee).getString(Constance.name);
                }

            if(mParentPhone==null){
                mParentPhone = orderObject.getJSONObject(Constance.consignee).getString(Constance.mobile);
            }
            if(mAddress==null){
                mAddress=orderObject.getJSONObject(Constance.consignee).getString(Constance.address);
            }
            }else {

            }
            handler.sendEmptyMessage(0);
//            PgyCrashManager.reportCaughtException(this,new Exception("handler.sendEmptyMessage(0);"));
         }catch (JSONException e) {
        e.printStackTrace();
    }

    }
Handler handler=new Handler(){
    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        load();
    }
};
    public void load(){
        getInventoryData();
//        PgyCrashManager.reportCaughtException(this,new Exception("getInventoryData();"));
        for (int i = 0; i < (mUids.size()); i++) {
            com.tencent.smtt.sdk.WebView mView = new com.tencent.smtt.sdk.WebView(ExInventoryActivity.this);
            com.tencent.smtt.sdk.WebSettings webSettings = mView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
            webSettings.setUseWideViewPort(true);//关键点
            webSettings.setLayoutAlgorithm(com.tencent.smtt.sdk.WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
            webSettings.setDisplayZoomControls(false);
            webSettings.setAllowFileAccess(true); // 允许访问文件
            webSettings.setBuiltInZoomControls(false); // 设置显示缩放按钮
            webSettings.setSupportZoom(false); // 支持缩放
            webSettings.setLoadWithOverviewMode(true);
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int mDensity = metrics.densityDpi;
            if (mDensity == 240) {
                webSettings.setDefaultZoom(com.tencent.smtt.sdk.WebSettings.ZoomDensity.FAR);
            } else if (mDensity == 160) {
                webSettings.setDefaultZoom(com.tencent.smtt.sdk.WebSettings.ZoomDensity.MEDIUM);
            } else if (mDensity == 120) {
                webSettings.setDefaultZoom(com.tencent.smtt.sdk.WebSettings.ZoomDensity.CLOSE);
            } else if (mDensity == DisplayMetrics.DENSITY_XHIGH) {
                webSettings.setDefaultZoom(com.tencent.smtt.sdk.WebSettings.ZoomDensity.FAR);
            } else if (mDensity == DisplayMetrics.DENSITY_TV) {
                webSettings.setDefaultZoom(com.tencent.smtt.sdk.WebSettings.ZoomDensity.FAR);
            } else {
                webSettings.setDefaultZoom(com.tencent.smtt.sdk.WebSettings.ZoomDensity.MEDIUM);
            }
            webSettings.setLayoutAlgorithm(com.tencent.smtt.sdk.WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
            //        //设置WebView可触摸放大缩小
            //            mView.setInitialScale(100);
            mView.setDrawingCacheEnabled(true);
            mView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
            cotent_ll.addView(mView);
        }

        if (mShareType == 0) {
            String id = DemoApplication.mUserObject.getString(Constance.id);
            String path="";
            String uids="";
            String numbers="";
            for(int x=0;x<mUids.size();x++){
                uids+=mUids.get(x)+",";
                numbers+=mNumbers.get(x)+",";
            }
            uids=uids.substring(0,uids.length()-1);
            numbers=numbers.substring(0,numbers.length()-1);
            for (int i = 0; i < cotent_ll.getChildCount(); i++) {
//                path = NetWorkConst.SCENE_HOST + "order_show.php?uid=" + id + "&goods=" + uids+ "&number=" + numbers+ "&page=" + (i+1)+morderUserUrl;
                 path = NetWorkConst.SCENE_HOST + "order_show.php?goods=" + uids + "&number=" + numbers+ "&page=" + (i+1) + morderUserUrl;
                if(i==cotent_ll.getChildCount()-1){
                    path+="&end";
                }
                Log.e("url",path);
//                PgyCrashManager.reportCaughtException(this,new Exception("((WebView) cotent_ll.getChildAt(i)).loadUrl("+path+");"));
                ((WebView) cotent_ll.getChildAt(i)).loadUrl(path);
            }

        } else {
            String id = DemoApplication.mUserObject.getString(Constance.id);
            String uids="";
            String numbers="";
            for(int x=0;x<mUids.size();x++){
                uids+=mUids.get(x)+",";
                numbers+=mNumbers.get(x)+",";
            }
            uids=uids.substring(0,uids.length()-1);
            numbers=numbers.substring(0,numbers.length()-1);
            for (int i = 0; i < cotent_ll.getChildCount(); i++) {
                String path = NetWorkConst.SCENE_HOST + "order_show.php?goods=" + uids + "&number=" + numbers+ "&page=" + (i+1) + morderUserUrl;
                if(i==cotent_ll.getChildCount()-1){
                    path+="&end";
                }
                Log.e("url",path);
                ((WebView) cotent_ll.getChildAt(i)).loadUrl(path);
            }
        }
    }
    private void getInventoryData() {
        if (mShareType == 0) {
            if (goodsList.length() <= 8) {
                String uid = "";
                String number = "";
                mPage = 1;
                for (int i = 0; i < goodsList.length(); i++) {
                    uid = uid + goodsList.getJSONObject(i).getJSONObject(Constance.product).getString(Constance.id) + ",";
                    number = number + goodsList.getJSONObject(i).getString(Constance.amount) + ",";
                }
                uid = uid.substring(0, uid.length() - 1);
                number = number.substring(0, number.length() - 1);
                mUids.add(uid);
                mNumbers.add(number);
            } else {
                mPage = (goodsList.length() % 8 == 0) ? goodsList.length() / 8 : (goodsList.length() / 8 + 1);
                for (int i = 0; i < mPage; i++) {
                    String uid = "";
                    String number = "";
                    for (int j = 8 * i; j < 8 * (i + 1); j++) {
                        if (j > goodsList.length() - 1)
                            break;
                        uid = uid + goodsList.getJSONObject(j).getJSONObject(Constance.product).getString(Constance.id) + ",";
                        number = number + goodsList.getJSONObject(j).getString(Constance.amount) + ",";

                    }
                    uid = uid.substring(0, uid.length() - 1);
                    number = number.substring(0, number.length() - 1);
                    mUids.add(uid);
                    mNumbers.add(number);
                }
            }
        } else {
            com.alibaba.fastjson.JSONArray goods = orderObject.getJSONArray(Constance.goods);
            if (goods.size() <= 8) {
                String uid = "";
                String number = "";
                mPage = 1;
                for (int i = 0; i < goods.size(); i++) {
                    uid = uid + goods.getJSONObject(i).getJSONObject(Constance.product).getString(Constance.id) + ",";
                    number = number + goods.getJSONObject(i).getString(Constance.total_amount) + ",";
                }
                uid = uid.substring(0, uid.length() - 1);
                number = number.substring(0, number.length() - 1);
                mUids.add(uid);
                mNumbers.add(number);
            } else {
                mPage = (goods.size() % 8 == 0) ? goods.size() / 8 : (goods.size() / 8 + 1);
                for (int i = 0; i < mPage; i++) {
                    String uid = "";
                    String number = "";
                    for (int j = 8 * i; j < 8 * (i + 1); j++) {
                        if (j > goods.size() - 1)
                            break;
                        uid = uid + goods.getJSONObject(j).getJSONObject(Constance.product).getString(Constance.id) + ",";
                        number = number + goods.getJSONObject(j).getString(Constance.total_amount) + ",";

                    }
                    uid = uid.substring(0, uid.length() - 1);
                    number = number.substring(0, number.length() - 1);
                    mUids.add(uid);
                    mNumbers.add(number);
                }
            }

//            JSONObject consigneeObject = orderObject.getJSONObject(Constance.consignee);
            if(DemoApplication.mUserObject==null){
                bocang.utils.UIUtils.showLoginDialog(this);
                finish();
                return;
            }
        }
            morderUserUrl = "&phone=" + mParentPhone + "&address=" + mAddress + "&username=" + mParentName;


    }

    @Override
    protected void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.save_rl:
                MyToast.show(this,"pdf文件已保存");
                savePDF();
                break;
            case R.id.wechat_ll://微信分享
                wechat_ll.setEnabled(false);
                Toast.makeText(this, "正在保存pdf文件..", Toast.LENGTH_LONG).show();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(500);
                            ExInventoryActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    savePDF();
                                    wechat_ll.setEnabled(true);
                                    Toast.makeText(ExInventoryActivity.this, "正在分享..", Toast.LENGTH_SHORT).show();
                                    typeShare = SendMessageToWX.Req.WXSceneSession;
                                    final Bitmap bitmap = ImageUtil.getBitmapByView(sv);
//                                    FileUtil.saveBitmap(bitmap, (String) mFileName);
                                    if(TextUtils.isEmpty(mFilePath)){
                                        savePDF();
                                    }
                                    ShareUtil.shareWxFile(ExInventoryActivity.this, (String) mFileName, mFilePath,true);
                                }
                            });
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

                break;
            case R.id.wechatmoments_ll://微信朋友圈
                Toast.makeText(this, "正在分享..", Toast.LENGTH_SHORT).show();
                typeShare = SendMessageToWX.Req.WXSceneTimeline;
                if (!AppUtils.isEmpty(bitmap)) {
                    ShareUtil.shareWxPic(ExInventoryActivity.this, (String) mFileName, bitmap,true);
                    return;
                }
                bitmap = ImageUtil.getBitmapByView(sv);
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        mShareImagePath = ScannerUtils.saveImageToGallery02(ExInventoryActivity.this, bitmap, ScannerUtils.ScannerType.RECEIVER);
                        ExInventoryActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ShareUtil.shareWxPic(ExInventoryActivity.this, (String) mFileName, bitmap,false);
                            }
                        });
                    }
                }).start();

                break;
            case R.id.share_qq_ll://qq分享
                Toast.makeText(this, "正在分享..", Toast.LENGTH_SHORT).show();
                typeShare = QQShare.SHARE_TO_QQ_TYPE_IMAGE;
                if (!AppUtils.isEmpty(mShareImagePath)) {
                    Toast.makeText(this, "图片保存为" + mShareImagePath, Toast.LENGTH_SHORT).show();
                    ShareUtil.shareQQLocalpic(ExInventoryActivity.this, mShareImagePath,mFileName+"");
                    //分享
                    return;
                }
                if(bitmap==null)bitmap=ImageUtil.getBitmapByView(sv);
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        mShareImagePath = ScannerUtils.saveImageToGallery02(ExInventoryActivity.this, bitmap, ScannerUtils.ScannerType.RECEIVER);
                        ExInventoryActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ShareUtil.shareQQLocalpic(ExInventoryActivity.this, mShareImagePath,mFileName+"");
                            }
                        });
                    }
                }).start();
                break;
            case R.id.save_ll://保存
                getSaveImage();
                break;

        }

    }

    private int typeShare;
    private String mShareImagePath = "";


    /**
     * 保存图片
     */
    public void getSaveImage() {
        if (!AppUtils.isEmpty(mShareImagePath)) {
            Toast.makeText(this, "图片保存为" + mShareImagePath, Toast.LENGTH_SHORT).show();
            return;
        }

        ActivityCompat.requestPermissions(this,
                new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"},
                1);
        PackageManager packageManager = this.getPackageManager();
        int permission = packageManager.checkPermission("android.permission.WRITE_EXTERNAL_STORAGE", "bc.juhao.com");
        if (PackageManager.PERMISSION_GRANTED != permission) {
            return;
        } else {
            this.setShowDialog(true);
            //            mActivity.setShowDialog("正在保存中..");
            this.showLoading();
            new Thread(new Runnable() {
                @Override
                public void run() {


                    ExInventoryActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            final Bitmap bitmap = ImageUtil.getBitmapByView(sv);
                            ScannerUtils.saveImageToGallery(ExInventoryActivity.this, bitmap, ScannerUtils.ScannerType.RECEIVER);
                            ExInventoryActivity.this.hideLoading();
                            bitmap.recycle();
                        }
                    });

                }
            }).start();
        }
    }

    private String mFilePath = "";
    private Object mFileName = "";

    /**
     * 保存PDF
     */
    private void savePDF() {
        Document document = new Document();
        try {
            File localFile;
            Object localObject1 = FileUtil.getFile(this);
            Object localObject2;
            Object localObject3;
            if (localObject1 != null) {
                localObject2 = new SimpleDateFormat("yyyyMMddHHmmss");
                localObject3 = new Date();
                if (mShareType == 0) {
                    localObject2 = "购物清单-" + ((SimpleDateFormat) localObject2).format((Date) localObject3) + ".pdf";
                    mFileName = "您的购物清单已出炉.pdf";
                } else {
                    localObject2 = "订单清单-" + ((SimpleDateFormat) localObject2).format((Date) localObject3) + ".pdf";
                    mFileName = "您的订单清单已出炉.pdf";
                }

                localFile = new File((File) localObject1, (String) localObject2);
                mFilePath = localFile.getAbsolutePath();
                PdfWriter.getInstance(document, new FileOutputStream(localFile));
                document.setMargins(0, 0, 0, 0);
                document.open();
                for (int i = 0; i < cotent_ll.getChildCount(); i++) {
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    Bitmap bitmap = FileUtil.captureWebView(((WebView) cotent_ll.getChildAt(i)));
                    //获取webview缩放率
                    bitmap.compress(Bitmap.CompressFormat.PNG /* FileType */, 100 /* Ratio */, stream);
                    Image jpg = Image.getInstance(stream.toByteArray());
                    jpg.setAlignment(Image.MIDDLE);
                    float heigth = jpg.getHeight();
                    float width = jpg.getWidth();
                    int percent = getPercent2(heigth, width);
                    jpg.scalePercent(percent);
                    document.add(jpg);
                    bitmap.recycle();
                }
                document.setPageCount(cotent_ll.getChildCount());
            }
        } catch (DocumentException de) {
            System.err.println(de.getMessage());
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }

        document.close();
        this.hideLoading();
    }

    public int getPercent(float h, float w) {
        int p = 0;
        float p2 = 0.0f;
        if (h > w) {
            p2 = 297 / h * 100;
        } else {
            p2 = 210 / w * 100;
        }
        p = Math.round(p2);
        return p;
    }

    public int getPercent2(float h, float w) {
        int p = 0;
        float p2 = 0.0f;
        p2 = 530 / w * 100;
        p = Math.round(p2);
        return p;
    }


}
