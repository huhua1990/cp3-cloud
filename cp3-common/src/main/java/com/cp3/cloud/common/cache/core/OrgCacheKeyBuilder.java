package com.cp3.cloud.common.cache.core;

import com.cp3.base.cache.model.CacheKeyBuilder;
import com.cp3.cloud.common.cache.CacheKeyDefinition;

import java.time.Duration;

/**
 * 部门 KEY
 * <p>
 * #c_org
 *
 * @author zuihou
 * @date 2020/9/20 6:45 下午
 */
public class OrgCacheKeyBuilder implements CacheKeyBuilder {
    @Override
    public String getPrefix() {
        return CacheKeyDefinition.ORG;
    }

    @Override
    public Duration getExpire() {
        return Duration.ofHours(24);
    }
}
