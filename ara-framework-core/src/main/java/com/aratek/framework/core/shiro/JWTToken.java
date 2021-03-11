package com.aratek.framework.core.shiro;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @author shijinlong
 * @date 2018-05-22
 * @description JWT token
 */
public class JWTToken implements AuthenticationToken {

    private String token;
    private String digest;

    public JWTToken(String token, String digest) {
        this.token = token;
        this.digest = digest;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return digest;
    }
}
