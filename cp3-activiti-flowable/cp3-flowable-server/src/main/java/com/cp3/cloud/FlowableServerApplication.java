package com.cp3.cloud;

import com.cp3.base.security.annotation.EnableLoginArgResolver;
import com.cp3.base.validator.annotation.EnableFormValidator;
import com.cp3.cloud.flowable.config.ApplicationConfiguration;
import com.cp3.cloud.flowable.servlet.AppDispatcherServletConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static com.cp3.cloud.common.constant.BizConstant.BUSINESS_PACKAGE;
import static com.cp3.cloud.common.constant.BizConstant.UTIL_PACKAGE;

/**
 * xxx启动类
 *
 * @author cp3
 * @date 2021-01-25
 */
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@EnableDiscoveryClient
@Configuration
@EnableFeignClients(value = { BUSINESS_PACKAGE, UTIL_PACKAGE })
@ComponentScan(basePackages = { BUSINESS_PACKAGE, UTIL_PACKAGE })
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@Slf4j
@EnableLoginArgResolver
@Import({ApplicationConfiguration.class,
        AppDispatcherServletConfiguration.class})
public class FlowableServerApplication {
    public static void main(String[] args) throws UnknownHostException {
        ConfigurableApplicationContext application = SpringApplication.run(FlowableServerApplication.class, args);
        Environment env = application.getEnvironment();
        log.info("\n----------------------------------------------------------\n\t" +
                        "应用 '{}' 启动成功! 访问连接:\n\t" +
                        "Swagger文档: \t\thttp://{}:{}/doc.html\n\t" +
                        "数据库监控: \t\thttp://{}:{}/druid\n" +
                        "----------------------------------------------------------",
                env.getProperty("spring.application.name"),
                InetAddress.getLocalHost().getHostAddress(),
                env.getProperty("server.port", "8080"),
                "127.0.0.1",
                env.getProperty("server.port", "8080"));
    }
}
