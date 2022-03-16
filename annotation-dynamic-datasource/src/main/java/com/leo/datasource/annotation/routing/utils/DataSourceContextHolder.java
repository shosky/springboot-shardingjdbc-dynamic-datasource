package com.leo.datasource.annotation.routing.utils;

/**
 * @author: leo wang
 * @date: 2022-03-15
 * @description: 数据源切换工具类
 **/
public class DataSourceContextHolder {

    /**
     * 当前线程对应数据源
     */
    private static final ThreadLocal<String> CONTEXT = new InheritableThreadLocal<>();

    /**
     * 设置当前线程数据源
     * @param dataSource
     */
    public static void setDataSource(String dataSource) {
        CONTEXT.set(dataSource);
    }

    /**
     * 获取当前线程持有的数据源
     *
     * @return
     */
    public static String getDataSource() {
        return CONTEXT.get();
    }

    /**
     * 清除当前线程持有数据源
     */
    public static void clearDataSource() {
        CONTEXT.remove();
    }
}
