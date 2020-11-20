package com.cp3.cloud.injection;

import com.cp3.cloud.injection.aspect.InjectionResultAspect;
import com.cp3.cloud.injection.core.InjectionCore;
import com.cp3.cloud.injection.mybatis.typehandler.RemoteDataTypeHandler;
import com.cp3.cloud.injection.properties.InjectionProperties;
import com.cp3.cloud.utils.SpringUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 关联字段数据注入工具 自动配置类
 *
 * @author cp3
 * @date 2019/09/20
 */
@Slf4j
@Configuration
@AllArgsConstructor
@EnableConfigurationProperties(InjectionProperties.class)
@ConditionalOnProperty(prefix = InjectionProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
public class InjectionDataAutoConfiguration {
    private InjectionProperties remoteProperties;

    /**
     * Spring 工具类
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public SpringUtils beanFactoryUtils() {
        return new SpringUtils();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = InjectionProperties.PREFIX, name = "aop-enabled", havingValue = "true", matchIfMissing = true)
    public InjectionResultAspect getRemoteAspect(InjectionCore injectionCore) {
        return new InjectionResultAspect(injectionCore);
    }

    @Bean
    @ConditionalOnMissingBean
    public InjectionCore getInjectionCore() {
        return new InjectionCore(remoteProperties);
    }

    /**
     * Mybatis 类型处理器： 处理 RemoteData 类型的字段
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public RemoteDataTypeHandler getRemoteDataTypeHandler() {
        return new RemoteDataTypeHandler();
    }
}

