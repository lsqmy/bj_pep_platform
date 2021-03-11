package com.aratek.framework.core.util;

import com.google.common.hash.Hashing;

/**
 * @author shijinlong
 * @date 2018-05-21
 * @description Hash帮助类
 */
public class HashUtil {

    /**
     * 计算MD5
     *
     * @param bytes
     * @return
     */
    public static String md5(byte[] bytes) {
        return Hashing.md5().hashBytes(bytes).toString().toUpperCase();
    }

    /**
     * 计算sha1
     *
     * @param bytes
     * @return
     */
    public static String sha1(byte[] bytes) {
        return Hashing.sha1().hashBytes(bytes).toString().toUpperCase();
    }

    /**
     * 计算sha256
     *
     * @param bytes
     * @return
     */
    public static String sha256(byte[] bytes) {
        return Hashing.sha256().hashBytes(bytes).toString().toUpperCase();
    }

    /**
     * 计算sha512
     *
     * @param bytes
     * @return
     */
    public static String sha512(byte[] bytes) {
        return Hashing.sha512().hashBytes(bytes).toString().toUpperCase();
    }


    /*public static void main(String[] args) {
        String input = "hello, world";
        // 计算MD5
        System.out.println(HashUtil.md5(input.getBytes()));
        // 计算sha256
        System.out.println(Hashing.sha256().hashBytes(input.getBytes()).toString());
        // 计算sha512
        System.out.println(Hashing.sha512().hashBytes(input.getBytes()).toString());
        // 计算crc32
        System.out.println(Hashing.crc32().hashBytes(input.getBytes()).toString());

        System.out.println(Hashing.md5().hashUnencodedChars(input).toString());
    }*/
}
