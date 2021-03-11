package com.aratek.framework.domain.base.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shijinlong
 * @date 2018-06-12
 * @description 解析树形数据工具类
 */
public class TreeParser {

    /**
     * 解析树形数据
     *
     * @param topId
     * @param entityList
     * @return
     */
    public static <E extends TreeEntity<E>> List<E> getTreeList(String topId, List<E> entityList) {
        List<E> resultList = new ArrayList<E>();

        //获取顶层元素集合
        String parentId;
        for (E entity : entityList) {
            parentId = entity.getfParentID();
            if (parentId == null || (topId != null && topId.equals(parentId))) {
                resultList.add(entity);
            }
        }

        //获取每个顶层元素的子数据集合
        for (E entity : resultList) {
            entity.setChildList(getSubList(entity.getfID(), entityList));
        }

        return resultList;
    }

    /**
     * 获取子数据集合
     *
     * @param id
     * @param entityList
     * @return
     */
    private static <E extends TreeEntity<E>> List<E> getSubList(String id, List<E> entityList) {
        List<E> childList = new ArrayList<E>();
        String parentId;

        //子集的直接子对象
        for (E entity : entityList) {
            parentId = entity.getfParentID();
            if (id.equals(parentId)) {
                childList.add(entity);
            }
        }

        //子集的间接子对象
        for (E entity : childList) {
            entity.setChildList(getSubList(entity.getfID(), entityList));
        }

        //递归退出条件
        if (childList.size() == 0) {
            return null;
        }

        return childList;
    }
}
