package com.aratek.framework.core.service.impl;

import com.aratek.framework.core.constant.AraCoreConstants;
import com.aratek.framework.core.dao.MailDAO;
import com.aratek.framework.core.service.EmailService;
import com.aratek.framework.core.util.StringUtil;
import com.aratek.framework.core.util.UUIDUtil;
import com.aratek.framework.domain.base.AraPair;
import com.aratek.framework.domain.core.Email;
import freemarker.template.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Date;

/**
 * @author shijinlong
 * @date 2018-05-11
 * @description 邮件服务 实现类
 */
@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailServiceImpl.class);

    /**
     * 发件邮箱
     */
    @Value("${spring.mail.username:araFramework@aratek.com.cn}")
    private String emailFrom;

//    @Value("${spring.mail.templatePath:araFramework@aratek.com.cn}")
//    private String templatePath;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @Autowired
    private MailDAO mailDAO;

    @Override
    public boolean sendSimpleMail(Email email) {
        try {
            //1.记录邮件发送的日志
            email.setfMailFrom(emailFrom);
            email.setfStatus(AraCoreConstants.MAIL_STATUS_SENDING);
            if (!insertMailLog(email)) {
                return false;
            }
            //2.发送邮件
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(emailFrom);
            message.setTo(email.getfMailTo());
            message.setSubject(email.getfSubject());
            message.setText(email.getContent());
            mailSender.send(message);
            //3.更新邮件发送记录的状态
            email.setfStatus(AraCoreConstants.MAIL_STATUS_SEND_SUCCESS);
            if (!updateMailLog(email)) {
                return false;
            }
            return true;
        } catch (Exception e) {
            LOGGER.error("邮件发送失败:{}", e);
            //记录邮件发送失败的日志
            email.setfStatus(AraCoreConstants.MAIL_STATUS_SEND_FAIL);
            updateMailLog(email);
            return false;
        }
    }


    @Override
    public boolean sendAttachmentsMail(Email email) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            //1.记录邮件发送的日志
            email.setfMailFrom(emailFrom);
            email.setfStatus(AraCoreConstants.MAIL_STATUS_SENDING);
            if (!insertMailLog(email)) {
                return false;
            }
            //2.发送邮件
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(emailFrom);
            helper.setTo(email.getfMailTo());
            helper.setSubject(email.getfSubject());
            helper.setText(email.getContent());
            //add attachments
            for (AraPair<String, File> pair : email.getAttachments()) {
                helper.addAttachment(pair.getFirst(), new FileSystemResource(pair.getSecond()));
            }
            mailSender.send(mimeMessage);
            //3.更新邮件发送记录的状态
            email.setfStatus(AraCoreConstants.MAIL_STATUS_SEND_SUCCESS);
            if (!updateMailLog(email)) {
                return false;
            }
            return true;
        } catch (Exception e) {
            LOGGER.error("邮件发送失败:{}", e);
            //记录邮件发送失败的日志
            email.setfStatus(AraCoreConstants.MAIL_STATUS_SEND_FAIL);
            updateMailLog(email);
            return false;
        }
    }

    @Override
    public boolean sendTemplateMail(Email email) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            //1.记录邮件发送的日志
            email.setfMailFrom(emailFrom);
            email.setfStatus(AraCoreConstants.MAIL_STATUS_SENDING);
            if (!insertMailLog(email)) {
                return false;
            }
            //2.发送邮件
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(emailFrom);
            helper.setTo(email.getfMailTo());
            helper.setSubject(email.getfSubject());

//            Map<String, Object> module = new HashMap<String, Object>(2);
//            module.put("username", "zggdczfr");

            //修改 application.properties 文件中的读取路径
//            freeMarkerConfigurer.getConfiguration().setClassLoaderForTemplateLoading(ClassLoader.getSystemClassLoader(), "frameworkftl");
            //读取 ftl 模板
            Template template = freeMarkerConfigurer.getConfiguration().getTemplate(email.getTemplateName());
            String text = FreeMarkerTemplateUtils.processTemplateIntoString(template, email.getKvMap());
            helper.setText(text, true);
            //add attachments
            if (null != email.getAttachments()) {
                for (AraPair<String, File> pair : email.getAttachments()) {
                    helper.addAttachment(pair.getFirst(), new FileSystemResource(pair.getSecond()));
                }
            }
//            FileSystemResource avatar = new FileSystemResource(
//                    new File("F:\\workspace\\SpringBootEmail\\src\\main\\resources\\frameworkftl\\img\\bappy.jpg"));
//            //<img src="cid:avatar" />
//            helper.addInline("avatar", avatar);
            mailSender.send(mimeMessage);
            //3.更新邮件发送记录的状态
            email.setfStatus(AraCoreConstants.MAIL_STATUS_SEND_SUCCESS);
            if (!updateMailLog(email)) {
                return false;
            }
            return true;
        } catch (Exception e) {
            LOGGER.error("邮件发送失败:{}", e);
            //记录邮件发送失败的日志
            email.setfStatus(AraCoreConstants.MAIL_STATUS_SEND_FAIL);
            updateMailLog(email);
            return false;
        }
    }

    @Override
    public boolean insertMailLog(Email email) {
        if (StringUtil.isBlank(email.getfMailTo())
                || StringUtil.isBlank(email.getfSubject())) {
            return false;
        }
        email.setfID(UUIDUtil.genID());
        email.setfBatchID(UUIDUtil.genID());
        email.setfSendTime(new Date());
        //记录邮件发送日志表
        mailDAO.insertEmailLog(email);
        return true;
    }

    @Override
    public boolean updateMailLog(Email email) {
        if (StringUtil.isBlank(email.getfID())
                || null == email.getfStatus()) {
            return false;
        }
        mailDAO.updateEmailLogStatus(email);
        return true;
    }
}
