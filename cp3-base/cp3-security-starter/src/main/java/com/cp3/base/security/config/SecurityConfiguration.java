package com.cp3.base.security.config;

import com.cp3.base.security.aspect.UriSecurityPreAuthAspect;
import com.cp3.base.security.aspect.VerifyAuthFunction;
import com.cp3.base.security.feign.UserResolverService;
import com.cp3.base.security.properties.SecurityProperties;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;

/**
 * 权限认证配置类
 *
 * @author zuihou
 * @date 2020年03月29日22:34:45
 */
@Order
@AllArgsConstructor
@EnableConfigurationProperties({SecurityProperties.class})
public class SecurityConfiguration {
    private final SecurityProperties securityProperties;

    @Bean
    @ConditionalOnProperty(prefix = SecurityProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
    public UriSecurityPreAuthAspect uriSecurityPreAuthAspect(VerifyAuthFunction verifyAuthFunction) {
        return new UriSecurityPreAuthAspect(verifyAuthFunction);
    }

    @Bean("fun")
    @ConditionalOnMissingBean(VerifyAuthFunction.class)
    public VerifyAuthFunction getVerifyAuthFunction(UserResolverService userResolverService) {
        return new VerifyAuthFunction(userResolverService, securityProperties);
    }

}
