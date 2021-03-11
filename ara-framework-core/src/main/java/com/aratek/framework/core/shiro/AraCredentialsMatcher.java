package com.aratek.framework.core.shiro;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;

/**
 * @author shijinlong
 * @date 2018-05-23
 * @description 自定义shiro密码验证类
 */
public class AraCredentialsMatcher extends SimpleCredentialsMatcher {

    @Override
    public boolean doCredentialsMatch(AuthenticationToken authcToken,
                                      AuthenticationInfo info) {
        //用jwt验证即可，这里直接返回true
        return true;
    }

    @Override
    public boolean equals(Object tokenCredentials, Object accountCredentials) {
        return true;
    }
}
