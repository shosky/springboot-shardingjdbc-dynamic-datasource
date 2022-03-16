package com.leo.datasource.pkg.user.domain;

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
@MapperScan(basePackages = com.leo.datasource.pkg.user.domain.UserDataSourceConfig.PACKAGE, sqlSessionFactoryRef = "userJdbcSqlSessionFactory")
public class UserDataSourceConfig {

    // 精确到 user.domain 目录，以便跟其他数据源隔离
    static final String PACKAGE = "com.leo.datasource.pkg.user.domain";
    static final String MAPPER_LOCATION = "classpath:mapper/mybatis/*.xml";

    @Value("${user.jdbc.url}")
    private String url;

    @Value("${user.jdbc.username}")
    private String user;

    @Value("${user.jdbc.password}")
    private String password;

    @Value("${user.jdbc.driver-class-name}")
    private String driverClass;

    @Bean(name = "userJdbcDataSource")
    @Primary
    public DataSource userJdbcDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(driverClass);
        dataSource.setUrl(url);
        dataSource.setUsername(user);
        dataSource.setPassword(password);
        return dataSource;
    }

    @Bean(name = "userJdbcTransactionManager")
    @Primary
    public DataSourceTransactionManager userJdbcTransactionManager() {
        return new DataSourceTransactionManager(userJdbcDataSource());
    }

    @Bean(name = "userJdbcSqlSessionFactory")
    @Primary
    public SqlSessionFactory userJdbcSqlSessionFactory(@Qualifier("userJdbcDataSource") DataSource masterDataSource)
            throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(masterDataSource);
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources(com.leo.datasource.pkg.user.domain.UserDataSourceConfig.MAPPER_LOCATION));
        return sessionFactory.getObject();
    }
}
