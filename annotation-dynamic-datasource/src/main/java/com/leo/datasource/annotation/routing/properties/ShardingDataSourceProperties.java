package com.leo.datasource.annotation.routing.properties;

import com.alibaba.druid.pool.DruidDataSource;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: leo wang
 * @date: 2022-03-15
 * @description:
 **/
@ConfigurationProperties(prefix = "sharding.datasource")
@Data
public class ShardingDataSourceProperties {

    /**
     * 多数据源配置
     */
    private Map<String, DruidDataSource> group = new HashMap<>();

}
