package com.leo.datasource.annotation.trade.domain.model;

import lombok.Data;

import java.util.Date;

/**
 * @author: leo wang
 * @date: 2022-03-15
 * @description:
 **/
@Data
public class TradePO {

    private Integer id;
    private Integer tradeId;
    private Integer orderId;
    private String goodsSubject;
    private Date gmtCreated;
    private Integer orgId;
}
