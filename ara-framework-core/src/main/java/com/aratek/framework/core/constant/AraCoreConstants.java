package com.aratek.framework.core.constant;

/**
 * @author shijinlong
 * @date 2018-04-28
 * @description core模块常量类
 */
public class AraCoreConstants {

    /**
     * token类型:1,系统用户;2,APP;
     */
    public static final String TOKEN_TYPE_SYS_USER = "1";
    public static final String TOKEN_TYPE_APP = "2";

    /**
     * 密钥
     */
    public static final String SECRET_KEY = "MHcCAQEEIKTna+5lOYrjhhp3xth58Ef3WosBb/haSpjj/VvQszNaoAoGCCqGSM49AwEHoUQDQgAE2DhX3HIW9uv8IOX40PpKDw2945ZVfUMif5Q6/Sj9doreq2kLcqVwomz8nKFKXxRtEUCR6zdbyl9xvEuBRmO4TQ==";

    /**
     * http header中token param名称
     */
    public static final String AUTH_KEY = "Authorization";

    /**
     * Unauthorized:未登录的HTTP响应码
     */
    public static final String HTTP_STATUS_UNAUTHORIZED = "401";

    /**
     * Forbidden:没有权限的HTTP响应码
     */
    public static final String HTTP_STATUS_FORBIDDEN = "403";

    public static final String EHCACHE_CONFIG_FILE = "classpath:ehcache-shiro.xml";


    /**
     * 状态
     * 1,保存
     * 2,启用
     * 3,禁用
     */
    public static final int STATUS_SAVE = 1;
    public static final int STATUS_ENABLE = 2;
    public static final int STATUS_DISABLE = 3;

    /**
     * 邮件发送状态
     * 0待发送,1发送中,2发送成功,3发送失败
     */
    public static final int MAIL_STATUS_WAIT_SEND = 0;
    public static final int MAIL_STATUS_SENDING = 1;
    public static final int MAIL_STATUS_SEND_SUCCESS = 2;
    public static final int MAIL_STATUS_SEND_FAIL = 3;

    /**
     * boolean
     * 0,否;1,是;
     */
    public static final int YES = 1;
    public static final int NO = 0;

    public static final String TRUE = "true";
    public static final String FALSE = "false";

    /**
     * 节点类型:1,菜单;2,权限;
     */
    public static final String NODE_TYPE_MENU = "1";
    public static final String NODE_TYPE_RIGHT = "2";

    /**
     * 导出excel名称前缀和后缀
     */
    public static final String EXCEL_PREFIX = "excel_";
    public static final String EXCEL_SUFFIX = ".xlsx";

    /**
     * 模块
     */
    public static final String MODULE_MENU = "menu";
    public static final String MODULE_MENU_RIGHT = "menuRight";
    public static final String MODULE_USER = "user";
    public static final String MODULE_USER_LOG = "userLog";
    public static final String MODULE_USER_LOGIN_LOG = "userLoginLog";
    public static final String MODULE_USER_ROLE_RIGHT = "userRoleRight";
    public static final String MODULE_ROLE = "role";
    public static final String MODULE_FTP = "ftp";
    public static final String MODULE_PARAM = "param";
    public static final String MODULE_PARAM_RIGHT = "paramRight";
    public static final String MODULE_COUNTRY = "country";
    public static final String MODULE_PROVINCE = "province";
    public static final String MODULE_CITY = "city";
    public static final String MODULE_REGION = "region";
    public static final String MODULE_APP_INFO = "appInfo";
    public static final String MODULE_APP_REGISTER = "appRegister";
    public static final String MODULE_APP_INFO_WHITE_RULE = "appInfoWhiteRule";

    /**
     * excel模板路径
     */
    public static final String EXCEL_TEMPLATE_PATH = "template/excel/";
    /**
     * excel模板前缀
     */
    public static final String EXCEL_TEMPLATE_PREFIX = "template_";
    /**
     * excel模板后缀
     */
    public static final String EXCEL_TEMPLATE_SUFFIX = ".xlsx";
    /**
     * excel模板
     */
    public static final String EXCEL_TEMPLATE_MENU = EXCEL_TEMPLATE_PATH + EXCEL_TEMPLATE_PREFIX + MODULE_MENU + EXCEL_TEMPLATE_SUFFIX;
    public static final String EXCEL_TEMPLATE_MENU_RIGHT = EXCEL_TEMPLATE_PATH + EXCEL_TEMPLATE_PREFIX + MODULE_MENU_RIGHT + EXCEL_TEMPLATE_SUFFIX;
    public static final String EXCEL_TEMPLATE_USER = EXCEL_TEMPLATE_PATH + EXCEL_TEMPLATE_PREFIX + MODULE_USER + EXCEL_TEMPLATE_SUFFIX;
    public static final String EXCEL_TEMPLATE_USER_LOG = EXCEL_TEMPLATE_PATH + EXCEL_TEMPLATE_PREFIX + MODULE_USER_LOG + EXCEL_TEMPLATE_SUFFIX;
    public static final String EXCEL_TEMPLATE_USER_LOGIN_LOG = EXCEL_TEMPLATE_PATH + EXCEL_TEMPLATE_PREFIX + MODULE_USER_LOGIN_LOG + EXCEL_TEMPLATE_SUFFIX;
    public static final String EXCEL_TEMPLATE_USER_ROLE_RIGHT = EXCEL_TEMPLATE_PATH + EXCEL_TEMPLATE_PREFIX + MODULE_USER_ROLE_RIGHT + EXCEL_TEMPLATE_SUFFIX;
    public static final String EXCEL_TEMPLATE_ROLE = EXCEL_TEMPLATE_PATH + EXCEL_TEMPLATE_PREFIX + MODULE_ROLE + EXCEL_TEMPLATE_SUFFIX;
    public static final String EXCEL_TEMPLATE_FTP = EXCEL_TEMPLATE_PATH + EXCEL_TEMPLATE_PREFIX + MODULE_FTP + EXCEL_TEMPLATE_SUFFIX;
    public static final String EXCEL_TEMPLATE_PARAM = EXCEL_TEMPLATE_PATH + EXCEL_TEMPLATE_PREFIX + MODULE_PARAM + EXCEL_TEMPLATE_SUFFIX;
    public static final String EXCEL_TEMPLATE_PARAM_RIGHT = EXCEL_TEMPLATE_PATH + EXCEL_TEMPLATE_PREFIX + MODULE_PARAM_RIGHT + EXCEL_TEMPLATE_SUFFIX;
    public static final String EXCEL_TEMPLATE_COUNTRY = EXCEL_TEMPLATE_PATH + EXCEL_TEMPLATE_PREFIX + MODULE_COUNTRY + EXCEL_TEMPLATE_SUFFIX;
    public static final String EXCEL_TEMPLATE_PROVINCE = EXCEL_TEMPLATE_PATH + EXCEL_TEMPLATE_PREFIX + MODULE_PROVINCE + EXCEL_TEMPLATE_SUFFIX;
    public static final String EXCEL_TEMPLATE_CITY = EXCEL_TEMPLATE_PATH + EXCEL_TEMPLATE_PREFIX + MODULE_CITY + EXCEL_TEMPLATE_SUFFIX;
    public static final String EXCEL_TEMPLATE_REGION = EXCEL_TEMPLATE_PATH + EXCEL_TEMPLATE_PREFIX + MODULE_REGION + EXCEL_TEMPLATE_SUFFIX;
    public static final String EXCEL_TEMPLATE_APP_INFO = EXCEL_TEMPLATE_PATH + EXCEL_TEMPLATE_PREFIX + MODULE_APP_INFO + EXCEL_TEMPLATE_SUFFIX;
    public static final String EXCEL_TEMPLATE_APP_REGISTER = EXCEL_TEMPLATE_PATH + EXCEL_TEMPLATE_PREFIX + MODULE_APP_REGISTER + EXCEL_TEMPLATE_SUFFIX;
    public static final String EXCEL_TEMPLATE_APP_INFO_WHITE_RULE = EXCEL_TEMPLATE_PATH + EXCEL_TEMPLATE_PREFIX + MODULE_APP_INFO_WHITE_RULE + EXCEL_TEMPLATE_SUFFIX;

}
