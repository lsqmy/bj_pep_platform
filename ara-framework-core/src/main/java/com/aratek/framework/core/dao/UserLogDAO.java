package com.aratek.framework.core.dao;

import com.aratek.framework.domain.base.mapper.BaseDAO;
import com.aratek.framework.domain.core.UserLog;

import java.util.List;

/**
 * @author shijinlong
 * @date 2018-05-09
 * @description 用户操作日志DAO
 */
public interface UserLogDAO extends BaseDAO<UserLog> {

    List<UserLog> selectUserLogList(UserLog userLog);
}
