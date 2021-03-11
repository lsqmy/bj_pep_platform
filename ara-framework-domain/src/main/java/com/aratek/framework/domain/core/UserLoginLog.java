package com.aratek.framework.domain.core;

import com.aratek.framework.domain.base.QueryDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.ibatis.type.Alias;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

/**
 * @author shijinlong
 * @date 2018-05-08
 * @description 用户登录日志对象
 * @table T_BD_USERLOGINLOG
 */
@Alias("araFwUserLoginLog")
@Table(name = "t_bd_userloginlog")
@ApiModel(value = "UserLoginLog", description = "用户登录日志对象")
public class UserLoginLog extends QueryDomain {

    @Id
    @Column(name = "fID")
    @ApiModelProperty(value = "主键ID", example = "6ce819cee70c11e78fdee03f494c2e87")
    private String fID;

    @Column(name = "fUserID")
    @ApiModelProperty(value = "用户ID", example = "6ce819cee70c11e78fdee03f494c2e88")
    private String fUserID;

    @Column(name = "fLoginDate")
    @ApiModelProperty(value = "登录时间", example = "2018-04-23 17:22:11")
    private Date fLoginDate;

    @Column(name = "fLoginType")
    @ApiModelProperty(value = "登录类型:IN登入,OUT退出", example = "IN")
    private String fLoginType;

    @Column(name = "fEquipmentNo")
    @ApiModelProperty(value = "设备号/计算机名", example = "BD9000")
    private String fEquipmentNo;

    @Column(name = "fIP")
    @ApiModelProperty(value = "IP地址", example = "192.168.1.119")
    private String fIP;

    @Column(name = "fPlatForm")
    @ApiModelProperty(value = "平台/系统版本", example = "windows10")
    private String fPlatForm;

    @Transient
    @ApiModelProperty(value = "用户名", example = "admin")
    private String fUserName;

    public String getfID() {
        return fID;
    }

    public void setfID(String fID) {
        this.fID = fID;
    }

    public String getfUserID() {
        return fUserID;
    }

    public void setfUserID(String fUserID) {
        this.fUserID = fUserID;
    }

    public Date getfLoginDate() {
        return fLoginDate;
    }

    public void setfLoginDate(Date fLoginDate) {
        this.fLoginDate = fLoginDate;
    }

    public String getfLoginType() {
        return fLoginType;
    }

    public void setfLoginType(String fLoginType) {
        this.fLoginType = fLoginType;
    }

    public String getfEquipmentNo() {
        return fEquipmentNo;
    }

    public void setfEquipmentNo(String fEquipmentNo) {
        this.fEquipmentNo = fEquipmentNo;
    }

    public String getfIP() {
        return fIP;
    }

    public void setfIP(String fIP) {
        this.fIP = fIP;
    }

    public String getfPlatForm() {
        return fPlatForm;
    }

    public void setfPlatForm(String fPlatForm) {
        this.fPlatForm = fPlatForm;
    }

    public String getfUserName() {
        return fUserName;
    }

    public void setfUserName(String fUserName) {
        this.fUserName = fUserName;
    }

}
