package com.itheima.health.dao;

import com.itheima.health.pojo.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @ClassName CheckItemDao
 * @Description TODO
 * @Author ly
 * @Company 深圳黑马程序员
 * @Date 2019/10/13 9:53
 * @Version V1.0
 */
public interface OrderSettingDao {

    void add(OrderSetting orderSetting);

    long findOrderSettingByOrderDate(Date orderDate);

    void updateOrderSettingByOrderDate(OrderSetting orderSetting);

    List<OrderSetting> findOrderSettingByCurrentDate(Map<String, Object> params);

    OrderSetting findOrderSettingPojoByOrderDate(Date date);

    //void updateReservationsByOrderDate(OrderSetting orderSetting);

    void updateByOrderDate(Date date);

    void updateReservationsByOrderDate(Date date);

    void deletePassOrderSetting(String date);
}
