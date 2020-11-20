package com.cp3.cloud.authority.service.auth;

import com.cp3.cloud.authority.entity.auth.UserToken;
import com.cp3.cloud.base.service.SuperService;

/**
 * <p>
 * 业务接口
 * token
 * </p>
 *
 * @author cp3
 * @date 2020-04-02
 */
public interface UserTokenService extends SuperService<UserToken> {
    /**
     * 重置用户登录
     *
     * @return
     */
    boolean reset();
}
