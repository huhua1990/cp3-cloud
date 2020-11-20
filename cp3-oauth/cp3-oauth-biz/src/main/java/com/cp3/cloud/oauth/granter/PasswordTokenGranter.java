package com.cp3.cloud.oauth.granter;

import com.cp3.cloud.authority.dto.auth.LoginParamDTO;
import com.cp3.cloud.base.R;
import com.cp3.cloud.jwt.model.AuthInfo;
import org.springframework.stereotype.Component;

import static com.cp3.cloud.oauth.granter.PasswordTokenGranter.GRANT_TYPE;

/**
 * 账号密码登录获取token
 *
 * @author cp3
 * @date 2020年03月31日10:22:55
 */
@Component(GRANT_TYPE)
public class PasswordTokenGranter extends AbstractTokenGranter implements TokenGranter {

    public static final String GRANT_TYPE = "password";

    @Override
    public R<AuthInfo> grant(LoginParamDTO tokenParameter) {
        return login(tokenParameter);
    }
}
