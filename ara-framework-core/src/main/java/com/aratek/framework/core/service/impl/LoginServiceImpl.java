package com.aratek.framework.core.service.impl;

import com.aratek.framework.core.constant.AraCoreConstants;
import com.aratek.framework.core.constant.AraResultCodeConstants;
import com.aratek.framework.core.dao.AppRegisterDAO;
import com.aratek.framework.core.dao.UserDAO;
import com.aratek.framework.core.dao.UserLoginLogDAO;
import com.aratek.framework.core.exception.AraAuthException;
import com.aratek.framework.core.service.LoginService;
import com.aratek.framework.core.service.UserService;
import com.aratek.framework.core.util.*;
import com.aratek.framework.domain.base.Result;
import com.aratek.framework.domain.constant.AraDomainConstants;
import com.aratek.framework.domain.core.AppRegister;
import com.aratek.framework.domain.core.User;
import com.aratek.framework.domain.core.UserLoginLog;
import com.aratek.security.gm.SM3Util;
import org.apache.commons.net.util.Base64;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author shijinlong
 * @date 2018-04-28
 * @description 登录注销 实现类
 */
@Service
public class LoginServiceImpl implements LoginService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginServiceImpl.class);

    @Autowired
    private UserService userService;

    @Autowired
    private AppRegisterDAO appRegisterDAO;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private UserLoginLogDAO userLoginLogDAO;

    /**
     * 默认token过期时间为15分钟
     */
    @Value("${ara.token.expireSeconds:900}")
    private long tokenExpireSeconds;

    /**
     * 加密方式，默认MD5
     */
    @Value("${ara.encrypt.method:MD5}")
    private String encryptMethod;

    /**
     * 输错密码锁定时间（单位分钟）
     */
    @Value("${ara.user.lockedTime:30}")
    private Integer lockedTime;

    /**
     * 密码过期时间（单位天）
     */
    @Value("${ara.user.passwordTime:90}")
    private Integer passwordTime;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map login(User user) {
        //1.check user name and password
        if (StringUtil.isBlank(user.getfName())
                || StringUtil.isBlank(user.getfPassWord())) {
            throw new AraAuthException(AraCoreConstants.HTTP_STATUS_UNAUTHORIZED, "Username or password error!");
        }
        String passWord = user.getfPassWord().trim();
        user.setfPassWord(AraEncryptUtil.userPawEncrypt(passWord, encryptMethod));
        //2.从数据库中根据用户名查找用户并校验密码、状态等
        User dbUser = userService.selectUserByName(user.getfName());
        //账号不存在
        if (dbUser == null) {
            return Result.error(AraResultCodeConstants.CODE_1003, "账号不存在");
//            throw new AraAuthException(AraCoreConstants.HTTP_STATUS_UNAUTHORIZED, "User didn't existed!");
        }
        if (null != dbUser.getfErrorNum() && dbUser.getfErrorNum() >= 5) {
            int minuteBetween = DateUtil.getMinuteBetween(dbUser.getfLastErrorTime().getTime(), System.currentTimeMillis());
            if (minuteBetween < lockedTime) {
                return Result.error(AraResultCodeConstants.CODE_1013, "密码已输错5次，请" + (lockedTime - minuteBetween) + "分钟后再试");
            } else {
                User errorUser = new User();
                errorUser.setfID(dbUser.getfID());
                errorUser.setfErrorNum(0);
                errorUser.setfLastErrorTime(dbUser.getfLastErrorTime());
                dbUser.setfErrorNum(0);
                userDAO.updateLoginError(errorUser);
            }
        }
        //账号锁定
        if (AraCoreConstants.STATUS_ENABLE != dbUser.getfStatus()) {
            return Result.error(AraResultCodeConstants.CODE_1003, "账号未启用");
//            throw new AraAuthException(AraCoreConstants.HTTP_STATUS_UNAUTHORIZED, "Account is locked!");
        }
        //账号过期
        if (null != dbUser.getfPeriodOfValidity() && dbUser.getfPeriodOfValidity().getTime() < System.currentTimeMillis()) {
            return Result.error(AraResultCodeConstants.CODE_1014, "账号过期");
        }
        //校验密码
        if (!dbUser.getfPassWord().equals(user.getfPassWord())) {
            User errorUser = new User();
            errorUser.setfID(dbUser.getfID());
            errorUser.setfErrorNum(null != dbUser.getfErrorNum() ? dbUser.getfErrorNum() + 1 : 1);
            errorUser.setfLastErrorTime(new Date());
            userDAO.updateLoginError(errorUser);
            return Result.error(AraResultCodeConstants.CODE_1011,"密码错误").putData(errorUser.getfErrorNum());
        }
        //3.校验成功，生成token
        String token = JWTUtil.createToken(dbUser, tokenExpireSeconds * 1000L);
        //4.更新登录时间
        user.setfID(dbUser.getfID());
        user.setfLoginTime(new Date());
        userDAO.updateUserLoginTime(user);
        //5.记录登录日志
        //5.1 设置login log对象信息
        UserLoginLog userLoginLog = createUserLoginLog(dbUser, "IN");
        //5.2 插入login log表
        userLoginLogDAO.insertSelective(userLoginLog);
        //6.查询用户有权限的菜单
//        List<Menu> menus = menuService.selectMenuTreeListByUserID(dbUser.getfID());
        //7.转成菜单树
//        MenuTree menuTree = MenuUtil.getMenuTree(menus);
//        return Result.ok().put("token", token).putData(menuTree);
        Map data = new HashMap(1);
        data.put("token", token);
        //是否为初始化数据
        if (dbUser.getfID().equals("9df1dbbe28e642e3b75e0b1ba0d7b345") && passWord.equals("admins")) {
            return Result.error(AraResultCodeConstants.CODE_1015, "请修改默认密码").putData(data);
        }
        //是否需要修改密码
        if (null != dbUser.getfLastChangePswTime() && DateUtil.getDayBetween(dbUser.getfLastChangePswTime().getTime(), System.currentTimeMillis()) > passwordTime) {
            return Result.error(AraResultCodeConstants.CODE_1015, "密码过期请修改密码").putData(data);
        }
        //更新输错次数
        User user1 = new User();
        user1.setfID(dbUser.getfID());
        user1.setfErrorNum(0);
        user1.setfLastErrorTime(null);
        userDAO.updateLoginError(user1);
        return Result.ok().putData(data);
    }

    @Override
    public Map refreshToken(User user) {
        //1.check user name and password
        if (StringUtil.isBlank(user.getfName())
                || StringUtil.isBlank(user.getfPassWord())) {
            throw new AraAuthException(AraCoreConstants.HTTP_STATUS_UNAUTHORIZED, "Username or password error!");
        }
        //2.从数据库中根据用户名查找用户并校验密码、状态等
        User dbUser = userService.selectUserByName(user.getfName());
        //账号不存在
        if (dbUser == null) {
            throw new AraAuthException(AraCoreConstants.HTTP_STATUS_UNAUTHORIZED, "User didn't existed!");
        }
        //校验密码
        if (!dbUser.getfPassWord().equals(user.getfPassWord())) {
            throw new AraAuthException(AraCoreConstants.HTTP_STATUS_UNAUTHORIZED, "Username or password error!");
        }
        //账号锁定
        if (AraCoreConstants.STATUS_ENABLE != dbUser.getfStatus()) {
            throw new AraAuthException(AraCoreConstants.HTTP_STATUS_UNAUTHORIZED, "Account is locked!");
        }
        //3.校验成功，生成token
        String token = JWTUtil.createToken(dbUser, tokenExpireSeconds * 1000L);
        Map data = new HashMap(1);
        data.put("token", token);
        return Result.ok().putData(data);
    }

    @Override
    public Map logout() {
        //1.在shiro中注销
        Subject currentUser = SecurityUtils.getSubject();
        currentUser.logout();
        User user = CurrentUserUtil.getCurrentUser();
        //2.记录注销日志
        //2.1 设置login log对象信息
        UserLoginLog userLoginLog = createUserLoginLog(user, "OUT");
        //2.2 插入login log表
        userLoginLogDAO.insertSelective(userLoginLog);
        return Result.ok();
    }

    @Override
    public Map appLogin(AppRegister appRegister) {
        //1.校验AppID和SecretKey
        AppRegister appRegister1 = appRegisterDAO.selectAppRegisterByIDAndSecretKey(appRegister);
        if (appRegister1 == null) {
            LOGGER.warn("APP不存在!AppID={},SecretKey={}", appRegister.getfAppID(), appRegister.getfSecretKey());
            return Result.error(AraResultCodeConstants.CODE_1406);
        }
        if (!appRegister.getfAppID().equals(appRegister1.getfAppID()) || !appRegister.getfSecretKey().equals(appRegister1.getfSecretKey())) {
            LOGGER.warn("AppID或key不正确!AppID={},SecretKey={}", appRegister.getfAppID(), appRegister.getfSecretKey());
            return Result.error(AraResultCodeConstants.CODE_1412);
        }
        //2.校验成功，生成token
        String token = JWTUtil.createTokenForApp(appRegister1, tokenExpireSeconds * 1000L);
        Map data = new HashMap(1);
        data.put("token", token);
        return Result.ok().putData(data);
    }

    /**
     * 生成用户登录日志对象
     *
     * @param user
     * @param loginType
     * @return
     */
    private UserLoginLog createUserLoginLog(User user, String loginType) {
        //1.get request
        HttpServletRequest request = HttpContextUtil.getHttpServletRequest();
        //2.create UserLoginLog
        UserLoginLog userLoginLog = new UserLoginLog();
        userLoginLog.setfID(UUIDUtil.genID());
        userLoginLog.setfIP(IPUtil.getIpAddr(request));
        userLoginLog.setfLoginType(loginType);
        userLoginLog.setfUserID(user.getfID());
        userLoginLog.setfLoginDate(new Date());
        userLoginLog.setfEquipmentNo(request.getHeader("EquipmentNo"));
        userLoginLog.setfPlatForm(request.getHeader("PlatForm"));
        return userLoginLog;
    }


}
