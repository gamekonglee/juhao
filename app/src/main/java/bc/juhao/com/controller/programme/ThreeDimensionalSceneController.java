package bc.juhao.com.controller.programme;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aliyun.iot.ilop.demo.DemoApplication;
import com.baiiu.filter.DropDownMenu;
import com.baiiu.filter.interfaces.OnFilterDoneListener;
import com.lib.common.hxp.view.PullToRefreshLayout;
import com.lib.common.hxp.view.PullableGridView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.zhy.http.okhttp.callback.Callback;

import java.util.ArrayList;
import java.util.List;

import bc.juhao.com.R;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.cons.NetWorkConst;
import bc.juhao.com.controller.BaseController;
import bc.juhao.com.listener.INetworkCallBack;
import bc.juhao.com.net.ApiClient;
import bc.juhao.com.ui.activity.programme.DiyActivity;
import bc.juhao.com.ui.activity.programme.ThreeDimensionalSceneActivity;
import bc.juhao.com.ui.adapter.SceneDropMenuAdapter;
import bc.juhao.com.utils.ImageUtil;
import bc.juhao.com.utils.UIUtils;
import bocang.json.JSONArray;
import bocang.json.JSONObject;
import bocang.utils.AppDialog;
import bocang.utils.AppUtils;
import bocang.utils.LogUtils;
import bocang.utils.MyToast;
import okhttp3.Call;
import okhttp3.Response;

/**
 * author: huanzhi
 * date:  2019-10-08$
 * ClassName: ThreeDimensionalSceneController$
 * Description:
 */
public class ThreeDimensionalSceneController extends BaseController implements OnFilterDoneListener, PullToRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener, INetworkCallBack {


    private ThreeDimensionalSceneActivity mView;
    private Intent mIntent;

    private DropDownMenu mDropDownMenu;
    private PullToRefreshLayout mPullToRefreshLayout;
    private PullableGridView mGvOrder;
    private ProgressBar mProgressBar;

    private ProAdapter mProAdapter;
    private boolean initFilterDropDownView;
    private JSONArray sceneAllAttrs;//从服务器获取的3维场景列表数据
    private JSONArray goodses; //界面显示3维场景列表数据
    private List<Integer> itemPosList = new ArrayList<>();//有选中值的itemPos列表，长度为3


    private int page = 1;
    //    private String mPublic = "";
    private String filterStr = "[0,13,0,0]";
//    private int isHot = 0;
//    private int isNew = 0;

    public ThreeDimensionalSceneController(ThreeDimensionalSceneActivity view) {
        this.mView = view;
        initView();
        initViewDate();
    }

    private void initViewDate() {
        page = 1;
        initFilterDropDownView = true;
        sendSceneList(page);
        sendSceneType();
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void initView() {
        mDropDownMenu = mView.findViewById(R.id.dropDownMenu);
        mPullToRefreshLayout = mView.findViewById(R.id.mFilterContentView);
        mGvOrder = mView.findViewById(R.id.gv_scene_list);
        mProgressBar = mView.findViewById(R.id.progress_bar);

        mProAdapter = new ProAdapter();
        mGvOrder.setAdapter(mProAdapter);

        mGvOrder.setOnItemClickListener(this);
        mPullToRefreshLayout.setOnRefreshListener(this);
    }


    private void initFilterDropDownView(JSONArray sceneAllAttrs) {
        if (itemPosList.size() < sceneAllAttrs.length()) {
//            MyToast.show(mView, sceneAllAttrs.length());
            itemPosList.add(0);
            itemPosList.add(0);
            itemPosList.add(0);
        }
        SceneDropMenuAdapter dropMenuAdapter = new SceneDropMenuAdapter(mView, sceneAllAttrs, itemPosList, this);
        mDropDownMenu.setMenuAdapter(dropMenuAdapter);
    }

    /**
     * 获取3D场景列表
     *
     * @param page
     */
    private void sendSceneList(int page) {

        mNetWork.getSceneList(page, "", filterStr, 0, 0, this);
    }

    /**
     * 获取场景排序方式
     */
    private void sendSceneType() {
        ApiClient.get3dSceneAttr(new Callback<String>() {
            @Override
            public String parseNetworkResponse(Response response, int id) throws Exception {
                return null;
            }

            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public String onResponse(String response, int id) {
                LogUtils.logE(BaseController.TAG, response);
                JSONObject ans = new JSONObject("{\"categories\":" + response + "}");
                sceneAllAttrs = ans.getJSONArray(Constance.categories);
                if (initFilterDropDownView) {//重复setMenuAdapter会报错
                    initFilterDropDownView(sceneAllAttrs);
                }
                return null;
            }
        });
    }

    @Override
    public void onSuccessListener(String requestCode, JSONObject ans) {
        mView.hideLoading();
        mProgressBar.setVisibility(View.GONE);
        switch (requestCode) {
            case NetWorkConst.SCENELIST_3D_URL://3D场景列表

                JSONArray goodsList = ans.getJSONArray(Constance.data);

                LogUtils.logE(BaseController.TAG, "3D场景列表:"+ ans.toString());
                if (null == mView || mView.isFinishing())
                    return;

//                if (null != mPullToRefreshLayout) {
//                    dismissRefesh();
//                }

                if (AppUtils.isEmpty(goodsList)) {
                    if (page == 1) {
                        MyToast.show(mView, R.string.no_data_from_server);
                    }else {
                        MyToast.show(mView, R.string.no_more_data);
                    }
                    goodses = new JSONArray();
                    return;
                }

                getDataSuccess(goodsList);

                break;
            case NetWorkConst.SCENEATTR_3D_URL:
                sceneAllAttrs = ans.getJSONArray(Constance.categories);
                if (initFilterDropDownView)//重复setMenuAdapter会报错
                    initFilterDropDownView(sceneAllAttrs);
                break;

        }
    }

    @Override
    public void onFailureListener(String requestCode, JSONObject ans) {

        if (ans != null)
            LogUtils.logE(BaseController.TAG, "3D场景列表错误信息:"+ ans.toString());

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

    int[] itemId = {0, 13, 0, 0};

    @Override
    public void onFilterDone(int titlePos, int itemPos, String itemStr) {

        mDropDownMenu.close();
        itemStr = sceneAllAttrs.getJSONObject(titlePos).getJSONArray(Constance.attr_list).getJSONObject(itemPos).getString(Constance.name);
        if (titlePos < 4)
            itemId[titlePos] = sceneAllAttrs.getJSONObject(titlePos).getJSONArray(Constance.attr_list).getJSONObject(itemPos).getInt(Constance.id);
        filterStr = "[";
        for (int i = 0; i < itemId.length; i++) {
            filterStr += itemId[i];
            if (i != itemId.length - 1) filterStr += ",";
        }
        filterStr += "]";

        if (titlePos == sceneAllAttrs.length()) {
            itemStr = "全部";
        }
        mDropDownMenu.setPositionIndicatorText(titlePos, itemStr);

        if (titlePos < itemPosList.size())
            itemPosList.remove(titlePos);
        itemPosList.add(titlePos, itemPos);

        mProgressBar.setVisibility(View.VISIBLE);
        sendSceneList(1);
    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        page = 1;
        initFilterDropDownView = false;
        sendSceneList(page);
        if (null != mPullToRefreshLayout) {
            dismissRefesh();
        }
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        initFilterDropDownView = false;
        sendSceneList((page++));
        if (null != mPullToRefreshLayout) {
            dismissRefesh();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

        JSONObject object = goodses.getJSONObject(position);
        DemoApplication.mSelectScreens.add(object);

        DemoApplication.SCENE_TYPE=3;
        mIntent = new Intent(mView, DiyActivity.class);
        mIntent.putExtra(Constance.is_find_home,true);
        mView.startActivity(mIntent);

        LogUtils.logE(BaseController.TAG, "position:"+ position + "  " + "id:"+ id + "  " + "adapterView"+ adapterView + "  " + "view:"+view);
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

    private void dismissRefesh() {
        mPullToRefreshLayout.refreshFinish(com.lib.common.hxp.view.PullToRefreshLayout.SUCCEED);
        mPullToRefreshLayout.loadmoreFinish(com.lib.common.hxp.view.PullToRefreshLayout.SUCCEED);
    }


    private class ProAdapter extends BaseAdapter {

        private String name;

        public ProAdapter() {
        }

        @Override
        public int getCount() {
            if (null == goodses) {
                return 0;
            }
            return goodses.length();
        }

        @Override
        public JSONObject getItem(int position) {
            if (null == goodses) {
                return null;
            }
            return goodses.getJSONObject(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {

            final ViewHolder viewHolder;
            if (convertView == null) {
                convertView = View.inflate(mView, R.layout.item_gridview_scene3, null);
                viewHolder = new ViewHolder();
                viewHolder.iv_scene = convertView.findViewById(R.id.iv_scene);
                viewHolder.tv_scene_name = convertView.findViewById(R.id.tv_scene_name);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            //图片路径
            String path = "";
            JSONObject object = goodses.getJSONObject(position);

            if (object != null) {
                name = object.getString(Constance.name);
                path = object.getString(Constance.image_thumb);
                ImageSize imageSize = new ImageSize(200, 200);
                ImageLoader.getInstance().loadImage(path, imageSize, new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String s, View view) {

                    }

                    @Override
                    public void onLoadingFailed(String s, View view, FailReason failReason) {

                    }

                    @Override
                    public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                        viewHolder.iv_scene.setImageBitmap(ImageUtil.compressImage02(bitmap, 50));
                    }

                    @Override
                    public void onLoadingCancelled(String s, View view) {

                    }
                });
            }


            viewHolder.tv_scene_name.setText(name);
            return convertView;
        }

        class ViewHolder {
            ImageView iv_scene;
            TextView tv_scene_name;

        }

    }


    @Override
    protected void handleMessage(int action, Object[] values) {

    }

    @Override
    protected void myHandleMessage(Message msg) {

    }
}
