package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.entity.Result;
import com.itheima.health.service.OrderSettingService;
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
@RequestMapping("/ordersetting")
public class OrderSettingMobileController {

    @Reference
    OrderSettingService orderSettingService;

    // 完成体检预约，返回Result
    @RequestMapping(value = "/test")
    public Result test(){
        orderSettingService.testUpdate();
        return new Result(true,"");
    }

    // 完成体检预约，返回Result
    @RequestMapping(value = "/test2")
    public Result test2(){
        orderSettingService.testUpdate2();
        return new Result(true,"");
    }

}
