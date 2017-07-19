package com.lyq.myapp.demo;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.lyq.myapp.jeecg.util.JsonUtil;
import com.lyq.myapp.jeecg.util.TableJson;
import com.lyq.myapp.jeecg.work.entity.WorkLogEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Jeecg接口调用类
 * Created by 云强 on 2017/7/19.
 */
public class Jeecg {

    public List<WorkLogEntity> workLog() {
        List<WorkLogEntity> list = new ArrayList<WorkLogEntity>();
        //创建okHttpClient对象
        OkHttpClient mOkHttpClient = new OkHttpClient();
//创建一个Request
        String ip = "http://119.29.78.148:8080/";
//        String ip = "http://localhost:8180/";

        String url = ip+"jeecg/wsWorkLogController.do?list&tableName=ssj_tally&id=402847ed5d4a5138015d4a5138fd0000&sign=735CE07A2AB9C1872983B09C85A770D9";
        final Request request = new Request.Builder()
                .url(url)

                .build();
        try {
            Response response = mOkHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                String rvalue = response.body().string();
                TableJson tableJson = (TableJson) JsonUtil.stringToObject(rvalue, TableJson.class);
                if (tableJson.isSuccess()) {
                    Gson gson = new Gson();
                    list = new ArrayList<WorkLogEntity>();
                    JsonArray array = new JsonParser().parse(rvalue).getAsJsonObject().getAsJsonArray("tableData");
                    for (final JsonElement elem : array) {
                        list.add(gson.fromJson(elem, WorkLogEntity.class));
                    }
                } else {
                    System.out.println("访问失败");
                }
            } else {
                throw new IOException("Unexpected code " + response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void main(String[] args) {
        //创建okHttpClient对象
        OkHttpClient mOkHttpClient = new OkHttpClient();
//创建一个Request
        String ip = "http://119.29.78.148:8080/";
//        String ip = "http://localhost:8180/";
        String url = ip+"jeecg/wsWorkLogController.do?list&tableName=ssj_tally&id=402847ed5d4a5138015d4a5138fd0000&sign=735CE07A2AB9C1872983B09C85A770D9";

//        String url = "http://localhost:8180/jeecg/wsWorkLogController.do?list&tableName=ims_travel&id=402847ed5d4a5138015d4a5138fd0000&sign=735CE07A2AB9C1872983B09C85A770D9";
        final Request request = new Request.Builder()
                .url(url)
                .build();
//new call
   /*     Call call = mOkHttpClient.newCall(request);
//请求加入调度
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                System.out.println(response.body().string());
                String rvalue = response.body().string();
//                JSONTokener jsonTokener = new JSONTokener(rvalue);
//                tableData
                System.out.println(rvalue);
                TableJson tableJson = (TableJson) JsonUtil.stringToObject(rvalue, TableJson.class);
                if (tableJson.isSuccess()) {
                    System.out.println(String.valueOf(tableJson.getTableData()));
                    System.out.println(tableJson.getTableData().toString());


                    Gson gson = new Gson();
                    List<WorkLogEntity> list = new ArrayList<WorkLogEntity>();
                    JsonArray array = new JsonParser().parse(rvalue).getAsJsonObject().getAsJsonArray("tableData");
                    for (final JsonElement elem : array) {
                        list.add(gson.fromJson(elem, WorkLogEntity.class));
                    }
                    System.out.println(list.size());
                } else {
                    System.out.println("访问失败");
                }

//                    JSONObject jsonObject = new JSONObject(rvalue);
//                    JSONArray datas = jsonObject.getJSONArray("tableData");
//                    System.out.println(datas.length());

            }
        });*/


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


//
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://localhost:8180/jeecg/")
//                .build();
//        WorkLog service = retrofit.create(WorkLog.class);
//        Call<ResponseBody> call = service.getList("402847ed5d4a5138015d4a5138fd0000","735CE07A2AB9C1872983B09C85A770D9");
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                try {
//                    System.out.println(response.body().string());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                t.printStackTrace();
//            }
//        });
    }

}
