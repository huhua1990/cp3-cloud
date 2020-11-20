package com.cp3.cloud.authority.service.auth;

import com.cp3.cloud.authority.entity.auth.Application;
import com.cp3.cloud.base.service.SuperCacheService;

/**
 * <p>
 * 业务接口
 * 应用
 * </p>
 *
 * @author cp3
 * @date 2019-12-15
 */
public interface ApplicationService extends SuperCacheService<Application> {
    /**
     * 根据 clientid 和 clientSecret 查询
     *
     * @param clientId
     * @param clientSecret
     * @return
     */
    Application getByClient(String clientId, String clientSecret);
}
