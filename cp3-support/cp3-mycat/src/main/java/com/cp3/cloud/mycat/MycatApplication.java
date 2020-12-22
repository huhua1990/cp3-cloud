package com.cp3.cloud.mycat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @Description
 * @Auther: hh
 * @Date: 2020/9/23 15:16
 * @Version:1.0
 */
@SpringBootApplication
@MapperScan("com.cp3.cloud.mycat.mapper")
public class MycatApplication {
    public static void main(String[] args) {
        SpringApplication.run(MycatApplication.class, args);
    }
}
