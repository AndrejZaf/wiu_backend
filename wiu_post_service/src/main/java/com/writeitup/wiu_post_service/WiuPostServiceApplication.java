package com.writeitup.wiu_post_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class WiuPostServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(WiuPostServiceApplication.class, args);
	}

}
