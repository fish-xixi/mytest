package com.itheima.health.test;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.service.OrderSettingService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @ClassName TestThread
 * @Description TODO
 * @Author ly
 * @Company 深圳黑马程序员
 * @Date 2019/10/21 0:55
 * @Version V1.0
 */
@RunWith(value = SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:springmvc.xml")
public class TestThread {

    @Reference
    OrderSettingService orderSettingService;

    @Test
    public void test(){
        orderSettingService.testUpdate();
    }
}
