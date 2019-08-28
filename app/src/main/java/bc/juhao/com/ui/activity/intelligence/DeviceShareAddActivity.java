package bc.juhao.com.ui.activity.intelligence;

import android.content.Intent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import bc.juhao.com.R;
import bc.juhao.com.bean.AccountDevDTO;
import bc.juhao.com.common.BaseActivity;
import bc.juhao.com.cons.Constance;


public class DeviceShareAddActivity extends BaseActivity {

    private TextView tv_title;
    private TextView tv_share_add;
    private ListView lv_devices_sharers;
    private AccountDevDTO accountDevDTO;

    @Override
    protected void InitDataView() {
        Intent intent=getIntent();
        if(intent!=null){
            accountDevDTO = (AccountDevDTO) getIntent().getSerializableExtra(Constance.data);
            if(accountDevDTO !=null){
                tv_title.setText(""+ accountDevDTO.getProductName());
            }
        }
    }

    @Override
    protected void initController() {

    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_device_share_add);
        tv_title = findViewById(R.id.tv_title);
        tv_share_add = findViewById(R.id.tv_share_add);
        lv_devices_sharers = findViewById(R.id.lv_devices_sharers);
        tv_share_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(DeviceShareAddActivity.this,DeviceShareAddUserActivity.class);
                intent.putExtra(Constance.data,accountDevDTO);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void initData() {

    }
}
