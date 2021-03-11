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
 * @date 2018-05-15
 * @description 区县
 * @table T_BD_REGION
 */
@Alias("araFwRegion")
@Table(name = "t_bd_region")
@ApiModel(value = "Region", description = "区县")
public class Region extends BaseDomain {

    @Column(name = "fName")
    @ApiModelProperty(value = "名称", example = "china")
    private String fName;

    @Column(name = "fSimpleName")
    @ApiModelProperty(value = "简称", example = "CN")
    private String fSimpleName;

    @Column(name = "fNumber")
    @ApiModelProperty(value = "编号", example = "1001")
    private String fNumber;

    @Column(name = "fDescription")
    @ApiModelProperty(value = "备注", example = "备注")
    private String fDescription;

    @Column(name = "fStatus")
    @ApiModelProperty(value = "状态", example = "2", notes = "1保存,2启用,3禁用")
    private Integer fStatus;

    @Column(name = "fCityID")
    @ApiModelProperty(value = "城市ID", example = "6ce819cee70c11e78fdee03f494c2e88")
    private String fCityID;

    @Transient
    @ApiModelProperty(value = "城市名称", example = "深圳")
    private String fCityName;

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getfSimpleName() {
        return fSimpleName;
    }

    public void setfSimpleName(String fSimpleName) {
        this.fSimpleName = fSimpleName;
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

    public String getfCityID() {
        return fCityID;
    }

    public void setfCityID(String fCityID) {
        this.fCityID = fCityID;
    }

    public String getfCityName() {
        return fCityName;
    }

    public void setfCityName(String fCityName) {
        this.fCityName = fCityName;
    }
}
