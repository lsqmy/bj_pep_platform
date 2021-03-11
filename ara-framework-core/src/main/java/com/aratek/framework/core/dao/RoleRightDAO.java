package com.aratek.framework.core.dao;

import com.aratek.framework.domain.base.mapper.BaseDAO;
import com.aratek.framework.domain.core.RoleRight;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author shijinlong
 * @date 2018-05-10
 * @description 角色权限DAO
 */
public interface RoleRightDAO extends BaseDAO<RoleRight> {


    List<RoleRight> selectRoleRightList(RoleRight roleRight);

    int insertRoleRightBatch(List<RoleRight> roleRightList);

    int deleteRoleRightByRoleID(@Param("roleID") String roleID);

    int deleteRoleRightBatch(List<String> idList);

    List<String> selectInitRightIDListByRoleID(@Param("roleID") String roleID);
}
