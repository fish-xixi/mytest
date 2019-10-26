package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constants.MessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.OrderSetting;
import com.itheima.health.service.OrderSettingService;
import com.itheima.health.utils.POIUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
@RequestMapping("/ordersetting")
public class OrderSettingController {

    @Reference
    OrderSettingService orderSettingService;

    // 文件上传：读取Excel中的数据，批量导入到数据库
    @RequestMapping(value = "/upload")
    public Result upload(MultipartFile excelFile){
        try {
            // 读取excel文件中的数据
            List<String[]> list = POIUtils.readExcel(excelFile);
            // 组织保存的数据List<OrderSetting>
            List<OrderSetting> orderSettingList = new ArrayList<>();
            for (String[] strings : list) {
                OrderSetting orderSetting = new OrderSetting(new Date(strings[0]),Integer.parseInt(strings[1]));
                orderSettingList.add(orderSetting);
            }
            orderSettingService.addList(orderSettingList);
            return new Result(true, MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }
    }

    // 使用当前年月，初始化当前年月对应的日期
    @RequestMapping(value = "/findOrderSettingByCurrentDate")
    public Result findOrderSettingByCurrentDate(String date){ // 参数2019-10
        try {
            /**
             * map
             * key         value
             * date          1
             * number        120
             * reservations  1
             */
            List<Map<String,Object>> list = orderSettingService.findOrderSettingByCurrentDate(date);
            return new Result(true, MessageConstant.GET_ORDERSETTING_SUCCESS,list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_ORDERSETTING_FAIL);
        }
    }

    // 点击设置，单条的方式使用预约时间更新预约人数
    @RequestMapping(value = "/updateNumberByOrderDate")
    public Result updateNumberByOrderDate(@RequestBody OrderSetting orderSetting){ //
        try {
            orderSettingService.updateNumberByOrderDate(orderSetting);
            return new Result(true, MessageConstant.ORDERSETTING_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ORDERSETTING_FAIL);
        }
    }
}
