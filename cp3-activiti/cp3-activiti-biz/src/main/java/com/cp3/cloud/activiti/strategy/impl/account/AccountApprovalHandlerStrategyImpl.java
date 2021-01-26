package com.cp3.cloud.activiti.strategy.impl.account;

import com.cp3.base.basic.R;
import com.cp3.cloud.activiti.strategy.impl.AbstractApprovalHandlerStrategy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description
 * @Auther: huhua
 * @Date: 2021/1/26
 */
@Component("Account")
public class AccountApprovalHandlerStrategyImpl extends AbstractApprovalHandlerStrategy {
    @Override
    protected R<Map<String, Object>> handlerApproval(Long businessId) {
        //test
        Map<String, Object> map= new HashMap<>();
        map.put("money", 9999);
        map.put("type", "JT");
        return R.success(map);
    }
}
