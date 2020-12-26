package com.cp3.base.validator.mateconstraint;

import com.cp3.base.validator.model.ConstraintInfo;

import java.lang.annotation.Annotation;


/**
 * 约束转换器
 *
 * @author zuihou
 * @date 2019-07-14 12:13
 */
public interface IConstraintConverter {

    /**
     * 支持的类型
     *
     * @param clazz 类型
     * @return 是否支持
     */
    boolean support(Class<? extends Annotation> clazz);

    /**
     * 转换
     *
     * @param ano 注解
     * @return 约束信息
     * @throws Exception 异常信息
     */
    ConstraintInfo converter(Annotation ano) throws Exception;
}
