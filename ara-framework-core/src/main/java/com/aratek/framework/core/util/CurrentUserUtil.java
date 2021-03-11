package com.aratek.framework.core.util;

import com.aratek.framework.core.constant.AraCoreConstants;
import com.aratek.framework.domain.base.CurrentUser;
import com.aratek.framework.domain.core.User;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @author shijinlong
 * @date 2018-05-08
 * @description CurrentUser帮助类
 */
public class CurrentUserUtil {

    /**
     * 从shiro获取当前JWT token，然后解密得到用户ID和Name
     *
     * @return
     */
    public static User getCurrentUser() {
        HttpServletRequest request = HttpContextUtil.getHttpServletRequest();
        String token = request.getHeader(AraCoreConstants.AUTH_KEY);
        return JWTUtil.getUserFromToken(token);
    }

    /**
     * 获取当前用户ID
     *
     * @return
     */
    public static String getCurrentUserID() {
        return getCurrentUser().getfID();
    }

    /**
     * 处理更新人和更新时间
     *
     * @param currentUser
     */
    public static void setOperatorInfo(CurrentUser currentUser) {
        currentUser.setCurrentUserID(getCurrentUserID());
        currentUser.setCurrentUserOperationTime(new Date());
    }
}
