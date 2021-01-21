package com.cp3.cloud.activiti.controller.flow.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description 节点
 * @Auther cp3
 * @Date 2021/1/11
 */
@Data
public class Node extends GraphElement {
    /**
     * 类型，比如userTask，startEvent.
     */
    private String type;

    /**
     * 是否还未完成.
     */
    private boolean active;

    /**
     * 进入这个节点的所有连线.
     */
    private List<Edge> incomingEdges = new ArrayList<Edge>();

    /**
     * 外出这个节点的所有连线.
     */
    private List<Edge> outgoingEdges = new ArrayList<Edge>();
}
