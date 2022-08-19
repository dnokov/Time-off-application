package com.learning.timeOffManagement.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class RestTemplateBean {
    @Value("${spring.rest.connection.connection-timeout}")
    private int timeOutMilis;

    @Bean
    RestTemplateBuilder restTemplateBuilder(){
        return new RestTemplateBuilder();
    }
    @Bean
    public RestTemplate restTemplate()
    {
        Duration timeOut = Duration.ofMillis(timeOutMilis);
        RestTemplateBuilder restTemplateBuilder = restTemplateBuilder();
        restTemplateBuilder.setConnectTimeout(timeOut);
        restTemplateBuilder.setReadTimeout(timeOut);
        return restTemplateBuilder.build();
    }
}
