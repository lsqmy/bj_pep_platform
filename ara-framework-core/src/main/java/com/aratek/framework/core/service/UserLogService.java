package com.aratek.framework.core.service;


import com.aratek.framework.domain.core.UserLog;

/**
 * @author shijinlong
 * @date 2018-05-09
 * @description 用户操作日志 接口类
 */
public interface UserLogService {

    /**
     * 记录用户操作日志
     *
     * @param userLog
     * @return
     */
    boolean insertUserLog(UserLog userLog);
}
