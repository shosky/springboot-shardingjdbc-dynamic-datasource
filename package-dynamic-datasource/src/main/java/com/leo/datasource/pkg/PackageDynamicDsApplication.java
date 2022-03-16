package com.leo.datasource.pkg;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @author: leo wang
 * @date: 2022-03-16
 * @description:
 **/
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@MapperScan("com.leo.datasource.pkg")
public class PackageDynamicDsApplication {
    public static void main(String[] args) {
        SpringApplication.run(PackageDynamicDsApplication.class, args);
    }
}
