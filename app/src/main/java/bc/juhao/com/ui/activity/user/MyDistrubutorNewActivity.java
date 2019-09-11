package bc.juhao.com.ui.activity.user;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.aliyun.iot.ilop.demo.DemoApplication;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

import bc.juhao.com.R;
import bc.juhao.com.adapter.BaseAdapterHelper;
import bc.juhao.com.adapter.QuickAdapter;
import bc.juhao.com.bean.DistribuBean;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.listener.INetworkCallBack;
import bc.juhao.com.ui.view.EndOfListView;
import bc.juhao.com.ui.view.ShowDialog;
import bc.juhao.com.utils.DateUtils;
import bc.juhao.com.utils.Network;
import bocang.json.JSONObject;
import bocang.utils.AppUtils;
import bocang.utils.CommonUtil;
import bocang.utils.LogUtils;
import bocang.utils.MyToast;
import bocang.view.BaseActivity;

/**
 * Created by gamekonglee on 2019/5/17.
 */

public class MyDistrubutorNewActivity extends BaseActivity implements OnItemClickListener, EndOfListView.OnEndOfListListener {

    private int mUserLevel;
    private AlertView mLevelView;
    private String[] mLevels;
    private View mNullView;
    private TextView mNullNetTv;
    private EndOfListView lv_distributor;
    private int mUserId;
    private List<DistribuBean> distribuBeans=new ArrayList<>();
    private QuickAdapter<DistribuBean> adapter;
    private Network network;
    int page=0;
    int per_page=20;
    private boolean isBottom=false;

    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {

    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_my_distributor_new);
        lv_distributor = findViewById(R.id.lv_distributor);
        mNullView = findViewById(R.id.null_view);
        View mNullNet = findViewById(R.id.null_net);
        Button mRefeshBtn = (Button) mNullNet.findViewById(R.id.refesh_btn);
        mRefeshBtn.setOnClickListener(this);
        mNullNetTv = (TextView) mNullNet.findViewById(R.id.tv);
        if(DemoApplication.mUserObject==null){
            MyToast.show(this,"数据加载中");
            return;
        }
        mUserLevel = DemoApplication.mUserObject.getInt(Constance.level);
        getLevel();

        //                    String joinedAt1 = "修改";
//                    tv.setText(joinedAt1);
//                    String joinedAt1 = "修改";
//                    tv.setText(joinedAt1);
//                    String joinedAt1 = "修改";
//                    tv.setText(joinedAt1);
        adapter = new QuickAdapter<DistribuBean>(this, R.layout.item_distrubute) {
            @Override
            protected void convert(BaseAdapterHelper helper, final DistribuBean item) {
                String name=item.getNickname();
                if(TextUtils.isEmpty(name)){
                    name=item.getUsername();
                }
                helper.setText(R.id.tv_name,name);
                helper.setText(R.id.tv_level,getLevel(Integer.parseInt(item.getLevel()))+"");
                helper.setText(R.id.tv_date, DateUtils.getStrTime02(item.getJoined_at()));
                if (mUserLevel < 2||mUserLevel==5) {
//                    String joinedAt1 = "修改";
//                    tv.setText(joinedAt1);
                    helper.getView(R.id.tv_edit).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mUserId = item.getId();
                            mLevelView = new AlertView(null, null, "取消", null, mLevels, MyDistrubutorNewActivity.this, AlertView.Style.ActionSheet, MyDistrubutorNewActivity.this);
                            mLevelView.show();
                        }
                    });
                }
                final String tel=item.getMobile();
                helper.setOnClickListener(R.id.tv_name, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!AppUtils.isEmpty(tel) && CommonUtil.isMobileNO(tel)) {
                            setPhone(tel);
                        } else {
                            MyToast.show(MyDistrubutorNewActivity.this, "该用户没有电话");
                        }
                    }
                });
                ListView lv_distru_02=helper.getView(R.id.lv_distributor_02);
                List<DistribuBean> distribuBeans02=item.getParent();
                if(distribuBeans02!=null&&distribuBeans02.size()>0){
                    QuickAdapter<DistribuBean> adapter02=new QuickAdapter<DistribuBean>(MyDistrubutorNewActivity.this,R.layout.item_distrubute) {
                        @Override
                        protected void convert(BaseAdapterHelper helper, final DistribuBean item) {
                            String name=item.getNickname();
                            if(TextUtils.isEmpty(name)){
                                name=item.getUsername();
                            }
                            helper.setText(R.id.tv_name,"  ├"+name);
                            helper.setText(R.id.tv_level,getLevel(Integer.parseInt(item.getLevel()))+"");
                            helper.setText(R.id.tv_date, DateUtils.getStrTime02(item.getJoined_at()));
                            if (mUserLevel < 2||mUserLevel==5) {
//                    String joinedAt1 = "修改";
//                    tv.setText(joinedAt1);
                                helper.getView(R.id.tv_edit).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        mUserId = item.getId();
                                        mLevelView = new AlertView(null, null, "取消", null, mLevels, MyDistrubutorNewActivity.this, AlertView.Style.ActionSheet, MyDistrubutorNewActivity.this);
                                        mLevelView.show();
                                    }
                                });
                            }
                            helper.setOnClickListener(R.id.tv_name, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (!AppUtils.isEmpty(tel) && CommonUtil.isMobileNO(tel)) {
                                        setPhone(tel);
                                    } else {
                                        MyToast.show(MyDistrubutorNewActivity.this, "该用户没有电话");
                                    }
                                }
                            });
                            ListView lv_distru_03=helper.getView(R.id.lv_distributor_02);
                            List<DistribuBean> distribuBeans03=item.getParent();
                            if(distribuBeans03!=null&&distribuBeans03.size()>0){
                                QuickAdapter<DistribuBean> adapter03=new QuickAdapter<DistribuBean>(MyDistrubutorNewActivity.this,R.layout.item_distrubute) {
                                    @Override
                                    protected void convert(BaseAdapterHelper helper, final DistribuBean item) {
                                        String name=item.getNickname();
                                        if(TextUtils.isEmpty(name)){
                                            name=item.getUsername();
                                        }
                                        helper.setText(R.id.tv_name,"     ├"+name);
                                        helper.setText(R.id.tv_level,getLevel(Integer.parseInt(item.getLevel()))+"");
                                        helper.setText(R.id.tv_date, DateUtils.getStrTime02(item.getJoined_at()));
                                        if (mUserLevel < 2||mUserLevel==5) {
//                    String joinedAt1 = "修改";
//                    tv.setText(joinedAt1);
                                            helper.getView(R.id.tv_edit).setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    mUserId = item.getId();
                                                    mLevelView = new AlertView(null, null, "取消", null, mLevels, MyDistrubutorNewActivity.this, AlertView.Style.ActionSheet, MyDistrubutorNewActivity.this);
                                                    mLevelView.show();
                                                }
                                            });
                                        }
                                        helper.setOnClickListener(R.id.tv_name, new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                if (!AppUtils.isEmpty(tel) && CommonUtil.isMobileNO(tel)) {
                                                    setPhone(tel);
                                                } else {
                                                    MyToast.show(MyDistrubutorNewActivity.this, "该用户没有电话");
                                                }
                                            }
                                        });
                                    }
                                };
                                lv_distru_03.setAdapter(adapter03);
                                adapter03.replaceAll(item.getParent());
                            }
                        }
                    };
                    lv_distru_02.setAdapter(adapter02);
                    adapter02.replaceAll(item.getParent());
                 }
            }
        };
        lv_distributor.setAdapter(adapter);
        network = new Network();
        lv_distributor.setOnEndOfListListener(this);
    }

    private void getAgents() {
        network.getAgentAll(page+"",""+per_page,new INetworkCallBack() {
            @Override
            public void onSuccessListener(String requestCode, JSONObject ans) {
                hideLoading();
                List<DistribuBean>temp=new Gson().fromJson(ans.getJSONArray(Constance.data).toString(),new TypeToken<List<DistribuBean>>(){}.getType());
                if(temp!=null&&temp.size()>0){
                    LogUtils.logE("page",page+"");
                    if(page==1){
                        distribuBeans=temp;
                    }else {
                        distribuBeans.addAll(temp);
                    }
                    adapter.replaceAll(distribuBeans);
                }else {
                    isBottom = true;
                    MyToast.show(MyDistrubutorNewActivity.this,"到底了");
                }
//                LogUtils.logE("distrubeans",distribuBeans.get(0).getNickname());
            }

            @Override
            public void onFailureListener(String requestCode, JSONObject ans) {

            }
        });
    }

    private void setPhone(final String phoneNumber) {
        ActivityCompat.requestPermissions(this,
                new String[]{"android.permission.CALL_PHONE"},
                1);
        ShowDialog mDialog = new ShowDialog();
        mDialog.show(this, "提示", "是否打电话给" + phoneNumber + "?", new ShowDialog.OnBottomClickListener() {
            @Override
            public void positive() {
                PackageManager packageManager = getPackageManager();
                int permission = packageManager.checkPermission("android.permission.CALL_PHONE", "bc.juhao.com");
                if (PackageManager.PERMISSION_GRANTED != permission) {
                    return;
                } else {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" + phoneNumber));
                    startActivity(intent);
                }
            }

            @Override
            public void negtive() {

            }
        });

    }
    private void getLevel() {

        switch (mUserLevel) {
            case 0:
            case 5:
                mLevels = new String[]{"二级", "三级", "消费者"};
                break;
            case 1:
                mLevels = new String[]{"三级", "消费者"};
                break;
        }
    }

    private String getLevel(int level) {
        String levelValue = "";
        if (level == 0) {
            levelValue = "一级";
        } else if (level == 1) {
            levelValue = "二级";
        } else if (level == 2) {
            levelValue = "三级";
        } else if(level==5){
            levelValue = "一级";
        }else {
            levelValue = "消费者";
        }
        return levelValue;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onViewClick(View v) {

    }

    @Override
    public void onItemClick(Object o, int position) {
        if (position == -1)
            return;
        String levelValue = mLevels[position];
        int level = -1;
        switch (levelValue) {
            case "二级":
                level = 1;
                break;
            case "三级":
                level = 2;
                break;
            case "消费者":
                level = 3;
                break;
        }
        setShowDialog(true);
        setShowDialog("正在设置中..");
        showLoading();
        network.editLevel(level, String.valueOf(mUserId), new INetworkCallBack() {
            @Override
            public void onSuccessListener(String requestCode, JSONObject ans) {
                if(ans!=null){

                }
                MyToast.show(MyDistrubutorNewActivity.this,"修改成功");
                page=1;
                distribuBeans=new ArrayList<>();
                getAgents();
            }

            @Override
            public void onFailureListener(String requestCode, JSONObject ans) {
                if(ans!=null)
                LogUtils.logE("failed",ans.toString());
                MyToast.show(MyDistrubutorNewActivity.this,"修改失败");
            }
        });
    }

    @Override
    public void onEndOfList(Object lastItem) {
        if(page==1&&distribuBeans.size()==0){
            return;
        }
        if(isBottom){
            return;
        }
        page++;
        getAgents();
    }
}
