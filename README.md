# springboot-shardingjdbc-dynamic-datasource

经常做SAAS系统或多租户系统的朋友，肯定遇到过这样几种情况。
- 开始搭建SAAS系统使用一个独立数据库，随着业务发展我们不得不考虑为比较大的租户单独隔离数据库。
- 数据量比较大的表，一开始使用实时表（一般最近7天或30天）与历史表相结合的方案解决大表性能问题，但是后续数据量再次增长，30天数据量可能超过千万级别，这时必须考虑分库分表方案了。

# Springboot集成ShardingJDBC实现动态数据源切换
基本上Java后端开始90%以上都是Springboot开发，本文主要讲解两种动态数据源切换方案。

## 1. 基于AbstractRoutingDataSource利用注解动态切换数据源

```yaml
#single-database
leo:
  datasource:
    group:
      order_db:
        type: com.alibaba.druid.pool.DruidDataSource
        driver-class-name: com.mysql.jdbc.Driver
        url: jdbc:mysql://localhost:3306/order_db
        username: root
        password: 
      baseinfo_db:
        type: com.alibaba.druid.pool.DruidDataSource
        driver-class-name: com.mysql.jdbc.Driver
        url: jdbc:mysql://localhost:3306/baseinfo_db
        username: root
        password: 
    defaultGroup: order_db
    enabled: true
#sharding-jdbc
sharding:
  datasource:
    group:
      trade0:
        type: com.alibaba.druid.pool.DruidDataSource
        driver-class-name: com.mysql.jdbc.Driver
        url: jdbc:mysql://localhost:3306/trade0
        username: root
        password: 
      trade1:
        type: com.alibaba.druid.pool.DruidDataSource
        driver-class-name: com.mysql.jdbc.Driver
        url: jdbc:mysql://localhost:3306/trade1
        username: root
        password: 
      trade2:
        type: com.alibaba.druid.pool.DruidDataSource
        driver-class-name: com.mysql.jdbc.Driver
        url: jdbc:mysql://localhost:3306/trade2
        username: root
        password: 
      trade3:
        type: com.alibaba.druid.pool.DruidDataSource
        driver-class-name: com.mysql.jdbc.Driver
        url: jdbc:mysql://localhost:3306/trade3
        username: root
        password: 
mybatis:
  configuration:
    map-underscore-to-camel-case: true
  mapper-locations: classpath:mapper/*.xml
```

通过`@DS()`注解切换数据源
```java
@Component
public class UserService {

    @Autowired
    private UserMapper userMapper;

    /**
    /* 走baseinfo-db数据源
    **/
    @DS("baseinfo_db")
    public List<UserPO> queryByOrgId(String orgId){
        return userMapper.queryByOrgId(orgId);
    }
}

@Component
public class OrderService {

    @Autowired
    private OrderMapper orderMapper;
    
    /**
    /* 走ShardingJDBC数据源
    **/
    @DS("sharding")
    public List<OrderPO> queryByOrgId(String orgId) {
        return orderMapper.queryByOrgId(orgId);
    }
}
```

## 2. 基于Package包隔离方案
package-dynamic-datasource

`@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})` 取消动态数据源配置

### 订单领域包(com.leo.datasource.pkg.order.domain)


```Java
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

```
### 用户领域包(com.leo.datasource.pkg.user.domain)


```java
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PackageDynamicDsApplication.class)
@Slf4j
public class TestOrderDomain {
    @Autowired
    private OrderService orderService;
  
    @Test
    private void test() {
        for (OrderPO orderPO : orderService.queryByOrgId("120341123")) {
            log.info(orderPO.getGoodsSubject());
        }
    }
}
```
