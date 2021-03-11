package com.aratek.framework.core.shiro;

import com.aratek.framework.core.service.UserService;
import com.aratek.framework.core.util.JWTUtil;
import com.aratek.framework.domain.core.User;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

/**
 * @author shijinlong
 * @date 2018-05-01
 * @description 自定义shiro realm
 */
public class UserRealm extends AuthorizingRealm {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserRealm.class);

    @Autowired
    private UserService userService;

    @Override
    public boolean supports(AuthenticationToken token) {
        //仅支持UsernamePasswordToken 类型的Token
        return token != null && token instanceof JWTToken;
    }

    //登录验证后访问授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) throws AuthorizationException {
//        LOGGER.debug("====登录验证后进行权限认证....");
        String token = (String) principalCollection.getPrimaryPrincipal();
//        LOGGER.debug("====token={}", token);
        //解密token获得user ID+Name
        User user = JWTUtil.getUserFromToken(token);
        //1.设置用户的权限
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        Set<String> perms = userService.selectRightListByUserID(user.getfID());
        if (perms == null) {
            perms = new HashSet<String>();
        }
//        LOGGER.debug("====perms={}", perms);
        simpleAuthorizationInfo.setStringPermissions(perms);
        //2.设置用户的角色
//        Set<String> roles = userService.selectRoleNameListByUserID(user.getfID());
//        if (roles == null) {
//            roles = new HashSet<String>();
//        }
//        LOGGER.debug("====roles={}", roles);
//        simpleAuthorizationInfo.setRoles(roles);
        return simpleAuthorizationInfo;
    }

    //登录认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
//        LOGGER.debug("====登录操作进行登录认证....");
        //得到token
        JWTToken jwtToken = (JWTToken) authenticationToken;
        String token = (String) jwtToken.getPrincipal();
//        LOGGER.debug("====token={}", token);
        return new SimpleAuthenticationInfo(token, token, getName());
    }
}
