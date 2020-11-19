package com.cp3.cloud.mq.properties;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;


/**
 * 操作日志配置类
 *
 * @author cp3
 * @date 2020年03月09日15:00:47
 */
@Data
@NoArgsConstructor
@ConfigurationProperties(prefix = MqProperties.PREFIX)
public class MqProperties {
    public static final String PREFIX = "zuihou.rabbitmq";

    /**
     * 是否启用
     */
    private Boolean enabled = true;

}
