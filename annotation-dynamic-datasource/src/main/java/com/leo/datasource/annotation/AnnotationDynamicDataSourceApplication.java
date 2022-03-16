package com.leo.datasource.annotation;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @author: leo wang
 * @date: 2022-03-15
 * @description:
 **/
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@MapperScan("com.leo.datasource")
public class AnnotationDynamicDataSourceApplication {
}
