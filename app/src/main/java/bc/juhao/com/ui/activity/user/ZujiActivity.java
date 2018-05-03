package bc.juhao.com.ui.activity.user;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import bc.juhao.com.R;
import bc.juhao.com.adapter.BaseAdapterHelper;
import bc.juhao.com.adapter.QuickAdapter;
import bc.juhao.com.bean.DaoMaster;
import bc.juhao.com.bean.DaoSession;
import bc.juhao.com.bean.DbGoodsBean;
import bc.juhao.com.bean.DbGoodsBeanDao;
import bc.juhao.com.common.BaseActivity;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.controller.user.CollectController;
import bc.juhao.com.ui.activity.product.ProDetailActivity;
import bc.juhao.com.ui.view.EndOfListView;
import bc.juhao.com.utils.DbHelper;
import bocang.json.JSONArray;
import bocang.json.JSONObject;
import bocang.utils.AppUtils;
import bocang.utils.MyToast;

public class ZujiActivity extends BaseActivity implements View.OnClickListener {

    private EndOfListView lv_zuji;
    private CheckBox checkAll;
    private ImageView iv_edit;
    private Button delete_bt;
    private ProAdapter adapter;
    private List<DbGoodsBean> goods;
    private boolean isEdit;
    private RelativeLayout rl;
    private boolean isCheck;
    private boolean isLastDelete;
    private ArrayList<Boolean> isCheckShowList;
    private ArrayList<Boolean> isCheckList;
    private List<DbGoodsBean> goodCheckList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {

    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_zuji);
        setColor(this, Color.WHITE);
        lv_zuji = findViewById(R.id.lv_zuji);
        iv_edit = findViewById(R.id.iv_edit);
        checkAll = findViewById(R.id.checkAll);
        delete_bt = findViewById(R.id.delete_bt);
        rl = findViewById(R.id.rl);
        adapter = new ProAdapter(this, R.layout.item_gv_collect);
        lv_zuji.setAdapter(adapter);
        iv_edit.setOnClickListener(this);
        checkAll.setOnClickListener(this);
        delete_bt.setOnClickListener(this);
        goods = new ArrayList<>();
        isCheckShowList = new ArrayList<>();
        isCheckList = new ArrayList<>();

        isEdit = false;
        isCheck = false;
        lv_zuji.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent mIntent = new Intent(ZujiActivity.this, ProDetailActivity.class);
                int productId =goods.get(position).getId();
                mIntent.putExtra(Constance.product, productId);
                startActivity(mIntent);
            }
        });
        loadData();
    }

    private void loadData() {
        DbGoodsBeanDao dbGoodsBeanDao= DbHelper.getSession(this).getDbGoodsBeanDao();
        goods=dbGoodsBeanDao.queryBuilder().orderDesc(DbGoodsBeanDao.Properties.Create_time).list();
        adapter.getCheck(false,true);
        adapter.getIsCheck(false,true);
        adapter.replaceAll(goods);
    }

    @Override
    protected void initData() {


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_edit:
                setEdit();
                break;
            case R.id.checkAll:
                setCheckAll();
                break;
            case R.id.delete_bt:
                senDeleteCollect();
                break;
        }
    }


    public void setEdit() {
        if (AppUtils.isEmpty(goods)||goods.size()==0) {
//            MyToast.show(mView, "还没有数据!");
            return;
        }
        if (isEdit) {
            isEdit = false;
            iv_edit.setImageResource(R.drawable.edit);
            adapter.getCheck(false, false);
            rl.setVisibility(View.GONE);
        } else {
            iv_edit.setImageResource(R.drawable.ic_ok);
            adapter.getCheck(true, false);
            rl.setVisibility(View.VISIBLE);
            isEdit = true;
        }
    }
    public void setCheckAll() {
        if (isCheck) {
            isCheck = false;
            adapter.getIsCheck(false, false);
        } else {
            isCheck = true;
            adapter.getIsCheck(true, false);
        }
    }

    /**
     * 删除收藏
     */
    public void senDeleteCollect() {
//        setShowDialog(true);
//        setShowDialog("正在删除收藏中!");
//        mView.showLoading();
        isLastDelete = false;
        adapter.getGoodCheck();

//            String id = goodCheckList.get(i).getId()+"";
            DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(this, "my-db", null);
            DaoMaster daoMaster = new DaoMaster(devOpenHelper.getWritableDatabase());
            DaoSession daoSession = daoMaster.newSession();
            DbGoodsBeanDao dbGoodsBeanDao=daoSession.getDbGoodsBeanDao();
        for (int i = 0; i < goodCheckList.size(); i++) {
            dbGoodsBeanDao.deleteByKey(goodCheckList.get(i).getG_id());
            if (i == goodCheckList.size() - 1) {
                isLastDelete = true;
            }
        }
        setEdit();
        loadData();

    }
    private class ProAdapter extends QuickAdapter<DbGoodsBean> {

        public ProAdapter(Context context, int layoutResId) {
            super(context, layoutResId);
        }

        public ProAdapter(Context context, int layoutResId, List<DbGoodsBean> data) {
            super(context, layoutResId, data);
        }

        @Override
        public int getCount() {
            if (null == goods)
                return 0;
            return goods.size();
        }

        @Override
        public DbGoodsBean getItem(int position) {
            if (null == goods)
                return null;
            return goods.get(position);
        }

        public void getCheck(boolean isCheck, boolean isStart) {
            if (AppUtils.isEmpty(goods))
                return;
            if (isStart) {
                for (int i = 0; i < goods.size(); i++) {
                    isCheckShowList.add(isCheck);
                }
            } else {
                for (int i = 0; i < goods.size(); i++) {
                    isCheckShowList.set(i, isCheck);
                }
            }
            notifyDataSetChanged();
        }

        private void getIsCheck(boolean isCheck, boolean isStart) {
            if (isStart) {
                isCheckList = new ArrayList<>();
                for (int i = 0; i < goods.size(); i++) {
                    isCheckList.add(isCheck);
                }
            } else {
                for (int i = 0; i < goods.size(); i++) {
                    isCheckList.set(i, isCheck);
                }
            }
            notifyDataSetChanged();
        }


        private void getGoodCheck() {
            goodCheckList = new ArrayList<>();
            for (int i = 0; i < isCheckList.size(); i++) {
                if (isCheckList.get(i)) {
                    goodCheckList.add(goods.get(i));
                }
            }
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        protected void convert(final BaseAdapterHelper helper, DbGoodsBean item) {
            helper.setText(R.id.name_tv,item.getName());
            helper.setText(R.id.priceTv,"￥" +item.getCurrent_price());
            helper.setText(R.id.price_old_Tv,"￥" +item.getPrice());
            ((TextView)helper.getView(R.id.price_old_Tv)).getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            ImageLoader.getInstance().displayImage(item.getOriginal_img(), (ImageView) helper.getView(R.id.imageView));
            CheckBox checkBox=helper.getView(R.id.checkbox);
            checkBox.setVisibility(isCheckShowList.get(helper.getPosition())?View.VISIBLE:View.INVISIBLE);
            checkBox.setChecked(isCheckList.get(helper.getPosition()));
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    isCheckList.set(helper.getPosition(), isChecked);
                }
            });
        }
    }
}
