package com.myassign;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
@ComponentScan("com.myassign")
public class ApiApplication {

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

    public static void main(String[] args) {
        /* @formatter:off */
        Arrays.stream( args )
              .forEach( log::debug );
        /* @formatter:on */
        SpringApplication.run(ApiApplication.class, args);
    }
}