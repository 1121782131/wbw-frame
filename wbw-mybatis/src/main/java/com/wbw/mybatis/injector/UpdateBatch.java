package com.wbw.mybatis.injector;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.apache.ibatis.executor.keygen.Jdbc3KeyGenerator;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

/**
 * 批量更新方法（优化版）
 */
public class UpdateBatch extends AbstractMethod {
    
    private DbType dbType;
    
    public UpdateBatch() {
        super("updateBatch");
    }
    
    public UpdateBatch(DbType dbType) {
        super("updateBatch");
        this.dbType = dbType;
    }
    
    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        KeyGenerator keyGenerator = NoKeyGenerator.INSTANCE;
        String keyProperty = null;
        String keyColumn = null;
        
        // 处理主键逻辑
        if (StringUtils.isNotBlank(tableInfo.getKeyProperty())) {
            if (tableInfo.getIdType() == IdType.AUTO) {
                // 自增主键
                keyGenerator = Jdbc3KeyGenerator.INSTANCE;
                keyProperty = tableInfo.getKeyProperty();
                keyColumn = tableInfo.getKeyColumn();
            } else {
                if (null != tableInfo.getKeySequence()) {
                    keyGenerator = TableInfoHelper.genKeyGenerator(this.methodName, tableInfo, builderAssistant);
                    keyProperty = tableInfo.getKeyProperty();
                    keyColumn = tableInfo.getKeyColumn();
                }
            }
        }
        
        String sql = "<script>%s</script>";
        String updateSql;
        
        // 根据数据库类型生成不同的SQL
        if (dbType != null && (dbType == DbType.ORACLE || dbType == DbType.ORACLE_12C)) {
            updateSql = createBatchOracleUpdateSql(tableInfo);
        } else {
            updateSql = createBatchUpdateSql(tableInfo);
        }
        
        String finalSql = String.format(sql, updateSql);
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, finalSql, modelClass);
        
        return this.addUpdateMappedStatement(mapperClass, modelClass, this.methodName, sqlSource);
    }
    
    /**
     * 创建批量更新SQL（MySQL等）
     */
    private String createBatchUpdateSql(TableInfo tableInfo) {
        StringBuilder sqlBuilder = new StringBuilder();
        
        sqlBuilder.append("<foreach collection=\"list\" item=\"item\" index=\"index\" separator=\";\">");
        sqlBuilder.append("UPDATE ").append(tableInfo.getTableName()).append(" <set>");
        
        // 生成SET语句
        tableInfo.getFieldList().forEach(field -> {
            sqlBuilder.append("<if test=\"item.").append(field.getProperty()).append(" != null\">")
                      .append(field.getColumn()).append(" = #{item.").append(field.getProperty()).append("},")
                      .append("</if>");
        });
        
        // 删除最后一个逗号（如果存在）
        sqlBuilder.append("</set>");
        sqlBuilder.append(" WHERE ").append(tableInfo.getKeyColumn())
                  .append(" = #{item.").append(tableInfo.getKeyProperty()).append("}");
        sqlBuilder.append("</foreach>");
        
        return sqlBuilder.toString();
    }
    
    /**
     * 创建批量更新SQL（Oracle）
     */
    private String createBatchOracleUpdateSql(TableInfo tableInfo) {
        StringBuilder sqlBuilder = new StringBuilder();
        
        sqlBuilder.append("<foreach collection=\"list\" item=\"item\" index=\"index\" ")
                  .append("separator=\";\" open=\"begin\" close=\";end;\"> ");
        sqlBuilder.append("UPDATE ").append(tableInfo.getTableName()).append(" <set>");
        
        // 生成SET语句
        tableInfo.getFieldList().forEach(field -> {
            sqlBuilder.append("<if test=\"item.").append(field.getProperty()).append(" != null\">")
                      .append(field.getColumn()).append(" = #{item.").append(field.getProperty()).append("},")
                      .append("</if>");
        });
        
        // 删除最后一个逗号（如果存在）
        sqlBuilder.append("</set>");
        sqlBuilder.append(" WHERE ").append(tableInfo.getKeyColumn())
                  .append(" = #{item.").append(tableInfo.getKeyProperty()).append("}");
        sqlBuilder.append("</foreach>");
        
        return sqlBuilder.toString();
    }
}