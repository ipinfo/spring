package com.example.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import io.ipinfo.spring.IPinfoSpring;
import org.springframework.beans.factory.annotation.Autowired;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer  {
    private final IPinfoSpring ipinfoSpring;

    @Autowired
    public WebMvcConfig(IPinfoSpring ipinfoSpring) {
        this.ipinfoSpring = ipinfoSpring;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(ipinfoSpring);
    }

}
