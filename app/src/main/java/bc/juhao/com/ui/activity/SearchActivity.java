package bc.juhao.com.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import bc.juhao.com.R;
import bc.juhao.com.common.BaseActivity;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.controller.SearchController;
import bc.juhao.com.ui.activity.product.ClassifyGoodsActivity;
import bc.juhao.com.ui.activity.product.SelectGoodsActivity;
import bc.juhao.com.ui.view.LineBreakLayout;
import bocang.json.JSONArray;
import bocang.json.JSONObject;
import bocang.utils.MyToast;

public class SearchActivity extends BaseActivity {

    private LineBreakLayout lineBreakLayout;
    private SearchController searchController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search2);
        setColor(this, Color.WHITE);
        final EditText et_search=findViewById(R.id.et_search_home);
        TextView tv_search=findViewById(R.id.tv_search);
        tv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SearchActivity.this,SelectGoodsActivity.class);
                if(et_search.getText()==null)
                {
                    return;
                }
                intent.putExtra("key",et_search.getText().toString().trim());
                setResult(120,intent);
                finish();
            }
        });
        et_search.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // TODO Auto-generated method stub
                // 修改回车键功能
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    // 先隐藏键盘
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(
                                            getCurrentFocus().getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                }
                Intent intent=new Intent(SearchActivity.this,SelectGoodsActivity.class);
                if(et_search.getText()==null)
                {
                    return false;
                }
                intent.putExtra("key",et_search.getText().toString().trim());
                setResult(120,intent);
                finish();
                return false;
            }
        });
        List<String > hot=new ArrayList<>();
        hot.add("现代简约");
        hot.add("吊灯");
        hot.add("铁艺灯");
        hot.add("新中式");
        hot.add("吊灯");
        hot.add("中式");
        hot.add("欧式");
        lineBreakLayout = findViewById(R.id.lbl);
        lineBreakLayout.setLables(hot,true);
        lineBreakLayout.setOnItemClick(new LineBreakLayout.OnItemClickListener() {
            @Override
            public void setOnItemClick(String lable) {
            JSONArray jsonArray=searchController.mClassifyGoodsLists;
            if(jsonArray==null){
                MyToast.show(SearchActivity.this,"数据还没加载完毕");
                return;
            }
            for(int i=0;i<jsonArray.length();i++){
                JSONArray categories=jsonArray.getJSONObject(i).getJSONArray(Constance.categories);
                for(int j=0;j<categories.length();j++){
                    String name=categories.getJSONObject(j).getString(Constance.name);
                    if(name.contains(lable)){
                        String categoriesId = categories.getJSONObject(j).getString(Constance.id);
                        Intent mIntent=new Intent(SearchActivity.this, SelectGoodsActivity.class);
                        mIntent.putExtra(Constance.categories, categoriesId);
                        mIntent.putExtra(Constance.name,lable);
                        startActivity(mIntent);
                        if(IssueApplication.isClassify==true){
                            IssueApplication.isClassify=false;
                        }
                        finish();
                        break;
                    }
                }
            }
            }
        });
    }

    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {
        searchController = new SearchController(this);

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }
}
