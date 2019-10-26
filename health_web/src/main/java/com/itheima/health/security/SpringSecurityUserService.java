package com.itheima.health.security;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.pojo.Permission;
import com.itheima.health.pojo.Role;
import com.itheima.health.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @ClassName SpringSecurityUserService
 * @Description TODO
 * @Author ly
 * @Company 深圳黑马程序员
 * @Date 2019/10/23 8:50
 * @Version V1.0
 */
@Component // id="springSecurityUserService"
public class SpringSecurityUserService implements UserDetailsService {

    @Reference
    UserService userService;


    // 从数据库查询完成认证和授权，传递的参数就是username，username表示登录名
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 使用登录名，查询数据库，获取User对象
        com.itheima.health.pojo.User user = userService.findUserByUsername(username);
        // 当前页面输入的用户名在数据库中不存在，此时表示登录不成功（用户名输入有误）:return null:表示用户名输入有误，后台会抛出一个异常
        // org.springframework.security.authentication.InternalAuthenticationServiceException
        if(user==null){
            return null;
        }
        // 如果用户名输入正确，比对密码
        String password = user.getPassword(); // 通过BCryptPasswordEncoder加密后的密码
        // 权限的集合（添加角色、权限）（后续从数据库查询）
        List<GrantedAuthority> list = new ArrayList<>();
        Set<Role> roles = user.getRoles();
        if(roles!=null && roles.size()>0){
            for (Role role : roles) {
                list.add(new SimpleGrantedAuthority(role.getKeyword())); // 角色
                Set<Permission> permissions = role.getPermissions();
                if(permissions!=null && permissions.size()>0){
                    for (Permission permission : permissions) {
                        list.add(new SimpleGrantedAuthority(permission.getKeyword())); // 权限
                    }
                }
            }
        }
        /**
         * public User(String username, String password, Collection<? extends GrantedAuthority>
         *     参数一：登录名
         *     参数二：比对的密码（默认会使用从数据库查询的密码和页面输入的密码进行比对，如果一致，表示登录成功，如果不一致，抛出异常，表示密码输入有误）
         *     参数三：权限的集合
         */
        // org.springframework.security.authentication.BadCredentialsException: Bad credentials
        return new org.springframework.security.core.userdetails.User(user.getUsername(),password,list);
    }
}
