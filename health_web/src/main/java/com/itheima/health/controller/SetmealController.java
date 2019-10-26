package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constants.MessageConstant;
import com.itheima.health.constants.RedisConstant;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.SetmealService;
import com.itheima.health.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.util.UUID;

/**
 * @ClassName CheckItemController
 * @Description TODO
 * @Author ly
 * @Company 深圳黑马程序员
 * @Date 2019/10/13 9:56
 * @Version V1.0
 */
@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Reference
    SetmealService setmealService;

    @Autowired
    JedisPool jedisPool;

    // 文件上传（springmvc上传）
    // 要求1：springmvc.xml配置CommonsMultipartResolver
    // 要求2：Controller类方法的参数上添加MultipartFile，用于接收页面传递的文件信息（字节）
    @RequestMapping(value = "/upload")
    public Result upload(@RequestParam(value = "imgFile") MultipartFile imgFile){
        try {
            // 文件名
            String filename = imgFile.getOriginalFilename();
            // 随机生成UUID的值作为文件名（防止文件名冲突）
            filename = UUID.randomUUID().toString()+filename.substring(filename.lastIndexOf("."));
            // 图片上传（上传到七牛云）
            QiniuUtils.upload2Qiniu(imgFile.getBytes(),filename);
            // 1、当用户上传图片后，将图片名称保存到redis的一个Set集合中，例如集合名称为setmealPicResources
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES,filename);
            // 文件上传
            return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS,filename);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
        }
    }

    // 新增保存
    @RequestMapping(value = "/add")
    public Result add(@RequestBody Setmeal setmeal,Integer [] checkgroupIds){
        try {
            setmealService.add(setmeal,checkgroupIds);
            return new Result(true, MessageConstant.ADD_SETMEAL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_SETMEAL_FAIL);
        }
    }

    // 分页查询
    @RequestMapping(value = "/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        return setmealService.pageQuery(queryPageBean.getCurrentPage(),queryPageBean.getPageSize(),queryPageBean.getQueryString());
    }
}
