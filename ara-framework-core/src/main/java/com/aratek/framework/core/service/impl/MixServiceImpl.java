package com.aratek.framework.core.service.impl;

import com.aratek.framework.core.dao.MixDAO;
import com.aratek.framework.core.service.MixService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author shijinlong
 * @date 2018-05-14
 * @description 混合 实现类
 */
@Service
public class MixServiceImpl implements MixService {

    @Autowired
    private MixDAO mixDAO;

    @Override
    public Integer selectNumber(String tableName, String columeName) {
        return mixDAO.selectNumber(tableName, columeName);
    }

    @Override
    public boolean isValueExist(String tableName, String columeName, String columeValue) {
        if (mixDAO.selectValue(tableName, columeName, columeValue) > 0) {
            //存在
            return true;
        } else {
            //不存在
            return false;
        }
    }
}
