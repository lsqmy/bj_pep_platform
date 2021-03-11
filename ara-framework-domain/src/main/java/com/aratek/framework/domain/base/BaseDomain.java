package com.aratek.framework.domain.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.Date;

/**
 * @author shijinlong
 * @date 2018-04-28
 * @description 基本对象，其他对象正常情况(含有创建人ID、创建时间、修改人ID、修改时间)下需要继承此对象，如不继承此对象，需要继承QueryDomain或CurrentUser
 * 注意：非数据库表字段需加上@Transient注解，不然通用mapper会报错
 */
@ApiModel(value = "BaseDomain", description = "基础对象模型")
public class BaseDomain extends QueryDomain {

    /**
     * 主键
     * 不自动生成，需要手动在service中生成ID
     */
    @Id
    @Column(name = "fID")
//    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "select uuid()")
//    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "SELECT REPLACE(UUID(),'-','')")
    @ApiModelProperty(value = "主键ID", example = "6ce819cee70c11e78fdee03f494c2e88")
    private String fID;

    @Column(name = "fCreatorID")
    @ApiModelProperty(value = "创建人ID", example = "9df1dbbe28e642e3b75e0b1ba0d7b345")
    private String fCreatorID;

    @Column(name = "fCreateTime")
    @ApiModelProperty(value = "创建时间", example = "2018-04-23 17:22:11")
//    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date fCreateTime;

    @Column(name = "fLastUpdateUserID")
    @ApiModelProperty(value = "最后修改人ID", example = "9df1dbbe28e642e3b75e0b1ba0d7b345")
    private String fLastUpdateUserID;

    @Column(name = "fLastUpdateTime")
    @ApiModelProperty(value = "最后修改时间", example = "2018-04-23 17:22:11")
//    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date fLastUpdateTime;

    @Transient
    @ApiModelProperty(value = "创建人名称", example = "zhangsan")
    private String fCreatorName;

    @Transient
    @ApiModelProperty(value = "最后修改人名称", example = "zhangsan")
    private String fLastUpdateUserName;

    public String getfID() {
        return fID;
    }

    public void setfID(String fID) {
        this.fID = fID;
    }

    public String getfCreatorID() {
        return fCreatorID;
    }

    public void setfCreatorID(String fCreatorID) {
        this.fCreatorID = fCreatorID;
    }

    public Date getfCreateTime() {
        return fCreateTime;
    }

    public void setfCreateTime(Date fCreateTime) {
        this.fCreateTime = fCreateTime;
    }

    public String getfLastUpdateUserID() {
        return fLastUpdateUserID;
    }

    public void setfLastUpdateUserID(String fLastUpdateUserID) {
        this.fLastUpdateUserID = fLastUpdateUserID;
    }

    public Date getfLastUpdateTime() {
        return fLastUpdateTime;
    }

    public void setfLastUpdateTime(Date fLastUpdateTime) {
        this.fLastUpdateTime = fLastUpdateTime;
    }

    public String getfCreatorName() {
        return fCreatorName;
    }

    public void setfCreatorName(String fCreatorName) {
        this.fCreatorName = fCreatorName;
    }

    public String getfLastUpdateUserName() {
        return fLastUpdateUserName;
    }

    public void setfLastUpdateUserName(String fLastUpdateUserName) {
        this.fLastUpdateUserName = fLastUpdateUserName;
    }

}
