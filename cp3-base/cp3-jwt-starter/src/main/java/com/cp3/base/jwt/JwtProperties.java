package com.cp3.base.jwt;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import static com.cp3.base.jwt.JwtProperties.PREFIX;

/**
 * 认证服务端 属性
 *
 * @author zuihou
 * @date 2018/11/20
 */
@Data
@NoArgsConstructor
@ConfigurationProperties(prefix = PREFIX)
public class JwtProperties {
    public static final String PREFIX = "cp3.authentication";

    /**
     * 过期时间 2h
     * 单位：s
     */
    private Long expire = 7200L;
    /**
     * 刷新token的过期时间 8h
     * 单位：s
     */
    private Long refreshExpire = 28800L;
    /**
     * 设置解析token时允许的是时间差
     * 单位：s
     * 使用场景1：多台服务器集群部署时，服务器时间戳可能不一致
     * 使用场景2：？
     */
    private Long allowedClockSkewSeconds = 0L;

}
