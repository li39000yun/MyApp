package com.lyq.myapp;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
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

import com.lyq.myapp.jeecg.util.JeecgUtil;
import com.lyq.myapp.jeecg.util.JsonUtil;
import com.lyq.myapp.jeecg.util.SignatureUtil;
import com.lyq.myapp.jeecg.util.TableJson;
import com.lyq.myapp.jeecg.work.entity.WorkLogEntity;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 添加日志
 */
public class AddWorkLogActivity extends AppCompatActivity implements View.OnClickListener {

    private Button addWorkSumbit = null;
    private Button addWorkBack = null;
    private EditText addWorkTitle = null;
    private EditText addWorkContent = null;
    private EditText addWorkDate = null;
    WorkLogEntity workLogEntity = null;


    //获取日期格式器对象
    DateFormat fmtDate = new java.text.SimpleDateFormat("yyyy-MM-dd");
    //获取一个日历对象
    Calendar dateAndTime = Calendar.getInstance(Locale.CHINA);
    //当点击DatePickerDialog控件的设置按钮时，调用该方法
    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener()
    {
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_work_log);

        // 初始化按钮
        addWorkSumbit = (Button) findViewById(R.id.addWorkSumbit);
        addWorkSumbit.setOnClickListener(this);// 设置按钮事件
        addWorkBack = (Button) findViewById(R.id.addWorkBack);
        addWorkBack.setOnClickListener(this);
        addWorkDate = (EditText) findViewById(R.id.addWorkDate);
        upDateDate();// 默认日期为当前日期
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
        DatePickerDialog  dateDlg = new DatePickerDialog(AddWorkLogActivity.this,
                d,
                dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH));
        dateDlg.show();
    }

    @Override
    public void onClick(View v) {
        System.out.println(v.getContext().toString() + ":" + v.getId());
        // 提交
        if (v.getId() == addWorkSumbit.getId()) {
            addWorkTitle = (EditText) findViewById(R.id.addWorkTitle);
            addWorkContent = (EditText) findViewById(R.id.addWorkContent);
            addWorkDate = (EditText) findViewById(R.id.addWorkDate);
            workLogEntity = new WorkLogEntity();
            workLogEntity.setTitle(addWorkTitle.getText().toString());
            workLogEntity.setContent(addWorkContent.getText().toString());

            // 开启一个子线程，进行网络操作，等待有返回结果，使用handler通知UI
            new Thread(networkTask).start();
        } else if (v.getId() == addWorkBack.getId()) {
            // 返回
            finish();
        }
    }

    /**
     * 调用添加日志接口
     */
    private String addWorkLog() {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String nowDate = sDateFormat.format(new java.util.Date());
        String loginName = "luke";
        String uuid = UUID.randomUUID().toString();// uuid
        // 设置值
        workLogEntity.setId(uuid);// id
        workLogEntity.setCreateDate(nowDate);// 创建时间
        workLogEntity.setCreateBy(loginName);// 创建人账号
        workLogEntity.setCreateName(loginName);// 创建人名称
        String data = JsonUtil.objectToString(workLogEntity);// 实体对象数据
        System.out.println(data);

        // 调用添加接口
        String url = JeecgUtil.JEECGPATH + "/cgFormDataController.do?addFormInfo";
        String key = "26F72780372E84B6CFAED6F7B19139CC47B1912B6CAED753";
        String tableName = "work_log";
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("tableName", tableName);
        paramMap.put("id", uuid);
        paramMap.put("data", data);
        paramMap.put("method", "addFormInfo");
        String sign = SignatureUtil.createSign(paramMap, key);// 生成签名
        System.out.println(sign);
        Map<String, Object> maps = new HashMap<>();
        maps.put("tableName", tableName);
        maps.put("id", uuid);
        maps.put("data", data);
        maps.put("sign", sign);

        FormBody.Builder builder = new FormBody.Builder();
        // 遍历key
        if (null != maps) {
            for (Map.Entry<String, Object> entry : maps.entrySet()) {
                System.out.println("Key = " + entry.getKey() + ", Value = "
                        + entry.getValue());
                builder.add(entry.getKey(), entry.getValue().toString());
            }
        }

        RequestBody body = builder.build();
        final Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        OkHttpClient mOkHttpClient = new OkHttpClient();
        String rvalue = "";
        try {
            Response response = mOkHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                rvalue = response.body().string();
                System.out.println(rvalue);
            } else {
                throw new IOException("Unexpected code " + response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rvalue;
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
            //addWorkContent.setText(tableJson.getMsg());// 设置返回结果到内容上
            new AlertDialog.Builder(AddWorkLogActivity.this)
                    .setTitle("提示")
                    .setMessage(tableJson.getMsg())
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                        @Override
                        public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                            finish();
                        }
                    }).show();
//            finish();
        }
    };

    /**
     * 网络操作相关的子线程
     */
    Runnable networkTask = new Runnable() {
        @Override
        public void run() {
            // TODO
            // 在这里进行 http request.网络请求相关操作
            String rvalue = addWorkLog();
            Message msg = new Message();
            Bundle data = new Bundle();
            data.putString("rvalue", rvalue);
            data.putString("value", "请求结果");
            msg.setData(data);
            handler.sendMessage(msg);
        }
    };

}
