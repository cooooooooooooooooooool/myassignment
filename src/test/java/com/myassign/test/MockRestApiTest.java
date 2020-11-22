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

import lombok.extern.slf4j.Slf4j;

@Slf4j
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@SpringBootTest(properties = "spring.config.location=classpath:/application-test.yml", webEnvironment = WebEnvironment.RANDOM_PORT)
public class MockRestApiTest {

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
    
    @Test
    public void sprayTest() throws Exception {
        
        // 뿌리기 전체 금액
        int targetPrice = 3247;
        
        // 사용자수
        int targetCount = 3;
        
        // Rest API 호출 결과
        MvcResult result = null;
        
        /* @formatter:off */
        // 뿌리기 테스트, 사용자 아이디 : user-0
        String userId = "user-0";
        result = mock.perform(MockMvcRequestBuilders.post("/transaction").headers(getHttpHeader(userId, roomId))
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
        
        // 받기 테스트, 사용자 아이디 : user-1 
        userId = "user-1";
        result = mock.perform(MockMvcRequestBuilders.put("/transaction/"+token).headers(getHttpHeader(userId, roomId))
                                                                               .contentType(MediaType.APPLICATION_JSON)
                                                                               .accept(MediaType.APPLICATION_JSON))
                                                    .andDo(print())
                                                    .andExpect(status().isOk())
                                                    .andReturn();
        
        // 받기 테스트 : 뿌린 사용자가 받기를 시도하는 경우 퍼미션 에러 발생, 사용자 아이디 : user-0 
        userId = "user-0";
        result = mock.perform(MockMvcRequestBuilders.put("/transaction/"+token).headers(getHttpHeader(userId, roomId))
                                                                               .contentType(MediaType.APPLICATION_JSON)
                                                                               .accept(MediaType.APPLICATION_JSON))
                                                    .andDo(print())
                                                    .andExpect(status().isForbidden())
                                                    .andReturn();
        
        // 상태 조회 테스트 : 본인이 뿌리기 조회, 사용자 아이디 : user-0
        userId = "user-0";
        result = mock.perform(MockMvcRequestBuilders.get("/transaction").headers(getHttpHeader(userId, roomId))
                                                                        .contentType(MediaType.APPLICATION_JSON)
                                                                        .accept(MediaType.APPLICATION_JSON)
                                                                        .param("token", token))
                                                    .andDo(print())
                                                    .andExpect(status().isOk())
                                                    .andReturn();
        
        // 상태 조회 테스트 : 다른 사용자가 뿌리기 조회시 에러 발생, 사용자 아이디 : user-1
        userId = "user-1";
        result = mock.perform(MockMvcRequestBuilders.get("/transaction").headers(getHttpHeader(userId, roomId))
                                                                        .contentType(MediaType.APPLICATION_JSON)
                                                                        .accept(MediaType.APPLICATION_JSON)
                                                                        .param("token", token))
                                                    .andDo(print())
                                                    .andExpect(status().isForbidden())
                                                    .andReturn();
        
        // 받기 테스트 : 2분(테스트환경) 후 받기 시도시 잘못된 요청 에러 발생, 사용자 아이디 : user-2

        Thread.sleep(60*1000*2);
        userId = "user-2";
        result = mock.perform(MockMvcRequestBuilders.put("/transaction/"+token).headers(getHttpHeader(userId, roomId))
                                                                               .contentType(MediaType.APPLICATION_JSON)
                                                                               .accept(MediaType.APPLICATION_JSON))
                                                    .andDo(print())
                                                    .andExpect(status().isBadRequest())
                                                    .andReturn();
        /* @formatter:on */
        log.info("transaction : {}", result.getResponse().getContentAsString());
    }

    private HttpHeaders getHttpHeader(String userId, UUID roomId) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-USER-ID", userId);
        if (roomId != null) {
            headers.add("X-ROOM-ID", roomId.toString());
        }
        return headers;
    }
}
