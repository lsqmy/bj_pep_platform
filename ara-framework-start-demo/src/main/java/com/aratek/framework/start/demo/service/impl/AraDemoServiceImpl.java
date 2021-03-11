package com.aratek.framework.start.demo.service.impl;

import com.aratek.framework.domain.core.User;
import com.aratek.framework.start.demo.dao.AraDemoDAO;
import com.aratek.framework.start.demo.service.AraDemoService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AraDemoServiceImpl implements AraDemoService {

    @Autowired
    private AraDemoDAO araDemoDAO;

    @Override
    public User findUserDemo(User user) {
        return araDemoDAO.findUserDemo(user);
    }

    @Override
    public List<User> findUserListDemo(User user) {
        return araDemoDAO.findUserListDemo();
    }

    @Override
    public Page<User> findByPage(int pageNo, int pageSize) {
        PageHelper.startPage(pageNo, pageSize);
        return araDemoDAO.findByPage();
    }
}
