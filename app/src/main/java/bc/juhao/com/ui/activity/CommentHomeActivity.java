package bc.juhao.com.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;

import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import bc.juhao.com.R;
import bc.juhao.com.adapter.BaseAdapterHelper;
import bc.juhao.com.adapter.QuickAdapter;
import bc.juhao.com.bean.Author;
import bc.juhao.com.bean.CommentBean;
import bc.juhao.com.bean.PostImageVideoBean;
import bc.juhao.com.common.BaseActivity;
import bc.juhao.com.cons.Constance;
import bc.juhao.com.cons.NetWorkConst;
import bc.juhao.com.net.ApiClient;
import bc.juhao.com.ui.activity.user.SuccessVideoActivity;
import bc.juhao.com.ui.adapter.SunImageAdapter;
import bc.juhao.com.ui.adapter.SunImageNoAddAdapter;
import bc.juhao.com.utils.DateUtils;
import bc.juhao.com.utils.ImageLoadProxy;
import bc.juhao.com.utils.UIUtils;
import bocang.utils.MyToast;
import okhttp3.Call;

public class CommentHomeActivity extends BaseActivity {

    private List<CommentBean> commentBeans;
    private ProgressDialog pd;

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
        setContentView(R.layout.activity_comment_home);
        setColor(this, Color.WHITE);
        ListView lv_comment=findViewById(R.id.lv_comment);
        QuickAdapter<CommentBean> adapter=new QuickAdapter<CommentBean>(this,R.layout.view_sunimage) {
            @Override
            protected void convert(BaseAdapterHelper helper, final CommentBean item) {
                Author author=item.getAuthor();
                if(author==null){
                    author=new Author();
                    if(IssueApplication.mUserObject!=null){
                    author.setUsername(IssueApplication.mUserObject.getString(Constance.username));
                    author.setAvatar(IssueApplication.mUserObject.getString(Constance.avatar));
                    }else {
                        author.setUsername("");
                        author.setAvatar("");
                    }
                }
                helper.setText(R.id.tv_nickname,author.getUsername());
                helper.setText(R.id.tv_time, DateUtils.getStrTime(item.getUpdated_at()+""));
                helper.setText(R.id.tv_comment,item.getContent());
                ImageView iv_avard=helper.getView(R.id.iv_avard);
                ImageLoadProxy.displayImage(NetWorkConst.SCENE_HOST+author.getAvatar(),iv_avard);
                GridView gv_goods_img=helper.getView(R.id.gv_goods_img);
                final List<PostImageVideoBean> postImageVideoBeans=new ArrayList<>();
                for(int i=0;i<item.getMovies().size();i++){
                    PostImageVideoBean temp=new PostImageVideoBean();
                    temp.isVideo=true;
                    temp.url=item.getMovies().get(i);
                    postImageVideoBeans.add(temp);
                }
                for(int j=0;j<item.getPath().size();j++){
                    PostImageVideoBean temp=new PostImageVideoBean();
                    temp.isVideo=false;
                    temp.url=item.getPath().get(j);
                    postImageVideoBeans.add(temp);
                }
                SunImageNoAddAdapter mdapter = new SunImageNoAddAdapter(CommentHomeActivity.this, postImageVideoBeans);
                gv_goods_img.setAdapter(mdapter);
                gv_goods_img.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        if(postImageVideoBeans.get(position).isVideo){
                            pd = ProgressDialog.show(CommentHomeActivity.this,"","加载中");
                            ApiClient.downloadMp4(NetWorkConst.SCENE_HOST+postImageVideoBeans.get(position).url, new FileCallBack("","") {
                                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
                                @Override
                                public void onError(Call call, Exception e, int id) {
                                    if(UIUtils.isValidContext(CommentHomeActivity.this)&&pd!=null&&pd.isShowing()){
                                        pd.dismiss();
                                    }
                                    MyToast.show(CommentHomeActivity.this,"网络异常");
                                }

                                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
                                @Override
                                public String onResponse(File response, int id) {
                                    if(UIUtils.isValidContext(CommentHomeActivity.this)&&pd!=null&&pd.isShowing()){
                                        pd.dismiss();
                                    }
                                    Intent intent = new Intent(CommentHomeActivity.this, SuccessVideoActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putString("text", response.getAbsolutePath());
                                    bundle.putBoolean(Constance.is_look,true);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                    return null;
                                }
                            });

                        }else {
                        Intent intent = new Intent(CommentHomeActivity.this, DetailPhotoActivity.class);
                        ArrayList arrayList= (ArrayList) item.getPath();
                        intent.putExtra(Constance.IMAGESHOW, arrayList);
                        intent.putExtra(Constance.IMAGEPOSITION, 0);
                        startActivity(intent);
                        }
                    }
                });
            }
        };
        lv_comment.setAdapter(adapter);
        commentBeans = IssueApplication.getCommentList();
        IssueApplication.setCommentList(null);
        adapter.replaceAll(commentBeans);
//        Bundle bundle=getIntent().getExtras();
//        if(bundle!=null){
//
//        }
    }

    @Override
    protected void initData() {

    }
}
