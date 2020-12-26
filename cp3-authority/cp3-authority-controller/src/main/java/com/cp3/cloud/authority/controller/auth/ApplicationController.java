package com.cp3.cloud.authority.controller.auth;


import cn.hutool.core.util.RandomUtil;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.cp3.base.annotation.security.PreAuth;
import com.cp3.base.basic.R;
import com.cp3.base.basic.controller.SuperCacheController;
import com.cp3.cloud.authority.dto.auth.ApplicationPageQuery;
import com.cp3.cloud.authority.dto.auth.ApplicationSaveDTO;
import com.cp3.cloud.authority.dto.auth.ApplicationUpdateDTO;
import com.cp3.cloud.authority.entity.auth.Application;
import com.cp3.cloud.authority.service.auth.ApplicationService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * 应用
 * </p>
 *
 * @author zuihou
 * @date 2019-12-15
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/application")
@Api(value = "Application", tags = "应用")
@ApiSupport(author = "zuihou")
@PreAuth(replace = "authority:application:")
public class ApplicationController extends SuperCacheController<ApplicationService, Long, Application, ApplicationPageQuery, ApplicationSaveDTO, ApplicationUpdateDTO> {

    @Override
    public R<Application> handlerSave(ApplicationSaveDTO applicationSaveDTO) {
        applicationSaveDTO.setClientId(RandomUtil.randomString(24));
        applicationSaveDTO.setClientSecret(RandomUtil.randomString(32));
        return super.handlerSave(applicationSaveDTO);
    }

}
