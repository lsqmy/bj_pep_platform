package com.aratek.framework.core.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import com.aratek.framework.core.constant.AraCoreConstants;
import com.aratek.framework.core.constant.AraResultCodeConstants;
import com.aratek.framework.core.dao.RoleDAO;
import com.aratek.framework.core.dao.RoleRightDAO;
import com.aratek.framework.core.dao.UserRoleDAO;
import com.aratek.framework.core.service.RoleService;
import com.aratek.framework.core.util.*;
import com.aratek.framework.domain.base.Result;
import com.aratek.framework.domain.core.*;
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
 * @date 2018-05-09
 * @description 角色服务 实现类
 */
@Service
public class RoleServiceImpl implements RoleService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoleServiceImpl.class);

    @Autowired
    private RoleDAO roleDAO;

    @Autowired
    private RoleRightDAO roleRightDAO;

    @Autowired
    private UserRoleDAO userRoleDAO;

    @Override
    public Role selectRoleByID(String fID) {
        return roleDAO.selectRoleByID(fID);
    }

    @Override
    public Role selectRoleByName(String fName) {
        return roleDAO.selectRoleByName(fName);
    }

    @Override
    public List<Role> selectRoleList(Role role) {
        PageHelper.startPage(role.getPageNum(), role.getPageSize(), PageUtil.getSortStr(role.getSortParams()));
        return roleDAO.selectRoleList(role);
    }

    @Override
    public Map exportRoleList(Role role) {
        //1.查询数据,是否查全部数据由前台传参控制
        PageHelper.startPage(role.getPageNum(), role.getPageSize(), PageUtil.getSortStr(role.getSortParams()));
        //如果pageSize为0，需设置orderByOnly为true
        if (role.getPageSize() == 0) {
            PageHelper.getLocalPage().setOrderByOnly(true);
        }
        List<Role> roleList = roleDAO.selectRoleList(role);
        if (roleList == null || roleList.size() == 0) {
            LOGGER.warn("没有数据!");
            return Result.error(AraResultCodeConstants.CODE_1601);
        }
        //2.设置导出配置
        // 获取导出excel指定模版
        TemplateExportParams params = new TemplateExportParams(AraCoreConstants.EXCEL_TEMPLATE_ROLE);
        // 标题开始行
        params.setHeadingStartRow(0);
        // 标题行数
        params.setHeadingRows(1);
        // 设置sheetName，若不设置该参数，则使用原本sheet名称
        params.setSheetName(AraCoreConstants.MODULE_ROLE);
        //3.模版--单sheet--导出
        Map<String, Object> map = new HashMap<String, Object>();
        List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();
        for (int i = 0; i < roleList.size(); i++) {
            Role role2 = roleList.get(i);
            Map<String, String> lm = new HashMap<String, String>();
            lm.put("num", i + 1 + "");
            lm.put("fStatus", ExcelUtil.dealWithStatus(role2.getfStatus()));
            lm.put("fNumber", role2.getfNumber());
            lm.put("fName", role2.getfName());
            lm.put("fCreatorName", role2.getfCreatorName());
            lm.put("fCreateTime", DateUtil.formatDateTime(role2.getfCreateTime()));
            lm.put("fLastUpdateUserName", role2.getfLastUpdateUserName());
            lm.put("fLastUpdateTime", DateUtil.formatDateTime(role2.getfLastUpdateTime()));
            lm.put("fDescription", role2.getfDescription());
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
            fos = new FileOutputStream("D:/temp/excel/" + AraCoreConstants.MODULE_ROLE + ".xlsx");
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
        ExcelUtil.setExcelExportResponse(AraCoreConstants.MODULE_ROLE, response);
        //5.写出数据输出流到页面
        BufferedOutputStream bufferedOutputStream = null;
        try {
            bufferedOutputStream = new BufferedOutputStream(response.getOutputStream());
            workbook.write(bufferedOutputStream);
            bufferedOutputStream.flush();
            bufferedOutputStream.close();
        } catch (IOException e) {
            LOGGER.error("exportRoleList.error:{}", e);
            return Result.error(AraResultCodeConstants.CODE_1602);
        } finally {
            if (bufferedOutputStream != null) {
                try {
                    bufferedOutputStream.close();
                } catch (IOException e) {
                    LOGGER.error("exportRoleList.error:{}", e);
                    return Result.error(AraResultCodeConstants.CODE_1602);
                }
            }
        }
        return Result.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map insertRole(Role role) {
        //校验number
        Example example = new Example(Role.class);
        example.createCriteria().andEqualTo("fNumber", role.getfNumber());
        Collection roles = roleDAO.selectByExample(example);
        if (!roles.isEmpty()) {
            return Result.error(AraResultCodeConstants.CODE_1004);
        }
        //校验角色名
        example.clear();
        roles.clear();
        example.createCriteria().andEqualTo("fName", role.getfName());
        if (!roles.isEmpty()) {
            return Result.error(AraResultCodeConstants.CODE_1104);
        }
        String uuid = UUIDUtil.genID();
        role.setfID(uuid);
        role.setfStatus(AraCoreConstants.STATUS_SAVE);
        Date now = new Date();
        String currentUserID = CurrentUserUtil.getCurrentUserID();
        role.setfCreatorID(currentUserID);
        role.setfCreateTime(now);
        role.setfLastUpdateUserID(currentUserID);
        role.setfLastUpdateTime(now);
        //非初始化数据
        role.setfIsInit(AraCoreConstants.NO);
        roleDAO.insertSelective(role);
        return Result.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map updateRoleInfo(Role role) {
        //1.校验编号是否存在
        if (StringUtil.isNotBlank(role.getfNumber())) {
            Role dbRole = roleDAO.selectRoleByNumber(role.getfNumber());
            if (dbRole != null) {
                if (!role.getfID().equals(dbRole.getfID())) {
                    return Result.error(AraResultCodeConstants.CODE_1004);
                }
            }
        }
        //验证角色是否存在
        Role dbRole = roleDAO.selectRoleByID(role.getfID());
        if (dbRole == null) {
            LOGGER.error("角色不存在!fID={}", role.getfID());
            return Result.error(AraResultCodeConstants.CODE_1103);
        }
        //处理操作人和操作时间
        CurrentUserUtil.setOperatorInfo(role);
        //根据ID更新角色信息
        roleDAO.updateRoleInfo(role);
        return Result.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map updateRoleStatus(Role role) {
        //处理操作人和操作时间
        CurrentUserUtil.setOperatorInfo(role);
        //根据ID更新角色状态
        roleDAO.updateRoleStatus(role);
        return Result.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map updateRoleStatusBatch(List<Role> roleList) {
        //更新角色状态
        roleDAO.updateRoleStatusBatch(roleList);
        return Result.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map deleteRoleByID(Role role) {
        //1.校验引用
        if (roleDAO.selectCountReference(role.getfID()) > 0) {
            //存在引用，不允许删除
            return Result.error(AraResultCodeConstants.CODE_1006);
        }
        //2.delete
        roleDAO.deleteByPrimaryKey(role.getfID());
        return Result.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map deleteRoleBatch(List<String> idList) {
        //1.校验引用
        for (String roleID : idList) {
            if (roleDAO.selectCountReference(roleID) > 0) {
                //存在引用，不允许删除
                return Result.error(AraResultCodeConstants.CODE_1006);
            }
        }
        //2.delete
        for (String roleID : idList) {
            roleDAO.deleteByPrimaryKey(roleID);
        }
        return Result.ok();
    }

    @Override
    public List<RoleRight> selectRoleRightList(RoleRight roleRight) {
        PageHelper.startPage(roleRight.getPageNum(), roleRight.getPageSize(), PageUtil.getSortStr(roleRight.getSortParams()));
        return roleRightDAO.selectRoleRightList(roleRight);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map insertRoleRightBatch(RoleRightVO roleRightVO) {
        //查询角色初始化的权限IDList
        List<String> rightIDList = roleRightDAO.selectInitRightIDListByRoleID(roleRightVO.getRoleID());
        //转为map
        Map<String, String> rightIDMap = ListUtil.idList2Map(rightIDList);
        //定义新的roleRightList
        List<RoleRight> newRoleRightList = new ArrayList<RoleRight>();
        //1.delete
        roleRightDAO.deleteRoleRightByRoleID(roleRightVO.getRoleID());
        //传入的roleRightList
        List<RoleRight> roleRightList = roleRightVO.getRoleRightList();
        //如果roleRightList为空,则只删除该角色下的权限
        if (roleRightList == null || roleRightList.size() == 0) {
            return Result.ok();
        }
        //2.insert
        String userID = CurrentUserUtil.getCurrentUserID();
        Date now = new Date();
        //遍历传入的roleRightList
        for (RoleRight roleRight : roleRightList) {
            //初始化的角色权限不需要再insert
            if (rightIDMap != null && rightIDMap.get(roleRight.getfRightID()) != null) {
                continue;
            }
            roleRight.setfID(UUIDUtil.genID());
            roleRight.setfRoleID(roleRightVO.getRoleID());
            roleRight.setfCreatorID(userID);
            roleRight.setfCreateTime(now);
            roleRight.setfLastUpdateUserID(userID);
            roleRight.setfLastUpdateTime(now);
            //非初始化数据
            roleRight.setfIsInit(AraCoreConstants.NO);
            //
            newRoleRightList.add(roleRight);
        }
        if (newRoleRightList != null && newRoleRightList.size() > 0) {
            //insert新增的权限
            roleRightDAO.insertRoleRightBatch(newRoleRightList);
        }
        return Result.ok();

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map deleteRoleRightBatch(List<String> idList) {
        roleRightDAO.deleteRoleRightBatch(idList);
        return Result.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map insertRoleUserBatch(RoleUserVO roleUserVO) {
        //获取初始化的该角色下的用户ID集合
        Map<String, String> initUserIDMap = ListUtil.idList2Map(userRoleDAO.selectInitUserIDListByRoleID(roleUserVO.getRoleID()));
        //1.delete 传入的roleID+userID
        userRoleDAO.deleteRoleUserBatch(roleUserVO);
        //2.insert
        String currentUserID = CurrentUserUtil.getCurrentUserID();
        Date now = new Date();
        List<UserRole> userRoleList = new ArrayList<UserRole>();
        for (String userID : roleUserVO.getUserIDList()) {
            //初始化的角色用户不需要再insert
            if (initUserIDMap != null && initUserIDMap.get(userID) != null) {
                continue;
            }
            UserRole userRole = new UserRole();
            userRole.setfID(UUIDUtil.genID());
            userRole.setfRoleID(roleUserVO.getRoleID());
            userRole.setfUserID(userID);
            userRole.setfCreatorID(currentUserID);
            userRole.setfCreateTime(now);
            userRole.setfLastUpdateUserID(currentUserID);
            userRole.setfLastUpdateTime(now);
            //非初始化数据
            userRole.setfIsInit(AraCoreConstants.NO);
            //add into list
            userRoleList.add(userRole);
        }
        userRoleDAO.insertRoleUserBatch(userRoleList);
        return Result.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map deleteRoleUserBatch(RoleUserVO roleUserVO) {
        userRoleDAO.deleteRoleUserBatch(roleUserVO);
        return Result.ok();
    }

    @Override
    public List<Role> selectUserRoleList(String userID) {
        if (StringUtil.isBlank(userID)) {
            //查询所有角色
            return roleDAO.selectRoleList(new Role());
        } else {
            //查询用户的角色
            return roleDAO.selectRoleListByUserID(userID);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map insertUserRoleBatch(UserRoleVO userRoleVO) {
        //获取初始化的该用户的角色ID集合
        Map<String, String> initRoleIDMap = ListUtil.idList2Map(userRoleDAO.selectInitRoleIDListByUserID(userRoleVO.getUserID()));
        //1.delete 传入的userID+roleID
        userRoleDAO.deleteUserRoleBatch(userRoleVO);
        //2.insert
        String currentUserID = CurrentUserUtil.getCurrentUserID();
        Date now = new Date();
        List<UserRole> userRoleList = new ArrayList<UserRole>();
        for (String roleID : userRoleVO.getRoleIDList()) {
            //初始化的不需要再insert
            if (initRoleIDMap != null && initRoleIDMap.get(roleID) != null) {
                continue;
            }
            UserRole userRole = new UserRole();
            userRole.setfID(UUIDUtil.genID());
            userRole.setfRoleID(roleID);
            userRole.setfUserID(userRoleVO.getUserID());
            userRole.setfCreatorID(currentUserID);
            userRole.setfCreateTime(now);
            userRole.setfLastUpdateUserID(currentUserID);
            userRole.setfLastUpdateTime(now);
            //非初始化数据
            userRole.setfIsInit(AraCoreConstants.NO);
            //add into list
            userRoleList.add(userRole);
        }
        userRoleDAO.insertRoleUserBatch(userRoleList);
        return Result.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map deleteUserRoleBatch(UserRoleVO userRoleVO) {
        userRoleDAO.deleteUserRoleBatch(userRoleVO);
        return Result.ok();
    }
}
