package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.health.constants.RedisConstant;
import com.itheima.health.dao.CheckGroupDao;
import com.itheima.health.dao.CheckItemDao;
import com.itheima.health.dao.SetmealDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.pojo.CheckGroup;
import com.itheima.health.pojo.CheckItem;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
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
@Service(interfaceClass = SetmealService.class)
@Transactional
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    SetmealDao setmealDao;

    @Autowired
    JedisPool jedisPool;

    @Override
    public void add(Setmeal setmeal, Integer[] checkgroupIds) {
        // 1：新增套餐，向t_setmeal中添加1条数据
        setmealDao.add(setmeal);
        // 2：新增套餐和检查组的中间表，向t_setmeal_checkgroup中添加多条数据（套餐的id，检查组的id）
        if(checkgroupIds!=null && checkgroupIds.length>0){
            addSetmealAndCheckGroup(setmeal.getId(),checkgroupIds);
        }
        // 3：当用户添加套餐后，将图片名称保存到redis的另一个Set集合中，例如集合名称为setmealPicDbResources
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,setmeal.getImg());
    }

    @Override
    public PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString) {
        // 1：初始化PageHelper
        PageHelper.startPage(currentPage,pageSize);
        // 2：查询，返回Page
        Page<Setmeal> page = setmealDao.findPage(queryString);
        // 3：结果封装
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public List<Setmeal> findAll() {
        return setmealDao.findAll();
    }

    // 使用mybatis的底层封装原理，封装数据
//    @Override
//    public Setmeal findById(Integer id) {
//        Setmeal setmeal = setmealDao.findById(id);
//        return setmeal;
//    }

    // 使用代码的方式处理业务逻辑
    @Autowired
    CheckGroupDao checkGroupDao;

    @Autowired
    CheckItemDao checkItemDao;

    @Override
    public Setmeal findById(Integer id) {
        Setmeal setmeal = setmealDao.findById(id); // 第一步
        List<CheckGroup> checkGroupList = checkGroupDao.findCheckGroupListBySetmealId(setmeal.getId());
        // 遍历checkGroupList
        for(CheckGroup checkgroup:checkGroupList){
            List<CheckItem> checkItemList = checkItemDao.findCheckItemsListByCheckGroupId(checkgroup.getId());
            checkgroup.setCheckItems(checkItemList);
        }
        setmeal.setCheckGroups(checkGroupList);
        return setmeal;
    }

    @Override
    public List<Map<String, Object>> findSetmealReport() {
        return setmealDao.findSetmealReport();
    }

    // 2：新增套餐和检查组的中间表
    private void addSetmealAndCheckGroup(Integer setmealId, Integer[] checkgroupIds) {
        for (Integer checkgroupId : checkgroupIds) {
            Map<String,Integer> map = new HashMap<>();
            map.put("setmealId",setmealId);
            map.put("checkgroupId",checkgroupId);
            setmealDao.addSetmealAndCheckGroup(map);
        }
    }
}
