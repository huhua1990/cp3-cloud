package com.cp3.cloud.log;


import com.cp3.cloud.jackson.JsonUtil;
import com.cp3.cloud.log.aspect.SysLogAspect;
import com.cp3.cloud.log.event.SysLogListener;
import com.cp3.cloud.log.monitor.PointUtil;
import com.cp3.cloud.log.properties.OptLogProperties;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 日志自动配置
 * <p>
 * 启动条件：
 * 1，存在web环境
 * 2，配置文件中zuihou.log.enabled=true 或者 配置文件中不存在：zuihou.log.enabled 值
 *
 * @author cp3
 * @date 2019/2/1
 */
@EnableAsync
@Configuration
@AllArgsConstructor
@ConditionalOnWebApplication
@ConditionalOnProperty(prefix = OptLogProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
public class LogAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public SysLogAspect sysLogAspect() {
        return new SysLogAspect();
    }

//    /**
//     * gateway 网关模块需要禁用 spring-webmvc 相关配置，必须通过在类上面加限制条件方式来实现， 不能直接Bean上面加
//     */
//    @ConditionalOnProperty(prefix = "zuihou.webmvc", name = "enabled", havingValue = "true", matchIfMissing = true)
//    public static class WebMvcConfig {
//        @Bean
//        public MdcMvcConfigurer getMdcMvcConfigurer() {
//            return new MdcMvcConfigurer();
//        }
//    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnExpression("${zuihou.log.enabled:true} && 'LOGGER'.equals('${zuihou.log.type:LOGGER}')")
    public SysLogListener sysLogListener() {
        return new SysLogListener((log) -> {
            PointUtil.debug("0", "OPT_LOG", JsonUtil.toJson(log));
        });
    }
}
