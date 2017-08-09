package com.lyq.myapp.jeecg.frame;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lyq.myapp.R;
import com.lyq.myapp.jeecg.util.JeecgUtil;

/**
 * 系统设置
 */
public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    Button xiaoxin;
    Button sony;
    TextView serviceUrl;// 当前服务器路径

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_setting);
        initView();
    }

    // 初始化界面
    private void initView() {
        // 初始化按钮
        xiaoxin = (Button) findViewById(R.id.xiaoxing);
        sony = (Button) findViewById(R.id.sony);
        serviceUrl = (TextView) findViewById(R.id.serviceUrl);
        // 绑定按钮事件
        xiaoxin.setOnClickListener(this);
        sony.setOnClickListener(this);
        // 设置服务器路径
        serviceUrl.setText(JeecgUtil.JEECGPATH);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == xiaoxin.getId()) {
            JeecgUtil.JEECGPATH = JeecgUtil.JEECGPATH_XIAOXIN;
            serviceUrl.setText(JeecgUtil.JEECGPATH);
        } else if (v.getId() == sony.getId()) {
            JeecgUtil.JEECGPATH = JeecgUtil.JEECGPATH_SONY;
            serviceUrl.setText(JeecgUtil.JEECGPATH);
        }
    }

}
