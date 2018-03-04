package bc.juhao.com.controller.programme;

import android.content.Intent;
import android.os.Environment;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.baiiu.filter.DropDownMenu;
import com.baiiu.filter.interfaces.OnFilterDoneListener;
import com.lib.common.hxp.view.PullToRefreshLayout;
import com.lib.common.hxp.view.PullableGridView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import bc.juhao.com.R;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.cons.NetWorkConst;
import bc.juhao.com.controller.BaseController;
import bc.juhao.com.listener.INetworkCallBack;
import bc.juhao.com.ui.activity.IssueApplication;
import bc.juhao.com.ui.activity.programme.SelectSceneActivity;
import bc.juhao.com.ui.adapter.SceneDropMenuAdapter;
import bc.juhao.com.utils.FileUtil;
import bc.juhao.com.utils.UIUtils;
import bocang.json.JSONArray;
import bocang.json.JSONObject;
import bocang.utils.AppDialog;
import bocang.utils.AppUtils;
import bocang.utils.MyToast;

/**
 * @author: Jun
 * @date : 2017/2/18 12:00
 * @description :
 */
public class SelectSceneController extends BaseController implements INetworkCallBack, OnFilterDoneListener, PullToRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener {
    private SelectSceneActivity mView;
    private DropDownMenu dropDownMenu;
    private JSONArray sceneAllAttrs;
    private PullToRefreshLayout mPullToRefreshLayout;
    private ProAdapter mProAdapter;
    private PullableGridView order_sv;
    private int page = 1;
    private JSONArray goodses;
    private boolean initFilterDropDownView;
    private String imageURL = "";
    private Intent mIntent;
    private String keyword;
    private ProgressBar pd;


    public SelectSceneController(SelectSceneActivity v) {
        mView = v;
        initView();
        initViewData();
    }


    private void initViewData() {
        page = 1;
        initFilterDropDownView = true;
        sendSceneList(page);
        sendSceneType();
        pd.setVisibility(View.VISIBLE);
    }

    private void initView() {
        dropDownMenu = (DropDownMenu) mView.findViewById(R.id.dropDownMenu);
        mPullToRefreshLayout = ((PullToRefreshLayout) mView.findViewById(R.id.mFilterContentView));
        mPullToRefreshLayout.setOnRefreshListener(this);
        order_sv = (PullableGridView) mView.findViewById(R.id.gridView);
        mProAdapter = new ProAdapter();
        order_sv.setAdapter(mProAdapter);
        order_sv.setOnItemClickListener(this);
        pd = (ProgressBar) mView.findViewById(R.id.pd);
    }

    private List<Integer> itemPosList = new ArrayList<>();//有选中值的itemPos列表，长度为3

    private void initFilterDropDownView(JSONArray sceneAllAttrs) {
        if (itemPosList.size() < sceneAllAttrs.length()) {
            itemPosList.add(0);
            itemPosList.add(0);
            itemPosList.add(0);
        }
        SceneDropMenuAdapter dropMenuAdapter = new SceneDropMenuAdapter(mView, sceneAllAttrs, itemPosList, this);
        dropDownMenu.setMenuAdapter(dropMenuAdapter);
    }

    @Override
    protected void handleMessage(int action, Object[] values) {

    }

    @Override
    protected void myHandleMessage(Message msg) {

    }

    /**
     * 场景列表
     */
    public void sendSceneList(int page) {
        mNetWork.sendSceneList(page, "20", keyword, this);
    }


    public void sendSceneType() {
        mNetWork.sendSceneType(this);
    }

    @Override
    public void onSuccessListener(String requestCode, JSONObject ans) {
        mView.hideLoading();
        pd.setVisibility(View.GONE);
        switch (requestCode) {
            case NetWorkConst.SCENELIST:
                if (null == mView || mView.isFinishing())
                    return;

                if (null != mPullToRefreshLayout) {
                    dismissRefesh();
                }
                JSONArray goodsList = ans.getJSONArray(Constance.scene);
                if (AppUtils.isEmpty(goodsList)) {
                    if (page == 1) {

                    }
                    goodses = new JSONArray();
                    dismissRefesh();
                    return;
                }

                getDataSuccess(goodsList);

                break;
            case NetWorkConst.SCENECATEGORY:
                sceneAllAttrs = ans.getJSONArray(Constance.categories);
                if (initFilterDropDownView)//重复setMenuAdapter会报错
                    initFilterDropDownView(sceneAllAttrs);
                break;

        }
    }

    @Override
    public void onFailureListener(String requestCode, JSONObject ans) {
        if (null == mView || mView.isFinishing())
            return;
        if (AppUtils.isEmpty(ans)) {
            AppDialog.messageBox(UIUtils.getString(R.string.server_error));
            return;
        }
        this.page--;
        if (null != mPullToRefreshLayout) {
            dismissRefesh();
        }
    }

    private void dismissRefesh() {
        mPullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
        mPullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
    }

    private void getDataSuccess(JSONArray array) {
        if (1 == page)
            goodses = array;
        else if (null != goodses) {
            for (int i = 0; i < array.length(); i++) {
                goodses.add(array.getJSONObject(i));
            }

            if (AppUtils.isEmpty(array))
                MyToast.show(mView, "没有更多内容了");
        }
        mProAdapter.notifyDataSetChanged();
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
                    //                    ImageLoader.getInstance().displayImage(imageURL
                    //                            , sceneBgIv);
                    break;
            }

            String path = imageURL;
            mIntent = new Intent();
            mIntent.putExtra(Constance.SCENE, path);
            mView.setResult(Constance.FROMDIY02, mIntent);//告诉原来的Activity 将数据传递给它
            mView.finish();//一定要调用该方法 关闭新的AC 此时 老是AC才能获取到Itent里面的值
        }
    }

    @Override
    public void onFilterDone(int titlePos, int itemPos, String itemStr) {
        dropDownMenu.close();
        if (0 == itemPos)
            itemStr = sceneAllAttrs.getJSONObject(titlePos).getString(Constance.attr_name);
        dropDownMenu.setPositionIndicatorText(titlePos, itemStr);

        if (titlePos < itemPosList.size())
            itemPosList.remove(titlePos);
        itemPosList.add(titlePos, itemPos);
        keyword = "[\"" + sceneAllAttrs.getJSONObject(titlePos).getJSONArray(Constance.attrVal).getString(itemPos) + "\"]";
        if (AppUtils.isEmpty(keyword))
            return;
        pd.setVisibility(View.VISIBLE);
        sendSceneList(1);

    }

    public void onBackPressed() {
        dropDownMenu.close();
    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        page = 1;
        initFilterDropDownView = false;
        sendSceneList(page);
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        initFilterDropDownView = false;
        sendSceneList((page++));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //        String path= NetWorkConst.SCENE_HOST+
        //                goodses.getJSONObject(position).getJSONObject(Constance.scene).getString(Constance.original_img);
        //        mIntent=new Intent();
        //        mIntent.putExtra(Constance.SCENE, path);
        //        mView.setResult(Constance.FROMDIY02, mIntent);//告诉原来的Activity 将数据传递给它
        //        mView.finish();//一定要调用该方法 关闭新的AC 此时 老是AC才能获取到Itent里面的值

        for (int i = 0; i < IssueApplication.mSelectScreens.length(); i++) {
            String selectName = IssueApplication.mSelectScreens.getJSONObject(i).getJSONObject(Constance.scene).getString(Constance.original_img);
            String name = goodses.getJSONObject(position).getJSONObject(Constance.scene).getString(Constance.original_img);
            if (selectName.equals(name)) {
                IssueApplication.mSelectScreens.delete(i);
                mProAdapter.notifyDataSetChanged();
                mView.select_num_tv.setText(IssueApplication.mSelectScreens.length() + "");
                return;
            }
        }
        IssueApplication.mSelectScreens.add(goodses.getJSONObject(position));
        mProAdapter.notifyDataSetChanged();
        mView.select_num_tv.setText(IssueApplication.mSelectScreens.length() + "");
    }

    public void goPhoto() {
        FileUtil.openImage(mView);
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
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(mView, R.layout.item_gridview_fm_scene02, null);

                holder = new ViewHolder();
                holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
                holder.check_iv = (ImageView) convertView.findViewById(R.id.check_iv);
                holder.textView = (TextView) convertView.findViewById(R.id.textView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            String path="";
            JSONObject object = goodses.getJSONObject(position);
            if (object.getJSONObject(Constance.scene) != null) {
                String name = object.getJSONObject(Constance.scene).getString(Constance.name);
                holder.textView.setText(name);
                path=object.getJSONObject(Constance.scene).getString(Constance.original_img);
                ImageLoader.getInstance().displayImage(NetWorkConst.SCENE_HOST + path
                        + "!400X400.png", holder.imageView);
            }

            holder.check_iv.setVisibility(View.GONE);
            for (int i = 0; i < IssueApplication.mSelectScreens.length(); i++) {
                String screenPath = IssueApplication.mSelectScreens.getJSONObject(i).getJSONObject(Constance.scene).getString(Constance.original_img);
                if (path.equals(screenPath)) {
                    holder.check_iv.setVisibility(View.VISIBLE);
                    break;
                }

            }


            return convertView;
        }

        class ViewHolder {
            ImageView imageView;
            TextView textView;
            ImageView check_iv;

        }
    }
}
