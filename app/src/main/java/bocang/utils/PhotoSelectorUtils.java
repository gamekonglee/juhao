package bocang.utils;

import android.app.Activity;

import com.donkingliang.imageselector.ClipImageActivity;
import com.donkingliang.imageselector.ImageSelectorActivity;

import java.util.ArrayList;


/**
 * Created by gamekonglee on 2018/4/2.
 */

public class PhotoSelectorUtils {
    /**
     * 打开相册，选择图片,可多选,不限数量。
     *
     * @param activity
     * @param requestCode
     */
    public static void openPhoto(Activity activity, int requestCode) {
        openPhoto(activity, requestCode, false,false, 9,null);
    }

    /**
     * 打开相册，选择图片,可多选,限制最大的选择数量。
     *
     * @param activity
     * @param requestCode
     * @param isSingle 是否单选
     * @param maxSelectCount 图片的最大选择数量，小于等于0时，不限数量，isSingle为false时才有用。
     */
    public static void openPhoto(Activity activity, int requestCode,
                                 boolean isSingle,boolean isUseCamera, int maxSelectCount,ArrayList<String > list) {
        ImageSelectorActivity.openActivity(activity, requestCode, isSingle, isUseCamera,maxSelectCount,list);
    }

/**
 * 打开相册，单选图片并剪裁。
 *
 * @param activity
 * @param requestCode
 */

    public static void openPhotoAndClip(Activity activity, int requestCode, boolean isUserCamera, ArrayList<String > list) {
        ClipImageActivity.openActivity(activity, requestCode,isUserCamera,list);
    }
}