package com.aratek.framework.core.dao;

import java.util.List;
import java.util.Map;

/**
 * @Author 姜寄羽
 * 查询Mysql数据表
 * @Date 2018/1/30 10:46
 */
public interface MysqlTableDAO {
    List<Map<String, Object>> queryList(Map<String, Object> map);

    int queryTotal(Map<String, Object> map);

    Map<String, String> queryTable(String tableName);

    List<Map<String, Object>> queryColumns(String tableName);

    List<String> queryStatus(Map<String, Object> map);
}
