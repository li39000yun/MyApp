package com.lyq.myapp.jeecg.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 加签、验签工具.
 *
 * @author zhoujf
 */
public class SignatureUtil {
    /**
     * 加签,MD5.
     *
     * @param paramMap 参数Map,不包含商户秘钥且顺序确定
     * @param key      商户秘钥
     * @return 签名串
     */
    public static String sign(Map<String, String> paramMap, String key) {
        if (key == null) {
            return "";
        }
        String sign = createSign(paramMap, key);
        return sign;
    }

    /**
     * 创建md5摘要,规则是:按参数名称a-z排序,遇到空值的参数不参加签名。
     */
    public static String createSign(Map<String, String> paramMap, String key) {
        StringBuffer sb = new StringBuffer();
        SortedMap<String, String> sort = new TreeMap<String, String>(paramMap);
        Set<Entry<String, String>> es = sort.entrySet();
        Iterator<Entry<String, String>> it = es.iterator();
        while (it.hasNext()) {
            @SuppressWarnings("rawtypes")
            Entry entry = (Entry) it.next();
            String k = (String) entry.getKey();
            String v = (String) entry.getValue();
            if (null != v && !"".equals(v) && !"null".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("key=" + key);
        String sign = MD5Util.MD5Encode(sb.toString(), "UTF-8").toUpperCase();
        return sign;
    }

    /**
     * 验签, 仅支持MD5.
     *
     * @param paramMap 参数Map,不包含商户秘钥且顺序确定
     * @param key      商户秘钥
     * @param sign     签名串
     * @return 验签结果
     */
    public static boolean checkSign(Map<String, String> paramMap, String key, String sign) {
        if (key == null) {
            return false;
        }
        if (sign == null) {
            return false;
        }

        return sign.equals(sign(paramMap, key));
    }

    public static void main(String[] args) {
//    	String url = "http://www.saphao.com:9999/P3-Web/commonxrs/toIndex.do?actId=402880ee51334a520151334c3eaf0001&openid=oR0jFt_DTsAUJebWqGeq3A1VWfRw&nickname=JEFF&subscribe=1&jwid=&sign=F5E56A64B650A98E67CCCFFF871C7133";
//    	Map<String,String> t = getSignMap(url);
//    	for(Map.Entry<String, String> entry:t.entrySet()){    
//    	     System.out.println(entry.getKey()+"--->"+entry.getValue());    
//    	}  
      /*  String key = "26F72780372E84B6CFAED6F7B19139CC47B1912B6CAED753";
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("tableName", "jform_le_main");
        paramMap.put("id", "402813815398698b015398698b710000");
        paramMap.put("data", "{jform_le_main:[{id=\"402813815398698b015398698b710000\",name:\"ceshi111111\",sex:1,remark:\"java developer\"}],jform_le_subone:[{main_id=\"402813815398698b015398698b710000\",name:\"ceshi111111\",sex:1,remark:\"java developer\"}],jform_le_submany:[{main_id=\"402813815398698b015398698b710000\",name:\"ceshi111111\",sex:1,remark:\"java developer\"},{name:\"ceshi111111\",sex:1,remark:\"java developer\"}]}");
        paramMap.put("method", "updateFormInfo");
        System.out.println(createSign(paramMap, key));*/

//        http://localhost:8180/jeecg/wsWorkLogController.do?list&tableName=ssj_tally&id=402847ed5d4a5138015d4a5138fd0000&sign=19F985722547B579B4D5A13F766E3C3B

        String key = "26F72780372E84B6CFAED6F7B19139CC47B1912B6CAED753";
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("tableName", "ssj_tally");
        paramMap.put("id", "402847ed5d4a5138015d4a5138fd0000");
//        paramMap.put("data", "{jform_le_main:[{id=\"402813815398698b015398698b710000\",name:\"ceshi111111\",sex:1,remark:\"java developer\"}],jform_le_subone:[{main_id=\"402813815398698b015398698b710000\",name:\"ceshi111111\",sex:1,remark:\"java developer\"}],jform_le_submany:[{main_id=\"402813815398698b015398698b710000\",name:\"ceshi111111\",sex:1,remark:\"java developer\"},{name:\"ceshi111111\",sex:1,remark:\"java developer\"}]}");
        paramMap.put("method", "getFormInfo");
        System.out.println(createSign(paramMap, key));
    }
}
