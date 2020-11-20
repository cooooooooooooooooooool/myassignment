/*===================================================================================
 *                    Copyright(c) 2019 POSCO ICT
 *
 * Project            : workcenter-api
 * Source File Name   : com.aworks.workcenter.api.auth.OauthClientDetailsLoader.java
 * Description        :
 * Author             : ddurung
 * Version            : 1.0.0
 * File Name related  :
 * Class Name related :
 * Created Date       : 2019. 10. 31.
 * Updated Date       : 2019. 10. 31.
 * Last modifier      : ddurung
 * Updated content    : 최초작성
 *
 *==================================================================================*/
package com.myassign.config;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.myassign.model.entity.User;
import com.myassign.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApiApplicationRunner implements ApplicationRunner {

    private final UserService userService;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        /* @formatter:off */
        List<User> list = new ArrayList<>();
        
        for (int i=0; i<5; i++) {
            User user = User.builder()
                            .id("user-"+i)
                            .name("사용자"+i)
                            .balance(Long.valueOf(Integer.MAX_VALUE))
                            .createDate(new Date()).build();
            log.info("user : " + user);
            list.add(user);
        }
        
        userService.createUser(list);
        /* @formatter:on */
    }
}
