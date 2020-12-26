package com.cp3.cloud.common.cache.common;


import com.cp3.base.cache.model.CacheKeyBuilder;
import com.cp3.cloud.common.cache.CacheKeyDefinition;

/**
 * 参数 KEY
 * {tenant}:PARAMETER_KEY:{key} -> value
 * <p>
 * #c_parameter
 *
 * @author zuihou
 * @date 2020/9/20 6:45 下午
 */
public class AreaCacheKeyBuilder implements CacheKeyBuilder {
    @Override
    public String getPrefix() {
        return CacheKeyDefinition.AREA;
    }

}
