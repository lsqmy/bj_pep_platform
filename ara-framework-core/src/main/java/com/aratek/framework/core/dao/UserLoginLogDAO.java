package com.aratek.framework.core.dao;

import com.aratek.framework.domain.base.mapper.BaseDAO;
import com.aratek.framework.domain.core.UserLoginLog;

import java.util.List;

/**
 * @author shijinlong
 * @date 2018-05-09
 * @description 用户登录日志DAO
 */
public interface UserLoginLogDAO extends BaseDAO<UserLoginLog> {

    List<UserLoginLog> selectUserLoginLogList(UserLoginLog userLoginLog);
}
