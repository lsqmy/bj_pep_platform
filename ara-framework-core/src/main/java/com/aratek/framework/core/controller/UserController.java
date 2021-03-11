package com.aratek.framework.core.controller;

import com.aratek.framework.core.annotation.SysLogAnnotation;
import com.aratek.framework.core.annotation.UserLogAnnotation;
import com.aratek.framework.core.constant.AraCoreConstants;
import com.aratek.framework.core.constant.AraResultCodeConstants;
import com.aratek.framework.core.service.UserService;
import com.aratek.framework.core.util.CurrentUserUtil;
import com.aratek.framework.core.util.ExcelUtil;
import com.aratek.framework.core.util.StringUtil;
import com.aratek.framework.domain.base.Result;
import com.aratek.framework.domain.base.controller.BaseController;
import com.aratek.framework.domain.constant.AraDomainConstants;
import com.aratek.framework.domain.core.RightVO;
import com.aratek.framework.domain.core.User;
import com.aratek.framework.domain.core.UserRight;
import com.aratek.framework.domain.core.UserRightVO;
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
 * @date 2018-04-28
 * @description 用户操作
 */
@Api(tags = "用户接口", description = "用户增删改查相关接口")
@RestController
@RequestMapping("user")
public class UserController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @SysLogAnnotation("查询当前用户信息")
//    @UserLogAnnotation(module = "user", actType = "select")
    @ApiOperation(value = "查询当前用户信息", notes = "查询当前用户信息")
    @RequestMapping(value = "selectUserByID", method = RequestMethod.GET)
    public Map selectUserByID() {
        User user = userService.selectUserByID(CurrentUserUtil.getCurrentUserID());
        return Result.ok().putData(user);
    }

    @RequiresPermissions(value = {"1002:select"})
    @SysLogAnnotation("查询用户列表")
//    @UserLogAnnotation(module = "user", actType = "select")
    @ApiOperation(value = "查询用户列表", notes = "分页查询用户列表")
    @RequestMapping(value = "selectUserList", method = RequestMethod.POST)
    public Map selectUserList(@RequestBody User user) {
        PageInfo pageInfo = new PageInfo(userService.selectUserList(user));
        return Result.ok().putData(pageInfo);
    }

    @RequiresPermissions(value = {"1002:export"})
    @SysLogAnnotation("导出用户列表")
    @UserLogAnnotation(module = "用户管理", actType = "导出")
    @ApiOperation(value = "导出用户列表", notes = "导出用户列表", produces = "application/vnd.ms-excel;charset=UTF-8")
    @RequestMapping(value = "exportUserList", method = RequestMethod.POST)
    public void exportUserList(@RequestBody User user) {
        try {
            //设置允许接收的响应头
            ExcelUtil.setResponseExposeHeaders();
            Map resultMap = userService.exportUserList(user);
            ExcelUtil.setExportResultResponse((String) resultMap.get(AraDomainConstants.DEFAULT_RESULT_CODE_NAME));
        } catch (Exception e) {
            LOGGER.error("导出异常:{}", e);
            ExcelUtil.setExportResultResponse(AraResultCodeConstants.CODE_1602);
        }
    }

    @RequiresPermissions(value = {"1002:save"})
    @SysLogAnnotation("新增用户")
    @UserLogAnnotation(module = "用户管理", actType = "新增")
    @ApiOperation(value = "新增用户", notes = "新增用户,UUID由帮助类UUIDUtil.genID()统一格式生成")
    @RequestMapping(value = "insertUser", method = RequestMethod.POST)
    public Map insertUser(@RequestBody User user) {
        if (StringUtil.isBlank(user.getfName())) {
            LOGGER.warn("用户名为空!");
            return Result.error(AraResultCodeConstants.CODE_1002);
        }
        if (StringUtil.isBlank(user.getfNumber())) {
            LOGGER.warn("编号为空!");
            return Result.error(AraResultCodeConstants.CODE_1007);
        }
        return userService.insertUser(user);
    }

    @RequiresPermissions(value = {"1002:update"})
    @SysLogAnnotation("修改用户信息")
    @UserLogAnnotation(module = "用户管理", actType = "修改")
    @ApiOperation(value = "修改用户信息", notes = "根据ID修改用户信息")
    @RequestMapping(value = "updateUserInfo", method = RequestMethod.POST)
    public Map updateUserInfo(@RequestBody User user) {
        if (StringUtil.isBlank(user.getfID())) {
            LOGGER.warn("用户ID为空!");
            return Result.error(AraResultCodeConstants.CODE_1001);
        }
        return userService.updateUserInfo(user);
    }

    @UserLogAnnotation(module = "user", actType = "update")
    @SysLogAnnotation("修改当前用户信息")
    @ApiOperation(value = "修改当前用户信息", notes = "修改当前用户信息")
    @RequestMapping(value = "updateCurrentUserInfo", method = RequestMethod.POST)
    public Map updateCurrentUserInfo(@RequestBody User user) {
        return userService.updateCurrentUserInfo(user);
    }

    @RequiresPermissions(value = {"1002:enable"})
    @SysLogAnnotation("批量启用用户")
    @UserLogAnnotation(module = "用户管理", actType = "启用")
    @ApiOperation(value = "批量启用用户", notes = "批量启用用户")
    @RequestMapping(value = "updateUserToEnableBatch", method = RequestMethod.POST)
    public Map updateUserStatusBatch(@RequestBody List<String> userIDs) {
        if (userIDs == null || userIDs.size() == 0) {
            return Result.error(AraResultCodeConstants.CODE_1001);
        }
        List<User> userList = new ArrayList<User>();
        String currentUserID = CurrentUserUtil.getCurrentUserID();
        Date now = new Date();
        for (String userID : userIDs) {
            User user = new User();
            user.setfID(userID);
            user.setfStatus(AraCoreConstants.STATUS_ENABLE);
            user.setCurrentUserID(currentUserID);
            user.setCurrentUserOperationTime(now);
            userList.add(user);
        }
        return userService.updateUserStatusBatch(userList);
    }

    @RequiresPermissions(value = {"1002:disable"})
    @SysLogAnnotation("批量禁用用户")
    @UserLogAnnotation(module = "用户管理", actType = "禁用")
    @ApiOperation(value = "批量禁用用户", notes = "批量禁用用户")
    @RequestMapping(value = "updateUserToDisableBatch", method = RequestMethod.POST)
    public Map updateUserToDisableBatch(@RequestBody List<String> userIDs) {
        if (userIDs == null || userIDs.size() == 0) {
            return Result.error(AraResultCodeConstants.CODE_1001);
        }
        List<User> userList = new ArrayList<User>();
        String currentUserID = CurrentUserUtil.getCurrentUserID();
        Date now = new Date();
        for (String userID : userIDs) {
            User user = new User();
            user.setfID(userID);
            user.setfStatus(AraCoreConstants.STATUS_DISABLE);
            user.setCurrentUserID(currentUserID);
            user.setCurrentUserOperationTime(now);
            userList.add(user);
        }
        return userService.updateUserStatusBatch(userList);
    }

    @SysLogAnnotation("修改用户密码")
    @UserLogAnnotation(module = "用户管理", actType = "修改密码")
    @ApiOperation(value = "修改用户密码", notes = "修改用户密码")
    @RequestMapping(value = "updateUserPassword", method = RequestMethod.POST)
    public Map updateUserPassword(@RequestBody User user) {
        if (StringUtil.isBlank(user.getfName())) {
            return Result.error(AraResultCodeConstants.CODE_1002);
        }
        if (StringUtil.isBlank(user.getfPassWord())) {
            return Result.error(AraResultCodeConstants.CODE_1009);
        }
        if (StringUtil.isBlank(user.getOldPassWord())) {
            return Result.error(AraResultCodeConstants.CODE_1009);
        }
        return userService.updateUserPassword(user);
    }

    /*@UserLogAnnotation(module = "user", actType = "delete")
    @ApiOperation(value = "删除用户", notes = "根据ID删除用户")
    @RequestMapping(value = "deleteUserByID", method = RequestMethod.DELETE)
    public Map deleteUserByID(@RequestBody User user) {
        if (StringUtil.isBlank(user.getfID())) {
            LOGGER.warn("用户ID为空!");
            return Result.error(AraResultCodeConstants.CODE_1001);
        }
        return userService.deleteUserByID(user.getfID());
    }*/

    @RequiresPermissions(value = {"1002:delete"})
    @SysLogAnnotation("批量删除用户")
    @UserLogAnnotation(module = "用户管理", actType = "删除")
    @ApiOperation(value = "批量删除用户", notes = "批量根据ID删除用户")
    @RequestMapping(value = "deleteUserBatch", method = RequestMethod.DELETE)
    public Map deleteUserBatch(@RequestBody List<String> userIDs) {
        if (userIDs == null || userIDs.size() == 0) {
            return Result.error(AraResultCodeConstants.CODE_1001);
        }
        return userService.deleteUserBatch(userIDs);
    }

    @RequiresPermissions(value = {"1002:right"})
    @SysLogAnnotation("设置用户权限")
    @UserLogAnnotation(module = "用户管理", actType = "设置权限")
    @ApiOperation(value = "设置用户权限", notes = "设置用户权限")
    @RequestMapping(value = "right/insertUserRightBatch", method = RequestMethod.POST)
    public Map insertUserRightBatch(@RequestBody UserRightVO userRightVO) {
        if (StringUtil.isBlank(userRightVO.getUserID())) {
            LOGGER.warn("ID为空!");
            return Result.error(AraResultCodeConstants.CODE_1001);
        }
        List<UserRight> userRightList = userRightVO.getUserRightList();
        if (userRightList != null && userRightList.size() > 0) {
            for (UserRight userRight : userRightList) {
                if (StringUtil.isBlank(userRight.getfMainMenuItemID())
                        || StringUtil.isBlank(userRight.getfRightID())) {
                    LOGGER.warn("参数校验未通过!");
                    return Result.error(AraResultCodeConstants.CODE_1000);
                }
            }
        }
        return userService.insertUserRightBatch(userRightVO);
    }

    /*@UserLogAnnotation(module = "userRight", actType = "delete")
    @ApiOperation(value = "批量删除用户权限", notes = "批量删除用户权限")
    @RequestMapping(value = "right/deleteUserRightBatch", method = RequestMethod.DELETE)
    public Map deleteUserRightBatch(@RequestBody List<String> idList) {
        if (idList == null || idList.size() == 0) {
            return Result.error(AraResultCodeConstants.CODE_1001);
        }
        return userService.deleteUserRightBatch(idList);
    }*/

    @RequiresPermissions(value = {"1020:select"})
    @SysLogAnnotation("查询用户权限列表")
//    @UserLogAnnotation(module = "userRight", actType = "select")
    @ApiOperation(value = "查询用户权限列表", notes = "查询用户权限列表")
    @RequestMapping(value = "right/selectUserRightListByRoleOrUser", method = RequestMethod.POST)
    public Map selectUserRightListByRoleOrUser(@RequestBody RightVO rightVO) {
        if (StringUtil.isBlank(rightVO.getRoleName()) && StringUtil.isBlank(rightVO.getUserName())) {
            LOGGER.warn("角色名和用户名不能同时为空!");
            return Result.error(AraResultCodeConstants.CODE_1012);
        }
        return userService.selectUserRightListByRoleOrUser(rightVO);
    }

    @RequiresPermissions(value = {"1020:export"})
    @SysLogAnnotation("导出用户权限列表")
    @UserLogAnnotation(module = "权限查询", actType = "导出")
    @ApiOperation(value = "导出用户权限列表", notes = "导出用户权限列表")
    @RequestMapping(value = "right/exportUserRightListByRoleOrUser", method = RequestMethod.POST)
    public void exportUserRightListByRoleOrUser(@RequestBody RightVO rightVO) {
        if (StringUtil.isBlank(rightVO.getRoleName()) && StringUtil.isBlank(rightVO.getUserName())) {
            LOGGER.warn("角色名和用户名不能同时为空!");
            //设置允许接收的响应头
            ExcelUtil.setResponseExposeHeaders();
            ExcelUtil.setExportResultResponse(AraResultCodeConstants.CODE_1602);
            return;
        }
        try {
            //设置允许接收的响应头
            ExcelUtil.setResponseExposeHeaders();
            Map resultMap = userService.exportUserRightListByRoleOrUser(rightVO);
            ExcelUtil.setExportResultResponse((String) resultMap.get(AraDomainConstants.DEFAULT_RESULT_CODE_NAME));
        } catch (Exception e) {
            LOGGER.error("导出异常:{}", e);
            ExcelUtil.setExportResultResponse(AraResultCodeConstants.CODE_1602);
        }
    }
}
