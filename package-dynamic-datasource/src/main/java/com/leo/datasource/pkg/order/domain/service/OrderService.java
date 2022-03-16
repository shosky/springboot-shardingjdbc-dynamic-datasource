package com.leo.datasource.pkg.order.domain.service;

import com.leo.datasource.pkg.order.domain.mapper.OrderMapper;
import com.leo.datasource.pkg.order.domain.model.OrderPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author: leo wang
 * @date: 2022-03-15
 * @description:
 **/
@Component
public class OrderService {

    @Autowired
    private OrderMapper orderMapper;

    public List<OrderPO> queryByOrgId(String orgId) {
        return orderMapper.queryByOrgId(orgId);
    }
}
