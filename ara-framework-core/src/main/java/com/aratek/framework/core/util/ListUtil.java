package com.aratek.framework.core.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author shijinlong
 * @date 2018-06-20
 * @description List帮助类
 */
public class ListUtil {

    /**
     * list to map
     *
     * @param strList
     * @return
     */
    public static Map<String, String> idList2Map(List<String> strList) {
        if (strList == null || strList.size() == 0) {
            return null;
        }
        Map<String, String> strMap = new HashMap<String, String>();
        for (String str : strList) {
            strMap.put(str, str);
        }
        return strMap;
    }
}
