package com.cp3.cloud.activiti.service.impl;


import com.cp3.cloud.activiti.dao.FlowRuleMapper;
import com.cp3.cloud.activiti.entity.FlowRule;
import com.cp3.cloud.activiti.service.FlowRuleService;
import com.cp3.base.basic.service.SuperServiceImpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 业务实现类
 * 流程规则
 * </p>
 *
 * @author cp3
 * @date 2021-01-11
 */
@Slf4j
@Service
public class FlowRuleServiceImpl extends SuperServiceImpl<FlowRuleMapper, FlowRule> implements FlowRuleService {
}
