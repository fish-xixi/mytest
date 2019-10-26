package com.itheima.health.dao;

import com.itheima.health.pojo.Order;

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
public interface OrderDao {

    List<Order> findOrderListByCondition(Order order);

    void add(Order order);

    Map<String,Object> findById(Integer id);

    Integer findTodayOrderNumber(String today);

    Integer findTodayVisitsNumber(String today);

    Integer findThisRegTimeBetweenOrderNumber(Map<String, String> params);

    Integer findThisRegTimeBetweenVisitsNumber(Map<String, String> params);

    List<Map<String,Object>> findHotSetmeal();
}
