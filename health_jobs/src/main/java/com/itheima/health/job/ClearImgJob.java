package com.itheima.health.job;

import com.itheima.health.constants.RedisConstant;
import com.itheima.health.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPool;

import java.util.Iterator;
import java.util.Set;

/**
 * @ClassName ClearImgJob
 * @Description TODO
 * @Author ly
 * @Company 深圳黑马程序员
 * @Date 2019/10/16 12:23
 * @Version V1.0
 */
public class ClearImgJob {

    @Autowired
    JedisPool jedisPool;

    // 使用Quartz清理图片
    public void clearImg(){
        // 1：获取2个key中不同 的数据，放置到set集合中
        Set<String> set = jedisPool.getResource().sdiff(RedisConstant.SETMEAL_PIC_RESOURCES, RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        Iterator<String> iterator = set.iterator();
        while(iterator.hasNext()){
            String pic = iterator.next();
            System.out.println("不同的图片名称："+pic);
            // 2：删除七牛云的图片
            QiniuUtils.deleteFileFromQiniu(pic);
            // 3：删除redis中上传图片的key中的值
            jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_RESOURCES,pic);
        }
    }
}
