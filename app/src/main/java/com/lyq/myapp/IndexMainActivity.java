package com.lyq.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.lyq.myapp.jeecg.frame.SettingActivity;
import com.lyq.myapp.jeecg.ims.activity.ImsAccountListActivity;
import com.lyq.myapp.jeecg.work.activity.WorkLogListActivity;

public class IndexMainActivity extends AppCompatActivity implements View.OnClickListener {

    Button buttonDemand;// 需求单
    Button buttonWorkLog;// 工作日志
    Button buttonAccount;// 账户
    Button buttonSetting;// 设置

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.index_main);
        initView();
    }

    /**
     * 初始化界面
     */
    private void initView() {
        buttonAccount = (Button) findViewById(R.id.buttonAccount);
        buttonWorkLog = (Button) findViewById(R.id.buttonWorkLog);
        buttonDemand = (Button) findViewById(R.id.buttonDemand);
        buttonSetting = (Button) findViewById(R.id.buttonSetting);
        // 添加点击事件
        buttonAccount.setOnClickListener(this);
        buttonWorkLog.setOnClickListener(this);
        buttonDemand.setOnClickListener(this);
        buttonSetting.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        if (buttonAccount.getId() == v.getId()) {// 个人账户
            intent.setClass(this, ImsAccountListActivity.class);
        } else if (buttonWorkLog.getId() == v.getId()) {// 工作日志
            intent.setClass(this, WorkLogListActivity.class);
        } else if (buttonDemand.getId() == v.getId()) {// 需求单
            intent.setClass(this, WorkLogListActivity.class);
        } else if (buttonSetting.getId() == v.getId()) {// 设置
            intent.setClass(this, SettingActivity.class);
        }
        startActivity(intent);
    }
}
