package com.itheima.test;

import com.itheima.health.utils.DateUtils;
import org.junit.Test;

/**
 * @ClassName TestDateUtils
 * @Description TODO
 * @Author ly
 * @Company 深圳黑马程序员
 * @Date 2019/10/25 10:17
 * @Version V1.0
 */
public class TestDateUtils {

    @Test
    public void date() throws Exception {
        //当前时间
        String today = DateUtils.parseDate2String(DateUtils.getToday());
        //本周的第1天
        String thisWeekFirstDay = DateUtils.parseDate2String(DateUtils.getThisWeekMonday());
        //本周的最后1天
        String thisWeekLastDay = DateUtils.parseDate2String(DateUtils.getSundayOfThisWeek());
        //本月的第1天
        String thisMonthFirstDay = DateUtils.parseDate2String(DateUtils.getFirstDay4ThisMonth());
        //本月的最后1天
        String thisMonthLastDay = DateUtils.parseDate2String(DateUtils.getLastDay4ThisMonth());
    }
}
