package com.aratek.framework.domain.core;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * @author shijinlong
 * @date 2018-06-08
 * @description 菜单权限树
 */
@ApiModel(value = "MenuRightTree", description = "菜单权限树")
public class MenuRightTree implements Serializable {

    private static final long serialVersionUID = -4702932409363059092L;

    @ApiModelProperty(value = "主键ID", example = "6ce819cee70c11e78fdee03f494c2e88")
    private String fRightID;

    @ApiModelProperty(value = "父节点FID", example = "309c04201e3c4cfb9b1d27a7b2b39eb8")
    private String fParentID;

    @ApiModelProperty(value = "菜单ID", example = "6ce819cee70c11e78fdee03f494c2e88")
    private String fMainMenuItemID;

    @ApiModelProperty(value = "权限编码", example = "insert")
    private String fRightCode;

    @ApiModelProperty(value = "权限名称", example = "新增用户")
    private String fRightName;

    @ApiModelProperty(value = "备注", example = "备注")
    private String fDescription;

    @ApiModelProperty(value = "是否初始化数据:0,否;1,是;", example = "0")
    private Integer fIsInit;

    @ApiModelProperty(value = "是否有权限", notes = "0,无;1,有;", example = "0")
    private Integer hasRight = 0;

    @ApiModelProperty(value = "子节点")
    private List<MenuRightTree> children;

    public String getfRightID() {
        return fRightID;
    }

    public void setfRightID(String fRightID) {
        this.fRightID = fRightID;
    }

    public String getfParentID() {
        return fParentID;
    }

    public void setfParentID(String fParentID) {
        this.fParentID = fParentID;
    }

    public String getfMainMenuItemID() {
        return fMainMenuItemID;
    }

    public void setfMainMenuItemID(String fMainMenuItemID) {
        this.fMainMenuItemID = fMainMenuItemID;
    }

    public String getfRightCode() {
        return fRightCode;
    }

    public void setfRightCode(String fRightCode) {
        this.fRightCode = fRightCode;
    }

    public String getfRightName() {
        return fRightName;
    }

    public void setfRightName(String fRightName) {
        this.fRightName = fRightName;
    }

    public String getfDescription() {
        return fDescription;
    }

    public void setfDescription(String fDescription) {
        this.fDescription = fDescription;
    }

    public Integer getfIsInit() {
        return fIsInit;
    }

    public void setfIsInit(Integer fIsInit) {
        this.fIsInit = fIsInit;
    }

    public Integer getHasRight() {
        return hasRight;
    }

    public void setHasRight(Integer hasRight) {
        this.hasRight = hasRight;
    }

    public List<MenuRightTree> getChildren() {
        return children;
    }

    public void setChildren(List<MenuRightTree> children) {
        this.children = children;
    }
}
