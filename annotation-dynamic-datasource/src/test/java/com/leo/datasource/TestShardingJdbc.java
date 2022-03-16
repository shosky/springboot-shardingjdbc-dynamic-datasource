package com.leo.datasource;

import com.leo.datasource.annotation.AnnotationDynamicDataSourceApplication;
import com.leo.datasource.annotation.trade.domain.model.TradePO;
import com.leo.datasource.annotation.trade.domain.service.TradeService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author: leo wang
 * @date: 2022-03-15
 * @description:
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AnnotationDynamicDataSourceApplication.class)
@Slf4j
public class TestShardingJdbc {

    @Autowired
    private TradeService tradeService;


    public void testSave() {
        log.info("result=" + tradeService.save("百事可乐300ML", 1000000119, 100000119, 1119));
        log.info("result=" + tradeService.save("统一老坛酸菜300g", 1000000021, 100000021, 1121));
    }

    @Test
    public void testQuery() {
        for (TradePO tradePO : tradeService.queryByOrgId(1119)) {
            log.info(tradePO.getGoodsSubject() + "----" + tradePO.getOrderId() + "----" + tradePO.getOrgId());
        }
    }
}
