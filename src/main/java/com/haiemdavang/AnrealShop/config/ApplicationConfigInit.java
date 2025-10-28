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

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverterFactory(stringToEnumConverterFactory);
    }
}
