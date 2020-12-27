package com.cp3.cloud.config.datasource;

import com.cp3.base.database.datasource.BaseMybatisConfiguration;
import com.cp3.base.database.properties.DatabaseProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;


/**
 * 配置一些拦截器
 *
 * @author cp3
 * @createTime 2020-12-9
 */
@Configuration
@Slf4j
@EnableConfigurationProperties({DatabaseProperties.class})
public class JobsMybatisAutoConfiguration extends BaseMybatisConfiguration {


    public JobsMybatisAutoConfiguration(DatabaseProperties databaseProperties) {
        super(databaseProperties);
    }
}


