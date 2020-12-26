package com.cp3.cloud.common.cache.common;


import com.cp3.base.cache.model.CacheKeyBuilder;
import com.cp3.cloud.common.cache.CacheKeyDefinition;

/**
 * 参数 KEY
 * <p>
 * #c_application
 *
 * @author zuihou
 * @date 2020/9/20 6:45 下午
 */
public class TokenUserIdCacheKeyBuilder implements CacheKeyBuilder {
    @Override
    public String getPrefix() {
        return CacheKeyDefinition.TOKEN_USER_ID;
    }

//    @Override
//    public Duration getExpire() {
//        return Duration.ofMinutes(15);
//    }
}
