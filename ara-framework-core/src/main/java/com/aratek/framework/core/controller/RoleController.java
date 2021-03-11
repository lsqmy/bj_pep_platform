package com.aratek.framework.core.controller;

import com.aratek.framework.core.annotation.SysLogAnnotation;
import com.aratek.framework.core.annotation.UserLogAnnotation;
import com.aratek.framework.core.constant.AraCoreConstants;
import com.aratek.framework.core.constant.AraResultCodeConstants;
import com.aratek.framework.core.service.RoleService;
import com.aratek.framework.core.util.CurrentUserUtil;
import com.aratek.framework.core.util.ExcelUtil;
import com.aratek.framework.core.util.StringUtil;
import com.aratek.framework.domain.base.Result;
import com.aratek.framework.domain.base.controller.BaseController;
import com.aratek.framework.domain.constant.AraDomainConstants;
import com.aratek.framework.domain.core.Role;
import com.aratek.framework.domain.core.RoleRight;
import com.aratek.framework.domain.core.RoleRightVO;
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
 * @date 2018-05-09
 * @description 角色操作
 */
@Api(tags = "角色接口", description = "角色增删改查相关接口")
@RestController
@RequestMapping("role")
public class RoleController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoleController.class);

    @Autowired
    private RoleService roleService;

    /*@UserLogAnnotation(module = "role", actType = "select")
    @ApiOperation(value = "查询角色", notes = "根据角色ID查询角色")
    @RequestMapping(value = "selectRoleByID", method = RequestMethod.GET)
    public Map selectRoleByID(@RequestParam(value = "fID") String roleID) {
        Role role = roleService.selectRoleByID(roleID);
        return Result.ok().putData(role);
    }*/

    @RequiresPermissions(value = {"1003:select"})
    @SysLogAnnotation("查询角色列表")
//    @UserLogAnnotation(module = "role", actType = "select")
    @ApiOperation(value = "查询角色列表", notes = "分页查询角色列表")
    @RequestMapping(value = "selectRoleList", method = RequestMethod.POST)
    public Map selectRoleList(@RequestBody Role role) {
        PageInfo pageInfo = new PageInfo(roleService.selectRoleList(role));
        return Result.ok().putData(pageInfo);
    }

    @RequiresPermissions(value = {"1003:export"})
    @SysLogAnnotation("导出角色列表")
    @UserLogAnnotation(module = "角色管理", actType = "导出")
    @ApiOperation(value = "导出角色列表", notes = "导出角色列表")
    @RequestMapping(value = "exportRoleList", method = RequestMethod.POST)
    public void exportRoleList(@RequestBody Role role) {
        try {
            //设置允许接收的响应头
            ExcelUtil.setResponseExposeHeaders();
            Map resultMap = roleService.exportRoleList(role);
            ExcelUtil.setExportResultResponse((String) resultMap.get(AraDomainConstants.DEFAULT_RESULT_CODE_NAME));
        } catch (Exception e) {
            LOGGER.error("导出异常:{}", e);
            ExcelUtil.setExportResultResponse(AraResultCodeConstants.CODE_1602);
        }
    }

    @RequiresPermissions(value = {"1003:save"})
    @SysLogAnnotation("新增角色")
    @UserLogAnnotation(module = "角色管理", actType = "新增")
    @ApiOperation(value = "新增角色", notes = "新增角色,UUID由帮助类UUIDUtil.genID()统一格式生成")
    @RequestMapping(value = "insertRole", method = RequestMethod.POST)
    public Map insertRole(@RequestBody Role role) {
        if (StringUtil.isBlank(role.getfName())) {
            LOGGER.warn("角色名为空!");
            return Result.error(AraResultCodeConstants.CODE_1102);
        }
        if (StringUtil.isBlank(role.getfNumber())) {
            LOGGER.warn("number为空!");
            return Result.error(AraResultCodeConstants.CODE_1007);
        }
        return roleService.insertRole(role);
    }

    @RequiresPermissions(value = {"1003:update"})
    @SysLogAnnotation("修改角色")
    @UserLogAnnotation(module = "角色管理", actType = "修改")
    @ApiOperation(value = "修改角色", notes = "根据ID修改角色信息")
    @RequestMapping(value = "updateRoleInfo", method = RequestMethod.POST)
    public Map updateRoleInfo(@RequestBody Role role) {
        if (StringUtil.isBlank(role.getfID())) {
            LOGGER.warn("角色ID为空!");
            return Result.error(AraResultCodeConstants.CODE_1001);
        }
        return roleService.updateRoleInfo(role);
    }

    @RequiresPermissions(value = {"1003:enable"})
    @SysLogAnnotation("批量启用角色")
    @UserLogAnnotation(module = "角色管理", actType = "启用")
    @ApiOperation(value = "批量启用角色", notes = "批量启用角色")
    @RequestMapping(value = "updateRoleToEnableBatch", method = RequestMethod.POST)
    public Map updateRoleToEnableBatch(@RequestBody List<String> idList) {
        if (idList == null || idList.size() == 0) {
            LOGGER.warn("角色ID为空!");
            return Result.error(AraResultCodeConstants.CODE_1001);
        }
        List<Role> roleList = new ArrayList<Role>();
        String currentUserID = CurrentUserUtil.getCurrentUserID();
        Date now = new Date();
        for (String roleID : idList) {
            Role role = new Role();
            role.setfID(roleID);
            role.setfStatus(AraCoreConstants.STATUS_ENABLE);
            role.setCurrentUserID(currentUserID);
            role.setCurrentUserOperationTime(now);
            roleList.add(role);
        }
        return roleService.updateRoleStatusBatch(roleList);
    }

    @RequiresPermissions(value = {"1003:disable"})
    @SysLogAnnotation("批量禁用角色")
    @UserLogAnnotation(module = "角色管理", actType = "禁用")
    @ApiOperation(value = "批量禁用角色", notes = "批量禁用角色")
    @RequestMapping(value = "updateRoleToDisableBatch", method = RequestMethod.POST)
    public Map updateRoleToDisableBatch(@RequestBody List<String> idList) {
        if (idList == null || idList.size() == 0) {
            LOGGER.warn("角色ID为空!");
            return Result.error(AraResultCodeConstants.CODE_1001);
        }
        List<Role> roleList = new ArrayList<Role>();
        String currentUserID = CurrentUserUtil.getCurrentUserID();
        Date now = new Date();
        for (String roleID : idList) {
            Role role = new Role();
            role.setfID(roleID);
            role.setfStatus(AraCoreConstants.STATUS_DISABLE);
            role.setCurrentUserID(currentUserID);
            role.setCurrentUserOperationTime(now);
            roleList.add(role);
        }
        return roleService.updateRoleStatusBatch(roleList);
    }

    /*@UserLogAnnotation(module = "role", actType = "delete")
    @ApiOperation(value = "删除角色", notes = "根据ID删除角色")
    @RequestMapping(value = "deleteRoleByID", method = RequestMethod.DELETE)
    public Map deleteRoleByID(@RequestBody Role role) {
        if (StringUtil.isBlank(role.getfID())) {
            LOGGER.warn("角色ID为空!");
            return Result.error(AraResultCodeConstants.CODE_1001);
        }
        return roleService.deleteRoleByID(role);
    }*/

    @RequiresPermissions(value = {"1003:delete"})
    @SysLogAnnotation("批量删除角色")
    @UserLogAnnotation(module = "角色管理", actType = "删除")
    @ApiOperation(value = "批量删除角色", notes = "批量删除角色")
    @RequestMapping(value = "deleteRoleBatch", method = RequestMethod.DELETE)
    public Map deleteRoleBatch(@RequestBody List<String> idList) {
        if (idList == null || idList.size() == 0) {
            LOGGER.warn("角色ID为空!");
            return Result.ok(AraResultCodeConstants.CODE_1001);
        }
        return roleService.deleteRoleBatch(idList);
    }

    /*@UserLogAnnotation(module = "roleRight", actType = "select")
    @ApiOperation(value = "分页查询角色权限列表", notes = "分页查询角色权限列表")
    @RequestMapping(value = "right/selectRoleRightListByRoleID", method = RequestMethod.POST)
    public Map selectRoleRightListByRoleID(@RequestBody RoleRight roleRight) {
        if (StringUtil.isBlank(roleRight.getfRoleID())) {
            LOGGER.warn("角色ID为空!");
            return Result.error(AraResultCodeConstants.CODE_1001);
        }
        PageInfo pageInfo = new PageInfo(roleService.selectRoleRightList(roleRight));
        return Result.ok().putData(pageInfo);
    }*/

    @RequiresPermissions(value = {"1003:right"})
    @SysLogAnnotation("设置角色权限")
    @UserLogAnnotation(module = "角色管理", actType = "设置权限")
    @ApiOperation(value = "设置角色权限", notes = "设置角色权限")
    @RequestMapping(value = "right/insertRoleRightBatch", method = RequestMethod.POST)
    public Map insertRoleRightBatch(@RequestBody RoleRightVO roleRightVO) {
        if (StringUtil.isBlank(roleRightVO.getRoleID())) {
            LOGGER.warn("ID为空!");
            return Result.error(AraResultCodeConstants.CODE_1001);
        }
        List<RoleRight> roleRightList = roleRightVO.getRoleRightList();
        if (roleRightList != null && roleRightList.size() > 0) {
            for (RoleRight roleRight : roleRightList) {
                if (StringUtil.isBlank(roleRight.getfMainMenuItemID())
                        || StringUtil.isBlank(roleRight.getfRightID())) {
                    LOGGER.warn("参数校验未通过!");
                    return Result.error(AraResultCodeConstants.CODE_1000);
                }
            }
        }
        return roleService.insertRoleRightBatch(roleRightVO);
    }

    /*@UserLogAnnotation(module = "roleRight", actType = "delete")
    @ApiOperation(value = "批量删除角色权限", notes = "批量删除角色权限")
    @RequestMapping(value = "right/deleteRoleRightBatch", method = RequestMethod.DELETE)
    public Map deleteRoleRightBatch(@RequestBody List<String> idList) {
        if (idList == null || idList.size() == 0) {
            LOGGER.warn("ID为空!");
            return Result.error(AraResultCodeConstants.CODE_1001);
        }
        return roleService.deleteRoleRightBatch(idList);
    }*/


}
