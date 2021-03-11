package com.aratek.framework.domain.core;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * @author shijinlong
 * @date 2018-06-08
 * @description 角色权限对象, 给角色设置权限用
 */
@ApiModel(value = "RoleRightVO", description = "角色权限对象,给角色设置权限用")
public class RoleRightVO implements Serializable {

    private static final long serialVersionUID = -7854978807469291309L;

    @ApiModelProperty(value = "角色ID", example = "6ce819cee70c11e78fdee03f494c2e88")
    private String roleID;

    @ApiModelProperty(value = "角色权限list")
    private List<RoleRight> roleRightList;

    public String getRoleID() {
        return roleID;
    }

    public void setRoleID(String roleID) {
        this.roleID = roleID;
    }

    public List<RoleRight> getRoleRightList() {
        return roleRightList;
    }

    public void setRoleRightList(List<RoleRight> roleRightList) {
        this.roleRightList = roleRightList;
    }
}
