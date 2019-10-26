package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.health.dao.UserPermissionsDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.pojo.Permission;
import com.itheima.health.pojo.User;
import com.itheima.health.service.UserPermissionsService;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


@Service(interfaceClass = UserPermissionsService.class)
public class UserPermissionsServiceImpl implements UserPermissionsService {

    @Autowired
    private UserPermissionsDao userPermissionsDao;

    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        //1、初始化参数——将得到的当前页b,和页码要求的每页显示的数量为b,一起计算后内部传给PageInfo
        PageHelper.startPage(currentPage, pageSize);
        //2、查询数据
        List<User> checkGroupList = userPermissionsDao.findPage(queryString);
        //3、封装PageHelp对应的结果集
        PageInfo<User> pageInfo = new PageInfo<>(checkGroupList);
        return new PageResult(pageInfo.getTotal(), pageInfo.getList());
    }

    @Override
    public User findById(Integer id) {
        User user = userPermissionsDao.findById(id);
        return user;
    }

    @Override
    public List<Permission> findAll() {
        List<Permission> permissionsList = userPermissionsDao.findAll();
        return permissionsList;
    }

    @Override
    public void edit(User user, Integer[] permissionsIds) {
       /* //1、更改权限（permission）——先删除原有的permission信息，再重新绑定
        userPermissionsDao.delete(user.getId());
        //2、更改基本信息
        userPermissionsDao.update(user);
        System.out.println("=====" + permissionsIds);*/
        //3、中间表的数据重新绑定
        addCheckGroup_CheckItems(user.getId(), permissionsIds);
    }

    private void addCheckGroup_CheckItems(Integer checkGroupId, Integer[] checkitemIds) {
        if (checkitemIds != null && checkitemIds.length > 0) {
            for (int i = 0; i < checkitemIds.length; i++) {
                //userPermissionsDao.addCheckGroup_CheckItems(checkGroupId, checkitemIds[i]);
            }
        }
    }
}
