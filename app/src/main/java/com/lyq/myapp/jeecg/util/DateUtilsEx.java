package com.lyq.myapp.jeecg.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具栏扩展
 * Created by 云强 on 2017/4/15.
 */
public class DateUtilsEx {

    /**
     * 获取当前日期是星期几<br>
     *
     * @param dt
     * @return 当前日期是星期几
     */
    public static String getWeekOfDate(Date dt) {
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    /**
     * 传入Unix时间戳(Unix timestamp)，返回日期
     *
     * @param dateStr Unix时间戳(Unix timestamp),eg1493222400000
     * @return 年月日yyyy-MM-dd;eg:2017-04-27
     */
    public static String formatDate(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date date = new Date(Long.parseLong(dateStr));
        return sdf.format(date);
    }

}
