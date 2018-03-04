package bc.juhao.com.ui.activity.product;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;

import com.lib.common.hxp.view.GridViewForScrollView;
import com.wq.photo.widget.PickConfig;
import com.yalantis.ucrop.UCrop;

import java.util.ArrayList;
import java.util.List;

import bc.juhao.com.R;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.controller.product.PostedImageController;
import bc.juhao.com.utils.FileUtil;
import bocang.view.BaseActivity;

/**
 * @author Jun
 * @time 2017/12/10  13:31
 * @desc ${TODD}
 */

public class PostedImageActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private PostedImageController mController;
    public int mProductId=0;
    public String mOrderId="";
    public String mType;

    // 图片 九宫格
    private GridViewForScrollView gv;
    // 图片 九宫格适配器
    private GridViewAdapter gvAdapter;

    // 用于保存图片资源文件
    public List<Bitmap> lists = new ArrayList<Bitmap>();
    private RelativeLayout save_rl;

    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {
        mController = new PostedImageController(this);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_posted_image);

        gv = (GridViewForScrollView) findViewById(R.id.imageGv);
        gvAdapter = new GridViewAdapter(this, lists);
        gv.setOnItemClickListener(this);
        gv.setAdapter(gvAdapter);
        gvAdapter.setList(lists);
        save_rl = getViewAndClick(R.id.save_rl);
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        mProductId=intent.getIntExtra(Constance.id,0);
        mOrderId=intent.getStringExtra(Constance.order_id);
    }

    @Override
    protected void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.save_rl://发布
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

            new PickConfig.Builder(PostedImageActivity.this)
                    .maxPickSize(9)//最多选择几张
                    .isneedcamera(true)//是否需要第一项是相机
                    .spanCount(4)//一行显示几张照片
                    .actionBarcolor(Color.parseColor("#EE7600"))//设置toolbar的颜色
                    .statusBarcolor(Color.parseColor("#EE7600")) //设置状态栏的颜色(5.0以上)
                    .isneedcrop(false)//受否需要剪裁
                    .setUropOptions(options) //设置剪裁参数
                    .isSqureCrop(true) //是否是正方形格式剪裁
                    .pickMode(PickConfig.MODE_MULTIP_PICK)//单选还是多选
                    .build();
        } else {
            dialog(position);
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
        //        if (resultCode == PostedImageActivity.RESULT_OK) { // 返回成功
        //            switch (requestCode) {
        //
        //                case 100:
        //                    List<String> mList = Album.parseResult(data);
        //                    for (int i = 0; i < mList.size(); i++) {
        //                        lists.add(BitmapFactory.decodeFile(mList.get(i)));
        //                    }
        //                    // 更新GrideView
        //                    gvAdapter.setList(lists);
        //                    break;
        //            }
        //        }
        if (resultCode == RESULT_OK && requestCode == PickConfig.PICK_REQUEST_CODE) {
            //在data中返回 选择的图片列表
            ArrayList<String> paths = data.getStringArrayListExtra("data");
            for (int i = 0; i < paths.size(); i++) {
                lists.add(BitmapFactory.decodeFile(paths.get(i)));
            }
            // 更新GrideView
            gvAdapter.setList(lists);
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
        return lists == null ? 0 : lists.size();
    }
}
