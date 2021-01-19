package com.cp3.cloud.activiti.service;

import com.cp3.base.basic.service.SuperService;
import com.cp3.cloud.activiti.entity.Leave;

/**
 * <p>
 * 业务接口
 * 假期申请
 * </p>
 *
 * @author cp3
 * @date 2021-01-11
 */
public interface LeaveService extends SuperService<Leave> {
    void toApproval(String id);
    void toApproval2(String id);
}
