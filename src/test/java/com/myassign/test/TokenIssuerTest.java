package com.myassign.test;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.myassign.model.dto.AccessToken;
import com.myassign.model.entity.User;
import com.myassign.util.AccessTokenIssuer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class TokenIssuerTest {

    @Test
    public void generateTokenTest() throws Exception {

        User user = User.builder().id("user-0").password(DigestUtils.sha256Hex("1234")).build();
        log.info("origin user : " + user);
        AccessToken accessToken = AccessTokenIssuer.generateAuthenticateToken(user);
        log.info("accessToken : " + accessToken);

        // 토큰 만료 시간 테스트
        Thread.sleep(5000);
        log.info("dcrypt token user : " + AccessTokenIssuer.decryptUserAccessToken(accessToken.getToken()));
    }
}
