package com.aratek.framework.core.config;


import com.aratek.framework.core.constant.AraCoreConstants;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.annotations.ApiIgnore;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;


/**
 * @author shijinlong
 * @date 2018-04-28
 * @description swagger配置类
 */

/**
 * ConditionalOnProperty参数说明：
 * String[] value() default {}; //数组，获取对应property名称的值，与name不可同时使用
 * String prefix() default "";//property名称的前缀，可有可无
 * String[] name() default {};//数组，property完整名称或部分名称（可与prefix组合使用，组成完整的property名称），与value不可同时使用
 * String havingValue() default "";//可与name组合使用，比较获取到的属性值与havingValue给定的值是否相同，相同才加载配置
 * boolean matchIfMissing() default false;//缺少该property时是否可以加载。如果为true，没有该property也会正常加载；反之报错
 * boolean relaxedNames() default true;//是否可以松散匹配
 */
@Configuration
@ConditionalOnClass({Docket.class, ApiInfo.class})
@ConditionalOnProperty(prefix = "aratek.swagger", name = {"enabled"}, havingValue = "true")
@EnableSwagger2
public class SwaggerConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(SwaggerConfig.class);

    @Bean("allApi")
    public Docket allApi(@Value("${ara.swagger.token:token}") String defaultToken) {
        ParameterBuilder parameterBuilder = new ParameterBuilder();
        //1.token
        Parameter token = parameterBuilder
                .parameterType("header") //参数类型，支持header,cookie,body,query etc.
                .name(AraCoreConstants.AUTH_KEY) //参数名
//                .defaultValue(AraCoreConstants.AUTH_KEY) //默认值
                .defaultValue(defaultToken) //默认值
                .description("token") //描述
                .modelRef(new ModelRef("string")) //参数值类型
                .required(false) //非必须
                .build();
        //2.equipment No
        Parameter equipmentNo = parameterBuilder
                .parameterType("header")
                .name("EquipmentNo")
                .defaultValue("swagger")
                .description("设备号/计算机名")
                .modelRef(new ModelRef("string"))
                .required(false)
                .build();
        //3.PlatForm
        Parameter platForm = parameterBuilder
                .parameterType("header")
                .name("PlatForm")
                .defaultValue("PC/win7")
                .description("平台/系统版本")
                .modelRef(new ModelRef("string"))
                .required(false)
                .build();
        //4.Accept-Language
        Parameter acceptLanguage = parameterBuilder
                .parameterType("header")
                .name("Accept-Language")
                .defaultValue("en-US")
                .description("用户语言")
                .modelRef(new ModelRef("string"))
                .required(false)
                .build();
        List<Parameter> parameterList = new ArrayList<Parameter>();
        parameterList.add(token);
        parameterList.add(acceptLanguage);
        parameterList.add(equipmentNo);
        parameterList.add(platForm);
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .groupName("全部接口")
                .select()
                //加了ApiOperation注解的类，才生成接口文档
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                //包下的类，才生成接口文档
//                .apis(RequestHandlerSelectors.basePackage("com.aratek.framework.core.controller"))
                .paths(PathSelectors.any())
                .build()
                .ignoredParameterTypes(ApiIgnore.class)
                .globalOperationParameters(parameterList);
//                .securitySchemes(securityList);
    }

    @Bean("frameworkApi")
    public Docket frameworkApi(@Value("${ara.swagger.token:token}") String defaultToken) {
        ParameterBuilder parameterBuilder = new ParameterBuilder();
        //1.token
        Parameter token = parameterBuilder
                .parameterType("header") //参数类型，支持header,cookie,body,query etc.
                .name(AraCoreConstants.AUTH_KEY) //参数名
//                .defaultValue(AraCoreConstants.AUTH_KEY) //默认值
                .defaultValue(defaultToken) //默认值
                .description("token") //描述
                .modelRef(new ModelRef("string")) //参数值类型
                .required(false) //非必须
                .build();
        //2.equipment No
        Parameter equipmentNo = parameterBuilder
                .parameterType("header")
                .name("EquipmentNo")
                .defaultValue("swagger")
                .description("设备号/计算机名")
                .modelRef(new ModelRef("string"))
                .required(false)
                .build();
        //3.PlatForm
        Parameter platForm = parameterBuilder
                .parameterType("header")
                .name("PlatForm")
                .defaultValue("PC/win7")
                .description("平台/系统版本")
                .modelRef(new ModelRef("string"))
                .required(false)
                .build();
        //4.Accept-Language
        Parameter acceptLanguage = parameterBuilder
                .parameterType("header")
                .name("Accept-Language")
                .defaultValue("en-US")
                .description("用户语言")
                .modelRef(new ModelRef("string"))
                .required(false)
                .build();
        List<Parameter> parameterList = new ArrayList<Parameter>();
        parameterList.add(token);
        parameterList.add(acceptLanguage);
        parameterList.add(equipmentNo);
        parameterList.add(platForm);
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .groupName("统一通用平台接口")
                .select()
                //加了ApiOperation注解的类，才生成接口文档
//                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                //包下的类，才生成接口文档
                .apis(RequestHandlerSelectors.basePackage("com.aratek.framework.core.controller"))
                .paths(PathSelectors.any())
                .build()
                .ignoredParameterTypes(ApiIgnore.class)
                .globalOperationParameters(parameterList);
//                .securitySchemes(securityList);
    }

    private ApiInfo apiInfo() {
        LOGGER.debug("SwaggerConfig.apiInfo");
        return new ApiInfoBuilder()
                .title("亚略特在线API文档")
                .description("aratek-api文档")
                .termsOfServiceUrl("http://www.aratek.com.cn")
                .version("1.0.0")
                .build();
    }

}