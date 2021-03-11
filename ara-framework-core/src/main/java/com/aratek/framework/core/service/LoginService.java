package com.aratek.framework.core.service;

import com.aratek.framework.domain.core.AppRegister;
import com.aratek.framework.domain.core.User;

import java.util.Map;

/**
 * @author shijinlong
 * @date 2018-04-28
 * @description 登录注销 接口类
 */
public interface LoginService {

    /**
     * 登录
     *
     * @param user
     * @return
     */
    Map login(User user);

    /**
     * 刷新token
     *
     * @param user
     * @return
     */
    Map refreshToken(User user);

    /**
     * 注销
     *
     * @return
     */
    Map logout();


    /**
     * APP登录
     *
     * @param appRegister
     * @return
     */
    Map appLogin(AppRegister appRegister);
}
