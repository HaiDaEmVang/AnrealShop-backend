package com.haiemdavang.AnrealShop.config;

import com.haiemdavang.AnrealShop.utils.StringToEnumConverterFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@PropertySource("classpath:errorMessage/MessageString.properties")
@RequiredArgsConstructor
public class ApplicationConfigInit implements WebMvcConfigurer {

    private final StringToEnumConverterFactory stringToEnumConverterFactory;
    public static String IMAGE_USER_DEFAULT = "https://res.cloudinary.com/dqogp38jb/image/upload/v1750060824/7309681_msx5j1.jpg";

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverterFactory(stringToEnumConverterFactory);
    }
}
