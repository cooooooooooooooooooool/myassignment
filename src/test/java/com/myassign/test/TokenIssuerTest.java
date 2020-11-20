package com.myassign.test;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.myassign.TokenIssuer;
import com.myassign.model.dto.AccessToken;
import com.myassign.model.entity.User;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class TokenIssuerTest {

    @Autowired
    private TokenIssuer tokenResolver;

    @Test
    public void generateTokenTest() throws Exception {

        User user = User.builder().id("mytest").password(DigestUtils.sha256Hex("1234")).build();
        log.info("origin user : " + user);
        AccessToken accessToken = tokenResolver.generateAuthenticateToken(user);
        log.info("accessToken : " + accessToken);

        // 토큰 만료 시간 테스트
        Thread.sleep(5000);
        log.info("dcrypt token user : " + tokenResolver.decryptUserAccessToken(accessToken.getToken()));
    }
}
