package com.cp3.cloud.sms.strategy;


import com.cp3.cloud.base.R;
import com.cp3.cloud.sms.entity.SmsTask;
import com.cp3.cloud.sms.entity.SmsTemplate;

/**
 * 抽象策略类: 发送短信
 * <p>
 * 每个短信 服务商都有自己的 发送短信策略(sdk)
 *
 * @author cp3
 * @date 2019-05-15
 */
public interface SmsStrategy {
    /**
     * 发送短信
     *
     * @param task
     * @param template
     * @return
     */
    R<String> sendSms(SmsTask task, SmsTemplate template);
}
