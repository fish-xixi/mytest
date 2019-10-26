package com.itheima.health.dao;

import com.itheima.health.pojo.Permission;
import com.itheima.health.pojo.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserPermissionsDao {
    List<User> findPage(String queryString);

    User findById(Integer id);

    List<Permission> findAll();
}
