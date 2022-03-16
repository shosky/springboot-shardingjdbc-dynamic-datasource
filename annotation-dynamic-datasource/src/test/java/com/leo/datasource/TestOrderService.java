package com.leo.datasource;

import com.leo.datasource.annotation.AnnotationDynamicDataSourceApplication;
import com.leo.datasource.annotation.order.domain.model.OrderPO;
import com.leo.datasource.annotation.order.domain.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Unit test for simple App.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AnnotationDynamicDataSourceApplication.class)
@Slf4j
public class TestOrderService {
    @Autowired
    private OrderService orderService;

    @Test
    public void test() {
        for (OrderPO orderPO : orderService.queryByOrgId("1121")) {
            log.info(orderPO.getGoodsSubject());
        }
    }
}
