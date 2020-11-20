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
				.excludePathPatterns("/expired")
				.excludePathPatterns("/login", "/login/action", "/webconsole/altCode/ref", "/webconsole/dialog/images/*", "/webskin/**/*")
				.excludePathPatterns("/portal/**")
				.excludePathPatterns("/css/**", "/font/**", "/dev/**", "/img/**", "/js/**", "/images/**")
				.excludePathPatterns("/csrf", "/swagger-resources/**", "/swagger-ui.html", "/v2/api-docs", "/webjars/**", "/configuration/ui", "/configuration/security");
		/* @formatter:off */
	}
}