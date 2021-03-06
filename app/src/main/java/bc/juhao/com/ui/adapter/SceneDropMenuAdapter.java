package bc.juhao.com.ui.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

import com.aliyun.iot.ilop.demo.DemoApplication;
import com.baiiu.filter.adapter.MenuAdapter;
import com.baiiu.filter.adapter.SimpleTextAdapter;
import com.baiiu.filter.interfaces.OnFilterDoneListener;
import com.baiiu.filter.interfaces.OnFilterItemClickListener;
import com.baiiu.filter.typeview.SingleGridView;
import com.baiiu.filter.util.UIUtil;
import com.baiiu.filter.view.FilterCheckedTextView;

import java.util.ArrayList;
import java.util.List;

import bc.juhao.com.R;
import bc.juhao.com.cons.Constance;
import bocang.json.JSONArray;
import bocang.json.JSONObject;


public class SceneDropMenuAdapter implements MenuAdapter {
    private final Context mContext;
    private OnFilterDoneListener onFilterDoneListener;
    private JSONArray sceneAllAttrs;
    private List<Integer> itemPosList;
    private String name;

    public SceneDropMenuAdapter(Context context, JSONArray sceneAllAttrs, List<Integer> itemPosList, OnFilterDoneListener onFilterDoneListener) {
        this.mContext = context;
        this.sceneAllAttrs = sceneAllAttrs;
        this.onFilterDoneListener = onFilterDoneListener;
        this.itemPosList = itemPosList;
    }

    @Override
    public int getMenuCount() {
        return sceneAllAttrs.length();
    }

    @Override
    public String getMenuTitle(int position) {
        if (position < itemPosList.size()) {
            int itemPos = itemPosList.get(position);
            if (itemPos != 0) {
                if(DemoApplication.SCENE_TYPE==3){
                    return sceneAllAttrs.getJSONObject(position).getJSONArray(Constance.attr_list).getJSONObject(itemPos).getString(Constance.name);
                }else {
                    return sceneAllAttrs.getJSONObject(position).getJSONArray(Constance.attrVal).getString(itemPos);
                }
            }
        }
        if(DemoApplication.SCENE_TYPE==3){
            name=sceneAllAttrs.getJSONObject(position).getString(Constance.filter_attr_name);
        }else {
            name = sceneAllAttrs.getJSONObject(position).getString(Constance.attr_name);

        }
        return name;
    }

    @Override
    public int getBottomMargin(int position) {

        return 0;
    }

    @Override
    public View getView(int position, FrameLayout parentContainer) {
        return createSingleGridView(position);
    }

    private View createSingleGridView(final int position) {
        SingleGridView<String> singleGridView = new SingleGridView<String>(mContext)
                .adapter(new SimpleTextAdapter<String>(null, mContext) {
                    @Override
                    public String provideText(String s) {
                        return s;
                    }

                    @Override
                    protected void initCheckedTextView(FilterCheckedTextView checkedTextView) {
                        checkedTextView.setPadding(0, UIUtil.dp(context, 3), 0, UIUtil.dp(context, 3));
                        checkedTextView.setGravity(Gravity.CENTER);
                        checkedTextView.setBackgroundResource(R.drawable.selector_filter_grid);
                    }
                })
                .onItemClick(new OnFilterItemClickListener<String>() {
                    @Override
                    public void onItemClick(int itemPos, String itemStr) {

                        if (onFilterDoneListener != null) {
                            onFilterDoneListener.onFilterDone(position, itemPos, itemStr);
                        }

                    }
                });

        List<String> list = new ArrayList<>();

        JSONObject goodsAllAttr = sceneAllAttrs.getJSONObject(position);
        JSONArray attr_list;
        if(DemoApplication.SCENE_TYPE==3){
            attr_list=goodsAllAttr.getJSONArray(Constance.attr_list);
            for (int i = 0; i < attr_list.length(); ++i) {
                list.add(attr_list.getJSONObject(i).getString(Constance.name));
            }
        }else {
            attr_list = goodsAllAttr.getJSONArray(Constance.attrVal);
            for (int i = 0; i < attr_list.length(); ++i) {
                list.add(attr_list.getString(i));
            }
        }


        int itemPos = 0;
        if (position < itemPosList.size())
            itemPos = itemPosList.get(position);
        singleGridView.setList(list, itemPos);

        return singleGridView;
    }
}
