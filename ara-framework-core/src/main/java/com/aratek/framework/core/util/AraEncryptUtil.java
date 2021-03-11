package com.aratek.framework.core.util;

import com.aratek.security.gm.SM3Util;
import org.apache.commons.net.util.Base64;

/**
 * @author licy
 * @version 0.0.1
 * @Title AraEncryptUtil
 * @note 加密工具类
 * @note Copyright 2018 by Aratek . All rights reserved
 * @time 2020/03/24
 * @modify
 * @time 2020/03/24
 **/

public class AraEncryptUtil {

    /**
     * 用户名密码加密
     * @param password 密码
     * @param encryptMethod 加密方式
     * @return
     */
    public static String userPawEncrypt(String password, String encryptMethod) {
        if (encryptMethod.equals("SM3")) {
            return Base64.encodeBase64String(SM3Util.hash(password.getBytes())).trim();
        } else {
            return AraMD5Utils.MD5Encode(password,"utf8").trim();
        }
    }
}
