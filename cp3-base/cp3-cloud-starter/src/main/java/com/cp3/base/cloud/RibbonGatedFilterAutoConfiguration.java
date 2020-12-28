package com.cp3.base.cloud;


import com.cp3.base.cloud.ribbon.GrayRule;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.netflix.ribbon.RibbonClientConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

/**
 * @Description 配置ribbon 相关
 * @Auther: cp3
 * @Date: 2020/12/28
 */
@AutoConfigureBefore(RibbonClientConfiguration.class)
public class RibbonGatedFilterAutoConfiguration {

    /**
     * 灰度发布 规则
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public GrayRule metadataAwareRule() {
        return new GrayRule();
    }
}
