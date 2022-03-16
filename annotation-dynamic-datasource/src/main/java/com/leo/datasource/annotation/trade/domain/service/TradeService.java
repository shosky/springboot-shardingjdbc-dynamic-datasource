package com.leo.datasource.annotation.trade.domain.service;

import com.leo.datasource.annotation.routing.annotation.DS;
import com.leo.datasource.annotation.trade.domain.mapper.TradeMapper;
import com.leo.datasource.annotation.trade.domain.model.TradePO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @author: leo wang
 * @date: 2022-03-15
 * @description:
 **/
@Component
public class TradeService {

    @Autowired
    private TradeMapper tradeMapper;

    @DS("sharding")
    public int save(String goodsSubject, Integer orderId,Integer tradeId,Integer orgId) {
        TradePO tradePO = new TradePO();
        tradePO.setGoodsSubject(goodsSubject);
        tradePO.setGmtCreated(new Date());
        tradePO.setOrderId(orderId);
        tradePO.setTradeId(tradeId);
        tradePO.setOrgId(orgId);
        return tradeMapper.save(tradePO);
    }

    @DS("sharding")
    public List<TradePO> queryByOrgId(Integer orgId){
        return tradeMapper.queryByOrgId(orgId);
    }
}
