package com.aratek.framework.core.service;


import com.aratek.framework.domain.core.UserLog;
import com.aratek.framework.domain.core.UserLoginLog;

import java.util.List;
import java.util.Map;

/**
 * @author shijinlong
 * @date 2018-05-15
 * @description 日志服务 接口类
 */
public interface LogService {

    /**
     * 分页查询用户登录日志列表
     *
     * @param userLoginLog 查询条件
     * @return
     */
    List<UserLoginLog> selectUserLoginLogList(UserLoginLog userLoginLog);

    /**
     * 导出用户登录日志列表
     *
     * @param userLoginLog
     * @return
     */
    Map exportUserLoginLogList(UserLoginLog userLoginLog);

    /**
     * 分页查询用户操作日志列表
     *
     * @param userLog 查询条件
     * @return
     */
    List<UserLog> selectUserLogList(UserLog userLog);

    /**
     * 导出用户操作日志列表
     *
     * @param userLog
     * @return
     */
    Map exportUserLogList(UserLog userLog);


}
