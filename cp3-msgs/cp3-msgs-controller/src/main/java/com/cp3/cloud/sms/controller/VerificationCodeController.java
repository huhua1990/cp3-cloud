package com.cp3.cloud.sms.controller;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSONObject;
import com.cp3.cloud.base.R;
import com.cp3.cloud.base.entity.SuperEntity;
import com.cp3.cloud.cache.model.CacheKey;
import com.cp3.cloud.cache.repository.CacheOps;
import com.cp3.cloud.common.cache.VerificationCodeCacheKeyBuilder;
import com.cp3.cloud.sms.dto.VerificationCodeDTO;
import com.cp3.cloud.sms.entity.SmsTask;
import com.cp3.cloud.sms.enumeration.SourceType;
import com.cp3.cloud.sms.enumeration.TemplateCodeType;
import com.cp3.cloud.sms.service.SmsTaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 通用验证码
 *
 * @author cp3
 * @date 2019/08/06
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/verification")
@Api(value = "VerificationCode", tags = "验证码")
public class VerificationCodeController {

    @Autowired
    private CacheOps cacheOps;
    @Autowired
    private SmsTaskService smsTaskService;

    /**
     * 通用的发送验证码功能
     * <p>
     * 一个系统可能有很多地方需要发送验证码（注册、找回密码等），每增加一个场景，VerificationCodeType 就需要增加一个枚举值
     *
     * @param data
     * @return
     */
    @ApiOperation(value = "发送验证码", notes = "发送验证码")
    @PostMapping(value = "/send")
    public R<Boolean> send(@Validated @RequestBody VerificationCodeDTO data) {
        String code = RandomUtil.randomNumbers(6);

        SmsTask smsTask = SmsTask.builder().build();
        smsTask.setSourceType(SourceType.SERVICE);
        JSONObject param = new JSONObject();
        param.put("1", code);
        smsTask.setTemplateParams(param.toString());
        smsTask.setReceiver(data.getMobile());
        smsTask.setDraft(false);
        smsTaskService.saveTask(smsTask, TemplateCodeType.COMMON_SMS);

        CacheKey cacheKey = new VerificationCodeCacheKeyBuilder().key(data.getType().name(), data.getMobile());
        cacheOps.set(cacheKey, code);
        return R.success();
    }

    /**
     * 对某种类型的验证码进行校验
     *
     * @param data
     * @return
     */
    @ApiOperation(value = "验证验证码", notes = "验证验证码")
    @PostMapping
    public R<Boolean> verification(@Validated(SuperEntity.Update.class) @RequestBody VerificationCodeDTO data) {
        CacheKey cacheKey = new VerificationCodeCacheKeyBuilder().key(data.getType().name(), data.getMobile());
        String code = cacheOps.get(cacheKey);
        return R.success(data.getCode().equals(code));
    }
}
