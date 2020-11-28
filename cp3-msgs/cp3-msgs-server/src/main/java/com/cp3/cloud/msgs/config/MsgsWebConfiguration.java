package com.cp3.cloud.msgs.config;

import com.cp3.cloud.boot.config.BaseConfig;
import com.cp3.cloud.log.event.SysLogListener;
import com.cp3.cloud.oauth.api.LogApi;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author cp3
 * @createTime 2017-12-15 14:42
 */
@Configuration
public class MsgsWebConfiguration extends BaseConfig {
    /**
     * zuihou.log.enabled = true 并且 zuihou.log.type=DB时实例该类
     *
     * @param logApi
     * @return
     */
    @Bean
    @ConditionalOnExpression("${zuihou.log.enabled:true} && 'DB'.equals('${zuihou.log.type:LOGGER}')")
    public SysLogListener sysLogListener(LogApi logApi) {
        return new SysLogListener((log) -> logApi.save(log));
    }
}
