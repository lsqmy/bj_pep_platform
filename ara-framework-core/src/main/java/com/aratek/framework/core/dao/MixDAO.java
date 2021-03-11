package com.aratek.framework.core.dao;

import org.apache.ibatis.annotations.Param;

/**
 * @author shijinlong
 * @date 2018-05-14
 * @description 混合DAO
 */
public interface MixDAO {

    /**
     * 查询最大number加1
     *
     * @param tableName  表名
     * @param columeName 字段名
     */
    Integer selectNumber(@Param("tableName") String tableName, @Param("columeName") String columeName);

    /**
     * 根据值查询表字段
     *
     * @param tableName
     * @param columeName
     * @param columeValue
     * @return
     */
    int selectValue(@Param("tableName") String tableName, @Param("columeName") String columeName, @Param("columeValue") String columeValue);

}
