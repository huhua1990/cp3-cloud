package com.cp3.cloud.http;

import org.slf4j.Logger;

/**
 * @author cp3
 * @date 2020/8/9 上午10:01
 */
public class InfoSlf4jFeignLogger extends feign.Logger {
    private final Logger logger;

    public InfoSlf4jFeignLogger(Logger logger) {
        this.logger = logger;
    }

    @Override
    protected void log(String configKey, String format, Object... args) {
        if (logger.isInfoEnabled()) {
            logger.info(String.format(methodTag(configKey) + format, args));
        }
    }

}
