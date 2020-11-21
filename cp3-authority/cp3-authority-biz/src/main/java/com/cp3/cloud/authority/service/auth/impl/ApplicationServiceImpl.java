package com.cp3.cloud.authority.service.auth.impl;

import cn.hutool.core.convert.Convert;
import com.cp3.cloud.authority.dao.auth.ApplicationMapper;
import com.cp3.cloud.authority.entity.auth.Application;
import com.cp3.cloud.authority.service.auth.ApplicationService;
import com.cp3.cloud.base.service.SuperCacheServiceImpl;
import com.cp3.cloud.database.mybatis.conditions.Wraps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.function.Function;

import static com.cp3.cloud.common.constant.CacheKey.APPLICATION;
import static com.cp3.cloud.common.constant.CacheKey.APPLICATION_CLIENT;
import static com.cp3.cloud.common.constant.CacheKey.buildTenantKey;

/**
 * <p>
 * 业务实现类
 * 应用
 * </p>
 *
 * @author cp3
 * @date 2019-12-15
 */
@Slf4j
@Service
public class ApplicationServiceImpl extends SuperCacheServiceImpl<ApplicationMapper, Application> implements ApplicationService {

    @Override
    protected String getRegion() {
        return APPLICATION;
    }

    @Override
    public Application getByClient(String clientId, String clientSecret) {
        String key = buildTenantKey(clientId, clientSecret);
        Function<String, Object> loader = (k) -> super.getObj(Wraps.<Application>lbQ()
                .select(Application::getId).eq(Application::getClientId, clientId).eq(Application::getClientSecret, clientSecret), Convert::toLong);
        return getByKey(APPLICATION_CLIENT, key, loader);
    }

}
