package com.cp3.cloud.common.cache.auth;

import com.cp3.base.cache.model.CacheKeyBuilder;
import com.cp3.cloud.common.cache.CacheKeyDefinition;

import java.time.Duration;

/**
 * 用户资源 KEY
 * <p>
 * #c_role_authority & #c_user_role
 *
 * @author zuihou
 * @date 2020/9/20 6:45 下午
 */
public class UserResourceCacheKeyBuilder implements CacheKeyBuilder {
    @Override
    public String getPrefix() {
        return CacheKeyDefinition.USER_RESOURCE;
    }

    @Override
    public Duration getExpire() {
        return Duration.ofHours(12);
    }
}
