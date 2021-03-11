package com.aratek.framework.domain.core;

import com.aratek.framework.domain.base.QueryDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.ibatis.type.Alias;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author shijinlong
 * @date 2018-05-17
 * @description APP升级信息明细
 * @table T_BD_APPINFOENTRY
 */
@Alias("araFwAppInfoEntry")
@Table(name = "t_bd_appinfoentry")
@ApiModel(value = "AppInfoEntry", description = "APP升级信息明细")
public class AppInfoEntry extends QueryDomain {

    @Id
    @Column(name = "fID")
    @ApiModelProperty(value = "主键ID", example = "6ce819cee70c11e78fdee03f494c2e87")
    private String fID;

    @Column(name = "fParentID")
    @ApiModelProperty(value = "主表ID", example = "6ce819cee70c11e78fdee03f494c2e87")
    private String fParentID;

    @Column(name = "fFileName")
    @ApiModelProperty(value = "文件名称", example = "abc.zip")
    private String fFileName;

    @Column(name = "fMD5")
    @ApiModelProperty(value = "文件MD5校验值", example = "6ce819cee70c11e78fdee03f494c2e87")
    private String fMD5;

    @Column(name = "fAppPath")
    @ApiModelProperty(value = "文件存放路径", example = "/easypass")
    private String fAppPath;

    @Column(name = "fLocalDirectory")
    @ApiModelProperty(value = "本地目录", example = "D:/publish/easypass")
    private String fLocalDirectory;

    public String getfID() {
        return fID;
    }

    public void setfID(String fID) {
        this.fID = fID;
    }

    public String getfParentID() {
        return fParentID;
    }

    public void setfParentID(String fParentID) {
        this.fParentID = fParentID;
    }

    public String getfFileName() {
        return fFileName;
    }

    public void setfFileName(String fFileName) {
        this.fFileName = fFileName;
    }

    public String getfMD5() {
        return fMD5;
    }

    public void setfMD5(String fMD5) {
        this.fMD5 = fMD5;
    }

    public String getfAppPath() {
        return fAppPath;
    }

    public void setfAppPath(String fAppPath) {
        this.fAppPath = fAppPath;
    }

    public String getfLocalDirectory() {
        return fLocalDirectory;
    }

    public void setfLocalDirectory(String fLocalDirectory) {
        this.fLocalDirectory = fLocalDirectory;
    }
}
