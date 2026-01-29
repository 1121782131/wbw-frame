package com.wbw.mybatis.util;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.experimental.UtilityClass;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * MyBatis Plus 工具类
 */
@UtilityClass
public class MyBatisUtil {

    /**
     * 将Page对象转换为另一个类型的Page对象
     * @param sourcePage 源Page
     * @param converter 转换函数
     * @param <T> 源类型
     * @param <R> 目标类型
     * @return 转换后的Page
     */
    public static <T, R> IPage<R> convertPage(IPage<T> sourcePage, Function<T, R> converter) {
        if (sourcePage == null || converter == null) {
            return new Page<>();
        }
        
        IPage<R> targetPage = new Page<>(
            sourcePage.getCurrent(),
            sourcePage.getSize(),
            sourcePage.getTotal(),
            sourcePage.searchCount()
        );
        
        targetPage.setRecords(
            sourcePage.getRecords()
                .stream()
                .map(converter)
                .collect(Collectors.toList())
        );
        
        return targetPage;
    }

    /**
     * 构建分页参数
     * @param current 当前页
     * @param size 每页大小
     * @return Page对象
     */
    public static <T> Page<T> buildPage(long current, long size) {
        return new Page<>(current, size);
    }

    /**
     * 构建不分页的参数（获取所有数据）
     * @return Page对象
     */
    public static <T> Page<T> buildNoPage() {
        return new Page<>(1, Integer.MAX_VALUE);
    }

    /**
     * 检查是否启用了分页
     * @param page Page对象
     * @return 是否启用分页
     */
    public static boolean isPageEnabled(IPage<?> page) {
        return page != null && page.getSize() < Integer.MAX_VALUE;
    }

    /**
     * 将集合分割成多个批次
     * @param list 原始集合
     * @param batchSize 批次大小
     * @param <T> 元素类型
     * @return 批次列表
     */
    public static <T> List<List<T>> splitList(List<T> list, int batchSize) {
        if (list == null || list.isEmpty()) {
            return List.of();
        }
        
        int totalSize = list.size();
        int batchCount = (totalSize + batchSize - 1) / batchSize;
        
        List<List<T>> batches = new java.util.ArrayList<>(batchCount);
        for (int i = 0; i < batchCount; i++) {
            int fromIndex = i * batchSize;
            int toIndex = Math.min(totalSize, (i + 1) * batchSize);
            batches.add(new java.util.ArrayList<>(list.subList(fromIndex, toIndex)));
        }
        
        return batches;
    }
    
    /**
     * 判断集合是否为空
     */
    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }
    
    /**
     * 判断集合是否不为空
     */
    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }
}