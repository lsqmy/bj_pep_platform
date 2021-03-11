package com.aratek.framework.domain.core;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * @author shijinlong
 * @date 2018-06-08
 * @description 角色用户对象, 给角色增加用户用
 */
@ApiModel(value = "RoleUserVO", description = "角色用户对象,给角色增加用户用")
public class RoleUserVO implements Serializable {

    private static final long serialVersionUID = 2099469829223918929L;

    @ApiModelProperty(value = "角色ID", example = "6ce819cee70c11e78fdee03f494c2e88")
    private String roleID;

    @ApiModelProperty(value = "用户ID数组")
    private List<String> userIDList;

    public String getRoleID() {
        return roleID;
    }

    public void setRoleID(String roleID) {
        this.roleID = roleID;
    }

    public List<String> getUserIDList() {
        return userIDList;
    }

    public void setUserIDList(List<String> userIDList) {
        this.userIDList = userIDList;
    }
}
