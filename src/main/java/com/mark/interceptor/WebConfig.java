package com.mark.interceptor;/*
 * @project: myblog
 * @name: WebConfig
 * @author: Mark
 * @Date: 2021/4/13
 * @Time: 23:41
 */

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/login")
                .excludePathPatterns("/logIn");
    }
}
