package com.aratek.framework.core.exception;

/**
 * @author shijinlong
 * @date 2018-04-28
 * @description 自定义异常:基本异常类
 */
public class AraBaseException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private String code;
    private String msg;

    public AraBaseException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public AraBaseException(String msg, Throwable e) {
        super(msg, e);
        this.msg = msg;
    }

    public AraBaseException(String code, String msg) {
        super(msg);
        this.msg = msg;
        this.code = code;
    }

    public AraBaseException(String code, String msg, Throwable e) {
        super(msg, e);
        this.msg = msg;
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


}
