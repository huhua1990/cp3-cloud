package com.cp3.cloud.security.annotation;

import com.cp3.cloud.security.config.LoginArgResolverConfig;
import com.cp3.cloud.security.config.UserResolveFeignConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 在启动类上添加该注解来----开启自动登录用户对象注入
 * Token转化SysUser
 *
 * @author cp3
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({UserResolveFeignConfiguration.class, LoginArgResolverConfig.class})
public @interface EnableLoginArgResolver {
}
