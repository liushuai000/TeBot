package com.niu.tebot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
public class MyWebConfigurer{

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));// 支持请求方式
        config.addAllowedOriginPattern("*");// 支持跨域
        config.setAllowCredentials(true);// cookie
        config.addAllowedHeader("*");// 允许请求头信息
        config.addExposedHeader("*");// 暴露的头部信息

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);// 添加地址映射
        return new CorsFilter(source);
    }

}
