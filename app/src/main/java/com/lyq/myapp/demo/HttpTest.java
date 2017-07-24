package com.lyq.myapp.demo;

import android.text.format.DateFormat;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.lyq.myapp.jeecg.util.JsonUtil;
import com.lyq.myapp.jeecg.util.SignatureUtil;
import com.lyq.myapp.jeecg.util.TableJson;
import com.lyq.myapp.jeecg.work.entity.WorkLogEntity;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Jeecg接口调用类
 * Created by 云强 on 2017/7/19.
 */
public class HttpTest {

    public void getFormInfo() {
        //创建okHttpClient对象
        OkHttpClient mOkHttpClient = new OkHttpClient();
        //创建一个Request
        // String ip = "http://119.29.78.148:8080/";
//        String ip = "http://localhost:8280/";
//        String url = ip + "jeecg/wsWorkLogController.do?list&tableName=ssj_tally&id=402847ed5d4a5138015d4a5138fd0000&sign=735CE07A2AB9C1872983B09C85A770D9";
        String url = "http://localhost:8280/jeecg/cgFormDataController.do?getFormInfo";

        String key = "26F72780372E84B6CFAED6F7B19139CC47B1912B6CAED753";
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("tableName", "ssj_tally");
        paramMap.put("id", "40283f815ad9b8da015ad9bd80440001");
        paramMap.put("method", "getFormInfo");
        String sign = SignatureUtil.createSign(paramMap, key);
        System.out.println(sign);
        Map<String, Object> maps = new HashMap<>();
        maps.put("tableName", "ssj_tally");
        maps.put("id", "40283f815ad9b8da015ad9bd80440001");
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
        try {
            Response response = mOkHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                String rvalue = response.body().string();
                System.out.println(rvalue);
            } else {
                throw new IOException("Unexpected code " + response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        HttpTest httpTest = new HttpTest();
        httpTest.testAddWork();
    }


    public void testAddWork() {
        WorkLogEntity workLogEntity = new WorkLogEntity();
//        INSERT INTO `jeecg`.`work_log` (`id`, `create_name`, `create_by`, `create_date`, `update_name`, `update_by`, `update_date`, `sys_org_code`, `sys_company_code`, `bpm_status`, `title`, `content`, `date`, `week`) VALUES ('2c90e4d85d647e3b015d6484d4630001', 'luke', 'luke', '2017-07-21 17:40:13', '', '', NULL, 'A02', 'A0', '', '20170721', '国资委，下午和李家伟碰了资产证券化率的表格。一共两张表格；李佳楠和杨文崴碰了操作说明；中午更新了正式环境；测试出来一些bug，项目业务类型确认阶段缺少', '2017-07-21 00:00:00', '星期五');
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String nowDate = sDateFormat.format(new java.util.Date());
        String loginName = "luke";
        String uuid = UUID.randomUUID().toString();// uuid
        // 设置值
        workLogEntity.setId(uuid);// id
        workLogEntity.setCreateDate(nowDate);// 创建时间
        workLogEntity.setCreateBy(loginName);// 创建人账号
        workLogEntity.setCreateName(loginName);// 创建人名称
        workLogEntity.setTitle("测试标题2");
        workLogEntity.setContent("测试内容2");
        workLogEntity.setDate("2017-07-23");
        String data = JsonUtil.objectToString(workLogEntity);// 实体对象数据
        System.out.println(data);

        // 调用添加接口
        String url = "http://localhost:8180/jeecg/cgFormDataController.do?addFormInfo";
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
        try {
            Response response = mOkHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                String rvalue = response.body().string();
                System.out.println(rvalue);
            } else {
                throw new IOException("Unexpected code " + response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
