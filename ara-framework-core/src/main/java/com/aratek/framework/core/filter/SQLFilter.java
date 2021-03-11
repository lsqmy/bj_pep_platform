package com.aratek.framework.core.filter;

import com.aratek.framework.core.exception.AraBaseException;
import com.aratek.framework.core.util.StringUtil;

/**
 * SQL过滤
 */
public class SQLFilter {

    /**
     * SQL注入过滤
     *
     * @param str 待验证的字符串
     */
    public static String sqlInject(String str) {
        if (StringUtil.isBlank(str)) {
            return null;
        }
        //去掉'|"|;|\字符
        str = StringUtil.replace(str, "'", "");
        str = StringUtil.replace(str, "\"", "");
        str = StringUtil.replace(str, ";", "");
        str = StringUtil.replace(str, "\\", "");

        //转换成小写
        str = str.toLowerCase();

        //非法字符
        String[] keywords = {"master", "truncate", "insert", "select", "delete", "update", "declare", "alter", "drop"};

        //判断是否包含非法字符
        for (String keyword : keywords) {
            if (str.indexOf(keyword) != -1) {
                throw new AraBaseException("包含非法字符");
            }
        }

        return str;
    }
}
