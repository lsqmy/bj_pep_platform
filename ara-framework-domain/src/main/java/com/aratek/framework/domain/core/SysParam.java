package com.aratek.framework.domain.core;

import com.aratek.framework.domain.base.BaseDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.ibatis.type.Alias;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * @author shijinlong
 * @date 2018-05-18
 * @description 系统参数
 * @table T_BD_PARAM
 */
@Alias("araFwSysParam")
@Table(name = "t_bd_param")
@ApiModel(value = "SysParam", description = "系统参数")
public class SysParam extends BaseDomain {

    @Column(name = "fName")
    @ApiModelProperty(value = "名称", example = "深圳")
    private String fName;

    @Column(name = "fNumber")
    @ApiModelProperty(value = "编号", example = "1001")
    private String fNumber;

    @Column(name = "fValue")
    @ApiModelProperty(value = "参数值", example = "aaabbb")
    private String fValue;

    @Column(name = "fDescription")
    @ApiModelProperty(value = "备注", example = "备注")
    private String fDescription;

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getfNumber() {
        return fNumber;
    }

    public void setfNumber(String fNumber) {
        this.fNumber = fNumber;
    }

    public String getfValue() {
        return fValue;
    }

    public void setfValue(String fValue) {
        this.fValue = fValue;
    }

    public String getfDescription() {
        return fDescription;
    }

    public void setfDescription(String fDescription) {
        this.fDescription = fDescription;
    }
}
