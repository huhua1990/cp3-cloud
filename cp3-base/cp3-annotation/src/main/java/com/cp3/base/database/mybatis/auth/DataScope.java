package com.cp3.base.database.mybatis.auth;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashMap;
import java.util.List;

/**
 * @author zuihou
 * @date 2019/2/1
 * 数据权限查询参数
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DataScope extends HashMap {
    /**
     * 限制范围的字段名称 （除个人外）
     */
    private String scopeName = "org_id";
    /**
     * 限制范围为个人时的字段名称
     */
    private String selfScopeName = "created_by";
    /**
     * 当前用户ID
     */
    private Long userId;

    /**
     * 具体的数据范围
     */
    private List<Long> orgIds;

}
