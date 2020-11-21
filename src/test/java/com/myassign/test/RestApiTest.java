package com.myassign.test;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import com.myassign.model.dto.AccessToken;
import com.myassign.model.entity.User;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class RestApiTest {

    @Autowired
    private RestTemplate restTemplate;

    private String myAccessToken;

    private UUID roomId;

    private User user;

    @Before
    public void setUp() {
        this.user = User.builder().id("user-0").password("1234").build();
        signin();
        getRoom();
    }

    /**
     * 뿌리기 테스트
     */
    @Test
    public void sprayTest() {

        int totalPrice = 3247;
        int userCount = 3;

        /* @formatter:off */
        ResponseEntity<String> responseEntity = restTemplate.exchange("http://localhost:8080/transaction?totalPrice={totalPrice}&userCount={userCount}", 
                                                                        HttpMethod.POST, 
                                                                        getHttpHeader(null, user.getId(), roomId),
                                                                        new ParameterizedTypeReference<String>() {},
                                                                        Integer.toString(totalPrice),
                                                                        Integer.toString(userCount));
        /* @formatter:on */
        log.info("responseEntity : " + responseEntity.getStatusCode());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        log.info("response body : " + responseEntity.getBody());
        assertThat(responseEntity.getStatusCode()).isNotNull();
    }

    private HttpEntity<?> getHttpHeader(Object body, String userId, UUID roomId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + myAccessToken);
        headers.add("X-USER-ID", userId);
        if (roomId != null) {
            headers.add("X-ROOM-ID", roomId.toString());
        }
        HttpEntity<?> entity = new HttpEntity<Object>(body, headers);
        return entity;
    }

    private void signin() {

        /* @formatter:off */
        ResponseEntity<AccessToken> responseEntity = restTemplate.exchange("http://localhost:8080/sign?userId={id}&password={password}", 
                                                                            HttpMethod.POST, 
                                                                            getHttpHeader(null, StringUtils.EMPTY, null),
                                                                            new ParameterizedTypeReference<AccessToken>() {}, 
                                                                            user.getId(), 
                                                                            user.getPassword());
        /* @formatter:on */
        assertThat(responseEntity.getBody()).isNotNull();

        AccessToken accessToken = responseEntity.getBody();
        log.info("accessToken : " + accessToken);

        if (accessToken != null)
            this.myAccessToken = accessToken.getToken();
    }

    private void getRoom() {

        /* @formatter:off */
        ResponseEntity<String> responseEntity = restTemplate.exchange("http://localhost:8080/room", 
                                                                      HttpMethod.GET, 
                                                                      getHttpHeader(null, StringUtils.EMPTY, null),
                                                                      new ParameterizedTypeReference<String>() {});
        /* @formatter:on */
        assertThat(responseEntity.getBody()).isNotNull();

        String roomId = responseEntity.getBody();

        if (roomId != null)
            this.roomId = UUID.fromString(roomId);
    }
}
