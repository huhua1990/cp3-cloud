package com.cp3.cloud.authority.service.auth.impl;

import cn.hutool.core.convert.Convert;

import com.cp3.base.basic.service.SuperCacheServiceImpl;
import com.cp3.base.cache.model.CacheKey;
import com.cp3.base.cache.model.CacheKeyBuilder;
import com.cp3.base.database.mybatis.conditions.Wraps;
import com.cp3.base.database.mybatis.conditions.query.LbqWrapper;
import com.cp3.cloud.authority.dao.auth.ApplicationMapper;
import com.cp3.cloud.authority.entity.auth.Application;
import com.cp3.cloud.authority.service.auth.ApplicationService;
import com.cp3.cloud.common.cache.auth.ApplicationCacheKeyBuilder;
import com.cp3.cloud.common.cache.auth.ApplicationClientCacheKeyBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.function.Function;

/**
 * <p>
 * 业务实现类
 * 应用
 * </p>
 *
 * @author zuihou
 * @date 2019-12-15
 */
@Slf4j
@Service

public class ApplicationServiceImpl extends SuperCacheServiceImpl<ApplicationMapper, Application> implements ApplicationService {

    @Override
    protected CacheKeyBuilder cacheKeyBuilder() {
        return new ApplicationCacheKeyBuilder();
    }

    @Override
    public Application getByClient(String clientId, String clientSecret) {
        LbqWrapper<Application> wrapper = Wraps.<Application>lbQ()
                .select(Application::getId).eq(Application::getClientId, clientId).eq(Application::getClientSecret, clientSecret);
        Function<CacheKey, Object> loader = k -> super.getObj(wrapper, Convert::toLong);
        CacheKey cacheKey = new ApplicationClientCacheKeyBuilder().key(clientId, clientSecret);
        return getByKey(cacheKey, loader);
    }

}
