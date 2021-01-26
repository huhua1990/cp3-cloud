package com.cp3.cloud.activiti.strategy;

import com.cp3.cloud.activiti.dto.ApprovalSaveDTO;

import java.util.Map;

/**
 * @Description 策略模式
 * @Auther: huhua
 * @Date: 2021/1/26
 */
public interface ApprovalHandlerStrategy {
    void approval(ApprovalSaveDTO approvalSaveDTO);
}
