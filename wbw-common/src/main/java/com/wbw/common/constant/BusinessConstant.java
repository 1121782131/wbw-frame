// com.wbw.common.constant.BusinessConstant.java
package com.wbw.common.constant;

/**
 * 业务常量类
 */
public interface BusinessConstant {
    
    /**
     * 响应状态码
     */
    interface StatusCode {
        int SUCCESS = 200;               // 成功
        int BAD_REQUEST = 400;           // 请求参数错误
        int UNAUTHORIZED = 401;          // 未授权
        int FORBIDDEN = 403;             // 禁止访问
        int NOT_FOUND = 404;             // 资源不存在
        int METHOD_NOT_ALLOWED = 405;    // 方法不允许
        int CONFLICT = 409;              // 资源冲突
        int INTERNAL_SERVER_ERROR = 500; // 服务器内部错误
        int SERVICE_UNAVAILABLE = 503;   // 服务不可用
        int GATEWAY_TIMEOUT = 504;       // 网关超时
    }
    
    /**
     * HTTP Header常量
     */
    interface HttpHeader {
        String AUTHORIZATION = "Authorization";
        String BEARER = "Bearer ";
        String CONTENT_TYPE = "Content-Type";
        String ACCEPT = "Accept";
        String USER_AGENT = "User-Agent";
        String X_REQUESTED_WITH = "X-Requested-With";
        String X_FORWARDED_FOR = "X-Forwarded-For";
        String X_REAL_IP = "X-Real-IP";
        String X_TRACE_ID = "X-Trace-Id";
        String X_TENANT_ID = "X-Tenant-Id";
        String X_USER_ID = "X-User-Id";
        String X_USERNAME = "X-Username";
    }
    
    /**
     * 内容类型常量
     */
    interface ContentType {
        String JSON = "application/json";
        String XML = "application/xml";
        String FORM = "application/x-www-form-urlencoded";
        String MULTIPART = "multipart/form-data";
        String TEXT_PLAIN = "text/plain";
        String TEXT_HTML = "text/html";
        String OCTET_STREAM = "application/octet-stream";
    }
    
    /**
     * 数据库相关常量
     */
    interface Database {
        String DEFAULT_TENANT_ID = "000000";           // 默认租户ID
        String DEFAULT_CREATE_BY = "system";          // 默认创建人
        String DEFAULT_UPDATE_BY = "system";          // 默认更新人
        int DEFAULT_VERSION = 1;                      // 默认版本号
        int NOT_DELETED = 0;                          // 未删除
        int DELETED = 1;                              // 已删除
        int DEFAULT_PAGE_NUM = 1;                     // 默认页码
        int DEFAULT_PAGE_SIZE = 10;                   // 默认页大小
        int MAX_PAGE_SIZE = 1000;                     // 最大页大小
    }
    
    /**
     * 缓存相关常量
     */
    interface Cache {
        String CACHE_KEY_PREFIX = "wbw:";             // 缓存key前缀
        String LOCK_KEY_PREFIX = "lock:";             // 锁key前缀
        String CAPTCHA_KEY_PREFIX = "captcha:";       // 验证码key前缀
        String TOKEN_KEY_PREFIX = "token:";           // token key前缀
        String USER_KEY_PREFIX = "user:";             // 用户key前缀
        String DICT_KEY_PREFIX = "dict:";             // 字典key前缀
        
        int DEFAULT_EXPIRE_TIME = 3600;               // 默认过期时间(秒)
        int CAPTCHA_EXPIRE_TIME = 300;                // 验证码过期时间(秒)
        int TOKEN_EXPIRE_TIME = 7200;                 // token过期时间(秒)
        int LOCK_EXPIRE_TIME = 30;                    // 锁过期时间(秒)
    }
    
    /**
     * 日志相关常量
     */
    interface Log {
        String TRACE_ID = "traceId";                  // 跟踪ID
        String SPAN_ID = "spanId";                    // 跨度ID
        String USER_ID = "userId";                    // 用户ID
        String USERNAME = "username";                 // 用户名
        String TENANT_ID = "tenantId";                // 租户ID
        String OPERATION = "operation";               // 操作
        String MODULE = "module";                     // 模块
        String IP = "ip";                             // IP地址
    }
    
    /**
     * 系统相关常量
     */
    interface System {
        String DEFAULT_CHARSET = "UTF-8";             // 默认字符集
        String DEFAULT_TIMEZONE = "Asia/Shanghai";   // 默认时区
        String SYSTEM_NAME = "wbw-frame";             // 系统名称
        String VERSION = "1.0.0";                     // 系统版本
        String ENV_DEV = "dev";                       // 开发环境
        String ENV_TEST = "test";                     // 测试环境
        String ENV_UAT = "uat";                       // 预发布环境
        String ENV_PROD = "prod";                     // 生产环境
    }
    
    /**
     * 分隔符常量
     */
    interface Separator {
        String COMMA = ",";                           // 逗号分隔符
        String SEMICOLON = ";";                       // 分号分隔符
        String COLON = ":";                           // 冒号分隔符
        String HYPHEN = "-";                          // 连字符分隔符
        String UNDERLINE = "_";                       // 下划线分隔符
        String VERTICAL_BAR = "|";                    // 竖线分隔符
        String SLASH = "/";                           // 斜杠分隔符
        String BACKSLASH = "\\";                      // 反斜杠分隔符
        String ASTERISK = "*";                        // 星号分隔符
        String AMPERSAND = "&";                       // 与号分隔符
        String EQUAL = "=";                           // 等号分隔符
        String QUESTION_MARK = "?";                   // 问号分隔符
        String AT = "@";                              // @分隔符
    }
}