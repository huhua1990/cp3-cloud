package com.cp3.cloud.oauth.granter;

import com.cp3.base.basic.R;
import com.cp3.base.database.properties.DatabaseProperties;
import com.cp3.base.exception.BizException;
import com.cp3.base.jwt.TokenUtil;
import com.cp3.base.jwt.model.AuthInfo;
import com.cp3.base.utils.SpringUtils;
import com.cp3.cloud.authority.dto.auth.LoginParamDTO;
import com.cp3.cloud.authority.service.auth.ApplicationService;
import com.cp3.cloud.authority.service.auth.OnlineService;
import com.cp3.cloud.authority.service.auth.UserService;
import com.cp3.cloud.oauth.event.LoginEvent;
import com.cp3.cloud.oauth.event.model.LoginStatusDTO;
import com.cp3.cloud.oauth.service.ValidateCodeService;
import com.cp3.cloud.tenant.service.TenantService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.cp3.cloud.oauth.granter.CaptchaTokenGranter.GRANT_TYPE;

/**
 * 验证码TokenGranter
 *
 * @author zuihou
 */
@Component(GRANT_TYPE)
@Slf4j
public class CaptchaTokenGranter extends AbstractTokenGranter implements TokenGranter {

    public static final String GRANT_TYPE = "captcha";
    private final ValidateCodeService validateCodeService;

    public CaptchaTokenGranter(TokenUtil tokenUtil, UserService userService,
                               TenantService tenantService, ApplicationService applicationService,
                               DatabaseProperties databaseProperties, ValidateCodeService validateCodeService,
                               OnlineService onlineService) {
        super(tokenUtil, userService, tenantService, applicationService, databaseProperties, onlineService);
        this.validateCodeService = validateCodeService;
    }

    @Override
    public R<AuthInfo> grant(LoginParamDTO loginParam) {
        R<Boolean> check = validateCodeService.check(loginParam.getKey(), loginParam.getCode());
        if (!check.getIsSuccess()) {
            String msg = check.getMsg();
            SpringUtils.publishEvent(new LoginEvent(LoginStatusDTO.fail(loginParam.getAccount(), msg)));
            throw BizException.validFail(check.getMsg());
        }

        return login(loginParam);
    }

}
