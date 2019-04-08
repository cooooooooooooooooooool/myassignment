package com.jm.test;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.jm.entity.Institute;
import com.jm.entity.User;
import com.jm.vo.AccessToken;
import com.jm.vo.FinanceStatVo;
import com.jm.vo.InstituteAvgMinMaxAmountVo;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RestAPITest {

	private static final Logger logger = LoggerFactory.getLogger(RestAPITest.class);

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
		this.user = new User("test02", "1234");
	}
	
	private void signin() {
		
		ResponseEntity<AccessToken> responseEntity = restTemplate.exchange("http://localhost:8080/api/oauth?id={id}&password={password}", HttpMethod.GET, getHttpHeader(null), new ParameterizedTypeReference<AccessToken>() {}, user.getId(), user.getPassword());
		assertThat(responseEntity.getBody()).isNotNull();
		
		AccessToken accessToken = responseEntity.getBody();
		logger.info("accessToken : " + accessToken);
		
		if (accessToken!=null) this.myAccessToken = accessToken.getToken();
	}
	
	/**
	 * 사용자 등록 테스트
	 */
	@Test
	public void signupTest() {
		ResponseEntity<String> responseEntity = restTemplate.exchange("http://localhost:8080/api/oauth", HttpMethod.POST, getHttpHeader(user), new ParameterizedTypeReference<String>() {});
		logger.info("responseEntity : " + responseEntity.getStatusCode());
		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		
		logger.info("response body : " + responseEntity.getBody());
		assertThat(responseEntity.getStatusCode()).isNotNull();
	}
	
	/**
	 * 사용자 인증 및 토큰 발급 테스트
	 */
	@Test
	public void signinTest() {
		
		ResponseEntity<AccessToken> responseEntity = restTemplate.exchange("http://localhost:8080/api/oauth?id={id}&password={password}", HttpMethod.GET, getHttpHeader(null), new ParameterizedTypeReference<AccessToken>() {}, user.getId(), user.getPassword());
		assertThat(responseEntity.getBody()).isNotNull();
		
		AccessToken accessToken = responseEntity.getBody();
		logger.info("accessToken : " + accessToken);
	}
	
	/**
	 * 토큰 재발급
	 * @throws InterruptedException 
	 */
	@Test
	public void tokenRefreshTest() throws InterruptedException {
		
		logger.info("signin and token generate!");
		signin();
		
		logger.info("waiting 12 seconds for token expire ...");
		Thread.sleep(12000);
		
		// 만료된 토큰으로 토큰 재발급 요청
		ResponseEntity<AccessToken> responseEntity = restTemplate.exchange("http://localhost:8080/api/oauth/token/refresh?id={id}&password={password}", HttpMethod.GET, getHttpHeader(null), new ParameterizedTypeReference<AccessToken>() {}, user.getId(), user.getPassword());
		assertThat(responseEntity.getBody()).isNotNull();
		
		AccessToken accessToken = responseEntity.getBody();
		logger.info("accessToken : " + accessToken);
	}
	
	/**
	 * 금융 기관별 데이터 초기화 테스트
	 * @throws InterruptedException 
	 */
	@Test
	public void initBankStatusTest() throws InterruptedException {
		signin();
		ResponseEntity<String> responseEntity = restTemplate.exchange("http://localhost:8080/api/banks/init", HttpMethod.POST, getHttpHeader(null), new ParameterizedTypeReference<String>() {});
		logger.info("responseEntity : " + responseEntity.getStatusCode());
		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
	}
	
	/**
	 * 전체 은행 코드 목록을 조회 테스트
	 */
	@Test
	public void getBanksTest() {
		signin();
		ResponseEntity<List<Institute>> responseEntity = restTemplate.exchange("http://localhost:8080/api/banks", HttpMethod.GET, getHttpHeader(null), new ParameterizedTypeReference<List<Institute>>() {});
		assertThat(responseEntity.getBody()).isNotNull();
		
		List<Institute> list = responseEntity.getBody();
		for (Institute institute : list) {
			logger.info("institute : " + institute);
		}
	}
	
	/**
	 * 연도별 지원 금액 합계 조회 테스트
	 */
	@Test
	public void getTotalAmountListTest() {
		
		signin();
		ResponseEntity<List<FinanceStatVo>> responseEntity = restTemplate.exchange("http://localhost:8080/api/banks/sum", HttpMethod.GET, getHttpHeader(null), new ParameterizedTypeReference<List<FinanceStatVo>>() {});
		assertThat(responseEntity.getBody()).isNotNull();
		
		List<FinanceStatVo> list = responseEntity.getBody();
		for (FinanceStatVo financeStat : list) {
			logger.info("financeStat : " + financeStat);
		}
	}
	
	/**
	 * 각 년도별 각 기관의 전체 지원금액 중에서 가장 큰 금액의 기관명 조회 테스트
	 */
	@Test
	public void getMaxAmountInstitueListTest() {
		
		signin();
		ResponseEntity<List<FinanceStatVo>> responseEntity = restTemplate.exchange("http://localhost:8080/api/banks/max", HttpMethod.GET, getHttpHeader(null), new ParameterizedTypeReference<List<FinanceStatVo>>() {});
		assertThat(responseEntity.getBody()).isNotNull();
		
		List<FinanceStatVo> list = responseEntity.getBody();
		for (FinanceStatVo financeStat : list) {
			logger.info("financeStat : " + financeStat);
		}
	}
	
	/**
	 * 각 년도별 각 기관의 전체 지원금액 중에서 가장 큰 금액의 기관명 조회 테스트
	 */
	@Test
	public void getInstituteAvgMinMaxAmountTest() {
		
		String instituteName = "외환은행";
		
		signin();
		ResponseEntity<InstituteAvgMinMaxAmountVo> responseEntity = restTemplate.exchange("http://localhost:8080/api/banks/avg/{instituteName}", HttpMethod.GET, getHttpHeader(null), new ParameterizedTypeReference<InstituteAvgMinMaxAmountVo>() {}, instituteName);
		assertThat(responseEntity.getBody()).isNotNull();
		
		InstituteAvgMinMaxAmountVo instituteAvgMinMaxAmount = responseEntity.getBody();
		logger.info("instituteAvgMinMaxAmount : " + instituteAvgMinMaxAmount);
	}
}
