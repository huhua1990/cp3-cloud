package com.cp3.cloud.activiti.controller.flow.listener;


import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;

/**
 * @Description
 * @Auther: huhua
 * @Date: 2021/1/21
 */
@Slf4j
public class MyActivitiEventListener implements ActivitiEventListener {

    //当时间被执行时调用该方法
    @Override
    public void onEvent(ActivitiEvent activitiEvent) {
        log.info("============ActivitiEventListener start============");
        log.info("事件类型:{}", activitiEvent.getType());
        log.info("ExecutionId:{}", activitiEvent.getExecutionId());
        log.info("ProcessInstanceId:{}", activitiEvent.getProcessInstanceId());
        log.info("============ActivitiEventListener  end============");
    }

    //这个是onEvent执行失败之后进行的处理
    // 如果是true则返回onEvent执行失败的异常
    // 如果是false则忽略这个异常
    @Override
    public boolean isFailOnException() {
        return false;
    }
}
