package com.cp3.cloud.flowable.controller.listen;

import org.flowable.engine.delegate.TaskListener;
import org.flowable.task.service.delegate.DelegateTask;

/**
 * <p>
 * ManagerTaskHandler
 * </p>
 * @Auther cp3
 * @Date 2021/1/28
 */
public class ManagerTaskHandler implements TaskListener {
    @Override
    public void notify(DelegateTask delegateTask) {
        delegateTask.setAssignee("经理");
    }
}
