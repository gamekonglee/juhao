package bc.juhao.com.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.aliyun.iot.ilop.demo.DemoApplication;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import bc.juhao.com.R;
import bc.juhao.com.adapter.BaseAdapterHelper;
import bc.juhao.com.adapter.QuickAdapter;
import bc.juhao.com.bean.RoomBean;
import bc.juhao.com.common.BaseActivity;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.net.ApiClient;
import bc.juhao.com.ui.activity.programme.SelectSceneActivity;
import bc.juhao.com.ui.view.EndOfGridView;
import bc.juhao.com.ui.view.EndOfListView;
import bc.juhao.com.ui.view.PMSwipeRefreshLayout;
import bc.juhao.com.utils.UIUtils;
import bocang.json.JSONArray;
import bocang.json.JSONObject;
import bocang.utils.LogUtils;
import bocang.utils.MyToast;

public class FindHomeListActivity extends BaseActivity implements EndOfListView.OnEndOfListListener, SwipeRefreshLayout.OnRefreshListener {

    private EndOfGridView listview;
    private PMSwipeRefreshLayout pullToRefresh;
    private int page=0;
    private JSONArray goodses;
    private int width;
    private List<RoomBean> roomBeanList;
    private String city;
    private String area;
    private String q;
    private QuickAdapter<RoomBean> adapter;
    private boolean isBottom;
    private TextView title_tv;
    
    @Override
    protected void InitDataView() {
        title_tv.setText(""+q);
//        page=1;
//        getRoomList();
    }

    @Override
    protected void initController() {

    }

    @Override
    protected void initView() {
    setContentView(R.layout.activity_findhome_list);
        setColor(this, Color.WHITE);
        listview = findViewById(R.id.listView);
        title_tv = findViewById(R.id.tv_title);
        width = UIUtils.getScreenWidth(this);
        adapter = new QuickAdapter<RoomBean>(FindHomeListActivity.this,R.layout.item_rooms) {
            @Override
            protected void convert(BaseAdapterHelper helper, RoomBean item) {
                helper.setText(R.id.tv_name,item.getName());
                helper.setText(R.id.tv_type,item.getType());
                helper.setText(R.id.tv_size,item.getArea());
                ImageView iv_img=helper.getView(R.id.iv_img);
                ImageLoader.getInstance().displayImage(item.getPic(),iv_img);
            }
        };
        listview.setAdapter(adapter);
        listview.setOnEndOfListListener(this);
        pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setColorSchemeColors(Color.BLUE,Color.YELLOW,Color.GREEN,Color.RED);
        pullToRefresh.setOnRefreshListener(this);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DemoApplication.SCENE_TYPE=3;
                Intent intent=new Intent(FindHomeListActivity.this, SelectSceneActivity.class);
                intent.putExtra(Constance.isFindHome,true);
                intent.putExtra(Constance.title,roomBeanList.get(position).getName());
//                String productId = goodses.getJSONObject(position).getInt(Constance.id)+"";
//                intent.putExtra(Constance.product, productId);
                startActivity(intent);

            }
        });

    }

    @Override
    protected void initData() {
        city = getIntent().getStringExtra(Constance.city);
        area = getIntent().getStringExtra(Constance.area);
        q = getIntent().getStringExtra(Constance.xiaoqu);
    }

    @Override
    public void onEndOfList(Object lastItem) {
        if(isBottom){
            return;
        }
        page++;
        pullToRefresh.setRefreshing(true);
        getRoomList();

    }

    @Override
    public void onRefresh() {
        page=1;
        isBottom=false;
        getRoomList();
    }

    public void getRoomList(){
        pullToRefresh.setRefreshing(true);
        new Thread(){
            @Override
            public void run() {
                super.run();
                final String estaeTypeList= ApiClient.getEstateType(city,area,q,page);
                LogUtils.logE("estaeTypeList",estaeTypeList);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pullToRefresh.setRefreshing(false);
                        JSONObject jsonObject=new JSONObject(estaeTypeList);
                        if(jsonObject!=null){
                            JSONArray data=jsonObject.getJSONArray(Constance.data);
                            if(data!=null||data.length()>0){
                                List<RoomBean >temp= new Gson().fromJson(data.toString(),new TypeToken<List<RoomBean>>(){}.getType());
                                if(temp==null||temp.size()==0){
                                    isBottom = true;
                                    MyToast.show(FindHomeListActivity.this,"没有数据了");
                                    return;
                                }
                                if(page==1){
                                    roomBeanList=temp;
                                }else {
                                    roomBeanList.addAll(temp);
                                }
                                adapter.replaceAll(roomBeanList);
                            }else {
                                isBottom = true;
                                MyToast.show(FindHomeListActivity.this,"没有数据了");
                                return;
                            }
                        }else {
                            isBottom = true;
                            MyToast.show(FindHomeListActivity.this,"没有数据了");
                            return;
                        }


                    }
                });

            }
        }.start();
    }

}
