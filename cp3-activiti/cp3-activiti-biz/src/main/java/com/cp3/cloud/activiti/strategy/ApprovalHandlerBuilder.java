package com.cp3.cloud.activiti.strategy;

import com.cp3.base.exception.BizException;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 *   ApprovalHandlerBuilder初始化
 * </p>
 * @Auther cp3
 * @Date 2021/1/26
 */
@Component
public class ApprovalHandlerBuilder {

    private final Map<String, ApprovalHandlerStrategy> starategyPool = new ConcurrentHashMap<>();

    public ApprovalHandlerBuilder(Map<String, ApprovalHandlerStrategy> starategyPool) {
        starategyPool.forEach(this.starategyPool::put);
    }

    /**
     * ApprovalHandlerStrategy
     *
     * @param businessType 业务类型
     * @return ApprovalHandlerStrategy
     */
    public ApprovalHandlerStrategy getStrategy(String businessType) {
        ApprovalHandlerStrategy approvalHandlerStrategy = starategyPool.get(businessType);
        if (approvalHandlerStrategy == null) {
            throw new BizException("businessType 不支持，请传递正确的 businessType 参数");
        }
        return approvalHandlerStrategy;
    }

}
