package com.supermarket.back.config;

//解决跨域问题
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration //自动配置
public class CrosConfig implements WebMvcConfigurer {
    @Override //以下代码不变
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowCredentials(true)
                .allowedMethods("POST", "HEAD", "GET", "PUT", "OPTIONS", "DELETE")
                .maxAge(3600)
                .allowedOriginPatterns("*");//Access-Control-Allow-Origin
    }
}

