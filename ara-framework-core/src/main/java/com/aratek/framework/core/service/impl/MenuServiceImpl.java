package com.aratek.framework.core.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import com.aratek.framework.core.constant.AraCoreConstants;
import com.aratek.framework.core.constant.AraResultCodeConstants;
import com.aratek.framework.core.dao.MenuDAO;
import com.aratek.framework.core.dao.RoleRightDAO;
import com.aratek.framework.core.dao.UserDAO;
import com.aratek.framework.core.dao.UserRightDAO;
import com.aratek.framework.core.service.MenuService;
import com.aratek.framework.core.util.*;
import com.aratek.framework.domain.base.Result;
import com.aratek.framework.domain.core.Menu;
import com.aratek.framework.domain.core.MenuTree;
import com.aratek.framework.domain.core.RoleRight;
import com.aratek.framework.domain.core.UserRight;
import com.github.pagehelper.PageHelper;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * @author shijinlong
 * @date 2018-05-10
 * @description 菜单服务 实现类
 */
@Service
public class MenuServiceImpl implements MenuService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MenuServiceImpl.class);

    @Autowired
    private MenuDAO menuDAO;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private UserRightDAO userRightDAO;

    @Autowired
    private RoleRightDAO roleRightDAO;

    @Override
    public List<MenuTree> selectMenuTreeListByUserID(String userID) {
        List<MenuTree> menuTreeList = menuDAO.selectMenuTreeList();
        //list to map
        Map<String, MenuTree> menuTreeMap = MenuUtil.list2Map(menuTreeList);
        //按照用户ID来标记菜单
        markRoleOrUserRight(menuTreeMap, null, userID);
        Set<String> roleIDList = userDAO.selectRoleIDListByUserID(userID);
        for (String roleID : roleIDList) {
            //按照角色ID来标记菜单
            markRoleOrUserRight(menuTreeMap, roleID, null);
        }
        //map to list
        menuTreeList = MenuUtil.map2List(menuTreeMap);
        return menuTreeList;
    }

    @Override
    public List<Menu> selectMenuList(Menu menu) {
        //如果菜单ID不为空：查询该菜单及下一级菜单；其他：查询菜单列表，排除root节点
        PageHelper.startPage(menu.getPageNum(), menu.getPageSize(), PageUtil.getSortStr(menu.getSortParams()));
        return menuDAO.selectMenuList(menu);
    }

    @Override
    public Map exportMenuList(Menu menu) {
        //1.查询数据,是否查全部数据由前台传参控制
        //如果菜单ID不为空：查询该菜单及下一级菜单；其他：查询菜单列表，排除root节点
        PageHelper.startPage(menu.getPageNum(), menu.getPageSize(), PageUtil.getSortStr(menu.getSortParams()));
        //如果pageSize为0，需设置orderByOnly为true
        if (menu.getPageSize() == 0) {
            PageHelper.getLocalPage().setOrderByOnly(true);
        }
        List<Menu> menuList = menuDAO.selectMenuList(menu);
        if (menuList == null || menuList.size() == 0) {
            LOGGER.warn("没有数据!");
            return Result.error(AraResultCodeConstants.CODE_1601);
        }
        //2.设置导出配置
        // 获取导出excel指定模版
        TemplateExportParams params = new TemplateExportParams(AraCoreConstants.EXCEL_TEMPLATE_MENU);
        // 标题开始行
        params.setHeadingStartRow(0);
        // 标题行数
        params.setHeadingRows(1);
        // 设置sheetName，若不设置该参数，则使用原本sheet名称
        params.setSheetName(AraCoreConstants.MODULE_MENU);
        //3.模版--单sheet--导出
        Map<String, Object> map = new HashMap<String, Object>();
        List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();
        for (int i = 0; i < menuList.size(); i++) {
            Menu menu2 = menuList.get(i);
            Map<String, String> lm = new HashMap<String, String>();
            lm.put("num", i + 1 + "");
            lm.put("fNumber", menu2.getfNumber());
            lm.put("fName", menu2.getfName());
            lm.put("fParentName", menu2.getfParentName());
            lm.put("fLevel", String.valueOf(menu2.getfLevel()));
            lm.put("fLink", menu2.getfLink());
            lm.put("fIcon", menu2.getfIcon());
            lm.put("fDisplayorder", String.valueOf(menu2.getfDisplayorder()));
            lm.put("fCreatorName", menu2.getfCreatorName());
            lm.put("fCreateTime", DateUtil.formatDateTime(menu2.getfCreateTime()));
            lm.put("fLastUpdateUserName", menu2.getfLastUpdateUserName());
            lm.put("fLastUpdateTime", DateUtil.formatDateTime(menu2.getfLastUpdateTime()));
            lm.put("fDescription", menu2.getfDescription());
            //add to list
            listMap.add(lm);
        }
        map.put("mapList", listMap);
        Workbook workbook = ExcelExportUtil.exportExcel(params, map);
        if (workbook == null) {
            LOGGER.error("生成excel失败!");
            return Result.error(AraResultCodeConstants.CODE_1602);
        }
        //test
        /*File savefile = new File("D:/temp/excel/");
        if (!savefile.exists()) {
            savefile.mkdirs();
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream("D:/temp/excel/" + AraCoreConstants.MODULE_MENU + ".xlsx");
            workbook.write(fos);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }*/
        //4.指定下载的文件名--设置响应头
        HttpServletResponse response = HttpContextUtil.getHttpServletResponse();
        ExcelUtil.setExcelExportResponse(AraCoreConstants.MODULE_MENU, response);
        //5.写出数据输出流到页面
        BufferedOutputStream bufferedOutputStream = null;
        try {
            bufferedOutputStream = new BufferedOutputStream(response.getOutputStream());
            workbook.write(bufferedOutputStream);
            bufferedOutputStream.flush();
            bufferedOutputStream.close();
        } catch (IOException e) {
            LOGGER.error("exportMenuList.error:{}", e);
            return Result.error(AraResultCodeConstants.CODE_1602);
        } finally {
            if (bufferedOutputStream != null) {
                try {
                    bufferedOutputStream.close();
                } catch (IOException e) {
                    LOGGER.error("exportMenuList.error:{}", e);
                    return Result.error(AraResultCodeConstants.CODE_1602);
                }
            }
        }
        return Result.ok();
    }

    @Override
    public List<MenuTree> selectMenuTreeList(String roleID, String userID) {
        List<MenuTree> menuTreeList = menuDAO.selectMenuTreeList();
        if (StringUtil.isNotBlank(roleID) || StringUtil.isNotBlank(userID)) {
            //list to map
            Map<String, MenuTree> menuTreeMap = MenuUtil.list2Map(menuTreeList);
            //按照角色或者用户来标记菜单
            markRoleOrUserRight(menuTreeMap, roleID, userID);
            //map to list
            menuTreeList = MenuUtil.map2List(menuTreeMap);
        }
        return menuTreeList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map insertMenu(Menu menu) {
        //1.校验编号是否已经存在
        if (StringUtil.isNotBlank(menu.getfNumber())) {
            Menu dbMenu = menuDAO.selectMenuByNumber(menu.getfNumber());
            if (dbMenu != null) {
                LOGGER.debug("编号已存在");
                return Result.error(AraResultCodeConstants.CODE_1004);
            }
        }
        //2.校验父节点是否存在
        if (StringUtil.isNotBlank(menu.getfParentID())) {
            Menu fatherMenu = menuDAO.selectByPrimaryKey(menu.getfParentID());
            if (fatherMenu == null) {
                LOGGER.debug("父节点不存在");
                return Result.error(AraResultCodeConstants.CODE_1201);
            }
        }
        //set id
        menu.setfID(UUIDUtil.genID());
        //3.处理操作人和操作时间
        String userID = CurrentUserUtil.getCurrentUserID();
        Date now = new Date();
        //默认非根节点
        menu.setfIsRoot(AraCoreConstants.NO);
        menu.setfCreatorID(userID);
        menu.setfCreateTime(now);
        menu.setfLastUpdateUserID(userID);
        menu.setfLastUpdateTime(now);
        //非初始化数据
        menu.setfIsInit(AraCoreConstants.NO);
        //4.插入数据
        menuDAO.insertSelective(menu);
        return Result.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map updateMenu(Menu menu) {
        //1.校验number
        if (StringUtil.isNotBlank(menu.getfNumber())) {
            Menu dbMenu = menuDAO.selectMenuByNumber(menu.getfNumber());
            if (dbMenu != null) {
                if (!dbMenu.getfID().equals(menu.getfID())) {
                    LOGGER.debug("编号已存在");
                    return Result.error(AraResultCodeConstants.CODE_1004);
                }
            }
        }
        //2.校验父节点是否存在
        if (StringUtil.isNotBlank(menu.getfParentID())) {
            Menu fatherMenu = menuDAO.selectByPrimaryKey(menu.getfParentID());
            if (fatherMenu == null) {
                LOGGER.debug("父节点不存在");
                return Result.error(AraResultCodeConstants.CODE_1201);
            }
        }
        //3.处理操作人和操作时间
        CurrentUserUtil.setOperatorInfo(menu);
        //4.update
        menuDAO.updateMenuInfo(menu);
        return Result.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map deleteMenuByID(String menuID) {
//        //1.删除菜单对应的权限
//        Example menuRightExample = new Example(MenuRight.class);
//        menuRightExample.createCriteria().andEqualTo("fMainMenuItemID", menuID);
//        menuRightDAO.deleteByExample(menuRightExample);
//        //2.删除用户对应的权限
//        Example userRightExample = new Example(UserRight.class);
//        userRightExample.createCriteria().andEqualTo("fMainMenuItemID", menuID);
//        userRightDAO.deleteByExample(userRightExample);
//        //3.删除角色对应的权限
//        Example roleRightExample = new Example(RoleRight.class);
//        roleRightExample.createCriteria().andEqualTo("fMainMenuItemID", menuID);
//        roleRightDAO.deleteByExample(roleRightExample);
        //1.查询菜单引用
        if (menuDAO.selectCountReference(menuID) > 0) {
            //存在引用，不允许删除
            return Result.error(AraResultCodeConstants.CODE_1006);
        }
        //2.删除菜单
        menuDAO.deleteMenuByID(menuID);
        return Result.ok();
    }

    /**
     * 按照角色或者用户来标记菜单
     *
     * @param menuTreeMap
     * @param roleID
     * @param userID
     */
    private void markRoleOrUserRight(Map<String, MenuTree> menuTreeMap, String roleID, String userID) {
        if (StringUtil.isNotBlank(roleID)) {
            //查询角色对应的权限
            Example example = new Example(RoleRight.class);
            example.createCriteria().andEqualTo("fRoleID", roleID);
            List<RoleRight> roleRightList = roleRightDAO.selectByExample(example);
            Map<String, RoleRight> roleRightMap = MenuUtil.list2Map(roleRightList, "getfRightID", RoleRight.class);
            for (Map.Entry<String, MenuTree> entry : menuTreeMap.entrySet()) {
                MenuTree menuTree = entry.getValue();
                //跳过菜单节点
                if (AraCoreConstants.NODE_TYPE_MENU.equals(menuTree.getNodeType())) {
                    continue;
                }
                //处理角色有权限的记录
                if (roleRightMap.get(menuTree.getfRightID()) != null) {
                    rightDataProcess(menuTreeMap, menuTree);
                }
            }
        } else if (StringUtil.isNotBlank(userID)) {
            //查询用户对应的权限
            Example example = new Example(UserRight.class);
            example.createCriteria().andEqualTo("fUserID", userID);
            List<UserRight> userRightList = userRightDAO.selectByExample(example);
            Map<String, UserRight> userRightMap = MenuUtil.list2Map(userRightList, "getfRightID", UserRight.class);
            for (Map.Entry<String, MenuTree> entry : menuTreeMap.entrySet()) {
                MenuTree menuTree = entry.getValue();
                //跳过菜单节点
                if (AraCoreConstants.NODE_TYPE_MENU.equals(menuTree.getNodeType())) {
                    continue;
                }
                //处理用户有权限的记录
                if (userRightMap.get(menuTree.getfRightID()) != null) {
                    rightDataProcess(menuTreeMap, menuTree);
                }
            }
        }
    }

    /**
     * 处理权限数据
     *
     * @param menuTreeMap
     * @param menuTree
     */
    private void rightDataProcess(Map<String, MenuTree> menuTreeMap, MenuTree menuTree) {
        menuTree.setHasRight(AraCoreConstants.YES);
        //获取父节点菜单的number
        String menuNumber = menuTreeMap.get(menuTree.getfMainMenuItemID()).getfNumber();
        //组装权限为1001:select
        String menuRightCode = menuNumber + ":" + menuTree.getfRightCode();
        menuTree.setMenuRightCode(menuRightCode);
        //递归处理父节点(即菜单节点)
        markParentMenu(menuTreeMap, menuTree);
    }

    /**
     * 递归调用,设置父节点为有权限
     *
     * @param menuTreeMap
     * @param menuTree
     */
    private void markParentMenu(Map<String, MenuTree> menuTreeMap, MenuTree menuTree) {
        MenuTree parentMenu = menuTreeMap.get(menuTree.getfParentID());
        //设置父节点为有权限
        parentMenu.setHasRight(AraCoreConstants.YES);
        if (StringUtil.isNotBlank(parentMenu.getfParentID())) {
            //递归调用,设置父节点为有权限
            markParentMenu(menuTreeMap, parentMenu);
        }
    }


}
