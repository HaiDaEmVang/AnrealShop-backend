package com.haiemdavang.AnrealShop.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:errorMessage/MessageString.properties")
public class ApplicationConfigInit {
}
