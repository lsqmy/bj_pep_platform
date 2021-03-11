package com.aratek.framework.core.shiro;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.SubjectContext;
import org.apache.shiro.web.mgt.DefaultWebSubjectFactory;

/**
 * @author shijinlong
 * @date 2018-05-22
 * @description Subject工厂
 */
public class StatelessDefaultSubjectFactory extends DefaultWebSubjectFactory {

    @Override
    public Subject createSubject(SubjectContext context) {
        //不创建 session
        context.setSessionCreationEnabled(false);
        return super.createSubject(context);
    }
}
