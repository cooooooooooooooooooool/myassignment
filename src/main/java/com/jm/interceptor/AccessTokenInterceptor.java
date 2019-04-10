package com.jm.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.jm.TokenIssuer;
import com.jm.exception.AccessTokenEmptyException;
import com.jm.exception.AccessTokenInvalidException;

@Component
public class AccessTokenInterceptor extends HandlerInterceptorAdapter {
	
	private static final Logger logger = LoggerFactory.getLogger(AccessTokenInterceptor.class);
	
	@Autowired
	private TokenIssuer tokenIssuer;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		
		String requestHeader = StringUtils.defaultString(request.getHeader("authorization"));
		
		logger.info("authorization of header : " + requestHeader);
		
		String requestAccessToken = StringUtils.removeStart(requestHeader, TokenIssuer.HEADER_PREFIX);
		
		if (StringUtils.isEmpty(requestAccessToken)) {
			throw new AccessTokenEmptyException("Access Token of header is empty.");
		}
		
		try {
			tokenIssuer.decryptUserAccessToken(requestAccessToken);
		} catch(TokenExpiredException e) {
			throw new TokenExpiredException(e.getLocalizedMessage());
		} catch(Exception e) {
			throw new AccessTokenInvalidException("Your token is invalid.");
		}
		
		return true;
	}
}