package com.aratek.framework.domain.core;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * @author shijinlong
 * @date 2018-06-08
 * @description 用户权限对象, 给用户设置权限用
 */
@ApiModel(value = "UserRightVO", description = "用户权限对象,给用户设置权限用")
public class UserRightVO implements Serializable {

    private static final long serialVersionUID = -1476985612357121238L;

    @ApiModelProperty(value = "用户ID", example = "6ce819cee70c11e78fdee03f494c2e88")
    private String userID;

    @ApiModelProperty(value = "用户权限list")
    private List<UserRight> userRightList;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public List<UserRight> getUserRightList() {
        return userRightList;
    }

    public void setUserRightList(List<UserRight> userRightList) {
        this.userRightList = userRightList;
    }
}
