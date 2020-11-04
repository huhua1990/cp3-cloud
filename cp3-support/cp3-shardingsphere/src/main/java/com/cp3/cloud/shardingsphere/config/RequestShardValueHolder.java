package com.cp3.cloud.shardingsphere.config;

/**
 * @Description 分库路由值暂存器，提供线程封闭环境
 * @Auther: hh
 * @Date: 2020/9/25 15:08
 * @Version:1.0
 */
public class RequestShardValueHolder {
    /**
     * 当前目标数据源键值
     */
    private static final ThreadLocal<String> CURRENT_SHARD_VALUE = new ThreadLocal<>();

    /**
     * 插入目标数据源键值
     *
     * @param code 键值
     */
    public static void set(String code) {
        CURRENT_SHARD_VALUE.set(code);
    }

    /**
     * 获取目标数据源键值
     *
     * @return 目标数据源键值
     */
    public static String get() {
        CURRENT_SHARD_VALUE.set("0");
        return CURRENT_SHARD_VALUE.get();
    }

    public static void remove() {
        CURRENT_SHARD_VALUE.remove();
    }

}
