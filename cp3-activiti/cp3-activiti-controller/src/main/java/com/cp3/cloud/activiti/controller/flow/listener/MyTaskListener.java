package com.cp3.cloud.activiti.controller.flow.listener;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

/**
 * @Description
 * @Auther: huhua
 * @Date: 2021/1/21
 */
@Slf4j
public class MyTaskListener implements TaskListener {
    @Override
    public void notify(DelegateTask delegateTask) {
        log.info("============TaskListener start============");
        String taskDefinitionKey = delegateTask.getTaskDefinitionKey();
        String eventName = delegateTask.getEventName();
        log.info("事件名称:{}", eventName);
        log.info("taskDefinitionKey:{}", taskDefinitionKey);
        log.info("============TaskListener end============");
    }
}
