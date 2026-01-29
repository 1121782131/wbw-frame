// com/wbw/common/utils/string/StringUtil.java
package com.wbw.common.utils.string;

import cn.hutool.core.util.StrUtil;
import com.wbw.common.constant.CommonConstant;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 */
public final class StringUtil {
    
    private StringUtil() {
        throw new IllegalStateException("Utility class");
    }
    
    /**
     * 判断字符串是否为空
     */
    public static boolean isEmpty(CharSequence str) {
        return StringUtils.isEmpty(str);
    }
    
    public static boolean isNotEmpty(CharSequence str) {
        return StringUtils.isNotEmpty(str);
    }
    
    public static boolean isBlank(CharSequence str) {
        return StringUtils.isBlank(str);
    }
    
    public static boolean isNotBlank(CharSequence str) {
        return StringUtils.isNotBlank(str);
    }
    
    /**
     * 去除空白字符
     */
    public static String trim(String str) {
        return StringUtils.trim(str);
    }
    
    public static String trimToNull(String str) {
        return StringUtils.trimToNull(str);
    }
    
    public static String trimToEmpty(String str) {
        return StringUtils.trimToEmpty(str);
    }
    
    /**
     * 字符串比较
     */
    public static boolean equals(CharSequence cs1, CharSequence cs2) {
        return StringUtils.equals(cs1, cs2);
    }
    
    public static boolean equalsIgnoreCase(CharSequence cs1, CharSequence cs2) {
        return StringUtils.equalsIgnoreCase(cs1, cs2);
    }
    
    /**
     * 字符串连接
     */
    public static String join(Object[] array, String separator) {
        return StringUtils.join(array, separator);
    }
    
    public static String join(Iterable<?> iterable, String separator) {
        return StringUtils.join(iterable, separator);
    }
    
    public static String joinWith(String separator, Object... objects) {
        return StringUtils.joinWith(separator, objects);
    }
    
    /**
     * 字符串分割
     */
    public static String[] split(String str, String separator) {
        return StringUtils.split(str, separator);
    }
    
    public static String[] splitByWholeSeparator(String str, String separator) {
        return StringUtils.splitByWholeSeparator(str, separator);
    }
    
    public static List<String> splitToList(String str, String separator) {
        if (isBlank(str)) {
            return Collections.emptyList();
        }
        return Arrays.asList(split(str, separator));
    }
    
    /**
     * 字符串填充
     */
    public static String leftPad(String str, int size, char padChar) {
        return StringUtils.leftPad(str, size, padChar);
    }
    
    public static String rightPad(String str, int size, char padChar) {
        return StringUtils.rightPad(str, size, padChar);
    }
    
    /**
     * 字符串截取
     */
    public static String substring(String str, int start) {
        return StringUtils.substring(str, start);
    }
    
    public static String substring(String str, int start, int end) {
        return StringUtils.substring(str, start, end);
    }
    
    public static String substringBefore(String str, String separator) {
        return StringUtils.substringBefore(str, separator);
    }
    
    public static String substringAfter(String str, String separator) {
        return StringUtils.substringAfter(str, separator);
    }
    
    public static String substringBetween(String str, String open, String close) {
        return StringUtils.substringBetween(str, open, close);
    }
    
    /**
     * 字符串查找
     */
    public static boolean contains(CharSequence seq, CharSequence searchSeq) {
        return StringUtils.contains(seq, searchSeq);
    }
    
    public static boolean containsIgnoreCase(CharSequence str, CharSequence searchStr) {
        return StringUtils.containsIgnoreCase(str, searchStr);
    }
    
    public static int indexOf(CharSequence seq, CharSequence searchSeq) {
        return StringUtils.indexOf(seq, searchSeq);
    }
    
    public static int lastIndexOf(CharSequence seq, CharSequence searchSeq) {
        return StringUtils.lastIndexOf(seq, searchSeq);
    }
    
    /**
     * 字符串替换
     */
    public static String replace(String text, String searchString, String replacement) {
        return StringUtils.replace(text, searchString, replacement);
    }
    
    public static String replaceIgnoreCase(String text, String searchString, String replacement) {
        return StrUtil.replaceIgnoreCase(text, searchString, replacement);
    }
    
    /**
     * 字符串转换
     */
    public static String toUpperCase(String str) {
        return StringUtils.upperCase(str);
    }
    
    public static String toLowerCase(String str) {
        return StringUtils.lowerCase(str);
    }
    
    public static String capitalize(String str) {
        return StringUtils.capitalize(str);
    }
    
    public static String uncapitalize(String str) {
        return StringUtils.uncapitalize(str);
    }
    
    public static String camelToUnderline(String camelCase) {
        if (isBlank(camelCase)) {
            return camelCase;
        }
        return camelCase.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
    }
    
    public static String underlineToCamel(String underline) {
        if (isBlank(underline)) {
            return underline;
        }
        String[] words = underline.split("_");
        StringBuilder result = new StringBuilder(words[0].toLowerCase());
        for (int i = 1; i < words.length; i++) {
            result.append(capitalize(words[i].toLowerCase()));
        }
        return result.toString();
    }
    
    public static String toCamelCase(String str) {
        return StrUtil.toCamelCase(str);
    }
    
    public static String toUnderlineCase(String str) {
        return StrUtil.toUnderlineCase(str);
    }
    
    /**
     * 验证字符串格式
     */
    public static boolean isEmail(String email) {
        return isMatch(email, CommonConstant.Regex.EMAIL);
    }
    
    public static boolean isPhone(String phone) {
        return isMatch(phone, CommonConstant.Regex.PHONE);
    }
    
    public static boolean isIdCard(String idCard) {
        return isMatch(idCard, CommonConstant.Regex.ID_CARD);
    }
    
    public static boolean isIpv4(String ip) {
        return isMatch(ip, CommonConstant.Regex.IPV4);
    }
    
    public static boolean isUrl(String url) {
        return isMatch(url, CommonConstant.Regex.URL);
    }
    
    public static boolean isChinese(String chinese) {
        return isMatch(chinese, CommonConstant.Regex.CHINESE);
    }
    
    public static boolean isNumber(String number) {
        return isMatch(number, CommonConstant.Regex.NUMBER);
    }
    
    public static boolean isLetter(String letter) {
        return isMatch(letter, CommonConstant.Regex.LETTER);
    }
    
    public static boolean isLetterNumber(String str) {
        return isMatch(str, CommonConstant.Regex.LETTER_NUMBER);
    }
    
    public static boolean isMatch(String str, String regex) {
        if (str == null || regex == null) {
            return false;
        }
        return Pattern.matches(regex, str);
    }
    
    /**
     * 提取字符串中的数字
     */
    public static String extractNumbers(String str) {
        if (isBlank(str)) {
            return "";
        }
        return str.replaceAll("[^0-9]", "");
    }
    
    /**
     * 提取字符串中的字母
     */
    public static String extractLetters(String str) {
        if (isBlank(str)) {
            return "";
        }
        return str.replaceAll("[^a-zA-Z]", "");
    }
    
    /**
     * 提取字符串中的中文
     */
    public static String extractChinese(String str) {
        if (isBlank(str)) {
            return "";
        }
        return str.replaceAll("[^\\u4e00-\\u9fa5]", "");
    }
    
    /**
     * 隐藏部分字符
     */
    public static String hide(String str, int start, int end, char hideChar) {
        if (isBlank(str)) {
            return str;
        }
        if (start < 0) start = 0;
        if (end > str.length()) end = str.length();
        if (start >= end) return str;
        
        char[] chars = str.toCharArray();
        for (int i = start; i < end; i++) {
            chars[i] = hideChar;
        }
        return new String(chars);
    }
    
    /**
     * 隐藏手机号中间4位
     */
    public static String hidePhone(String phone) {
        if (!isPhone(phone)) {
            return phone;
        }
        return phone.substring(0, 3) + "****" + phone.substring(7);
    }
    
    /**
     * 隐藏身份证号中间部分
     */
    public static String hideIdCard(String idCard) {
        if (isBlank(idCard)) {
            return idCard;
        }
        if (idCard.length() == 15) {
            return idCard.substring(0, 6) + "******" + idCard.substring(12);
        } else if (idCard.length() == 18) {
            return idCard.substring(0, 6) + "********" + idCard.substring(14);
        }
        return idCard;
    }
    
    /**
     * 隐藏邮箱中间部分
     */
    public static String hideEmail(String email) {
        if (!isEmail(email)) {
            return email;
        }
        int atIndex = email.indexOf('@');
        if (atIndex <= 2) {
            return email;
        }
        String prefix = email.substring(0, 2);
        String suffix = email.substring(atIndex);
        return prefix + "****" + suffix;
    }

    
    /**
     * 格式化字符串
     */
    public static String format(String template, Object... params) {
        return StrUtil.format(template, params);
    }
    
    /**
     * 去除HTML标签
     */
    public static String stripHtml(String html) {
        if (isBlank(html)) {
            return html;
        }
        return html.replaceAll("<[^>]+>", "");
    }
    
    /**
     * 计算字符串相似度
     */
    public static double similarity(String str1, String str2) {
        if (str1 == null || str2 == null) {
            return 0.0;
        }
        if (str1.equals(str2)) {
            return 1.0;
        }
        
        int len1 = str1.length();
        int len2 = str2.length();
        int[][] dp = new int[len1 + 1][len2 + 1];
        
        for (int i = 0; i <= len1; i++) {
            dp[i][0] = i;
        }
        for (int j = 0; j <= len2; j++) {
            dp[0][j] = j;
        }
        
        for (int i = 1; i <= len1; i++) {
            char c1 = str1.charAt(i - 1);
            for (int j = 1; j <= len2; j++) {
                char c2 = str2.charAt(j - 1);
                if (c1 == c2) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = Math.min(Math.min(dp[i - 1][j], dp[i][j - 1]), dp[i - 1][j - 1]) + 1;
                }
            }
        }
        
        int maxLen = Math.max(len1, len2);
        if (maxLen == 0) {
            return 1.0;
        }
        return 1.0 - (double) dp[len1][len2] / maxLen;
    }
    
    /**
     * 判断字符串是否为JSON格式
     */
    public static boolean isJson(String str) {
        if (isBlank(str)) {
            return false;
        }
        str = trim(str);
        return (str.startsWith("{") && str.endsWith("}")) || (str.startsWith("[") && str.endsWith("]"));
    }
    
    /**
     * 判断字符串是否为XML格式
     */
    public static boolean isXml(String str) {
        if (isBlank(str)) {
            return false;
        }
        str = trim(str);
        return str.startsWith("<") && str.endsWith(">");
    }
    
    /**
     * 字符串反转
     */
    public static String reverse(String str) {
        if (isBlank(str)) {
            return str;
        }
        return new StringBuilder(str).reverse().toString();
    }
    
    /**
     * 计算字符串出现次数
     */
    public static int countOccurrences(String str, String sub) {
        if (isBlank(str) || isBlank(sub)) {
            return 0;
        }
        return (str.length() - str.replace(sub, "").length()) / sub.length();
    }
    
    /**
     * 去除重复字符
     */
    public static String removeDuplicates(String str) {
        if (isBlank(str)) {
            return str;
        }
        Set<Character> seen = new LinkedHashSet<>();
        for (char c : str.toCharArray()) {
            seen.add(c);
        }
        StringBuilder sb = new StringBuilder();
        for (Character c : seen) {
            sb.append(c);
        }
        return sb.toString();
    }
}