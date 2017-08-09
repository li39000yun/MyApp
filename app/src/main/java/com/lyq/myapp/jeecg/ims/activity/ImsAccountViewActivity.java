package com.lyq.myapp.jeecg.ims.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lyq.myapp.R;
import com.lyq.myapp.jeecg.ims.entity.ImsAccountEntity;

/**
 * 查看账户信息
 */
public class ImsAccountViewActivity extends AppCompatActivity implements View.OnClickListener {

    private Button accountBack = null;
    private ImsAccountEntity account = null;
    private TextView accountTitle = null;// 标题
    private TextView accountAccount = null;// 用户名
    private TextView accountPassword = null;// 密码
    private TextView accountUrl = null;// 网址
    private TextView accountRemark = null;// 备注

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_view);
        initView();// 初始化界面
    }

    // 初始化界面
    private void initView() {
        // 初始化按钮
        accountBack = (Button) findViewById(R.id.accountBack);
        accountBack.setOnClickListener(this);
        // 初始化文本
        Intent intent = this.getIntent();
        account = (ImsAccountEntity) intent.getExtras().getSerializable("account");
        accountTitle = (TextView) findViewById(R.id.accountTitle);
        accountAccount = (TextView) findViewById(R.id.accountAccount);
        accountPassword = (TextView) findViewById(R.id.accountPassword);
        accountUrl = (TextView) findViewById(R.id.accountUrl);
        accountRemark = (TextView) findViewById(R.id.accountRemark);
        accountTitle.setText(account.getTitle());
        accountAccount.setText(account.getAccount());
        accountPassword.setText(account.getPassword());
        accountUrl.setText(account.getUrl());
        accountRemark.setText(account.getRemark());
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == accountBack.getId()) {
            // 返回
            finish();
        }
    }

}
