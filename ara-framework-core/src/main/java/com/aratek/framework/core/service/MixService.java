package com.aratek.framework.core.service;


/**
 * @author shijinlong
 * @date 2018-05-14
 * @description 混合 接口类
 */
public interface MixService {

    /**
     * 查询最大number加1
     *
     * @param tableName  表名
     * @param columeName 字段名
     */
    Integer selectNumber(String tableName, String columeName);

    /**
     * 判断值是否存在
     *
     * @param tableName
     * @param columeName
     * @param columeValue
     * @return
     */
    boolean isValueExist(String tableName, String columeName, String columeValue);

}
