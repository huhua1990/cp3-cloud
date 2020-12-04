package com.cp3.cloud.gateway.filter;

import cn.hutool.core.util.IdUtil;
import com.cp3.cloud.context.BaseContextConstants;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 生成日志链路追踪id，并传入header中
 *
 * @author cp3
 * @date 2020年03月09日18:02:47
 */
@Slf4j
@Component
public class TraceFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //链路追踪id
        String traceId = IdUtil.fastSimpleUUID();
        MDC.put(BaseContextConstants.LOG_TRACE_ID, traceId);
        ServerHttpRequest serverHttpRequest = exchange.getRequest().mutate()
                .headers(h -> h.add(BaseContextConstants.TRACE_ID_HEADER, traceId))
                .build();
        log.info("TraceFilter traceId:{}",traceId);
        ServerWebExchange build = exchange.mutate().request(serverHttpRequest).build();
        return chain.filter(build);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
