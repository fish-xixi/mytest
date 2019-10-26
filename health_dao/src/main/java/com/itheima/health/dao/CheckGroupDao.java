package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.CheckGroup;

import java.util.List;
import java.util.Map;

/**
 * @ClassName CheckItemDao
 * @Description TODO
 * @Author ly
 * @Company 深圳黑马程序员
 * @Date 2019/10/13 9:53
 * @Version V1.0
 */
public interface CheckGroupDao {

    void add(CheckGroup checkGroup);

    //void addCheckGroupCheckItem(@Param(value = "checkGroup_Id") Integer checkGroupId, @Param(value = "checkItem_Id") Integer checkItemId);

    void addCheckGroupCheckItem(Map<String, Integer> map);

    Page<CheckGroup> findPage(String queryString);

    CheckGroup findById(Integer id);

    List<Integer> findCheckItemIdsByCheckGroupId(Integer id);

    void edit(CheckGroup checkGroup);

    void deleteCheckGroupAndCheckItemByCheckItemId(Integer id);

    List<CheckGroup> findAll();

    List<CheckGroup> findCheckGroupListBySetmealId(Integer setmealId);
}
