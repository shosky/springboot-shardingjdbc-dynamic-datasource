package com.leo.datasource.pkg.user.domain.service;

import com.leo.datasource.pkg.user.domain.mapper.UserMapper;
import com.leo.datasource.pkg.user.domain.model.UserPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author: leo wang
 * @date: 2022-03-15
 * @description:
 **/
@Component
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public List<UserPO> queryByOrgId(String orgId) {
        return userMapper.queryByOrgId(orgId);
    }
}
