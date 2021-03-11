package com.aratek.framework.domain.core;

import com.aratek.framework.domain.base.AraPair;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.ibatis.type.Alias;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.File;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author shijinlong
 * @date 2018-05-11
 * @description 邮件对象
 * @table T_BD_MAILSEND
 */
@Alias("araFwMail")
@Table(name = "t_bd_mailsend")
@ApiModel(value = "Email", description = "邮件对象")
public class Email implements Serializable {

    private static final long serialVersionUID = -5825886295930175439L;

    @Id
    @Column(name = "fID")
    @ApiModelProperty(value = "主键ID", example = "6ce819cee70c11e78fdee03f494c2e88")
    private String fID;

    @Column(name = "fBatchID")
    @ApiModelProperty(value = "批次号", example = "6ce819cee70c11e78fdee03f494c2e88")
    private String fBatchID;

    @Column(name = "fUserID")
    @ApiModelProperty(value = "用户ID", example = "6ce819cee70c11e78fdee03f494c2e88")
    private String fUserID;

    @Column(name = "fMailFrom")
    @ApiModelProperty(value = "发件人邮箱地址", example = "mailTest1@qq.com")
    private String fMailFrom;

    @Column(name = "fMailTo")
    @ApiModelProperty(value = "收件人邮箱地址", example = "lisi@aratek.com.cn")
    private String fMailTo;

    @Column(name = "fWCC")
    @ApiModelProperty(value = "抄送人邮箱地址", example = "wangwu@aratek.com.cn")
    private String fWCC;

    @Column(name = "fBCC")
    @ApiModelProperty(value = "密送人邮箱地址", example = "wangwu@aratek.com.cn")
    private String fBCC;

    @Column(name = "fSubject")
    @ApiModelProperty(value = "主题", example = "主题-邮件测试")
    private String fSubject;

    @Column(name = "fStatus")
    @ApiModelProperty(value = "发送状态", example = "2", notes = "0待发送,1发送中,2发送成功,3发送失败")
    private Integer fStatus;

    @Column(name = "fSendTime")
    @ApiModelProperty(value = "发送时间", example = "2018-05-15 17:22:11")
    private Date fSendTime;

    //必填参数
    @Transient
    @ApiModelProperty(value = "邮件内容", example = "邮件内容")
    private String content;

    //选填
    @Transient
    @ApiModelProperty(value = "邮件模板名称", example = "test.ftl")
    private String templateName;
    @Transient
    @ApiModelProperty(value = "自定义参数")
    private HashMap<String, String> kvMap;
    @Transient
    @ApiModelProperty(value = "附件列表")
    private List<AraPair<String, File>> attachments;


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getfID() {
        return fID;
    }

    public void setfID(String fID) {
        this.fID = fID;
    }

    public String getfBatchID() {
        return fBatchID;
    }

    public void setfBatchID(String fBatchID) {
        this.fBatchID = fBatchID;
    }

    public String getfUserID() {
        return fUserID;
    }

    public void setfUserID(String fUserID) {
        this.fUserID = fUserID;
    }

    public String getfMailFrom() {
        return fMailFrom;
    }

    public void setfMailFrom(String fMailFrom) {
        this.fMailFrom = fMailFrom;
    }

    public String getfMailTo() {
        return fMailTo;
    }

    public void setfMailTo(String fMailTo) {
        this.fMailTo = fMailTo;
    }

    public String getfWCC() {
        return fWCC;
    }

    public void setfWCC(String fWCC) {
        this.fWCC = fWCC;
    }

    public String getfBCC() {
        return fBCC;
    }

    public void setfBCC(String fBCC) {
        this.fBCC = fBCC;
    }

    public String getfSubject() {
        return fSubject;
    }

    public void setfSubject(String fSubject) {
        this.fSubject = fSubject;
    }

    public Integer getfStatus() {
        return fStatus;
    }

    public void setfStatus(Integer fStatus) {
        this.fStatus = fStatus;
    }

    public Date getfSendTime() {
        return fSendTime;
    }

    public void setfSendTime(Date fSendTime) {
        this.fSendTime = fSendTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public HashMap<String, String> getKvMap() {
        return kvMap;
    }

    public void setKvMap(HashMap<String, String> kvMap) {
        this.kvMap = kvMap;
    }

    public List<AraPair<String, File>> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<AraPair<String, File>> attachments) {
        this.attachments = attachments;
    }
}
