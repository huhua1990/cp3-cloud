package com.cp3.cloud.security.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 属性
 *
 * @author cp3
 * @date 2020年02月24日10:48:35
 */
@Data
@ConfigurationProperties(prefix = SecurityProperties.PREFIX)
public class SecurityProperties {
    public final static String PREFIX = "zuihou.security";
    /**
     * 是否启用uri权限
     */
    private Boolean enabled = true;
    /**
     * 查询用户信息的调用方式
     */
    private UserType type = UserType.FEIGN;
}
