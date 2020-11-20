package com.myassign.test;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.URLEncoder;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myassign.TokenIssuer;
import com.myassign.model.dto.AccessToken;
import com.myassign.model.entity.User;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class MockRestAPITest {

    private User user;

    private String accessToken;

    @Autowired
    private MockMvc mock;

    @Before
    public void setUp() {
        this.user = User.builder().id("test02").password("1234").build();
        log.info("RestAPITest's setup is done...");
    }

    public static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            final String jsonContent = mapper.writeValueAsString(obj);
            return jsonContent;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static AccessToken toAccessToken(final String contents) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            final AccessToken object = mapper.readValue(contents, AccessToken.class);
            return object;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 사용자 등록 테스트
     */
    @Test
    public void signupTest() throws Exception {
        /* @formatter:off */
        mock.perform(MockMvcRequestBuilders.post("/oauth").content(asJsonString(user))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)).andDo(print())
                                                .andExpect(status().isOk());
        /* @formatter:on */
    }

    /**
     * 사용자 인증 및 토큰 발급 테스트
     */
    @Test
    public void signinTest() throws Exception {

        // 사용자 등록
        signupTest();

        // 사용자 인증 및 토큰 발급 테스트
        MvcResult result = mock.perform(MockMvcRequestBuilders.get("/oauth?id=" + user.getId() + "&password=" + user.getPassword()).content(asJsonString(user)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk()).andReturn();

        AccessToken token = toAccessToken(result.getResponse().getContentAsString());
        this.accessToken = token.getToken();
    }

    /**
     * 토큰 재발급
     * 
     * @throws InterruptedException
     */
    @Test
    public void tokenRefreshTest() throws Exception {

        // 사용자 등록
        signupTest();

        signinTest();

        log.info("waiting 62 seconds for token expire ...");
        Thread.sleep(1000 * 60 + 2 * 1000);

        mock.perform(MockMvcRequestBuilders.get("/oauth/token/refresh?id=" + user.getId() + "&password=" + user.getPassword()).header(HttpHeaders.AUTHORIZATION, TokenIssuer.HEADER_PREFIX + accessToken).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk());
    }

    /**
     * 금융 기관별 데이터 초기화 테스트
     * 
     * @throws InterruptedException
     */
    @Test
    public void initBankStatusTest() throws Exception {

        // 사용자 등록
        signupTest();
        signinTest();

        mock.perform(MockMvcRequestBuilders.post("/banks/init").header(HttpHeaders.AUTHORIZATION, TokenIssuer.HEADER_PREFIX + accessToken).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk());
    }

    /**
     * 전체 은행 코드 목록을 조회 테스트
     */
    @Test
    public void getBanksTest() throws Exception {
        signupTest();
        signinTest();
        mock.perform(MockMvcRequestBuilders.get("/banks").header(HttpHeaders.AUTHORIZATION, TokenIssuer.HEADER_PREFIX + accessToken).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk());
    }

    /**
     * 연도별 지원 금액 합계 조회 테스트
     */
    @Test
    public void getTotalAmountListTest() throws Exception {
        signupTest();
        signinTest();

        // csv 데이터 초기화
        initBankStatusTest();

        mock.perform(MockMvcRequestBuilders.get("/banks/sum").header(HttpHeaders.AUTHORIZATION, TokenIssuer.HEADER_PREFIX + accessToken).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk());
    }

    /**
     * 각 년도별 각 기관의 전체 지원금액 중에서 가장 큰 금액의 기관명 조회 테스트
     */
    @Test
    public void getMaxAmountInstitueListTest() throws Exception {
        signupTest();
        signinTest();

        // csv 데이터 초기화
        initBankStatusTest();

        mock.perform(MockMvcRequestBuilders.get("/banks/max").header(HttpHeaders.AUTHORIZATION, TokenIssuer.HEADER_PREFIX + accessToken).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk());
    }

    /**
     * 각 년도별 각 기관의 전체 지원금액 중에서 가장 큰 금액의 기관명 조회 테스트
     */
    @Test
    public void getInstituteAvgMinMaxAmountTest() throws Exception {

        String instituteName = "외환은행";

        signupTest();
        signinTest();

        // csv 데이터 초기화
        initBankStatusTest();

        mock.perform(MockMvcRequestBuilders.get("/banks/avg/" + URLEncoder.encode(instituteName, "UTF-8")).header(HttpHeaders.AUTHORIZATION, TokenIssuer.HEADER_PREFIX + accessToken).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk());
    }
}
