package com.cp3.cloud.authority.controller.auth;

import com.cp3.cloud.authority.dto.auth.UserTokenPageDTO;
import com.cp3.cloud.authority.dto.auth.UserTokenSaveDTO;
import com.cp3.cloud.authority.dto.auth.UserTokenUpdateDTO;
import com.cp3.cloud.authority.entity.auth.UserToken;
import com.cp3.cloud.authority.service.auth.UserTokenService;
import com.cp3.cloud.base.controller.SuperController;
import com.cp3.cloud.security.annotation.PreAuth;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * <p>
 * 前端控制器
 * token
 * </p>
 *
 * @author cp3
 * @date 2020-04-02
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/userToken")
@Api(value = "UserToken", tags = "token")
@PreAuth(replace = "userToken:")
public class UserTokenController extends SuperController<UserTokenService, Long, UserToken, UserTokenPageDTO, UserTokenSaveDTO, UserTokenUpdateDTO> {


}
