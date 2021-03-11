package com.aratek.framework.core.exception;

/**
 * @author shijinlong
 * @date 2018-04-28
 * @description 权限异常
 */
public class AraAuthException extends AraBaseException {
    public AraAuthException(String msg) {
        super(msg);
    }

    public AraAuthException(String msg, Throwable e) {
        super(msg, e);
    }

    public AraAuthException(String code, String msg) {
        super(code, msg);
    }

    public AraAuthException(String code, String msg, Throwable e) {
        super(code, msg, e);
    }
}
