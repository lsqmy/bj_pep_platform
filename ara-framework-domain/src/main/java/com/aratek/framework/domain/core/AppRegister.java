package com.aratek.framework.domain.core;

import com.aratek.framework.domain.base.BaseDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.ibatis.type.Alias;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * @author shijinlong
 * @date 2018-05-23
 * @description APP注册信息
 * @table T_BD_APPREGISTER
 */
@Alias("araFwAppRegister")
@Table(name = "t_bd_appregister")
@ApiModel(value = "AppRegister", description = "APP注册信息")
public class AppRegister extends BaseDomain {

    @Column(name = "fAppID")
    @ApiModelProperty(value = "AppID", example = "easypass")
    private String fAppID;

    @Column(name = "fAppName")
    @ApiModelProperty(value = "App名称", example = "easypass安卓客户端")
    private String fAppName;

    @Column(name = "fSecretKey")
    @ApiModelProperty(value = "APP密钥", example = "AHGFGSDKGWKHSGH")
    private String fSecretKey;

    @Column(name = "fStatus")
    @ApiModelProperty(value = "状态", example = "2", notes = "1保存,2启用,3禁用")
    private Integer fStatus;

    @Column(name = "fDescription")
    @ApiModelProperty(value = "版本描述", example = "版本描述")
    private String fDescription;

    public String getfAppID() {
        return fAppID;
    }

    public void setfAppID(String fAppID) {
        this.fAppID = fAppID;
    }

    public String getfAppName() {
        return fAppName;
    }

    public void setfAppName(String fAppName) {
        this.fAppName = fAppName;
    }

    public String getfSecretKey() {
        return fSecretKey;
    }

    public void setfSecretKey(String fSecretKey) {
        this.fSecretKey = fSecretKey;
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
