package com.lyq.myapp;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.lyq.myapp.jeecg.util.JsonUtil;
import com.lyq.myapp.jeecg.util.TableJson;
import com.lyq.myapp.jeecg.work.entity.WorkLogEntity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Demo3Activity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private ListView list = null;

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
            for (WorkLogEntity log : logs) {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("itemTitle", log.getTitle());
                map.put("itemText", log.getContent());
                mylist.add(map);
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


        list.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v) {
        System.out.println(v.getId());
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
    }
}
