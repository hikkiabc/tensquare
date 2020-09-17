package com.tsq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class Eureka8686 {
    public static void main(String[] args) {
        SpringApplication.run(Eureka8686.class,args);
    }
}
