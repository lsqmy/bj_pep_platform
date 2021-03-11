package com.aratek.framework.core.service;

import com.aratek.framework.domain.core.MenuRight;
import com.aratek.framework.domain.core.MenuRightTree;

import java.util.List;
import java.util.Map;

/**
 * @author shijinlong
 * @date 2018-05-10
 * @description 菜单权限服务 接口类
 */
public interface MenuRightService {

    /**
     * 查询菜单的权限
     *
     * @param menuRight 权限
     * @return
     */
    List<MenuRight> selectMenuRightList(MenuRight menuRight);

    /**
     * 导出权限列表
     *
     * @param menuRight
     * @return
     */
    Map exportMenuRightList(MenuRight menuRight);

    /**
     * 新增菜单权限
     *
     * @param menuRight 权限
     */
    Map insertMenuRight(MenuRight menuRight);

    /**
     * 修改菜单权限
     *
     * @param menuRight 权限
     */
    Map updateMenuRight(MenuRight menuRight);

    /**
     * 批量删除菜单权限
     *
     * @param idList
     * @return
     */
    Map deleteMenuRightBatch(List<String> idList);

    /**
     * 查询菜单权限列表
     *
     * @param roleID
     * @param userID
     * @return
     */
    List<MenuRightTree> selectMenuRightListForTree(String roleID, String userID);
}
