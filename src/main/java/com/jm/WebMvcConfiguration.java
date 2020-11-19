package com.jm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import com.jm.interceptor.AccessTokenInterceptor;

@Component
public class WebMvcConfiguration extends WebMvcConfigurationSupport {

    @Autowired
    private AccessTokenInterceptor accessTokenInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        /* @formatter:off */
		registry.addInterceptor(accessTokenInterceptor)
		        .addPathPatterns("/**/*")
		        .excludePathPatterns("/oauth/**");
		/* @formatter:on */
    }
}