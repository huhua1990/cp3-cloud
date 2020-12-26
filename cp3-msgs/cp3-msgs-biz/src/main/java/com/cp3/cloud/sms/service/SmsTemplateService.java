package com.cp3.cloud.sms.service;

import com.cp3.base.basic.service.SuperService;
import com.cp3.cloud.sms.entity.SmsTemplate;

/**
 * <p>
 * 业务接口
 * 短信模板
 * </p>
 *
 * @author zuihou
 * @date 2019-08-01
 */
public interface SmsTemplateService extends SuperService<SmsTemplate> {
    /**
     * 保存模板，并且将模板内容解析成json格式
     *
     * @param smsTemplate 短信模版
     */
    void saveTemplate(SmsTemplate smsTemplate);

    /**
     * 修改
     *
     * @param smsTemplate 短信模版
     */
    void updateTemplate(SmsTemplate smsTemplate);
}
