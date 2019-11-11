package bc.juhao.com.ui.view.popwindow;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.ArrayMap;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bc.juhao.com.R;
import bc.juhao.com.adapter.BaseAdapterHelper;
import bc.juhao.com.adapter.QuickAdapter;
import bc.juhao.com.bean.Categories;
import bocang.utils.LogUtils;


/**
 * @author huanzhi
 * @date 2019/10/4
 * ClassName FilterPopWindow
 * Description 二级筛选popupWindow
 */
public class FilterPopWindow extends BasePopwindown implements View.OnClickListener {

    private ImageView iv_back;
    private TextView tv_title;
    private ListView lv_selected_category;
    private TextView tv_ensure;

    private Context mContext;
    private List<Categories> mSecondLevelCategory; //二级category数据
    private Map<Integer, Boolean> mCategoryIds;//已选中的category id(点击确定后返回)
    private String mCategoryName;// toolbar上显示的一级分类名称
    private Map<Integer, Boolean> mTempCategoryIds;//传进来的category id(没有点击确定按钮返回的数据)

    private QuickAdapter<Categories> mCategoryAdapter;

    public FilterPopWindow(Context context, List<Categories> secondLevelCategory, Map<Integer, Boolean> categoryIds, String categotyName) {
        super(context);
        this.mContext = context;
        this.mSecondLevelCategory = secondLevelCategory;
        this.mCategoryIds = categoryIds;
        this.mCategoryName = categotyName;

        initData();
//        mCategoryIds = new HashMap<>();
        mTempCategoryIds = new HashMap<>();
        for (Map.Entry<Integer, Boolean> entry : categoryIds.entrySet()) {
//            mCategoryIds.put(entry.getKey(), entry.getValue());
            mTempCategoryIds.put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    protected void initView(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.pop_filter_layout, null, false);
        iv_back = view.findViewById(R.id.iv_back);
        tv_title = view.findViewById(R.id.tv_title);
        lv_selected_category = view.findViewById(R.id.lv_selected_category);
        tv_ensure = view.findViewById(R.id.tv_ensure);

        iv_back.setOnClickListener(this);
        tv_ensure.setOnClickListener(this);

//        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
//        int height = context.getResources().getDimensionPixelSize(resourceId);
        mPopupWindow = new PopupWindow(view, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        mPopupWindow.setAnimationStyle(R.style.popupWindowAnimRight);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.showAtLocation(view, Gravity.TOP, 85, 0);

    }

    private void initData() {
        tv_title.setText(mCategoryName);

        mCategoryAdapter = new QuickAdapter<Categories>(mContext, R.layout.item_filter_second, mSecondLevelCategory) {

            @Override
            protected void convert(BaseAdapterHelper helper, final Categories item) {
                LinearLayout ll_selected_category = helper.getView(R.id.ll_selected_category);
                final CheckBox cb_selected = helper.getView(R.id.cb_selected);
                helper.setText(R.id.tv_category, item.getName());

                cb_selected.setVisibility(item.isSelected() ? View.VISIBLE : View.GONE);

                ll_selected_category.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        if (item.isSelected()) {
                            cb_selected.setVisibility(View.GONE);
                            item.setSelected(false);
                            mCategoryIds.remove(item.getId());
                        } else {
                            cb_selected.setVisibility(View.VISIBLE);
                            item.setSelected(true);
                            mCategoryIds.put(item.getId(), true);

                            LogUtils.logE("category", "mCategoryIds:" + mCategoryIds.toString());
                            LogUtils.logE("category", "mTempCategoryIds:" + mTempCategoryIds.toString());
                        }
                    }


                });

            }
        };


        lv_selected_category.setAdapter(mCategoryAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
////                mCategoryIds.clear();
//                for (int i=0; i<mSecondLevelCategory.size(); i++){
//
//                    for (Map.Entry<Integer, Boolean> entry : mTempCategoryIds.entrySet()) {
//                        if (entry.getKey().equals(mSecondLevelCategory.get(i).getId())) {
//                            mSecondLevelCategory.get(i).setSelected(true);
//                        } else {
//                            mSecondLevelCategory.get(i).setSelected(false);
//                        }
//                    }
//
//                }
//                mCategoryAdapter.notifyDataSetChanged();
//                meanCallBack.transmitData(mTempCategoryIds);
//                break;
            case R.id.tv_ensure:
                meanCallBack.transmitData(mCategoryIds);
                break;
            default:
//                for (int i=0; i<mSecondLevelCategory.size(); i++){
//
//                    for (Map.Entry<Integer, Boolean> entry : mTempCategoryIds.entrySet()) {
//                        if (entry.getKey().equals(mSecondLevelCategory.get(i).getId())) {
//                            mSecondLevelCategory.get(i).setSelected(true);
//                        } else {
//                            mSecondLevelCategory.get(i).setSelected(false);
//                        }
//                    }
//
//                }
                mCategoryAdapter.notifyDataSetChanged();
                break;
        }
    }


    public interface onMeanCallBack {
        void transmitData(Map<Integer, Boolean> categoryIds);
    }

    private onMeanCallBack meanCallBack;

    public void setOnMeanCallBack(onMeanCallBack callBack) {
        meanCallBack = callBack;
    }

}
