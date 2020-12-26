package com.cp3.cloud.msg.api.fallback;

import com.cp3.base.basic.R;
import com.cp3.cloud.msg.api.SmsApi;
import com.cp3.cloud.sms.dto.SmsSendTaskDTO;
import com.cp3.cloud.sms.dto.VerificationCodeDTO;
import com.cp3.cloud.sms.entity.SmsTask;
import org.springframework.stereotype.Component;

/**
 * 熔断
 *
 * @author zuihou
 * @date 2019/07/25
 */
@Component
public class SmsApiFallback implements SmsApi {
    @Override
    public R<SmsTask> send(SmsSendTaskDTO smsTaskDTO) {
        return R.timeout();
    }

    @Override
    public R<Boolean> sendCode(VerificationCodeDTO data) {
        return R.timeout();
    }

    @Override
    public R<Boolean> verification(VerificationCodeDTO data) {
        return R.timeout();
    }
}
