package com.wbw.mybatis.config;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.wbw.mybatis.injector.InsertBatch;
import com.wbw.mybatis.injector.UpdateBatch;

import java.util.List;

/**
 * 自定义SQL注入器
 * 用于注入批量操作方法
 */
public class CustomSqlInjector extends DefaultSqlInjector {

    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass, TableInfo tableInfo) {
        // 获取默认的注入方法
        List<AbstractMethod> methodList = super.getMethodList(mapperClass, tableInfo);
        
        // 添加自定义的批量操作方法
        methodList.add(new InsertBatch());
        methodList.add(new UpdateBatch());
        
        return methodList;
    }
}