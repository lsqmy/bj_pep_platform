package com.aratek.framework.core.util;

import com.aratek.framework.domain.base.Result;

import java.util.Map;

/**
 * @author shijinlong
 * @date 2019-01-02
 */
public class TestUtil {

    public static Map testResult(){
        return Result.ok().putMapData("token","tttttttttt");
    }

    public static void main(String[] args) {
        Map map = testResult();
        System.out.println(map);
        System.out.println("over");
    }
}
