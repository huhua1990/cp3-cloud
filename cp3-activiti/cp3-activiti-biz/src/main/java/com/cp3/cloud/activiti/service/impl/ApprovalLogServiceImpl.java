package com.cp3.cloud.activiti.service.impl;


import com.cp3.cloud.activiti.dao.ApprovalLogMapper;
import com.cp3.cloud.activiti.entity.ApprovalLog;
import com.cp3.cloud.activiti.service.ApprovalLogService;
import com.cp3.base.basic.service.SuperServiceImpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 业务实现类
 * 审批日志
 * </p>
 *
 * @author cp3
 * @date 2021-01-11
 */
@Slf4j
@Service
public class ApprovalLogServiceImpl extends SuperServiceImpl<ApprovalLogMapper, ApprovalLog> implements ApprovalLogService {
}
