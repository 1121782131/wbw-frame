package com.wbw.web.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

/**
 * 身份证验证器实现
 */
public class IdCardValidatorImpl implements ConstraintValidator<IdCard, String> {
    
    private static final Pattern ID_CARD_PATTERN = 
        Pattern.compile("^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$");
    
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // 为空时不校验
        if (StringUtils.isBlank(value)) {
            return true;
        }
        
        if (!ID_CARD_PATTERN.matcher(value).matches()) {
            return false;
        }
        
        // 校验码验证（如果需要更严格的验证）
        return checkCode(value);
    }
    
    /**
     * 校验码验证
     */
    private boolean checkCode(String idCard) {
        if (idCard.length() != 18) {
            return true; // 15位身份证不校验
        }
        
        char[] idCardArray = idCard.toCharArray();
        int[] coefficient = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
        char[] checkCode = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};
        
        int sum = 0;
        for (int i = 0; i < 17; i++) {
            sum += (idCardArray[i] - '0') * coefficient[i];
        }
        
        char expectedCode = checkCode[sum % 11];
        return expectedCode == Character.toUpperCase(idCardArray[17]);
    }
}