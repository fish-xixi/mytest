package com.itheima.health.service;

import com.itheima.health.entity.PageResult;
import com.itheima.health.pojo.Permission;
import com.itheima.health.pojo.User;


import java.util.List;

public interface UserPermissionsService {
    PageResult findPage(Integer currentPage, Integer pageSize, String queryString);

    User findById(Integer id);

    List<Permission> findAll();

    void edit(User user, Integer[] permissionsIds);
}
