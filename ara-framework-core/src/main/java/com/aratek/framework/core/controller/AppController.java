package com.aratek.framework.core.controller;

import com.aratek.framework.core.annotation.SysLogAnnotation;
import com.aratek.framework.core.annotation.UserLogAnnotation;
import com.aratek.framework.core.constant.AraResultCodeConstants;
import com.aratek.framework.core.service.AppService;
import com.aratek.framework.core.util.ExcelUtil;
import com.aratek.framework.core.util.StringUtil;
import com.aratek.framework.domain.base.Result;
import com.aratek.framework.domain.base.controller.BaseController;
import com.aratek.framework.domain.constant.AraDomainConstants;
import com.aratek.framework.domain.core.AppInfo;
import com.aratek.framework.domain.core.AppInfoEntry;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * @author shijinlong
 * @date 2018-05-17
 * @description App接口
 */
@Api(tags = "App接口", description = "App相关接口")
@RestController
@RequestMapping("app")
public class AppController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppController.class);

    @Autowired
    private AppService appService;

    @RequiresPermissions(value = {"1012:select"})
    @SysLogAnnotation("分页查询App升级信息列表")
//    @UserLogAnnotation(module = "appInfo", actType = "select")
    @ApiOperation(value = "分页查询App升级信息列表", notes = "分页查询App升级信息列表")
    @RequestMapping(value = "/info/selectAppInfoList", method = RequestMethod.POST)
    public Map selectAppInfoList(@RequestBody(required = false) AppInfo appInfo) {
        if (appInfo == null) {
            appInfo = new AppInfo();
        }
        PageInfo pageInfo = new PageInfo(appService.selectAppInfoList(appInfo));
        return Result.ok().putData(pageInfo);
    }

    @RequiresPermissions(value = {"1012:export"})
    @SysLogAnnotation("导出App升级信息列表")
    @UserLogAnnotation(module = "appInfo", actType = "export")
    @ApiOperation(value = "导出App升级信息列表", notes = "导出App升级信息列表")
    @RequestMapping(value = "/info/exportAppInfoList", method = RequestMethod.POST)
    public void exportAppInfoList(@RequestBody(required = false) AppInfo appInfo) {
        if (appInfo == null) {
            appInfo = new AppInfo();
        }
        try {
            //设置允许接收的响应头
            ExcelUtil.setResponseExposeHeaders();
            Map resultMap = appService.exportAppInfoList(appInfo);
            ExcelUtil.setExportResultResponse((String) resultMap.get(AraDomainConstants.DEFAULT_RESULT_CODE_NAME));
        } catch (Exception e) {
            LOGGER.error("导出异常:{}", e);
            ExcelUtil.setExportResultResponse(AraResultCodeConstants.CODE_1602);
        }
    }

    @RequiresPermissions(value = {"1012:save"})
    @SysLogAnnotation("新增App升级信息")
    @UserLogAnnotation(module = "appInfo", actType = "insert")
    @ApiOperation(value = "新增App升级信息", notes = "新增App升级信息")
    @RequestMapping(value = "/info/insertAppInfo", method = RequestMethod.POST)
    public Map insertAppInfo(@RequestBody AppInfo appInfo) {
        if (StringUtil.isBlank(appInfo.getfAppID())) {
            LOGGER.warn("AppID为空!");
            return Result.error(AraResultCodeConstants.CODE_1403);
        }
        if (StringUtil.isBlank(appInfo.getfAppName())) {
            LOGGER.warn("App名称为空!");
            return Result.error(AraResultCodeConstants.CODE_1404);
        }
        if (StringUtil.isBlank(appInfo.getfAppName())) {
            LOGGER.warn("App版本为空!");
            return Result.error(AraResultCodeConstants.CODE_1405);
        }
        return appService.insertAppInfo(appInfo);
    }

    @RequiresPermissions(value = {"1012:update"})
    @SysLogAnnotation("修改App升级信息")
    @UserLogAnnotation(module = "appInfo", actType = "update")
    @ApiOperation(value = "修改App升级信息", notes = "修改App升级信息")
    @RequestMapping(value = "/info/updateAppInfo", method = RequestMethod.POST)
    public Map updateAppInfo(@RequestBody AppInfo appInfo) {
        if (StringUtil.isBlank(appInfo.getfID())) {
            LOGGER.warn("ID为空!");
            return Result.error(AraResultCodeConstants.CODE_1001);
        }
        return appService.updateAppInfo(appInfo);
    }

    @RequiresPermissions(value = {"1012:delete"})
    @SysLogAnnotation("批量删除App升级信息")
    @UserLogAnnotation(module = "appInfo", actType = "delete")
    @ApiOperation(value = "批量删除App升级信息", notes = "批量删除App升级信息")
    @RequestMapping(value = "/info/deleteAppInfoBatch", method = RequestMethod.DELETE)
    public Map deleteAppInfoBatch(@RequestBody List<String> idList) {
        if (idList == null || idList.size() == 0) {
            LOGGER.warn("ID为空!");
            return Result.error(AraResultCodeConstants.CODE_1001);
        }
        return appService.deleteAppInfoBatch(idList);
    }

    @RequiresPermissions(value = {"1012:select"})
    @SysLogAnnotation("查询App升级信息明细列表")
//    @UserLogAnnotation(module = "appInfoEntry", actType = "select")
    @ApiOperation(value = "查询App升级信息明细列表", notes = "查询App升级信息明细列表")
    @RequestMapping(value = "/infoEntry/selectAppInfoEntryList", method = RequestMethod.POST)
    public Map selectAppInfoEntryList(@RequestBody(required = false) AppInfoEntry appInfoEntry) {
        List<AppInfoEntry> appInfoEntryList = appService.selectAppInfoEntryList(appInfoEntry);
        return Result.ok().putData(appInfoEntryList);
    }

    @RequiresPermissions(value = {"1012:save"})
//    @SysLogAnnotation("新增App升级信息明细")
    @UserLogAnnotation(module = "appInfoEntry", actType = "insert")
    @ApiOperation(value = "新增App升级信息明细", notes = "新增App升级信息明细")
    @RequestMapping(value = "/infoEntry/insertAppInfoEntry", method = RequestMethod.POST)
    public Map insertAppInfoEntry(@RequestParam("file") MultipartFile file,
                                  @RequestParam("appID") String appID,
                                  @RequestParam("parentID") String parentID,
                                  @RequestParam("localDirectory") String localDirectory) {
        return appService.insertAppInfoEntry(appID, parentID, localDirectory, file);
    }

    /*@UserLogAnnotation(module = "appInfoEntry", actType = "insert")
    @ApiOperation(value = "批量新增App升级信息明细", notes = "批量新增App升级信息明细")
    @RequestMapping(value = "/infoEntry/insertAppInfoEntryBatch", method = RequestMethod.POST)
    public Map insertAppInfoEntryBatch(@RequestBody AppInfo appInfo) {
        if (StringUtil.isBlank(appInfo.getfID())) {
            LOGGER.warn("ID为空!");
            return Result.error(AraResultCodeConstants.CODE_1001);
        }
        if (null == appInfo.getAppInfoEntryList() || appInfo.getAppInfoEntryList().size() == 0) {
            LOGGER.warn("App升级信息明细为空!");
            return Result.error(AraResultCodeConstants.CODE_1000);
        }
        return appService.insertAppInfoEntryBatch(appInfo);
    }*/

    /*@UserLogAnnotation(module = "appInfoEntry", actType = "update")
    @ApiOperation(value = "修改App升级信息明细", notes = "修改App升级信息明细")
    @RequestMapping(value = "/infoEntry/updateAppInfoEntry", method = RequestMethod.POST)
    public Map updateAppInfoEntry(@RequestBody AppInfoEntry appInfoEntry) {
        if (StringUtil.isBlank(appInfoEntry.getfID())) {
            LOGGER.warn("ID为空!");
            return Result.error(AraResultCodeConstants.CODE_1001);
        }
        return appService.updateAppInfoEntry(appInfoEntry);
    }*/


    @RequiresPermissions(value = {"1012:delete"})
    @SysLogAnnotation("批量删除App升级信息明细")
    @UserLogAnnotation(module = "appInfoEntry", actType = "delete")
    @ApiOperation(value = "批量删除App升级信息明细", notes = "批量删除App升级信息明细")
    @RequestMapping(value = "/infoEntry/deleteAppInfoEntryBatch", method = RequestMethod.DELETE)
    public Map deleteAppInfoEntryBatch(@RequestBody List<AppInfoEntry> appInfoEntryList) {
        if (appInfoEntryList == null || appInfoEntryList.size() == 0) {
            LOGGER.warn("ID为空!");
            return Result.error(AraResultCodeConstants.CODE_1001);
        }
        for (AppInfoEntry appInfoEntry : appInfoEntryList) {
            if (StringUtil.isBlank(appInfoEntry.getfID())) {
                LOGGER.warn("ID为空!");
                return Result.error(AraResultCodeConstants.CODE_1001);
            }
            if (StringUtil.isBlank(appInfoEntry.getfAppPath())) {
                LOGGER.warn("App路径为空!");
                return Result.error(AraResultCodeConstants.CODE_1408);
            }
        }
        return appService.deleteAppInfoEntryBatch(appInfoEntryList);
    }


}
