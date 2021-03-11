package com.aratek.framework.core.controller;

import com.aratek.framework.core.annotation.SysLogAnnotation;
import com.aratek.framework.core.annotation.UserLogAnnotation;
import com.aratek.framework.core.constant.AraResultCodeConstants;
import com.aratek.framework.core.service.MenuService;
import com.aratek.framework.core.util.CurrentUserUtil;
import com.aratek.framework.core.util.ExcelUtil;
import com.aratek.framework.core.util.MenuUtil;
import com.aratek.framework.core.util.StringUtil;
import com.aratek.framework.domain.base.Result;
import com.aratek.framework.domain.base.controller.BaseController;
import com.aratek.framework.domain.base.tree.TreeParser;
import com.aratek.framework.domain.constant.AraDomainConstants;
import com.aratek.framework.domain.core.Menu;
import com.aratek.framework.domain.core.MenuTree;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author shijinlong
 * @date 2018-05-10
 * @description 菜单服务
 */
@Api(tags = "菜单服务接口", description = "菜单服务相关接口")
@RequestMapping("menu")
@RestController
public class MenuController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MenuController.class);

    @Autowired
    private MenuService menuService;

    @SysLogAnnotation("查询当前用户有权限的菜单树")
//    @UserLogAnnotation(module = "menu", actType = "select")
    @ApiOperation(value = "查询当前用户有权限的菜单树", notes = "查询当前用户有权限的菜单树")
    @RequestMapping(value = "selectMenuTreeByCurrentUser", method = RequestMethod.GET)
    public Map selectMenuTreeByCurrentUser() {
        List<MenuTree> menuTreeList = TreeParser.getTreeList(null, menuService.selectMenuTreeListByUserID(CurrentUserUtil.getCurrentUserID()));
        //排序
        MenuUtil.sortMenu(menuTreeList);
        return Result.ok().putData(menuTreeList);
    }

    @RequiresPermissions(value = {"1001:select"})
    @SysLogAnnotation("查询菜单树")
//    @UserLogAnnotation(module = "menu", actType = "select")
    @ApiOperation(value = "查询菜单树", notes = "查询菜单树")
    @RequestMapping(value = "selectMenuTreeList", method = RequestMethod.POST)
    public Map selectMenuTreeList(@RequestParam(value = "roleID", required = false) String roleID,
                                  @RequestParam(value = "userID", required = false) String userID) {
        List<MenuTree> menuTreeList = TreeParser.getTreeList(null, menuService.selectMenuTreeList(roleID, userID));
        //排序
        MenuUtil.sortMenu(menuTreeList);
        return Result.ok().putData(menuTreeList);
    }

    @RequiresPermissions(value = {"1002:right","1003:right"},logical = Logical.OR)
    @SysLogAnnotation("查询菜单树")
//    @UserLogAnnotation(module = "menu", actType = "select")
    @ApiOperation(value = "查询菜单树", notes = "查询菜单树")
    @RequestMapping(value = "selectMenuTreeListToRight", method = RequestMethod.POST)
    public Map selectMenuTreeListToRight(@RequestParam(value = "roleID", required = false) String roleID,
                                         @RequestParam(value = "userID", required = false) String userID) {
        List<MenuTree> menuTreeList = TreeParser.getTreeList(null, menuService.selectMenuTreeList(roleID, userID));
        //排序
        MenuUtil.sortMenu(menuTreeList);
        return Result.ok().putData(menuTreeList);
    }

    /*@UserLogAnnotation(module = "menu", actType = "select")
    @ApiOperation(value = "查询当前用户有权限的菜单列表", notes = "查询当前用户有权限的菜单列表")
    @RequestMapping(value = "selectMenuListByCurrentUser", method = RequestMethod.GET)
    public Map selectMenuListByCurrentUser() {
//        List<Menu> menus = menuService.selectMenuTreeListByUserID(CurrentUserUtil.getCurrentUserID());
        return Result.ok().putData(menuService.selectMenuTreeListByUserID(CurrentUserUtil.getCurrentUserID()));
    }*/

    @RequiresPermissions(value = {"1001:select"})
    @SysLogAnnotation("分页查询菜单列表")
//    @UserLogAnnotation(module = "menu", actType = "select")
    @ApiOperation(value = "分页查询菜单列表", notes = "分页查询菜单列表;如要查询所有菜单，传入pageSize为0;如有传入菜单ID则会分页查询该菜单及其下一级菜单")
    @RequestMapping(value = "selectMenuList", method = RequestMethod.POST)
    public Map selectMenuList(@RequestBody Menu menu) {
        PageInfo pageInfo = new PageInfo(menuService.selectMenuList(menu));
        return Result.ok().putData(pageInfo);
    }

    @RequiresPermissions(value = {"1001:export"})
    @SysLogAnnotation("导出菜单列表")
    @UserLogAnnotation(module = "菜单", actType = "导出")
    @ApiOperation(value = "导出菜单列表", notes = "导出菜单列表")
    @RequestMapping(value = "exportMenuList", method = RequestMethod.POST)
    public void exportMenuList(@RequestBody Menu menu) {
        try {
            //设置允许接收的响应头
            ExcelUtil.setResponseExposeHeaders();
            Map resultMap = menuService.exportMenuList(menu);
            ExcelUtil.setExportResultResponse((String) resultMap.get(AraDomainConstants.DEFAULT_RESULT_CODE_NAME));
        } catch (Exception e) {
            LOGGER.error("导出异常:{}", e);
            ExcelUtil.setExportResultResponse(AraResultCodeConstants.CODE_1602);
        }
    }

    @RequiresPermissions(value = {"1001:save"})
    @SysLogAnnotation("新增菜单")
    @UserLogAnnotation(module = "菜单", actType = "新增")
    @ApiOperation(value = "新增菜单", notes = "新增菜单")
    @RequestMapping(value = "insertMenu", method = RequestMethod.POST)
    public Map insertMenu(@RequestBody Menu menu) {
        if (StringUtil.isBlank(menu.getfParentID())) {
            LOGGER.warn("父节点为空!");
            return Result.error(AraResultCodeConstants.CODE_1402);
        }
        if (StringUtil.isBlank(menu.getfNumber())) {
            LOGGER.warn("菜单编号为空!");
            return Result.error(AraResultCodeConstants.CODE_1007);
        }
        return menuService.insertMenu(menu);
    }

    @RequiresPermissions(value = {"1001:update"})
    @SysLogAnnotation("修改菜单")
    @UserLogAnnotation(module = "菜单", actType = "修改")
    @ApiOperation(value = "修改菜单", notes = "修改菜单")
    @RequestMapping(value = "updateMenu", method = RequestMethod.POST)
    public Map updateMenu(@RequestBody Menu menu) {
        if (StringUtil.isBlank(menu.getfID())) {
            LOGGER.warn("菜单ID为空!");
            return Result.error(AraResultCodeConstants.CODE_1001);
        }
        return menuService.updateMenu(menu);
    }

    @RequiresPermissions(value = {"1001:delete"})
    @SysLogAnnotation("删除菜单")
    @UserLogAnnotation(module = "菜单", actType = "删除")
    @ApiOperation(value = "删除菜单", notes = "删除菜单")
    @RequestMapping(value = "deleteMenu", method = RequestMethod.DELETE)
    public Map deleteMenu(@RequestBody Menu menu) {
        if (StringUtil.isBlank(menu.getfID())) {
            LOGGER.warn("菜单ID为空!");
            return Result.error(AraResultCodeConstants.CODE_1001);
        }
        return menuService.deleteMenuByID(menu.getfID());
    }

}
