package bc.juhao.com.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.util.List;

import bc.juhao.com.R;
import bc.juhao.com.cons.NetWorkConst;


/**
 * @author Jun
 * @time 2017/11/26  13:37
 * @desc ${TODD}
 */

public class ImageDetailAdapter  extends PagerAdapter {
    private List<String> imageSrc;
    private Context context;
    private final DisplayImageOptions options;

    public ImageDetailAdapter(Context context, List<String> imageSrc){
        this.context=context;
        this.imageSrc=imageSrc;
        //默认为false
        // 图片显示出来的效果
        options = new DisplayImageOptions.Builder()
                .cacheOnDisk(true) //默认为false
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new FadeInBitmapDisplayer(500)) // 图片显示出来的效果
                .showStubImage(R.drawable.bg_default)
                .showImageForEmptyUri(R.drawable.bg_default)
                .cacheInMemory()
                .build();
    }

    @Override
    public int getCount() {
        return imageSrc.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView photoView=new ImageView(context);
        String imageUrl= NetWorkConst.SCENE_HOST+imageSrc.get(position);
        ImageLoader.getInstance().displayImage(imageUrl,photoView,options);
        container.addView(photoView,ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        return photoView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
