package com.wbw.web.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.Duration;
import java.time.Instant;

/**
 * 增强版日志拦截器，支持请求日志分类
 */
@Component
@Slf4j
public class LogInterceptor implements HandlerInterceptor {
    
    // 创建专门的请求日志记录器
    private static final Logger REQUEST_LOGGER = LoggerFactory.getLogger("REQUEST_LOG");
    private static final Logger ERROR_LOGGER = LoggerFactory.getLogger("ERROR_LOG");
    private static final Logger SLOW_LOGGER = LoggerFactory.getLogger("SLOW_LOG");
    
    private static final ThreadLocal<RequestLogInfo> REQUEST_INFO = new ThreadLocal<>();
    
    // 慢请求阈值（毫秒）
    private static final long SLOW_REQUEST_THRESHOLD = 1000;
    
    @Override
    public boolean preHandle(HttpServletRequest request, 
                            HttpServletResponse response, 
                            Object handler) {
        RequestLogInfo logInfo = new RequestLogInfo();
        logInfo.setStartTime(Instant.now());
        logInfo.setRequestId(generateRequestId());
        logInfo.setIp(getClientIp(request));
        logInfo.setMethod(request.getMethod());
        logInfo.setUri(request.getRequestURI());
        logInfo.setUserAgent(request.getHeader("User-Agent"));
        logInfo.setReferer(request.getHeader("Referer"));
        
        // 记录请求参数（对于GET请求）
        String queryString = request.getQueryString();
        if (StringUtils.isNotEmpty(queryString)) {
            logInfo.setParams(queryString);
        }
        
        REQUEST_INFO.set(logInfo);
        
        // 记录请求开始日志到专门的请求日志文件
        REQUEST_LOGGER.info("请求开始 => 请求ID: {}, 路径: {}, 方法: {}, IP: {}, User-Agent: {}", 
                logInfo.getRequestId(), logInfo.getUri(), logInfo.getMethod(),
                logInfo.getIp(), logInfo.getUserAgent());
        
        return true;
    }
    
    @Override
    public void afterCompletion(HttpServletRequest request, 
                               HttpServletResponse response, 
                               Object handler, 
                               Exception ex) {
        try {
            RequestLogInfo logInfo = REQUEST_INFO.get();
            if (logInfo != null) {
                long duration = Duration.between(logInfo.getStartTime(), Instant.now()).toMillis();
                int status = response.getStatus();
                
                // 构建日志信息
                String logMessage = buildLogMessage(logInfo, duration, status, ex);
                
                // 根据不同情况记录到不同的日志
                if (ex != null) {
                    ERROR_LOGGER.error(logMessage);
                } else if (status >= 400) {
                    ERROR_LOGGER.warn(logMessage);
                } else if (duration > SLOW_REQUEST_THRESHOLD) {
                    SLOW_LOGGER.warn(logMessage);
                } else {
                    REQUEST_LOGGER.info(logMessage);
                }
                
                // 同时输出到控制台（便于调试）
                log.info(logMessage);
            }
        } finally {
            REQUEST_INFO.remove();
        }
    }
    
    /**
     * 构建日志消息
     */
    private String buildLogMessage(RequestLogInfo logInfo, long duration, int status, Exception ex) {
        StringBuilder sb = new StringBuilder();
        sb.append("请求结束 => ");
        sb.append("请求ID: ").append(logInfo.getRequestId()).append(", ");
        sb.append("路径: ").append(logInfo.getMethod()).append(" ").append(logInfo.getUri());
        
        if (StringUtils.isNotEmpty(logInfo.getParams())) {
            sb.append("?").append(logInfo.getParams());
        }
        
        sb.append(", ");
        sb.append("耗时: ").append(duration).append("ms, ");
        sb.append("状态: ").append(status).append(", ");
        sb.append("IP: ").append(logInfo.getIp());
        
        if (StringUtils.isNotEmpty(logInfo.getReferer())) {
            sb.append(", Referer: ").append(logInfo.getReferer());
        }
        
        if (ex != null) {
            sb.append(", 异常: ").append(ex.getClass().getSimpleName()).append(": ").append(ex.getMessage());
        }
        
        return sb.toString();
    }
    
    /**
     * 生成请求ID
     */
    private String generateRequestId() {
        return System.currentTimeMillis() + "-" + 
               Thread.currentThread().getId() + "-" + 
               Double.toString(Math.random()).substring(2, 8);
    }
    
    /**
     * 获取客户端IP地址
     */
    private String getClientIp(HttpServletRequest request) {
        String[] headers = {
            "X-Forwarded-For",
            "X-Real-IP",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_CLIENT_IP",
            "HTTP_X_FORWARDED_FOR"
        };
        
        for (String header : headers) {
            String ip = request.getHeader(header);
            if (StringUtils.isNotEmpty(ip) && !"unknown".equalsIgnoreCase(ip)) {
                // 多次反向代理后会有多个IP值，第一个为真实IP
                int index = ip.indexOf(",");
                return index != -1 ? ip.substring(0, index).trim() : ip.trim();
            }
        }
        
        return request.getRemoteAddr();
    }
    
    /**
     * 请求日志信息类
     */
    static class RequestLogInfo {
        private Instant startTime;
        private String requestId;
        private String ip;
        private String method;
        private String uri;
        private String params;
        private String userAgent;
        private String referer;
        
        // getter和setter方法
        public Instant getStartTime() { return startTime; }
        public void setStartTime(Instant startTime) { this.startTime = startTime; }
        
        public String getRequestId() { return requestId; }
        public void setRequestId(String requestId) { this.requestId = requestId; }
        
        public String getIp() { return ip; }
        public void setIp(String ip) { this.ip = ip; }
        
        public String getMethod() { return method; }
        public void setMethod(String method) { this.method = method; }
        
        public String getUri() { return uri; }
        public void setUri(String uri) { this.uri = uri; }
        
        public String getParams() { return params; }
        public void setParams(String params) { this.params = params; }
        
        public String getUserAgent() { return userAgent; }
        public void setUserAgent(String userAgent) { this.userAgent = userAgent; }
        
        public String getReferer() { return referer; }
        public void setReferer(String referer) { this.referer = referer; }
    }
}