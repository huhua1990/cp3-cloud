package com.cp3.cloud.activiti.service.impl;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baidu.fsg.uid.UidGenerator;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.cp3.base.exception.BizException;
import com.cp3.base.utils.StrPool;
import com.cp3.cloud.activiti.dao.LeaveMapper;
import com.cp3.cloud.activiti.entity.Approval;
import com.cp3.cloud.activiti.entity.Leave;
import com.cp3.cloud.activiti.service.ApprovalService;
import com.cp3.cloud.activiti.service.FlowService;
import com.cp3.cloud.activiti.service.LeaveService;
import com.cp3.base.basic.service.SuperServiceImpl;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
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
    FlowService flowService;
    @Autowired
    ApprovalService approvalService;
    @Autowired
    RuntimeService runtimeService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void toApproval(String id) throws BizException {
        Leave leave = this.baseMapper.selectById(id);
        String key = Leave.class.getSimpleName();
        String businessKey = key + StrPool.DOT + leave.getId();
        Map<String, Object> vars = new HashMap<>();
        vars.put("objId", leave.getId());//业务id
        vars.put("classType", key);//业务类型
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(key, businessKey, vars);

        log.info("提交请假审批单成功。businessKey:{},procInstId:{}", businessKey, processInstance.getProcessInstanceId());

        //提交不产生审批单
        /*Approval approval = Approval.builder()
                .businessId(leave.getId())
                .businessType(key)
                .procInstId(processInstance.getProcessInstanceId())
                .approvalContext("提交审批")
                .build();
        approvalService.save(approval);*/
    }
}
