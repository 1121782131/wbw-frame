// com/wbw/jwt/config/JwtAutoConfiguration.java
package com.wbw.security.config;

import com.wbw.security.aspect.AnonymousAspect;
import com.wbw.security.filter.JwtAuthFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * JWT自动配置
 */
@Configuration
@Import({
    JwtProperties.class
})
@ConditionalOnProperty(prefix = "wbw.jwt", name = "enabled", havingValue = "true", matchIfMissing = true)
public class JwtAutoConfiguration {
    
    /**
     * 注册匿名注解切面
     */
    @Bean
    @ConditionalOnMissingBean
    public AnonymousAspect anonymousAspect() {
        return new AnonymousAspect();
    }
    
    /**
     * 注册JWT过滤器（可选，默认不启用）
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "wbw.jwt.filter", name = "enabled", havingValue = "true")
    public FilterRegistrationBean<JwtAuthFilter> jwtAuthFilterRegistration(JwtAuthFilter filter) {
        FilterRegistrationBean<JwtAuthFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(filter);
        registration.addUrlPatterns("/*");
        registration.setName("jwtAuthFilter");
        registration.setOrder(1);
        return registration;
    }
}