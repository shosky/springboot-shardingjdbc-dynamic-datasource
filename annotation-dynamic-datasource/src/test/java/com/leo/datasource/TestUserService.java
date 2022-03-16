package com.leo.datasource;

import com.leo.datasource.annotation.AnnotationDynamicDataSourceApplication;
import com.leo.datasource.annotation.user.domain.model.UserPO;
import com.leo.datasource.annotation.user.domain.service.UserService;
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
public class TestUserService {

    @Autowired
    private UserService userService;

    @Test
    public void testUserService() {
        for (UserPO userPO : userService.queryByOrgId("1121")) {
            log.info(userPO.getName());
        }
    }
}
