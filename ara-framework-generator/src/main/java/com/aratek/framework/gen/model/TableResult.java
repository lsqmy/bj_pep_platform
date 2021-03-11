package com.aratek.framework.gen.model;

import java.util.List;

/**
 * 表数据
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年12月20日 上午12:02:55
 */
public class TableResult {
    //表的名称
    private String tableName;
    //表的备注
    private String comments;
    //表的主键
    private ColumnResult pk;
    //表的列名(不包含主键)
    private List<ColumnResult> columns;

    //类名(第一个字母大写)，如：sys_user => SysUser
    private String className;
    //类名(第一个字母小写)，如：sys_user => sysUser
    private String objectName;

    /**
     * 表别名 t0,t1....
     */
    private String alias;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public ColumnResult getPk() {
        return pk;
    }

    public void setPk(ColumnResult pk) {
        this.pk = pk;
    }

    public List<ColumnResult> getColumns() {
        return columns;
    }

    public void setColumns(List<ColumnResult> columns) {
        this.columns = columns;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public String getAlias() {
        return alias;
    }

    public TableResult setAlias(String alias) {
        this.alias = alias;
        return this;
    }
}
