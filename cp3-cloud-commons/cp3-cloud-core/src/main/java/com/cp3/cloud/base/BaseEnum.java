package com.cp3.cloud.base;

import com.baomidou.mybatisplus.annotation.IEnum;
import com.cp3.cloud.utils.MapHelper;

import java.util.Arrays;
import java.util.Map;

/**
 * 枚举类型基类
 *
 * @author cp3
 * @date 2019/07/26
 */
public interface BaseEnum extends IEnum<String> {
    /**
     * 将制定的枚举集合转成 map
     * key -> name
     * value -> desc
     *
     * @param list
     * @return
     */
    static Map<String, String> getMap(BaseEnum[] list) {
        return MapHelper.uniqueIndex(Arrays.asList(list), BaseEnum::getCode, BaseEnum::getDesc);
    }

    /**
     * 编码重写
     *
     * @return
     */
    default String getCode() {
        return toString();
    }

    /**
     * 描述
     *
     * @return
     */
    String getDesc();

    /**
     * 判断val是否跟当前枚举相等
     *
     * @param val
     * @return
     */
    default boolean eq(String val) {
        return this.getCode().equalsIgnoreCase(val);
    }

    /**
     * 枚举值
     *
     * @return
     */
    @Override
    default String getValue() {
        return getCode();
    }
}
