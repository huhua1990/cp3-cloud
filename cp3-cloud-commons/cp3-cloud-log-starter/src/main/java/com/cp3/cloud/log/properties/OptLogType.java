package com.cp3.cloud.log.properties;

/**
 * 日志类型
 *
 * @author cp3
 * @date 2020年03月09日15:00:04
 */
public enum OptLogType {
    /**
     * 通过logger记录日志到本地
     */
    LOGGER,
    /**
     * 记录日志到数据库
     */
    DB,
    ;
}
