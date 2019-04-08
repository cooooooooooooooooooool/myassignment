package com.jm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.IteratorUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.web.client.RestTemplate;

import com.jm.entity.Institute;
import com.jm.service.InstituteService;

@EnableScheduling
@ServletComponentScan
@SpringBootApplication
public class StartBankApplication extends SpringBootServletInitializer {
	
	@Bean 
	public ServletWebServerFactory servletWebServerFactory(){
		return new TomcatServletWebServerFactory();
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean
	public TaskScheduler taskScheduler() {
		return new ConcurrentTaskScheduler();
	}
	
	@Bean
	@Qualifier("instituteCsvHeaderMap")
	public Map<String, String> instituteCsvHeaderMap() {
		Map<String, String> map = new HashMap<>();
		map.put("주택도시기금1)(억원)", "CITY_FUND");
		map.put("국민은행(억원)", "KB");
		map.put("우리은행(억원)", "WOORI");
		map.put("신한은행(억원)", "SHINHAN");
		map.put("한국시티은행(억원)", "CITY");
		map.put("하나은행(억원)", "HANA");
		map.put("농협은행/수협은행(억원)", "NH");
		map.put("외환은행(억원)", "KEB");
		map.put("기타은행(억원)", "ETC");
		return map;
	}
	
	@Bean
	@Qualifier("instituteNameMap")
	public Map<String, String> instituteNameMap() {
		Map<String, String> map = new HashMap<>();
		map.put("CITY_FUND", "주택도시기금");
		map.put("KB", "국민은행");
		map.put("WOORI", "우리은행");
		map.put("SHINHAN", "신한은행");
		map.put("CITY", "한국시티은행");
		map.put("HANA", "하나은행");
		map.put("NH", "농협은행/수협은행");
		map.put("KEB", "외환은행");
		map.put("ETC", "기타은행");
		return map;
	}
	
	/*
	 * csv to h2 database
	 */
	@Bean
	public CommandLineRunner initBank(InstituteService instituteService, @Qualifier("instituteCsvHeaderMap") Map<String, String> instituteCsvHeaderMap, @Qualifier("instituteNameMap") Map<String, String> instituteNameMap) {
		
		return args -> {
			
			Iterator<String> keyset = instituteCsvHeaderMap.keySet().iterator();
			List<String> keys = IteratorUtils.toList(keyset); 
			
			List<Institute> list = new ArrayList<>();
			for (String key : keys) {
				list.add(new Institute(instituteCsvHeaderMap.get(key), instituteNameMap.get(instituteCsvHeaderMap.get(key))));
			}
			
			// 은행 코드 저장
			if (list!=null && list.size()>0) {
				instituteService.saveInstitutes(list);
			}
		};
	}
	
	public static void main(String[] args) {
		SpringApplication.run(StartBankApplication.class, args);
	}
}