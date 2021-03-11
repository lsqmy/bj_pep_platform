package com.aratek.framework.domain.core;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @author shijinlong
 * @date 2018-06-21
 * @description 用户已有权限查询VO
 */
@ApiModel(value = "RightVO", description = "用户已有权限查询VO")
public class RightVO implements Serializable {

    private static final long serialVersionUID = -231155732935632664L;

    @ApiModelProperty(value = "角色名", example = "管理员")
    private String roleName;

    @ApiModelProperty(value = "用户名", example = "admin")
    private String userName;

    @ApiModelProperty(value = "菜单编号", example = "1001")
    private String menuNumber;

    @ApiModelProperty(value = "权限码", example = "select")
    private String rightCode;

    @ApiModelProperty(value = "权限来源", example = "admin")
    private String rightSource;

    @ApiModelProperty(value = "菜单名称", example = "用户管理")
    private String menuName;

    @ApiModelProperty(value = "权限名称", example = "查询")
    private String rightName;

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMenuNumber() {
        return menuNumber;
    }

    public void setMenuNumber(String menuNumber) {
        this.menuNumber = menuNumber;
    }

    public String getRightCode() {
        return rightCode;
    }

    public void setRightCode(String rightCode) {
        this.rightCode = rightCode;
    }

    public String getRightSource() {
        return rightSource;
    }

    public void setRightSource(String rightSource) {
        this.rightSource = rightSource;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getRightName() {
        return rightName;
    }

    public void setRightName(String rightName) {
        this.rightName = rightName;
    }
}
