package com.myassign.test;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Ignore;
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

import com.myassign.model.dto.TransactionResultDto;
import com.myassign.model.dto.TransactionStatusDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Ignore
@SpringBootTest
@RunWith(SpringRunner.class)
public class RestApiTest {

    @Autowired
    private RestTemplate restTemplate;

    private String token;

    private UUID roomId;

    @Before
    public void setUp() {
        getRoom();
    }

    /**
     * 뿌리기 테스트
     */
    @Test
    public void sprayTest() {

        String userId = "user-0";

        int totalPrice = 3247;
        int userCount = 3;

        /* @formatter:off */
        ResponseEntity<String> responseEntity = restTemplate.exchange("http://localhost:8080/transaction?totalPrice={totalPrice}&userCount={userCount}", 
                                                                        HttpMethod.POST, 
                                                                        getHttpHeader(null, userId, roomId),
                                                                        new ParameterizedTypeReference<String>() {},
                                                                        Integer.toString(totalPrice),
                                                                        Integer.toString(userCount));
        /* @formatter:on */
        log.info("responseEntity : " + responseEntity.getStatusCode());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        log.info("response body : " + responseEntity.getBody());
        assertThat(responseEntity.getStatusCode()).isNotNull();

        this.token = responseEntity.getBody();
    }

    /**
     * 받기 테스트
     */
    @Test
    public void receiveTest() {

        String userId = "user-1";

        // 뿌리기 후 토큰 받기
        sprayTest();

        /* @formatter:off */
        ResponseEntity<TransactionResultDto> responseEntity = restTemplate.exchange("http://localhost:8080/transaction/{token}", 
                                                                                    HttpMethod.PUT,
                                                                                    getHttpHeader(null, userId, roomId),
                                                                                    new ParameterizedTypeReference<TransactionResultDto>() {},
                                                                                    token);
        /* @formatter:on */
        log.info("responseEntity : " + responseEntity.getStatusCode());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        TransactionResultDto result = responseEntity.getBody();

        log.info("result : " + result);
    }

    /**
     * 상태 조회 테스트
     */
    @Test
    public void statusTest() {

        String userId = "user-0";

        // 뿌리기 후 토큰 받기
        sprayTest();
        receiveTest();

        /* @formatter:off */
        ResponseEntity<TransactionStatusDto> responseEntity = restTemplate.exchange("http://localhost:8080/transaction?token={token}", 
                                                                                    HttpMethod.GET, 
                                                                                    getHttpHeader(null, userId, roomId),
                                                                                    new ParameterizedTypeReference<TransactionStatusDto>() {},
                                                                                    token);
        /* @formatter:on */
        log.info("responseEntity : " + responseEntity.getStatusCode());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        TransactionStatusDto result = responseEntity.getBody();

        log.info("result : " + result);
    }

    private HttpEntity<?> getHttpHeader(Object body, String userId, UUID roomId) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-USER-ID", userId);
        if (roomId != null) {
            headers.add("X-ROOM-ID", roomId.toString());
        }
        HttpEntity<?> entity = new HttpEntity<Object>(body, headers);
        return entity;
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
