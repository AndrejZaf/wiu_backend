package com.writeitup.wiu_user_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class WiuUserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(WiuUserServiceApplication.class, args);
    }

}
