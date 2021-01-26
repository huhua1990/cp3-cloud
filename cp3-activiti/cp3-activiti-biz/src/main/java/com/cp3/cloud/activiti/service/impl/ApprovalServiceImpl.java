package com.cp3.cloud.activiti.service.impl;


import cn.hutool.core.bean.BeanUtil;
import com.cp3.cloud.activiti.dao.ApprovalMapper;
import com.cp3.cloud.activiti.dto.ApprovalSaveDTO;
import com.cp3.cloud.activiti.entity.Approval;
import com.cp3.cloud.activiti.service.ApprovalService;
import com.cp3.base.basic.service.SuperServiceImpl;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 业务实现类
 * 审批
 * </p>
 *
 * @author cp3
 * @date 2021-01-11
 */
@Slf4j
@Service
public class ApprovalServiceImpl extends SuperServiceImpl<ApprovalMapper, Approval> implements ApprovalService {

}
