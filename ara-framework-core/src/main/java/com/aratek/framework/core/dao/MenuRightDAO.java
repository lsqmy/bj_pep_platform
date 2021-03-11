package com.aratek.framework.core.dao;

import com.aratek.framework.domain.base.mapper.BaseDAO;
import com.aratek.framework.domain.core.MenuRight;
import com.aratek.framework.domain.core.MenuRightTree;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * @author shijinlong
 * @date 2018-05-10
 * @description 菜单权限DAO
 */
public interface MenuRightDAO extends BaseDAO<MenuRight> {

    /**
     * @param menuRight
     * @return
     */
    List<MenuRight> selectMenuRightList(MenuRight menuRight);

    /**
     * 批量插入菜单权限
     *
     * @param menuRightList
     * @return
     */
    int insertMenuRightList(List<MenuRight> menuRightList);

    int updateMenuRight(MenuRight menuRight);


    /**
     * 批量删除菜单权限
     *
     * @param idList
     * @return
     */
    int deleteMenuRightBatch(List<String> idList);

    /**
     * 查找菜单权限引用
     *
     * @param idList
     * @return
     */
    int selectCountReference(List<String> idList);

    /**
     * 查询菜单权限树的list
     *
     * @return
     */
    Set<MenuRightTree> selectMenuRightListForTree();

    /**
     * 根据roleID查询菜单权限树的list
     *
     * @param roleID
     * @return
     */
    Set<MenuRightTree> selectMenuRightListForTreeByRoleID(@Param("roleID") String roleID);

    /**
     * 根据userID查询菜单权限树的list
     *
     * @param userID
     * @return
     */
    Set<MenuRightTree> selectMenuRightListForTreeByUserID(@Param("userID") String userID);
}
