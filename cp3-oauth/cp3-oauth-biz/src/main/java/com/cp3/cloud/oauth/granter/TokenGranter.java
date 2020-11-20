package com.cp3.cloud.oauth.granter;


import com.cp3.cloud.authority.dto.auth.LoginParamDTO;
import com.cp3.cloud.base.R;
import com.cp3.cloud.jwt.model.AuthInfo;

/**
 * 授权认证统一接口.
 *
 * @author cp3
 * @date 2020年03月31日10:21:21
 */
public interface TokenGranter {

    /**
     * 获取用户信息
     *
     * @param loginParam 授权参数
     * @return LoginDTO
     */
    R<AuthInfo> grant(LoginParamDTO loginParam);

}
