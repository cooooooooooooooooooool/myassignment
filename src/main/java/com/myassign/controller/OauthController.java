package com.myassign.controller;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.myassign.TokenIssuer;
import com.myassign.exception.AccessTokenEmptyException;
import com.myassign.exception.AccessTokenInvalidException;
import com.myassign.exception.UserNotFoundException;
import com.myassign.exception.UserPasswordWrongException;
import com.myassign.model.dto.AccessToken;
import com.myassign.model.entity.User;
import com.myassign.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@RestController
@RequestMapping(value = "/oauth")
public class OauthController {

    @Autowired
    private UserService service;

    @Autowired
    private TokenIssuer tokenIssuer;

    /**
     * 사용자 등록
     * 
     * @throws Exception
     */
    @PostMapping()
    public String signup(@RequestBody User user) throws Exception {
        user.setPassword(DigestUtils.sha256Hex(user.getPassword()));
        service.createUser(user);
        AccessToken accessToken = tokenIssuer.generateAuthenticateToken(user);
        return accessToken.getToken();
    }

    /**
     * 토큰 발급
     * 
     * @return
     */
    @GetMapping()
    public AccessToken signin(@RequestParam(value = "id", required = true) String id, @RequestParam(value = "password", required = true) String password) throws Exception {

        User user = service.getUser(id);

        if (user == null)
            throw new UserNotFoundException(id);

        // 비밀번호 체크
        if (!user.getPassword().equals(DigestUtils.sha256Hex(password)))
            throw new UserPasswordWrongException(id);

        // 토큰 발급 및 반환
        return tokenIssuer.generateAuthenticateToken(user);
    }

    /**
     * 토큰 발급
     * 
     * @return
     */
    @GetMapping("/token/refresh")
    public AccessToken refresh(@RequestHeader("authorization") String authorization, @RequestParam(value = "id", required = true) String id, @RequestParam(value = "password", required = true) String password) throws Exception {

        AccessToken accessToken = null;
        log.info("authorization of header : " + authorization);

        String requestAccessToken = StringUtils.removeStart(authorization, TokenIssuer.HEADER_PREFIX);

        if (StringUtils.isEmpty(requestAccessToken)) {
            throw new AccessTokenEmptyException("Access Token of header is empty.");
        }

        try {
            tokenIssuer.decryptUserAccessToken(requestAccessToken);
        } catch (TokenExpiredException e) {

            User user = service.getUser(id);

            if (user == null)
                throw new UserNotFoundException(id);

            // 비밀번호 체크
            if (!user.getPassword().equals(DigestUtils.sha256Hex(password)))
                throw new UserPasswordWrongException(id);

            accessToken = tokenIssuer.generateAuthenticateToken(user);
        } catch (JWTVerificationException e) {
            throw new AccessTokenInvalidException("Your token is invalid.");
        }

        // 토큰 재발
        return accessToken;
    }
}