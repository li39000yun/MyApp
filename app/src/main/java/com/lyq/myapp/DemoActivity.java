package com.lyq.myapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.lyq.myapp.demo.Jeecg;
import com.lyq.myapp.jeecg.work.entity.WorkLogEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DemoActivity extends AppCompatActivity {
    private ListView list = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.index_main);
//        Example01 example01 = new Example01();
//        example01.testHttp();
        list = (ListView) findViewById(R.id.MyListView);
        Jeecg jeecg = new Jeecg();
        List<WorkLogEntity> logs = jeecg.workLog();
        List<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
        for (WorkLogEntity log : logs) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("itemTitle", log.getTitle());
            map.put("itemText", log.getContent());
            mylist.add(map);
        }
//        //组织数据源
//        List<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
//        for (int i = 0; i < 10; i++) {
//            HashMap<String, String> map = new HashMap<String, String>();
//            map.put("itemTitle", "This is Title");
//            map.put("itemText", "This is text");
//            mylist.add(map);
//        }
        //配置适配器
        SimpleAdapter adapter = new SimpleAdapter(this,
                mylist,//数据源
                R.layout.work_log_list_listitem,//显示布局
                new String[]{"itemTitle", "itemText"}, //数据源的属性字段
                new int[]{R.id.itemTitle, R.id.itemText}); //布局里的控件id
        //添加并且显示
        list.setAdapter(adapter);
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
}
