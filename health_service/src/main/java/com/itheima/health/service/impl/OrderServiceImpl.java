package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.constants.MessageConstant;
import com.itheima.health.dao.MemberDao;
import com.itheima.health.dao.OrderDao;
import com.itheima.health.dao.OrderSettingDao;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Member;
import com.itheima.health.pojo.Order;
import com.itheima.health.pojo.OrderSetting;
import com.itheima.health.service.OrderService;
import com.itheima.health.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
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
@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService {

    // 预约
    @Autowired
    OrderDao orderDao;

    // 会员
    @Autowired
    MemberDao memberDao;

    // 预约设置
    @Autowired
    OrderSettingDao orderSettingDao;



    @Override
    public Result submit(Map<String, Object> map) {
            /**
             * 1：使用预约 时间作为查询条件，查询预约设置表，判断当前时间是否可以预约
                 可以预约：往下执行
                 不可以预约：提示：”当前时间不能进行体检预约”
             */
            String orderDate = (String)map.get("orderDate");
            Date date = null; // 预约时间
            try {
                date = DateUtils.parseString2Date(orderDate);
            } catch (Exception e) {
                e.printStackTrace();
            }
            OrderSetting orderSetting = orderSettingDao.findOrderSettingPojoByOrderDate(date);
            // 没有查询出来，此时不可以预约
            if(orderSetting==null){
                return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
            }
            /**
             * 2：获取预约设置表中的最多可预约人数和实际预约人数，如果2个值相等，此时提示：“预约已满，不能预约”
             */
            int number = orderSetting.getNumber();
            int reservations = orderSetting.getReservations();
            if(reservations>=number){
                return new Result(false, MessageConstant.ORDER_FULL);
            }
            /**
             * 3：使用手机号作为查询条件，查询会员表，判断当前手机号是否注册过会员
             如果没有查询到结果，此时当前手机号没有注册过会员，自动注册会员（向会员表中新增一条数据）
             如果查询到结果，此时当前手机号是会员，判断当前会员是否在同一时间，预约了同一套餐，保证不能重复预约，使用会员id、预约时间、套餐id，查询预约表（t_order），如果存在数据，就说明是重复预约，提示：“同一套餐、同一时间不能重复预约”
             */
            // 获取手机号
            String telephone = (String)map.get("telephone");
            Member member =  memberDao.findMemberByTelephone(telephone);
            // 如果没有查询到结果，此时当前手机号没有注册过会员，自动注册会员（向会员表中新增一条数据）
            if(member==null){
                member = new Member();
                member.setName((String)map.get("name")); // 姓名
                member.setPhoneNumber(telephone); // 手机号
                member.setIdCard((String)map.get("idCard")); // 身份证号
                member.setSex((String)map.get("sex")); // 性别
                member.setRegTime(new Date()); // 会员注册时间，当前时间
                memberDao.add(member); // member中有新增id
            }
            //如果查询到结果，此时当前手机号是会员，判断当前会员是否在同一时间，预约了同一套餐，保证不能重复预约，使用会员id、预约时间、套餐id，查询预约表（t_order），如果存在数据，就说明是重复预约，提示：“同一套餐、同一时间不能重复预约”
            else{
                // 组织查询条件
                Order order = new Order(member.getId(),date,null,null,Integer.parseInt((String)map.get("setmealId")));
                List<Order> list = orderDao.findOrderListByCondition(order);
                // 查询到结果，表示重复预约
                if(list!=null && list.size()>0){
                    return new Result(false, MessageConstant.HAS_ORDERED);
                }
            }

            // 4：如果可以预约，根据预约时间更新预约设置表，让可预约人数的字段+1
            //orderSetting.setReservations(orderSetting.getReservations()+1);
            //orderSettingDao.updateReservationsByOrderDate(orderSetting);
            orderSettingDao.updateReservationsByOrderDate(date);
            // 5：如果可以预约，向预约表中插入1条数据（t_order），表示完成预约
            Order order = new Order(member.getId(),date,(String)map.get("orderType"),Order.ORDERSTATUS_NO,Integer.parseInt((String)map.get("setmealId")));
            orderDao.add(order);
            return new Result(true,MessageConstant.ORDER_SUCCESS,order);

    }

    @Override
    public Map<String, Object> findById(Integer id) {
        Map<String,Object> map = orderDao.findById(id);
        if(map!=null){
            Date orderDate = (Date)map.get("orderDate");
            // 将日期类型转换成字符串类型
            try {
                String orderDateStr = DateUtils.parseDate2String(orderDate);
                map.put("orderDate",orderDateStr);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return map;
    }
}
