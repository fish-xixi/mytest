package com.itheima.test;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @ClassName TestCalendar
 * @Description TODO
 * @Author ly
 * @Company 深圳黑马程序员
 * @Date 2019/10/23 12:01
 * @Version V1.0
 */
public class TestCalendar {

    @Test
    public void test(){
        List<String> monthsList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH,-12); // 当前时间往前推12个月
        for (int i = 0; i < 12; i++) {
            calendar.add(Calendar.MONTH,1); // 向前推12个月，再向后推1个月（2018-11）
            String month = new SimpleDateFormat("yyyy-MM").format(calendar.getTime());
            monthsList.add(month);
        }
        System.out.println(monthsList);
    }
}
