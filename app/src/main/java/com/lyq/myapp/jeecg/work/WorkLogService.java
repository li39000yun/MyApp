package com.lyq.myapp.jeecg.work;

import com.lyq.myapp.jeecg.util.JeecgUtil;
import com.lyq.myapp.jeecg.util.JsonUtil;
import com.lyq.myapp.jeecg.util.SignatureUtil;
import com.lyq.myapp.jeecg.work.entity.WorkLogEntity;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 工作日志服务类
 * Created by 云强 on 2017/7/24.
 */
public class WorkLogService {

    private final String key = "26F72780372E84B6CFAED6F7B19139CC47B1912B6CAED753";

    /**
     * 调用修改日志接口
     */
    public String editWorkLog(WorkLogEntity workLogEntity) {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String nowDate = sDateFormat.format(new java.util.Date());
        String loginName = "luke";
        // 设置值
        workLogEntity.setUpdateDate(nowDate);// 修改时间
        workLogEntity.setUpdateBy(loginName);// 修改人账号
        workLogEntity.setUpdateName(loginName);// 修改人名称
        String data = JsonUtil.objectToString(workLogEntity);// 实体对象数据

        // 调用添加接口
        String url = JeecgUtil.JEECGPATH + "/cgFormDataController.do?updateFormInfo";
        String tableName = "work_log";
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("tableName", tableName);
        paramMap.put("id", workLogEntity.getId());
        paramMap.put("data", data);
        paramMap.put("method", "updateFormInfo");
        String sign = SignatureUtil.createSign(paramMap, key);// 生成签名
        Map<String, Object> maps = new HashMap<>();
        maps.put("tableName", tableName);
        maps.put("id", workLogEntity.getId());
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

    /**
     * 查询日志列表
     *
     * @return
     */
    public String listWorkLog() {
        String url = JeecgUtil.JEECGPATH + "/wsWorkLogController.do?list";
        String tableName = "work_log";
        String id = "402847ed5d4a5138015d4a5138fd0000";
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("tableName", tableName);
        paramMap.put("id", id);
        paramMap.put("method", "getFormInfo");
        String sign = SignatureUtil.createSign(paramMap, key);// 生成签名
        Map<String, Object> maps = new HashMap<>();
        maps.put("tableName", tableName);
        maps.put("id", id);
        maps.put("sign", sign);

        FormBody.Builder builder = new FormBody.Builder();
        // 遍历key
        if (null != maps) {
            for (Map.Entry<String, Object> entry : maps.entrySet()) {
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
}
