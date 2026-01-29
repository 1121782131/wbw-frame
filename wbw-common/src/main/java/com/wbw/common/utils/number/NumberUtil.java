// com/wbw/common/utils/number/NumberUtil.java
package com.wbw.common.utils.number;

import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;
import com.wbw.common.utils.string.StringUtil;
import org.apache.commons.lang3.math.NumberUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;

/**
 * 数字工具类
 */
public final class NumberUtil {
    
    private NumberUtil() {
        throw new IllegalStateException("Utility class");
    }
    
    /**
     * 判断是否为数字
     */
    public static boolean isNumber(String str) {
        return NumberUtils.isCreatable(str);
    }
    
    public static boolean isDigits(String str) {
        return NumberUtils.isDigits(str);
    }
    
    public static boolean isInteger(String str) {
        if (StringUtil.isBlank(str)) {
            return false;
        }
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    public static boolean isLong(String str) {
        if (StringUtil.isBlank(str)) {
            return false;
        }
        try {
            Long.parseLong(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    public static boolean isDouble(String str) {
        if (StringUtil.isBlank(str)) {
            return false;
        }
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    /**
     * 数字转换
     */
    public static Integer toInt(String str) {
        return toInt(str, null);
    }
    
    public static Integer toInt(String str, Integer defaultValue) {
        return NumberUtils.toInt(str, defaultValue);
    }
    
    public static Long toLong(String str) {
        return toLong(str, null);
    }
    
    public static Long toLong(String str, Long defaultValue) {
        return NumberUtils.toLong(str, defaultValue);
    }
    
    public static Double toDouble(String str) {
        return toDouble(str, null);
    }
    
    public static Double toDouble(String str, Double defaultValue) {
        return NumberUtils.toDouble(str, defaultValue);
    }
    
    public static BigDecimal toBigDecimal(String str) {
        return toBigDecimal(str, BigDecimal.ZERO);
    }
    
    public static BigDecimal toBigDecimal(String str, BigDecimal defaultValue) {
        if (StringUtil.isBlank(str)) {
            return defaultValue;
        }
        try {
            return new BigDecimal(str.trim());
        } catch (Exception e) {
            return defaultValue;
        }
    }
    
    /**
     * 数字比较
     */
    public static int compare(Number x, Number y) {
        return NumberUtils.compare(x.intValue(), y.intValue());
    }
    
    public static boolean isGreaterThan(Number x, Number y) {
        return compare(x, y) > 0;
    }
    
    public static boolean isGreaterThanOrEqual(Number x, Number y) {
        return compare(x, y) >= 0;
    }
    
    public static boolean isLessThan(Number x, Number y) {
        return compare(x, y) < 0;
    }
    
    public static boolean isLessThanOrEqual(Number x, Number y) {
        return compare(x, y) <= 0;
    }
    
    public static boolean isEqual(Number x, Number y) {
        return compare(x, y) == 0;
    }
    
    /**
     * 获取最大值
     */
    public static int max(int... array) {
        return NumberUtils.max(array);
    }
    
    public static long max(long... array) {
        return NumberUtils.max(array);
    }
    
    public static double max(double... array) {
        return NumberUtils.max(array);
    }
    
    public static BigDecimal max(BigDecimal... array) {
        if (array == null || array.length == 0) {
            return null;
        }
        BigDecimal max = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] != null && array[i].compareTo(max) > 0) {
                max = array[i];
            }
        }
        return max;
    }
    
    /**
     * 获取最小值
     */
    public static int min(int... array) {
        return NumberUtils.min(array);
    }
    
    public static long min(long... array) {
        return NumberUtils.min(array);
    }
    
    public static double min(double... array) {
        return NumberUtils.min(array);
    }
    
    public static BigDecimal min(BigDecimal... array) {
        if (array == null || array.length == 0) {
            return null;
        }
        BigDecimal min = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] != null && array[i].compareTo(min) < 0) {
                min = array[i];
            }
        }
        return min;
    }
    
    /**
     * 数字运算
     */
    public static BigDecimal add(Number x, Number y) {
        return add(x, y, 2);
    }
    
    public static BigDecimal add(Number x, Number y, int scale) {
        BigDecimal b1 = toBigDecimal(x.toString());
        BigDecimal b2 = toBigDecimal(y.toString());
        return b1.add(b2).setScale(scale, RoundingMode.HALF_UP);
    }
    
    public static BigDecimal subtract(Number x, Number y) {
        return subtract(x, y, 2);
    }
    
    public static BigDecimal subtract(Number x, Number y, int scale) {
        BigDecimal b1 = toBigDecimal(x.toString());
        BigDecimal b2 = toBigDecimal(y.toString());
        return b1.subtract(b2).setScale(scale, RoundingMode.HALF_UP);
    }
    
    public static BigDecimal multiply(Number x, Number y) {
        return multiply(x, y, 2);
    }
    
    public static BigDecimal multiply(Number x, Number y, int scale) {
        BigDecimal b1 = toBigDecimal(x.toString());
        BigDecimal b2 = toBigDecimal(y.toString());
        return b1.multiply(b2).setScale(scale, RoundingMode.HALF_UP);
    }
    
    public static BigDecimal divide(Number x, Number y) {
        return divide(x, y, 2);
    }
    
    public static BigDecimal divide(Number x, Number y, int scale) {
        if (y == null || y.doubleValue() == 0) {
            throw new ArithmeticException("除数不能为零");
        }
        BigDecimal b1 = toBigDecimal(x.toString());
        BigDecimal b2 = toBigDecimal(y.toString());
        return b1.divide(b2, scale, RoundingMode.HALF_UP);
    }






    /**
     * 格式化数字
     */
    public static String format(double number, String pattern) {
        DecimalFormat df = new DecimalFormat(pattern);
        return df.format(number);
    }

    public static String format(double number) {
        return format(number, "#,##0.00");
    }

    public static String format(BigDecimal number) {
        return format(number, "#,##0.00");
    }

    public static String format(BigDecimal number, String pattern) {
        if (number == null) {
            return "0.00";
        }
        DecimalFormat df = new DecimalFormat(pattern);
        return df.format(number);
    }

    /**
     * 百分比计算
     */
    public static double percent(double part, double total) {
        if (total == 0) {
            return 0;
        }
        return (part / total) * 100;
    }

    public static String percentStr(double part, double total) {
        return format(percent(part, total), "0.00") + "%";
    }

    /**
     * 计算增长率
     */
    public static double growthRate(double current, double previous) {
        if (previous == 0) {
            return current == 0 ? 0 : 100;
        }
        return ((current - previous) / previous) * 100;
    }


    public static double randomDouble(double min, double max) {
        return min + Math.random() * (max - min);
    }
    
    /**
     * 判断是否为偶数
     */
    public static boolean isEven(int number) {
        return number % 2 == 0;
    }
    
    public static boolean isOdd(int number) {
        return !isEven(number);
    }
    
    /**
     * 判断是否为质数
     */
    public static boolean isPrime(int number) {
        if (number <= 1) {
            return false;
        }
        if (number <= 3) {
            return true;
        }
        if (number % 2 == 0 || number % 3 == 0) {
            return false;
        }
        for (int i = 5; i * i <= number; i += 6) {
            if (number % i == 0 || number % (i + 2) == 0) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * 计算阶乘
     */
    public static long factorial(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("阶乘数不能为负数");
        }
        long result = 1;
        for (int i = 2; i <= n; i++) {
            result *= i;
        }
        return result;
    }
    
    /**
     * 数字转中文
     */
    public static String toChinese(int number) {
        String[] units = {"", "十", "百", "千", "万", "十", "百", "千", "亿", "十", "百", "千"};
        String[] digits = {"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};
        
        if (number == 0) {
            return "零";
        }
        
        String str = String.valueOf(number);
        char[] chars = str.toCharArray();
        StringBuilder result = new StringBuilder();
        
        for (int i = 0; i < chars.length; i++) {
            int digit = chars[i] - '0';
            int unitIndex = chars.length - i - 1;
            
            if (digit == 0) {
                // 处理连续的零
                if (i > 0 && chars[i - 1] != '0') {
                    result.append("零");
                }
            } else {
                result.append(digits[digit]).append(units[unitIndex]);
            }
        }
        
        // 处理特殊情况：一十 -> 十
        String chinese = result.toString();
        if (chinese.startsWith("一十")) {
            chinese = chinese.substring(1);
        }
        
        return chinese;
    }
    
    /**
     * 数字转英文
     */
    public static String toEnglish(int number) {
        if (number < 0 || number > 999999999) {
            return String.valueOf(number);
        }
        
        if (number == 0) {
            return "zero";
        }
        
        return convertToEnglish(number).trim();
    }
    
    private static String convertToEnglish(int number) {
        if (number < 20) {
            return ONES[number];
        } else if (number < 100) {
            return TENS[number / 10] + (number % 10 != 0 ? "-" + ONES[number % 10] : "");
        } else if (number < 1000) {
            return ONES[number / 100] + " hundred" + (number % 100 != 0 ? " and " + convertToEnglish(number % 100) : "");
        } else if (number < 1000000) {
            return convertToEnglish(number / 1000) + " thousand" + (number % 1000 != 0 ? " " + convertToEnglish(number % 1000) : "");
        } else {
            return convertToEnglish(number / 1000000) + " million" + (number % 1000000 != 0 ? " " + convertToEnglish(number % 1000000) : "");
        }
    }
    
    private static final String[] ONES = {
        "", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine",
        "ten", "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen",
        "seventeen", "eighteen", "nineteen"
    };
    
    private static final String[] TENS = {
        "", "", "twenty", "thirty", "forty", "fifty", "sixty", "seventy", "eighty", "ninety"
    };
    
    /**
     * 转换为科学计数法
     */
    public static String toScientificNotation(double number, int significantDigits) {
        if (number == 0) {
            return "0.0";
        }
        
        int exponent = (int) Math.floor(Math.log10(Math.abs(number)));
        double coefficient = number / Math.pow(10, exponent);
        
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(significantDigits - 1);
        df.setMinimumFractionDigits(0);
        
        return df.format(coefficient) + "E" + exponent;
    }
    
    /**
     * 判断数字是否在范围内
     */
    public static boolean isInRange(Number number, Number min, Number max) {
        if (number == null || min == null || max == null) {
            return false;
        }
        double value = number.doubleValue();
        return value >= min.doubleValue() && value <= max.doubleValue();
    }
    
    /**
     * 数字数组操作
     */
    public static int[] toIntArray(String str, String separator) {
        if (StringUtil.isBlank(str)) {
            return new int[0];
        }
        String[] parts = str.split(separator);
        int[] result = new int[parts.length];
        for (int i = 0; i < parts.length; i++) {
            result[i] = toInt(parts[i], 0);
        }
        return result;
    }
    
    public static List<Integer> toIntList(String str, String separator) {
        return Ints.asList(toIntArray(str, separator));
    }
    
    public static long[] toLongArray(String str, String separator) {
        if (StringUtil.isBlank(str)) {
            return new long[0];
        }
        String[] parts = str.split(separator);
        long[] result = new long[parts.length];
        for (int i = 0; i < parts.length; i++) {
            result[i] = toLong(parts[i], 0L);
        }
        return result;
    }
    
    public static List<Long> toLongList(String str, String separator) {
        return Longs.asList(toLongArray(str, separator));
    }
    
    /**
     * 计算平均值
     */
    public static double average(int... numbers) {
        if (numbers == null || numbers.length == 0) {
            return 0;
        }
        double sum = 0;
        for (int num : numbers) {
            sum += num;
        }
        return sum / numbers.length;
    }
    
    public static double average(long... numbers) {
        if (numbers == null || numbers.length == 0) {
            return 0;
        }
        double sum = 0;
        for (long num : numbers) {
            sum += num;
        }
        return sum / numbers.length;
    }
    
    /**
     * 计算中位数
     */
    public static double median(int... numbers) {
        if (numbers == null || numbers.length == 0) {
            return 0;
        }
        java.util.Arrays.sort(numbers);
        int middle = numbers.length / 2;
        if (numbers.length % 2 == 1) {
            return numbers[middle];
        } else {
            return (numbers[middle - 1] + numbers[middle]) / 2.0;
        }
    }
    
    /**
     * 数字安全转换（避免空指针）
     */
    public static Optional<Integer> safeToInt(String str) {
        try {
            return Optional.of(Integer.parseInt(str));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
    
    public static Optional<Long> safeToLong(String str) {
        try {
            return Optional.of(Long.parseLong(str));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
    
    public static Optional<Double> safeToDouble(String str) {
        try {
            return Optional.of(Double.parseDouble(str));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}