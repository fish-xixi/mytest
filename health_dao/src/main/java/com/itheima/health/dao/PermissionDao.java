package com.itheima.health.dao;

import com.itheima.health.pojo.Permission;

import java.util.Set;

/**
 * @ClassName CheckItemDao
 * @Description TODO
 * @Author ly
 * @Company 深圳黑马程序员
 * @Date 2019/10/13 9:53
 * @Version V1.0
 */
public interface PermissionDao {
    // 使用角色id，查询当前角色具有的权限集合
    Set<Permission> findPermissionsByRoleId(Integer roleId);
}
