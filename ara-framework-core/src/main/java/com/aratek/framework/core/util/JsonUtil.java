package com.aratek.framework.core.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * @author shijinlong
 * @date 2018-04-28
 * @description JSON帮助类
 */
public class JsonUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonUtil.class);

    private static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
//        OBJECT_MAPPER.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        OBJECT_MAPPER.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        // 忽略json字符串中不识别的属性
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 忽略无法转换的对象 “No serializer found for class com.xxx.xxx”
        OBJECT_MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        //为NULL不参加序列化
        OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    /**
     * encode object to json string
     *
     * @param obj
     * @return
     */
    public static String toJson(Object obj) {
        try {
            return OBJECT_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            LOGGER.error("JsonUtil.toJson.error:\n{}", e);
        }
        return null;
    }

    /**
     * parse json string to object
     *
     * @param jsonStr
     * @param clazz
     * @return
     */
    public static Object fromJson(String jsonStr, Class<? extends Object> clazz) {
        try {
            return OBJECT_MAPPER.readValue(jsonStr, clazz);
        } catch (IOException e) {
            LOGGER.error("JsonUtil.fromJson.error:\n{}", e);
        }
        return null;
    }

    /*public static void main(String[] args) {
        User user = new User();
        user.setfLoginTime(new Date());
        System.out.println(JsonUtil.toJson(user));
        String str = "{\"fLoginTime\":\"2018-05-15 09:36:27\"}";
        User user2 = (User) JsonUtil.fromJson(str,User.class);
        System.out.println(user2);
    }*/

}
