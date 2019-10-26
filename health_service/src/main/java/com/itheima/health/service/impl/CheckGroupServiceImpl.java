package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.health.dao.CheckGroupDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.pojo.CheckGroup;
import com.itheima.health.service.CheckGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

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
@Service(interfaceClass = CheckGroupService.class)
@Transactional
public class CheckGroupServiceImpl implements CheckGroupService {

    @Autowired
    CheckGroupDao checkGroupDao;

    @Override
    public void add(CheckGroup checkGroup, Integer[] checkitemIds) {
        //1：组织基本信息数据，向检查组的表中添加1行数据
        checkGroupDao.add(checkGroup);
        //2：遍历checkitemIds的数组，获取检查项的id，同时和检查组的id，向检查项和检查组的中间表插入多行数据
        addCheckGroupAndCheckItem(checkGroup.getId(),checkitemIds);
    }

    @Override
    public PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString) {
        // 分页插件
        PageHelper.startPage(currentPage,pageSize);
        Page<CheckGroup> page = checkGroupDao.findPage(queryString);
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public CheckGroup findById(Integer id) {
        return checkGroupDao.findById(id);
    }

    @Override
    public List<Integer> findCheckItemIdsByCheckGroupId(Integer id) {
        return checkGroupDao.findCheckItemIdsByCheckGroupId(id);
    }

    @Override
    public void edit(CheckGroup checkGroup, Integer[] checkitemIds) {
        // 1：获取检查组的基本信息，执行更新
        checkGroupDao.edit(checkGroup);
        // 2：使用检查组的id，删除中间表的数据
        checkGroupDao.deleteCheckGroupAndCheckItemByCheckItemId(checkGroup.getId());
        // 3：重新遍历checkitemIds的数组，获取检查项的id，同时和检查组的id，向检查项和检查组的中间表插入多行数据
        addCheckGroupAndCheckItem(checkGroup.getId(),checkitemIds);
    }

    @Override
    public List<CheckGroup> findAll() {
        return checkGroupDao.findAll();
    }

    // 遍历checkitemIds的数组，获取检查项的id，同时和检查组的id，向检查项和检查组的中间表插入多行数据
    private void addCheckGroupAndCheckItem(Integer checkGroupId, Integer[] checkitemIds) {
        // 遍历
        for (Integer checkItemId : checkitemIds) {
            // 方案一：组织实体类，使用OGNL表达式解析
            // 方案二：Dao中传递多个参数，使用@Param
            //checkGroupDao.addCheckGroupCheckItem(checkGroupId,checkItemId);
            // 方案三：使用Map结构
            Map<String,Integer> map = new HashMap<>();
            map.put("checkGroup_Id",checkGroupId);
            map.put("checkItem_Id",checkItemId);
            checkGroupDao.addCheckGroupCheckItem(map);
        }
    }
}
