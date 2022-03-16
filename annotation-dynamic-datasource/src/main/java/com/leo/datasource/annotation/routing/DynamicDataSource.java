package com.leo.datasource.annotation.routing;

import com.leo.datasource.annotation.routing.utils.DataSourceContextHolder;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.Map;

/**
 * @author: leo wang
 * @date: 2022-03-15
 * @description: 动态数据源
 **/
public class DynamicDataSource extends AbstractRoutingDataSource {

    /**
     * 私有构造函数，创建DynamicDataSource
     *
     * @param defaultDataSource
     * @param targetDataSources
     */
    private DynamicDataSource(DataSource defaultDataSource, Map<Object, Object> targetDataSources) {
        //如果setTargetDataSources指定的数据源不存在，将会使用默认的数据源
        this.setDefaultTargetDataSource(defaultDataSource);
        //集合的Key可以为任何数据类型，当前类会通过泛型的方式来实现查找
        this.setTargetDataSources(targetDataSources);
        //指定对默认的数据源是否应用宽松的回退，如果找不到当前查找键（Look Up Key）的特定数据源，就返回默认的数据源，默认为true
        this.setLenientFallback(true);
        //设置DataSourceLookup为解析数据源的字符串，默认是使用JndiDataSourceLookup；允许直接指定应用程序服务器数据源的JNDI名称
        this.setDataSourceLookup(null);
        //将设置的默认数据源、目标数据源解析为真实的数据源对象赋值给resolvedDefaultDataSource变量和resolvedDataSources变量
        this.afterPropertiesSet();
    }

    /**
     * 确定当前线程的查找键，这通常用于检查线程绑定事物的上下文，允许是任意的键（Look Up Key），
     * 返回的查找键（Look Up Key）需要与存储的查找键（Look Up key）类型匹配
     */
    @Override
    protected Object determineCurrentLookupKey() {
        return DataSourceContextHolder.getDataSource();
    }

    /**
     * 构建动态数据源
     *
     * @param defaultDataSource
     * @param targetDataSources
     * @return
     */
    public static DynamicDataSource newInstance(DataSource defaultDataSource, Map<Object, Object> targetDataSources) {
        return new DynamicDataSource(defaultDataSource, targetDataSources);
    }
}
