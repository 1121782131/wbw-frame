package com.wbw.mybatis.annotation;

import java.lang.annotation.*;

/**
 * 数据源注解
 * 用于在方法级别指定使用的数据源
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface DataSource {
    
    /**
     * 数据源名称
     * 
     * @return 数据源名称
     */
    String value();
    
    /**
     * 是否在方法执行后恢复默认数据源
     * 
     * @return 是否恢复默认数据源
     */
    boolean restore() default true;
}
