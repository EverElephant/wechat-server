package com.kodgames.wechattools.util;

import org.joda.time.DateTime;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DateUtil {
    public static boolean isSameDay(Date one, Date two) {
        return TimeUnit.MILLISECONDS.toDays(one.getTime()) == TimeUnit.MILLISECONDS.toDays(two.getTime());
    }

    /**
     * 获取给定日期凌晨的日期,00:00:00:000
     *
     * @return
     */
    public static Date getDateMorning(Date date) {
        return getDateMorning(date, 0);
    }

    /**
     * 获取给定日期n天后凌晨的日期
     *
     * @return
     */
    public static Date getDateMorning(Date date, int n) {
        DateTime dateTime = new DateTime(date);
        return dateTime.withTimeAtStartOfDay().plusDays(n).toDate();

    }

    /**
     * 获取给定日期23:59:59:999的日期时间
     *
     * @param date
     * @return
     */
    public static Date getDateNight(Date date) {
        return getDateNight(date, 0);
    }

    /**
     * 获取给定日期的最后1毫秒的日期时间
     *
     * @param date
     * @return
     */
    public static Date getDateNight(Date date, int n) {
        DateTime time = new DateTime(date).withTimeAtStartOfDay().plusDays(n + 1).minusMillis(1);
        return time.toDate();
    }
}
