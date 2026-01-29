// com/wbw/common/utils/json/JsonUtil.java
package com.wbw.common.utils.json;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONWriter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

/**
 * JSON工具类
 */
@Slf4j
public class JsonUtil {
    
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    private JsonUtil() {
        throw new IllegalStateException("Utility class");
    }
    
    /**
     * 对象转JSON字符串
     */
    public static String toJson(Object object) {
        if (object == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("对象转JSON失败: {}", e.getMessage());
            return null;
        }
    }
    
    /**
     * JSON字符串转对象
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        if (json == null || json.isEmpty()) {
            return null;
        }
        try {
            return objectMapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            log.error("JSON转对象失败: {}", e.getMessage());
            return null;
        }
    }
    
    /**
     * 使用Fastjson2将对象转JSON字符串
     */
    public static String toJsonFast(Object object) {
        if (object == null) {
            return null;
        }
        return JSON.toJSONString(object);
    }
    
    /**
     * 使用Fastjson2将JSON字符串转对象
     */
    public static <T> T fromJsonFast(String json, Class<T> clazz) {
        if (json == null || json.isEmpty()) {
            return null;
        }
        return JSON.parseObject(json, clazz);
    }
    
    /**
     * 将对象转换为JSON字符串（带格式化）
     */
    public static String toPrettyJson(Object object) {
        if (object == null) {
            return null;
        }
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("对象转格式化JSON失败: {}", e.getMessage());
            return null;
        }
    }
    
    /**
     * 将JSON字符串转换为List
     */
    public static <T> List<T> parseList(String json, Class<T> clazz) {
        if (json == null || json.isEmpty()) {
            return null;
        }
        try {
            return JSON.parseArray(json, clazz);
        } catch (Exception e) {
            log.error("JSON转List失败: {}", e.getMessage());
            return null;
        }
    }
    
    /**
     * 将JSON字符串转换为Map
     */
    public static <K, V> Map<K, V> parseMap(String json, Class<K> keyClass, Class<V> valueClass) {
        if (json == null || json.isEmpty()) {
            return null;
        }
        try {
            return JSON.parseObject(json, Map.class);
        } catch (Exception e) {
            log.error("JSON转Map失败: {}", e.getMessage());
            return null;
        }
    }
    
    /**
     * 验证是否为有效的JSON字符串
     */
    public static boolean isValidJson(String json) {
        if (json == null || json.isEmpty()) {
            return false;
        }
        try {
            JSON.parse(json);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * 对象深度复制（通过JSON序列化）
     */
    public static <T> T deepClone(T object, Class<T> clazz) {
        if (object == null) {
            return null;
        }
        String json = toJson(object);
        return fromJson(json, clazz);
    }
    
    /**
     * 对象转JSON字节数组
     */
    public static byte[] toJsonBytes(Object object) {
        if (object == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsBytes(object);
        } catch (JsonProcessingException e) {
            log.error("对象转JSON字节数组失败: {}", e.getMessage());
            return null;
        }
    }
    
    /**
     * JSON字节数组转对象
     */
    public static <T> T fromJsonBytes(byte[] bytes, Class<T> clazz) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        try {
            return objectMapper.readValue(bytes, clazz);
        } catch (Exception e) {
            log.error("JSON字节数组转对象失败: {}", e.getMessage());
            return null;
        }
    }
}