package com.lyq.myapp.jeecg.ims.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.lyq.myapp.R;
import com.lyq.myapp.jeecg.ims.ImsAccountService;
import com.lyq.myapp.jeecg.ims.entity.ImsAccountEntity;
import com.lyq.myapp.jeecg.util.JsonUtil;
import com.lyq.myapp.jeecg.util.TableJson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 查询个人账户列表
 */
public class ImsAccountListActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private ListView list = null;
    //    private Button addWorkButton = null;// 添加账户
    private Map<Integer, ImsAccountEntity> idMap = new HashMap<Integer, ImsAccountEntity>();// id数组，key=序号；value=子对象的id值

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_main);
        initView();
    }

    // 初始化界面
    private void initView() {
        list = (ListView) findViewById(R.id.MyListView);
        list.setOnItemClickListener(this);// 设置点击事件
//        addWorkButton = (Button) findViewById(R.id.addWorkButton);
//        addWorkButton.setOnClickListener(this);

        // 加载数据
        // 开启一个子线程，进行网络操作，等待有返回结果，使用handler通知UI
        new Thread(accountListTask).start();
    }

    @Override
    public void onClick(View v) {
        // 跳转到添加账户界面
//        if (v.getId() == addWorkButton.getId()) {
//            Intent intent = new Intent();
//            intent.setClass(this, WorkLogAddActivity.class);
//            startActivity(intent);
//        }
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            // UI界面的更新等相关操作
            String rvalue = data.getString("rvalue");
            TableJson tableJson = (TableJson) JsonUtil.stringToObject(rvalue, TableJson.class);
            //组织数据源
            List<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
            if (tableJson.isSuccess()) {
                Gson gson = new Gson();
                List<ImsAccountEntity> accounts = new ArrayList<ImsAccountEntity>();
                JsonArray array = new JsonParser().parse(rvalue).getAsJsonObject().getAsJsonArray("tableData");
                for (final JsonElement elem : array) {
                    accounts.add(gson.fromJson(elem, ImsAccountEntity.class));
                }
                int count = 0;
                for (ImsAccountEntity account : accounts) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("itemTitle", account.getTitle());
                    map.put("itemText", account.getAccount());
                    mylist.add(map);
                    idMap.put(count++, account);// 设置id值数组
                }
            } else {
                System.out.println("wsImsAccountController访问失败");
            }
            //配置适配器
            SimpleAdapter adapter = new SimpleAdapter(ImsAccountListActivity.this,
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
    Runnable accountListTask = new Runnable() {
        @Override
        public void run() {
            // 在这里进行 http request.网络请求相关操作
            ImsAccountService imsAccountService = new ImsAccountService();
            String rvalue = imsAccountService.list();
            Message msg = new Message();
            Bundle data = new Bundle();
            data.putString("rvalue", rvalue);
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
        // 跳转到查看账户界面
        Intent intent = new Intent();
        intent.setClass(this, ImsAccountViewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("account", idMap.get(position));// 设置id
//        bundle.putInt("position", position);// 设置序号
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
