package com.leo.datasource.annotation.user.domain.model;

import lombok.Data;

import java.util.Date;

/**
 * @author: leo wang
 * @date: 2022-03-15
 * @description:
 **/
@Data
public class UserPO {

    private Integer id;
    private String name;
    private String orgId;
    private Integer age;
    private Date gmtCreated;
}
