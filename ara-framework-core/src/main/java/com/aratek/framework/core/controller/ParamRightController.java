package com.aratek.framework.core.controller;

import com.aratek.framework.core.annotation.SysLogAnnotation;
import com.aratek.framework.core.annotation.UserLogAnnotation;
import com.aratek.framework.core.constant.AraResultCodeConstants;
import com.aratek.framework.core.service.ParamRightService;
import com.aratek.framework.core.util.ExcelUtil;
import com.aratek.framework.core.util.StringUtil;
import com.aratek.framework.domain.base.Result;
import com.aratek.framework.domain.base.controller.BaseController;
import com.aratek.framework.domain.constant.AraDomainConstants;
import com.aratek.framework.domain.core.ParamRight;
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
 * @date 2018-06-22
 * @description 权限参数接口
 */
@Api(tags = "权限参数接口", description = "权限参数相关接口")
@RestController
@RequestMapping("sysParam/right")
public class ParamRightController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParamRightController.class);

    @Autowired
    private ParamRightService paramRightService;

    @RequiresPermissions(value = {"1021:select"})
    @SysLogAnnotation("分页查询权限参数列表")
//    @UserLogAnnotation(module = "paramRight", actType = "select")
    @ApiOperation(value = "分页查询权限参数列表", notes = "分页查询权限参数列表")
    @RequestMapping(value = "selectParamRightList", method = RequestMethod.POST)
    public Map selectParamRightList(@RequestBody ParamRight paramRight) {
        PageInfo pageInfo = new PageInfo(paramRightService.selectParamRightList(paramRight));
        return Result.ok().putData(pageInfo);
    }

    @RequiresPermissions(value = {"1021:export"})
    @SysLogAnnotation("导出权限参数列表")
    @UserLogAnnotation(module = "权限码", actType = "导出")
    @ApiOperation(value = "导出权限参数列表", notes = "导出权限参数列表")
    @RequestMapping(value = "exportParamRightList", method = RequestMethod.POST)
    public void exportParamRightList(@RequestBody ParamRight paramRight) {
        try {
            //设置允许接收的响应头
            ExcelUtil.setResponseExposeHeaders();
            Map resultMap = paramRightService.exportParamRightList(paramRight);
            ExcelUtil.setExportResultResponse((String) resultMap.get(AraDomainConstants.DEFAULT_RESULT_CODE_NAME));
        } catch (Exception e) {
            LOGGER.error("导出异常:{}", e);
            ExcelUtil.setExportResultResponse(AraResultCodeConstants.CODE_1602);
        }
    }

    @RequiresPermissions(value = {"1021:save"})
    @SysLogAnnotation("新增权限参数")
    @UserLogAnnotation(module = "权限码", actType = "新增")
    @ApiOperation(value = "新增权限参数", notes = "新增权限参数")
    @RequestMapping(value = "insertParamRight", method = RequestMethod.POST)
    public Map insertParamRight(@RequestBody ParamRight paramRight) {
        if (StringUtil.isBlank(paramRight.getfRightCode())) {
            LOGGER.warn("RightCode为空!");
            return Result.error(AraResultCodeConstants.CODE_1000);
        }
        if (StringUtil.isBlank(paramRight.getfRightName())) {
            LOGGER.warn("RightName为空!");
            return Result.error(AraResultCodeConstants.CODE_1000);
        }
        if (paramRight.getfDisplayorder() == null) {
            LOGGER.warn("Displayorder为空!");
            return Result.error(AraResultCodeConstants.CODE_1000);
        }
        return paramRightService.insertParamRight(paramRight);
    }

    @RequiresPermissions(value = {"1021:update"})
    @SysLogAnnotation("修改权限参数信息")
    @UserLogAnnotation(module = "权限码", actType = "修改")
    @ApiOperation(value = "修改权限参数信息", notes = "修改权限参数信息")
    @RequestMapping(value = "updateParamRight", method = RequestMethod.POST)
    public Map updateParamRight(@RequestBody ParamRight paramRight) {
        if (StringUtil.isBlank(paramRight.getfID())) {
            LOGGER.warn("ID为空!");
            return Result.error(AraResultCodeConstants.CODE_1001);
        }
        return paramRightService.updateParamRight(paramRight);
    }

    @RequiresPermissions(value = {"1021:delete"})
    @SysLogAnnotation("批量根据ID删除权限参数")
    @UserLogAnnotation(module = "权限码", actType = "删除")
    @ApiOperation(value = "批量根据ID删除权限参数", notes = "批量根据ID删除权限参数")
    @RequestMapping(value = "deleteParamRightBatch", method = RequestMethod.DELETE)
    public Map deleteParamRightBatch(@RequestBody List<String> idList) {
        if (idList == null || idList.size() == 0) {
            LOGGER.warn("ID为空!");
            return Result.error(AraResultCodeConstants.CODE_1001);
        }
        return paramRightService.deleteParamRightBatch(idList);
    }
}
