package com.aratek.framework.core.dao;

import com.aratek.framework.domain.base.mapper.BaseDAO;
import com.aratek.framework.domain.core.RoleRight;
import com.aratek.framework.domain.core.UserRight;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author shijinlong
 * @date 2018-05-10
 * @description 用户权限DAO
 */
public interface UserRightDAO extends BaseDAO<UserRight> {

    List<RoleRight> selectUserRightList(UserRight userRight);

    int insertUserRightBatch(List<UserRight> userRightList);

    int deleteUserRightBatch(List<String> idList);

    int deleteUserRightByUserID(@Param("userID") String userID);

    List<String> selectInitRightIDListByUserID(@Param("userID") String userID);
}
