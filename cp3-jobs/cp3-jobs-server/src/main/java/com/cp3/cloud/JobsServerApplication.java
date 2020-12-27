package com.cp3.cloud;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;

import java.net.InetAddress;

/**
 * @Description 任务服务
 * @Auther: cp3
 * @Date: 2020/12/9
 */
@SpringBootApplication
@ComponentScan({
        "com.cp3.cloud",
        "com.xxl.job.admin",
})
@Slf4j
public class JobsServerApplication {
    public static void main(String[] args) {
        try {
            ConfigurableApplicationContext application = SpringApplication.run(JobsServerApplication.class, args);
            Environment env = application.getEnvironment();
            log.info("\n----------------------------------------------------------\n\t" +
                            "应用 '{}' 运行成功! 访问连接:\n\t" +
                            "Swagger文档: \t\thttp://{}:{}/doc.html\n\t" +
                            "数据库监控: \t\thttp://{}:{}/druid\n" +
                            "----------------------------------------------------------",
                    env.getProperty("spring.application.name"),
                    InetAddress.getLocalHost().getHostAddress(),
                    env.getProperty("server.port", "8080"),
                    "127.0.0.1",
                    env.getProperty("server.port", "8080"));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
