package bc.juhao.com.controller.user;

import android.app.ActionBar;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.lib.common.hxp.view.PullToRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import bc.juhao.com.R;
import bc.juhao.com.bean.DistriButorBean;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.cons.NetWorkConst;
import bc.juhao.com.controller.BaseController;
import bc.juhao.com.listener.INetworkCallBack;
import bc.juhao.com.ui.activity.IssueApplication;
import bc.juhao.com.ui.activity.user.MyDistributorActivity;
import bc.juhao.com.ui.view.ShowDialog;
import bc.juhao.com.utils.DateUtils;
import bocang.json.JSONArray;
import bocang.json.JSONObject;
import bocang.utils.AppUtils;
import bocang.utils.CommonUtil;
import bocang.utils.MyToast;

/**
 * @author: Jun
 * @date : 2017/9/19 9:12
 * @description :
 */
public class MyDistributorController extends BaseController implements PullToRefreshLayout.OnRefreshListener, INetworkCallBack, View.OnClickListener, OnItemClickListener {
    private MyDistributorActivity mView;
    private TableLayout table_tl;
    private TableLayout table_head;
    private String[] mlistHead = {"会员名称", "级别", "注册日期", "操作"};
    private List<DistriButorBean> mDistriButorBeans = new ArrayList<>();
    private View mNullView;
    private View mNullNet;
    private Button mRefeshBtn;
    private TextView mNullNetTv;
    private AlertView mLevelView;
    private String[] mLevels;
    private int mUserLevel;
    private String mUserId;

    public MyDistributorController(MyDistributorActivity v) {
        mView = v;
        initView();
        initViewData();
    }

    private void initViewData() {
        getAgentAll();
    }

    private void initView() {
        table_tl = (TableLayout) mView.findViewById(R.id.table_tl);
        table_head = (TableLayout) mView.findViewById(R.id.table_head);
        mNullView = mView.findViewById(R.id.null_view);
        mNullNet = mView.findViewById(R.id.null_net);
        mRefeshBtn = (Button) mNullNet.findViewById(R.id.refesh_btn);
        mRefeshBtn.setOnClickListener(this);
        mNullNetTv = (TextView) mNullNet.findViewById(R.id.tv);
//        mNullViewTv = (TextView) mNullView.findViewById(R.id.tv);
        if(IssueApplication.mUserObject==null){
            MyToast.show(mView,"数据加载中");
            return;
        }
        mUserLevel = IssueApplication.mUserObject.getInt(Constance.level);
        getLevel();
        mLevelView = new AlertView(null, null, "取消", null,
                mLevels,
                mView, AlertView.Style.ActionSheet, this);
    }


    private void getLevel() {

        switch (mUserLevel) {
            case 0:
                mLevels = new String[]{"二级", "三级", "消费者"};
                break;
            case 1:
                mLevels = new String[]{"三级", "消费者"};
                break;
        }
    }

    private void getAgentAll() {
        mView.setShowDialog(true);
        mView.setShowDialog("载入中..");
        mView.showLoading();
        mNetWork.getAgentAll(this);

    }

    @Override
    protected void handleMessage(int action, Object[] values) {

    }

    @Override
    protected void myHandleMessage(Message msg) {

    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        getAgentAll();
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        getAgentAll();
    }


    @Override
    public void onSuccessListener(String requestCode, JSONObject ans) {
        switch (requestCode) {
            case NetWorkConst.AGENT_ALL_URL://获取我的分销商
                mView.hideLoading();
                mDistriButorBeans = new ArrayList<>();
                JSONArray jsonArray = ans.getJSONArray(Constance.data);
                //01
                if(jsonArray==null||jsonArray.length()==0)
                {
                    return;
                }
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    int id = object.getInt(Constance.id);
                    String nickname = object.getString(Constance.nickname);
                    String username = object.getString(Constance.username);
                    String mobile = object.getString(Constance.mobile);
                    int level = object.getInt(Constance.level);
                    String joined_at = object.getString(Constance.joined_at);
                    String sign = "├";
                    if (AppUtils.isEmpty(nickname)) {
                        nickname = username;
                    }
                    mDistriButorBeans.add(new DistriButorBean(id, nickname, level, joined_at, sign, mobile));
                    //下级02
                    JSONArray child02Array = object.getJSONArray(Constance.parent);
                    for (int j = 0; j < child02Array.length(); j++) {
                        JSONObject object02 = child02Array.getJSONObject(j);
                        int id02 = object02.getInt(Constance.id);
                        String username02 = object02.getString(Constance.username);
                        String nickname02 = object02.getString(Constance.nickname);
                        String mobile02 = object.getString(Constance.mobile);
                        int level02 = object02.getInt(Constance.level);
                        String joined_at02 = object02.getString(Constance.joined_at);
                        String sign02 = "    ├";
                        if (AppUtils.isEmpty(nickname02)) {
                            nickname02 = username02;
                        }
                        mDistriButorBeans.add(new DistriButorBean(id02, nickname02, level02, joined_at02, sign02, mobile02));
                        //                //下级03
                        JSONArray child03Array = object02.getJSONArray(Constance.parent);
                        for (int k = 0; k < child03Array.length(); k++) {
                            JSONObject object03 = child03Array.getJSONObject(k);
                            int id03 = object03.getInt(Constance.id);
                            String nickname03 = object03.getString(Constance.nickname);
                            String username03 = object03.getString(Constance.username);
                            String mobile03 = object.getString(Constance.mobile);
                            int level03 = object03.getInt(Constance.level);
                            String joined_at03 = object03.getString(Constance.joined_at);
                            String sign03 = "        ├";
                            if (AppUtils.isEmpty(nickname03)) {
                                nickname03 = username03;
                            }
                            mDistriButorBeans.add(new DistriButorBean(id03, nickname03, level03, joined_at03, sign03, mobile03));
                        }
                    }

                }

                if (mDistriButorBeans.size() == 0) {
                    mNullView.setVisibility(View.VISIBLE);
                    mNullNet.setVisibility(View.GONE);
                    return;
                }

                mNullView.setVisibility(View.GONE);
                mNullNet.setVisibility(View.GONE);
                intTableData();
                break;
            case NetWorkConst.LEVEL_EDIT_URL://修改级别
                getAgentAll();
                break;
        }

    }

    private void setPhone(final String phoneNumber) {
        ActivityCompat.requestPermissions(mView,
                new String[]{"android.permission.CALL_PHONE"},
                1);
        ShowDialog mDialog = new ShowDialog();
        mDialog.show(mView, "提示", "是否打电话给" + phoneNumber + "?", new ShowDialog.OnBottomClickListener() {
            @Override
            public void positive() {
                PackageManager packageManager = mView.getPackageManager();
                int permission = packageManager.checkPermission("android.permission.CALL_PHONE", "bc.juhao.com");
                if (PackageManager.PERMISSION_GRANTED != permission) {
                    return;
                } else {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" + phoneNumber));
                    mView.startActivity(intent);
                }
            }

            @Override
            public void negtive() {

            }
        });

    }


    @Override
    public void onFailureListener(String requestCode, JSONObject ans) {
        mView.hideLoading();
        mNullNet.setVisibility(View.VISIBLE);
    }

    private void setTableHead() {

        table_head.setStretchAllColumns(true);

        TableRow tableRow = new TableRow(mView);

        for (int i = 0; i < mlistHead.length; i++) {
            TextView tv = new TextView(mView);
            tv.setText(mlistHead[i]);
            tv.setBackgroundResource(R.drawable.table_row);
            tv.setTextColor(mView.getResources().getColor(R.color.txt_black));
            tv.setTextSize(17);
            tv.setGravity(Gravity.CENTER);
            tv.setSingleLine();
            tv.setHeight(80);
            tv.getPaint().setFakeBoldText(true);
            tableRow.addView(tv);
        }

        table_tl.addView(tableRow, new TableLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.FILL_PARENT));

    }

    private void intTableData() {
        table_tl.removeAllViews();
        setTableHead();
        table_tl.setStretchAllColumns(true);
        foreachTableData();
    }

    private void foreachTableData() {
        for (int i = 0; i < mDistriButorBeans.size(); i++) {
            TableRow tableRow = new TableRow(mView);
            final String userName = mDistriButorBeans.get(i).getUsername();
            final String tel = mDistriButorBeans.get(i).getTel();
            final String uid = mDistriButorBeans.get(i).getId() + "";
            for (int j = 0; j < mlistHead.length; j++) {
                TextView tv = new TextView(mView);
                tv.setBackgroundResource(R.drawable.table_row);
                tv.setTextColor(mView.getResources().getColor(R.color.txt_black));
                tv.setTextSize(15);
                tv.setGravity(Gravity.CENTER);
                tv.setSingleLine();
                tv.setHeight(100);
                switch (j) {
                    case 0:
                        tv.setGravity(Gravity.CENTER | Gravity.LEFT);
                        tv.setText(mDistriButorBeans.get(i).getSign() + userName);
                        tv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (!AppUtils.isEmpty(tel) && CommonUtil.isMobileNO(tel)) {
                                    setPhone(tel);
                                } else {
                                    MyToast.show(mView, "该用户没有电话");
                                }
                            }
                        });
                        break;
                    case 1:
                        tv.setText(getLevel(mDistriButorBeans.get(i).getLevel()));
                        break;
                    case 2:
                        String joinedAt = mDistriButorBeans.get(i).getJoined_at();
                        tv.setText(DateUtils.getStrTime02(joinedAt));
                        break;
                    case 3:
                        if (mUserLevel < 2) {
                            String joinedAt1 = "修改";
                            tv.setTextColor(mView.getResources().getColor(R.color.green));
                            tv.setText(joinedAt1);
                            tv.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mUserId = uid;
                                    mLevelView.show();
                                }
                            });
                        }
                        //                        if ("├".equals(mDistriButorBeans.get(i).getSign()) && mUserLevel < 2) {
                        //                            String joinedAt1 = "修改";
                        //                            tv.setTextColor(mView.getResources().getColor(R.color.green));
                        //                            tv.setText(joinedAt1);
                        //                            tv.setOnClickListener(new View.OnClickListener() {
                        //                                @Override
                        //                                public void onClick(View v) {
                        //                                    mUserId = uid;
                        //                                    mLevelView.show();
                        //                                }
                        //                            });
                        //                        }

                        break;
                }

                tableRow.addView(tv);

            }
            table_tl.addView(tableRow, new TableLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT,
                    ActionBar.LayoutParams.FILL_PARENT));


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
        } else {
            levelValue = "消费者";
        }
        return levelValue;
    }

    @Override
    public void onClick(View v) {
        getAgentAll();
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
        mView.setShowDialog(true);
        mView.setShowDialog("正在设置中..");
        mView.showLoading();
        mNetWork.editLevel(level, mUserId, this);

    }
}
