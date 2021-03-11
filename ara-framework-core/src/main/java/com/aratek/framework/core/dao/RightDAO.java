package com.aratek.framework.core.dao;

import com.aratek.framework.domain.core.RightVO;

import java.util.List;

/**
 * @author shijinlong
 * @date 2018-06-21
 * @description 权限DAO
 */
public interface RightDAO {

    /**
     * 查询用户权限列表
     *
     * @param rightVO
     * @return
     */
    List<RightVO> selectUserRightListByRoleName(RightVO rightVO);

    /**
     * 查询用户权限列表
     *
     * @param rightVO
     * @return
     */
    List<RightVO> selectUserRightListByUserName(RightVO rightVO);

}
