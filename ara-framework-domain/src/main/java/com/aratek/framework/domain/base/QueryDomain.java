package com.aratek.framework.domain.base;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Transient;
import java.util.Date;
import java.util.LinkedHashMap;

/**
 * @author shijinlong
 * @date 2018-05-09
 * @description 查询条件对象，其他对象不继承BaseDomain情况下需要继承此对象
 * 注意：非数据库表字段需加上@Transient注解，不然通用mapper会报错
 */
@ApiModel(value = "QueryDomain", description = "查询条件对象")
public class QueryDomain extends CurrentUser {

    @Transient
    @ApiModelProperty(value = "当前页数", example = "1")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Integer pageNum = 0;

    @Transient
    @ApiModelProperty(value = "每页条数", example = "10")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Integer pageSize = 0;

    @Transient
    @ApiModelProperty(value = "查询条件-创建时间-起始", example = "2018-04-23 17:22:11")
    private Date queryCreateTimeStart;

    @Transient
    @ApiModelProperty(value = "查询条件-创建时间-结束", example = "2018-05-23 17:22:11")
    private Date queryCreateTimeEnd;

    @Transient
    @ApiModelProperty(value = "查询条件-修改时间-起始", example = "2018-04-23 17:22:11")
    private Date queryUpdateTimeStart;

    @Transient
    @ApiModelProperty(value = "查询条件-修改时间-结束", example = "2018-05-23 17:22:11")
    private Date queryUpdateTimeEnd;

    @Transient
    @ApiModelProperty(value = "排序字段参数map")
    private LinkedHashMap<String, String> sortParams;


    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Date getQueryCreateTimeStart() {
        return queryCreateTimeStart;
    }

    public void setQueryCreateTimeStart(Date queryCreateTimeStart) {
        this.queryCreateTimeStart = queryCreateTimeStart;
    }

    public Date getQueryCreateTimeEnd() {
        return queryCreateTimeEnd;
    }

    public void setQueryCreateTimeEnd(Date queryCreateTimeEnd) {
        this.queryCreateTimeEnd = queryCreateTimeEnd;
    }

    public Date getQueryUpdateTimeStart() {
        return queryUpdateTimeStart;
    }

    public void setQueryUpdateTimeStart(Date queryUpdateTimeStart) {
        this.queryUpdateTimeStart = queryUpdateTimeStart;
    }

    public Date getQueryUpdateTimeEnd() {
        return queryUpdateTimeEnd;
    }

    public void setQueryUpdateTimeEnd(Date queryUpdateTimeEnd) {
        this.queryUpdateTimeEnd = queryUpdateTimeEnd;
    }

    public LinkedHashMap<String, String> getSortParams() {
        return sortParams;
    }

    public void setSortParams(LinkedHashMap<String, String> sortParams) {
        this.sortParams = sortParams;
    }
}
