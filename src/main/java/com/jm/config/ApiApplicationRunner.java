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
package com.jm.config;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ApiApplicationRunner implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {
        /* @formatter:off */
        /*
        repository.saveAll( Arrays.asList( this.getOauthClientDetails( "cloudrpa-client1", "7aKTMjx9qkHHipSg" ),
                                           this.getOauthClientDetails( "cloudrpa-client2", "FJVFmCEn8NIKbJnQ" ),
                                           this.getOauthClientDetails( "cloudrpa-client3", "uZEaHZUdvPzOGgkT" ) ) );
         */
        /* @formatter:on */
    }
}
