package com.lyq.myapp.demo;

import com.lyq.myapp.jeecg.util.JsonUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 云强 on 2017/7/19.
 */
public class Demo {

    public static void main(String[] args) {
        String json = "{\"username\":\"zms\",\"age\":43,\"addr\":\"江西省高安市村前镇\"}";
        JSONObject jsonObject2 = null;
        try {
            jsonObject2 = new JSONObject(json);
            String str = "名字:" + jsonObject2.getString("username") + "年薪:" + jsonObject2.getString("age") + jsonObject2.getString("addr") + "\n";
            System.out.println(str);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
