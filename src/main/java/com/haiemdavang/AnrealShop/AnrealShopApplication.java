package com.haiemdavang.AnrealShop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class AnrealShopApplication {

	public static void main(String[] args) {
		SpringApplication.run(AnrealShopApplication.class, args);
	}

}
