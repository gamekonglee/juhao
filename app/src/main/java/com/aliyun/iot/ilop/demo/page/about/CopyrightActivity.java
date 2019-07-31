package com.aliyun.iot.ilop.demo.page.about;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.aliyun.iot.aep.sdk.framework.AActivity;

import bc.juhao.com.R;

/**
 * Created by feijie.xfj on 17/11/19.
 */

public class CopyrightActivity extends AActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.copyright_activity);

        ((TextView) this.findViewById(R.id.topbar_title_textview)).setText(R.string.copyright_title_text);
        this.findViewById(R.id.topbar_back_imageview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
