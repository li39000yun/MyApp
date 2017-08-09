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
public class WorkLogViewActivity extends AppCompatActivity implements View.OnClickListener {

    private Button addWorkEdit = null;
    private Button addWorkBack = null;
    private WorkLogEntity workLog = null;
    private TextView addWorkTitle = null;
    private TextView addWorkContent = null;
    private TextView addWorkDate = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.work_log_view);
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
        if (v.getId() == addWorkEdit.getId()) {
            // 跳转到修改日志界面
            Intent intent = new Intent();
            intent.setClass(this, WorkLogEditActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("workLog", workLog);// 设置id
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (v.getId() == addWorkBack.getId()) {
            // 返回
            finish();
        }
    }
}
