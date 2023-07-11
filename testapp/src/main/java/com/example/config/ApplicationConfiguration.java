package com.example.config;

import io.ipinfo.api.IPinfo;
import io.ipinfo.spring.IPinfoSpring;
import io.ipinfo.spring.strategies.attribute.SessionAttributeStrategy;
import io.ipinfo.spring.strategies.interceptor.BotInterceptorStrategy;
import io.ipinfo.spring.strategies.ip.SimpleIPStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {
    @Bean
    public IPinfoSpring ipinfoSpring() {
        return new IPinfoSpring.Builder()
            .setIPinfo(new IPinfo.Builder().build())
            .interceptorStrategy(new BotInterceptorStrategy())
            .ipStrategy(new SimpleIPStrategy())
            .attributeStrategy(new SessionAttributeStrategy())
            .build();
    }
}
