package bc.juhao.com.ui.activity.programme;

import android.content.Intent;
import android.view.View;

import bc.juhao.com.R;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.utils.ImageLoadProxy;
import bocang.view.BaseActivity;
import uk.co.senab.photoview.PhotoView;

/**
 * @author: Jun
 * @date : 2017/3/28 9:40
 * @description :
 */
public class ImageDetailActivity extends BaseActivity {
    private String mImagePath="";
    private PhotoView photo_iv;
    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {

    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_image_detail);
        photo_iv = (PhotoView)findViewById(R.id.photo_iv);
        ImageLoadProxy.displayImage(mImagePath,photo_iv);
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        mImagePath=intent.getStringExtra(Constance.photo);
    }

    @Override
    protected void onViewClick(View v) {

    }
}
