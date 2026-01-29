package com.wbw.web.config;

import com.alibaba.fastjson2.JSONFactory;
import com.alibaba.fastjson2.support.config.FastJsonConfig;
import com.alibaba.fastjson2.support.spring6.http.converter.FastJsonHttpMessageConverter;
import com.wbw.web.handler.LogInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Web MVC配置
 */
@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final LogInterceptor logInterceptor;
    
    /**
     * 配置Fastjson2为默认JSON处理器
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        // 移除默认的Jackson转换器
        converters.removeIf(converter -> converter.getClass().getName().contains("MappingJackson2"));
        
        // 添加Fastjson2转换器
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        FastJsonConfig config = new FastJsonConfig();
        
        // 配置Fastjson2
        config.setDateFormat("yyyy-MM-dd HH:mm:ss");
        config.setCharset(StandardCharsets.UTF_8);
        
        // 配置序列化特性
        com.alibaba.fastjson2.JSONWriter.Feature[] writerFeatures = {
            com.alibaba.fastjson2.JSONWriter.Feature.WriteMapNullValue,
            com.alibaba.fastjson2.JSONWriter.Feature.PrettyFormat
        };
        config.setWriterFeatures(writerFeatures);
        
        converter.setFastJsonConfig(config);
        
        // 设置支持的MediaType
        List<MediaType> mediaTypes = new ArrayList<>();
        mediaTypes.add(MediaType.APPLICATION_JSON);
        mediaTypes.add(MediaType.TEXT_PLAIN);
        converter.setSupportedMediaTypes(mediaTypes);
        
        converters.add(0, converter);
    }
    
    /**
     * 注册拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 日志拦截器
        registry.addInterceptor(logInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/swagger-ui/**", "/v3/api-docs/**", "/webjars/**");
    }
    
    /**
     * 静态资源配置
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
    
    /**
     * 创建Fastjson2配置Bean
     */
    @Bean
    @ConditionalOnMissingBean
    public FastJsonConfig fastJsonConfig() {
        FastJsonConfig config = new FastJsonConfig();
        config.setDateFormat("yyyy-MM-dd HH:mm:ss");
        config.setCharset(StandardCharsets.UTF_8);
        
        // 全局配置JSONFactory
        JSONFactory.setUseJacksonAnnotation(false);
        
        return config;
    }
}