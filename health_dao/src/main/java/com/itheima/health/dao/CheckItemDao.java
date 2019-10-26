package com.itheima.health.dao;

import com.itheima.health.pojo.CheckItem;

import java.util.List;

/**
 * @ClassName CheckItemDao
 * @Description TODO
 * @Author ly
 * @Company 深圳黑马程序员
 * @Date 2019/10/13 9:53
 * @Version V1.0
 */
public interface CheckItemDao {
    void add(CheckItem checkItem);

    List<CheckItem> findPage(String queryString);

    void deleteById(Integer id);

    long findCheckGroupAndCheckItemByCheckItemId(Integer id);

    CheckItem findById(Integer id);

    void edit(CheckItem checkItem);

    List<CheckItem> findAll();

    List<CheckItem> findCheckItemsListByCheckGroupId(Integer checkGroupId);
}
