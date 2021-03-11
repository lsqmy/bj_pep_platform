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
 * @description 城市
 * @table T_BD_CITY
 */
@Alias("araFwCity")
@Table(name = "t_bd_city")
@ApiModel(value = "City", description = "城市")
public class City extends BaseDomain {

    @Column(name = "fName")
    @ApiModelProperty(value = "名称", example = "深圳")
    private String fName;

    @Column(name = "fSimpleName")
    @ApiModelProperty(value = "简称", example = "SZ")
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

    @Column(name = "fProvinceID")
    @ApiModelProperty(value = "省份ID", example = "6ce819cee70c11e78fdee03f494c2e88")
    private String fProvinceID;

    @Transient
    @ApiModelProperty(value = "省份名称", example = "广东")
    private String fProvinceName;

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

    public String getfProvinceID() {
        return fProvinceID;
    }

    public void setfProvinceID(String fProvinceID) {
        this.fProvinceID = fProvinceID;
    }

    public String getfProvinceName() {
        return fProvinceName;
    }

    public void setfProvinceName(String fProvinceName) {
        this.fProvinceName = fProvinceName;
    }
}
