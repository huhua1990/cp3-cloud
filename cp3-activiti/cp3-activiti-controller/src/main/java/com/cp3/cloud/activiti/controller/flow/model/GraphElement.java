package com.cp3.cloud.activiti.controller.flow.model;

import lombok.Data;

/**
 * @Description 节点和连线的父类
 * @Auther cp3
 * @Date 2021/1/11
 */
@Data
public class GraphElement {
    /**
     * 实例id，历史的id.
     */
    private String id;

    /**
     * 节点名称，bpmn图形中的id.
     */
    private String name;
}
