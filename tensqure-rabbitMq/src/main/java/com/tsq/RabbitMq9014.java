package com.tsq;

import com.tsq.utils.SnowFlakeWorker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class RabbitMq9014 {
    public static void main(String[] args) {
        SpringApplication.run(RabbitMq9014.class,args);
    }

    @Bean
    public SnowFlakeWorker snowFlakeWorker(){
        return new SnowFlakeWorker(1,1);
    }
}
