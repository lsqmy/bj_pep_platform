package com.aratek.framework.core.dao;

import com.aratek.framework.domain.base.mapper.BaseDAO;
import com.aratek.framework.domain.core.RoleUserVO;
import com.aratek.framework.domain.core.UserRole;
import com.aratek.framework.domain.core.UserRoleVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author shijinlong
 * @date 2018-05-09
 * @description 用户角色DAO
 */
public interface UserRoleDAO extends BaseDAO<UserRole> {


    int deleteRoleUserBatch(@Param("roleUserVO") RoleUserVO roleUserVO);

    int insertRoleUserBatch(List<UserRole> userRoleList);

    /**
     * 根据角色ID查询初始化的用户ID列表
     *
     * @param roleID
     * @return
     */
    List<String> selectInitUserIDListByRoleID(@Param("roleID") String roleID);

    int deleteUserRoleBatch(@Param("userRoleVO") UserRoleVO userRoleVO);

    List<String> selectInitRoleIDListByUserID(@Param("userID") String userID);
}
