package com.lyq.myapp.jeecg.service;

import com.lyq.myapp.jeecg.util.JeecgUtil;
import com.lyq.myapp.jeecg.util.SignatureUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 个人账户服务类
 * Created by 云强 on 2017/7/24.
 */
public class CommonService {

    private final static String key = "26F72780372E84B6CFAED6F7B19139CC47B1912B6CAED753";

    /**
     * 远程调用
     *
     * @param methodUrl
     * @param paramMap
     * @return
     */
    public static String execute(String methodUrl, Map<String, String> paramMap) {
        String url = JeecgUtil.JEECGPATH + methodUrl;
        String sign = SignatureUtil.createSign(paramMap, key);// 生成签名
        Map<String, Object> maps = new HashMap<>();
        for (String key : paramMap.keySet()) {
            maps.put(key, paramMap.get(key));
        }
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
            } else {
                throw new IOException("Unexpected code " + response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rvalue;
    }

}
