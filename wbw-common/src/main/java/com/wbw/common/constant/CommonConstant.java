// com.wbw.common.constant.CommonConstant.java
package com.wbw.common.constant;

/**
 * 通用常量类
 */
public interface CommonConstant {
    
    /**
     * 字符编码
     */
    interface Charset {
        String UTF8 = "UTF-8";
        String GBK = "GBK";
        String ISO88591 = "ISO-8859-1";
    }
    
    /**
     * 日期格式
     */
    interface DateFormat {
        String DATE = "yyyy-MM-dd";
        String DATETIME = "yyyy-MM-dd HH:mm:ss";
        String DATETIME_MS = "yyyy-MM-dd HH:mm:ss.SSS";
        String TIME = "HH:mm:ss";
        String TIME_MS = "HH:mm:ss.SSS";
        String DATE_SLASH = "yyyy/MM/dd";
        String DATETIME_SLASH = "yyyy/MM/dd HH:mm:ss";
        String DATE_COMPACT = "yyyyMMdd";
        String DATETIME_COMPACT = "yyyyMMddHHmmss";
        String DATETIME_MS_COMPACT = "yyyyMMddHHmmssSSS";
    }
    
    /**
     * 数字常量
     */
    interface Number {
        Integer ZERO = 0;
        Integer ONE = 1;
        Integer TWO = 2;
        Integer THREE = 3;
        Integer FOUR = 4;
        Integer FIVE = 5;
        Integer SIX = 6;
        Integer SEVEN = 7;
        Integer EIGHT = 8;
        Integer NINE = 9;
        Integer TEN = 10;
        Integer HUNDRED = 100;
        Integer THOUSAND = 1000;
    }
    
    /**
     * 符号常量
     */
    interface Symbol {
        String DOT = ".";
        String COMMA = ",";
        String COLON = ":";
        String SEMICOLON = ";";
        String HYPHEN = "-";
        String UNDERLINE = "_";
        String SLASH = "/";
        String BACKSLASH = "\\";
        String VERTICAL_BAR = "|";
        String ASTERISK = "*";
        String QUESTION_MARK = "?";
        String AMPERSAND = "&";
        String EQUAL = "=";
        String AT = "@";
        String SHARP = "#";
        String DOLLAR = "$";
        String PERCENT = "%";
        String CARET = "^";
        String PLUS = "+";
        String MINUS = "-";
        String LEFT_PARENTHESIS = "(";
        String RIGHT_PARENTHESIS = ")";
        String LEFT_BRACKET = "[";
        String RIGHT_BRACKET = "]";
        String LEFT_BRACE = "{";
        String RIGHT_BRACE = "}";
        String LESS_THAN = "<";
        String GREATER_THAN = ">";
        String SINGLE_QUOTE = "'";
        String DOUBLE_QUOTE = "\"";
        String BACK_QUOTE = "`";
        String TILDE = "~";
        String EXCLAMATION = "!";
    }
    
    /**
     * 布尔常量
     */
    interface Boolean {
        String TRUE = "true";
        String FALSE = "false";
        String YES = "yes";
        String NO = "no";
        String ON = "on";
        String OFF = "off";
        String ENABLED = "enabled";
        String DISABLED = "disabled";
    }
    
    /**
     * 常用正则表达式
     */
    interface Regex {
        String EMAIL = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        String PHONE = "^1[3-9]\\d{9}$";
        String ID_CARD = "(^\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x)$)";
        String IPV4 = "^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
        String URL = "^(https?|ftp|file)://[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]$";
        String CHINESE = "^[\\u4e00-\\u9fa5]+$";
        String NUMBER = "^[0-9]+$";
        String LETTER = "^[a-zA-Z]+$";
        String LETTER_NUMBER = "^[a-zA-Z0-9]+$";
        String UUID = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$";
    }
    
    /**
     * 文件相关常量
     */
    interface File {
        String KB = "KB";
        String MB = "MB";
        String GB = "GB";
        String TB = "TB";
        String PB = "PB";
        
        Long KB_SIZE = 1024L;
        Long MB_SIZE = 1024 * KB_SIZE;
        Long GB_SIZE = 1024 * MB_SIZE;
        Long TB_SIZE = 1024 * GB_SIZE;
        Long PB_SIZE = 1024 * TB_SIZE;
    }
}