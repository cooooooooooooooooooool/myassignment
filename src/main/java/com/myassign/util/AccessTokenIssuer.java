package com.myassign.util;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.myassign.model.dto.AccessToken;
import com.myassign.model.entity.User;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class AccessTokenIssuer {

    public static final String HEADER_PREFIX = "Bearer ";

    @Value("${secret}")
    private static final String secret = "myscret";

    public AccessToken generateAuthenticateToken(User user) throws JWTCreationException {
        Date issueDate = new Date(System.currentTimeMillis());
        Date expireDate = new Date(System.currentTimeMillis() + AccessToken.expireTime);

        String token = JWT.create().withSubject(user.getId()).withExpiresAt(expireDate).sign(Algorithm.HMAC256(secret));
        log.info("token : " + token);

        return AccessToken.builder().type("BEARER").token(token).issueDate(issueDate).expireDate(expireDate).build();
    }

    public String decryptUserAccessToken(String accessToken) throws JWTVerificationException {
        String userId = JWT.require(Algorithm.HMAC256(secret)).build().verify(accessToken).getSubject();
        log.info("userId : " + userId);
        return userId;
    }
}
