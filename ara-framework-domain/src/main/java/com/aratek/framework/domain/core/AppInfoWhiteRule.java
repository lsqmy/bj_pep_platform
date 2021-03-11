package com.aratek.framework.domain.core;

import com.aratek.framework.domain.base.BaseDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.ibatis.type.Alias;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * @author shijinlong
 * @date 2018-06-22
 * @description APP升级白名单
 * @table T_BD_APPINFOWHITERULE
 */
@Alias("araFwAppInfoWhiteRule")
@Table(name = "t_bd_appinfowhiterule")
@ApiModel(value = "AppInfoWhiteRule", description = "APP升级白名单")
public class AppInfoWhiteRule extends BaseDomain {

    @Column(name = "fAppID")
    @ApiModelProperty(value = "AppID", example = "easypass")
    private String fAppID;

    @Column(name = "fWhiteType")
    @ApiModelProperty(value = "白名单类型:1,IPv4;2,SN;", example = "1")
    private String fWhiteType;

    @Column(name = "fCheckType")
    @ApiModelProperty(value = "校验类型:1,精准匹配;2,通配符匹配;3,区间范围;", example = "3")
    private String fCheckType;

    @Column(name = "fValueOne")
    @ApiModelProperty(value = "参数值1", example = "192.168.1.110")
    private String fValueOne;

    @Column(name = "fValueTwo")
    @ApiModelProperty(value = "参数值2", example = "192.168.1.119")
    private String fValueTwo;

    @Column(name = "fStatus")
    @ApiModelProperty(value = "状态", example = "2", notes = "1保存,2启用,3禁用")
    private Integer fStatus;

    @Column(name = "fDescription")
    @ApiModelProperty(value = "备注", example = "备注")
    private String fDescription;


    public String getfAppID() {
        return fAppID;
    }

    public void setfAppID(String fAppID) {
        this.fAppID = fAppID;
    }

    public String getfWhiteType() {
        return fWhiteType;
    }

    public void setfWhiteType(String fWhiteType) {
        this.fWhiteType = fWhiteType;
    }

    public String getfCheckType() {
        return fCheckType;
    }

    public void setfCheckType(String fCheckType) {
        this.fCheckType = fCheckType;
    }

    public String getfValueOne() {
        return fValueOne;
    }

    public void setfValueOne(String fValueOne) {
        this.fValueOne = fValueOne;
    }

    public String getfValueTwo() {
        return fValueTwo;
    }

    public void setfValueTwo(String fValueTwo) {
        this.fValueTwo = fValueTwo;
    }

    public Integer getfStatus() {
        return fStatus;
    }

    public void setfStatus(Integer fStatus) {
        this.fStatus = fStatus;
    }

    public String getfDescription() {
        return fDescription;
    }

    public void setfDescription(String fDescription) {
        this.fDescription = fDescription;
    }
}
