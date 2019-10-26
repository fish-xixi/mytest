package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.dao.MemberDao;
import com.itheima.health.dao.OrderDao;
import com.itheima.health.service.ReportService;
import com.itheima.health.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName CheckItemServiceImpl
 * @Description TODO
 * @Author ly
 * @Company 深圳黑马程序员
 * @Date 2019/10/13 9:54
 * @Version V1.0
 */
@Service(interfaceClass = ReportService.class)
@Transactional
public class ReportServiceImpl implements ReportService {

    @Autowired
    MemberDao memberDao;

    @Autowired
    OrderDao orderDao;

    @Override
    public Map<String, Object> getBusinessReportData() {
        Map<String,Object> map = new HashMap<>();
        try {
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

            // 今天新增会员数
            Integer todayNewMember = memberDao.findTodayNewMember(today);
            // 总会员数
            Integer totalMember = memberDao.findTotalMember();
            // 本周新增会员数
            Integer thisWeekNewMember = memberDao.findRegTimeAfterDateNewMember(thisWeekFirstDay);
            // 本月新增会员数
            Integer thisMonthNewMember = memberDao.findRegTimeAfterDateNewMember(thisMonthFirstDay);

            // 订单相关
            // 今天预约数
            Integer todayOrderNumber = orderDao.findTodayOrderNumber(today);
            // 今天到诊数
            Integer todayVisitsNumber = orderDao.findTodayVisitsNumber(today);
            // 本周预约数
            Map<String,String> params = new HashMap<>();
            params.put("begin",thisWeekFirstDay);
            params.put("end",thisWeekLastDay);
            Integer thisWeekOrderNumber = orderDao.findThisRegTimeBetweenOrderNumber(params);
            Map<String,String> params2 = new HashMap<>();
            params2.put("begin",thisWeekFirstDay);
            params2.put("end",thisWeekLastDay);
            Integer thisWeekVisitsNumber = orderDao.findThisRegTimeBetweenVisitsNumber(params2);
            // 本月预约数
            Map<String,String> params1 = new HashMap<>();
            params1.put("begin",thisMonthFirstDay);
            params1.put("end",thisMonthLastDay);
            Integer thisMonthOrderNumber = orderDao.findThisRegTimeBetweenOrderNumber(params1);
            Map<String,String> params3 = new HashMap<>();
            params3.put("begin",thisMonthFirstDay);
            params3.put("end",thisMonthLastDay);
            Integer thisMonthVisitsNumber = orderDao.findThisRegTimeBetweenVisitsNumber(params3);
            // 热门套餐
            List<Map<String,Object>> hotSetmeal = orderDao.findHotSetmeal();


            map.put("reportDate",today);
            map.put("todayNewMember",todayNewMember );
            map.put("totalMember",totalMember );
            map.put("thisWeekNewMember",thisWeekNewMember );
            map.put("thisMonthNewMember",thisMonthNewMember );
            map.put("todayOrderNumber",todayOrderNumber );
            map.put("todayVisitsNumber",todayVisitsNumber );
            map.put("thisWeekOrderNumber",thisWeekOrderNumber );
            map.put("thisWeekVisitsNumber",thisWeekVisitsNumber );
            map.put("thisMonthOrderNumber",thisMonthOrderNumber );
            map.put("thisMonthVisitsNumber",thisMonthVisitsNumber );
            map.put("hotSetmeal",hotSetmeal );
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("运行时异常");
        }
        return map;
    }
}
