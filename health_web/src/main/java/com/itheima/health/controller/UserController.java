package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constants.MessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName CheckItemController
 * @Description TODO
 * @Author ly
 * @Company 深圳黑马程序员
 * @Date 2019/10/13 9:56
 * @Version V1.0
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Reference
    UserService userService;

    // 从SpringSecurity中获取用户名
    @RequestMapping(value = "/getUsername")
    public Result getUsername(){
        try {
            User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String username = user.getUsername(); // username表示登录名，admin
            // 获取真实姓名，使用登录名查询数据库，返回真实姓名
            return new Result(true, MessageConstant.GET_USERNAME_SUCCESS,username);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_USERNAME_FAIL);
        }
    }

}
