package com.cp3.cloud.activiti.service.impl;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baidu.fsg.uid.UidGenerator;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.cp3.base.exception.BizException;
import com.cp3.base.utils.StrPool;
import com.cp3.cloud.activiti.dao.LeaveMapper;
import com.cp3.cloud.activiti.entity.Approval;
import com.cp3.cloud.activiti.entity.Flow;
import com.cp3.cloud.activiti.entity.FlowRule;
import com.cp3.cloud.activiti.entity.Leave;
import com.cp3.cloud.activiti.service.ApprovalService;
import com.cp3.cloud.activiti.service.FlowRuleService;
import com.cp3.cloud.activiti.service.FlowService;
import com.cp3.cloud.activiti.service.LeaveService;
import com.cp3.base.basic.service.SuperServiceImpl;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 业务实现类
 * 假期申请
 * </p>
 *
 * @author cp3
 * @date 2021-01-11
 */
@Slf4j
@Service
public class LeaveServiceImpl extends SuperServiceImpl<LeaveMapper, Leave> implements LeaveService {
    @Autowired
    FlowRuleService flowRuleService;
    @Autowired
    FlowService flowService;
    @Autowired
    ApprovalService approvalService;
    @Autowired
    RuntimeService runtimeService;
    @Resource
    UidGenerator uidGenerator;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void toApproval(String id) throws BizException {
        Leave leave = this.baseMapper.selectById(id);
        Approval approval = Approval.builder()
                .leaveId(leave.getId())
                .leaveState(1)
                .build();
        approvalService.save(approval);

        String key = Leave.class.getSimpleName();
        String businessKey = key + StrPool.DOT + leave.getId();
        Map<String, Object> vars = new HashMap<>();
        vars.put("objId", leave.getId());
        vars.put("classType", key);

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(key, businessKey, vars);
        approval.setFlowId(Long.valueOf(processInstance.getProcessInstanceId()));
        approvalService.saveOrUpdate(approval);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void toApproval2(String id) throws BizException {
        Leave leave = this.baseMapper.selectById(id);
        //匹配规则
        List<FlowRule> flowRuleList = flowRuleService.list(
                Wrappers.<FlowRule>lambdaQuery().eq(FlowRule::getSystemCode, leave.getSystemCode())
                        .eq(FlowRule::getBusiType, leave.getBusiType()));
        //通过规则获取流程model,生成审批单位
        if(CollectionUtil.isEmpty(flowRuleList)){
            log.error("没有配置匹配规则");
            throw new BizException("没有配置匹配规则");
        }

        flowRuleList.stream().forEach(flowRule -> {
            Flow flow = flowService.getById(flowRule.getFlowId());
            log.info("审批单:{},匹配规则:{},匹配到工作流程:{}",leave.getId(), flowRule.getId(),flowRule.getFlowId());
            Approval approval = Approval.builder()
                    .leaveId(leave.getId())
                    .ruleId(flowRule.getFlowId())
                    .leaveState(1)
                    .procInstId(uidGenerator.getUid())//生成流程实例业务id
                    .build();
            approvalService.save(approval);
            //启动流程
            ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(flow.getProcdefCode(), String.valueOf(approval.getProcInstId()), null);
            approval.setFlowId(Long.valueOf(processInstance.getProcessInstanceId()));
            //更新进行实例id
            //TODO 查看approval对象数据是否过来
            approvalService.saveOrUpdate(approval);
        });

    }
}
