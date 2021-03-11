package com.aratek.framework.core.util;

import com.aratek.framework.core.constant.AraCoreConstants;
import com.aratek.framework.core.exception.AraAuthException;
import com.aratek.framework.domain.core.AppRegister;
import com.aratek.framework.domain.core.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author shijinlong
 * @date 2018-05-03
 * @description JWT帮助类
 */
public class JWTUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(JWTUtil.class);

    /**
     * 从数据声明生成令牌
     *
     * @param claims    数据声明
     * @param ttlMillis 超时时长,单位:毫秒
     * @return base64加密后的令牌
     */
    public static String createToken(Map<String, Object> claims, long ttlMillis) {
        //默认token过期时间为15分钟
        long tokenExpireSeconds = 900;
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        JwtBuilder builder = Jwts.builder()
                .setIssuedAt(now)
                .setClaims(claims)
                .signWith(signatureAlgorithm, AraCoreConstants.SECRET_KEY);
        long expMillis;
        if (ttlMillis > 0) {
            expMillis = nowMillis + ttlMillis;
        } else {
            expMillis = nowMillis + tokenExpireSeconds * 1000L;
        }
        //token过期时间
        Date exp = new Date(expMillis);
        builder.setExpiration(exp);
        String token = builder.compact();
        return Base64Util.encode(token);
    }

    /**
     * 生成令牌
     *
     * @param user      用户
     * @param ttlMillis 超时时长,单位:毫秒
     * @return
     */
    public static String createToken(User user, long ttlMillis) {
        Map<String, Object> claims = new HashMap<String, Object>(2);
        claims.put("tokenType", AraCoreConstants.TOKEN_TYPE_SYS_USER);
        claims.put("fID", user.getfID());
        claims.put("fName", user.getfName());
        return createToken(claims, ttlMillis);
    }

    /**
     * APP生成令牌
     *
     * @param appRegister
     * @param ttlMillis
     * @return
     */
    public static String createTokenForApp(AppRegister appRegister, long ttlMillis) {
        Map<String, Object> claims = new HashMap<String, Object>(2);
        claims.put("tokenType", AraCoreConstants.TOKEN_TYPE_APP);
        claims.put("fID", appRegister.getfID());
        claims.put("fAppID", appRegister.getfAppID());
        return createToken(claims, ttlMillis);
    }

    /**
     * 从令牌中获取数据声明
     *
     * @param token 令牌
     * @return 数据声明
     */
    public static Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(AraCoreConstants.SECRET_KEY)
                    .parseClaimsJws(Base64Util.decode(token))  //base64解密
                    .getBody();
        } catch (Exception e) {
            throw new AraAuthException(AraCoreConstants.HTTP_STATUS_UNAUTHORIZED, "Invalid token.", e);
        }
        return claims;
    }

    /**
     * 从令牌中获取用户信息
     *
     * @param token
     * @return
     */
    public static User getUserFromToken(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            User user = new User();
            user.setfName((String) claims.get("fName"));
            user.setfID((String) claims.get("fID"));
            return user;
        } catch (AraAuthException e) {
            throw e;
        } catch (Exception e) {
            throw new AraAuthException(AraCoreConstants.HTTP_STATUS_UNAUTHORIZED, "Invalid token.", e);
        }
    }

    /**
     * 从令牌中获取ID
     *
     * @param token
     * @return
     */
    public static String getIDFromToken(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            return (String) claims.get("fID");
        } catch (AraAuthException e) {
            throw e;
        } catch (Exception e) {
            throw new AraAuthException(AraCoreConstants.HTTP_STATUS_UNAUTHORIZED, "Invalid token.", e);
        }
    }

    /**
     * 从token获取字段值
     *
     * @param field
     * @return
     */
    public static String getFieldValueFromToken(String field) {
        Claims claims = JWTUtil.getClaimsFromToken(HttpContextUtil.getHttpServletRequest().getHeader(AraCoreConstants.AUTH_KEY));
        return (String) claims.get(field);
    }

    /**
     * 从token获取字段值
     *
     * @param field
     * @param token
     * @return
     */
    public static String getFieldValueFromToken(String field, String token) {
        Claims claims = JWTUtil.getClaimsFromToken(token);
        return (String) claims.get(field);
    }

    /**
     * 判断令牌是否过期
     *
     * @param token 令牌
     * @return 是否过期
     */
    public static Boolean isTokenExpired(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            Date expiration = claims.getExpiration();
            Date now = new Date();
            return expiration.before(now);
        } catch (Exception e) {
            LOGGER.error("Token expired!\n{}", e);
            return true;
        }
    }

    /**
     * 刷新令牌
     *
     * @param token     原令牌
     * @param ttlMillis 超时时长,单位:毫秒
     * @return 新令牌
     */
    public static String refreshToken(String token, long ttlMillis) {
        String refreshedToken;
        try {
            Claims claims = getClaimsFromToken(token);
            claims.put("refresh", new Date());
            refreshedToken = createToken(claims, ttlMillis);
        } catch (Exception e) {
            refreshedToken = null;
        }
        return refreshedToken;
    }

    /**
     * 验证令牌
     *
     * @param token 令牌
     * @param user  用户
     * @return 是否有效
     */
    public static Boolean validateToken(String token, User user) {
        try {
            User jwtUser = getUserFromToken(token);
            if (user == null) {
                return (StringUtil.isNotBlank(jwtUser.getfID())
                        && StringUtil.isNotBlank(jwtUser.getfName())
                        && !isTokenExpired(token));
            }
            return (StringUtil.isNotBlank(jwtUser.getfID())
                    && StringUtil.isNotBlank(jwtUser.getfName())
                    && jwtUser.getfID().equals(user.getfID())
                    && jwtUser.getfName().equals(user.getfName())
                    && !isTokenExpired(token));
        } catch (Exception e) {
            LOGGER.error("Invalid token!\n{}", e);
            return false;
        }
    }

    /**
     * APP验证令牌
     *
     * @param token 令牌
     * @return 是否有效:true,有效;false,无效;
     */
    public static Boolean validateTokenForApp(String token) {
        return !isTokenExpired(token);
    }

    public static void main(String[] args) {
        User user = new User();
        user.setfID("9df1dbbe28e642e3b75e0b1ba0d7b345");
        user.setfName("admins");
        long t = 900 * 1000L;
        String token = JWTUtil.createToken(user, t);
        System.out.println(t);
        System.out.println(token);

        token = "ZXlKaGJHY2lPaUpJVXpJMU5pSjkuZXlKbVNVUWlPaUk1WkdZeFpHSmlaVEk0WlRZME1tVXpZamMxWlRCaU1XSmhNR1EzWWpNME5TSXNJbVpPWVcxbElqb2lZV1J0YVc1eklpd2lkRzlyWlc1VWVYQmxJam9pTVNJc0ltVjRjQ0k2TVRVMU5UWTFPVEk1TTMwLmdtOVhOQjJXd1FRbUxLMDgwczZCR1FMbmtXWl81bGVVUFcwUVpvMGsyUVk=";
        boolean result = JWTUtil.validateTokenForApp(token);
        System.out.println(result);
    }

}
