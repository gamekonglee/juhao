package bc.juhao.com.ui.activity.user;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;

import java.io.FileNotFoundException;
import java.util.Hashtable;

import bc.juhao.com.R;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.ui.activity.product.ProDetailActivity;
import bc.juhao.com.utils.RGBLuminanceSource;
import bocang.utils.MyToast;
import bocang.utils.PermissionUtils;
import bocang.view.BaseActivity;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by bocang02 on 16/10/13.
 */

public class SimpleScannerActivity extends BaseActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;
    private Button qrcode_fileAlbum;
    public static final int REQUEST_CODE = 200;
    private Intent mIntent;

    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {

    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_simplescanner);
        //沉浸式状态栏
//        setColor(this, getResources().getColor(R.color.colorPrimary));
        mScannerView = (ZXingScannerView) findViewById(R.id.qrcode_ZXingScannerView);
        qrcode_fileAlbum = getViewAndClick(R.id.qrcode_fileAlbum);

        PermissionUtils.requestPermission(SimpleScannerActivity.this, PermissionUtils.CODE_CAMERA, new PermissionUtils.PermissionGrant() {
            @Override
            public void onPermissionGranted(int requestCode) {

            }
        });

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtils.requestPermissionsResult(this, requestCode, permissions, grantResults, new PermissionUtils.PermissionGrant() {
            @Override
            public void onPermissionGranted(int requestCode) {

            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.qrcode_fileAlbum:
                Intent innerIntent = new Intent(Intent.ACTION_GET_CONTENT); //"android.intent.action.GET_CONTENT"
                innerIntent.addCategory(Intent.CATEGORY_OPENABLE);
                innerIntent.setType("image/*");
                innerIntent.putExtra("return-data", true);
                startActivityForResult(innerIntent, REQUEST_CODE);
                break;

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void handleResult(Result result) {
        mIntent = new Intent(this, ProDetailActivity.class);
        int productId = Integer.parseInt(result.getText());
        mIntent.putExtra(Constance.product, productId);
        this.startActivity(mIntent);
        this.finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            //读取相册
            Uri uri = data.getData();
            ContentResolver cr = this.getContentResolver();
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                Result result = scanningImage(bitmap);
                if (result != null) {
                    MyToast.show(this, result.getText().toString());
                } else {
                    Toast.makeText(this, "没有数据", Toast.LENGTH_SHORT).show();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(this, "没有数据", Toast.LENGTH_SHORT).show();
            }
        }
    }


    /**
     * 扫描二维码图片的方法
     *
     * @return
     */
    public Result scanningImage(Bitmap bitmap) {
        Hashtable<DecodeHintType, String> hints = new Hashtable<DecodeHintType, String>();
        hints.put(DecodeHintType.CHARACTER_SET, "UTF8"); //设置二维码内容的编码

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true; // 先获取原大小
        options.inJustDecodeBounds = false; // 获取新的大小
        int sampleSize = (int) (options.outHeight / (float) 200);
        if (sampleSize <= 0)
            sampleSize = 1;
        options.inSampleSize = sampleSize;
        RGBLuminanceSource source = new RGBLuminanceSource(bitmap);
        BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
        QRCodeReader reader = new QRCodeReader();
        try {
            return reader.decode(bitmap1, hints);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
