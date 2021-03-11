package com.aratek.framework.core.controller;

import com.aratek.framework.core.annotation.SysLogAnnotation;
import com.aratek.framework.core.annotation.UserLogAnnotation;
import com.aratek.framework.core.constant.AraResultCodeConstants;
import com.aratek.framework.core.service.RoleService;
import com.aratek.framework.core.service.UserService;
import com.aratek.framework.core.util.StringUtil;
import com.aratek.framework.domain.base.Result;
import com.aratek.framework.domain.base.controller.BaseController;
import com.aratek.framework.domain.core.RoleUserVO;
import com.aratek.framework.domain.core.UserRoleVO;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author shijinlong
 * @date 2018-06-08
 * @description 用户与角色操作
 */
@Api(tags = "用户与角色接口", description = "用户与角色相关接口")
@RestController
@RequestMapping("user/role")
public class UserRoleController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserRoleController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @RequiresPermissions(value = {"1003:bind"})
    @SysLogAnnotation("查询角色下的用户")
//    @UserLogAnnotation(module = "userRole", actType = "select")
    @ApiOperation(value = "查询角色下的用户", notes = "查询角色下的用户")
    @RequestMapping(value = "selectRoleUserList", method = RequestMethod.POST)
    public Map selectRoleUserList(@RequestParam(value = "roleID", required = false) String roleID) {
        PageInfo pageInfo = new PageInfo(userService.selectRoleUserList(roleID));
        return Result.ok().putData(pageInfo);
    }

    @RequiresPermissions(value = {"1003:bind"})
    @SysLogAnnotation("批量新增角色下的用户")
    @UserLogAnnotation(module = "角色用户", actType = "新增")
    @ApiOperation(value = "批量新增角色下的用户", notes = "批量新增角色下的用户")
    @RequestMapping(value = "insertRoleUserBatch", method = RequestMethod.POST)
    public Map insertRoleUserBatch(@RequestBody RoleUserVO roleUserVO) {
        if (StringUtil.isBlank(roleUserVO.getRoleID())) {
            LOGGER.warn("ID为空!");
            return Result.error(AraResultCodeConstants.CODE_1001);
        }
        List<String> userIDList = roleUserVO.getUserIDList();
        if (userIDList == null || userIDList.size() == 0) {
            LOGGER.warn("参数校验未通过!");
            return Result.error(AraResultCodeConstants.CODE_1000);
        }
        return roleService.insertRoleUserBatch(roleUserVO);
    }

    @RequiresPermissions(value = {"1003:bind"})
    @SysLogAnnotation("批量删除角色下的用户")
    @UserLogAnnotation(module = "角色用户", actType = "删除")
    @ApiOperation(value = "批量删除角色下的用户", notes = "批量删除角色下的用户")
    @RequestMapping(value = "deleteRoleUserBatch", method = RequestMethod.DELETE)
    public Map deleteRoleUserBatch(@RequestBody RoleUserVO roleUserVO) {
        if (StringUtil.isBlank(roleUserVO.getRoleID())) {
            LOGGER.warn("ID为空!");
            return Result.error(AraResultCodeConstants.CODE_1001);
        }
        List<String> userIDList = roleUserVO.getUserIDList();
        if (userIDList == null || userIDList.size() == 0) {
            LOGGER.warn("参数校验未通过!");
            return Result.error(AraResultCodeConstants.CODE_1000);
        }
        return roleService.deleteRoleUserBatch(roleUserVO);
    }

    @RequiresPermissions(value = {"1002:bind"})
    @SysLogAnnotation("查询用户的角色")
//    @UserLogAnnotation(module = "userRole", actType = "select")
    @ApiOperation(value = "查询用户的角色", notes = "查询用户的角色")
    @RequestMapping(value = "selectUserRoleList", method = RequestMethod.POST)
    public Map selectUserRoleList(@RequestParam(value = "userID", required = false) String userID) {
        PageInfo pageInfo = new PageInfo(roleService.selectUserRoleList(userID));
        return Result.ok().putData(pageInfo);
    }

    @RequiresPermissions(value = {"1002:bind"})
    @SysLogAnnotation("批量新增用户的角色")
    @UserLogAnnotation(module = "用户角色", actType = "新增")
    @ApiOperation(value = "批量新增用户的角色", notes = "批量新增用户的角色")
    @RequestMapping(value = "insertUserRoleBatch", method = RequestMethod.POST)
    public Map insertUserRoleBatch(@RequestBody UserRoleVO userRoleVO) {
        if (StringUtil.isBlank(userRoleVO.getUserID())) {
            LOGGER.warn("ID为空!");
            return Result.error(AraResultCodeConstants.CODE_1001);
        }
        List<String> roleIDList = userRoleVO.getRoleIDList();
        if (roleIDList == null || roleIDList.size() == 0) {
            LOGGER.warn("参数校验未通过!");
            return Result.error(AraResultCodeConstants.CODE_1000);
        }
        return roleService.insertUserRoleBatch(userRoleVO);
    }

    @RequiresPermissions(value = {"1002:bind"})
    @SysLogAnnotation("批量删除用户的角色")
    @UserLogAnnotation(module = "用户角色", actType = "删除")
    @ApiOperation(value = "批量删除用户的角色", notes = "批量删除用户的角色")
    @RequestMapping(value = "deleteUserRoleBatch", method = RequestMethod.DELETE)
    public Map deleteUserRoleBatch(@RequestBody UserRoleVO userRoleVO) {
        if (StringUtil.isBlank(userRoleVO.getUserID())) {
            LOGGER.warn("ID为空!");
            return Result.error(AraResultCodeConstants.CODE_1001);
        }
        List<String> roleIDList = userRoleVO.getRoleIDList();
        if (roleIDList == null || roleIDList.size() == 0) {
            LOGGER.warn("参数校验未通过!");
            return Result.error(AraResultCodeConstants.CODE_1000);
        }
        return roleService.deleteUserRoleBatch(userRoleVO);
    }

}
