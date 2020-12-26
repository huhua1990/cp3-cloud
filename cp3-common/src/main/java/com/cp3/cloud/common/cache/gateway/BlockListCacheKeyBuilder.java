package com.cp3.cloud.common.cache.gateway;

import com.cp3.base.cache.model.CacheKeyBuilder;
import com.cp3.cloud.common.cache.CacheKeyDefinition;

/**
 * 阻止列表 KEY
 * <p>
 *
 * @author zuihou
 * @date 2020/9/20 6:45 下午
 */
public class BlockListCacheKeyBuilder implements CacheKeyBuilder {
    @Override
    public String getPrefix() {
        return CacheKeyDefinition.BLOCKLIST;
    }

}
