package com.itheima.health.controller;

import com.itheima.health.constants.MessageConstant;
import com.itheima.health.constants.RedisMessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

/**
 * @ClassName CheckItemController
 * @Description TODO
 * @Author ly
 * @Company 深圳黑马程序员
 * @Date 2019/10/13 9:56
 * @Version V1.0
 */
@RestController
@RequestMapping("/validateCode")
public class ValidateCodeMobileController {

    @Autowired
    JedisPool jedisPool;

    // 业务场景：体检预约的时候发送验证码
    @RequestMapping(value = "/send4Order")
    public Result send4Order(String telephone){
        try {
            // 1：生成4位的验证码
            Integer code = ValidateCodeUtils.generateValidateCode(4);
            // 2：调用阿里云短信服务工具类，发送短信（内容：生成的验证码）
            // SMSUtils.sendShortMessage(telephone,code.toString());
            System.out.println("发送验证码成功！验证码是："+code);
            // 3：将手机号作为key，验证码作为值，保存到Redis中（设置redis的保存时间5分钟），目的后续的校验
            jedisPool.getResource().setex(telephone+ RedisMessageConstant.SENDTYPE_ORDER,5*60,code.toString());
            return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
    }

    // 业务场景：登录的时候发送验证码
    @RequestMapping(value = "/send4Login")
    public Result send4Login(String telephone){
        try {
            // 1：生成4位的验证码
            Integer code = ValidateCodeUtils.generateValidateCode(4);
            // 2：调用阿里云短信服务工具类，发送短信（内容：生成的验证码）
            // SMSUtils.sendShortMessage(telephone,code.toString());
            System.out.println("发送验证码成功！验证码是："+code);
            // 3：将手机号作为key，验证码作为值，保存到Redis中（设置redis的保存时间5分钟），目的后续的校验
            jedisPool.getResource().setex(telephone+ RedisMessageConstant.SENDTYPE_LOGIN,5*60,code.toString());
            return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
    }

}
