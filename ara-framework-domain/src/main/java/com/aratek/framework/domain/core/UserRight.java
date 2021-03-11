package com.aratek.framework.domain.core;

import com.aratek.framework.domain.base.BaseDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.ibatis.type.Alias;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * @author shijinlong
 * @date 2018-05-10
 * @description 用户权限对象
 * @table T_BD_USERRIGHTS
 */
@Alias("araFwUserRight")
@Table(name = "t_bd_userrights")
@ApiModel(value = "UserRight", description = "用户权限对象")
public class UserRight extends BaseDomain {

    @Column(name = "fRightID")
    @ApiModelProperty(value = "菜单权限ID", example = "6ce819cee70c11e78fdee03f494c2e88")
    private String fRightID;

    @Column(name = "fUserID")
    @ApiModelProperty(value = "用户ID", example = "6ce819cee70c11e78fdee03f494c2e88")
    private String fUserID;

    @Column(name = "fMainMenuItemID")
    @ApiModelProperty(value = "菜单ID", example = "6ce819cee70c11e78fdee03f494c2e88")
    private String fMainMenuItemID;

    @Column(name = "fIsInit")
    @ApiModelProperty(value = "是否初始化数据:0,否;1,是;", example = "0")
    private Integer fIsInit;

    public String getfRightID() {
        return fRightID;
    }

    public void setfRightID(String fRightID) {
        this.fRightID = fRightID;
    }

    public String getfUserID() {
        return fUserID;
    }

    public void setfUserID(String fUserID) {
        this.fUserID = fUserID;
    }

    public String getfMainMenuItemID() {
        return fMainMenuItemID;
    }

    public void setfMainMenuItemID(String fMainMenuItemID) {
        this.fMainMenuItemID = fMainMenuItemID;
    }

    public Integer getfIsInit() {
        return fIsInit;
    }

    public void setfIsInit(Integer fIsInit) {
        this.fIsInit = fIsInit;
    }
}
