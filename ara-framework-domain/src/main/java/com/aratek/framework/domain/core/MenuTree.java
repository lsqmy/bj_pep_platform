package com.aratek.framework.domain.core;

import com.aratek.framework.domain.base.tree.TreeEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author shijinlong
 * @date 2018-05-11
 * @description 菜单树
 */
@Alias("araFwMenuTree")
@ApiModel(value = "MenuTree", description = "菜单树")
public class MenuTree implements TreeEntity<MenuTree>, Serializable {

    private static final long serialVersionUID = 8648274299181560325L;

    @ApiModelProperty(value = "主键ID", example = "6ce819cee70c11e78fdee03f494c2e88")
    private String fID;

    @ApiModelProperty(value = "父节点FID", example = "309c04201e3c4cfb9b1d27a7b2b39eb8")
    private String fParentID;

    @ApiModelProperty(value = "创建人ID", example = "9df1dbbe28e642e3b75e0b1ba0d7b345")
    private String fCreatorID;

    @ApiModelProperty(value = "创建时间", example = "2018-04-23 17:22:11")
    private Date fCreateTime;

    @ApiModelProperty(value = "最后修改人ID", example = "9df1dbbe28e642e3b75e0b1ba0d7b345")
    private String fLastUpdateUserID;

    @ApiModelProperty(value = "最后修改时间", example = "2018-04-23 17:22:11")
    private Date fLastUpdateTime;

    @ApiModelProperty(value = "名称", example = "admin")
    private String fName;

    @ApiModelProperty(value = "编号", example = "1001")
    private String fNumber;

    @ApiModelProperty(value = "长编号", example = "100001")
    private String fLongNumber;

    @ApiModelProperty(value = "备注", example = "备注")
    private String fDescription;

    @ApiModelProperty(value = "是否根节点:0,否;1,是;", example = "0")
    private Integer fIsRoot;

    @ApiModelProperty(value = "层次", example = "2")
    private Integer fLevel;

    @ApiModelProperty(value = "菜单连接")
    private String fLink;

    @ApiModelProperty(value = "菜单图标")
    private String fIcon;

    @ApiModelProperty(value = "菜单序号", example = "1")
    private Integer fDisplayorder;

    @ApiModelProperty(value = "菜单ID", example = "6ce819cee70c11e78fdee03f494c2e88")
    private String fMainMenuItemID;

    @ApiModelProperty(value = "权限ID", example = "6ce819cee70c11e78fdee03f494c2e88")
    private String fRightID;

    @ApiModelProperty(value = "权限Code", example = "select")
    private String fRightCode;

    @ApiModelProperty(value = "菜单权限编码", example = "1001:select")
    private String menuRightCode;

    @ApiModelProperty(value = "节点类型:1,菜单;2,权限;", example = "1")
    private String nodeType;

    @ApiModelProperty(value = "是否有权限", notes = "0,无;1,有;", example = "0")
    private Integer hasRight = 0;

    @ApiModelProperty(value = "是否初始化数据:0,否;1,是;", example = "0")
    private Integer fIsInit;

    @ApiModelProperty(value = "子节点")
    private List<MenuTree> childList;


    @Override
    public String getfID() {
        return fID;
    }

    public void setfID(String fID) {
        this.fID = fID;
    }

    @Override
    public String getfParentID() {
        return fParentID;
    }

    public void setfParentID(String fParentID) {
        this.fParentID = fParentID;
    }

    public String getfCreatorID() {
        return fCreatorID;
    }

    public void setfCreatorID(String fCreatorID) {
        this.fCreatorID = fCreatorID;
    }

    public Date getfCreateTime() {
        return fCreateTime;
    }

    public void setfCreateTime(Date fCreateTime) {
        this.fCreateTime = fCreateTime;
    }

    public String getfLastUpdateUserID() {
        return fLastUpdateUserID;
    }

    public void setfLastUpdateUserID(String fLastUpdateUserID) {
        this.fLastUpdateUserID = fLastUpdateUserID;
    }

    public Date getfLastUpdateTime() {
        return fLastUpdateTime;
    }

    public void setfLastUpdateTime(Date fLastUpdateTime) {
        this.fLastUpdateTime = fLastUpdateTime;
    }

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

    public String getfMainMenuItemID() {
        return fMainMenuItemID;
    }

    public void setfMainMenuItemID(String fMainMenuItemID) {
        this.fMainMenuItemID = fMainMenuItemID;
    }

    public String getfRightID() {
        return fRightID;
    }

    public void setfRightID(String fRightID) {
        this.fRightID = fRightID;
    }

    public String getfRightCode() {
        return fRightCode;
    }

    public void setfRightCode(String fRightCode) {
        this.fRightCode = fRightCode;
    }

    public String getMenuRightCode() {
        return menuRightCode;
    }

    public void setMenuRightCode(String menuRightCode) {
        this.menuRightCode = menuRightCode;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public Integer getHasRight() {
        return hasRight;
    }

    public void setHasRight(Integer hasRight) {
        this.hasRight = hasRight;
    }

    public Integer getfIsInit() {
        return fIsInit;
    }

    public void setfIsInit(Integer fIsInit) {
        this.fIsInit = fIsInit;
    }

    public List<MenuTree> getChildList() {
        return childList;
    }

    @Override
    public void setChildList(List<MenuTree> childList) {
        this.childList = childList;
    }
}
