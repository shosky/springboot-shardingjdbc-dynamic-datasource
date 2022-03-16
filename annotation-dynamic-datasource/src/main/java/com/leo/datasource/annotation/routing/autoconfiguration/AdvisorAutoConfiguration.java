package com.leo.datasource.annotation.routing.autoconfiguration;

import com.leo.datasource.annotation.routing.aspect.DataSourceMethodInterceptor;
import com.leo.datasource.annotation.routing.properties.LeoDataSourceProperties;
import com.leo.datasource.annotation.routing.properties.ShardingDataSourceProperties;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: leo wang
 * @date: 2022-03-15
 * @description:
 **/
@Configuration
public class AdvisorAutoConfiguration {

    private static final String DEFAULT_POINT_CUT = "@annotation(com.leo.datasource.annotation.routing.annotation.DS)";

    @Bean("dataSourcePointCutAdvice")
    @ConditionalOnClass(value = {DataSourceMethodInterceptor.class})
    public DefaultPointcutAdvisor defaultPointcutAdvisor(LeoDataSourceProperties leoDataSourceProperties, ShardingDataSourceProperties shardingDataSourceProperties) {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        //获取切面表达式
        pointcut.setExpression(DEFAULT_POINT_CUT);
        // 配置增强类advisor
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor();
        advisor.setPointcut(pointcut);
        advisor.setAdvice(new DataSourceMethodInterceptor(leoDataSourceProperties));
        advisor.setOrder(-10);
        return advisor;
    }

}
