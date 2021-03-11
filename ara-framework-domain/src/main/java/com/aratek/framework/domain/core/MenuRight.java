package com.aratek.framework.domain.core;

import com.aratek.framework.domain.base.BaseDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.ibatis.type.Alias;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @author shijinlong
 * @date 2018-05-10
 * @description 菜单权限对象
 * @table T_BD_MENURIGHTS
 */
@Alias("araFwMenuRight")
@Table(name = "t_bd_menurights")
@ApiModel(value = "MenuRight", description = "菜单权限对象")
public class MenuRight extends BaseDomain {

    @Column(name = "fMainMenuItemID")
    @ApiModelProperty(value = "菜单ID", example = "6ce819cee70c11e78fdee03f494c2e88")
    private String fMainMenuItemID;

    @Column(name = "fRightCode")
    @ApiModelProperty(value = "权限编码", example = "insert")
    private String fRightCode;

    @Column(name = "fRightName")
    @ApiModelProperty(value = "权限名称", example = "新增用户")
    private String fRightName;

    @Column(name = "fDescription")
    @ApiModelProperty(value = "备注", example = "备注")
    private String fDescription;

    @Column(name = "fIsInit")
    @ApiModelProperty(value = "是否初始化数据:0,否;1,是;", example = "0")
    private Integer fIsInit;

    @Transient
    @ApiModelProperty(value = "菜单名称", example = "用户管理")
    private String fMainMenuItemName;

    @Transient
    @ApiModelProperty(value = "菜单编号", example = "1002")
    private String fMainMenuItemNumber;

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

    public String getfMainMenuItemName() {
        return fMainMenuItemName;
    }

    public void setfMainMenuItemName(String fMainMenuItemName) {
        this.fMainMenuItemName = fMainMenuItemName;
    }

    public String getfMainMenuItemNumber() {
        return fMainMenuItemNumber;
    }

    public void setfMainMenuItemNumber(String fMainMenuItemNumber) {
        this.fMainMenuItemNumber = fMainMenuItemNumber;
    }
}
