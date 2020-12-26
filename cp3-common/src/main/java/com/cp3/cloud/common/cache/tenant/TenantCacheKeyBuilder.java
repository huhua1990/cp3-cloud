package com.cp3.cloud.common.cache.tenant;

import com.cp3.base.cache.model.CacheKeyBuilder;
import com.cp3.base.utils.StrPool;
import com.cp3.cloud.common.cache.CacheKeyDefinition;

import java.time.Duration;

/**
 * 系统用户 KEY
 * <p>
 * #d_tenant
 *
 * @author zuihou
 * @date 2020/9/20 6:45 下午
 */
public class TenantCacheKeyBuilder implements CacheKeyBuilder {
    @Override
    public String getTenant() {
        return StrPool.EMPTY;
    }

    @Override
    public String getPrefix() {
        return CacheKeyDefinition.TENANT;
    }

    @Override
    public Duration getExpire() {
        return Duration.ofHours(24);
    }
}
