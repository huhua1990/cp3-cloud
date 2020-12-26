/*
 * Copyright 2002-2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cp3.cloud.oauth.granter;

import com.cp3.base.basic.R;
import com.cp3.base.database.properties.DatabaseProperties;
import com.cp3.base.jwt.TokenUtil;
import com.cp3.base.jwt.model.AuthInfo;
import com.cp3.cloud.authority.dto.auth.LoginParamDTO;
import com.cp3.cloud.authority.service.auth.ApplicationService;
import com.cp3.cloud.authority.service.auth.OnlineService;
import com.cp3.cloud.authority.service.auth.UserService;
import com.cp3.cloud.tenant.service.TenantService;
import org.springframework.stereotype.Component;

import static com.cp3.cloud.oauth.granter.PasswordTokenGranter.GRANT_TYPE;

/**
 * 账号密码登录获取token
 *
 * @author Dave Syer
 * @author zuihou
 * @date 2020年03月31日10:22:55
 */
@Component(GRANT_TYPE)
public class PasswordTokenGranter extends AbstractTokenGranter implements TokenGranter {

    public static final String GRANT_TYPE = "password";

    public PasswordTokenGranter(TokenUtil tokenUtil, UserService userService, TenantService tenantService,
                                ApplicationService applicationService, DatabaseProperties databaseProperties,
                                OnlineService onlineService) {
        super(tokenUtil, userService, tenantService, applicationService, databaseProperties, onlineService);
    }

    @Override
    public R<AuthInfo> grant(LoginParamDTO tokenParameter) {
        return login(tokenParameter);
    }
}
