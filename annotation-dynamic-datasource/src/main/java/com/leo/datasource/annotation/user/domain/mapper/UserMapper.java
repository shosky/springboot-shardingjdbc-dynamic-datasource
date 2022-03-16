package com.leo.datasource.annotation.user.domain.mapper;

import com.leo.datasource.annotation.user.domain.model.UserPO;
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
public interface UserMapper {

    @Select("select * from t_user where org_id = #{orgId}")
    public List<UserPO> queryByOrgId(@Param("orgId") String orgId);
}
