package com.aratek.framework.core.service.impl;

import com.aratek.framework.core.dao.UserLogDAO;
import com.aratek.framework.core.service.UserLogService;
import com.aratek.framework.core.util.CurrentUserUtil;
import com.aratek.framework.core.util.UUIDUtil;
import com.aratek.framework.domain.core.UserLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author shijinlong
 * @date 2018-05-09
 * @description 用户操作日志 实现类
 */
@Service
public class UserLogServiceImpl implements UserLogService {

    @Autowired
    private UserLogDAO userLogDAO;


    @Override
    public boolean insertUserLog(UserLog userLog) {
        userLog.setfID(UUIDUtil.genID());
        userLog.setfUserID(CurrentUserUtil.getCurrentUserID());
        userLog.setfCreateTime(new Date());
        userLogDAO.insertSelective(userLog);
        return true;
    }
}
