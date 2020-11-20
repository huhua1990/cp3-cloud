package com.cp3.cloud.http;

import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignLoggerFactory;

/**
 * @author cp3
 * @date 2020/8/9 上午10:03
 */
public class InfoFeignLoggerFactory implements FeignLoggerFactory {
    @Override
    public feign.Logger create(Class<?> type) {
        return new InfoSlf4jFeignLogger(LoggerFactory.getLogger(type));
    }
}
