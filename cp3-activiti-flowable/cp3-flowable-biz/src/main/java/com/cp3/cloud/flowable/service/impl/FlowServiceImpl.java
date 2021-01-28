package com.cp3.cloud.flowable.service.impl;


import com.cp3.cloud.flowable.dao.FlowMapper;
import com.cp3.cloud.flowable.entity.Flow;
import com.cp3.cloud.flowable.service.FlowService;
import com.cp3.base.basic.service.SuperServiceImpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 业务实现类
 * 流程
 * </p>
 *
 * @author cp3
 * @date 2021-01-25
 */
@Slf4j
@Service
public class FlowServiceImpl extends SuperServiceImpl<FlowMapper, Flow> implements FlowService {
}
