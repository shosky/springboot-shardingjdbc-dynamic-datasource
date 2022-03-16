package com.leo.datasource.annotation.routing.annotation;

import java.lang.annotation.*;

/**
 * @author: leo wang
 * @date: 2022-03-15
 * @description: 切换数据源注解
 **/
@Target(value = ElementType.METHOD)
@Retention(value = RetentionPolicy.RUNTIME)
@Documented
public @interface DS {

    /**
     * 数据源
     * @return
     */
    String value();

}
