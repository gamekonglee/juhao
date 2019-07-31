package bc.juhao.com.controller.user;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.widget.TextView;

import com.aliyun.iot.ilop.demo.DemoApplication;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;

import java.io.File;

import bc.juhao.com.R;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.cons.NetWorkConst;
import bc.juhao.com.controller.BaseController;
import bc.juhao.com.listener.INetworkCallBack;
import bc.juhao.com.ui.activity.EditValueActivity;
import bc.juhao.com.ui.activity.IssueApplication;
import bc.juhao.com.ui.activity.user.PerfectMydataActivity;
import bc.juhao.com.utils.FileUtil;
import bc.juhao.com.utils.ImageLoadProxy;
import bc.juhao.com.utils.NetWorkUtils;
import bc.juhao.com.utils.UIUtils;
import bc.juhao.com.utils.photo.CameraUtil;
import bocang.json.JSONObject;
import bocang.utils.AppDialog;
import bocang.utils.AppUtils;
import bocang.utils.CommonUtil;
import bocang.utils.MyLog;
import bocang.utils.MyToast;
import bocang.utils.PermissionUtils;
import cn.qqtheme.framework.picker.DatePicker;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author: Jun
 * @date : 2017/1/21 16:45
 * @description :
 */
public class PerfectMydataController extends BaseController implements OnItemClickListener, INetworkCallBack {
    private PerfectMydataActivity mView;
    private Intent mIntent;
    private TextView name_tv, sex_tv, birthday_tv, phone_tv, telephone_tv, email_tv;
    private AlertView mSexView;
    private AlertView mHeadView;
    private CircleImageView head_iv;
    private int sexType = 0;
    private String imageURL;

    public PerfectMydataController(PerfectMydataActivity v) {
        mView = v;
        initView();
        initViewData();
    }

    private void initViewData() {
        JSONObject mUserObject = DemoApplication.mUserObject;
        if(AppUtils.isEmpty(mUserObject)) return;
        if(!AppUtils.isEmpty(mUserObject.getString(Constance.avatar))){
            String avatar = NetWorkConst.SCENE_HOST + mUserObject.getString(Constance.avatar);
            if (!AppUtils.isEmpty(avatar))
                ImageLoadProxy.displayHeadIcon(avatar, head_iv);
        }

        String nickname = mUserObject.getString(Constance.nickname);
        name_tv.setText(nickname);
        int sex = mUserObject.getInt(Constance.gender);
        sexType = sex;
        sex_tv.setText(sex == 0 ? "男" : "女");
        birthday_tv.setText(mUserObject.getString(Constance.birthday));

    }


    private void initView() {
        name_tv = (TextView) mView.findViewById(R.id.name_tv);
        sex_tv = (TextView) mView.findViewById(R.id.sex_tv);
        birthday_tv = (TextView) mView.findViewById(R.id.birthday_tv);
        phone_tv = (TextView) mView.findViewById(R.id.phone_tv);
        telephone_tv = (TextView) mView.findViewById(R.id.telephone_tv);
        email_tv = (TextView) mView.findViewById(R.id.email_tv);
        mSexView = new AlertView(null, null, "取消", null,
                Constance.SEXTYPE,
                mView, AlertView.Style.ActionSheet, this);
        mHeadView = new AlertView(null, null, "取消", null,
                Constance.CAMERTYPE,
                mView, AlertView.Style.ActionSheet, this);
        head_iv = (CircleImageView) mView.findViewById(R.id.head_iv);
    }

    /**
     * 编辑页面
     *
     * @param title
     * @param type
     */
    public void setIntent(String title, int type) {
        mIntent = new Intent(mView, EditValueActivity.class);
        mIntent.putExtra(Constance.TITLE, title);
        mIntent.putExtra(Constance.CODE, type);
        mView.startActivityForResult(mIntent, type);
    }

    public void ActivityResult(int requestCode, int resultCode, final Intent data) {
        if (camera != null)
            camera.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 001) {
            String value = data.getStringExtra(Constance.VALUE);
            if (AppUtils.isEmpty(value))
                return;
            name_tv.setText(value);
        } else if (resultCode == 003) {
            String value = data.getStringExtra(Constance.VALUE);
            if (AppUtils.isEmpty(value))
                return;
            birthday_tv.setText(value);
        } else if (resultCode == 004) {
            String value = data.getStringExtra(Constance.VALUE);
            if (AppUtils.isEmpty(value))
                return;
            phone_tv.setText(value);
        } else if (resultCode == 005) {
            String value = data.getStringExtra(Constance.VALUE);
            if (AppUtils.isEmpty(value))
                return;
            telephone_tv.setText(value);
        } else if (resultCode == 006) {
            String value = data.getStringExtra(Constance.VALUE);
            if (AppUtils.isEmpty(value))
                return;
            email_tv.setText(value);
        }else if(resultCode==-1){
            switch (requestCode) {
                case Constance.PHOTO_WITH_CAMERA: {// 拍照获取图片
                    String status = Environment.getExternalStorageState();
                    if (status.equals(Environment.MEDIA_MOUNTED)) { // 是否有SD卡
                        File imageFile = new File(DemoApplication.cameraPath, DemoApplication.imagePath + ".jpg");
                        imageURL = "file://"+imageFile;
                        final Uri uri=Uri.parse("file://"+imageFile);
                        head_iv.setImageURI(uri);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                final String resultJson = NetWorkUtils.uploadFile(head_iv.getDrawable(), NetWorkConst.UPLOADAVATAR, null, uri.toString());
                                //                            //分享的操作
                                mView.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                    }
                                });
                            }
                        }).start();

//                        File imageFile = new File(IssueApplication.cameraPath, IssueApplication.imagePath + ".jpg");
//                        if (imageFile.exists()) {
////                            imageURL = "file://" + imageFile.toString();
////                            IssueApplication.imagePath = null;
////                            IssueApplication.cameraPath = null;
////                            mView.mPath=imageURL;
////                            displaySceneBg(mView.mPath, 0);
//                        if (imageFile.exists()) {
//                            imageURL = "file://" + imageFile.toString();
//                            IssueApplication.imagePath = null;
//                            IssueApplication.cameraPath = null;
//
//
//                            Uri uri=FileProvider.getUriForFile(mView, "com.bocang.juhao.fileprovider", new File(imageFile.toString()));
//                            Intent intent = new Intent("com.android.camera.action.CROP");
//                            intent.setDataAndType(uri, "image/*");
//                            intent.putExtra("scale", true);// 是否保留比例
//                            intent.putExtra("scaleUpIfNeeded", true);//黑边
//                            intent.putExtra("crop", "true");//发送裁剪信号
//                            intent.putExtra("aspectX", 1);
//                            intent.putExtra("aspectY", 0.8);
//                            intent.putExtra("outputX", 300);
//                            intent.putExtra("outputY", 300);
//                            intent.putExtra("return-data", false);
//                            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageURL);//URI 将URI指向相应的file:///…，
//                            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
//                            intent.putExtra("noFaceDetection", true); // no face detection
//                            mView.startActivityForResult(intent, Constance.FLAG_UPLOAD_IMAGE_CUT);

//                        } else {
//                            AppDialog.messageBox("读取图片失败！");
//                        }
//                    } else {
//                        AppDialog.messageBox("没有SD卡！");
//                    }
                }
                break;
                }
                case Constance.PHOTO_WITH_DATA: // 从图库中选择图片
                    // 照片的原始资源地址
                    imageURL = data.getData().toString();
                    head_iv.setImageURI(data.getData());
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            final String resultJson = NetWorkUtils.uploadFile(head_iv.getDrawable(), NetWorkConst.UPLOADAVATAR, null, imageURL.toString());
                            //                            //分享的操作
                            mView.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                }
                            });
                        }
                    }).start();
                    break;
                case Constance.FLAG_UPLOAD_IMAGE_CUT:
                    final Uri uri=data.getData();
                    head_iv.setImageURI(uri);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            final String resultJson = NetWorkUtils.uploadFile(head_iv.getDrawable(), NetWorkConst.UPLOADAVATAR, null, uri.toString());
                            //                            //分享的操作
                            mView.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                }
                            });
                        }
                    }).start();
                    break;
            }
        }else if(requestCode== Constance.FLAG_UPLOAD_IMAGE_CUT){
            final Uri uri=data.getData();
            head_iv.setImageURI(uri);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final String resultJson = NetWorkUtils.uploadFile(head_iv.getDrawable(), NetWorkConst.UPLOADAVATAR, null, uri.toString());
                    //                            //分享的操作
                    mView.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                        }
                    });
                }
            }).start();
        }
    }

    @Override
    public void onItemClick(Object o, final int position) {
        if (mSexView.toString().equals(o.toString())) {
            selectSex(position);
        } else if (mHeadView.toString().equals(o.toString())) {
            openImage(position);

        }
    }

    public void openImage(int position) {
        switch (position) {
            case 0:
                if (camera != null)
                    PermissionUtils.requestPermission(mView, PermissionUtils.CODE_CAMERA, new PermissionUtils.PermissionGrant() {
                        @Override
                        public void onPermissionGranted(int requestCode) {
                            camera.onDlgCameraClick();
                        }
                    });

                break;
            case 1:
                if (camera != null)
                    PermissionUtils.requestPermission(mView, PermissionUtils.CODE_READ_EXTERNAL_STORAGE, new PermissionUtils.PermissionGrant() {
                        @Override
                        public void onPermissionGranted(int requestCode) {
                            camera.onDlgPhotoClick();
                        }
                    });
                break;
        }
    }

    /**
     * 选择性别
     *
     * @param position
     */
    private void selectSex(int position) {
        switch (position) {
            case 0:
                sex_tv.setText("男");
                sexType = 0;
                break;
            case 1:
                sex_tv.setText("女");
                sexType = 1;
                break;
        }
    }

    /**
     * 选择性别
     */
    public void selectSex() {
        mSexView.show();
    }

    /**
     * 选择生日
     */
    public void selectBirthday() {
        DatePicker picker = new DatePicker(mView);
        picker.setRange(CommonUtil.getYear(), 1920);//年份范围
        picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
            @Override

            public void onDatePicked(String year, String month, String day) {
                birthday_tv.setText(year + "-" + month + "-" + day);
            }

        });
        picker.show();
    }

    /**
     * 修改用户信息
     */
    public void sendUpdateUser() {
        String birthday = birthday_tv.getText().toString();
        String nicName = name_tv.getText().toString();
        String values = "";
        int gender = sexType;
        if (AppUtils.isEmpty(birthday)) {
            MyToast.show(mView, "请选择出生日期");
            return;
        }
        if (AppUtils.isEmpty(gender)) {
            MyToast.show(mView, "请选择性别");
            return;
        }
        if (AppUtils.isEmpty(nicName)) {
            MyToast.show(mView, "请输入昵称");
            return;
        }

        mView.setShowDialog(true);
        mView.setShowDialog("正在保存中..");
        mView.showLoading();

        mNetWork.sendUpdateUser(values,nicName, birthday, gender, this);
    }

    private CameraUtil camera;

    /**
     * 头像
     */
    public void setHead() {

        FileUtil.openImage(mView);

        if (camera == null) {
            camera = new CameraUtil(mView, new CameraUtil.CameraDealListener() {
                @Override
                public void onCameraTakeSuccess(String path) {
                    MyLog.e("onCameraTakeSuccess: " + path);
                    camera.cropImageUri(1, 1, 256);
                }

                @Override
                public void onCameraPickSuccess(String path) {
                    MyLog.e("onCameraPickSuccess: " + path);
                    Uri uri ;
                    if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N){
                    uri= FileProvider.getUriForFile(mView, "com.bocang.juhao.fileprovider", new File(path));
                    }else {
                        uri = Uri.parse("file://" + path);
                    }
                    camera.cropImageUri(uri, 1, 1, 256);
                }

                @Override
                public void onCameraCutSuccess(final String uri) {
                    File file = new File(uri);
                    Uri uriTemp;
                    if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N){
                        uriTemp= FileProvider.getUriForFile(mView, "com.bocang.juhao.fileprovider", new File(uri));
                    }else {
                        uriTemp = Uri.parse("file://" + uri);
                    }
                    head_iv.setImageURI(uriTemp);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            final String resultJson = NetWorkUtils.uploadFile(head_iv.getDrawable(), NetWorkConst.UPLOADAVATAR, null, uri.toString());
                            //                            //分享的操作
                            mView.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                }
                            });
                        }
                    }).start();
                }
            });
        }

//        mHeadView.show();
    }


    @Override
    protected void handleMessage(int action, Object[] values) {

    }

    @Override
    protected void myHandleMessage(Message msg) {

    }


    @Override
    public void onSuccessListener(String requestCode, JSONObject ans) {
        mView.hideLoading();
        switch (requestCode) {
            case NetWorkConst.UPDATEPROFILE:
                AppDialog.messageBox(UIUtils.getString(R.string.update_save));
                mView.finish();
                break;
        }
    }

    @Override
    public void onFailureListener(String requestCode, JSONObject ans) {
        mView.hideLoading();
        if (null == mView || mView.isFinishing())
            return;
        if (AppUtils.isEmpty(ans)) {
            AppDialog.messageBox(UIUtils.getString(R.string.server_error));
            return;
        }
        AppDialog.messageBox(ans.getString(Constance.error_desc));
    }
}
