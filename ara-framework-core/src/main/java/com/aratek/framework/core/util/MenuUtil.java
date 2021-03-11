package com.aratek.framework.core.util;

import com.aratek.framework.core.constant.AraCoreConstants;
import com.aratek.framework.domain.core.MenuTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.*;

/**
 * @author shijinlong
 * @date 2018-06-12
 * @description 菜单帮助类
 */
public class MenuUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(MenuUtil.class);

    /**
     * list to map
     *
     * @param menuTreeList
     * @return
     */
    public static Map<String, MenuTree> list2Map(List<MenuTree> menuTreeList) {
        Map<String, MenuTree> menuTreeMap = new HashMap<String, MenuTree>();
        for (MenuTree menuTree : menuTreeList) {
            menuTreeMap.put(menuTree.getfID(), menuTree);
        }
        return menuTreeMap;
    }

    /**
     * map to list
     *
     * @param menuTreeMap
     * @return
     */
    public static List<MenuTree> map2List(Map<String, MenuTree> menuTreeMap) {
        List<MenuTree> menuTreeList = new ArrayList<MenuTree>();
        for (Map.Entry<String, MenuTree> entry : menuTreeMap.entrySet()) {
            menuTreeList.add(entry.getValue());
        }
        return menuTreeList;
    }

    /**
     * list to map
     *
     * @param list
     * @param keyMethodName 作为map的key的方法名
     * @param c
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> Map<K, V> list2Map(List<V> list, String keyMethodName, Class<V> c) {
        Map<K, V> map = new HashMap<K, V>();
        if (list != null) {
            try {
                Method methodGetKey = c.getMethod(keyMethodName);
                for (int i = 0; i < list.size(); i++) {
                    V value = list.get(i);
                    @SuppressWarnings("unchecked")
                    K key = (K) methodGetKey.invoke(list.get(i));
                    map.put(key, value);
                }
            } catch (Exception e) {
                throw new IllegalArgumentException("field can't match the key!");
            }
        }
        return map;
    }

    /**
     * 菜单排序
     *
     * @param menuTreeList
     */
    public static void sortMenu(List<MenuTree> menuTreeList) {
        if (menuTreeList == null || menuTreeList.size() == 0) {
            return;
        }
        //排序当前list
        Collections.sort(menuTreeList, new Comparator<MenuTree>() {
            /*
             * int compare(Person p1, Person p2) 返回一个基本类型的整型，
             * 返回负数表示：m1小于m2，
             * 返回0 表示：m1和m2相等，
             * 返回正数表示：m1大于m2
             */
            public int compare(MenuTree m1, MenuTree m2) {
                if (AraCoreConstants.NODE_TYPE_MENU.equals(m1.getNodeType())
                        && AraCoreConstants.NODE_TYPE_MENU.equals(m2.getNodeType())) {
//                    LOGGER.debug("1.m1={}", JsonUtil.toJson(m1));
//                    LOGGER.debug("1.m2={}", JsonUtil.toJson(m2));
                    //1.菜单按照displayOrder升序排列
                    if (m1.getfDisplayorder() > m2.getfDisplayorder()) {
                        return 1;
                    } else if (m1.getfDisplayorder() == m2.getfDisplayorder()) {
                        return 0;
                    } else {
                        return -1;
                    }
                } else if (AraCoreConstants.NODE_TYPE_RIGHT.equals(m1.getNodeType())
                        && AraCoreConstants.NODE_TYPE_RIGHT.equals(m2.getNodeType())) {
//                    LOGGER.debug("2.m1={}", JsonUtil.toJson(m1));
//                    LOGGER.debug("2.m2={}", JsonUtil.toJson(m2));
                    //2.权限按照rightName升序排列
                    return m1.getfRightCode().compareTo(m2.getfRightCode());
                } else {
//                    LOGGER.debug("====-1====");
                    return -1;
                }
            }
        });
        //递归对子list排序
        for (MenuTree menuTree : menuTreeList) {
            sortMenu(menuTree.getChildList());
        }
    }
}
