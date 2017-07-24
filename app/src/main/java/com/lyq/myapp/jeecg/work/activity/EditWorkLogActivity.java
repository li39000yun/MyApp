package com.lyq.myapp.jeecg.work.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lyq.myapp.R;
import com.lyq.myapp.jeecg.util.DateUtilsEx;
import com.lyq.myapp.jeecg.work.entity.WorkLogEntity;

/**
 * 查看日志
 */
public class EditWorkLogActivity extends AppCompatActivity implements View.OnClickListener {

    private Button addWorkEdit = null;
    private Button addWorkBack = null;
    private WorkLogEntity workLog = null;
    private TextView addWorkTitle = null;
    private TextView addWorkContent = null;
    private TextView addWorkDate = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_work_log);
        initView();// 初始化界面
    }

    // 初始化界面
    private void initView() {
        // 初始化按钮
        addWorkEdit = (Button) findViewById(R.id.addWorkEdit);
        addWorkEdit.setOnClickListener(this);// 设置按钮事件
        addWorkBack = (Button) findViewById(R.id.addWorkBack);
        addWorkBack.setOnClickListener(this);
        Intent intent = this.getIntent();
        workLog = (WorkLogEntity) intent.getExtras().getSerializable("workLog");
        addWorkTitle = (TextView) findViewById(R.id.addWorkTitle);
        addWorkContent = (TextView) findViewById(R.id.addWorkContent);
        addWorkDate = (TextView) findViewById(R.id.addWorkDate);
        addWorkTitle.setText(workLog.getTitle());
        addWorkContent.setText(workLog.getContent());
        addWorkDate.setText(DateUtilsEx.formatDate(workLog.getDate()));
    }

    @Override
    public void onClick(View v) {
        // 提交
        if (v.getId() == addWorkEdit.getId()) {
        } else if (v.getId() == addWorkBack.getId()) {
            // 返回
            finish();
        }
    }
}
