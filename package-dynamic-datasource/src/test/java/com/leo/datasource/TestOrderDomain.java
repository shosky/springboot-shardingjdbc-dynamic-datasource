package com.leo.datasource;

import com.leo.datasource.pkg.PackageDynamicDsApplication;
import com.leo.datasource.pkg.order.domain.model.OrderPO;
import com.leo.datasource.pkg.order.domain.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author: leo wang
 * @date: 2022-03-16
 * @description:
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PackageDynamicDsApplication.class)
@Slf4j
public class TestOrderDomain {
    @Autowired
    private OrderService orderService;

    @Test
    private void test() {
        for (OrderPO orderPO : orderService.queryByOrgId("120341123")) {
            log.info(orderPO.getGoodsSubject());
        }
    }
}
