package com.aratek.framework.core.controller;

import com.aratek.framework.core.annotation.SysLogAnnotation;
import com.aratek.framework.core.annotation.UserLogAnnotation;
import com.aratek.framework.core.constant.AraResultCodeConstants;
import com.aratek.framework.core.service.SysParamService;
import com.aratek.framework.core.util.ExcelUtil;
import com.aratek.framework.core.util.StringUtil;
import com.aratek.framework.domain.base.Result;
import com.aratek.framework.domain.base.controller.BaseController;
import com.aratek.framework.domain.constant.AraDomainConstants;
import com.aratek.framework.domain.core.SysParam;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author shijinlong
 * @date 2018-05-18
 * @description 系统参数接口
 */
@Api(tags = "系统参数接口", description = "系统参数增删改查相关接口")
@RestController
@RequestMapping("sysParam")
public class SysParamController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SysParamController.class);

    @Autowired
    private SysParamService sysParamService;

    @RequiresPermissions(value = {"1019:select"})
    @SysLogAnnotation("分页查询系统参数列表")
//    @UserLogAnnotation(module = "sysParam", actType = "select")
    @ApiOperation(value = "分页查询系统参数列表", notes = "分页查询系统参数列表")
    @RequestMapping(value = "selectSysParamList", method = RequestMethod.POST)
    public Map selectSysParamList(@RequestBody SysParam sysParam) {
        PageInfo pageInfo = new PageInfo(sysParamService.selectSysParamList(sysParam));
        return Result.ok().putData(pageInfo);
    }

    @RequiresPermissions(value = {"1019:export"})
    @SysLogAnnotation("导出系统参数列表")
    @UserLogAnnotation(module = "参数设置", actType = "导出")
    @ApiOperation(value = "导出系统参数列表", notes = "导出系统参数列表")
    @RequestMapping(value = "exportSysParamList", method = RequestMethod.POST)
    public void exportSysParamList(@RequestBody SysParam sysParam) {
        try {
            //设置允许接收的响应头
            ExcelUtil.setResponseExposeHeaders();
            Map resultMap = sysParamService.exportSysParamList(sysParam);
            ExcelUtil.setExportResultResponse((String) resultMap.get(AraDomainConstants.DEFAULT_RESULT_CODE_NAME));
        } catch (Exception e) {
            LOGGER.error("导出异常:{}", e);
            ExcelUtil.setExportResultResponse(AraResultCodeConstants.CODE_1602);
        }
    }

    @RequiresPermissions(value = {"1019:save"})
    @SysLogAnnotation("新增系统参数")
    @UserLogAnnotation(module = "参数设置", actType = "新增")
    @ApiOperation(value = "新增系统参数", notes = "新增系统参数")
    @RequestMapping(value = "insertSysParam", method = RequestMethod.POST)
    public Map insertSysParam(@RequestBody SysParam sysParam) {
        if (StringUtil.isBlank(sysParam.getfName())) {
            LOGGER.warn("name为空!");
            return Result.error(AraResultCodeConstants.CODE_1008);
        }
        if (StringUtil.isBlank(sysParam.getfNumber())) {
            LOGGER.warn("number为空!");
            return Result.error(AraResultCodeConstants.CODE_1007);
        }
        return sysParamService.insertSysParam(sysParam);
    }

    @RequiresPermissions(value = {"1019:update"})
    @SysLogAnnotation("修改系统参数信息")
    @UserLogAnnotation(module = "参数设置", actType = "修改")
    @ApiOperation(value = "修改系统参数信息", notes = "修改系统参数信息")
    @RequestMapping(value = "updateSysParam", method = RequestMethod.POST)
    public Map updateSysParam(@RequestBody SysParam sysParam) {
        if (StringUtil.isBlank(sysParam.getfID())) {
            LOGGER.warn("ID为空!");
            return Result.error(AraResultCodeConstants.CODE_1001);
        }
        return sysParamService.updateSysParam(sysParam);
    }

    /*@UserLogAnnotation(module = "sysParam", actType = "delete")
    @ApiOperation(value = "根据ID删除系统参数", notes = "根据ID删除系统参数")
    @RequestMapping(value = "deleteSysParamByID", method = RequestMethod.DELETE)
    public Map deleteSysParamByID(@RequestBody SysParam sysParam) {
        if (StringUtil.isBlank(sysParam.getfID())) {
            LOGGER.warn("ID为空!");
            return Result.error(AraResultCodeConstants.CODE_1001);
        }
        return sysParamService.deleteSysParamByID(sysParam.getfID());
    }*/

    @RequiresPermissions(value = {"1019:delete"})
    @SysLogAnnotation("批量根据ID删除系统参数")
    @UserLogAnnotation(module = "参数设置", actType = "删除")
    @ApiOperation(value = "批量根据ID删除系统参数", notes = "批量根据ID删除系统参数")
    @RequestMapping(value = "deleteSysParamBatch", method = RequestMethod.DELETE)
    public Map deleteSysParamBatch(@RequestBody List<String> idList) {
        if (idList == null || idList.size() == 0) {
            LOGGER.warn("ID为空!");
            return Result.error(AraResultCodeConstants.CODE_1001);
        }
        return sysParamService.deleteSysParamBatch(idList);
    }
}
