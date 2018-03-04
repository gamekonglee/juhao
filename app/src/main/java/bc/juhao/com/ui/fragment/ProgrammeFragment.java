package bc.juhao.com.ui.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lib.common.hxp.view.PullToRefreshLayout;
import com.lib.common.hxp.view.PullableGridView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.List;

import bc.juhao.com.R;
import bc.juhao.com.bean.Classify;
import bc.juhao.com.bean.Programme;
import bc.juhao.com.common.BaseFragment;
import bc.juhao.com.controller.programme.ProgrammeController;
import bc.juhao.com.listener.ILinearLayoutListener;
import bc.juhao.com.ui.activity.MainActivity;
import bc.juhao.com.ui.activity.programme.DiyActivity;
import bc.juhao.com.ui.adapter.GridviewAdapter;
import bc.juhao.com.ui.view.MyLinearLayout;
import bc.juhao.com.ui.view.SearchNestedScrollParent;
import bc.juhao.com.utils.UIUtils;
import bocang.utils.AppUtils;
import bocang.utils.IntentUtil;
import bocang.utils.MyToast;

/**
 * @author Jun
 * @time 2017/1/5  12:00
 * @desc 配灯方案页面
 */
public class ProgrammeFragment extends BaseFragment implements View.OnClickListener {
    private ProgrammeController mController;
    private RelativeLayout add_rl;
    public List<Programme> mProgrammes;
    private FrameLayout main_fl;
    private LinearLayout square_ll,my_works_ll,select_ll;
    private TextView square_tv,my_works_tv,select_tv;
    private View square_view,my_works_view,select_view;
    // 声明PopupWindow对象的引用
    private PopupWindow popupWindow;
    //分类的gridview
    private GridView gridView1;
    //产地的gridview
    private GridView gridView2;
    //popupWindow的布局view
    private View popupWindow_view;
    private List<Classify> classifyList;
    private List<Classify> producingList;
    private GridviewAdapter mydapter;
    private GridviewAdapter mydapter2;
    public int mProgrammeType=0;
    private SearchNestedScrollParent contentView;
    private MyLinearLayout contentView_ll;
    public  PullToRefreshLayout mPullToRefreshLayout;
    public PullableGridView order_sv;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fm_programme, null);

    }

    @Override
    protected void initController() {
        mController = new ProgrammeController(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        if(!isToken()){
            if(AppUtils.isEmpty(mController.mSchemes)){
                mController.page=1;
                mController.sendFangAnList();
            }
            if(MainActivity.toFilter){
                MainActivity.toFilter=false;
                select_ll.performClick();
            }
        }

    }

    @Override
    protected void initViewData() {
        String[] styleArrs = UIUtils.getStringArr(R.array.style);
        String[] spaceArrs = UIUtils.getStringArr(R.array.space);
        classifyList = new ArrayList<>();
        for(int i=0;i<styleArrs.length;i++)
        {
            classifyList.add(new Classify(styleArrs[i]));
        }
        producingList = new ArrayList<>();
        for(int i=0;i<spaceArrs.length;i++)
        {
            producingList.add(new Classify(spaceArrs[i]));
        }

    }

    @Override
    protected void initView() {
        add_rl =  getView().findViewById(R.id.add_rl);
        add_rl.setOnClickListener(this);
        select_ll = (LinearLayout)  getView().findViewById(R.id.select_ll);
        select_ll.setOnClickListener(this);
        my_works_ll = (LinearLayout) getView().findViewById(R.id.my_works_ll);
        my_works_ll.setOnClickListener(this);
        square_ll = (LinearLayout)  getView().findViewById(R.id.square_ll);
        square_ll.setOnClickListener(this);
        main_fl = (FrameLayout) getActivity().findViewById(R.id.main_fl);
        square_tv = (TextView)  getView().findViewById(R.id.square_tv);
        my_works_tv = (TextView)  getView().findViewById(R.id.my_works_tv);
        select_tv = (TextView) getView().findViewById(R.id.select_tv);
        square_view =   getView().findViewById(R.id.square_view);
        my_works_view = getView().findViewById(R.id.my_works_view);
        select_view =   getView().findViewById(R.id.select_view);
        contentView = (SearchNestedScrollParent) getView().findViewById(R.id.contentView);
        order_sv = (PullableGridView) getView().findViewById(R.id.gridView);
        contentView_ll = (MyLinearLayout)  getView().findViewById(R.id.contentView_ll);
        mPullToRefreshLayout = ((PullToRefreshLayout) getActivity().findViewById(R.id.mFilterContentView));
        contentView_ll.setOnMeasure(true);
        contentView_ll.setListener(new ILinearLayoutListener() {
            @Override
            public void onLinearLayoutListener(boolean isMeasure) {
                mPullToRefreshLayout.mMeasuredHeight = contentView_ll.mMeasuredHeight;
                mPullToRefreshLayout.setOnMeasure(true);
                order_sv.mTopHeight = contentView_ll.mTopHeight;
                order_sv.setOnMeasure(true);
            }
        });
    }



    @Override
    protected void initData() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
//        MyToast.show(getContext(),""+v.getId());
        switch (v.getId()) {
            case R.id.add_rl://新增
                IntentUtil.startActivity(this.getActivity(), DiyActivity.class,false);
                break;
            case R.id.select_ll://筛选
                getPopupWindow();
                popupWindow.showAtLocation(main_fl, Gravity.RIGHT, 0, 0);
                break;
            case R.id.my_works_ll://我的作品
                mProgrammeType=1;
                selectProgrammeType();
                mController.getRefershData();
                break;
            case R.id.square_ll://广场
                mProgrammeType=0;
                selectProgrammeType();
                mController.getRefershData();
                break;
        }
    }

    /**
     * 选择方案类型状态改变
     */
    private void selectProgrammeType(){
        square_tv.setTextColor(getActivity().getResources().getColor(R.color.txt_black));
        my_works_tv.setTextColor(getActivity().getResources().getColor(R.color.txt_black));
        square_view.setVisibility(View.GONE);
        my_works_view.setVisibility(View.GONE);
        switch (mProgrammeType){
            case 0://广场
                square_tv.setTextColor(getActivity().getResources().getColor(R.color.green));
                square_view.setVisibility(View.VISIBLE);
                break;
            case 1://我的作品
                my_works_tv.setTextColor(getActivity().getResources().getColor(R.color.green));
                my_works_view.setVisibility(View.VISIBLE);
                break;

        }
    }



    //在这获取popupwindow的实例
    private void getPopupWindow() {
        if (null != popupWindow) {
            popupWindow.dismiss();
            return;
        } else {
            initPopuptWindow();
        }
    }

    /**
     * 创建 PopuptWindow的实例
     */
    public void initPopuptWindow() {
        popupWindow_view = View.inflate(getActivity(), R.layout.pop_programme_select, null);
        // 创建PopupWindow实例,LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT分别是宽度和高度
        popupWindow = new PopupWindow(popupWindow_view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        // 设置动画效果
        popupWindow.setAnimationStyle(R.style.AnimationFade);

        gridView1 = (GridView) popupWindow_view.findViewById(R.id.gridview1);
        gridView2 = (GridView) popupWindow_view.findViewById(R.id.gridview2);
        Button btnOk = (Button) popupWindow_view.findViewById(R.id.btn_ok);
        Button btnCancal = (Button) popupWindow_view.findViewById(R.id.btn_cancal);
        LinearLayout main_ll = (LinearLayout) popupWindow_view.findViewById(R.id.main_ll);
        main_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //关闭popupWindow
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                    popupWindow = null;
                }
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = "";
                String str2 = "";
                //取出选中的属性
                for (int i = 0; i < classifyList.size(); i++) {
                    if (classifyList.get(i).isChecked()) {
                        str = classifyList.get(i).getStr();
                    }
                }
                //取出选中的属性
                for (int i = 0; i < producingList.size(); i++) {
                    if (producingList.get(i).isChecked()) {
                        str2 = producingList.get(i).getStr();
                    }
                }
                if(AppUtils.isEmpty(str) && AppUtils.isEmpty(str2)){
                    MyToast.show(ProgrammeFragment.this.getActivity(),"请选择条件!");
                    return;
                }

                if(str.equals("全部")){
                    mController.mStyle="";
                }else{
                    mController.mStyle=str;
                }
                if(str2.equals("全部")){
                    mController.mSpace="";
                }else{
                    mController.mSpace=str2;
                }
                mController.getRefershData();
                //关闭popupWindow
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                    popupWindow = null;
                }
            }
        });

        btnCancal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < classifyList.size(); i++) {
                    classifyList.get(i).setChecked(false);
                }
                for (int i = 0; i < producingList.size(); i++) {
                    producingList.get(i).setChecked(false);
                }
                mydapter.getSaveList().clear();
                mydapter.notifyDataSetChanged();
                mydapter2.notifyDataSetChanged();
            }
        });

        mydapter = new GridviewAdapter(classifyList, ProgrammeFragment.this.getActivity());
        gridView1.setAdapter(mydapter);
        gridView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //如果选中就设置非选中。非选中就设置选中
                classifyList.get(position).setChecked(!classifyList.get(position).isChecked());
                //只有一条目选中
                for (int i = 0; i < classifyList.size(); i++) {
                    if (i == position) {
                        continue;
                    }
                    classifyList.get(i).setChecked(false);
                }
                mydapter.notifyDataSetChanged();
            }
        });


        mydapter2 = new GridviewAdapter(producingList,ProgrammeFragment.this.getActivity());
        gridView2.setAdapter(mydapter2);
        gridView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //如果选中就设置非选中。非选中就设置选中
                producingList.get(position).setChecked(!producingList.get(position).isChecked());
                //只有一条目选中
                for (int i = 0; i < producingList.size(); i++) {
                    if (i == position) {
                        continue;
                    }
                    producingList.get(i).setChecked(false);
                }
                mydapter2.notifyDataSetChanged();
            }
        });


        //对popupWindows进行触摸监听
        popupWindow_view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                boolean b = inRangeOfView(popupWindow_view, event);
                if (b) {
                    return true;
                } else
                    return false;
            }
        });
    }

    /**
     * 用于判断点击范围是否在弹出框内，点击外面就收起popupWindows
     */
    private boolean inRangeOfView(View view, MotionEvent ev) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int x = location[0];
        int y = location[1];
        if (ev.getX() < x || ev.getX() > (x + view.getWidth()) || ev.getY() < y || ev.getY() > (y + view.getHeight())) {
            if (popupWindow != null && popupWindow.isShowing()) {
                popupWindow.dismiss();
                popupWindow = null;
            }
            return false;
        }
        return true;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            popupWindow = null;
        }
    }
}
