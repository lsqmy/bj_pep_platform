package com.aratek.framework.core.dao;

import com.aratek.framework.domain.base.mapper.BaseDAO;
import com.aratek.framework.domain.core.Menu;
import com.aratek.framework.domain.core.MenuTree;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * @author shijinlong
 * @date 2018-05-10
 * @description 菜单DAO
 */
public interface MenuDAO extends BaseDAO<Menu> {

    List<Menu> selectMenulistByUserID(@Param("fID") String fID);

    /**
     * 如果菜单ID不为空：查询该菜单及下一级菜单；
     * 其他：查询菜单列表，排除root节点
     *
     * @param menu
     * @return
     */
    List<Menu> selectMenuList(Menu menu);

    /**
     * 查询菜单+权限列表
     *
     * @return
     */
    List<MenuTree> selectMenuTreeList();

    /**
     * 查询根目录的权限list
     *
     * @return
     */
    Set<String> selectRootMenuRightIDList();

    /**
     * 查询菜单的引用
     *
     * @param menuID
     * @return
     */
    int selectCountReference(@Param("menuID") String menuID);

    Menu selectMenuByNumber(@Param("fNumber") String fNumber);

    int updateMenuInfo(Menu menu);


    int deleteMenuByID(@Param("menuID") String menuID);
}
