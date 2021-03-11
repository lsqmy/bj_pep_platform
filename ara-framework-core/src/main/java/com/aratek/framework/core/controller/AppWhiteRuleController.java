package com.aratek.framework.core.controller;

import com.aratek.framework.core.annotation.SysLogAnnotation;
import com.aratek.framework.core.annotation.UserLogAnnotation;
import com.aratek.framework.core.constant.AraCoreConstants;
import com.aratek.framework.core.constant.AraResultCodeConstants;
import com.aratek.framework.core.service.AppWhiteRuleService;
import com.aratek.framework.core.util.CurrentUserUtil;
import com.aratek.framework.core.util.ExcelUtil;
import com.aratek.framework.core.util.StringUtil;
import com.aratek.framework.domain.base.Result;
import com.aratek.framework.domain.base.controller.BaseController;
import com.aratek.framework.domain.constant.AraDomainConstants;
import com.aratek.framework.domain.core.AppInfoWhiteRule;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author shijinlong
 * @date 2018-06-22
 * @description APP升级白名单接口
 */
@Api(tags = "APP升级白名单接口", description = "APP升级白名单相关接口")
@RestController
@RequestMapping("app/white")
public class AppWhiteRuleController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppWhiteRuleController.class);

    @Autowired
    private AppWhiteRuleService appWhiteRuleService;

    @RequiresPermissions(value = {"1012:whitelist"})
    @SysLogAnnotation("查询APP升级白名单列表")
//    @UserLogAnnotation(module = "appInfoWhiteRule", actType = "select")
    @ApiOperation(value = "查询APP升级白名单列表", notes = "查询APP升级白名单列表")
    @RequestMapping(value = "selectAppInfoWhiteRuleList", method = RequestMethod.POST)
    public Map selectAppInfoWhiteRuleList(@RequestBody AppInfoWhiteRule appInfoWhiteRule) {
        PageInfo pageInfo = new PageInfo(appWhiteRuleService.selectAppInfoWhiteRuleList(appInfoWhiteRule));
        return Result.ok().putData(pageInfo);
    }

    @RequiresPermissions(value = {"1012:whitelist"})
    @SysLogAnnotation("导出APP升级白名单列表")
    @UserLogAnnotation(module = "appInfoWhiteRule", actType = "export")
    @ApiOperation(value = "导出APP升级白名单列表", notes = "导出APP升级白名单列表")
    @RequestMapping(value = "exportAppInfoWhiteRuleList", method = RequestMethod.POST)
    public void exportAppInfoWhiteRuleList(@RequestBody AppInfoWhiteRule appInfoWhiteRule) {
        try {
            //设置允许接收的响应头
            ExcelUtil.setResponseExposeHeaders();
            Map resultMap = appWhiteRuleService.exportAppInfoWhiteRuleList(appInfoWhiteRule);
            ExcelUtil.setExportResultResponse((String) resultMap.get(AraDomainConstants.DEFAULT_RESULT_CODE_NAME));
        } catch (Exception e) {
            LOGGER.error("导出异常:{}", e);
            ExcelUtil.setExportResultResponse(AraResultCodeConstants.CODE_1602);
        }
    }

    @RequiresPermissions(value = {"1012:whitelist"})
    @SysLogAnnotation("新增APP升级白名单")
    @UserLogAnnotation(module = "appInfoWhiteRule", actType = "insert")
    @ApiOperation(value = "新增APP升级白名单", notes = "新增APP升级白名单")
    @RequestMapping(value = "insertAppInfoWhiteRule", method = RequestMethod.POST)
    public Map insertAppInfoWhiteRule(@RequestBody AppInfoWhiteRule appInfoWhiteRule) {
        if (StringUtil.isBlank(appInfoWhiteRule.getfAppID())) {
            LOGGER.warn("AppID为空!");
            return Result.error(AraResultCodeConstants.CODE_1403);
        }
        if (StringUtil.isBlank(appInfoWhiteRule.getfWhiteType())) {
            LOGGER.warn("WhiteType为空!");
            return Result.error(AraResultCodeConstants.CODE_1410);
        }
        if (StringUtil.isBlank(appInfoWhiteRule.getfCheckType())) {
            LOGGER.warn("CheckType为空!");
            return Result.error(AraResultCodeConstants.CODE_1411);
        }
        return appWhiteRuleService.insertAppInfoWhiteRule(appInfoWhiteRule);
    }

    @RequiresPermissions(value = {"1012:whitelist"})
    @SysLogAnnotation("修改APP升级白名单")
    @UserLogAnnotation(module = "appInfoWhiteRule", actType = "update")
    @ApiOperation(value = "修改APP升级白名单", notes = "修改APP升级白名单")
    @RequestMapping(value = "updateAppInfoWhiteRule", method = RequestMethod.POST)
    public Map updateAppInfoWhiteRule(@RequestBody AppInfoWhiteRule appInfoWhiteRule) {
        if (StringUtil.isBlank(appInfoWhiteRule.getfID())) {
            LOGGER.warn("ID为空!");
            return Result.error(AraResultCodeConstants.CODE_1001);
        }
        return appWhiteRuleService.updateAppInfoWhiteRule(appInfoWhiteRule);
    }

    @RequiresPermissions(value = {"1012:whitelist"})
    @SysLogAnnotation("批量启用APP升级白名单")
    @UserLogAnnotation(module = "appInfoWhiteRule", actType = "update")
    @ApiOperation(value = "批量启用APP升级白名单", notes = "批量启用APP升级白名单")
    @RequestMapping(value = "updateAppInfoWhiteRuleToEnableBatch", method = RequestMethod.POST)
    public Map updateAppInfoWhiteRuleToEnableBatch(@RequestBody List<String> idList) {
        if (idList == null || idList.size() == 0) {
            LOGGER.warn("ID为空!");
            return Result.error(AraResultCodeConstants.CODE_1001);
        }
        List<AppInfoWhiteRule> appInfoWhiteRuleList = new ArrayList<AppInfoWhiteRule>();
        String currentUserID = CurrentUserUtil.getCurrentUserID();
        Date now = new Date();
        for (String id : idList) {
            AppInfoWhiteRule appInfoWhiteRule = new AppInfoWhiteRule();
            appInfoWhiteRule.setfID(id);
            appInfoWhiteRule.setfStatus(AraCoreConstants.STATUS_ENABLE);
            appInfoWhiteRule.setCurrentUserID(currentUserID);
            appInfoWhiteRule.setCurrentUserOperationTime(now);
            appInfoWhiteRuleList.add(appInfoWhiteRule);
        }
        return appWhiteRuleService.updateAppInfoWhiteRuleStatusBatch(appInfoWhiteRuleList);
    }

    @RequiresPermissions(value = {"1012:whitelist"})
    @SysLogAnnotation("批量禁用APP升级白名单")
    @UserLogAnnotation(module = "city", actType = "update")
    @ApiOperation(value = "批量禁用APP升级白名单", notes = "批量禁用APP升级白名单")
    @RequestMapping(value = "updateAppInfoWhiteRuleToDisableBatch", method = RequestMethod.POST)
    public Map updateAppInfoWhiteRuleToDisableBatch(@RequestBody List<String> idList) {
        if (idList == null || idList.size() == 0) {
            LOGGER.warn("ID为空!");
            return Result.error(AraResultCodeConstants.CODE_1001);
        }
        List<AppInfoWhiteRule> appInfoWhiteRuleList = new ArrayList<AppInfoWhiteRule>();
        String currentUserID = CurrentUserUtil.getCurrentUserID();
        Date now = new Date();
        for (String id : idList) {
            AppInfoWhiteRule appInfoWhiteRule = new AppInfoWhiteRule();
            appInfoWhiteRule.setfID(id);
            appInfoWhiteRule.setfStatus(AraCoreConstants.STATUS_DISABLE);
            appInfoWhiteRule.setCurrentUserID(currentUserID);
            appInfoWhiteRule.setCurrentUserOperationTime(now);
            appInfoWhiteRuleList.add(appInfoWhiteRule);
        }
        return appWhiteRuleService.updateAppInfoWhiteRuleStatusBatch(appInfoWhiteRuleList);
    }


    @RequiresPermissions(value = {"1012:whitelist"})
    @SysLogAnnotation("批量删除APP升级白名单")
    @UserLogAnnotation(module = "appInfoWhiteRule", actType = "delete")
    @ApiOperation(value = "批量删除APP升级白名单", notes = "批量删除APP升级白名单")
    @RequestMapping(value = "deleteAppInfoWhiteRuleBatch", method = RequestMethod.DELETE)
    public Map deleteAppInfoWhiteRuleBatch(@RequestBody List<String> idList) {
        if (idList == null || idList.size() == 0) {
            LOGGER.warn("ID为空!");
            return Result.error(AraResultCodeConstants.CODE_1001);
        }
        return appWhiteRuleService.deleteAppInfoWhiteRuleBatch(idList);
    }
}
