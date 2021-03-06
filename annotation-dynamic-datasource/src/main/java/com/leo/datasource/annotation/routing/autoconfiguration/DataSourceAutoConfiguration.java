package com.leo.datasource.annotation.routing.autoconfiguration;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.leo.datasource.annotation.routing.DynamicDataSource;
import com.leo.datasource.annotation.routing.constant.DataSourceConstant;
import com.leo.datasource.annotation.routing.properties.LeoDataSourceProperties;
import com.leo.datasource.annotation.routing.properties.ShardingDataSourceProperties;
import org.apache.shardingsphere.api.config.sharding.ShardingRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.TableRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.strategy.InlineShardingStrategyConfiguration;
import org.apache.shardingsphere.shardingjdbc.api.ShardingDataSourceFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: leo wang
 * @date: 2022-03-15
 * @description:
 **/
@Configuration
@AutoConfigureBefore(DruidDataSourceAutoConfigure.class)
@EnableConfigurationProperties({LeoDataSourceProperties.class, ShardingDataSourceProperties.class})
@ConditionalOnProperty(prefix = "leo.datasource", name = "enabled", havingValue = "true", matchIfMissing = true)
@Slf4j
public class DataSourceAutoConfiguration implements InitializingBean, DisposableBean {

    @Bean("dynamicDataSources")
    public DataSource loadDataSources(LeoDataSourceProperties leoDataSourceProperties, ShardingDataSourceProperties shardingDataSourceProperties) {
        Map<String, DruidDataSource> groups = leoDataSourceProperties.getGroup();
        if (groups.isEmpty()) {
            throw new RuntimeException("????????????????????????");
        }
        if (!groups.containsKey(leoDataSourceProperties.getDefaultGroup())) {
            throw new RuntimeException("???????????????????????????");
        }
        Map<Object, Object> targetDataSources = new HashMap<>(groups.size());
        //??????Properties????????????Map
        groups.keySet().forEach(key -> targetDataSources.put(key, groups.get(key)));
        //??????sharding????????????Map
        buildShardingDataSource(shardingDataSourceProperties, targetDataSources);
        //?????????????????????
        return DynamicDataSource.newInstance(leoDataSourceProperties.getDefaultDataSource(), targetDataSources);
    }

    public void buildShardingDataSource(ShardingDataSourceProperties dataSourceProperties, Map<Object, Object> targetDataSources) {
        if (dataSourceProperties == null) {
            return;
        }
        Map<String, DruidDataSource> groups = dataSourceProperties.getGroup();
        // ????????????
        TableRuleConfiguration tradeTableRuleConfig = new TableRuleConfiguration
                ("t_trade", "trade${0..3}.t_trade_${0..3}");
        tradeTableRuleConfig.setTableShardingStrategyConfig(
                new InlineShardingStrategyConfiguration("org_id", "t_trade_${org_id % 4}"));

        // ????????????
        tradeTableRuleConfig.setDatabaseShardingStrategyConfig(new InlineShardingStrategyConfiguration
                ("org_id", "trade${org_id % 4}"));

        // ??????????????????
        ShardingRuleConfiguration shardingRuleConfig = new ShardingRuleConfiguration();
        shardingRuleConfig.getTableRuleConfigs().add(tradeTableRuleConfig);

        Map<String, DataSource> shardingDataSourceMap = new HashMap<>();
        groups.keySet().stream().filter(key -> key.startsWith("trade")).forEach(key -> shardingDataSourceMap.put(key, groups.get(key)));

        DataSource dataSource = null;
        try {
            Properties properties = new Properties();
            properties.setProperty("sql.show",Boolean.TRUE.toString());
            dataSource = ShardingDataSourceFactory.createDataSource
                    (shardingDataSourceMap, shardingRuleConfig, properties);
        } catch (SQLException e) {
            throw new RuntimeException("??????Sharding???????????????", e);
        }

        targetDataSources.put(DataSourceConstant.SHARDING_DATASOURCE_NAME, dataSource);
    }


    @Override
    public void destroy() throws Exception {
        log.info("?????????--??????????????????----??????????????????????????????DataSourceAutoConfiguration???");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("????????????--??????????????????----??????????????????????????????DataSourceAutoConfiguration???");
    }
}
