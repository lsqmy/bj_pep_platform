package com.aratek.framework.core.util;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author shijinlong
 * @date 2018-06-25
 * @description 分页帮助类
 */
public class PageUtil {

    /**
     * 默认排序字段
     */
    public static final String DEFAULT_SORT_STR = "fLastUpdateTime desc";

    public static final String LOGIN_LOG_SORT_STR = "fLoginDate desc";
    public static final String USER_LOG_SORT_STR = "fCreateTime desc";

    /**
     * 组装排序字段1
     *
     * @param sortParams
     * @return
     */
    public static String getSortStr(LinkedHashMap<String, String> sortParams) {
        if (sortParams == null || sortParams.size() == 0) {
            return DEFAULT_SORT_STR;
        }
        StringBuilder sb = new StringBuilder();
        for (Map.Entry entry : sortParams.entrySet()) {
            sb.append(",").append(entry.getKey()).append(" ").append(entry.getValue());
        }
        return sb.toString().substring(1);
    }

    /**
     * 组装排序字段2
     *
     * @param sortParams
     * @param sortStr
     * @return
     */
    public static String getSortStr(LinkedHashMap<String, String> sortParams, String sortStr) {
        if (sortParams == null || sortParams.size() == 0) {
            return sortStr;
        }
        StringBuilder sb = new StringBuilder();
        for (Map.Entry entry : sortParams.entrySet()) {
            sb.append(",").append(entry.getKey()).append(" ").append(entry.getValue());
        }
        return sb.toString().substring(1);
    }

    /**
     * 组装排序字段3
     * 对Jackson转换名称的对象做反转
     *
     * @param sortParams
     * @param clazz
     * @return
     */
    public static String getSortStr(LinkedHashMap<String, String> sortParams, Class clazz) {
        if (sortParams == null || sortParams.size() == 0) {
            return DEFAULT_SORT_STR;
        }
        StringBuilder sb = new StringBuilder();
        for (Map.Entry entry : sortParams.entrySet()) {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                JsonProperty jsonProperty = field.getAnnotation(JsonProperty.class);
                if (jsonProperty != null) {
                    if (entry.getKey().equals(jsonProperty.value())) {
                        field.getName();
                        sb.append(",").append(field.getName()).append(" ").append(entry.getValue());
                        break;
                    }
                }
            }
        }
        return sb.toString().substring(1);
    }


//    public static void main(String[] args) {
//        LinkedHashMap map = new LinkedHashMap();
//        map.put("a","asc");
//        map.put("b","desc");
//        map.put("c","desc");
//        map.put("d","desc");
//        map.put("aa","desc");
//        System.out.println(getSortStr(map));
////        System.out.println(getSortStr(null));
//    }
}
