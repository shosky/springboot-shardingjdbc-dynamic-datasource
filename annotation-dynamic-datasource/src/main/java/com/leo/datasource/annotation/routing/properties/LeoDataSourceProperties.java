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
@ConfigurationProperties(prefix = "leo.datasource")
@Data
public class LeoDataSourceProperties {

    private String defaultGroup = "";

    /**
     * 多数据源配置
     */
    private Map<String, DruidDataSource> group = new HashMap<>();

    /**
     * 获取默认数据源
     * @return
     */
    public DruidDataSource getDefaultDataSource() {
        return this.group.get(this.getDefaultGroup());
    }
}
