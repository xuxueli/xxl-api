package com.xxl.api.admin.web.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import jakarta.annotation.Resource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * web mvc config
 *
 * @author xuxueli 2018-04-02 20:48:20
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Resource
    private CookieInterceptor cookieInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(cookieInterceptor).addPathPatterns("/**");
    }

}