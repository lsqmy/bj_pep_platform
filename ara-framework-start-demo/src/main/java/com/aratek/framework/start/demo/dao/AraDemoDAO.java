package com.aratek.framework.start.demo.dao;

import com.aratek.framework.domain.core.User;
import com.github.pagehelper.Page;

import java.util.List;

public interface AraDemoDAO {

    User findUserDemo(User user);

    List<User> findUserListDemo();

    /**
     * 分页查询数据
     *
     * @return
     */
    Page<User> findByPage();
}
