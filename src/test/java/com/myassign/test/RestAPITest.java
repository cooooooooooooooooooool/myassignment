package com.myassign.test;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

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
import com.myassign.model.dto.FinanceStatVo;
import com.myassign.model.dto.InstituteAvgMinMaxAmountVo;
import com.myassign.model.entity.Institute;
import com.myassign.model.entity.User;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class RestAPITest {

    @Autowired
    private RestTemplate restTemplate;

    private String myAccessToken;

    private User user;

    private HttpEntity<?> getHttpHeader(Object body) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + myAccessToken);
        HttpEntity<?> entity = new HttpEntity<Object>(body, headers);
        return entity;
    }

    @Before
    public void setUp() {
        this.user = User.builder().id("test02").password("1234").build();
    }

    private void signin() {

        /* @formatter:off */
        ResponseEntity<AccessToken> responseEntity = restTemplate.exchange("http://localhost:8080/api/oauth?id={id}&password={password}", 
                                                                            HttpMethod.GET, 
                                                                            getHttpHeader(null), 
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

    /**
     * 사용자 등록 테스트
     */
    @Test
    public void signupTest() {
        /* @formatter:off */
        ResponseEntity<String> responseEntity = restTemplate.exchange("http://localhost:8080/api/oauth", 
                                                                        HttpMethod.POST, 
                                                                        getHttpHeader(user), 
                                                                        new ParameterizedTypeReference<String>() {});
        /* @formatter:on */
        log.info("responseEntity : " + responseEntity.getStatusCode());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        log.info("response body : " + responseEntity.getBody());
        assertThat(responseEntity.getStatusCode()).isNotNull();
    }

    /**
     * 사용자 인증 및 토큰 발급 테스트
     */
    @Test
    public void signinTest() {

        /* @formatter:off */
        ResponseEntity<AccessToken> responseEntity = restTemplate.exchange("http://localhost:8080/api/oauth?id={id}&password={password}", 
                                                                            HttpMethod.GET, 
                                                                            getHttpHeader(null), 
                                                                            new ParameterizedTypeReference<AccessToken>() {}, 
                                                                            user.getId(), 
                                                                            user.getPassword());
        /* @formatter:on */
        assertThat(responseEntity.getBody()).isNotNull();

        AccessToken accessToken = responseEntity.getBody();
        log.info("accessToken : " + accessToken);
    }

    /**
     * 토큰 재발급
     * 
     * @throws InterruptedException
     */
    @Test
    public void tokenRefreshTest() throws InterruptedException {

        log.info("Signin and token generate!");
        signin();

        log.info("Waiting 65 seconds for token expire ...");
        Thread.sleep(1000 * 65);

        // 만료된 토큰으로 토큰 재발급 요청
        /* @formatter:off */
        ResponseEntity<AccessToken> responseEntity = restTemplate.exchange("http://localhost:8080/api/oauth/token/refresh?id={id}&password={password}", 
                                                                            HttpMethod.GET, 
                                                                            getHttpHeader(null), 
                                                                            new ParameterizedTypeReference<AccessToken>() {}, 
                                                                            user.getId(), 
                                                                            user.getPassword());
        /* @formatter:on */
        assertThat(responseEntity.getBody()).isNotNull();

        AccessToken accessToken = responseEntity.getBody();
        log.info("accessToken : " + accessToken);
    }

    /**
     * 금융 기관별 데이터 초기화 테스트
     * 
     * @throws InterruptedException
     */
    @Test
    public void initBankStatusTest() throws InterruptedException {
        signin();
        /* @formatter:off */
        ResponseEntity<String> responseEntity = restTemplate.exchange("http://localhost:8080/api/banks/init", 
                                                                        HttpMethod.POST, 
                                                                        getHttpHeader(null), 
                                                                        new ParameterizedTypeReference<String>() {});
        /* @formatter:on */
        log.info("responseEntity : " + responseEntity.getStatusCode());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    /**
     * 전체 은행 코드 목록을 조회 테스트
     */
    @Test
    public void getBanksTest() {
        signin();
        ResponseEntity<List<Institute>> responseEntity = restTemplate.exchange("http://localhost:8080/api/banks", HttpMethod.GET, getHttpHeader(null), new ParameterizedTypeReference<List<Institute>>() {
        });
        assertThat(responseEntity.getBody()).isNotNull();

        List<Institute> list = responseEntity.getBody();
        for (Institute institute : list) {
            log.info("institute : " + institute);
        }
    }

    /**
     * 연도별 지원 금액 합계 조회 테스트
     */
    @Test
    public void getTotalAmountListTest() {

        signin();
        /* @formatter:off */
        ResponseEntity<List<FinanceStatVo>> responseEntity = restTemplate.exchange("http://localhost:8080/api/banks/sum", 
                                                                                    HttpMethod.GET, 
                                                                                    getHttpHeader(null), 
                                                                                    new ParameterizedTypeReference<List<FinanceStatVo>>() {});
        /* @formatter:on */
        assertThat(responseEntity.getBody()).isNotNull();

        List<FinanceStatVo> list = responseEntity.getBody();
        for (FinanceStatVo financeStat : list) {
            log.info("financeStat : " + financeStat);
        }
    }

    /**
     * 각 년도별 각 기관의 전체 지원금액 중에서 가장 큰 금액의 기관명 조회 테스트
     */
    @Test
    public void getMaxAmountInstitueListTest() {

        signin();
        /* @formatter:off */
        ResponseEntity<List<FinanceStatVo>> responseEntity = restTemplate.exchange("http://localhost:8080/api/banks/max", 
                                                                                    HttpMethod.GET, 
                                                                                    getHttpHeader(null), 
                                                                                    new ParameterizedTypeReference<List<FinanceStatVo>>() {});
        /* @formatter:on */
        assertThat(responseEntity.getBody()).isNotNull();

        List<FinanceStatVo> list = responseEntity.getBody();
        for (FinanceStatVo financeStat : list) {
            log.info("financeStat : " + financeStat);
        }
    }

    /**
     * 각 년도별 각 기관의 전체 지원금액 중에서 가장 큰 금액의 기관명 조회 테스트
     */
    @Test
    public void getInstituteAvgMinMaxAmountTest() {

        String instituteName = "외환은행";

        signin();
        /* @formatter:off */
        ResponseEntity<InstituteAvgMinMaxAmountVo> responseEntity = restTemplate.exchange("http://localhost:8080/api/banks/avg/{instituteName}", 
                                                                                            HttpMethod.GET, 
                                                                                            getHttpHeader(null), 
                                                                                            new ParameterizedTypeReference<InstituteAvgMinMaxAmountVo>() {}, 
                                                                                            instituteName);
        /* @formatter:on */
        assertThat(responseEntity.getBody()).isNotNull();

        InstituteAvgMinMaxAmountVo instituteAvgMinMaxAmount = responseEntity.getBody();
        log.info("instituteAvgMinMaxAmount : " + instituteAvgMinMaxAmount);
    }
}
