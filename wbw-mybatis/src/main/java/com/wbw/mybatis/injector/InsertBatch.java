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
 * 批量插入方法
 */
public class InsertBatch extends AbstractMethod {
    
    private DbType dbType;
    
    public InsertBatch() {
        super("insertBatch");
    }
    
    public InsertBatch(DbType dbType) {
        super("insertBatch");
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
        String insertSql;
        
        // 根据数据库类型生成不同的SQL
        if (dbType != null && (dbType == DbType.ORACLE || dbType == DbType.ORACLE_12C)) {
            insertSql = createBatchOracleInsertSql(tableInfo);
        } else {
            insertSql = createBatchInsertSql(tableInfo);
        }
        
        String finalSql = String.format(sql, insertSql);
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, finalSql, modelClass);
        
        return this.addInsertMappedStatement(mapperClass, modelClass, this.methodName, 
                sqlSource, keyGenerator, keyProperty, keyColumn);
    }
    
    /**
     * 创建批量插入SQL（MySQL等）
     */
    private String createBatchInsertSql(TableInfo tableInfo) {
        StringBuilder sqlBuilder = new StringBuilder();
        
        // 准备字段SQL
        sqlBuilder.append("INSERT INTO ").append(tableInfo.getTableName()).append(" ");
        sqlBuilder.append(prepareFieldSql(tableInfo));
        sqlBuilder.append(" VALUES ");
        sqlBuilder.append(prepareValuesSqlForBatch(tableInfo));
        
        return sqlBuilder.toString();
    }
    
    /**
     * 创建批量插入SQL（Oracle）
     */
    private String createBatchOracleInsertSql(TableInfo tableInfo) {
        StringBuilder sqlBuilder = new StringBuilder();
        
        sqlBuilder.append("<foreach collection=\"list\" item=\"item\" index=\"index\" ")
                  .append("separator=\";\" open=\"begin\" close=\";end;\"> ");
        sqlBuilder.append("INSERT INTO ").append(tableInfo.getTableName()).append(" ");
        sqlBuilder.append(prepareFieldSql(tableInfo));
        sqlBuilder.append(" VALUES ");
        sqlBuilder.append(prepareValuesSqlForOracleBatch(tableInfo));
        sqlBuilder.append("</foreach>");
        
        return sqlBuilder.toString();
    }
    
    /**
     * 准备字段SQL
     */
    private String prepareFieldSql(TableInfo tableInfo) {
        StringBuilder fieldSql = new StringBuilder();
        fieldSql.append("(");
        
        // 主键字段
        if (StringUtils.isNotBlank(tableInfo.getKeyColumn())) {
            fieldSql.append(tableInfo.getKeyColumn()).append(",");
        }
        
        // 其他字段
        tableInfo.getFieldList().forEach(field -> {
            fieldSql.append(field.getColumn()).append(",");
        });
        
        // 删除最后一个逗号
        if (fieldSql.charAt(fieldSql.length() - 1) == ',') {
            fieldSql.deleteCharAt(fieldSql.length() - 1);
        }
        
        fieldSql.append(")");
        return fieldSql.toString();
    }
    
    /**
     * 准备批量插入的值SQL（MySQL等）
     */
    private String prepareValuesSqlForBatch(TableInfo tableInfo) {
        StringBuilder valueSql = new StringBuilder();
        
        valueSql.append("<foreach collection=\"list\" item=\"item\" index=\"index\" ")
                .append("separator=\",\" open=\"(\" close=\")\">");
        
        // 主键值
        if (StringUtils.isNotBlank(tableInfo.getKeyProperty())) {
            valueSql.append("#{item.").append(tableInfo.getKeyProperty()).append("},");
        }
        
        // 其他字段值
        tableInfo.getFieldList().forEach(field -> {
            valueSql.append("#{item.").append(field.getProperty()).append("},");
        });
        
        // 删除最后一个逗号
        if (valueSql.charAt(valueSql.length() - 1) == ',') {
            valueSql.deleteCharAt(valueSql.length() - 1);
        }
        
        valueSql.append("</foreach>");
        return valueSql.toString();
    }
    
    /**
     * 准备批量插入的值SQL（Oracle）
     */
    private String prepareValuesSqlForOracleBatch(TableInfo tableInfo) {
        StringBuilder valueSql = new StringBuilder();
        valueSql.append("(");
        
        // 主键值
        if (StringUtils.isNotBlank(tableInfo.getKeyProperty())) {
            valueSql.append("#{item.").append(tableInfo.getKeyProperty()).append("},");
        }
        
        // 其他字段值
        tableInfo.getFieldList().forEach(field -> {
            valueSql.append("#{item.").append(field.getProperty()).append("},");
        });
        
        // 删除最后一个逗号
        if (valueSql.charAt(valueSql.length() - 1) == ',') {
            valueSql.deleteCharAt(valueSql.length() - 1);
        }
        
        valueSql.append(")");
        return valueSql.toString();
    }
}