package com.cp3.cloud.log.event;


import cn.hutool.core.util.StrUtil;
import com.cp3.cloud.context.BaseContextHandler;
import com.cp3.cloud.log.entity.OptLogDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;

import java.util.function.Consumer;


/**
 * 异步监听日志事件
 *
 * @author cp3
 * @date 2019-07-01 15:13
 */
@Slf4j
@AllArgsConstructor
public class SysLogListener {

    private Consumer<OptLogDTO> consumer;

    @Async
    @Order
    @EventListener(SysLogEvent.class)
    public void saveSysLog(SysLogEvent event) {
        OptLogDTO sysLog = (OptLogDTO) event.getSource();
        if (sysLog == null || StrUtil.isEmpty(sysLog.getTenantCode())) {
            log.warn("租户编码不存在，忽略操作日志=={}", sysLog != null ? sysLog.getRequestUri() : "");
            return;
        }
        BaseContextHandler.setTenant(sysLog.getTenantCode());

        consumer.accept(sysLog);
    }

}
