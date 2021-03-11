package com.aratek.framework.core.dao;

import com.aratek.framework.domain.base.mapper.BaseDAO;
import com.aratek.framework.domain.core.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author shijinlong
 * @date 2018-04-28
 * @description 用户DAO
 */
public interface UserDAO extends BaseDAO<User> {

    User selectUserByID(@Param("fID") String fID);

    User selectUserByName(@Param("fName") String fName);

    User selectUserByNumber(@Param("fNumber") String fNumber);

    /**
     * 根据用户ID查询权限list
     *
     * @param userID
     * @return
     */
    Set<String> selectRightListByUserID(@Param("userID") String userID);

    /**
     * 根据用户ID查询角色Name list
     *
     * @param userID
     * @return
     */
    Set<String> selectRoleNameListByUserID(@Param("userID") String userID);

    /**
     * 根据用户ID查询角色ID list
     *
     * @param userID
     * @return
     */
    Set<String> selectRoleIDListByUserID(@Param("userID") String userID);

    List<User> selectUserList(User user);

    int updateUserInfo(User user);

    int updateUserLoginTime(User user);

    int updateUserStatus(User user);

    int updateUserStatusBatch(List<User> userList);

    /**
     * 根据用户ID查询用户引用
     *
     * @param userID
     * @return
     */
    int selectCountUserReferenceByUserID(@Param("userID") String userID);

    /**
     * 查询number是否已经存在
     *
     * @param fNumber
     * @return
     */
    int selectCountNumber(@Param("fNumber") String fNumber);

    List<User> selectUserListByRoleID(@Param("roleID") String roleID);

    /**
     * 根据ID更新用户密码
     *
     * @param userID
     * @param pwd
     * @return
     */
    int updateUserPasswordByID(@Param("userID") String userID, @Param("pwd") String pwd, @Param("fLastChangePswTime") Date fLastChangePswTime);

    /**
     * 修改当前用户信息
     * @param user
     * @return
     */
    int updateCurrentUserInfo(User user);

    /**
     * 更新输错密码信息
     * @param user
     * @return
     */
    int updateLoginError(User user);

}
