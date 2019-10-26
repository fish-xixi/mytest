package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constants.MessageConstant;
import com.itheima.health.constants.RedisMessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Member;
import com.itheima.health.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
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
@RequestMapping("/login")
public class LoginMobileController {

    @Autowired
    JedisPool jedisPool;

    @Reference
    MemberService memberService;


    // 手机登录登录
    @RequestMapping(value = "/check")
    public Result check(@RequestBody Map<String,Object> map, HttpServletResponse response){
        try {
            // 1：获取手机号和验证码
            String telephone = (String)map.get("telephone");
            String validateCode = (String)map.get("validateCode");
            // 2：取出redis里面的验证码和用户页面输入的验证码比较
            String redisValidateCode = jedisPool.getResource().get(telephone+ RedisMessageConstant.SENDTYPE_LOGIN);
            if(redisValidateCode==null || !redisValidateCode.equals(validateCode)){
                return new Result(false, MessageConstant.VALIDATECODE_ERROR);
            }
            // 3：使用手机号查询会员表，判断是否是会员
            Member member = memberService.findMemberByTelephone(telephone);
            if(member==null){
                // 表示现在还不是会员，注册会员
                member = new Member();
                member.setPhoneNumber(telephone);
                member.setRegTime(new Date()); // 注册时间
                memberService.add(member);
            }
            // 4：使用Cookie存放用户信息（保存登录状态）
            Cookie cookie = new Cookie("login_member_telephone",telephone);
            cookie.setPath("/");// 有效路径
            cookie.setMaxAge(30*24*60*60); // 有效时间
            response.addCookie(cookie);
            return new Result(true, MessageConstant.LOGIN_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.LOGIN_FAIL);
        }
    }

}
