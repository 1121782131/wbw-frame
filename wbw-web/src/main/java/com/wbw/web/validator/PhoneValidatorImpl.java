package com.wbw.web.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

/**
 * 手机号验证器实现
 */
public class PhoneValidatorImpl implements ConstraintValidator<Phone, String> {
    
    private static final Pattern PHONE_PATTERN = 
        Pattern.compile("^1[3-9]\\d{9}$");
    
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // 为空时不校验，由@NotBlank等注解处理
        if (StringUtils.isBlank(value)) {
            return true;
        }
        
        return PHONE_PATTERN.matcher(value).matches();
    }
}