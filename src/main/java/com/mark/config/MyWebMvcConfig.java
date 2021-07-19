package com.mark.config;/*
 * @project: myblog
 * @name: MyWebMvcConfig
 * @author: Mark
 * @Date: 2021/7/11
 * @Time: 22:44
 */

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MyWebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 所有以imgUpload开头的请求都会去后面配置的路径下寻找资源
        registry.addResourceHandler("/imgUpload/**").addResourceLocations("D:\\IDEA\\workspace\\myblog\\src\\main\\resources\\static\\images");
    }
}
