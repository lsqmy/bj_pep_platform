package com.aratek.framework.core.controller;

import com.aratek.framework.core.annotation.SysLogAnnotation;
import com.aratek.framework.core.annotation.UserLogAnnotation;
import com.aratek.framework.core.constant.AraResultCodeConstants;
import com.aratek.framework.core.service.LogService;
import com.aratek.framework.core.util.ExcelUtil;
import com.aratek.framework.domain.base.Result;
import com.aratek.framework.domain.base.controller.BaseController;
import com.aratek.framework.domain.constant.AraDomainConstants;
import com.aratek.framework.domain.core.UserLog;
import com.aratek.framework.domain.core.UserLoginLog;
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

import java.util.Map;

/**
 * @author shijinlong
 * @date 2018-05-15
 * @description 日志接口
 */
@Api(tags = "日志接口", description = "日志相关接口")
@RestController
@RequestMapping("log")
public class LogController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogController.class);

    @Autowired
    private LogService logService;

    @RequiresPermissions(value = {"1017:select"})
    @SysLogAnnotation("分页查询用户登录日志列表")
//    @UserLogAnnotation(module = "userLoginLog", actType = "select")
    @ApiOperation(value = "分页查询用户登录日志列表", notes = "分页查询用户登录日志列表")
    @RequestMapping(value = "selectUserLoginLogList", method = RequestMethod.POST)
    public Map selectUserLoginLogList(@RequestBody UserLoginLog userLoginLog) {
        PageInfo pageInfo = new PageInfo(logService.selectUserLoginLogList(userLoginLog));
        return Result.ok().putData(pageInfo);
    }

    @RequiresPermissions(value = {"1017:export"})
    @SysLogAnnotation("导出用户登录日志列表")
    @UserLogAnnotation(module = "用户登录日志", actType = "导出")
    @ApiOperation(value = "导出用户登录日志列表", notes = "导出用户登录日志列表")
    @RequestMapping(value = "exportUserLoginLogList", method = RequestMethod.POST)
    public void exportUserLoginLogList(@RequestBody UserLoginLog userLoginLog) {
        try {
            //设置允许接收的响应头
            ExcelUtil.setResponseExposeHeaders();
            Map resultMap = logService.exportUserLoginLogList(userLoginLog);
            ExcelUtil.setExportResultResponse((String) resultMap.get(AraDomainConstants.DEFAULT_RESULT_CODE_NAME));
        } catch (Exception e) {
            LOGGER.error("导出异常:{}", e);
            ExcelUtil.setExportResultResponse(AraResultCodeConstants.CODE_1602);
        }
    }

    @RequiresPermissions(value = {"1018:select"})
    @SysLogAnnotation("分页查询用户操作日志列表")
//    @UserLogAnnotation(module = "userLog", actType = "select")
    @ApiOperation(value = "分页查询用户操作日志列表", notes = "分页查询用户操作日志列表")
    @RequestMapping(value = "selectUserLogList", method = RequestMethod.POST)
    public Map selectUserLogList(@RequestBody UserLog userLog) {
        PageInfo pageInfo = new PageInfo(logService.selectUserLogList(userLog));
        return Result.ok().putData(pageInfo);
    }

    @RequiresPermissions(value = {"1018:export"})
    @SysLogAnnotation("导出用户操作日志列表")
    @UserLogAnnotation(module = "用户操作日志", actType = "导出")
    @ApiOperation(value = "导出用户操作日志列表", notes = "导出用户操作日志列表")
    @RequestMapping(value = "exportUserLogList", method = RequestMethod.POST)
    public void exportUserLogList(@RequestBody UserLog userLog) {
        try {
            //设置允许接收的响应头
            ExcelUtil.setResponseExposeHeaders();
            Map resultMap = logService.exportUserLogList(userLog);
            ExcelUtil.setExportResultResponse((String) resultMap.get(AraDomainConstants.DEFAULT_RESULT_CODE_NAME));
        } catch (Exception e) {
            LOGGER.error("导出异常:{}", e);
            ExcelUtil.setExportResultResponse(AraResultCodeConstants.CODE_1602);
        }
    }


}
