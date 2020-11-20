package com.myassign.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.myassign.exception.UserNotFoundException;
import com.myassign.exception.UserPasswordWrongException;
import com.myassign.model.dto.AccessToken;
import com.myassign.model.dto.UserDto;
import com.myassign.model.entity.User;
import com.myassign.service.UserService;
import com.myassign.util.AccessTokenIssuer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/sign")
public class SignController {

    private final UserService userService;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * 로그인 처리
     */
    @PostMapping
    public AccessToken signin(@RequestParam String userId, @RequestParam String password) {

        log.info("userId : {}, password : {}", userId, password);

        User user = userService.getUser(userId);

        if (user == null)
            throw new UserNotFoundException(userId);

        // 비밀번호 체크
        if (!user.getPassword().equals(DigestUtils.sha256Hex(password)))
            throw new UserPasswordWrongException(userId);

        // 토큰 발급 및 반환
        return AccessTokenIssuer.generateAuthenticateToken(user);
    }

    /**
     * 로그인 사용자 조회
     */
    @GetMapping("/find")
    public UserDto getAccessTokenUser(HttpServletRequest request) {

        User user = userService.getUser((String) request.getAttribute("loginUser"));

        if (user == null)
            throw new UserNotFoundException("no login user");

        return convertToDto(user);
    }

    private UserDto convertToDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }
}