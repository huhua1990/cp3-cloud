package com.cp3.cloud.oauth.api.hystrix;

import com.cp3.cloud.oauth.api.OrgApi;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * 熔断类
 *
 * @author cp3
 * @date 2020年02月09日11:24:23
 */
@Component
public class OrgApiFallback implements OrgApi {
    @Override
    public Map<Serializable, Object> findOrgByIds(Set<Serializable> ids) {
        return Collections.emptyMap();
    }

    @Override
    public Map<Serializable, Object> findOrgNameByIds(Set<Serializable> ids) {
        return Collections.emptyMap();
    }
}
