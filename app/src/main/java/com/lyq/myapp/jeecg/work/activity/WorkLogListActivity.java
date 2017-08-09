package com.lyq.myapp.jeecg.work.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lyq.myapp.R;
import com.lyq.myapp.jeecg.util.JsonUtil;
import com.lyq.myapp.jeecg.util.TableJson;
import com.lyq.myapp.jeecg.work.WorkLogService;
import com.lyq.myapp.jeecg.work.entity.WorkLogEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorkLogListActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private ListView list = null;
    private Button addWorkButton = null;// 添加日志
    private Map<Integer, WorkLogEntity> idMap = new HashMap<Integer, WorkLogEntity>();// id数组，key=序号；value=子对象的id值

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.work_log_main);
        initView();
    }

    // 初始化界面
    private void initView() {
        list = (ListView) findViewById(R.id.MyListView);
        list.setOnItemClickListener(this);// 设置点击事件
        addWorkButton = (Button) findViewById(R.id.addWorkButton);
        addWorkButton.setOnClickListener(this);

        // 加载数据
        // 开启一个子线程，进行网络操作，等待有返回结果，使用handler通知UI
        new Thread(workListTask).start();
    }

    @Override
    public void onClick(View v) {
        // 跳转到添加日志界面
        if (v.getId() == addWorkButton.getId()) {
            Intent intent = new Intent();
            intent.setClass(this, WorkLogAddActivity.class);
            startActivity(intent);
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
            //组织数据源
            List<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
            if (tableJson.isSuccess()) {
                Gson gson = new Gson();
                List<WorkLogEntity> logs = new ArrayList<WorkLogEntity>();
                System.out.println(rvalue);
                JsonElement jsonElement = new JsonParser().parse(rvalue);
                JsonObject object = jsonElement.getAsJsonObject();
                JsonArray arrays = object.getAsJsonArray("tableData");
                JsonArray array = new JsonParser().parse(rvalue).getAsJsonObject().getAsJsonArray("tableData");
                for (final JsonElement elem : array) {
                    logs.add(gson.fromJson(elem, WorkLogEntity.class));
                }
                int count = 0;
                for (WorkLogEntity log : logs) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("itemTitle", log.getTitle());
                    map.put("itemText", log.getContent());
                    mylist.add(map);
                    idMap.put(count++, log);// 设置id值数组
                }
            } else {
                System.out.println("wsWorkLogController访问失败");
            }
            //配置适配器
            SimpleAdapter adapter = new SimpleAdapter(WorkLogListActivity.this,
                    mylist,//数据源
                    R.layout.work_log_list_listitem,//显示布局
                    new String[]{"itemTitle", "itemText"}, //数据源的属性字段
                    new int[]{R.id.itemTitle, R.id.itemText}); //布局里的控件id
            //添加并且显示
            list.setAdapter(adapter);
        }
    };

    /**
     * 网络操作相关的子线程
     */
    Runnable workListTask = new Runnable() {
        @Override
        public void run() {
            // TODO
            // 在这里进行 http request.网络请求相关操作
            WorkLogService workLogService = new WorkLogService();
            String rvalue = workLogService.listWorkLog();
            Message msg = new Message();
            Bundle data = new Bundle();
            data.putString("rvalue", rvalue);
            data.putString("value", "请求结果");
            msg.setData(data);
            handler.sendMessage(msg);
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // 跳转到查看日志界面
        Intent intent = new Intent();
        intent.setClass(this, WorkLogViewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("workLog", idMap.get(position));// 设置id
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
