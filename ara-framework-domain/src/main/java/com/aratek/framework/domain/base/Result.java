package com.aratek.framework.domain.base;

import com.aratek.framework.domain.constant.AraDomainConstants;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yanp
 * @version 0.0.1
 * @Title Result
 * @note 控制层返回结果json结构封装
 * @note Copyright 2018 by Aratek . All rights reserved
 * @time 2018/1/10
 * @modify
 * @time 2018/1/10
 **/
public class Result extends HashMap<String, Object> {
    public Result() {
        put(AraDomainConstants.DEFAULT_RESULT_CODE_NAME, AraDomainConstants.DEFAULT_RESULT_CODE_VALUE_OK);
        put(AraDomainConstants.DEFAULT_RESULT_MESSAGE_NAME, AraDomainConstants.DEFAULT_RESULT_MESSAGE_VALUE_OK);
    }


    public static Result error() {
        return error(AraDomainConstants.DEFAULT_RESULT_CODE_VALUE_ERROR, AraDomainConstants.DEFAULT_RESULT_MESSAGE_VALUE_ERROR);
    }

    public static Result error(String code) {
        return error(code, AraDomainConstants.DEFAULT_RESULT_MESSAGE_VALUE_ERROR);
    }

    public static Result error(String code, String msg) {
        Result r = new Result();
        r.put(AraDomainConstants.DEFAULT_RESULT_CODE_NAME, code);
        r.put(AraDomainConstants.DEFAULT_RESULT_MESSAGE_NAME, msg);
        return r;
    }

    public static Result ok(String msg) {
        Result r = new Result();
        r.put(AraDomainConstants.DEFAULT_RESULT_MESSAGE_NAME, msg);
        return r;
    }

    public static Result ok(Map<String, Object> map) {
        Result r = new Result();
        r.putAll(map);
        return r;
    }

    public static Result ok() {
        return new Result();
    }

    @Override
    public Result put(String key, Object value) {
        super.put(key, value);
        return this;
    }

    public Result putData(Object value) {
        super.put("data", value);
        return this;
    }

    public Result putMapData(String key,Object value) {
        Map<String,Object> data = (Map<String, Object>) this.get("data");
        if (data == null){
            data = new HashMap<String, Object>();
        }
        data.put(key,value);
        super.put("data", data);
        return this;
    }
}
