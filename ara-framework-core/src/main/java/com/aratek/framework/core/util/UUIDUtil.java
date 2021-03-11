package com.aratek.framework.core.util;

import java.util.UUID;

/**
 * @author shijinlong
 * @date 2018-04-28
 * @description UUID帮助类
 */
public class UUIDUtil {

    /**
     * 统一的生成数据库ID方法
     *
     * @return
     */
    public static String genID() {
        return gen32();
    }

    public static String gen16() {
        int machineId = 1;//最大支持1-9个集群机器部署
        int hashCodeV = UUID.randomUUID().toString().hashCode();
        if (hashCodeV < 0) {//有可能是负数
            hashCodeV = -hashCodeV;
        }
        // 0 代表前面补充0
        // 4 代表长度为4
        // d 代表参数为正数型
        return machineId + String.format("%015d", hashCodeV);
    }

    /**
     * 生成32位UUID，无"-"
     *
     * @return
     */
    public static String gen32() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 生成UUID，无"-"，取中间16位
     *
     * @return
     */
    public static String genMiddle16() {
        return gen32().substring(8,24);
    }

    /*public static void main(String[] args) {
        System.out.println(gen16());
//        for (int i = 0; i < 10; i++) {
//            System.out.println(genID());
//        }
    }*/
}
