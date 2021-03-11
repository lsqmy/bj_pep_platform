package com.aratek.framework.core.util;

import com.google.common.io.BaseEncoding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;

/**
 * @author shijinlong
 * @date 2018-04-28
 * @description Base64帮助类
 * Guava 20 (to be released in early 2016) will be the final Guava release to support Java 6, or even Java 7. Guava 21 (ideally mid-2016) will require Java 8.
 */
public class Base64Util {

    private static final Logger log = LoggerFactory.getLogger(Base64Util.class);

    private static final String UTF8 = "UTF-8";

    /**
     * 编码
     *
     * @param data
     * @return String
     */
    public static String encode(byte[] data) {
        return BaseEncoding.base64().encode(data);
    }

    /**
     * 编码
     *
     * @param data
     * @return String
     */
    public static String encode(String data) {
        try {
            return BaseEncoding.base64().encode(data.getBytes(UTF8));
        } catch (UnsupportedEncodingException e) {
            log.error("Base64Util.encode.error:\n", e);
        }
        return null;
    }

    /**
     * 解码
     *
     * @param data
     * @return byte[]
     */
    public static byte[] decodeToBytes(String data) {
        return BaseEncoding.base64().decode(data);
    }

    /**
     * 解码
     *
     * @param data
     * @return String
     */
    public static String decode(String data) {
        try {
            return new String(BaseEncoding.base64().decode(data), UTF8);
        } catch (UnsupportedEncodingException e) {
            log.error("Base64Util.decode.error:\n", e);
        }
        return null;
    }

}
