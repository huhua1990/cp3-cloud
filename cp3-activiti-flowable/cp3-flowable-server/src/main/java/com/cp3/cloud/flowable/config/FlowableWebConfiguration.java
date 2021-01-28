package com.cp3.cloud.flowable.config;

import com.cp3.cloud.oauth.api.LogApi;
import com.cp3.base.boot.config.BaseConfig;
import com.cp3.base.log.event.SysLogListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * xxx-Web配置
 *
 * @author cp3
 * @date 2021-01-25
 */
@Configuration
public class FlowableWebConfiguration extends BaseConfig {

    /**
     * cp3.log.enabled = true 并且 cp3.log.type=DB时实例该类
     */
    @Bean
    @ConditionalOnExpression("${cp3.log.enabled:true} && 'DB'.equals('${cp3.log.type:LOGGER}')")
    public SysLogListener sysLogListener(LogApi logApi) {
        return new SysLogListener(logApi::save);
    }
}
