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
 * @date 2018-05-17
 * @description APP升级信息
 * @table T_BD_APPINFO
 */
@Alias("araFwAppInfo")
@Table(name = "t_bd_appinfo")
@ApiModel(value = "AppInfo", description = "APP升级信息")
public class AppInfo extends BaseDomain {

    @Column(name = "fAppID")
    @ApiModelProperty(value = "AppID", example = "easypass")
    private String fAppID;

    @Column(name = "fAppName")
    @ApiModelProperty(value = "App名称", example = "easypass安卓客户端")
    private String fAppName;

    @Column(name = "fPassWord")
    @ApiModelProperty(value = "密码", example = "pwd123456")
    private String fPassWord;

    @Column(name = "fAppVersion")
    @ApiModelProperty(value = "版本", example = "1.0.1")
    private String fAppVersion;

    @Column(name = "fForce")
    @ApiModelProperty(value = "强制升级标识:0,不强制;1,强制升级;", example = "1")
    private Integer fForce;

    @Column(name = "fDescription")
    @ApiModelProperty(value = "版本描述", example = "版本描述")
    private String fDescription;

    @Transient
    @ApiModelProperty(value = "App升级信息明细list")
    private List<AppInfoEntry> appInfoEntryList;


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

    public String getfPassWord() {
        return fPassWord;
    }

    public void setfPassWord(String fPassWord) {
        this.fPassWord = fPassWord;
    }

    public String getfAppVersion() {
        return fAppVersion;
    }

    public void setfAppVersion(String fAppVersion) {
        this.fAppVersion = fAppVersion;
    }

    public Integer getfForce() {
        return fForce;
    }

    public void setfForce(Integer fForce) {
        this.fForce = fForce;
    }

    public String getfDescription() {
        return fDescription;
    }

    public void setfDescription(String fDescription) {
        this.fDescription = fDescription;
    }

    public List<AppInfoEntry> getAppInfoEntryList() {
        return appInfoEntryList;
    }

    public void setAppInfoEntryList(List<AppInfoEntry> appInfoEntryList) {
        this.appInfoEntryList = appInfoEntryList;
    }
}
