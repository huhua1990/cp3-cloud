package com.cp3.cloud.config.datasource;


import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusProperties;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusPropertiesCustomizer;
import com.cp3.base.database.datasource.defaults.BaseMasterDatabaseConfiguration;
import com.cp3.base.database.properties.DatabaseProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.scripting.LanguageDriver;
import org.apache.ibatis.type.TypeHandler;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.cp3.cloud.common.constant.BizConstant.BUSINESS_PACKAGE;
import static com.cp3.cloud.common.constant.BizConstant.UTIL_PACKAGE;

/**
 * @author cp3
 * @createTime 2020-12-9
 */
@Configuration
@Slf4j
@MapperScan(
        basePackages = {
                UTIL_PACKAGE,
                BUSINESS_PACKAGE,
                "com.xxl.job.admin.dao",
        },
        annotationClass = Repository.class,
        sqlSessionFactoryRef = BaseMasterDatabaseConfiguration.DATABASE_PREFIX + "SqlSessionFactory")
@EnableConfigurationProperties({MybatisPlusProperties.class})
@ConditionalOnExpression("!'DATASOURCE'.equals('${cp3.database.multiTenantType}')")
public class JobsDatabaseAutoConfiguration extends BaseMasterDatabaseConfiguration {

    public JobsDatabaseAutoConfiguration(MybatisPlusProperties properties,
                                           DatabaseProperties databaseProperties,
                                           ObjectProvider<Interceptor[]> interceptorsProvider,
                                           ObjectProvider<TypeHandler[]> typeHandlersProvider,
                                           ObjectProvider<LanguageDriver[]> languageDriversProvider,
                                           ResourceLoader resourceLoader,
                                           ObjectProvider<DatabaseIdProvider> databaseIdProvider,
                                           ObjectProvider<List<ConfigurationCustomizer>> configurationCustomizersProvider,
                                           ObjectProvider<List<MybatisPlusPropertiesCustomizer>> mybatisPlusPropertiesCustomizerProvider,
                                           ApplicationContext applicationContext) {
        super(properties, databaseProperties, interceptorsProvider, typeHandlersProvider,
                languageDriversProvider, resourceLoader, databaseIdProvider,
                configurationCustomizersProvider, mybatisPlusPropertiesCustomizerProvider, applicationContext);
        log.debug("检测到 cp3.database.multiTenantType!=DATASOURCE，加载了 JobsDatabaseAutoConfiguration");
    }

}
