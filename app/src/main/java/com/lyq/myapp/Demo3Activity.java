package com.lyq.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.lyq.myapp.jeecg.util.JsonUtil;
import com.lyq.myapp.jeecg.util.TableJson;
import com.lyq.myapp.jeecg.work.activity.ViewWorkLogActivity;
import com.lyq.myapp.jeecg.work.entity.WorkLogEntity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Demo3Activity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private ListView list = null;
    private Button buttonTest = null;
    private Button addWorkButton = null;// 添加日志
    private Map<Integer, WorkLogEntity> idMap = new HashMap<Integer, WorkLogEntity>();// id数组，key=序号；value=子对象的id值

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.index_main);
        // 加载数据
        Bundle bundle = getIntent().getExtras();   //得到传过来的bundle
        String rvalue = bundle.getString("data");//读出数据
        TableJson tableJson = (TableJson) JsonUtil.stringToObject(rvalue, TableJson.class);
        //组织数据源
        List<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
        if (tableJson.isSuccess()) {
            Gson gson = new Gson();
            List<WorkLogEntity> logs = new ArrayList<WorkLogEntity>();
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
        list = (ListView) findViewById(R.id.MyListView);
        SimpleAdapter adapter = new SimpleAdapter(this,
                mylist,//数据源
                R.layout.my_listitem,//显示布局
                new String[]{"itemTitle", "itemText"}, //数据源的属性字段
                new int[]{R.id.itemTitle, R.id.itemText}); //布局里的控件id
        //添加并且显示
        list.setAdapter(adapter);
        list.setOnItemClickListener(this);// 设置点击事件

        addWorkButton = (Button) findViewById(R.id.addWorkButton);
        addWorkButton.setOnClickListener(this);


        buttonTest = (Button) findViewById(R.id.buttonTest);
        buttonTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //创建okHttpClient对象
                OkHttpClient mOkHttpClient = new OkHttpClient();
                //创建一个Request
                String ip = "http://li39000yun.vicp.cc/";
                //        String ip = "http://localhost:8180/";
                String url = ip + "jeecg/wsWorkLogController.do?list";
                // 创建请求的参数body

                FormBody.Builder builder = new FormBody.Builder();
                HashMap<String, Object> maps = new HashMap<String, Object>();
                maps.put("tableName", "ssj_tally");
                maps.put("id", "402847ed5d4a5138015d4a5138fd0000");
                maps.put("sign", "735CE07A2AB9C1872983B09C85A770D9");

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
                //new call
                Call call = mOkHttpClient.newCall(request);
                //请求加入调度
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String rvalue = response.body().string();
                        System.out.println(rvalue);
                    }
                });
            }
        });
    }

    @Override
    public void onClick(View v) {
        System.out.println(v.getId());
        // 跳转到添加日志界面
        if (v.getId() == addWorkButton.getId()) {
            Intent intent = new Intent();
            intent.setClass(this, AddWorkLogActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

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
        System.out.println(view.getId());
        System.out.println(position);
        System.out.println(id);
        System.out.println(parent.getId());
        System.out.println(view.getContext());
        TextView itemTitle = (TextView) view.findViewById(R.id.itemTitle);
        System.out.println("itemTitle.getText()" + itemTitle.getText());
        System.out.println(itemTitle.getId());
        // 跳转到查看日志界面
        Intent intent = new Intent();
        intent.setClass(this, ViewWorkLogActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("workLog", idMap.get(position));// 设置id
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
