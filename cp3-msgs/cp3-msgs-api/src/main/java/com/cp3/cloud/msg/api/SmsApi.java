package com.cp3.cloud.msg.api;


import com.cp3.base.basic.R;
import com.cp3.base.basic.entity.SuperEntity;
import com.cp3.cloud.msg.api.fallback.SmsApiFallback;
import com.cp3.cloud.sms.dto.SmsSendTaskDTO;
import com.cp3.cloud.sms.dto.VerificationCodeDTO;
import com.cp3.cloud.sms.entity.SmsTask;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 文件接口
 *
 * @author zuihou
 * @date 2019/06/21
 */
@FeignClient(name = "${lamp.feign.msg-server:lamp-msg-server}", fallback = SmsApiFallback.class)
public interface SmsApi {
    /**
     * 短信发送
     *
     * @param smsTaskDTO 短信参数
     * @return 短信任务
     */
    @RequestMapping(value = "/smsTask/send", method = RequestMethod.POST)
    R<SmsTask> send(@RequestBody SmsSendTaskDTO smsTaskDTO);

    /**
     * 发送验证码
     *
     * @param data 验证码参数
     * @return 是否执行发送
     */
    @PostMapping(value = "/verification/send")
    R<Boolean> sendCode(@Validated @RequestBody VerificationCodeDTO data);

    /**
     * 验证
     *
     * @param data 验证码参数
     * @return 是否验证成功
     */
    @PostMapping("/verification")
    R<Boolean> verification(@Validated(SuperEntity.Update.class) @RequestBody VerificationCodeDTO data);
}
