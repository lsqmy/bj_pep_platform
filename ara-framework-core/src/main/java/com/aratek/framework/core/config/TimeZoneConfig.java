package com.aratek.framework.core.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

import javax.annotation.PostConstruct;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author shijinlong
 * @date 2018-05-08
 * @description 时区配置类
 * 默认时区设置为GMT+8
 * 默认时间格式为yyyy-MM-dd HH:mm:ss
 */
@Configuration
public class TimeZoneConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(TimeZoneConfig.class);

    @Value("${ara.timeZone:GMT+8}")
    private String timeZone;

    @Value("${ara.dateFormatPattern:yyyy-MM-dd HH:mm:ss}")
    private String dateFormatPattern;

    @PostConstruct
    void timeZoneInit() {
        TimeZone.setDefault(TimeZone.getTimeZone(timeZone));
    }

    @Bean
    public Converter<String, Date> addDateConvert() {
        LOGGER.debug("TimeZoneConfig.timeZone={}", timeZone);
        LOGGER.debug("TimeZoneConfig.dateFormatPattern={}", dateFormatPattern);
        return new Converter<String, Date>() {
            @Override
            public Date convert(String source) {
                SimpleDateFormat sdf = new SimpleDateFormat(dateFormatPattern);
                Date date = null;
                try {
                    date = sdf.parse(source);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return date;
            }
        };
    }
}
