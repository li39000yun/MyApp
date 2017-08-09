package com.lyq.myapp.jeecg.ims;

import com.lyq.myapp.jeecg.service.CommonService;
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
 * 个人账户服务类
 * Created by 云强 on 2017/7/24.
 */
public class ImsAccountService {

    /**
     * 查询日志列表
     *
     * @return
     */
    public String list() {
        String methodUrl = "/wsImsAccountController.do?list";
        Map<String, String> paramMap = new HashMap<String, String>();
        return CommonService.execute(methodUrl, paramMap);
    }
    
}
