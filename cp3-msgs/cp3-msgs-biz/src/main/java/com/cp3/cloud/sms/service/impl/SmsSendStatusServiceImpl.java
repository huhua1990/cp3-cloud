package com.cp3.cloud.sms.service.impl;


import com.cp3.cloud.base.service.SuperServiceImpl;
import com.cp3.cloud.sms.dao.SmsSendStatusMapper;
import com.cp3.cloud.sms.entity.SmsSendStatus;
import com.cp3.cloud.sms.service.SmsSendStatusService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 业务实现类
 * 短信发送状态
 * </p>
 *
 * @author cp3
 * @date 2019-08-01
 */
@Slf4j
@Service

public class SmsSendStatusServiceImpl extends SuperServiceImpl<SmsSendStatusMapper, SmsSendStatus> implements SmsSendStatusService {

}
