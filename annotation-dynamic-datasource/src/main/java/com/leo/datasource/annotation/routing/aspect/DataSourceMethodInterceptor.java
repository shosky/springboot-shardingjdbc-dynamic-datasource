package com.leo.datasource.annotation.routing.aspect;

import com.leo.datasource.annotation.routing.annotation.DS;
import com.leo.datasource.annotation.routing.constant.DataSourceConstant;
import com.leo.datasource.annotation.routing.properties.LeoDataSourceProperties;
import com.leo.datasource.annotation.routing.utils.DataSourceContextHolder;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;

/**
 * @author: leo wang
 * @date: 2022-03-15
 * @description:
 **/
public class DataSourceMethodInterceptor implements MethodInterceptor {

    private LeoDataSourceProperties dataSourceProperties;

    public DataSourceMethodInterceptor(LeoDataSourceProperties dataSourceProperties) {
        this.dataSourceProperties = dataSourceProperties;
    }

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        Method method = methodInvocation.getMethod();
        DS dataSource = method.getAnnotation(DS.class);
        String dsVal = dataSource.value();

        if (!dataSourceProperties.getGroup().containsKey(dsVal) && !dsVal.equals(DataSourceConstant.SHARDING_DATASOURCE_NAME)) {
            throw new NullPointerException(String.format("数据源配置【%s】不存在", dsVal));
        }

        try {
            //切换到指定的数据源
            DataSourceContextHolder.setDataSource(dsVal);
            //调用TargetDataSource标记的切换数据源方法
            Object result = methodInvocation.proceed();
            return result;
        } catch (Throwable ex) {
            throw ex;
        } finally {
            DataSourceContextHolder.clearDataSource();
        }

    }
}
