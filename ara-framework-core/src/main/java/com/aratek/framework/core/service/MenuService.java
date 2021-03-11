package com.aratek.framework.core.service;

import com.aratek.framework.domain.core.Menu;
import com.aratek.framework.domain.core.MenuTree;

import java.util.List;
import java.util.Map;

/**
 * @author shijinlong
 * @date 2018-05-10
 * @description 菜单服务 接口类
 */
public interface MenuService {

    /**
     * 查询用户有权限的菜单列表
     *
     * @param userID
     * @return
     */
    List<MenuTree> selectMenuTreeListByUserID(String userID);

    /**
     * 查询菜单列表
     *
     * @param menu
     * @return
     */
    List<Menu> selectMenuList(Menu menu);

    /**
     * 导出菜单列表
     *
     * @param menu
     * @return
     */
    Map exportMenuList(Menu menu);

    /**
     * 查询菜单+权限列表
     *
     * @param roleID
     * @param userID
     * @return
     */
    List<MenuTree> selectMenuTreeList(String roleID, String userID);

    /**
     * 新增菜单
     *
     * @param menu
     */
    Map insertMenu(Menu menu);

    /**
     * 修改菜单
     *
     * @param menu
     */
    Map updateMenu(Menu menu);

    /**
     * 删除菜单
     *
     * @param menuID 菜单ID
     */
    Map deleteMenuByID(String menuID);
}
