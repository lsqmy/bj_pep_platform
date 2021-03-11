package com.aratek.framework.core.service;


import com.aratek.framework.domain.core.*;

import java.util.List;
import java.util.Map;

/**
 * @author shijinlong
 * @date 2018-05-09
 * @description 角色服务 接口类
 */
public interface RoleService {

    /**
     * 根据ID查询角色
     *
     * @param fID
     * @return
     */
    Role selectRoleByID(String fID);

    /**
     * 根据角色名查询角色
     *
     * @param fName
     * @return
     */
    Role selectRoleByName(String fName);

    /**
     * 分页查询角色列表
     *
     * @param role
     * @return
     */
    List<Role> selectRoleList(Role role);

    /**
     * 导出角色列表
     *
     * @param role
     * @return
     */
    Map exportRoleList(Role role);

    /**
     * 新增角色
     *
     * @param role
     * @return
     */
    Map insertRole(Role role);

    /**
     * 修改角色信息
     *
     * @param role
     * @return
     */
    Map updateRoleInfo(Role role);

    /**
     * 修改角色状态
     *
     * @param role
     * @return
     */
    Map updateRoleStatus(Role role);

    Map updateRoleStatusBatch(List<Role> roleList);

    /**
     * 删除角色
     *
     * @param role
     * @return
     */
    Map deleteRoleByID(Role role);

    /**
     * 批量删除角色
     *
     * @param idList
     * @return
     */
    Map deleteRoleBatch(List<String> idList);

    /**
     * 分页查询角色权限列表
     *
     * @param roleRight
     * @return
     */
    List<RoleRight> selectRoleRightList(RoleRight roleRight);

    /**
     * 批量新增角色权限
     *
     * @param roleRightVO
     * @return
     */
    Map insertRoleRightBatch(RoleRightVO roleRightVO);

    /**
     * 批量删除角色权限
     *
     * @param idList
     * @return
     */
    Map deleteRoleRightBatch(List<String> idList);

    /**
     * 批量新增角色下的用户
     *
     * @param roleUserVO
     * @return
     */
    Map insertRoleUserBatch(RoleUserVO roleUserVO);

    /**
     * 批量删除角色下的用户
     *
     * @param roleUserVO
     * @return
     */
    Map deleteRoleUserBatch(RoleUserVO roleUserVO);

    List<Role> selectUserRoleList(String userID);

    Map insertUserRoleBatch(UserRoleVO userRoleVO);

    Map deleteUserRoleBatch(UserRoleVO userRoleVO);

}
