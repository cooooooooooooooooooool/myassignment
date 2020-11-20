package com.myassign.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.myassign.interceptor.AccessTokenInterceptor;

@EnableWebMvc
@Configuration
public class InterceptorConfiguration implements WebMvcConfigurer {

    @Autowired
    private AccessTokenInterceptor accessTokenInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        /* @formatter:off */
		registry.addInterceptor(accessTokenInterceptor)
				.addPathPatterns("/**/*")
				.excludePathPatterns("/sign")
				.excludePathPatterns("/room");
		/* @formatter:off */
	}
}