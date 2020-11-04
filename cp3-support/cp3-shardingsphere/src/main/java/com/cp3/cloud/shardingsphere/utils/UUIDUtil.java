package com.cp3.cloud.shardingsphere.utils;

import cn.hutool.core.util.IdUtil;

/**
 * @Description
 * @Auther: hh
 * @Date: 2020/9/18 19:36
 * @Version:1.0
 */
public class UUIDUtil {

    /**
     * 带-的UUID字符串
     * @return
     */
    public static String randomUUID(){
        return IdUtil.randomUUID();
    }
    /**
     * 不带-的UUID字符串，32位
     * @return
     */
    public static String simpleUUID(){
        return IdUtil.simpleUUID();
    }
}
