package com.lyq.myapp.jeecg.work.activity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.lyq.myapp.R;
import com.lyq.myapp.jeecg.util.DateUtilsEx;
import com.lyq.myapp.jeecg.util.JeecgUtil;
import com.lyq.myapp.jeecg.util.JsonUtil;
import com.lyq.myapp.jeecg.util.TableJson;
import com.lyq.myapp.jeecg.work.WorkLogService;
import com.lyq.myapp.jeecg.work.entity.WorkLogEntity;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * 查看日志
 */
public class WorkLogEditActivity extends AppCompatActivity implements View.OnClickListener {

    private Button addWorkSumbit = null;
    private Button addWorkBack = null;
    private WorkLogEntity workLog = null;
    private EditText addWorkTitle = null;
    private EditText addWorkContent = null;
    private EditText addWorkDate = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.work_log_edit);
        initView();// 初始化界面
    }

    // 初始化界面
    private void initView() {
        // 初始化按钮
        addWorkSumbit = (Button) findViewById(R.id.addWorkSumbit);
        addWorkSumbit.setOnClickListener(this);// 设置按钮事件
        addWorkBack = (Button) findViewById(R.id.addWorkBack);
        addWorkBack.setOnClickListener(this);
        Intent intent = this.getIntent();
        workLog = (WorkLogEntity) intent.getExtras().getSerializable("workLog");
        addWorkTitle = (EditText) findViewById(R.id.addWorkTitle);
        addWorkContent = (EditText) findViewById(R.id.addWorkContent);
        addWorkDate = (EditText) findViewById(R.id.addWorkDate);
        addWorkTitle.setText(workLog.getTitle());
        addWorkContent.setText(workLog.getContent());
        addWorkDate.setText(DateUtilsEx.formatDate(workLog.getDate()));
        addWorkDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    showDatePickDlg();
                    return true;
                }
                return false;
            }
        });
        addWorkDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showDatePickDlg();
                }
            }
        });
    }

    private void showDatePickDlg() {
        DatePickerDialog dateDlg = new DatePickerDialog(WorkLogEditActivity.this,
                d,
                dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH));
        dateDlg.show();
    }

    //获取日期格式器对象
    DateFormat fmtDate = new java.text.SimpleDateFormat("yyyy-MM-dd");
    //获取一个日历对象
    Calendar dateAndTime = Calendar.getInstance(Locale.CHINA);
    //当点击DatePickerDialog控件的设置按钮时，调用该方法
    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            //修改日历控件的年，月，日
            //这里的year,monthOfYear,dayOfMonth的值与DatePickerDialog控件设置的最新值一致
            dateAndTime.set(Calendar.YEAR, year);
            dateAndTime.set(Calendar.MONTH, monthOfYear);
            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            //将页面TextView的显示更新为最新时间
            upDateDate();
        }
    };

    // 将页面TextView的显示更新为最新时间
    private void upDateDate() {
        addWorkDate.setText(fmtDate.format(dateAndTime.getTime()));
    }

    @Override
    public void onClick(View v) {
        // 提交
        if (v.getId() == addWorkSumbit.getId()) {
            addWorkTitle = (EditText) findViewById(R.id.addWorkTitle);
            addWorkContent = (EditText) findViewById(R.id.addWorkContent);
            addWorkDate = (EditText) findViewById(R.id.addWorkDate);
            workLog.setTitle(addWorkTitle.getText().toString());
            workLog.setContent(addWorkContent.getText().toString());
            workLog.setDate(addWorkDate.getText().toString());

            // 开启一个子线程，进行网络操作，等待有返回结果，使用handler通知UI
            new Thread(editWorkTask).start();
        } else if (v.getId() == addWorkBack.getId()) {
            // 返回
            finish();
        }
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String val = data.getString("value");
            Log.i("mylog", "请求结果为-->" + val);
            // TODO
            // UI界面的更新等相关操作
            String rvalue = data.getString("rvalue");
            TableJson tableJson = (TableJson) JsonUtil.stringToObject(rvalue, TableJson.class);
            new AlertDialog.Builder(WorkLogEditActivity.this)
                    .setTitle("提示")
                    .setMessage(tableJson.getMsg())
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                        @Override
                        public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                        }
                    }).show();
        }
    };

    /**
     * 网络操作相关的子线程
     */
    Runnable editWorkTask = new Runnable() {
        @Override
        public void run() {
            // TODO
            // 在这里进行 http request.网络请求相关操作
            String rvalue = editWorkLog();
            Message msg = new Message();
            Bundle data = new Bundle();
            data.putString("rvalue", rvalue);
            data.putString("value", "请求结果");
            msg.setData(data);
            handler.sendMessage(msg);
        }
    };

    // 修改工作日志
    private String editWorkLog() {
        WorkLogService workLogService = new WorkLogService();
        return workLogService.editWorkLog(workLog);
    }
}
