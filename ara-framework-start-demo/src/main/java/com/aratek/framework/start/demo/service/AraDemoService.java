package com.aratek.framework.start.demo.service;


import com.aratek.framework.domain.core.User;
import com.github.pagehelper.Page;

import java.util.List;

public interface AraDemoService {

    User findUserDemo(User user);

    List<User> findUserListDemo(User user);

    /**
     * 分页查询
     *
     * @param pageNo   页号
     * @param pageSize 每页显示记录数
     * @return
     */
    Page<User> findByPage(int pageNo, int pageSize);
}
