package com.cp3.cloud.injection.core;

import com.cp3.cloud.injection.annonation.InjectionField;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * 封装字段上标记了 InjectionField 注解的字段
 *
 * @author cp3
 * @date 2020/5/8 下午9:19
 */
@Data
@AllArgsConstructor
public class FieldParam {
    /**
     * 当前字段上的注解
     */
    private InjectionField injection;
    /**
     * 从当前字段的值构造出的调用api#method方法的参数
     */
    private Serializable queryKey;
    /**
     * 当前字段的具体值
     */
    private Object fieldValue;
}
