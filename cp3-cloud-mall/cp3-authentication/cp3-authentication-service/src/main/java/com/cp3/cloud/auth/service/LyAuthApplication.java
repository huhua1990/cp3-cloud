package com.cp3.cloud.auth.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @Author: 98050
 * @Time: 2018-10-23 20:11
 * @Feature: 授权服务启动器
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
//@EnableHystrix
public class LyAuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(LyAuthApplication.class,args);
    }

}
