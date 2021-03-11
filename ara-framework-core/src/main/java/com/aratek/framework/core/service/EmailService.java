package com.aratek.framework.core.service;


import com.aratek.framework.domain.core.Email;

/**
 * @author shijinlong
 * @date 2018-05-11
 * @description 邮件服务 接口类
 */
public interface EmailService {

    /**
     * 发送简单邮件
     *
     * @param email 邮件对象
     * @return
     */
    boolean sendSimpleMail(Email email);

    /**
     * 发送简单邮件-带附件
     *
     * @param email 邮件对象
     * @return
     */
    boolean sendAttachmentsMail(Email email);

    /**
     * 发送模板邮件
     *
     * @param email 邮件对象
     * @return
     */
    boolean sendTemplateMail(Email email);

    /**
     * 记录邮件发送日志
     *
     * @param email 邮件对象
     * @return
     */
    boolean insertMailLog(Email email);

    /**
     * 更新邮件发送记录的状态
     *
     * @param email 邮件对象
     * @return
     */
    boolean updateMailLog(Email email);
}
