package com.haiemdavang.AnrealShop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.client.RestTemplate;

@Configuration
@PropertySource("classpath:errorMessage/MessageString.properties")
public class ApplicationConfigInit {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
