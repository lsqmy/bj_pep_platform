package com.aratek.framework.domain.core;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * @author shijinlong
 * @date 2018-06-21
 * @description 用户角色对象, 给用户增加角色用
 */
@ApiModel(value = "UserRoleVO", description = "用户角色对象,给用户增加角色用")
public class UserRoleVO implements Serializable {

    private static final long serialVersionUID = 8981040564054589729L;

    @ApiModelProperty(value = "用户ID", example = "6ce819cee70c11e78fdee03f494c2e88")
    private String userID;

    @ApiModelProperty(value = "角色ID数组")
    private List<String> roleIDList;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public List<String> getRoleIDList() {
        return roleIDList;
    }

    public void setRoleIDList(List<String> roleIDList) {
        this.roleIDList = roleIDList;
    }
}
