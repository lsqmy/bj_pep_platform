//package com.aratek.framework.core.service;
//
///**
// * @author shijinlong
// * @date 2018-04-28
// * @description Token服务 接口类
// */
//public interface TokenService {
//
//    /**
//     * 生成token
//     *
//     * @param userName
//     * @param secret
//     * @return 返回token信息
//     */
//    String createToken(String userName, String secret, long ttlMillis);
//
//    /**
//     * 设置token过期
//     *
//     * @param userId 用户ID
//     */
//    void expireToken(String userId);
//
//    /**
//     * 注销token
//     *
//     * @param userId
//     */
//    void deleteToken(String userId);
//
//    /**
//     * 校验token
//     * @param token
//     * @param userName
//     * @param secret
//     * @return
//     */
//    boolean verifyToken(String token,String userName,String secret);
//
//    /**
//     * 根据token获取用户名
//     * @param token
//     * @param secret
//     * @return
//     */
//    String getUserNameFromToken(String token,String secret);
//
//}
