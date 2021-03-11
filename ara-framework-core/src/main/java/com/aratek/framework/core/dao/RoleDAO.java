package com.aratek.framework.core.dao;

import com.aratek.framework.domain.base.mapper.BaseDAO;
import com.aratek.framework.domain.core.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author shijinlong
 * @date 2018-05-09
 * @description 角色DAO
 */
public interface RoleDAO extends BaseDAO<Role> {

    Role selectRoleByID(@Param("fID") String fID);

    Role selectRoleByName(@Param("fName") String fName);

    List<Role> selectRoleList(Role role);

    int updateRoleInfo(Role role);

    int updateRoleStatus(Role role);

    /**
     * 查询角色引用
     *
     * @param roleID
     * @return
     */
    int selectCountReference(@Param("roleID") String roleID);

    int updateRoleStatusBatch(List<Role> roleList);

    Role selectRoleByNumber(@Param("fNumber") String fNumber);

    List<Role> selectRoleListByUserID(@Param("userID") String userID);
}
