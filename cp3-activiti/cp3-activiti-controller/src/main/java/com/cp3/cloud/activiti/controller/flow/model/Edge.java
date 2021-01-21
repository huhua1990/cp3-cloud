package com.cp3.cloud.activiti.controller.flow.model;

import lombok.Data;

/**
 * @Description 连线
 * @Auther cp3
 * @Date 2021/1/11
 */
@Data
public class Edge extends GraphElement {
    /**
     * 起点.
     */
    private Node src;

    /**
     * 终点.
     */
    private Node dest;

    /**
     * 循环.
     */
    private boolean cycle;
}
