package com.itheima.health.security;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constants.MessageConstant;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Permission;
import com.itheima.health.pojo.Role;
import com.itheima.health.pojo.User;
import com.itheima.health.service.UserPermissionsService;
import com.itheima.health.service.UserService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@RestController
@RequestMapping("/userPermissions")
public class UserPermissionsController {

    @Reference
    private UserPermissionsService userPermissionsService;

    @Reference
    private UserService userService;

    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        PageResult pageResult = userPermissionsService.findPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize(), queryPageBean.getQueryString());
        return pageResult;
    }

    @RequestMapping("/findById")
    public Result findById(Integer id) {
        User user = userPermissionsService.findById(id);
        if (user != null) {
            return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS, user);
        } else {
            return new Result(false, MessageConstant.QUERY_CHECKGROUP_FAIL);
        }

    }

    @RequestMapping("/findAll")
    public Result findAll() {
        List<Permission> permissionList = userPermissionsService.findAll();
        if (permissionList != null && permissionList.size() >0) {
            return new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,permissionList);
        } else {
            return new Result(false,MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
    }

    @RequestMapping("/findPermissionsByName")
    public List<Integer> findPermissionsByName (String name) {
        System.out.println("=====name=====" + name);

        User user = userService.findUserByUsername(name);
        if (user == null) {
            return null;
        }
        List<Integer> list = new ArrayList<>();
        Set<Role> roles = user.getRoles();
        for (Role role : roles) {
            Set<Permission> permissions = role.getPermissions();
            for (Permission permission : permissions) {
                System.out.println("==permission.getId()==" + permission.getId());
                list.add(permission.getId());
            }
        }
        return list;
    }

    @RequestMapping("/edit")
    public Result edit(@RequestBody User user, Integer[] permissionsIds) {
        try {
            userPermissionsService.edit(user,permissionsIds);
            return new Result(true,MessageConstant.EDIT_CHECKGROUP_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.EDIT_CHECKGROUP_FAIL);
        }
    }

}
