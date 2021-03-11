package com.aratek.framework.core.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author shijinlong
 * @date 2018-04-28
 * @description 日期帮助类
 */
public class DateUtil {
    /**
     * 时间格式(yyyy-MM-dd)
     */
    public static final String DATE_PATTERN = "yyyy-MM-dd";
    /**
     * 时间格式(yyyy-MM-dd HH:mm:ss)
     */
    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * 日期格式化 日期格式为：yyyy-MM-dd
     *
     * @param date 日期
     * @return 返回yyyy-MM-dd格式日期
     */
    public static String format(Date date) {
        return format(date, DATE_PATTERN);
    }

    /**
     * 日期格式化 日期格式为：yyyy-MM-dd HH:mm:ss
     *
     * @param date 日期
     * @return 返回yyyy-MM-dd HH:mm:ss格式日期
     */
    public static String formatDateTime(Date date) {
        return format(date, DATE_TIME_PATTERN);
    }

    /**
     * 日期格式化 日期格式为：yyyy-MM-dd
     *
     * @param date    日期
     * @param pattern 格式，如：DateUtils.DATE_TIME_PATTERN
     * @return 返回yyyy-MM-dd格式日期
     */
    public static String format(Date date, String pattern) {
        if (date != null) {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            return df.format(date);
        }
        return null;
    }

    /**
     * 获取当前时间 日期格式为传入的格式
     *
     * @param pattern 格式，如：yyyyMMdd
     * @return
     */
    public static String getNowByPattern(String pattern) {
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        return df.format(new Date());
    }

    /**
     * 比较两个时间相差的分钟
     *
     * @param start
     * @param end
     * @return
     */
    public static int getMinuteBetween(long start, long end) {
        if (end <= start) {
            long l1 = (start - end) / 1000;
            return (int) (l1 / 60) * -1;
        }
        long l = (end - start) / 1000;
        return (int) (l / 60);
    }

    /**
     * 比较两个时间相差的天数
     *
     * @param start
     * @param end
     * @return
     */
    public static int getDayBetween(long start, long end) {
        if (end <= start) {
            long l1 = start - end;
            return (int) (l1 / (24 * 60 * 60 * 1000)) * -1;
        }
        long l = end - start;
        return (int) (l / (24 * 60 * 60 * 1000));
    }


}
