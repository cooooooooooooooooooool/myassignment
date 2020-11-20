package com.myassign.test;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

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
import com.myassign.model.dto.AccessToken;
import com.myassign.util.AccessTokenIssuer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class MockRestAPITest {

    private String accessToken;

    private UUID roomId;

    @Autowired
    private MockMvc mock;

    @Before
    public void setUp() throws Exception {
        /* @formatter:off */
        MvcResult result = mock.perform(MockMvcRequestBuilders.get("/room").contentType(MediaType.APPLICATION_JSON)
                                                                           .accept(MediaType.APPLICATION_JSON))
                                                              .andDo(print())
                                                              .andExpect(status().isOk())
                                                              .andReturn();
        /* @formatter:off */
        
        this.roomId = UUID.fromString(result.getResponse().getContentAsString());
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
    
    @Test
    public void sprayTest() throws Exception {
        
        String userId = "user-0";
        String password = "1234";
        int targetPrice = 3247;
        int targetCount = 3;
        
        // 인증 처리
        login(userId, password);
        
        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, AccessTokenIssuer.HEADER_PREFIX + accessToken);
        headers.add("X-USER-ID", userId);
        headers.add("X-ROOM-ID", roomId.toString());
        
        /* @formatter:off */
        MvcResult result = mock.perform(MockMvcRequestBuilders.get("/sign/find").headers(headers)
                                                                                .contentType(MediaType.APPLICATION_JSON)
                                                                                .accept(MediaType.APPLICATION_JSON))
                                                              .andDo(print())
                                                              .andExpect(status().isOk())
                                                              .andReturn();
        /* @formatter:on */
        log.info("pre login user : {}", result.getResponse().getContentAsString());

        /* @formatter:off */
        result = mock.perform(MockMvcRequestBuilders.post("/spray").headers(headers)
                                                                   .contentType(MediaType.APPLICATION_JSON)
                                                                   .accept(MediaType.APPLICATION_JSON)
                                                                   .param("totalPrice", Integer.toString(targetPrice))
                                                                   .param("userCount", Integer.toString(targetCount)))
                                                    .andDo(print())
                                                    .andExpect(status().isOk())
                                                    .andReturn();
        /* @formatter:on */
        String token = result.getResponse().getContentAsString();
        log.info("transaction token : {}", token);

        /* @formatter:off */
        result = mock.perform(MockMvcRequestBuilders.get("/transaction").headers(headers)
                                                                        .contentType(MediaType.APPLICATION_JSON)
                                                                        .accept(MediaType.APPLICATION_JSON)
                                                                        .param("token", token))
                                                    .andDo(print())
                                                    .andExpect(status().isOk())
                                                    .andReturn();
        /* @formatter:on */
        log.info("transaction : {}", result.getResponse().getContentAsString());

        /* @formatter:off */
        result = mock.perform(MockMvcRequestBuilders.get("/sign/find").headers(headers)
                                                                      .contentType(MediaType.APPLICATION_JSON)
                                                                      .accept(MediaType.APPLICATION_JSON))
                                                    .andDo(print())
                                                    .andExpect(status().isOk())
                                                    .andReturn();
        /* @formatter:on */
        log.info("after login user : {}", result.getResponse().getContentAsString());
    }

    /**
     * 사용자 인증 테스트
     */
    private void login(String userId, String password) throws Exception {

        /* @formatter:off */
        MvcResult result = mock.perform(MockMvcRequestBuilders.post("/sign").header(HttpHeaders.AUTHORIZATION, AccessTokenIssuer.HEADER_PREFIX + accessToken)
                                                                            .contentType(MediaType.APPLICATION_JSON)
                                                                            .accept(MediaType.APPLICATION_JSON)
                                                                            .param("userId", userId)
                                                                            .param("password", password))
                                                              .andDo(print())
                                                              .andExpect(status().isOk())
                                                              .andReturn();
        /* @formatter:on */
        AccessToken token = toAccessToken(result.getResponse().getContentAsString());
        log.info("accessToken : {}", token);
        this.accessToken = token.getToken();
    }
}
