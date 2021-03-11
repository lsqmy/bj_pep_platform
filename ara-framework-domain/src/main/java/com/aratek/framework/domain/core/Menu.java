package com.aratek.framework.domain.core;

import com.aratek.framework.domain.base.BaseDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.ibatis.type.Alias;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;

/**
 * @author shijinlong
 * @date 2018-05-08
 * @description 菜单对象
 * @table T_BD_MAINMENUITEM
 */
@Alias("araFwMenu")
@Table(name = "t_bd_mainmenuitem")
@ApiModel(value = "Menu", description = "菜单对象")
public class Menu extends BaseDomain {

    @Column(name = "fName")
    @ApiModelProperty(value = "名称", example = "admin")
    private String fName;

    @Column(name = "fNumber")
    @ApiModelProperty(value = "编号", example = "1001")
    private String fNumber;

    @Column(name = "fLongNumber")
    @ApiModelProperty(value = "长编号", example = "100001")
    private String fLongNumber;

    @Column(name = "fParentID")
    @ApiModelProperty(value = "父节点FID", example = "309c04201e3c4cfb9b1d27a7b2b39eb8")
    private String fParentID;

    @Column(name = "fDescription")
    @ApiModelProperty(value = "备注", example = "备注")
    private String fDescription;

    @Column(name = "fIsRoot")
    @ApiModelProperty(value = "是否根节点:0,否;1,是;", example = "0")
    private Integer fIsRoot;

    @Column(name = "fLevel")
    @ApiModelProperty(value = "层次", example = "2")
    private Integer fLevel;

    @Column(name = "fLink")
    @ApiModelProperty(value = "菜单连接")
    private String fLink;

    @Column(name = "fIcon")
    @ApiModelProperty(value = "菜单图标")
    private String fIcon;

    @Column(name = "fDisplayorder")
    @ApiModelProperty(value = "菜单序号", example = "1")
    private Integer fDisplayorder;

    @Column(name = "fIsInit")
    @ApiModelProperty(value = "是否初始化数据:0,否;1,是;", example = "0")
    private Integer fIsInit;

    @Transient
    @ApiModelProperty(value = "父节点name", example = "系统管理")
    private String fParentName;

    @Transient
    @ApiModelProperty(value = "菜单对应权限")
    private List<MenuRight> menuRights;

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getfNumber() {
        return fNumber;
    }

    public void setfNumber(String fNumber) {
        this.fNumber = fNumber;
    }

    public String getfLongNumber() {
        return fLongNumber;
    }

    public void setfLongNumber(String fLongNumber) {
        this.fLongNumber = fLongNumber;
    }

    public String getfParentID() {
        return fParentID;
    }

    public void setfParentID(String fParentID) {
        this.fParentID = fParentID;
    }

    public String getfDescription() {
        return fDescription;
    }

    public void setfDescription(String fDescription) {
        this.fDescription = fDescription;
    }

    public Integer getfIsRoot() {
        return fIsRoot;
    }

    public void setfIsRoot(Integer fIsRoot) {
        this.fIsRoot = fIsRoot;
    }

    public Integer getfLevel() {
        return fLevel;
    }

    public void setfLevel(Integer fLevel) {
        this.fLevel = fLevel;
    }

    public String getfLink() {
        return fLink;
    }

    public void setfLink(String fLink) {
        this.fLink = fLink;
    }

    public String getfIcon() {
        return fIcon;
    }

    public void setfIcon(String fIcon) {
        this.fIcon = fIcon;
    }

    public Integer getfDisplayorder() {
        return fDisplayorder;
    }

    public void setfDisplayorder(Integer fDisplayorder) {
        this.fDisplayorder = fDisplayorder;
    }

    public Integer getfIsInit() {
        return fIsInit;
    }

    public void setfIsInit(Integer fIsInit) {
        this.fIsInit = fIsInit;
    }

    public String getfParentName() {
        return fParentName;
    }

    public void setfParentName(String fParentName) {
        this.fParentName = fParentName;
    }

    public List<MenuRight> getMenuRights() {
        return menuRights;
    }

    public void setMenuRights(List<MenuRight> menuRights) {
        this.menuRights = menuRights;
    }
}
