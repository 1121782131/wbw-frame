// com/wbw/common/result/PageResult.java
package com.wbw.common.result;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.wbw.common.constant.BusinessConstant;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 分页返回结果
 * @param <T> 数据类型
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PageResult<T> extends Result<List<T>> implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 分页信息
     */
    private PageInfo page;
    
    /**
     * 构造函数
     */
    public PageResult(List<T> data, PageInfo page) {
        super(BusinessConstant.StatusCode.SUCCESS, "查询成功", data, true);
        this.page = page;
    }
    
    public static <T> PageResult<T> success(List<T> data, long total, long pageNum, long pageSize) {
        PageInfo pageInfo = PageInfo.of(total, pageNum, pageSize);
        return new PageResult<>(data, pageInfo);
    }
    
    /**
     * 分页信息类
     */
    @Data
    @NoArgsConstructor
    public static class PageInfo implements Serializable {
        
        private static final long serialVersionUID = 1L;
        
        /**
         * 总记录数
         */
        private Long total;
        
        /**
         * 当前页码
         */
        private Long pageNum;
        
        /**
         * 每页大小
         */
        private Long pageSize;
        
        /**
         * 总页数
         */
        private Long pages;
        
        /**
         * 是否有下一页
         */
        private Boolean hasNext;
        
        /**
         * 是否有上一页
         */
        private Boolean hasPrevious;
        
        /**
         * 创建分页信息
         */
        public static PageInfo of(long total, long pageNum, long pageSize) {
            PageInfo pageInfo = new PageInfo();
            pageInfo.setTotal(total);
            pageInfo.setPageNum(pageNum);
            pageInfo.setPageSize(pageSize);
            
            long pages = pageSize == 0 ? 0 : (total + pageSize - 1) / pageSize;
            pageInfo.setPages(pages);
            pageInfo.setHasNext(pageNum < pages);
            pageInfo.setHasPrevious(pageNum > 1);
            
            return pageInfo;
        }
        
        /**
         * 计算偏移量
         */
        public long getOffset() {
            return (pageNum - 1) * pageSize;
        }
    }
}