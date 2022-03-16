package com.leo.datasource.pkg.order.domain.mapper;

import com.leo.datasource.pkg.order.domain.model.OrderPO;
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
public interface OrderMapper {

    @Select("select * from t_order where org_id = #{orgId}")
    public List<OrderPO> queryByOrgId(@Param("orgId") String orgId);
}
