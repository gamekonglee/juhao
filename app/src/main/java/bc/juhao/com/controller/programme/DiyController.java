package bc.juhao.com.controller.programme;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.alibaba.fastjson.JSON;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.greenrobot.eventbus.EventBus;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import bc.juhao.com.R;
import bc.juhao.com.bean.GoodsShape;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.cons.NetWorkConst;
import bc.juhao.com.controller.BaseController;
import bc.juhao.com.listener.IDiyProductInfoListener;
import bc.juhao.com.listener.INetworkCallBack;
import bc.juhao.com.listener.ISelectScreenListener;
import bc.juhao.com.ui.activity.IssueApplication;
import bc.juhao.com.ui.activity.product.SelectGoodsActivity;
import bc.juhao.com.ui.activity.programme.DiyActivity;
import bc.juhao.com.ui.activity.programme.SelectSceneActivity;
import bc.juhao.com.ui.activity.programme.SelectSchemeActivity;
import bc.juhao.com.ui.activity.programme.ShareProgrammeActivity;
import bc.juhao.com.ui.view.ScannerUtils;
import bc.juhao.com.ui.view.SingleTouchView;
import bc.juhao.com.ui.view.StickerView;
import bc.juhao.com.ui.view.TouchView;
import bc.juhao.com.ui.view.TouchView02;
import bc.juhao.com.ui.view.popwindow.DiyProductInfoPopWindow;
import bc.juhao.com.ui.view.popwindow.SelectScreenPopWindow;
import bc.juhao.com.utils.FileUtil;
import bc.juhao.com.utils.ImageUtil;
import bc.juhao.com.utils.MyShare;
import bc.juhao.com.utils.UIUtils;
import bocang.json.JSONArray;
import bocang.json.JSONObject;
import bocang.utils.AppDialog;
import bocang.utils.AppUtils;
import bocang.utils.MyToast;

/**
 * @author: Jun
 * @date : 2017/2/18 10:33
 * @description :场景配灯
 */
public class DiyController extends BaseController implements INetworkCallBack {
    private DiyActivity mView;
    private ImageView sceneBgIv, jingxian_iv;
    private String imageURL = "";
    private RelativeLayout diyContainerRl;
    private Boolean isFullScreen = false;
    private Intent mIntent;
    private LinearLayout seekbar_ll;
    private int mScreenWidth;
    private int mScreenheight;
    private FrameLayout mFrameLayout;
    private ProgressBar pd2;
    private String mStyle = "";
    private String mSpace = "";
    private int TIME_OUT = 10 * 1000;   //超时时间
    private String CHARSET = "utf-8"; //设置编码
    private String mTitle = "";
    private String mContent = "";
    private FrameLayout main_fl;
    private SeekBar seekbar;
    private int mSeekNum = 50;
    private ProAdapter mAdapter;
    private ListView product_lv, diy_lv;

    //    private PicsGestureLayout picsGestureLayout = null;

    //当前处于编辑状态的贴纸
    private StickerView mCurrentView;
    //存储贴纸列表
    private ArrayList<View> mViews;


    public DiyController(DiyActivity v) {
        mView = v;
        initView();
        initViewData();
    }


    private void initViewData() {

        if (mView.isFromPhoto == true) {
            for (int i = 0; i < mView.goodsShapeList.size(); i++) {
                mView.mGoodsObject = mView.goodsList.get(i);
                displayCheckedGoods02(mView.mGoodsObject, mView.goodsShapeList.get(i));
            }
            if (!AppUtils.isEmpty(mView.mPath)) {
                displaySceneBg(mView.mPath, mView.mScaleType);
            }
        } else {
            if (!AppUtils.isEmpty(mView.mGoodsObject)) {
                displayCheckedGoods(mView.mGoodsObject);
            }
        }

        goodses = IssueApplication.mSelectProducts;
        mAdapter = new ProAdapter();
        product_lv.setAdapter(mAdapter);
    }

    private Bitmap mSceneBg;

    private void initView() {
        sceneBgIv = (ImageView) mView.findViewById(R.id.sceneBgIv);
        diyContainerRl = (RelativeLayout) mView.findViewById(R.id.diyContainerRl);
        main_fl = (FrameLayout) mView.findViewById(R.id.main_fl);
        jingxian_iv = (ImageView) mView.findViewById(R.id.jingxian_iv);
        mScreenWidth = mView.getWindowManager().getDefaultDisplay().getWidth(); // 屏幕宽（像素，如：px）
        mScreenheight = mView.getWindowManager().getDefaultDisplay().getHeight(); // 屏幕高（像素，如：p）
        mFrameLayout = (FrameLayout) mView.findViewById(R.id.sceneFrameLayout);
        seekbar_ll = (LinearLayout) mView.findViewById(R.id.seekbar_ll);
        seekbar = (SeekBar) mView.findViewById(R.id.seekbar);
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mSeekNum = progress;
                if (!AppUtils.isEmpty(mSceneBg))
                    ImageUtil.changeLight02(sceneBgIv, mSceneBg, mSeekNum);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        pd2 = (ProgressBar) mView.findViewById(R.id.pd2);
        initImageLoader();
        product_lv = (ListView) mView.findViewById(R.id.product_lv);
        diy_lv = (ListView) mView.findViewById(R.id.diy_lv);
        mViews = new ArrayList<>();

    }

    @Override
    protected void handleMessage(int action, Object[] values) {

    }

    @Override
    protected void myHandleMessage(Message msg) {

    }

    /**
     * 保存
     */
    public void saveDiy() {
        if (!mView.isToken()) {
            mIntent = new Intent(mView, SelectSchemeActivity.class);
            mView.startActivityForResult(mIntent, Constance.FROMSCHEME);
        }
    }

    /**
     * 保存方案
     */
    private void saveData() {
        //                产品ID的累加
        StringBuffer goodsid = new StringBuffer();
        for (int i = 0; i < mView.mSelectedLightSA.size(); i++) {
            goodsid.append(mView.mSelectedLightSA.valueAt(i).getString(Constance.id) + "");
            if (i < mView.mSelectedLightSA.size() - 1) {
                goodsid.append(",");
            }
        }

        diyContainerRl.setVisibility(View.INVISIBLE);
        mView.select_ll.setVisibility(View.INVISIBLE);

        if (!AppUtils.isEmpty(mCurrentView))
            mCurrentView.setInEdit(false);

        //截图
//        final Bitmap imageData = ImageUtil.compressImage(ImageUtil.takeScreenShot(mView));
        final Bitmap imageData = ImageUtil.takeScreenShot(mView);
        final String imgUri=ScannerUtils.saveImageToGallery(mView, imageData, ScannerUtils.ScannerType.RECEIVER);
        mView.select_ll.setVisibility(View.VISIBLE);
        diyContainerRl.setVisibility(View.VISIBLE);
        mView.setShowDialog(true);
        mView.setShowDialog("正在保存中...");
        mView.showLoading();
        final String url = NetWorkConst.FANGANUPLOAD;//地址
        int id = MyShare.get(mView).getInt(Constance.USERCODEID);
        final Map<String, String> params = new HashMap<String, String>();
        params.put("name", mTitle);
        params.put("goods_id", goodsid.toString());
        params.put("content", mContent);
        params.put("style", mStyle);
        params.put("space", mSpace);
        params.put("parent_id", id + "");

        final String imageName = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date()) + ".png";
        new Thread(new Runnable() { //开启线程上传文件
            @Override
            public void run() {
                final String resultJson = uploadFile(imageData, url, params, imageName);
                //                final Result result = JSON.parseObject(resultJson, Result.class);
                //分享的操作
                mView.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mView.hideLoading();
                        if (AppUtils.isEmpty(resultJson)) {
                            MyToast.show(mView, "保存失败!");
                            return;
                        }

                        com.alibaba.fastjson.JSONObject object = JSON.parseObject(resultJson);
                        int isResult = object.getInteger(Constance.error_code);
                        if (isResult != 0) {
                            MyToast.show(mView, "保存失败!");
                            return;
                        }
                        String title = "来自 " + mTitle + " 方案的分享";
                        final String path = NetWorkConst.SHAREFANAN + object.getJSONObject(Constance.fangan).getString(Constance.id);
//                        final String imgPath = NetWorkConst.SCENE_HOST + object.getJSONObject(Constance.fangan).getString(Constance.path);
                        final String imgPath = imgUri;
                        Intent intent = new Intent(mView, ShareProgrammeActivity.class);
                        intent.putExtra(Constance.SHARE_PATH, path);
                        intent.putExtra(Constance.SHARE_IMG_PATH, imgPath);
                        intent.putExtra(Constance.TITLE, title);
                        intent.putExtra(Constance.id,object.getJSONObject(Constance.fangan).getString(Constance.id));
                        mView.startActivity(intent);
                    }
                });

            }
        }).start();
    }

    /**
     * 照相
     */
    public void goPhoto() {
        FileUtil.openImage(mView);
    }

    /**
     * 删除
     */
    public void goDetele() {
        mFrameLayout.removeView(mFrameLayout.findViewWithTag(IssueApplication.mLightIndex));
        mViews.remove(mCurrentView);
        mView.mSelectedLightSA.remove(IssueApplication.mLightIndex);
    }


    /**
     * 产品镜像
     */
    public void sendProductJinxianImage() {
        try {
            StickerView stickerView = (StickerView) mFrameLayout.findViewWithTag(IssueApplication.mLightIndex);
            stickerView.getFlipView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isGoCart = false;

    /**
     * 保存购物车
     */
    public void goShoppingCart() {
        if (!mView.isToken()) {
            if (mView.mSelectedLightSA.size() == 0) {
                MyToast.show(mView, "请选择产品");
                return;
            }
            mView.setShowDialog(true);
            mView.setShowDialog("正在加入购物车中...");
            mView.showLoading();
            isGoCart = false;
            for (int i = 0; i < mView.mSelectedLightSA.size(); i++) {
                JSONObject object = mView.mSelectedLightSA.valueAt(i);
                String id = object.getString(Constance.id);
                JSONArray propertieArray = object.getJSONArray(Constance.properties);
                if (propertieArray.length() == 0) {
                    sendGoShoppingCart(id, "", 1);
                } else {
                    if (!AppUtils.isEmpty(mView.mProductId) && id.equals(mView.mProductId)) {
                        sendGoShoppingCart(id, mView.mProperty, 1);
                    } else {
                        JSONArray attrsArray = propertieArray.getJSONObject(0).getJSONArray(Constance.attrs);
                        String propertie = attrsArray.getJSONObject(0).toString();
                        sendGoShoppingCart(id, propertie, 1);
                    }
                }
                if (i == mView.mSelectedLightSA.size() - 1) {
                    isGoCart = true;
                }
            }
        }
    }


    /**
     * 调节图片亮度
     */
    public void goBrightness() {
        seekbar_ll.setVisibility(View.VISIBLE);
    }

    private void sendGoShoppingCart(String product, String property, int mount) {
        mNetWork.sendShoppingCart(product, property, mount, this);
    }


    /**
     * android上传文件到服务器
     *
     * @param file       需要上传的文件
     * @param RequestURL 请求的rul
     * @return 返回响应的内容
     */
    private String uploadFile(Bitmap file, String RequestURL, Map<String, String> param, String imageName) {
        String result = null;
        String BOUNDARY = UUID.randomUUID().toString();  //边界标识   随机生成
        String PREFIX = "--", LINE_END = "\r\n";
        String CONTENT_TYPE = "multipart/form-data";   //内容类型
        String token = MyShare.get(UIUtils.getContext()).getString(Constance.TOKEN);
        // 显示进度框
        //      showProgressDialog();
        try {
            URL url = new URL(RequestURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(TIME_OUT);
            conn.setConnectTimeout(TIME_OUT);
            conn.setDoInput(true);  //允许输入流
            conn.setDoOutput(true); //允许输出流
            conn.setUseCaches(false);  //不允许使用缓存
            conn.setRequestMethod("POST");  //请求方式
            conn.setRequestProperty("Charset", CHARSET);  //设置编码
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("X-bocang-Authorization", token);
            conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);
            if (file != null) {
                /**
                 * 当文件不为空，把文件包装并且上传
                 */
                DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
                StringBuffer sb = new StringBuffer();

                String params = "";
                if (param != null && param.size() > 0) {
                    Iterator<String> it = param.keySet().iterator();
                    while (it.hasNext()) {
                        sb = null;
                        sb = new StringBuffer();
                        String key = it.next();
                        String value = param.get(key);
                        sb.append(PREFIX).append(BOUNDARY).append(LINE_END);
                        sb.append("Content-Disposition: form-data; name=\"").append(key).append("\"").append(LINE_END).append(LINE_END);
                        sb.append(value).append(LINE_END);
                        params = sb.toString();
                        dos.write(params.getBytes());
                    }
                }
                sb = new StringBuffer();
                sb.append(PREFIX);
                sb.append(BOUNDARY);
                sb.append(LINE_END);
                /**
                 * 这里重点注意：
                 * name里面的值为服务器端需要key   只有这个key 才可以得到对应的文件
                 * filename是文件的名字，包含后缀名的   比如:abc.png
                 */
                sb.append("Content-Disposition: form-data; name=\"").append("image").append("\"")
                        .append(";filename=\"").append(imageName).append("\"\n");
                sb.append("Content-Type: image/png");
                sb.append(LINE_END).append(LINE_END);
                dos.write(sb.toString().getBytes());
                InputStream is = ImageUtil.Bitmap2InputStream(file);
                byte[] bytes = new byte[1024];
                int len = 0;
                while ((len = is.read(bytes)) != -1) {
                    dos.write(bytes, 0, len);
                }


                is.close();
                //                dos.write(file);
                dos.write(LINE_END.getBytes());
                byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes();
                dos.write(end_data);

                dos.flush();
                /**
                 * 获取响应码  200=成功
                 * 当响应成功，获取响应的流
                 */

                int res = conn.getResponseCode();
                System.out.println("res=========" + res);
                if (res == 200) {
                    InputStream input = conn.getInputStream();
                    StringBuffer sb1 = new StringBuffer();
                    int ss;
                    while ((ss = input.read()) != -1) {
                        sb1.append((char) ss);
                    }
                    result = sb1.toString();
                } else {
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 选产品
     */
    public void selectProduct() {
        mIntent = new Intent(mView, SelectGoodsActivity.class);
        mIntent.putExtra(Constance.ISSELECTGOODS, true);
        mView.startActivityForResult(mIntent, Constance.FROMDIY);
    }

    /**
     * 选场景
     */
    public void selectScreen() {
        SelectScreenPopWindow popWindow = new SelectScreenPopWindow(mView);
        popWindow.setListener(new ISelectScreenListener() {
            @Override
            public void onSelectScreenChanged(int type) {
                switch (type) {
                    case 0:
                        selectSceneDatas();
                        break;
                    case 1:
                        FileUtil.getTakePhoto(mView);
                        break;
                    case 2:
                        FileUtil.getPickPhoto(mView);
                        break;
                }
            }
        });
        popWindow.onShow(main_fl);
    }

    /**
     * 选择场景
     */
    public void selectSceneDatas() {
        mIntent = new Intent(mView, SelectSceneActivity.class);
        mIntent.putExtra(Constance.ISSELECTSCRENES, true);
        mView.startActivityForResult(mIntent, Constance.FROMDIY02);
    }


    public void ActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == mView.RESULT_OK) { // 返回成功
            switch (requestCode) {
                case Constance.PHOTO_WITH_CAMERA: {// 拍照获取图片
                    String status = Environment.getExternalStorageState();
                        if (status.equals(Environment.MEDIA_MOUNTED)) { // 是否有SD卡
                            File imageFile = new File(IssueApplication.cameraPath, IssueApplication.imagePath + ".jpg");
                            if (imageFile.exists()) {
                                imageURL = "file://" + imageFile.toString();
                                IssueApplication.imagePath = null;
                                IssueApplication.cameraPath = null;
                                mView.mPath=imageURL;
                                displaySceneBg(mView.mPath, 0);
//                        File imageFile = new File(IssueApplication.cameraPath, IssueApplication.imagePath + ".jpg");
//                        if (imageFile.exists()) {
//                            imageURL = "file://" + imageFile.toString();
//                            IssueApplication.imagePath = null;
//                            IssueApplication.cameraPath = null;
//
//
//                            Intent intent = new Intent("com.android.camera.action.CROP");
//                            intent.setDataAndType(Uri.parse(imageURL), "image/*");
//                            intent.putExtra("crop", "true");
//                            intent.putExtra("aspectX", 1);
//                            intent.putExtra("aspectY", 0.8);
//                            intent.putExtra("outputX", 300);
//                            intent.putExtra("outputY", 300);
//                            intent.putExtra("scale", true);
//                            intent.putExtra("noFaceDetection", false);
//                            intent.putExtra("return-data", true);
//                            mView.startActivityForResult(intent, 3);
                            //                            displaySceneBg(imageURL, 0);
                        } else {
                            AppDialog.messageBox("读取图片失败！");
                        }
                    } else {
                        AppDialog.messageBox("没有SD卡！");
                    }
                    break;
                }
                case Constance.PHOTO_WITH_DATA: // 从图库中选择图片
                    // 照片的原始资源地址
                    imageURL = data.getData().toString();
                    displaySceneBg(imageURL, 1);
                    break;
            }
        } else if (requestCode == Constance.FROMDIY) {
            mView.mSelectType = 0;
            mView.switchProOrDiy();
        } else if (requestCode == Constance.FROMDIY02) {
            selectShowData();
            if (AppUtils.isEmpty(data))
                return;
            mView.mPath = data.getStringExtra(Constance.SCENE);
            if (!AppUtils.isEmpty(mView.mPath)) {
                displaySceneBg(mView.mPath, 0);
            }
        } else if (requestCode == Constance.FROMSCHEME) {
            if (AppUtils.isEmpty(data))
                return;
            mStyle = data.getStringExtra(Constance.style);
            mSpace = data.getStringExtra(Constance.space);
            mContent = data.getStringExtra(Constance.content);
            mTitle = data.getStringExtra(Constance.title);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(300);
                        mView.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                saveData();
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();


        }
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

    private DisplayImageOptions options;
    private ImageLoader imageLoader;
    private int mLightNumber = -1;// 点出来的灯的编号
    private int mLightId = 0;//点出来的灯的序号
    private int leftMargin = 0;

    private void displayCheckedGoods(final JSONObject goods) {
        if (AppUtils.isEmpty(goods))
            return;
        String path="";
        if(goods.getJSONObject(Constance.app_img)!=null)
        {
             path = goods.getJSONObject(Constance.app_img).getString(Constance.img);
        }else {
            MyToast.show(mView,"该产品尚无图片");
            return;
        }
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
                        addStickerView(loadedImage);

                    }

                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {
                        pd2.setVisibility(View.GONE);
                    }
                });
    }


    //添加表情
    private void addStickerView(Bitmap bitmap) {

        final StickerView stickerView = new StickerView(mView);
        stickerView.setBitmap(bitmap);
        stickerView.mLightCount = mLightNumber;
        stickerView.setOperationListener(new StickerView.OperationListener() {
            @Override
            public void onDeleteClick() {
                mViews.remove(stickerView);
                mFrameLayout.removeView(stickerView);
                mView.mSelectedLightSA.remove(IssueApplication.mLightIndex);
            }

            @Override
            public void onEdit(StickerView stickerView) {
                mCurrentView.setInEdit(false);
                mCurrentView = stickerView;
                mCurrentView.setInEdit(true);
            }

            @Override
            public void onTop(StickerView stickerView) {
                int position = mViews.indexOf(stickerView);
                if (position == mViews.size() - 1) {
                    return;
                }
                StickerView stickerTemp = (StickerView) mViews.remove(position);
                mViews.add(mViews.size(), stickerTemp);
            }
        });
        FrameLayout.LayoutParams lp2 = new FrameLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        stickerView.setTag(mLightNumber);
        mFrameLayout.addView(stickerView, lp2);
        mViews.add(stickerView);
        setCurrentEdit(stickerView);
    }

    /**
     * 设置当前处于编辑模式的贴纸
     */
    private void setCurrentEdit(StickerView stickerView) {
        if (mCurrentView != null) {
            mCurrentView.setInEdit(false);
        }
        mCurrentView = stickerView;
        stickerView.setInEdit(true);
    }


    /**
     * 添加图片
     *
     * @param bitmap
     */
    private void addImageView(Bitmap bitmap) {
        pd2.setVisibility(View.GONE);
        // 被点击的灯的编号加1
        mLightNumber++;
        IssueApplication.mLightIndex = mLightNumber;
        // 设置灯图的ImageView的初始宽高和位置
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                mScreenWidth / 3 * 2 / 3,
                (mScreenWidth / 3 * 2 / 3 * bitmap.getHeight()) / bitmap.getWidth());
        // 设置灯点击出来的位置
        if (mView.mSelectedLightSA.size() == 1) {
            leftMargin = mScreenWidth / 3 * 2 / 3;
        } else if (mView.mSelectedLightSA.size() == 2) {
            leftMargin = mScreenWidth / 3 * 2 / 3 * 2;
        } else if (mView.mSelectedLightSA.size() == 3) {
            leftMargin = 0;
        }
        lp.setMargins(mScreenWidth / 2 - (mScreenWidth / 3 * 2 / 6), 20, 0, 0);


        TouchView02 touchView = new TouchView02(mView);
        touchView.setLayoutParams(lp);
        touchView.setImageBitmap(bitmap);// 设置被点击的灯的图片
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


    private void displayCheckedGoods02(final JSONObject goods, final GoodsShape goodsShape) {
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

                        // 设置灯图的ImageView的初始宽高和位置
                        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(goodsShape.getWidth(), goodsShape.getHeight());
                        // 设置灯点击出来的位置
                        if (mView.mSelectedLightSA.size() == 1) {
                            leftMargin = mScreenWidth / 3 * 2 / 3;
                        } else if (mView.mSelectedLightSA.size() == 2) {
                            leftMargin = mScreenWidth / 3 * 2 / 3 * 2;
                        } else if (mView.mSelectedLightSA.size() == 3) {
                            leftMargin = 0;
                        }
                        //                        lp.setMargins(goodsShape.getY(),0, 0,Math.abs(goodsShape.getX()));
                        //                        lp.setMargins(goodsShape.getY(), mScreenheight - goodsShape.getX() - (goodsShape.getHeight()), 0, 0);
                        lp.setMargins(goodsShape.getX(), goodsShape.getY(), 0, 0);


                        //                        lp.setMargins(goodsShape.getY(),482, 0,0);
                        //                        if(goodsShape.getX()>0){
                        //                            lp.setMargins(goodsShape.getY(),0, 0,goodsShape.getX());
                        //                        }else{
                        //                            lp.setMargins(goodsShape.getY(),mScreenheight/2-goodsShape.getX(), 0,0);
                        //                        }
                        Matrix matrix = new Matrix();
                        // 设置旋转角度
                        matrix.setRotate(goodsShape.getRotate());
                        // 重新绘制Bitmap
                        loadedImage = Bitmap.createBitmap(loadedImage, 0, 0, loadedImage.getWidth(), loadedImage.getHeight(), matrix, true);


                        TouchView touchView = new TouchView(mView);
                        touchView.setLayoutParams(lp);
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
                        touchView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                MyToast.show(mView, mLightNumber + "");
                            }
                        });
                    }

                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {
                        pd2.setVisibility(View.GONE);
                    }
                });
    }

    private void displayCheckedGoods03(String path) {
        if (AppUtils.isEmpty(path))
            return;
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

                        //                        // 设置灯图的ImageView的初始宽高和位置
                        //                        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                        //                                mScreenWidth / 3 * 2 / 3,
                        //                                (mScreenWidth / 3 * 2 / 3 * loadedImage.getHeight()) / loadedImage.getWidth());
                        //                        // 设置灯点击出来的位置
                        //                        if (mView.mSelectedLightSA.size() == 1) {
                        //                            leftMargin = mScreenWidth / 3 * 2 / 3;
                        //                        } else if (mView.mSelectedLightSA.size() == 2) {
                        //                            leftMargin = mScreenWidth / 3 * 2 / 3 * 2;
                        //                        } else if (mView.mSelectedLightSA.size() == 3) {
                        //                            leftMargin = 0;
                        //                        }
                        //                        lp.setMargins(leftMargin, 0, 0, 0);
                        // 设置灯图的ImageView的初始宽高和位置
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
                        lp.setMargins(mScreenWidth / 2 - (mScreenWidth / 3 * 2 / 6), 20, 0, 0);


                        TouchView02 touchView = new TouchView02(mView);
                        touchView.setLayoutParams(lp);
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
     * 加载场景背景图
     *
     * @param path 场景img_url
     */
    private void displaySceneBg(String path, int scaleType) {
        if (scaleType == 0) {
            sceneBgIv.setScaleType(ImageView.ScaleType.FIT_XY);
        } else {
            sceneBgIv.setScaleType(ImageView.ScaleType.FIT_CENTER);
        }
        imageURL = path;
        imageLoader.displayImage(path, sceneBgIv, options,
                new ImageLoadingListener() {

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
                    public void onLoadingComplete(String imageUri, View view,
                                                  Bitmap loadedImage) {
                        pd2.setVisibility(View.GONE);


                        mSceneBg = ImageUtil.drawable2Bitmap(sceneBgIv.getDrawable());
                    }

                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {
                        pd2.setVisibility(View.GONE);
                    }
                });
    }

    /**
     * 判断是否满屏
     */
    public void selectIsFullScreen() {
        seekbar_ll.setVisibility(View.GONE);
        if (!isFullScreen) {
            diyContainerRl.setVisibility(View.INVISIBLE);
            mView.select_ll.setVisibility(View.GONE);
            isFullScreen = true;
        } else {
            diyContainerRl.setVisibility(View.VISIBLE);
            mView.select_ll.setVisibility(View.VISIBLE);
            isFullScreen = false;
        }
        if (AppUtils.isEmpty(mCurrentView))
            return;
        mCurrentView.setInEdit(false);
    }

    public void sendShoppingCart() {
        mNetWork.sendShoppingCart(this);
    }

    @Override
    public void onSuccessListener(String requestCode, JSONObject ans) {
        switch (requestCode) {
            case NetWorkConst.ADDCART:
                if (isGoCart) {
                    mView.hideLoading();
                    MyToast.show(mView, "加入购物车成功!");
                    sendShoppingCart();
                }
                break;
            case NetWorkConst.GETCART:
                if (ans.getJSONArray(Constance.goods_groups).length() > 0) {
                    IssueApplication.mCartCount = ans.getJSONArray(Constance.goods_groups)
                            .getJSONObject(0).getJSONArray(Constance.goods).length();
                } else {
                    IssueApplication.mCartCount = 0;
                }
                EventBus.getDefault().post(Constance.CARTCOUNT);
                break;
        }
    }

    @Override
    public void onFailureListener(String requestCode, JSONObject ans) {

    }

    private DiyProductInfoPopWindow mPopWindow;

    /**
     * 产品详情
     */
    public void getProductDetail() {
        mPopWindow = new DiyProductInfoPopWindow(mView, mView);
        final JSONObject jsonObject = mView.mSelectedLightSA.get(IssueApplication.mLightIndex);
        if (AppUtils.isEmpty(jsonObject)) {
            MyToast.show(mView, "请选择产品!");
            return;
        }


        mPopWindow.productObject = jsonObject;
        mPopWindow.initViewData();
        mPopWindow.onShow(main_fl);
        mPopWindow.setListener(new IDiyProductInfoListener() {
            @Override
            public void onDiyProductInfo(int type, String msg) {
                getShowProductType(jsonObject, type, msg);
            }
        });

    }

    private void getShowProductType(JSONObject jsonObject, int type, String msg) {
        String productId = jsonObject.getString(Constance.id);
        switch (type) {
            case 0://二维码

                String name = jsonObject.getString(Constance.name);
                String path = NetWorkConst.SHAREPRODUCT + productId;
                addImageView(ImageUtil.getTowCodeImage(ImageUtil.createQRImage(path, 150, 150), name));
                break;
            case 1://参数
                addImageView(ImageUtil.textAsBitmap(msg));
                break;
            case 2://Logo
                String logoPath = NetWorkConst.SHAREIMAGE_LOGO;
                displayCheckedGoods03(logoPath);
                break;
            case 3://产品卡
                String cardPath = NetWorkConst.WEB_PRODUCT_CARD + productId;
                displayCheckedGoods03(cardPath);
                break;
        }
    }

    /**
     * 背景图镜像
     */
    public void sendBackgroudImage() {
        Bitmap mBitmap = ImageUtil.drawable2Bitmap(sceneBgIv.getDrawable());
        if (mBitmap != null) {

            Bitmap temp = ImageUtil.convertBmp(mBitmap);
            if (temp != null) {
                sceneBgIv.setImageBitmap(temp);
                mBitmap.recycle();
            }
        }
    }

    public static JSONArray goodses = new JSONArray();

    public void selectShowData() {
        if (mView.mSelectType == 0) {
            goodses = IssueApplication.mSelectProducts;
            mAdapter = new ProAdapter();
            product_lv.setAdapter(mAdapter);
        } else {
            goodses = IssueApplication.mSelectScreens;
            mAdapter = new ProAdapter();
            product_lv.setAdapter(mAdapter);
        }
    }

    public void setRestart() {
        try {
            ((SingleTouchView) (mFrameLayout.findViewWithTag(IssueApplication.mLightIndex))).isScale = false;
        } catch (Exception e) {

        }

    }

    private class ProAdapter extends BaseAdapter {
        public ProAdapter() {
        }

        @Override
        public int getCount() {
            if (null == goodses)
                return 0;
            return goodses.length();

        }

        @Override
        public JSONObject getItem(int position) {
            if (null == goodses)
                return null;
            return goodses.getJSONObject(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(mView, R.layout.item_gridview_diy, null);


                holder = new ViewHolder();
                holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
                holder.delete_iv = (ImageView) convertView.findViewById(R.id.delete_iv);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.imageView.setImageResource(R.drawable.bg_default);
            holder.imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            try {
                if (mView.mSelectType == 0) {
                    ImageLoader.getInstance().displayImage(goodses.getJSONObject(position).getJSONObject(Constance.app_img).getString(Constance.img)
                            , holder.imageView);
                    holder.imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                } else {
                    holder.imageView.setImageResource(R.drawable.bg_default);
                    ImageLoader.getInstance().displayImage(NetWorkConst.SCENE_HOST +
                            goodses.getJSONObject(position).getJSONObject(Constance.scene).getString(Constance.original_img)
                            + "!400X400.png", holder.imageView);
                    holder.imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                }
            } catch (Exception e) {

            }


            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mView.mSelectType == 0) {
                        if (AppUtils.isEmpty(goodses.getJSONObject(position)))
                            return;
                        mView.mGoodsObject = goodses.getJSONObject(position);
                        displayCheckedGoods(mView.mGoodsObject);
                    } else {
                        if (AppUtils.isEmpty(goodses.getJSONObject(position)))
                            return;
                        mView.mPath = NetWorkConst.SCENE_HOST + goodses.getJSONObject(position).getJSONObject(Constance.scene).getString(Constance.original_img);
                        if (!AppUtils.isEmpty(mView.mPath)) {
                            displaySceneBg(mView.mPath, 0);
                        }
                    }

                    try {
                        ((SingleTouchView) (mFrameLayout.findViewWithTag(IssueApplication.mLightIndex))).isScale = false;
                    } catch (Exception e) {

                    }
                }
            });

            holder.delete_iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mView.mSelectType == 0) {
                        IssueApplication.mSelectProducts.delete(position);
                        notifyDataSetChanged();

                    } else {
                        IssueApplication.mSelectScreens.delete(position);
                        notifyDataSetChanged();
                    }

                }
            });

            return convertView;
        }

        class ViewHolder {
            ImageView imageView;
            ImageView delete_iv;
        }
    }


}
