package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constants.MessageConstant;
import com.itheima.health.constants.RedisMessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Order;
import com.itheima.health.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Map;

/**
 * @ClassName CheckItemController
 * @Description TODO
 * @Author ly
 * @Company 深圳黑马程序员
 * @Date 2019/10/13 9:56
 * @Version V1.0
 */
@RestController
@RequestMapping("/order")
public class OrderMobileController {

    @Reference
    OrderService orderService;

    @Autowired
    JedisPool jedisPool;


    // 完成体检预约，返回Result
    @RequestMapping(value = "/submit")
    public Result submit(@RequestBody Map<String,Object> map ){
        Result result = null;
        try {
            /**
             * 一：比对验证码输入是否正确
             1：获取页面表单中的手机号和验证码
             2：使用手机号从Redis中获取redis存放到验证码
             3：页面输入的验证码和Redis中存放的验证码进行比对
             * 比对成功，往后直行
             * 比对不成功，说明验证码输入有误，不能进行提交
             */
            // 1：获取页面表单中的手机号和验证码
            String telephone = (String)map.get("telephone");
            String validateCode = (String)map.get("validateCode");
            // 2：使用手机号从Redis中获取redis存放到验证码
            String redisInValidateCode = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_ORDER);
            // 3：页面输入的验证码和Redis中存放的验证码进行比对
            if(redisInValidateCode == null || !redisInValidateCode.equals(validateCode)){
                return new Result(false,MessageConstant.VALIDATECODE_ERROR);
            }
            // 因为在移动端调用的预约保存的功能，预约类型应该属于微信预约
            map.put("orderType", Order.ORDERTYPE_WEIXIN);
            result = orderService.submit(map);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.ORDER_FAIL);
        }
    }

    // 使用订单ID，查询订单的详情信息
    @RequestMapping(value = "/findById")
    public Result findById(Integer id){
        Result result = null;
        try {
            Map<String,Object> map = orderService.findById(id);
            return new Result(false,MessageConstant.QUERY_ORDER_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_ORDER_FAIL);
        }
    }
}
