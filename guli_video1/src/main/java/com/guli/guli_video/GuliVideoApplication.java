package com.guli.guli_video;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableEurekaClient
@ComponentScan(basePackages={"com.guli.guli_video","com.guli.common"})
public class GuliVideoApplication {

	public static void main(String[] args) {
		SpringApplication.run(GuliVideoApplication.class, args);
	}

}
