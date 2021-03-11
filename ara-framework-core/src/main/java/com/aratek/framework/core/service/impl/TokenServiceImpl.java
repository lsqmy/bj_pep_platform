//package com.aratek.framework.core.service.impl;
//
//import com.aratek.framework.core.constant.AraCoreConstants;
//import com.aratek.framework.core.exception.AraAuthException;
//import com.aratek.framework.core.service.TokenService;
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.JwtBuilder;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//import java.util.Date;
//
///**
// * @author shijinlong
// * @date 2018-04-28
// * @description Token服务 实现类
// */
//@Service
//public class TokenServiceImpl implements TokenService {
//
////    private static final Logger LOGGER = LoggerFactory.getLogger(TokenServiceImpl.class);
//
//    /**
//     * 默认token过期时间为5分钟
//     */
//    @Value("${ara.token.expireSeconds:300}")
//    private long tokenExpireSeconds;
//
//    @Override
//    public String createToken(String userName, String secret, long ttlMillis) {
//        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
//        long nowMillis = System.currentTimeMillis();
//        Date now = new Date(nowMillis);
//        JwtBuilder builder = Jwts.builder()
////                .setId("")
//                .setIssuedAt(now)
//                .setSubject(userName)
//                .signWith(signatureAlgorithm, secret);
//        long expMillis = nowMillis;
//        if (ttlMillis > 0) {
//            expMillis = nowMillis + ttlMillis;
//        } else {
//            expMillis = nowMillis + tokenExpireSeconds * 1000;
//        }
//        //token过期时间
//        Date exp = new Date(expMillis);
//        builder.setExpiration(exp);
//        return builder.compact();
//    }
//
//    @Override
//    public void expireToken(String userId) {
//
//    }
//
//    @Override
//    public void deleteToken(String userId) {
//
//    }
//
//    @Override
//    public boolean verifyToken(String token, String userName, String secret) {
//        return true;
//    }
//
//
//    @Override
//    public String getUserNameFromToken(String token, String secret) {
//        try {
//            Claims claim = Jwts.parser()
//                    .setSigningKey(secret)
//                    .parseClaimsJws(token)
//                    .getBody();
//            return claim.getSubject();
//        } catch (Exception e) {
//            throw new AraAuthException(AraCoreConstants.HTTP_STATUS_UNAUTHORIZED, "Invalid token.", e);
//        }
//    }
//
//    public static void main(String[] args) {
//        TokenServiceImpl service = new TokenServiceImpl();
//        String token = service.createToken("zhangsan","123123",System.currentTimeMillis()+60000);
//        System.out.println(token);
//        try {
//            Claims claim = Jwts.parser()
//                    .setSigningKey("123123")
//                    .parseClaimsJws(token)
//                    .getBody();
//            System.out.println(claim.getSubject());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//
//}
