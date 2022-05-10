package com.ils.modules.mes.produce.util;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;

import cn.hutool.core.date.DateUtil;
import org.apache.commons.lang3.StringUtils;

import com.ils.common.exception.ILSBootException;
import com.ils.common.util.BigDecimalUtils;
import com.ils.common.util.DateUtils;
import com.ils.modules.mes.enums.TimeUnitEnum;

/**
 * @Description: 时间计算工具类
 * @author: fengyi
 */

public class AutoTimeUtils {

    /** 以毫秒表示的时间 DAY_IN_MILLIS */
    private static final long DAY_IN_MILLIS = 24 * 3600 * 1000;

    /** 以毫秒表示的时间 HOUR_IN_MILLIS */
    private static final long HOUR_IN_MILLIS = 3600 * 1000;

    /** 以毫秒表示的时间 MINUTE_IN_MILLIS */
    private static final long MINUTE_IN_MILLIS = 60 * 1000;

    /** 以毫秒表示的时间 SECOND_IN_MILLIS */
    private static final long SECOND_IN_MILLIS = 1000;

    /** 计算时长填充日期字段 */
    private static String date1 = "2020-01-01 ";

    /** 计算时长填充日期字段 */
    private static String date2 = "2020-01-02 ";

    /** 一天最后时间填充字段 */
    private static String date24 = " 24:00:00";

    /** 日期时间格式化字符串 */
    public static String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /** 日期格式化字符串 */
    public static String DATE_PATTERN = "yyyy-MM-dd";

    /** 时间格式化字符串 */
    public static String TIME_PATTERN = "HH:mm:ss";

    /**
     * 
     * 以毫秒计算班次时长
     * 
     * @param t1
     * @param t2
     * @return
     */
    public static Long getTimeDuration(String t1, String t2) {
        // 08:00:00-12:00:00
        if (t1.compareTo(t2) < 0) {
            t1 = date1 + t1;
            t2 = date1 + t2;
            // 23:00:00-08:00:00
        } else {
            t1 = date1 + t1;
            t2 = date2 + t2;
        }
        try {
            Long t3 = DateUtils.parseDate(t1, DATE_TIME_PATTERN).getTime();
            Long t4 = DateUtils.parseDate(t2, DATE_TIME_PATTERN).getTime();
            return (t4 - t3);
        } catch (ParseException e) {
            throw new ILSBootException("班次时间解析错误");
        }
    }

    /**
     * 
     * 时长转换为毫秒
     * 
     * @param timePeriod
     * @param timeUnit
     * @return
     */
    public static Long convertTime(BigDecimal timePeriod, String timeUnit) {

        if (timePeriod == null || StringUtils.isBlank(timeUnit)) {
            return 0L;
        }
        TimeUnitEnum timeUnitEnum = TimeUnitEnum.getTimeUnitEnum(timeUnit);
        BigDecimal timeDuration = null;
        switch (timeUnitEnum) {
            case dd:
                timeDuration = BigDecimalUtils.multiply(timePeriod, new BigDecimal(DAY_IN_MILLIS));
                break;
            case hh:
                timeDuration = BigDecimalUtils.multiply(timePeriod, new BigDecimal(HOUR_IN_MILLIS));
                break;
            case mi:
                timeDuration = BigDecimalUtils.multiply(timePeriod, new BigDecimal(MINUTE_IN_MILLIS));
                break;
            case ss:
                timeDuration = BigDecimalUtils.multiply(timePeriod, new BigDecimal(SECOND_IN_MILLIS));
                break;
            default:
        }
        return timeDuration.longValue();
    }

    /**
     * 
     * 获取指定时间那天还剩余的时间
     * 
     * @param date
     * @return
     */
    public static long calDayLeftTime(Date date) {
        long t1 = date.getTime();
        String strDate = DateUtils.formatDate(date, DATE_PATTERN);
        strDate = strDate + date24;
        Long t2;
        try {
            t2 = DateUtils.parseDate(strDate, DATE_TIME_PATTERN).getTime();
            return t2 - t1;
        } catch (ParseException e) {
            throw new ILSBootException("时间解析错误");
        }
    }

    /**
     * 
     * 基准时间跟班次开始时间比较
     * 
     * @param date
     * @param shiftStartTime
     * @return
     */
    public static boolean compareShift(Date date, String shiftStartTime) {
        String strTime = DateUtils.formatDate(date, TIME_PATTERN);
        if (strTime.compareTo(shiftStartTime) > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 组合日期和时间封装为date对象
     *
     * @param date
     * @param time
     * @author niushuai
     * @date: 2021/6/4 14:58:23
     * @return: {@link Date}
     */
    public static Date combineDateAndTime(Date date, String time) {
        return DateUtil.parseDateTime(DateUtil.formatDate(date) + " " + time);
    }


    public static void main(String[] args) throws ParseException {

        System.err.println(AutoTimeUtils.calDayLeftTime(DateUtils.parseDate("2021-03-08 18:00:00", DATE_TIME_PATTERN)));

    }

}
