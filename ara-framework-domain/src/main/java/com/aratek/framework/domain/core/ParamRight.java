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
 * @description 权限参数
 * @table T_BD_PARAMRIGHT
 */
@Alias("araFwParamRight")
@Table(name = "t_bd_paramright")
@ApiModel(value = "ParamRight", description = "权限参数")
public class ParamRight extends BaseDomain {

    @Column(name = "fRightCode")
    @ApiModelProperty(value = "权限编码", example = "select")
    private String fRightCode;

    @Column(name = "fRightName")
    @ApiModelProperty(value = "权限名称", example = "查询")
    private String fRightName;

    @Column(name = "fDisplayorder")
    @ApiModelProperty(value = "序号", example = "1")
    private Integer fDisplayorder;

    public String getfRightCode() {
        return fRightCode;
    }

    public void setfRightCode(String fRightCode) {
        this.fRightCode = fRightCode;
    }

    public String getfRightName() {
        return fRightName;
    }

    public void setfRightName(String fRightName) {
        this.fRightName = fRightName;
    }

    public Integer getfDisplayorder() {
        return fDisplayorder;
    }

    public void setfDisplayorder(Integer fDisplayorder) {
        this.fDisplayorder = fDisplayorder;
    }
}
