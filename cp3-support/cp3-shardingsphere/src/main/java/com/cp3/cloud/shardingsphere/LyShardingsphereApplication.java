package com.cp3.cloud.shardingsphere;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.transaction.jta.JtaAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import tk.mybatis.spring.annotation.MapperScan;


@SpringBootApplication(exclude = {JtaAutoConfiguration.class})
@MapperScan(basePackages = "com.cp3.cloud.shardingsphere.mapper")
public class LyShardingsphereApplication {
    public static void main(String[] args) {
        SpringApplication.run(LyShardingsphereApplication.class, args);
    }

}

