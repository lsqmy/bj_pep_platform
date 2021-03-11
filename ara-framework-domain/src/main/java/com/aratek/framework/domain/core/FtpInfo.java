package com.aratek.framework.domain.core;

import com.aratek.framework.domain.base.BaseDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.ibatis.type.Alias;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * @author shijinlong
 * @date 2018-05-22
 * @description ftp信息表
 * @table T_BD_FTPINFO
 */
@Alias("araFwFtpInfo")
@Table(name = "t_bd_ftpinfo")
@ApiModel(value = "FtpInfo", description = "ftp信息表")
public class FtpInfo extends BaseDomain {

    @Column(name = "fHost")
    @ApiModelProperty(value = "主机地址", example = "192.168.1.1")
    private String fHost;

    @Column(name = "fPort")
    @ApiModelProperty(value = "端口号", example = "21")
    private Integer fPort;

    @Column(name = "fUserName")
    @ApiModelProperty(value = "ftp用户名", example = "ftpuser")
    private String fUserName;

    @Column(name = "fPassWord")
    @ApiModelProperty(value = "ftp用户密码", example = "ftp123456")
    private String fPassWord;

    @Column(name = "fStatus")
    @ApiModelProperty(value = "状态", example = "2", notes = "1,保存;2,启用;3,禁用;")
    private Integer fStatus;

    @Column(name = "fDescription")
    @ApiModelProperty(value = "备注", example = "备注")
    private String fDescription;


    public String getfHost() {
        return fHost;
    }

    public void setfHost(String fHost) {
        this.fHost = fHost;
    }

    public Integer getfPort() {
        return fPort;
    }

    public void setfPort(Integer fPort) {
        this.fPort = fPort;
    }

    public String getfUserName() {
        return fUserName;
    }

    public void setfUserName(String fUserName) {
        this.fUserName = fUserName;
    }

    public String getfPassWord() {
        return fPassWord;
    }

    public void setfPassWord(String fPassWord) {
        this.fPassWord = fPassWord;
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
