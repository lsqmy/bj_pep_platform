package com.aratek.framework.domain.core;

import com.aratek.framework.domain.base.BaseDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.ibatis.type.Alias;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * @author shijinlong
 * @date 2018-05-09
 * @description 用户角色对象
 * @table T_BD_USERROLE
 */
@Alias("araFwUserRole")
@Table(name = "t_bd_userrole")
@ApiModel(value = "UserRole", description = "用户角色对象")
public class UserRole extends BaseDomain {

    @Column(name = "fUserID")
    @ApiModelProperty(value = "用户ID", example = "6ce819cee70c11e78fdee03f494c2e88")
    private String fUserID;

    @Column(name = "fRoleID")
    @ApiModelProperty(value = "角色ID", example = "6ce819cee70c11e78fdee03f494c2e88")
    private String fRoleID;

    @Column(name = "fIsInit")
    @ApiModelProperty(value = "是否初始化数据:0,否;1,是;", example = "0")
    private Integer fIsInit;

    public String getfUserID() {
        return fUserID;
    }

    public void setfUserID(String fUserID) {
        this.fUserID = fUserID;
    }

    public String getfRoleID() {
        return fRoleID;
    }

    public void setfRoleID(String fRoleID) {
        this.fRoleID = fRoleID;
    }

    public Integer getfIsInit() {
        return fIsInit;
    }

    public void setfIsInit(Integer fIsInit) {
        this.fIsInit = fIsInit;
    }
}
