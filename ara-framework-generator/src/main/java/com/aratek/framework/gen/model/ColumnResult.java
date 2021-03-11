package com.aratek.framework.gen.model;

/**
 * 列的属性
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年12月20日 上午12:01:45
 */
public class ColumnResult {
    /**
     * 列名
     **/
    private String columnName;

    /**
     * 列名类型
     */
    private String dataType;

    /**
     * 列名备注(这里放中文列名)
     */
    private String comments;

    /**
     * 属性名称(第一个字母大写)，如：user_name => UserName
     */
    private String attrName;

    /**
     * 属性名称(第一个字母小写)，如：user_name => userName
     */
    private String attrname;

    /**
     * 属性类型
     */
    private String attrType;

    /**
     * 可以为空
     */
    private String nullable;

    /**
     * 长度
     */
    private Integer length;

    private String extra;

    /**
     * 检查是否为主键
     */
    private String columnKey;

    /**
     * 业务类型：
     * fid : 主键 -> 不予显示
     * time : 时间类型 -> sql使用时间段查询，vue 使用 timepicker组件
     * date : 日期类型 -> sql使用时间段查询，vue使用datepicker组件
     * status : 状态类型 -> vue显示时用 Dot组件，查数据，有几种类型，没有的话默认0或1
     * gender : 性别 -> 男女，html显示用render,vue查询时用radio
     * nation : 民族 -> 集成民族部分代码
     * string : 编号或名称 -> 头数据模糊查询
     * other : 其他 -> equal查询
     */
    private String businessType;

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getAttrname() {
        return attrname;
    }

    public void setAttrname(String attrname) {
        this.attrname = attrname;
    }

    public String getAttrName() {
        return attrName;
    }

    public void setAttrName(String attrName) {
        this.attrName = attrName;
    }

    public String getAttrType() {
        return attrType;
    }

    public void setAttrType(String attrType) {
        this.attrType = attrType;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getNullable() {
        return nullable;
    }

    public void setNullable(String nullable) {
        this.nullable = nullable;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public void setColumnKey(String columnKey) {
        this.columnKey = columnKey;
    }

    public String getColumnKey() {
        return columnKey;
    }

    public String getBusinessType() {
        return businessType;
    }

    public ColumnResult setBusinessType(String businessType) {
        this.businessType = businessType;
        return this;
    }
}
