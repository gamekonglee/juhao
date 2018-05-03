package bc.juhao.com.ui.activity.user;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;

import bc.juhao.com.R;
import bc.juhao.com.cons.NetWorkConst;
import bc.juhao.com.controller.user.MerchantInfoController;
import bc.juhao.com.ui.activity.IssueApplication;
import bc.juhao.com.ui.view.BaiduMapContainer;
import bc.juhao.com.ui.view.ObservableScrollView;
import bocang.utils.MyToast;
import bocang.view.BaseActivity;

/**
 * @author: Jun
 * @date : 2017/5/20 15:59
 * @description :
 */
public class MerchantInfoActivity extends BaseActivity {
    private MerchantInfoController mController;
    MapView bmapView;
    BaiduMap mBaiduMap;
    public float latx = 23.018124f;
    public float laty = 113.104568f;
    private ObservableScrollView scrollView;
    public BaiduMapContainer baiduMapContainer;
    public String mAddress="";
    private TextView tv_kefu;

    @Override
    protected void InitDataView() {

    }


    @Override
    protected void initController() {
        mController = new MerchantInfoController(this);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_merchant_info);
        setColor(this, Color.WHITE);
        bmapView =  findViewById(R.id.bmapView);
        mBaiduMap = bmapView.getMap();
        //普通地图
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        //定义Maker坐标点
        scrollView = (ObservableScrollView) findViewById(R.id.scrollView);
        baiduMapContainer = (BaiduMapContainer)findViewById(R.id.baiduMapContainer);
        baiduMapContainer.setScrollView(scrollView);
        tv_kefu = findViewById(R.id.tv_kefu);
        tv_kefu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(IssueApplication.mUserObject==null){
//                    isToken();
                    MyToast.show(MerchantInfoActivity.this,"数据加载中，请稍等");
                    return;
                }
                String parent_name = IssueApplication.mUserObject.getString("parent_name");
                String parent_id = IssueApplication.mUserObject.getString("parent_id");
                String userIcon = NetWorkConst.SCENE_HOST + IssueApplication.mUserObject.getString("parent_avatar");
                mController.sendCall("尝试连接聊天服务..请连接?", parent_id, parent_name, userIcon);
            }
        });
    }

    public void initMyLocation() {
        mBaiduMap.setMyLocationEnabled(true);

        //普通地图
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        //开启交通图
        mBaiduMap.setTrafficEnabled(true);

        // 开发者可根据自己实际的业务需求，利用标注覆盖物，在地图指定的位置上添加标注信息。具体实现方法如下：
        //定义Maker坐标点
        LatLng point = new LatLng(latx, laty);

        //        LatLng point = new LatLng(23.018124, 113.104568);
        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.icon_openmap_mark);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(bitmap);
        //在地图上添加Marker，并显示
        mBaiduMap.addOverlay(option);


        //设定中心点坐标

        LatLng cenpt = new LatLng(latx, laty);
        //定义地图状态
        MapStatus mMapStatus = new MapStatus.Builder()
                .target(cenpt)
                .zoom(13)
                .build();
        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化


        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        //改变地图状态
        mBaiduMap.setMapStatus(mMapStatusUpdate);
        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                MyToast.show(MerchantInfoActivity.this,mAddress);
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        bmapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        bmapView.onPause();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        bmapView.onDestroy();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onViewClick(View v) {

    }
}
