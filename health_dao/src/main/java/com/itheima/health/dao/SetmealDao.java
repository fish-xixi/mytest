package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.Setmeal;

import java.util.List;
import java.util.Map; /**
 * @ClassName CheckItemDao
 * @Description TODO
 * @Author ly
 * @Company 深圳黑马程序员
 * @Date 2019/10/13 9:53
 * @Version V1.0
 */
public interface SetmealDao {

    void add(Setmeal setmeal);

    void addSetmealAndCheckGroup(Map<String, Integer> map);

    Page<Setmeal> findPage(String queryString);

    List<Setmeal> findAll();

    Setmeal findById(Integer id);

    List<Map<String,Object>> findSetmealReport();
}
