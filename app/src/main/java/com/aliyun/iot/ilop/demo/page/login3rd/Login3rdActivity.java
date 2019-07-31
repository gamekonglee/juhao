package com.aliyun.iot.ilop.demo.page.login3rd;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.iot.aep.sdk.log.ALog;
import com.aliyun.iot.aep.sdk.threadpool.ThreadPool;

import java.io.IOException;

import bc.juhao.com.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by feijie.xfj on 18/1/11.
 */

public class Login3rdActivity extends Activity {
    private static final String TAG = "Login3rdActivity";
    private Button authLoginBtn;

    private EditText userNameET;

    private TextView titleTV;

    private void queryAuthCode() {
        ThreadPool.DefaultThreadPool.getInstance().submit(new Runnable() {
            @Override
            public void run() {
                String url = " http://11.160.241.217/admin/oauth2/authorize?client_id=test" +
                        "&response_type=code&username=" + userNameET.getText() + "&redirect_uri=http://iot.example.com/code_callback";
                OkHttpClient okHttpClient = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(url)
                        .build();
                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        ALog.i(TAG, e.toString());
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String data = response.body().string();
                        try {
                            JSONObject j = JSONObject.parseObject(data);
                            if (j.containsKey("code")) {
                                ALog.i(TAG, "code: " + j.getString("code"));
                                Intent intent = new Intent();
                                intent.putExtra("authCode", j.getString("code"));
                                setResult(RESULT_OK, intent);
                                finish();
                            }
                        } catch (Exception e) {

                        }
                        ALog.i(TAG, response.body().string());
                    }
                });

            }
        });
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login3rd);
        userNameET = findViewById(R.id.ilop_3rd_auth_login_username);
        authLoginBtn = findViewById(R.id.ilop_3rd_auth_login_btn);
        titleTV = findViewById(R.id.topbar_title_textview);

        titleTV.setText("三方授权登录");
        authLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(userNameET.getText())) {
                    Toast.makeText(getApplicationContext(), "用户名不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                queryAuthCode();
            }
        });
    }
}
