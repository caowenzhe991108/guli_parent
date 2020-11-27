package com.guli.staticcenter;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages={"com.guli.staticcenter","com.guli.common"})
@EnableEurekaClient
@EnableFeignClients
public class StaticcenterApplication {
    public static void main(String[] args) {
        SpringApplication.run(StaticcenterApplication.class, args);
    }
}
