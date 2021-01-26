package com.cp3.cloud.activiti.strategy.impl;

import cn.hutool.core.bean.BeanUtil;
import com.cp3.base.basic.R;
import com.cp3.cloud.activiti.dto.ApprovalSaveDTO;
import com.cp3.cloud.activiti.entity.Approval;
import com.cp3.cloud.activiti.service.ApprovalService;
import com.cp3.cloud.activiti.strategy.ApprovalHandlerStrategy;
import javafx.concurrent.Task;
import org.activiti.engine.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * @Description
 * @Auther: huhua
 * @Date: 2021/1/26
 */
@Component
public class AbstractApprovalHandlerStrategy implements ApprovalHandlerStrategy {
    @Autowired
    private TaskService taskService;
    @Autowired
    private ApprovalService approvalService;

    @Override
    @Transactional
    public void approval(ApprovalSaveDTO approvalSaveDTO) {
        R<Map<String, Object>> mapR = this.handlerApproval(approvalSaveDTO.getBusinessId());
        taskService.complete(approvalSaveDTO.getTaskId(), mapR.getData());
        approvalService.save(BeanUtil.copyProperties(approvalSaveDTO, Approval.class));
    }

    protected R<Map<String, Object>> handlerApproval(Long businessId){
        return null;
    }
}
