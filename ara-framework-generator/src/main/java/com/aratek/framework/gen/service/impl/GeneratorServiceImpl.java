package com.aratek.framework.gen.service.impl;

import com.aratek.framework.core.dao.MysqlTableDAO;
import com.aratek.framework.core.exception.AraBaseException;
import com.aratek.framework.gen.model.ColumnResult;
import com.aratek.framework.gen.model.TableResult;
import com.aratek.framework.gen.service.GeneratorService;
import com.aratek.framework.gen.util.GenUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;

/**
 * @author jiangjy
 */
@Service("sysGeneratorService")
public class GeneratorServiceImpl implements GeneratorService {

    @Autowired
    private MysqlTableDAO mysqlTableDAO;

    @Override
    public List<Map<String, Object>> queryList(Map<String, Object> map) {
        return mysqlTableDAO.queryList(map);
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return mysqlTableDAO.queryTotal(map);
    }

    @Override
    public Map<String, String> queryTable(String tableName) {
        return mysqlTableDAO.queryTable(tableName);
    }

    @Override
    public List<Map<String, Object>> queryColumns(String tableName) {
        return mysqlTableDAO.queryColumns(tableName);
    }

    @Override
    public byte[] generatorCode(String[] tableNames) throws AraBaseException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);

        for (String tableName : tableNames) {
            //查询表信息
            Map<String, String> table = queryTable(tableName);
            //查询列信息
            List<Map<String, Object>> columns = queryColumns(tableName);
            //生成代码
            TableResult result = GenUtils.getTableResult(table, columns);
            setColumnBusinessTypeStatus(result);
            GenUtils.generatorCode(result, zip);
        }
        IOUtils.closeQuietly(zip);
        return outputStream.toByteArray();
    }

    /**
     * 对status字段做附加处理
     *
     * @param tableResult 查出来的表结果
     */
    private void setColumnBusinessTypeStatus(TableResult tableResult) {
        Map<String, Object> queryMap = new HashMap<String, Object>(3, 1);
        queryMap.put("tableName", tableResult.getTableName().toLowerCase());
        queryMap.put("columnName", "");
        for (ColumnResult columnResult : tableResult.getColumns()) {
            if ("status".equals(columnResult.getBusinessType())) {
                queryMap.put("columnName", columnResult.getColumnName());
                List<String> statusRes = mysqlTableDAO.queryStatus(queryMap);
                if (statusRes.size() > 1) {
                    for (String status : statusRes) {
                        columnResult.setBusinessType(columnResult.getBusinessType() + ":" + status);
                    }
                } else {
                    columnResult.setBusinessType(columnResult.getBusinessType() + ":0:1");
                }
            }
        }
    }

}

