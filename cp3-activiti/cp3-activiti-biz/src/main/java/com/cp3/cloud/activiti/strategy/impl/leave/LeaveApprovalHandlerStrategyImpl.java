package com.cp3.cloud.activiti.strategy.impl.leave;

import com.cp3.base.basic.R;
import com.cp3.base.utils.DateUtils;
import com.cp3.cloud.activiti.entity.Leave;
import com.cp3.cloud.activiti.service.LeaveService;
import com.cp3.cloud.activiti.strategy.impl.AbstractApprovalHandlerStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description
 * @Auther: huhua
 * @Date: 2021/1/26
 */
@Component("Leave")
public class LeaveApprovalHandlerStrategyImpl extends AbstractApprovalHandlerStrategy {

    @Autowired
    LeaveService leaveService;

    @Override
    protected R<Map<String, Object>> handlerApproval(Long businessId) {
        Leave leave = leaveService.getById(businessId);
        Map<String, Object> map = new HashMap<>();
        map.put("leaveType",leave.getLeaveType());
        map.put("day", DateUtils.getBetweenDay(leave.getStartTime().toLocalDate(), leave.getEndTime().toLocalDate()));
        return R.success(map);
    }
}
