// com/wbw/jwt/config/JwtProperties.java
package com.wbw.security.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * JWT配置属性
 */
@Data
@Component
@ConfigurationProperties(prefix = "wbw.jwt")
public class JwtProperties {
    
    /**
     * JWT密钥（必须配置）
     */
    private String secret;
    
    /**
     * 访问令牌过期时间（秒），默认2小时
     */
    private Long accessTokenExpire = 7200L;
    
    /**
     * 刷新令牌过期时间（秒），默认30天
     */
    private Long refreshTokenExpire = 2592000L;
    
    /**
     * 令牌头名称
     */
    private String header = "Authorization";
    
    /**
     * 令牌前缀
     */
    private String prefix = "Bearer ";
    
    /**
     * 是否启用自动配置
     */
    private boolean enabled = true;
    
    /**
     * 是否启用安全上下文
     */
    private boolean contextEnabled = true;
    
    /**
     * 默认白名单路径
     */
    private List<String> whiteList = new ArrayList<>();
    
    /**
     * 获取完整的密钥
     */
    public String getSecret() {
        if (secret == null || secret.trim().isEmpty()) {
            throw new IllegalStateException("JWT密钥未配置，请设置wbw.jwt.secret");
        }
        return secret;
    }
    
    /**
     * 获取密钥字节数组
     */
    public byte[] getSecretBytes() {
        return getSecret().getBytes();
    }
}