package com.aratek.framework.domain.core;

import com.aratek.framework.domain.base.BaseDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.ibatis.type.Alias;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * @author shijinlong
 * @date 2018-05-01
 * @description 角色对象
 * @table T_BD_ROLE
 */
@Alias("araFwRole")
@Table(name = "t_bd_role")
@ApiModel(value = "Role", description = "角色对象")
public class Role extends BaseDomain {

    @Column(name = "fName")
    @ApiModelProperty(value = "名称", example = "user")
    private String fName;

    @Column(name = "fNumber")
    @ApiModelProperty(value = "编号", example = "1001")
    private String fNumber;

    @Column(name = "fDescription")
    @ApiModelProperty(value = "备注", example = "普通用户")
    private String fDescription;

    @Column(name = "fStatus")
    @ApiModelProperty(value = "状态", example = "2", notes = "1保存,2启用,3禁用")
    private Integer fStatus;

    @Column(name = "fIsInit")
    @ApiModelProperty(value = "是否初始化数据:0,否;1,是;", example = "0")
    private Integer fIsInit;

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

    public String getfDescription() {
        return fDescription;
    }

    public void setfDescription(String fDescription) {
        this.fDescription = fDescription;
    }

    public Integer getfStatus() {
        return fStatus;
    }

    public void setfStatus(Integer fStatus) {
        this.fStatus = fStatus;
    }

    public Integer getfIsInit() {
        return fIsInit;
    }

    public void setfIsInit(Integer fIsInit) {
        this.fIsInit = fIsInit;
    }
}
