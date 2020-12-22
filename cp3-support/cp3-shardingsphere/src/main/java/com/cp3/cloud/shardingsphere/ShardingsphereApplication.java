package com.cp3.cloud.shardingsphere;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.transaction.jta.JtaAutoConfiguration;
import tk.mybatis.spring.annotation.MapperScan;


@SpringBootApplication(exclude = {JtaAutoConfiguration.class})
@MapperScan(basePackages = "com.cp3.cloud.shardingsphere.mapper")
public class ShardingsphereApplication {
    public static void main(String[] args) {
        SpringApplication.run(ShardingsphereApplication.class, args);
    }
}

