package com.cp3.cloud.oauth.controller;


import com.cp3.cloud.authority.service.common.ParameterService;
import com.cp3.cloud.base.R;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * 参数配置
 * </p>
 *
 * @author cp3
 * @date 2020-02-05
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/parameter")
@Api(value = "Parameter", tags = "参数配置")
public class OauthParameterController {
    @Autowired
    private ParameterService parameterService;


    @GetMapping("/value")
    public R<String> getValue(@RequestParam(value = "key") String key, @RequestParam(value = "defVal") String defVal) {
        return R.success(parameterService.getValue(key, defVal));
    }

}
