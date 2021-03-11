package com.aratek.framework.domain.base.tree;

import java.util.List;

/**
 * @param <E>
 * @author shijinlong
 * @date 2018-06-12
 * @description 树形数据实体接口
 */
public interface TreeEntity<E> {

    String getfID();

    String getfParentID();

    void setChildList(List<E> childList);
}
