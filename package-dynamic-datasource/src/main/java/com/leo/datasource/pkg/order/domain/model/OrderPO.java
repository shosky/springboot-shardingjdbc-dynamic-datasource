package com.leo.datasource.pkg.order.domain.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author: leo wang
 * @date: 2022-03-15
 * @description:
 **/
@Data
public class OrderPO implements Serializable {

    private Integer id;
    private Integer payAmount;
    private String orderId;
    private Integer payLineNumber;
    private String goodsSubject;
    private Date gmtCreated;
    private String orgId;
}
