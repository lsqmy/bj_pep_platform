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
 * @description 意见反馈
 * @table T_BD_FEEDBACK
 */
@Alias("araFwFeedback")
@Table(name = "t_bd_feedback")
@ApiModel(value = "Feedback", description = "意见反馈")
public class Feedback extends BaseDomain {

    @Column(name = "fUserID")
    @ApiModelProperty(value = "用户ID", example = "9df1dbbe28e642e3b75e0b1ba0d7b345")
    private String fUserID;

    @Column(name = "fSuggestion")
    @ApiModelProperty(value = "建议", example = "建议建议建议")
    private String fSuggestion;

    @Column(name = "fDescription")
    @ApiModelProperty(value = "备注", example = "备注")
    private String fDescription;

    @Column(name = "fStatus")
    @ApiModelProperty(value = "状态", example = "2", notes = "1保存,2启用,3禁用")
    private Integer fStatus;

    public String getfUserID() {
        return fUserID;
    }

    public void setfUserID(String fUserID) {
        this.fUserID = fUserID;
    }

    public String getfSuggestion() {
        return fSuggestion;
    }

    public void setfSuggestion(String fSuggestion) {
        this.fSuggestion = fSuggestion;
    }

    public String getfDescription() {
        return fDescription;
    }

    public void setfDescription(String fDescription) {
        this.fDescription = fDescription;
    }

    public Integer getfStatus() {
        return fStatus;
    }

    public void setfStatus(Integer fStatus) {
        this.fStatus = fStatus;
    }
}
