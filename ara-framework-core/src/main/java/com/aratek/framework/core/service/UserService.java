package com.aratek.framework.core.service;


import com.aratek.framework.domain.core.RightVO;
import com.aratek.framework.domain.core.User;
import com.aratek.framework.domain.core.UserRightVO;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author shijinlong
 * @date 2018-04-28
 * @description 用户服务 接口类
 */
public interface UserService {

    /**
     * 根据ID查询用户
     *
     * @param fID
     * @return
     */
    User selectUserByID(String fID);

    /**
     * 根据用户名查询用户
     *
     * @param fName
     * @return
     */
    User selectUserByName(String fName);

    /**
     * 分页查询用户列表
     *
     * @param user
     * @return
     */
    List<User> selectUserList(User user);

    /**
     * 导出用户列表
     *
     * @param user
     * @return
     */
    Map exportUserList(User user);

    /**
     * 新增用户
     *
     * @param user
     * @return
     */
    Map insertUser(User user);

    /**
     * 修改用户信息
     *
     * @param user
     * @return
     */
    Map updateUserInfo(User user);

    /**
     * 修改用户状态
     *
     * @param user
     * @return
     */
    Map updateUserStatus(User user);

    /**
     * 批量修改用户状态
     *
     * @param userList
     * @return
     */
    Map updateUserStatusBatch(List<User> userList);

    /**
     * 删除用户
     *
     * @param userID
     * @return
     */
    Map deleteUserByID(String userID);

    /**
     * 批量删除用户
     *
     * @param userIDs
     * @return
     */
    Map deleteUserBatch(List<String> userIDs);

    /**
     * 根据用户ID查询用户拥有的权限list
     *
     * @param userID
     * @return
     */
    Set<String> selectRightListByUserID(String userID);

    /**
     * 根据用户ID查询用户拥有的角色list
     *
     * @param userID
     * @return
     */
    Set<String> selectRoleNameListByUserID(String userID);


    Map deleteUserRightBatch(List<String> idList);

    /**
     * 设置用户权限
     *
     * @param userRightVO
     * @return
     */
    Map insertUserRightBatch(UserRightVO userRightVO);

    /**
     * 查询角色下的用户,如果roleID为空则返回
     *
     * @param roleID
     * @return
     */
    List<User> selectRoleUserList(String roleID);

    /**
     * 修改用户密码
     *
     * @param user
     * @return
     */
    Map updateUserPassword(User user);

    /**
     * 修改当前用户信息
     *
     * @param user
     * @return
     */
    Map updateCurrentUserInfo(User user);

    /**
     * 查询用户权限列表
     *
     * @param rightVO
     * @return
     */
    Map selectUserRightListByRoleOrUser(RightVO rightVO);


    /**
     * 导出用户权限列表
     *
     * @param rightVO
     * @return
     */
    Map exportUserRightListByRoleOrUser(RightVO rightVO);
}
