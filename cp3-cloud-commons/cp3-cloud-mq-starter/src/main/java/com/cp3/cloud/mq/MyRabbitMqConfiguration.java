package com.cp3.cloud.mq;

import com.cp3.cloud.mq.properties.MqProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * rabbit 禁用配置
 * <p>
 * 若自建服务的 包名 跟当前类的包名不同，请在服务的启动类上配置下列注解，否则启动报错
 * \@ComponentScan({
 * "xxx",  // xxx 改成自建服务的 包名
 * "com.cp3.cloud.mq"
 * })
 *
 * @author cp3
 * @date 2019/09/20
 */
@Configuration
@Import(MyRabbitMqConfiguration.RabbitMqConfiguration.class)
public class MyRabbitMqConfiguration {
    @Slf4j
    @Configuration
    @ConditionalOnProperty(prefix = MqProperties.PREFIX, name = "enabled", havingValue = "false", matchIfMissing = true)
    @EnableAutoConfiguration(exclude = {RabbitAutoConfiguration.class})
    public static class RabbitMqConfiguration {
        public RabbitMqConfiguration() {
            log.warn("检测到zuihou.rabbitmq.enabled=false，排除了 RabbitMQ");
        }
    }

}
