package com.aratek.framework.core.controller;

import com.aratek.framework.core.annotation.SysLogAnnotation;
import com.aratek.framework.core.annotation.UserLogAnnotation;
import com.aratek.framework.core.constant.AraResultCodeConstants;
import com.aratek.framework.core.service.MenuRightService;
import com.aratek.framework.core.util.ExcelUtil;
import com.aratek.framework.core.util.StringUtil;
import com.aratek.framework.domain.base.Result;
import com.aratek.framework.domain.base.controller.BaseController;
import com.aratek.framework.domain.constant.AraDomainConstants;
import com.aratek.framework.domain.core.MenuRight;
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
 * @date 2018-05-10
 * @description 菜单权限服务
 */
@Api(tags = "菜单权限服务接口", description = "菜单权限服务相关接口")
@RequestMapping("menu/right")
@RestController
public class MenuRightController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MenuRightController.class);

    @Autowired
    private MenuRightService menuRightService;

    @RequiresPermissions(value = {"1004:select"})
    @SysLogAnnotation("分页查询权限列表")
//    @UserLogAnnotation(module = "menuRight", actType = "select")
    @ApiOperation(value = "分页查询权限列表", notes = "分页查询权限列表")
    @RequestMapping(value = "selectMenuRightList", method = RequestMethod.POST)
    public Map selectMenuRightList(@RequestBody MenuRight menuRight) {
        PageInfo pageInfo = new PageInfo(menuRightService.selectMenuRightList(menuRight));
        return Result.ok().putData(pageInfo);
    }

    @RequiresPermissions(value = {"1004:export"})
    @SysLogAnnotation("导出权限列表")
    @UserLogAnnotation(module = "菜单权限", actType = "导出")
    @ApiOperation(value = "导出权限列表", notes = "导出权限列表")
    @RequestMapping(value = "exportMenuRightList", method = RequestMethod.POST)
    public void exportMenuRightList(@RequestBody MenuRight menuRight) {
        try {
            //设置允许接收的响应头
            ExcelUtil.setResponseExposeHeaders();
            Map resultMap = menuRightService.exportMenuRightList(menuRight);
            ExcelUtil.setExportResultResponse((String) resultMap.get(AraDomainConstants.DEFAULT_RESULT_CODE_NAME));
        } catch (Exception e) {
            LOGGER.error("导出异常:{}", e);
            ExcelUtil.setExportResultResponse(AraResultCodeConstants.CODE_1602);
        }
    }

    @RequiresPermissions(value = {"1004:save"})
    @SysLogAnnotation("新增菜单权限")
    @UserLogAnnotation(module = "菜单权限", actType = "新增")
    @ApiOperation(value = "新增菜单权限", notes = "新增菜单权限")
    @RequestMapping(value = "insertMenuRight", method = RequestMethod.POST)
    public Map insertMenuRight(@RequestBody MenuRight menuRight) {
        if (StringUtil.isBlank(menuRight.getfMainMenuItemID())) {
            LOGGER.warn("MainMenuItemID为空!");
            return Result.error(AraResultCodeConstants.CODE_1001);
        }
        return menuRightService.insertMenuRight(menuRight);
    }

    @RequiresPermissions(value = {"1004:update"})
    @SysLogAnnotation("修改菜单权限")
    @UserLogAnnotation(module = "菜单权限", actType = "修改")
    @ApiOperation(value = "修改菜单权限", notes = "修改菜单权限")
    @RequestMapping(value = "updateMenuRight", method = RequestMethod.POST)
    public Map updateMenuRight(@RequestBody MenuRight menuRight) {
        if (StringUtil.isBlank(menuRight.getfID())) {
            LOGGER.warn("ID为空!");
            return Result.error(AraResultCodeConstants.CODE_1001);
        }
        return menuRightService.updateMenuRight(menuRight);
    }

    @RequiresPermissions(value = {"1004:delete"})
    @SysLogAnnotation("删除菜单权限")
    @UserLogAnnotation(module = "菜单权限", actType = "删除")
    @ApiOperation(value = "删除菜单权限", notes = "删除菜单权限")
    @RequestMapping(value = "deleteMenuRightBatch", method = RequestMethod.DELETE)
    public Map deleteMenuRightBatch(@RequestBody List<String> idList) {
        if (idList == null || idList.size() == 0) {
            LOGGER.warn("ID为空!");
            return Result.error(AraResultCodeConstants.CODE_1001);
        }
        return menuRightService.deleteMenuRightBatch(idList);
    }
}
