package com.aratek.framework.domain.core;

import com.aratek.framework.domain.base.BaseDomain;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.ibatis.type.Alias;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

/**
 * @author shijinlong
 * @date 2018-04-28
 * @description 用户对象
 * @table T_BD_USER
 */
@Alias("araFwUser")
@Table(name = "t_bd_user")
@ApiModel(value = "User", description = "用户对象")
public class User extends BaseDomain {

    @Column(name = "fName")
    @ApiModelProperty(value = "用户名", example = "zhangsan")
    private String fName;

    @Column(name = "fPassWord")
    @ApiModelProperty(value = "密码", example = "123456")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String fPassWord;

    @Column(name = "fNumber")
    @ApiModelProperty(value = "编号", example = "1001")
    private String fNumber;

    @Column(name = "fEmail")
    @ApiModelProperty(value = "邮箱", example = "zhangsan@qq.com")
    private String fEmail;

    @Column(name = "fMobile")
    @ApiModelProperty(value = "手机", example = "18912341234")
    private String fMobile;

    @Column(name = "fQQ")
    @ApiModelProperty(value = "QQ", example = "123456")
    private String fQQ;

    @Column(name = "fWeiXin")
    @ApiModelProperty(value = "微信", example = "weixin")
    private String fWeiXin;

    @Column(name = "fStatus")
    @ApiModelProperty(value = "状态", example = "2", notes = "1保存,2启用,3禁用")
    private Integer fStatus;

    @Column(name = "fLoginTime")
    @ApiModelProperty(value = "最近一次登录时间", example = "2018-04-23 17:22:11")
    private Date fLoginTime;

    @Column(name = "fDescription")
    @ApiModelProperty(value = "备注", example = "desc")
    private String fDescription;

    @Column(name = "fIsInit")
    @ApiModelProperty(value = "是否初始化数据:0,否;1,是;", example = "0")
    private Integer fIsInit;

    @Column(name = "fErrorNum")
    @ApiModelProperty(value = "输错密码次数", example = "0")
    private Integer fErrorNum;

    @Column(name = "fLastErrorTime")
    @ApiModelProperty(value = "最后一次输错密码时间", example = "2018-04-23 17:22:11")
    private Date fLastErrorTime;

    @Column(name = "fLastChangePswTime")
    @ApiModelProperty(value = "最后一次修改密码时间", example = "2018-04-23 17:22:11")
    private Date fLastChangePswTime;

    @Column(name = "fPeriodOfValidity")
    @ApiModelProperty(value = "账号有效期截至时间", example = "2018-04-23 17:22:11")
    private Date fPeriodOfValidity;

    @Transient
    @ApiModelProperty(value = "旧密码", example = "123456")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String oldPassWord;

    public Integer getfErrorNum() {
        return fErrorNum;
    }

    public void setfErrorNum(Integer fErrorNum) {
        this.fErrorNum = fErrorNum;
    }

    public Date getfLastErrorTime() {
        return fLastErrorTime;
    }

    public void setfLastErrorTime(Date fLastErrorTime) {
        this.fLastErrorTime = fLastErrorTime;
    }

    public Date getfLastChangePswTime() {
        return fLastChangePswTime;
    }

    public void setfLastChangePswTime(Date fLastChangePswTime) {
        this.fLastChangePswTime = fLastChangePswTime;
    }

    public Date getfPeriodOfValidity() {
        return fPeriodOfValidity;
    }

    public void setfPeriodOfValidity(Date fPeriodOfValidity) {
        this.fPeriodOfValidity = fPeriodOfValidity;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getfPassWord() {
        return fPassWord;
    }

    public void setfPassWord(String fPassWord) {
        this.fPassWord = fPassWord;
    }

    public String getfNumber() {
        return fNumber;
    }

    public void setfNumber(String fNumber) {
        this.fNumber = fNumber;
    }

    public String getfEmail() {
        return fEmail;
    }

    public void setfEmail(String fEmail) {
        this.fEmail = fEmail;
    }

    public String getfMobile() {
        return fMobile;
    }

    public void setfMobile(String fMobile) {
        this.fMobile = fMobile;
    }

    public String getfQQ() {
        return fQQ;
    }

    public void setfQQ(String fQQ) {
        this.fQQ = fQQ;
    }

    public String getfWeiXin() {
        return fWeiXin;
    }

    public void setfWeiXin(String fWeiXin) {
        this.fWeiXin = fWeiXin;
    }

    public Integer getfStatus() {
        return fStatus;
    }

    public void setfStatus(Integer fStatus) {
        this.fStatus = fStatus;
    }

    public Date getfLoginTime() {
        return fLoginTime;
    }

    public void setfLoginTime(Date fLoginTime) {
        this.fLoginTime = fLoginTime;
    }

    public String getfDescription() {
        return fDescription;
    }

    public void setfDescription(String fDescription) {
        this.fDescription = fDescription;
    }

    public Integer getfIsInit() {
        return fIsInit;
    }

    public void setfIsInit(Integer fIsInit) {
        this.fIsInit = fIsInit;
    }

    public String getOldPassWord() {
        return oldPassWord;
    }

    public void setOldPassWord(String oldPassWord) {
        this.oldPassWord = oldPassWord;
    }
}
