package com.aratek.framework.domain.base;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

/**
 * @author shijinlong
 * @date 2018-05-09
 * @description 当前操作人对象，其他对象不继承BaseDomain和QueryDomain情况下需要继承此对象
 * 注意：非数据库表字段需加上@Transient注解，不然通用mapper会报错
 */
@ApiModel(value = "CurrentUser", description = "当前操作人对象")
public class CurrentUser implements Serializable {

    private static final long serialVersionUID = 8426017207578876946L;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Transient
    @ApiModelProperty(value = "当前操作人ID", example = "6ce819cee70c12e78fdee03f494c2e89")
    private String currentUserID;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Transient
    @ApiModelProperty(value = "当前操作时间", example = "2018-05-09 11:12:13")
    private Date currentUserOperationTime;

    public String getCurrentUserID() {
        return currentUserID;
    }

    public void setCurrentUserID(String currentUserID) {
        this.currentUserID = currentUserID;
    }

    public Date getCurrentUserOperationTime() {
        return currentUserOperationTime;
    }

    public void setCurrentUserOperationTime(Date currentUserOperationTime) {
        this.currentUserOperationTime = currentUserOperationTime;
    }
}
