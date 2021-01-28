package com.cp3.cloud.flowable.service.impl;


import com.cp3.cloud.flowable.dao.ApprovalMapper;
import com.cp3.cloud.flowable.entity.Approval;
import com.cp3.cloud.flowable.service.ApprovalService;
import com.cp3.base.basic.service.SuperServiceImpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 业务实现类
 * 审批
 * </p>
 *
 * @author cp3
 * @date 2021-01-25
 */
@Slf4j
@Service
public class ApprovalServiceImpl extends SuperServiceImpl<ApprovalMapper, Approval> implements ApprovalService {
}
