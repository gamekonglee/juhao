package bc.juhao.com.ui.activity.product;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aliyun.iot.ilop.demo.DemoApplication;
import com.donkingliang.imageselector.utils.ImageSelectorUtils;
import com.lib.common.hxp.view.GridViewForScrollView;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import bc.juhao.com.R;
import bc.juhao.com.bean.PostImageVideoBean;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.controller.product.PostedImageController;
import bc.juhao.com.ui.activity.IssueApplication;
import bc.juhao.com.ui.activity.user.SuccessVideoActivity;
import bc.juhao.com.ui.activity.user.VideoShotActivity;
import bc.juhao.com.ui.adapter.GridViewPostAdapter;
import bc.juhao.com.utils.FileUtil;
import bc.juhao.com.utils.ImageLoadProxy;
import bc.juhao.com.utils.ImageUtil;
import bocang.utils.AppDialog;
import bocang.utils.AppUtils;
import bocang.utils.LogUtils;
import bocang.utils.MyToast;
import bocang.view.BaseActivity;

import static bc.juhao.com.cons.Constance.PHOTO_WITH_CAMERA;

/**
 * @author Jun
 * @time 2017/12/10  13:31
 * @desc ${TODD}
 */

public class PostedImageActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private PostedImageController mController;
    public String mProductId="";
    public String mOrderId="";
    public String mType;

    // 图片 九宫格
    private GridViewForScrollView gv;
    // 图片 九宫格适配器
    private GridViewPostAdapter gvAdapter;

    // 用于保存图片资源文件
    public List<PostImageVideoBean> lists = new ArrayList<PostImageVideoBean>();
    public List<File> files=new ArrayList<>();
    private RelativeLayout save_rl;
    private ImageView iv_goods;
    private TextView tv_name;
    private TextView tv_property;
    private String goods;
    private String property;
    private String img;
    private ArrayList<String> images;
//    private boolean[] isVideo;
    private String path;
    public List<Bitmap> upLoadBitmap;
    public List<File> uploadFile;

    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {
        mController = new PostedImageController(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void initView() {
        setContentView(R.layout.activity_posted_image);
        setColor(this,Color.WHITE);
        gv = (GridViewForScrollView) findViewById(R.id.imageGv);
        iv_goods = findViewById(R.id.iv_image);
        tv_name = findViewById(R.id.tv_name);
        tv_property = findViewById(R.id.tv_property);
        gvAdapter = new GridViewPostAdapter(this, lists);
        gv.setOnItemClickListener(this);
        gv.setAdapter(gvAdapter);
        gvAdapter.setList(lists);
        save_rl = getViewAndClick(R.id.save_rl);
        if (!AppUtils.isEmpty(property)) {
            tv_property.setText(property);
            tv_property.setVisibility(View.VISIBLE);
        } else {
            tv_property.setVisibility(View.GONE);
        }
        tv_name.setText(goods);
        ImageLoadProxy.displayImage(img,iv_goods);
//        isVideo = new boolean[]{false,false,false};
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        mProductId=intent.getStringExtra(Constance.id);
        mOrderId=intent.getStringExtra(Constance.order_id);
        goods = intent.getStringExtra(Constance.goods);
        property = intent.getStringExtra(Constance.property);
        img = intent.getStringExtra(Constance.img);
        images=new ArrayList<>();
        lists=new ArrayList<>();
    }

    @Override
    protected void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.save_rl://发布
                upLoadBitmap = new ArrayList<>();
                uploadFile = new ArrayList<>();

                for(int i=0;i<lists.size();i++){
                if(lists.get(i).isVideo){
                    uploadFile.add(new File(lists.get(i).path));
                }else {
                    upLoadBitmap.add(lists.get(i).bitmap);
                }
                }
                mController.reviceOrder();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                1);
        if (position == getDataSize()) {// 点击“+”号位置添加图片
            //            Album.startAlbum(PostedImageActivity.this, 100, 8  // 指定选择数量。
            //                    , ContextCompat.getColor(PostedImageActivity.this, R.color.colorPrimary)        // 指定Toolbar的颜色。
            //                    , ContextCompat.getColor(PostedImageActivity.this.getApplicationContext(),
            //                            R.color.colorPrimaryDark));  // 指定状态栏的颜色。
            //图片剪裁的一些设置
            UCrop.Options options = new UCrop.Options();
            //图片生成格式
            options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
            //图片压缩比
            options.setCompressionQuality(80);
//
//            new PickConfig.Builder(PostedImageActivity.this)
//                    .maxPickSize(9)//最多选择几张
//                    .isneedcamera(true)//是否需要第一项是相机
//                    .spanCount(4)//一行显示几张照片
//                    .actionBarcolor(Color.parseColor("#EE7600"))//设置toolbar的颜色
//                    .statusBarcolor(Color.parseColor("#EE7600")) //设置状态栏的颜色(5.0以上)
//                    .isneedcrop(false)//受否需要剪裁
//                    .setUropOptions(options) //设置剪裁参数
//                    .isSqureCrop(true) //是否是正方形格式剪裁
//                    .pickMode(PickConfig.MODE_MULTIP_PICK)//单选还是多选
//                    .build();
            //限数量的多选(比喻最多9张)

            FileUtil.openSunImage(this);
        } else {
            if(lists.get(position).isVideo){
             Intent intent=new Intent(this, SuccessVideoActivity.class);
             intent.putExtra(Constance.is_look,true);
             intent.putExtra("text",path);
             startActivity(intent);
            }else {
            dialog(position);
            }
        }
    }

    @Override
    protected void onDestroy() {
        //删除文件夹及文件
        FileUtil.deleteDir();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constance.REQUEST_CODE && data != null) {
            ArrayList<String> temps=data.getStringArrayListExtra(
                    ImageSelectorUtils.SELECT_RESULT);
            if(lists!=null&&temps.size()+lists.size()>3){
                MyToast.show(this,"上传图片最多3张");
                return;
            }
            images.addAll(temps);
            //获取选择器返回的数据
            for (int i = 0; i < temps.size(); i++) {
                PostImageVideoBean postImageVideoBean=new PostImageVideoBean();
                postImageVideoBean.isVideo=false;
                postImageVideoBean.bitmap=ImageUtil.adjustImage(this,temps.get(i));
                lists.add(postImageVideoBean);
            }
            // 更新GrideView
            gvAdapter.setList(lists);
        }else if(requestCode==PHOTO_WITH_CAMERA){
            String status = Environment.getExternalStorageState();
            if (status.equals(Environment.MEDIA_MOUNTED)) { // 是否有SD卡
                File imageFile = new File(DemoApplication.cameraPath, DemoApplication.imagePath + ".jpg");
                if (imageFile.exists()) {
//                    String imageURL = "file://" + imageFile.toString();
                    String imageURL =  imageFile.toString();
                    if(images!=null&&images.size()>=3){
                        MyToast.show(this,"上传图片最多3张");
                        return;
                    }
                    images.add(imageURL);
                    PostImageVideoBean postImageVideoBean=new PostImageVideoBean();
                    postImageVideoBean.isVideo=false;
                    postImageVideoBean.bitmap=BitmapFactory.decodeFile(imageURL);
                    lists.add(postImageVideoBean);
                    gvAdapter.setList(lists);
                    DemoApplication.imagePath = null;
                    DemoApplication.cameraPath = null;
                } else {
                    AppDialog.messageBox("读取图片失败！");
                }
            } else {
                AppDialog.messageBox("没有SD卡！");
            }
        }else if(requestCode==300&&resultCode==300){
            if(images!=null&&images.size()>=3){
                MyToast.show(this,"上传图片最多3张");
                return;
            }
            path = data.getStringExtra(Constance.path);
            files.add(new File(path));
            LogUtils.logE("path", path);
            MediaMetadataRetriever media = new MediaMetadataRetriever();
            media.setDataSource(path);
//            View view=View.inflate(this,R.layout.video_start,null);
//            View imageView=view.findViewById(R.id.iv_img);
//            imageView.setBackground(new BitmapDrawable(bitmap));
//            Bitmap temp=ImageUtil.loadBitmapFromView(view);
            PostImageVideoBean postImageVideoBean=new PostImageVideoBean();
            postImageVideoBean.isVideo=true;
            postImageVideoBean.bitmap=media.getFrameAtTime();
            postImageVideoBean.path=path;
            lists.add(postImageVideoBean);
            DemoApplication.imagePath = null;
            DemoApplication.cameraPath = null;
            gvAdapter.setList(lists);
        }else if(requestCode==300&&resultCode==100){
            startActivityForResult(new Intent(this, VideoShotActivity.class),300);
        }
    }

    /*
  * Dialog对话框提示用户删除操作
  * position为删除图片位置
  */
    protected void dialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("确认移除已添加图片吗？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                lists.remove(position);
                gvAdapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private int getDataSize() {
        return lists == null ?0 : lists.size();
    }
}
