package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.dao.OrderSettingDao;
import com.itheima.health.pojo.OrderSetting;
import com.itheima.health.service.OrderSettingService;
import com.itheima.health.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @ClassName CheckItemServiceImpl
 * @Description TODO
 * @Author ly
 * @Company 深圳黑马程序员
 * @Date 2019/10/13 9:54
 * @Version V1.0
 */
@Service(interfaceClass = OrderSettingService.class)
@Transactional
public class OrderSettingServiceImpl implements OrderSettingService {

    @Autowired
    OrderSettingDao orderSettingDao;

    @Override
    public void addList(List<OrderSetting> orderSettingList) {
        for (OrderSetting orderSetting : orderSettingList) {
            // 判断，使用当前时间查询，判断当前时间在数据库中是否存在该记录
            long count = orderSettingDao.findOrderSettingByOrderDate(orderSetting.getOrderDate());
            // 存在数据
            if(count>0){
                // 使用当前时间，更新对应的课预约人数的数量
                orderSettingDao.updateOrderSettingByOrderDate(orderSetting);
            }
            // 不存在数据
            else{
                // 保存到数据库
                orderSettingDao.add(orderSetting);
            }

        }
    }

    @Override
    public List<Map<String, Object>> findOrderSettingByCurrentDate(String date) {
        // 当前月的开始时间
        String begin = date+"-01";
        // 当前月的结束时间
        String end = date+"-31";
        // 组织参数Map
        Map<String,Object> params = new HashMap<>();
        params.put("begin",begin);
        params.put("end",end);
        // 使用当前月的开始时间和结束时间查询当前月的预约设置信息
        List<OrderSetting> list = orderSettingDao.findOrderSettingByCurrentDate(params);
        // 组织返回结果List<Map<String, Object>>
        List<Map<String, Object>> mapList = new ArrayList<>();
        for (OrderSetting orderSetting : list) {
            Map<String,Object> map = new HashMap<>();
            map.put("date",orderSetting.getOrderDate().getDate()); // 获取当前月的日期
            map.put("number",orderSetting.getNumber());// 最多可预约的人数
            map.put("reservations",orderSetting.getReservations());// 当天实际预约人数
            mapList.add(map);
        }
        return mapList;
    }

    @Override
    public void updateNumberByOrderDate(OrderSetting orderSetting) {
        // 判断，使用当前时间查询，判断当前时间在数据库中是否存在该记录
        long count = orderSettingDao.findOrderSettingByOrderDate(orderSetting.getOrderDate());
        // 存在数据
        if(count>0){
            // 使用当前时间，更新对应的课预约人数的数量
            orderSettingDao.updateOrderSettingByOrderDate(orderSetting);
        }
        // 不存在数据
        else{
            // 保存到数据库
            orderSettingDao.add(orderSetting);
        }
    }

    @Override
    public void testUpdate() {
        Date date = null;
        try {
            date = DateUtils.parseString2Date("2019-10-30");
        } catch (Exception e) {
            e.printStackTrace();
        }
        OrderSetting orderSetting = orderSettingDao.findOrderSettingPojoByOrderDate(date);
        orderSetting.setReservations(orderSetting.getReservations()+1);
        //orderSettingDao.updateReservationsByOrderDate(orderSetting);
        System.out.println("111111111");
        System.out.println("222222222");

    }

    @Override
    public void testUpdate2() {
        Date date = null;
        try {
            date = DateUtils.parseString2Date("2019-10-30");
        } catch (Exception e) {
            e.printStackTrace();
        }
        orderSettingDao.updateByOrderDate(date);
        System.out.println("111111111");
        System.out.println("222222222");
    }


}
