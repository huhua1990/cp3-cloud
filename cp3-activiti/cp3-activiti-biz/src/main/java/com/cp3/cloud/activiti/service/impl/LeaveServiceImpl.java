package com.cp3.cloud.activiti.service.impl;


import com.cp3.cloud.activiti.dao.LeaveMapper;
import com.cp3.cloud.activiti.entity.Leave;
import com.cp3.cloud.activiti.service.LeaveService;
import com.cp3.base.basic.service.SuperServiceImpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
}
