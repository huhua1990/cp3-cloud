package com.cp3.cloud.activiti.controller.flow.listener;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;

/**
 * @Description 自定义执行监听器
 * @Auther: huhua
 * @Date: 2021/1/21
 */
@Slf4j
public class MyExecutionListener implements ExecutionListener{


    @Override
    public void notify(DelegateExecution delegateExecution) throws Exception {
        log.info("============executionListener start============");
        String eventName = delegateExecution.getEventName();
        String currentActivitiId = delegateExecution.getCurrentActivityId();
        log.info("事件名称:{}", eventName);
        log.info("ActivitiId:{}", currentActivitiId);
        log.info("============executionListener  end============");
    }
}
