package com.cp3.cloud.flowable.config.datasource;

import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.cp3.cloud.oauth.api.UserApi;
import com.cp3.base.database.datasource.BaseMybatisConfiguration;
import com.cp3.base.database.mybatis.auth.DataScopeInnerInterceptor;
import com.cp3.base.database.properties.DatabaseProperties;
import com.cp3.base.utils.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * 配置一些 Mybatis 常用重用拦截器
 * xxx
 *
 * @author cp3
 * @date 2021-01-25
 */
@Configuration
@Slf4j
@EnableConfigurationProperties({DatabaseProperties.class})
public class FlowableMybatisAutoConfiguration extends BaseMybatisConfiguration {

    public FlowableMybatisAutoConfiguration(DatabaseProperties databaseProperties) {
        super(databaseProperties);
    }


    /**
     * 数据权限插件
     * @return 数据权限插件
     */
    @Override
    protected List<InnerInterceptor> getPaginationBeforeInnerInterceptor() {
        List<InnerInterceptor> list = new ArrayList<>();
        Boolean isDataScope = databaseProperties.getIsDataScope();
        if (isDataScope) {
            list.add(new DataScopeInnerInterceptor(userId -> SpringUtils.getBean(UserApi.class).getDataScopeById(userId)));
        }
        return list;
    }

}
