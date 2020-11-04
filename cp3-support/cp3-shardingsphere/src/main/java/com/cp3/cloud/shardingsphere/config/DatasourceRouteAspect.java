package com.cp3.cloud.shardingsphere.config;

import io.shardingsphere.api.HintManager;
import io.shardingsphere.core.hint.HintManagerHolder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @Description
 * @Auther: hh
 * @Date: 2020/9/25 15:07
 * @Version:1.0
 */
@Component
@Aspect
@Slf4j
public class DatasourceRouteAspect {
    /**
     * DAO层切面
     */
    //@Pointcut("execution( * com.xxx.xxx.xxx..*.dao.*Mapper.*(..))")
    @Pointcut("execution( * com.cp3.cloud.shardingsphere.*.*Mapper.*(..))")
    public void pointCut() {
    }

    @Before("pointCut()")
    public void before(JoinPoint joinPoint) {
        HintManagerHolder.clear();
        HintManager hintManager = HintManager.getInstance();

        String code = RequestShardValueHolder.get();
        log.info("aspect:current request thread id:{}, code:{}", Thread.currentThread().getId(), code);
        hintManager.setDatabaseShardingValue(code);
        //指定表方式
        //hintManager.addDatabaseShardingValue("ds", 1);
        //hintManager.addTableShardingValue("user_demo",1);
    }

    /**
     * 完成之后清理shardvalue
     *
     * @param joinPoint
     */
    @After("pointCut()")
    public void after(JoinPoint joinPoint) {
        HintManagerHolder.clear();
    }

}
