package com.aratek.framework.core.dao;

import com.aratek.framework.domain.base.mapper.BaseDAO;
import com.aratek.framework.domain.core.Email;

/**
 * @author shijinlong
 * @date 2018-05-15
 * @description 邮件DAO
 */
public interface MailDAO extends BaseDAO<Email> {


    int insertEmailLog(Email email);

    int updateEmailLogStatus(Email email);
}
