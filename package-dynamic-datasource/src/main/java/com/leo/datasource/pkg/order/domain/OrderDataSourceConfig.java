package com.leo.datasource.pkg.order.domain;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * @author: leo wang
 * @date: 2022-03-14
 * @description:
 **/
@Configuration
@MapperScan(basePackages = OrderDataSourceConfig.PACKAGE, sqlSessionFactoryRef = "orderJdbcSqlSessionFactory")
public class OrderDataSourceConfig {

    // 精确到 order.domain 目录，以便跟其他数据源隔离
    static final String PACKAGE = "com.leo.datasource.pkg.order.domain";
    static final String MAPPER_LOCATION = "classpath:mapper/jdbc/*.xml";

    @Value("${order.jdbc.url}")
    private String url;

    @Value("${order.jdbc.username}")
    private String user;

    @Value("${order.jdbc.password}")
    private String password;

    @Value("${order.jdbc.driver-class-name}")
    private String driverClass;

    /**
     * 生产数据源
     * @return
     */
    @Bean(name = "orderJdbcDataSource")
    @Primary
    public DataSource masterDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(driverClass);
        dataSource.setUrl(url);
        dataSource.setUsername(user);
        dataSource.setPassword(password);
        return dataSource;
    }

    /**
     * 将数据源加入Spring事务管理器
     * @return
     */
    @Bean(name = "orderTransactionManager")
    @Primary
    public DataSourceTransactionManager orderTransactionManager() {
        return new DataSourceTransactionManager(masterDataSource());
    }

    /**
     * 给MyBaits的SqlSessionFactoryy注入自定义的DataSource
     * @param masterDataSource
     * @return
     * @throws Exception
     */
    @Bean(name = "orderJdbcSqlSessionFactory")
    @Primary
    public SqlSessionFactory jdbcSqlSessionFactory(@Qualifier("orderJdbcDataSource") DataSource masterDataSource)
            throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(masterDataSource);
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources(OrderDataSourceConfig.MAPPER_LOCATION));
        return sessionFactory.getObject();
    }
}
