package com.example.spb.realm;

import com.example.spb.entity.dto.JwtToken;
import com.example.spb.entity.po.User;
import com.example.spb.service.UserService;
import com.example.spb.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


public class MyRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;


    private JwtUtil jwtUtil;
    // 用户认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

//        JwtToken jwtToken = (JwtToken) authenticationToken;
//        String jwt = (String) jwtToken.getPrincipal();
//
//        // 解析验证 JWT
//        Claims claims = jwtUtil.getClaimsByToken(jwt);
//        String job_id = claims.getSubject();
//
//
//        // 根据用户名查询数据库中的用户信息
//        User user = userService.findByJobID(job_id);
//
//        if (user == null) {
//            // 如果用户不存在，抛出 UnknownAccountException 异常
//            throw new UnknownAccountException("用户不存在");
//        }
//        // 如果用户名和密码验证通过，返回一个包含用户信息的 AuthenticationInfo 对象
//        return new SimpleAuthenticationInfo(job_id, jwt, getName());

        UsernamePasswordToken upToken = (UsernamePasswordToken) token;
        String job_id = upToken.getUsername();
        String password = new String(upToken.getPassword());

        // 从数据库或其他数据源查询用户信息
        User user = userService.findByJobID(job_id);
        if (user == null) {
            throw new UnknownAccountException("User not found");
        }else {
            if(!password.equals(user.getPassword()))
                throw new AuthenticationException("PassWord Wrong");
            else {
                SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(user.getJobId(), user.getPassword(), getName());

                // 如果用户存在，返回认证信息
                return simpleAuthenticationInfo;
            }
        }
    }

    // 用户授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        // 在这里实现用户的授权逻辑，比如根据用户名从数据库中查询用户的角色和权限信息，并返回授权信息
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        System.out.println("Principal: " + principalCollection);

        String job_id = (String) principalCollection.getPrimaryPrincipal();
        String role = userService.findByJobID(job_id).getRole();
        // 根据用户身份添加角色和权限

        if (role.equals("党员")) {
            info.addRole("党员");
        } else if (role.equals("支部书记")) {
            info.addRole("支部书记");
            info.addStringPermission("manage");
        }else if (role.equals("积极分子")){
            info.addRole("积极分子");
        }else {
            info.addRole("超级管理员");
            info.addStringPermission("manage");
        }

        return info;


    }
}

