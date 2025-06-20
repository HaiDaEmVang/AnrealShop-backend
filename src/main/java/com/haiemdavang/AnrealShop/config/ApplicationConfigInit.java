package com.haiemdavang.AnrealShop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.client.RestTemplate;

@Configuration
@PropertySource("classpath:errorMessage/MessageString.properties")
public class ApplicationConfigInit {
    public static String IMAGE_USER_DEFAULT = "https://res.cloudinary.com/dqogp38jb/image/upload/v1750060824/7309681_msx5j1.jpg";

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
