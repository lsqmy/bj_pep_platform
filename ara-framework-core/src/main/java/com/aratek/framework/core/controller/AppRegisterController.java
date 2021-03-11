package com.aratek.framework.core.controller;

import com.aratek.framework.core.annotation.SysLogAnnotation;
import com.aratek.framework.core.annotation.UserLogAnnotation;
import com.aratek.framework.core.constant.AraCoreConstants;
import com.aratek.framework.core.constant.AraResultCodeConstants;
import com.aratek.framework.core.service.AppRegisterService;
import com.aratek.framework.core.util.CurrentUserUtil;
import com.aratek.framework.core.util.ExcelUtil;
import com.aratek.framework.core.util.StringUtil;
import com.aratek.framework.domain.base.Result;
import com.aratek.framework.domain.base.controller.BaseController;
import com.aratek.framework.domain.constant.AraDomainConstants;
import com.aratek.framework.domain.core.AppRegister;
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
 * @date 2018-05-23
 * @description APP注册信息接口
 */
@Api(tags = "APP注册信息接口", description = "APP注册信息相关接口")
@RestController
@RequestMapping("app/register")
public class AppRegisterController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppRegisterController.class);

    @Autowired
    private AppRegisterService appRegisterService;

    @RequiresPermissions(value = {"1013:select"})
    @SysLogAnnotation("分页查询APP注册信息列表")
//    @UserLogAnnotation(module = "appRegister", actType = "select")
    @ApiOperation(value = "分页查询APP注册信息列表", notes = "分页查询APP注册信息列表")
    @RequestMapping(value = "selectAppRegisterList", method = RequestMethod.POST)
    public Map selectAppRegisterList(@RequestBody AppRegister appRegister) {
        PageInfo pageInfo = new PageInfo(appRegisterService.selectAppRegisterList(appRegister));
        return Result.ok().putData(pageInfo);
    }

    @RequiresPermissions(value = {"1013:export"})
    @SysLogAnnotation("导出APP注册信息列表")
    @UserLogAnnotation(module = "appRegister", actType = "export")
    @ApiOperation(value = "导出APP注册信息列表", notes = "导出APP注册信息列表")
    @RequestMapping(value = "exportAppRegisterList", method = RequestMethod.POST)
    public void exportAppRegisterList(@RequestBody AppRegister appRegister) {
        try {
            //设置允许接收的响应头
            ExcelUtil.setResponseExposeHeaders();
            Map resultMap = appRegisterService.exportAppRegisterList(appRegister);
            ExcelUtil.setExportResultResponse((String) resultMap.get(AraDomainConstants.DEFAULT_RESULT_CODE_NAME));
        } catch (Exception e) {
            LOGGER.error("导出异常:{}", e);
            ExcelUtil.setExportResultResponse(AraResultCodeConstants.CODE_1602);
        }
    }

    /*@UserLogAnnotation(module = "appRegister", actType = "select")
    @ApiOperation(value = "根据ID查询APP注册信息", notes = "根据ID查询APP注册信息")
    @RequestMapping(value = "selectAppRegisterByID", method = RequestMethod.POST)
    public Map selectAppRegisterByID(@RequestBody AppRegister appRegister) {
        AppRegister info = appRegisterService.selectAppRegisterByID(appRegister.getfID());
        if (info == null) {
            LOGGER.warn("App不存在!");
            return Result.error(AraResultCodeConstants.CODE_1406);
        }
        return Result.ok().putData(info);
    }*/

    @RequiresPermissions(value = {"1013:save"})
    @SysLogAnnotation("新增APP注册信息")
    @UserLogAnnotation(module = "appRegister", actType = "insert")
    @ApiOperation(value = "新增APP注册信息", notes = "新增APP注册信息")
    @RequestMapping(value = "insertAppRegister", method = RequestMethod.POST)
    public Map insertAppRegister(@RequestBody AppRegister appRegister) {
        if (StringUtil.isBlank(appRegister.getfAppID())) {
            LOGGER.warn("appID is null!");
            return Result.error(AraResultCodeConstants.CODE_1403);
        }
        if (null == appRegister.getfAppName()) {
            LOGGER.warn("appName is null!");
            return Result.error(AraResultCodeConstants.CODE_1404);
        }
        if (StringUtil.isBlank(appRegister.getfSecretKey())) {
            LOGGER.warn("app secret key is null!");
            return Result.error(AraResultCodeConstants.CODE_1407);
        }
        return appRegisterService.insertAppRegister(appRegister);
    }

    @RequiresPermissions(value = {"1013:update"})
    @SysLogAnnotation("修改APP注册信息信息")
    @UserLogAnnotation(module = "appRegister", actType = "update")
    @ApiOperation(value = "修改APP注册信息信息", notes = "修改APP注册信息信息")
    @RequestMapping(value = "updateAppRegister", method = RequestMethod.POST)
    public Map updateAppRegister(@RequestBody AppRegister appRegister) {
        if (StringUtil.isBlank(appRegister.getfID())) {
            LOGGER.warn("ID为空!");
            return Result.error(AraResultCodeConstants.CODE_1001);
        }
        return appRegisterService.updateAppRegister(appRegister);
    }

    @RequiresPermissions(value = {"1013:enable"})
    @SysLogAnnotation("批量启用APP注册信息")
    @UserLogAnnotation(module = "appRegister", actType = "update")
    @ApiOperation(value = "批量启用APP注册信息", notes = "批量启用APP注册信息")
    @RequestMapping(value = "updateFtpToEnableBatch", method = RequestMethod.POST)
    public Map updateFtpToEnableBatch(@RequestBody List<String> idList) {
        if (idList == null || idList.size() == 0) {
            LOGGER.warn("ID为空!");
            return Result.error(AraResultCodeConstants.CODE_1001);
        }
        List<AppRegister> appRegisterList = new ArrayList<AppRegister>();
        String currentUserID = CurrentUserUtil.getCurrentUserID();
        Date now = new Date();
        for (String id : idList) {
            AppRegister appRegister = new AppRegister();
            appRegister.setfID(id);
            appRegister.setfStatus(AraCoreConstants.STATUS_ENABLE);
            appRegister.setCurrentUserID(currentUserID);
            appRegister.setCurrentUserOperationTime(now);
            appRegisterList.add(appRegister);
        }
        return appRegisterService.updateAppRegisterStatusBatch(appRegisterList);
    }

    @RequiresPermissions(value = {"1013:disable"})
    @SysLogAnnotation("批量禁用APP注册信息")
    @UserLogAnnotation(module = "appRegister", actType = "update")
    @ApiOperation(value = "批量禁用APP注册信息", notes = "批量禁用APP注册信息")
    @RequestMapping(value = "updateFtpToDisableBatch", method = RequestMethod.POST)
    public Map updateFtpToDisableBatch(@RequestBody List<String> idList) {
        if (idList == null || idList.size() == 0) {
            LOGGER.warn("ID为空!");
            return Result.error(AraResultCodeConstants.CODE_1001);
        }
        List<AppRegister> appRegisterList = new ArrayList<AppRegister>();
        String currentUserID = CurrentUserUtil.getCurrentUserID();
        Date now = new Date();
        for (String id : idList) {
            AppRegister appRegister = new AppRegister();
            appRegister.setfID(id);
            appRegister.setfStatus(AraCoreConstants.STATUS_DISABLE);
            appRegister.setCurrentUserID(currentUserID);
            appRegister.setCurrentUserOperationTime(now);
            appRegisterList.add(appRegister);
        }
        return appRegisterService.updateAppRegisterStatusBatch(appRegisterList);
    }

    /*@SysLogAnnotation("根据ID删除APP注册信息")
    @UserLogAnnotation(module = "appRegister", actType = "delete")
    @ApiOperation(value = "根据ID删除APP注册信息", notes = "根据ID删除APP注册信息")
    @RequestMapping(value = "deleteAppRegisterByID", method = RequestMethod.DELETE)
    public Map deleteAppRegisterByID(@RequestBody AppRegister appRegister) {
        if (StringUtil.isBlank(appRegister.getfID())) {
            LOGGER.warn("ID为空!");
            return Result.error(AraResultCodeConstants.CODE_1001);
        }
        return appRegisterService.deleteAppRegisterByID(appRegister.getfID());
    }*/

    @RequiresPermissions(value = {"1013:delete"})
    @SysLogAnnotation("批量根据ID删除APP注册信息")
    @UserLogAnnotation(module = "appRegister", actType = "delete")
    @ApiOperation(value = "批量根据ID删除APP注册信息", notes = "批量根据ID删除APP注册信息")
    @RequestMapping(value = "deleteAppRegisterBatch", method = RequestMethod.DELETE)
    public Map deleteAppRegisterBatch(@RequestBody List<String> idList) {
        if (idList == null || idList.size() == 0) {
            LOGGER.warn("ID为空!");
            return Result.error(AraResultCodeConstants.CODE_1001);
        }
        return appRegisterService.deleteAppRegisterBatch(idList);
    }

}
