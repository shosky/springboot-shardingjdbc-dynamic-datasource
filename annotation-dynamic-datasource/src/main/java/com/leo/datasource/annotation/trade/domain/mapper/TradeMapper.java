package com.leo.datasource.annotation.trade.domain.mapper;

import com.leo.datasource.annotation.trade.domain.model.TradePO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author: leo wang
 * @date: 2022-03-15
 * @description:
 **/
@Mapper
public interface TradeMapper {

    int save(TradePO tradePO);

    @Select("select * from t_trade where org_id = #{orgId}")
    List<TradePO> queryByOrgId(@Param("orgId") Integer orgId);
}
