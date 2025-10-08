package com.haiemdavang.AnrealShop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAspectJAutoProxy
@EnableScheduling
@EnableCaching
public class AnrealShopApplication {

	public static void main(String[] args) {
		SpringApplication.run(AnrealShopApplication.class, args);
	}

}
