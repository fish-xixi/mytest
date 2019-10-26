package com.itheima.health.service;

import com.itheima.health.pojo.OrderSetting;

import java.util.List;
import java.util.Map;

public interface OrderSettingService {

    void addList(List<OrderSetting> orderSettingList);

    List<Map<String, Object>> findOrderSettingByCurrentDate(String date);

    void updateNumberByOrderDate(OrderSetting orderSetting);

    void testUpdate();

    void testUpdate2();

    //删除过时的预约设置数据
    void deletePassOrderSetting();


}
