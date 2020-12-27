package com.cp3.cloud.oauth.config;

import com.cp3.base.boot.config.BaseConfig;
import com.cp3.base.log.event.SysLogListener;
import com.cp3.cloud.authority.service.common.OptLogService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zuihou
 * @date 2017-12-15 14:42
 */
@Configuration
public class OauthWebConfiguration extends BaseConfig {

    /**
     * cp3.log.enabled = true 并且 cp3.log.type=DB时实例该类
     */
    @Bean
    @ConditionalOnExpression("${cp3.log.enabled:true} && 'DB'.equals('${cp3.log.type:LOGGER}')")
    public SysLogListener sysLogListener(OptLogService optLogService) {
        return new SysLogListener(optLogService::save);
    }
}
