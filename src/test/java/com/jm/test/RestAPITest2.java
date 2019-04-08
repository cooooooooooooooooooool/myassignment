package com.jm.test;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jm.entity.User;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class RestAPITest2 {

	private static final Logger logger = LoggerFactory.getLogger(RestAPITest2.class);

	private User user;

	@Autowired
	private MockMvc mockMvc;

	@Before
	public void setUp() {
		this.user = new User("test02", "1234");
		logger.info("RestAPITest's setup is done...");
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

	/**
	 * 사용자 등록 테스트
	 */
	@Test
	public void signupTest() throws Exception {
		
		mockMvc.perform(MockMvcRequestBuilders
				.post("/oauth")
				.content(asJsonString(user))
			    .contentType(MediaType.APPLICATION_JSON)
			    .accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk());
	}
	
	/**
	 * 사용자 인증 및 토큰 발급 테스트
	 */
	@Test
	public void signinTest() throws Exception {
		
		// 사용자 등록
		signupTest();
		
		// 사용자 인증 및 토큰 발급 테스트
		mockMvc.perform(MockMvcRequestBuilders
				.get("/oauth?id=" + user.getId() + "&password=" + user.getPassword())
				.content(asJsonString(user))
			    .contentType(MediaType.APPLICATION_JSON)
			    .accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk());
	}
}
