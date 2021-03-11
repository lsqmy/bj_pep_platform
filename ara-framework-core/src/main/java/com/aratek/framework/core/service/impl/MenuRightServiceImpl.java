package com.aratek.framework.core.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import com.aratek.framework.core.constant.AraCoreConstants;
import com.aratek.framework.core.constant.AraResultCodeConstants;
import com.aratek.framework.core.dao.MenuRightDAO;
import com.aratek.framework.core.dao.UserDAO;
import com.aratek.framework.core.service.MenuRightService;
import com.aratek.framework.core.util.*;
import com.aratek.framework.domain.base.Result;
import com.aratek.framework.domain.core.MenuRight;
import com.aratek.framework.domain.core.MenuRightTree;
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
 * @description 菜单权限服务 实现类
 */
@Service
public class MenuRightServiceImpl implements MenuRightService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MenuRightServiceImpl.class);

    @Autowired
    private MenuRightDAO menuRightDAO;

    @Autowired
    private UserDAO userDAO;

    @Override
    public List<MenuRight> selectMenuRightList(MenuRight menuRight) {
        PageHelper.startPage(menuRight.getPageNum(), menuRight.getPageSize(), PageUtil.getSortStr(menuRight.getSortParams()));
        return menuRightDAO.selectMenuRightList(menuRight);
    }

    @Override
    public Map exportMenuRightList(MenuRight menuRight) {
        //1.查询数据,是否查全部数据由前台传参控制
        PageHelper.startPage(menuRight.getPageNum(), menuRight.getPageSize(), PageUtil.getSortStr(menuRight.getSortParams()));
        //如果pageSize为0，需设置orderByOnly为true
        if (menuRight.getPageSize() == 0) {
            PageHelper.getLocalPage().setOrderByOnly(true);
        }
        List<MenuRight> menuRightList = menuRightDAO.selectMenuRightList(menuRight);
        if (menuRightList == null || menuRightList.size() == 0) {
            LOGGER.warn("没有数据!");
            return Result.error(AraResultCodeConstants.CODE_1601);
        }
        //2.设置导出配置
        // 获取导出excel指定模版
        TemplateExportParams params = new TemplateExportParams(AraCoreConstants.EXCEL_TEMPLATE_MENU_RIGHT);
        // 标题开始行
        params.setHeadingStartRow(0);
        // 标题行数
        params.setHeadingRows(1);
        // 设置sheetName，若不设置该参数，则使用原本sheet名称
        params.setSheetName(AraCoreConstants.MODULE_MENU_RIGHT);
        //3.模版--单sheet--导出
        Map<String, Object> map = new HashMap<String, Object>();
        List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();
        for (int i = 0; i < menuRightList.size(); i++) {
            MenuRight menuRight2 = menuRightList.get(i);
            Map<String, String> lm = new HashMap<String, String>();
            lm.put("num", i + 1 + "");
            lm.put("fRightCode", menuRight2.getfRightCode());
            lm.put("fRightName", menuRight2.getfRightName());
            lm.put("fMainMenuItemNumber", menuRight2.getfMainMenuItemNumber());
            lm.put("fMainMenuItemName", menuRight2.getfMainMenuItemName());
            lm.put("fCreatorName", menuRight2.getfCreatorName());
            lm.put("fCreateTime", DateUtil.formatDateTime(menuRight2.getfCreateTime()));
            lm.put("fLastUpdateUserName", menuRight2.getfLastUpdateUserName());
            lm.put("fLastUpdateTime", DateUtil.formatDateTime(menuRight2.getfLastUpdateTime()));
            lm.put("fDescription", menuRight2.getfDescription());
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
            fos = new FileOutputStream("D:/temp/excel/" + AraCoreConstants.MODULE_MENU_RIGHT + ".xlsx");
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
        ExcelUtil.setExcelExportResponse(AraCoreConstants.MODULE_MENU_RIGHT, response);
        //5.写出数据输出流到页面
        BufferedOutputStream bufferedOutputStream = null;
        try {
            bufferedOutputStream = new BufferedOutputStream(response.getOutputStream());
            workbook.write(bufferedOutputStream);
            bufferedOutputStream.flush();
            bufferedOutputStream.close();
        } catch (IOException e) {
            LOGGER.error("exportMenuRightList.error:{}", e);
            return Result.error(AraResultCodeConstants.CODE_1602);
        } finally {
            if (bufferedOutputStream != null) {
                try {
                    bufferedOutputStream.close();
                } catch (IOException e) {
                    LOGGER.error("exportMenuRightList.error:{}", e);
                    return Result.error(AraResultCodeConstants.CODE_1602);
                }
            }
        }
        return Result.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map insertMenuRight(MenuRight menuRight) {
        //校验权限码是否已存在
        Example menuRightExample = new Example(MenuRight.class);
        menuRightExample.createCriteria().andEqualTo("fRightCode", menuRight.getfRightCode()).andEqualTo("fMainMenuItemID", menuRight.getfMainMenuItemID());
        Collection MenuRightList = menuRightDAO.selectByExample(menuRightExample);
        if (!MenuRightList.isEmpty()) {
            return Result.error(AraResultCodeConstants.CODE_1202);
        }
        //获取当前操作人
        String userID = CurrentUserUtil.getCurrentUserID();
        Date now = new Date();
        //处理菜单权限
        menuRight.setfID(UUIDUtil.genID());
        menuRight.setfCreatorID(userID);
        menuRight.setfCreateTime(now);
        menuRight.setfLastUpdateUserID(userID);
        menuRight.setfLastUpdateTime(now);
        //非初始化数据
        menuRight.setfIsInit(AraCoreConstants.NO);
        //批量插入数据
        menuRightDAO.insertSelective(menuRight);
        return Result.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map updateMenuRight(MenuRight menuRight) {
        //校验权限码
        if (StringUtil.isNotBlank(menuRight.getfMainMenuItemID())) {
            Example example = new Example(MenuRight.class);
            example.createCriteria().andEqualTo("fRightCode", menuRight.getfRightCode()).andEqualTo("fMainMenuItemID", menuRight.getfMainMenuItemID());
            List<MenuRight> menuRightList = menuRightDAO.selectByExample(example);
            if (menuRightList != null && menuRightList.size() > 0) {
                if (!menuRightList.get(0).getfID().equals(menuRight.getfID())) {
                    LOGGER.debug("该权限码已存在!");
                    return Result.error(AraResultCodeConstants.CODE_1202, "该权限码已存在");
                }
            }
        }
        //设置当前操作人及操作时间
        CurrentUserUtil.setOperatorInfo(menuRight);
        menuRightDAO.updateMenuRight(menuRight);
        return Result.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map deleteMenuRightBatch(List<String> idList) {
        //1.校验引用
        if (menuRightDAO.selectCountReference(idList) > 0) {
            //存在引用，不允许删除
            return Result.error(AraResultCodeConstants.CODE_1006);
        }
        //2.delete
        menuRightDAO.deleteMenuRightBatch(idList);
        return Result.ok();
    }

    @Override
    public List<MenuRightTree> selectMenuRightListForTree(String roleID, String userID) {
        Set<MenuRightTree> menuRightTreeSet = new HashSet<MenuRightTree>();
        if (StringUtil.isNotBlank(roleID)) {
            menuRightTreeSet.addAll(menuRightDAO.selectMenuRightListForTreeByRoleID(roleID));
        } else if (StringUtil.isNotBlank(userID)) {
            //1.查询出用户ID对应的菜单list
            menuRightTreeSet.addAll(menuRightDAO.selectMenuRightListForTreeByUserID(userID));
            //2.查询出用户的角色
            Set<String> sets = userDAO.selectRoleIDListByUserID(userID);
            //3.查询出角色对应的菜单list
            for (String set : sets) {
                menuRightTreeSet.addAll(menuRightDAO.selectMenuRightListForTreeByRoleID(roleID));
            }
        } else {
            menuRightTreeSet.addAll(menuRightDAO.selectMenuRightListForTree());
        }
        return new ArrayList<MenuRightTree>(menuRightTreeSet);
    }
}
