package com.aratek.framework.core.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import com.aratek.framework.core.constant.AraCoreConstants;
import com.aratek.framework.core.constant.AraResultCodeConstants;
import com.aratek.framework.core.dao.RightDAO;
import com.aratek.framework.core.dao.UserDAO;
import com.aratek.framework.core.dao.UserRightDAO;
import com.aratek.framework.core.service.UserService;
import com.aratek.framework.core.util.*;
import com.aratek.framework.domain.base.Result;
import com.aratek.framework.domain.core.RightVO;
import com.aratek.framework.domain.core.User;
import com.aratek.framework.domain.core.UserRight;
import com.aratek.framework.domain.core.UserRightVO;
import com.github.pagehelper.PageHelper;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * @author shijinlong
 * @date 2018-04-28
 * @description 用户服务 实现类
 */
@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private UserRightDAO userRightDAO;

    @Autowired
    private RightDAO rightDAO;

    /**
     * 加密方式，默认MD5
     */
    @Value("${ara.encrypt.method:MD5}")
    private String encryptMethod;


    @Override
    public User selectUserByID(String fID) {
        return userDAO.selectUserByID(fID);
    }

    @Override
    public User selectUserByName(String fName) {
        return userDAO.selectUserByName(fName);
    }

    @Override
    public List<User> selectUserList(User user) {
        PageHelper.startPage(user.getPageNum(), user.getPageSize(), PageUtil.getSortStr(user.getSortParams()));
        return userDAO.selectUserList(user);
    }

    @Override
    public Map exportUserList(User user) {
        //1.查询数据,是否查全部数据由前台传参控制
        PageHelper.startPage(user.getPageNum(), user.getPageSize(), PageUtil.getSortStr(user.getSortParams()));
        //如果pageSize为0，需设置orderByOnly为true
        if (user.getPageSize() == 0) {
            PageHelper.getLocalPage().setOrderByOnly(true);
        }
        List<User> userList = userDAO.selectUserList(user);
        if (userList == null || userList.size() == 0) {
            LOGGER.warn("没有数据!");
            return Result.error(AraResultCodeConstants.CODE_1601);
        }
        //2.设置导出配置
        // 获取导出excel指定模版
        TemplateExportParams params = new TemplateExportParams(AraCoreConstants.EXCEL_TEMPLATE_USER);
        // 标题开始行
        params.setHeadingStartRow(0);
        // 标题行数
        params.setHeadingRows(1);
        // 设置sheetName，若不设置该参数，则使用原本sheet名称
        params.setSheetName(AraCoreConstants.MODULE_USER);
        //3.模版--单sheet--导出
        Map<String, Object> map = new HashMap<String, Object>();
        List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();
        for (int i = 0; i < userList.size(); i++) {
            User user2 = userList.get(i);
            Map<String, String> lm = new HashMap<String, String>();
            lm.put("num", i + 1 + "");
            lm.put("fStatus", ExcelUtil.dealWithStatus(user2.getfStatus()));
            lm.put("fNumber", user2.getfNumber());
            lm.put("fName", user2.getfName());
            lm.put("fEmail", user2.getfEmail());
            lm.put("fMobile", user2.getfMobile());
            lm.put("fQQ", user2.getfQQ());
            lm.put("fWeiXin", user2.getfWeiXin());
            lm.put("fCreatorName", user2.getfCreatorName());
            lm.put("fCreateTime", DateUtil.formatDateTime(user2.getfCreateTime()));
            lm.put("fLastUpdateUserName", user2.getfLastUpdateUserName());
            lm.put("fLastUpdateTime", DateUtil.formatDateTime(user2.getfLastUpdateTime()));
            lm.put("fLoginTime", DateUtil.formatDateTime(user2.getfLoginTime()));
            lm.put("fDescription", user2.getfDescription());
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
            fos = new FileOutputStream("D:/temp/excel/" + AraCoreConstants.MODULE_USER + ".xlsx");
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
        ExcelUtil.setExcelExportResponse(AraCoreConstants.MODULE_USER, response);
        //5.写出数据输出流到页面
        BufferedOutputStream bufferedOutputStream = null;
        try {
//            response.setHeader(AraDomainConstants.DEFAULT_RESULT_CODE_NAME,AraDomainConstants.DEFAULT_RESULT_CODE_VALUE_OK);
            bufferedOutputStream = new BufferedOutputStream(response.getOutputStream());
            workbook.write(bufferedOutputStream);
            bufferedOutputStream.flush();
            bufferedOutputStream.close();
        } catch (IOException e) {
            LOGGER.error("exportUserList.error:{}", e);
            return Result.error(AraResultCodeConstants.CODE_1602);
        } finally {
            if (bufferedOutputStream != null) {
                try {
                    bufferedOutputStream.close();
                } catch (IOException e) {
                    LOGGER.error("exportUserList.error:{}", e);
                    return Result.error(AraResultCodeConstants.CODE_1602);
                }
            }
        }
        return Result.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map insertUser(User user) {
        //1.校验编号是否存在
        if (userDAO.selectCountNumber(user.getfNumber()) > 0) {
            return Result.error(AraResultCodeConstants.CODE_1004);
        }
        //2.判断用户名是否已存在
        if (null != userDAO.selectUserByName(user.getfName())) {
            return Result.error(AraResultCodeConstants.CODE_1005);
        }
        //3.生成userID,插入用户表
        String uuid = UUIDUtil.genID();
        //密码加密
        String passWord = user.getfPassWord();
        user.setfPassWord(AraEncryptUtil.userPawEncrypt(passWord, encryptMethod));
        user.setfID(uuid);
        Date now = new Date();
        String currentUserID = CurrentUserUtil.getCurrentUserID();
        user.setfCreatorID(currentUserID);
        user.setfCreateTime(now);
        user.setfLastUpdateUserID(currentUserID);
        user.setfLastUpdateTime(now);
        user.setfLoginTime(null);
        //默认状态为:1,保存
        user.setfStatus(AraCoreConstants.STATUS_SAVE);
        //非初始化数据
        user.setfIsInit(AraCoreConstants.NO);
        userDAO.insertSelective(user);
        return Result.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map updateUserInfo(User user) {
        //1.校验编号是否存在
        if (StringUtil.isNotBlank(user.getfNumber())) {
            User dbUser = userDAO.selectUserByNumber(user.getfNumber());
            if (dbUser != null) {
                if (!user.getfID().equals(dbUser.getfID())) {
                    return Result.error(AraResultCodeConstants.CODE_1004);
                }
            }
        }
        //2.校验用户名是否已存在
        if (StringUtil.isNotBlank(user.getfName())) {
            User dbUser = userDAO.selectUserByName(user.getfName());
            if (dbUser != null) {
                if (!user.getfID().equals(dbUser.getfID())) {
                    return Result.error(AraResultCodeConstants.CODE_1005);
                }
            }
        }
        //3.处理操作人和操作时间
        CurrentUserUtil.setOperatorInfo(user);
        //4.根据ID更新用户信息
        userDAO.updateUserInfo(user);
        return Result.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map updateUserStatus(User user) {
        //验证用户是否存在
//        User dbUser = userDAO.selectUserByID(user.getfID());
//        if (dbUser == null) {
//            LOGGER.error("用户不存在!fID={}", user.getfID());
//            return Result.error(AraResultCodeConstants.CODE_1003);
//        }
        //处理操作人和操作时间
        CurrentUserUtil.setOperatorInfo(user);
        //根据ID更新用户状态
        userDAO.updateUserStatus(user);
        return Result.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map updateUserStatusBatch(List<User> userList) {
        userDAO.updateUserStatusBatch(userList);
        return Result.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map deleteUserByID(String userID) {
//        //1.删除用户的权限
//        Example rightExample = new Example(UserRight.class);
//        rightExample.createCriteria().andEqualTo("fUserID", userID);
//        userRightDAO.deleteByExample(rightExample);
//        //2.删除用户的角色
//        Example roleExample = new Example(UserRole.class);
//        roleExample.createCriteria().andEqualTo("fUserID", userID);
//        userRoleDAO.deleteByExample(roleExample);
        //1.查询用户引用
        int referenceNum = userDAO.selectCountUserReferenceByUserID(userID);
        if (referenceNum > 0) {
            //存在引用，不允许删除
            return Result.error(AraResultCodeConstants.CODE_1006);
        }
        //2.删除用户
        userDAO.deleteByPrimaryKey(userID);
        return Result.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map deleteUserBatch(List<String> userIDs) {
        for (String userID : userIDs) {
            //1.查询用户引用
            int referenceNum = userDAO.selectCountUserReferenceByUserID(userID);
            if (referenceNum > 0) {
                //存在引用，不允许删除
                return Result.error(AraResultCodeConstants.CODE_1006);
            }
            //2.删除用户
            userDAO.deleteByPrimaryKey(userID);
        }
        return Result.ok();
    }

    @Override
    public Set<String> selectRightListByUserID(String userID) {
        return userDAO.selectRightListByUserID(userID);
    }

    @Override
    public Set<String> selectRoleNameListByUserID(String userID) {
        return userDAO.selectRoleNameListByUserID(userID);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map deleteUserRightBatch(List<String> idList) {
        userRightDAO.deleteUserRightBatch(idList);
        return Result.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map insertUserRightBatch(UserRightVO userRightVO) {
        //获取初始化的该用户下的权限ID集合
        Map<String, String> initRightIDMap = ListUtil.idList2Map(userRightDAO.selectInitRightIDListByUserID(userRightVO.getUserID()));
        List<UserRight> newUserRightList = new ArrayList<UserRight>();
        //1.delete 非初始化的权限
        userRightDAO.deleteUserRightByUserID(userRightVO.getUserID());
        //获取传入的userRightList
        List<UserRight> userRightList = userRightVO.getUserRightList();
        //如果userRightList为空,则只删除该用户下的非初始化的权限
        if (userRightList == null || userRightList.size() == 0) {
            return Result.ok();
        }
        //2.insert
        String userID = CurrentUserUtil.getCurrentUserID();
        Date now = new Date();
        //遍历传入的userRightList
        for (UserRight userRight : userRightVO.getUserRightList()) {
            //初始化的用户权限不需要再insert
            if (initRightIDMap != null && initRightIDMap.get(userRight.getfRightID()) != null) {
                continue;
            }
            userRight.setfID(UUIDUtil.genID());
            userRight.setfUserID(userRightVO.getUserID());
            userRight.setfCreatorID(userID);
            userRight.setfCreateTime(now);
            userRight.setfLastUpdateUserID(userID);
            userRight.setfLastUpdateTime(now);
            //非初始化数据
            userRight.setfIsInit(AraCoreConstants.NO);
            //add into list
            newUserRightList.add(userRight);
        }
        userRightDAO.insertUserRightBatch(newUserRightList);
        return Result.ok();
    }

    @Override
    public List<User> selectRoleUserList(String roleID) {
        if (StringUtil.isBlank(roleID)) {
            //查询所有用户
            User user = new User();
            PageHelper.startPage(user.getPageNum(), user.getPageSize(), PageUtil.getSortStr(user.getSortParams()));
            return userDAO.selectUserList(user);
        } else {
            //查询角色下的用户
            PageHelper.startPage(0, 0, PageUtil.getSortStr(null));
            return userDAO.selectUserListByRoleID(roleID);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map updateUserPassword(User user) {
        //1.与当前用户ID获取的DB数据比对
        String userID = CurrentUserUtil.getCurrentUserID();
        User user1 = userDAO.selectUserByID(userID);
        if (user1 == null || !user1.getfName().equals(user.getfName()) || !user1.getfPassWord().equals(AraEncryptUtil.userPawEncrypt(user.getOldPassWord(), encryptMethod))) {
            LOGGER.debug("userID={}", userID);
            LOGGER.debug("user={}", JsonUtil.toJson(user));
            LOGGER.debug("user1={}", JsonUtil.toJson(user1));
            //密码不正确
            return Result.error(AraResultCodeConstants.CODE_1011,"原密码不正确");
        }
        //2.与用户名查询出来的密码比对
        User user2 = userDAO.selectUserByName(user.getfName());
        if (user2 == null || !user2.getfPassWord().equals(AraEncryptUtil.userPawEncrypt(user.getOldPassWord(),encryptMethod))) {
            LOGGER.debug("userID={}", userID);
            LOGGER.debug("user={}", JsonUtil.toJson(user));
            LOGGER.debug("user2={}", JsonUtil.toJson(user2));
            //密码不正确
            return Result.error(AraResultCodeConstants.CODE_1011,"原密码不正确");
        }
        //3.update password
        userDAO.updateUserPasswordByID(userID, AraEncryptUtil.userPawEncrypt(user.getfPassWord(), encryptMethod), new Date());
        return Result.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map updateCurrentUserInfo(User user) {
        //1.获取当前用户ID
        String userID = CurrentUserUtil.getCurrentUserID();
        //2.set userID
        user.setfID(userID);
        //3.处理操作人和操作时间
        CurrentUserUtil.setOperatorInfo(user);
        //4.根据ID更新用户信息
        userDAO.updateCurrentUserInfo(user);
        return Result.ok();
    }

    @Override
    public Map selectUserRightListByRoleOrUser(RightVO rightVO) {
        String roleName = rightVO.getRoleName();
        String userName = rightVO.getUserName();
        List<RightVO> rightList = new ArrayList<RightVO>();
        if (StringUtil.isNotBlank(roleName) && StringUtil.isNotBlank(userName)) {
            rightList = rightDAO.selectUserRightListByRoleName(rightVO);
        } else if (StringUtil.isNotBlank(roleName)) {
            //只根据角色名查询相关权限
            rightList = rightDAO.selectUserRightListByRoleName(rightVO);
        } else if (StringUtil.isNotBlank(userName)) {
            //根据用户名查询该用户的权限
            rightList = rightDAO.selectUserRightListByUserName(rightVO);
            rightList = mergeRightList(rightList);
        }
        return Result.ok().putData(rightList);
    }

    @Override
    public Map exportUserRightListByRoleOrUser(RightVO rightVO) {
        //1.查询数据,是否查全部数据由前台传参控制
        String roleName = rightVO.getRoleName();
        String userName = rightVO.getUserName();
        List<RightVO> rightList = new ArrayList<RightVO>();
        if (StringUtil.isNotBlank(roleName) && StringUtil.isNotBlank(userName)) {
            rightList = rightDAO.selectUserRightListByRoleName(rightVO);
        } else if (StringUtil.isNotBlank(roleName)) {
            //只根据角色名查询相关权限
            rightList = rightDAO.selectUserRightListByRoleName(rightVO);
        } else if (StringUtil.isNotBlank(userName)) {
            //根据用户名查询该用户的权限
            rightList = rightDAO.selectUserRightListByUserName(rightVO);
            rightList = mergeRightList(rightList);
        }
        if (rightList == null || rightList.size() == 0) {
            LOGGER.warn("没有数据!");
            return Result.error(AraResultCodeConstants.CODE_1601);
        }
        //2.设置导出配置
        // 获取导出excel指定模版
        TemplateExportParams params = new TemplateExportParams(AraCoreConstants.EXCEL_TEMPLATE_USER_ROLE_RIGHT);
        // 标题开始行
        params.setHeadingStartRow(0);
        // 标题行数
        params.setHeadingRows(1);
        // 设置sheetName，若不设置该参数，则使用原本sheet名称
        params.setSheetName(AraCoreConstants.MODULE_USER_ROLE_RIGHT);
        //3.模版--单sheet--导出
        Map<String, Object> map = new HashMap<String, Object>();
        List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();
        for (int i = 0; i < rightList.size(); i++) {
            RightVO rightVO2 = rightList.get(i);
            Map<String, String> lm = new HashMap<String, String>();
            lm.put("num", i + 1 + "");
            lm.put("menuNumber", rightVO2.getMenuNumber());
            lm.put("menuName", rightVO2.getMenuName());
            lm.put("rightCode", rightVO2.getRightCode());
            lm.put("rightName", rightVO2.getRightName());
            lm.put("rightSource", rightVO2.getRightSource());
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
            fos = new FileOutputStream("D:/temp/excel/" + AraCoreConstants.MODULE_USER_ROLE_RIGHT + ".xlsx");
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
        ExcelUtil.setExcelExportResponse(AraCoreConstants.MODULE_USER_ROLE_RIGHT, response);
        //5.写出数据输出流到页面
        BufferedOutputStream bufferedOutputStream = null;
        try {
            bufferedOutputStream = new BufferedOutputStream(response.getOutputStream());
            workbook.write(bufferedOutputStream);
            bufferedOutputStream.flush();
            bufferedOutputStream.close();
        } catch (IOException e) {
            LOGGER.error("exportUserRightListByRoleOrUser.error:{}", e);
            return Result.error(AraResultCodeConstants.CODE_1602);
        } finally {
            if (bufferedOutputStream != null) {
                try {
                    bufferedOutputStream.close();
                } catch (IOException e) {
                    LOGGER.error("exportUserRightListByRoleOrUser.error:{}", e);
                    return Result.error(AraResultCodeConstants.CODE_1602);
                }
            }
        }
        return Result.ok();
    }

    /**
     * 处理权限列表的权限来源字段,合并重复项
     *
     * @param rightList
     */
    private List<RightVO> mergeRightList(List<RightVO> rightList) {
        if (rightList == null || rightList.size() == 0) {
            return rightList;
        }
        //1.merge
        Map<String, RightVO> rightMap = new HashMap<String, RightVO>();
        for (RightVO rightVO : rightList) {
            String key = String.format("%s:%s", rightVO.getMenuNumber(), rightVO.getRightCode());
            RightVO existedRight = rightMap.get(key);
            if (existedRight != null) {
                //合并权限来源
                rightVO.setRightSource(String.format("%s,%s", existedRight.getRightSource(), rightVO.getRightSource()));
            }
            rightMap.put(key, rightVO);
        }
        //2.map to list
        List<RightVO> newRightList = new ArrayList<RightVO>();
        for (Map.Entry<String, RightVO> entry : rightMap.entrySet()) {
            newRightList.add(entry.getValue());
        }
        return newRightList;
    }



}
