package com.cp3.cloud.sms.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.cp3.cloud.base.service.SuperServiceImpl;
import com.cp3.cloud.exception.BizException;
import com.cp3.cloud.sms.dao.SmsTemplateMapper;
import com.cp3.cloud.sms.entity.SmsTemplate;
import com.cp3.cloud.sms.service.SmsTemplateService;
import com.cp3.cloud.utils.CodeGenerate;
import com.cp3.cloud.utils.StrHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.cp3.cloud.exception.code.ExceptionCode.BASE_VALID_PARAM;

/**
 * <p>
 * 业务实现类
 * 短信模板
 * </p>
 *
 * @author cp3
 * @date 2019-08-01
 */
@Slf4j
@Service

public class SmsTemplateServiceImpl extends SuperServiceImpl<SmsTemplateMapper, SmsTemplate> implements SmsTemplateService {

    @Autowired
    private CodeGenerate codeGenerate;

    private static String getParamByContent(String content, String regEx) {
        //编译正则表达式
        Pattern pattern = Pattern.compile(regEx);
        //忽略大小写的写法:
        Matcher matcher = pattern.matcher(content);

        // 查找字符串中是否有匹配正则表达式的字符/字符串//有序， 目的是为了兼容 腾讯云参数
        JSONObject obj = new JSONObject(true);
        while (matcher.find()) {
            String key = matcher.group(1);
            obj.put(key, "");
        }
        if (obj.isEmpty()) {
            throw BizException.wrap("模板内容解析失败，请认真详细内容格式");
        }

        return obj.toString();
    }

    private void buildParams(SmsTemplate smsTemplate) {
        String content = smsTemplate.getContent();
        if (StrUtil.isNotEmpty(content)) {
            String param = getParamByContent(content, smsTemplate.getProviderType().getRegex());
            smsTemplate.setTemplateParams(param);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveTemplate(SmsTemplate smsTemplate) {
        buildParams(smsTemplate);
        int count = super.count(Wrappers.<SmsTemplate>lambdaQuery().eq(SmsTemplate::getCustomCode, smsTemplate.getCustomCode()));
        if (count > 0) {
            throw BizException.wrap(BASE_VALID_PARAM.build("自定义编码重复"));
        }
        smsTemplate.setCustomCode(StrHelper.getOrDef(smsTemplate.getCustomCode(), codeGenerate.next()));
        super.save(smsTemplate);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateTemplate(SmsTemplate smsTemplate) {
        buildParams(smsTemplate);
        super.updateById(smsTemplate);
    }
}
