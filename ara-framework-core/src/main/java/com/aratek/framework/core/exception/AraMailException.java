package com.aratek.framework.core.exception;

/**
 * @author shijinlong
 * @date 2018-05-11
 * @description 邮件异常
 */
public class AraMailException extends AraBaseException {

    public AraMailException(String msg) {
        super(msg);
    }

    public AraMailException(String msg, Throwable e) {
        super(msg, e);
    }

    public AraMailException(String code, String msg) {
        super(code, msg);
    }

    public AraMailException(String code, String msg, Throwable e) {
        super(code, msg, e);
    }
}
