package com.cp3.cloud.zipkin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class LyZipkinApplication {

	public static void main(String[] args) {
		SpringApplication.run(LyZipkinApplication.class, args);
	}

}
