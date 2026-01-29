package com.wbw.mybatis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 自定义基础Mapper，扩展MyBatis Plus的BaseMapper
 * 包含批量操作方法
 * 
 * @param <T> 实体类型
 */
public interface BaseMapperX<T> extends BaseMapper<T> {

    /**
     * 批量插入
     * 注意：需要实体类有主键字段，并且主键字段有@TableId注解
     * 
     * @param list 实体列表
     * @return 插入条数
     */
    int insertBatch(@Param("list") List<T> list);

    /**
     * 批量更新（根据ID更新）
     * 注意：需要实体类有主键字段，并且主键字段有@TableId注解
     * 
     * @param list 实体列表
     * @return 更新条数
     */
    int updateBatch(@Param("list") List<T> list);

    /**
     * 插入或更新（根据ID判断）
     * 如果ID存在则更新，不存在则插入（MySQL的ON DUPLICATE KEY UPDATE语法）
     * 
     * @param entity 实体
     * @return 操作条数
     */
    default int saveOrUpdate(T entity) {
        if (entity == null) {
            return 0;
        }
        // 这里使用MyBatis Plus的insertOrUpdate方法
        return this.insertOrUpdate(entity) ? 1 : 0;
    }

    /**
     * MyBatis Plus自带的插入或更新方法
     */
    boolean insertOrUpdate(T entity);
}