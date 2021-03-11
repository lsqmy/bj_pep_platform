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
 * @description 用户操作日志对象
 * @table T_BD_USERLOG
 */
@Alias("araFwUserLog")
@Table(name = "t_bd_userlog")
@ApiModel(value = "UserLog", description = "用户操作日志对象")
public class UserLog extends QueryDomain {

    @Id
    @Column(name = "fID")
    @ApiModelProperty(value = "主键ID", example = "6ce819cee70c11e78fdee03f494c2e87")
    private String fID;

    @Column(name = "fUserID")
    @ApiModelProperty(value = "用户ID", example = "6ce819cee70c11e78fdee03f494c2e88")
    private String fUserID;

    @Column(name = "fActType")
    @ApiModelProperty(value = "操作类型", example = "123456")
    private String fActType;

    @Column(name = "fUri")
    @ApiModelProperty(value = "URI", example = "/user/selectUserList")
    private String fUri;

    @Column(name = "fModel")
    @ApiModelProperty(value = "模块名称", example = "1001")
    private String fModel;

    @Column(name = "fCreateTime")
    @ApiModelProperty(value = "创建时间", example = "2018-04-23 17:22:11")
    private Date fCreateTime;

    @Column(name = "fEquipmentNo")
    @ApiModelProperty(value = "设备号/计算机名", example = "BD9000")
    private String fEquipmentNo;

    @Column(name = "fIP")
    @ApiModelProperty(value = "IP地址", example = "192.168.1.119")
    private String fIP;

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

    public String getfActType() {
        return fActType;
    }

    public void setfActType(String fActType) {
        this.fActType = fActType;
    }

    public String getfUri() {
        return fUri;
    }

    public void setfUri(String fUri) {
        this.fUri = fUri;
    }

    public String getfModel() {
        return fModel;
    }

    public void setfModel(String fModel) {
        this.fModel = fModel;
    }

    public Date getfCreateTime() {
        return fCreateTime;
    }

    public void setfCreateTime(Date fCreateTime) {
        this.fCreateTime = fCreateTime;
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

    public String getfUserName() {
        return fUserName;
    }

    public void setfUserName(String fUserName) {
        this.fUserName = fUserName;
    }

}
