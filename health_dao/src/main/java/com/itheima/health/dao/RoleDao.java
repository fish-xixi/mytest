package com.itheima.health.dao;

import com.itheima.health.pojo.Role;

import java.util.Set;

/**
 * @ClassName CheckItemDao
 * @Description TODO
 * @Author ly
 * @Company 深圳黑马程序员
 * @Date 2019/10/13 9:53
 * @Version V1.0
 */
public interface RoleDao {

    // 根据用户id，查询角色的集合
    Set<Role> findRolesByUserId(Integer userId);
}
