package com.jm;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.jm.entity.User;
import com.jm.vo.AccessToken;

@Component
@PropertySource("classpath:global.properties")
public class TokenIssuer {
	
	private static final Logger logger = LoggerFactory.getLogger(TokenIssuer.class);
	
	public static final String HEADER_PREFIX = "Bearer ";
	
	@Value("${secret}")
	private String secret;
	
	public AccessToken generateAuthenticateToken(User user) throws JWTCreationException {
		Date issueDate = new Date(System.currentTimeMillis());
		Date expireDate = new Date(System.currentTimeMillis() + AccessToken.expireTime);
		
	    String token = JWT.create()
                .withSubject(user.getId())
                .withExpiresAt(expireDate)
                .sign(Algorithm.HMAC256(secret));
	    logger.info("token : " + token);
	    
	    return AccessToken.builder().type("BEARER").token(token).issueDate(issueDate).expireDate(expireDate).build();
	}
	
	public String decryptUserAccessToken(String accessToken) throws JWTVerificationException {
		String userId = JWT.require(Algorithm.HMAC256(secret))
                .build()
                .verify(accessToken)
                .getSubject();
		logger.info("userId : " + userId);
		return userId;
	}
}
